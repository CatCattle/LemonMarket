import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MarketManager {
    private List<IndividualSeller> individualSellers;
    private List<IndividualBuyer> individualBuyers;
    private List<Dealer> dealers;
    private List<Car> cars;
    private ScheduledExecutorService scheduler;
    private int round;

    public MarketManager() {
        individualSellers = new ArrayList<>();
        individualBuyers = new ArrayList<>();
        dealers = new ArrayList<>();
        cars = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
        round = 0;
    }

    public void addIndividualBuyer(IndividualBuyer buyer) {
        individualBuyers.add(buyer);
    }

    public void addIndividualSeller(IndividualSeller seller) {
        individualSellers.add(seller);
    }

    public void addDealer(Dealer dealer) {
        dealers.add(dealer);
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getCars() {
        return cars;
    }

    public void startMarket() {
        scheduler.scheduleAtFixedRate(this::startRound, 0, 1, TimeUnit.MINUTES);
    }

    public void stopMarket() {
        scheduler.shutdown();
    }

    private void nextRound() {
        this.round++;
        for (Car car : cars) {
            car.nextRound();
        }
    }

    public void generateSellerSupply(IndividualSeller seller) {
        if (seller.getSoldCar() || round == 1) {
            Car newCar = new Car();
            addCar(newCar);
            try (Connection conn = connectToDatabase()) {
                    saveCarToDatabase(newCar, conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            seller.setCarSupply(newCar);
        }
    }

    public void generateBuyerDemand(IndividualBuyer buyer) {
        if (buyer.getBoughtCar() || round == 1) {
            char[] levels = {'A', 'B', 'C'};
            int[] prices = {100000, 200000, 400000};
            Random random = new Random();

            // 随机生成目标车辆等级
            int index = random.nextInt(levels.length);
            char targetLevel = levels[index];

            // 随机生成目标车辆价格
            index = random.nextInt(prices.length);
            int demandPrice = prices[index];

            // 随机生成期望折旧系数
            double deprecationFactor = random.nextDouble(0.5, 1);

            buyer.setCarDemand(targetLevel, demandPrice, deprecationFactor);
        }
    }

    public void printCars() {
        System.out.println("Current cars in the market:");
        if (cars.isEmpty()) {
            System.out.println("No cars available.");
        } else {
            for (Car car : cars) {
                System.out.println(car); // 假设 `Car` 类已实现 `toString` 方法
            }
        }
    }

    public double calculateLemonLevel() {
        if (cars.isEmpty()) {
            System.out.println("No cars available.");
        } else {
            int lemon = 0;
            int peach = 0;
            for (Car car : cars) {
                if (car.isLemon()) {
                    lemon++;
                } else {
                    peach++;
                }
            }
            return (double) lemon / (lemon + peach);
        }
        return -1;
    }

    public void startRound() {
        System.out.println("Starting a new round...");
        nextRound();

        // 上一轮卖出车的个人卖家生成新的车辆供给
        for (IndividualSeller seller : individualSellers) {
            generateSellerSupply(seller);
        }

        // 上一轮买到车的个人买家生成新的车辆需求
        for (IndividualBuyer buyer : individualBuyers) {
            generateBuyerDemand(buyer);
        }

        // 所有车贬值
        for (Car car : cars) {
            car.depreciate();
        }

        // 记录该轮初始信息
        System.out.println("回合初柠檬度为：" + calculateLemonLevel());
        // 经销商竞拍阶段
        for (Car car : cars) {
            Dealer highestBidder = null;
            double highestBid = 0;
            for (Dealer dealer : dealers) {
                double bid = dealer.bid(car);
                if (bid > highestBid) {
                    highestBid = bid;
                    highestBidder = dealer;
                }
            }
            if (highestBidder != null) {
                highestBidder.buyCar(car);
                System.out.println("经销商 " + highestBidder.getDealerID() + " 买车啦！");
            }
        }

        // 经销商定价出售
        for (Dealer dealer : dealers) {
            dealer.priceCarsForSale();
        }

        // 个人买家竞拍阶段
        for (IndividualBuyer buyer : individualBuyers) {
            for (Dealer dealer : dealers) {
                Car car = dealer.getCarForSale();
                if (car != null && buyer.bid(car)) {
                    dealer.sellCar(car);
                    buyer.updateDemand();
                    cars.remove(car);
                    System.out.println("经销商 " + dealer.getDealerID() + " 卖车啦！");
                }
            }
        }

        printCars();

        for (Car car : cars) {
            car.reduceDepreciationFactor();
        }

        System.out.println("Round completed.");

        try (Connection conn = connectToDatabase()) {
            for (IndividualBuyer buyer : individualBuyers) {
                saveIndividualBuyerToDatabase(buyer, conn);
            }

            for (IndividualSeller seller : individualSellers) {
                saveIndividualSellerToDatabase(seller, conn);
            }

            for (Dealer dealer : dealers) {
                saveDealerToDatabase(dealer, conn);
                Car[] Inventory = dealer.getInventory();
                System.out.println(Inventory);
                int dealerId = dealer.getDealerID();
                if (Inventory.length > 0) {
                    for (Car car : Inventory) {
                        if (car != null) {
                            saveInventoryToDatabase(car.getCarID(), dealerId, conn);
                        }
                    }
                }
            }

            for (Car car : cars) {
                saveCarToDatabase(car, conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("回合末柠檬度为：" + calculateLemonLevel());

    }

    private Connection connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/lemon_market";
        String user = "root";
        String password = "15818530035";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveIndividualBuyerToDatabase(IndividualBuyer buyer, Connection conn) {
        String sql = "INSERT INTO buyers (BuyerID, DemandPrice, BoughtCar, TargetLevel) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "DemandPrice = VALUES(DemandPrice), " +
                "BoughtCar = VALUES(BoughtCar), " +
                "TargetLevel = VALUES(TargetLevel)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buyer.getBuyerID());
            stmt.setInt(2, buyer.getDemandPrice());
            stmt.setBoolean(3, buyer.getBoughtCar());
            stmt.setString(4, String.valueOf(buyer.getTargetLevel()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveIndividualSellerToDatabase(IndividualSeller seller, Connection conn) {
        String sql = "INSERT INTO sellers (SellerID, SellPrice, SoldCar, SellCarID) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "SellPrice = VALUES(SellPrice), " +
                "SoldCar = VALUES(SoldCar), " +
                "SellCarID = VALUES(SellCarID)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seller.getSellerID());
            stmt.setInt(2, seller.getSupplyPrice());
            stmt.setBoolean(3, seller.getSoldCar());
            stmt.setString(4, seller.getSellCarID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveDealerToDatabase(Dealer dealer, Connection conn) {
        String sql = "INSERT INTO dealers (DealerID, Balance, NetProfit, InventoryCount) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "Balance = VALUES(Balance), " +
                "NetProfit = VALUES(NetProfit), " +
                "InventoryCount = VALUES(InventoryCount)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dealer.getDealerID());
            stmt.setInt(2, dealer.getBalance());
            stmt.setInt(3, dealer.getNetProfit());
            stmt.setInt(4, dealer.getInventoryCount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void saveCarToDatabase(Car car, Connection conn) {
        // Updated SQL to include the Owner field
        String sql = "INSERT INTO cars (CarID, Level, OriginalPrice, Lemon, DF, CurrentValue, Round, Price, Owner) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "Level = VALUES(Level), " +
                "OriginalPrice = VALUES(OriginalPrice), " +
                "Lemon = VALUES(Lemon), " +
                "DF = VALUES(DF), " +
                "CurrentValue = VALUES(CurrentValue), " +
                "Round = VALUES(Round), " +
                "Price = VALUES(Price), " +
                "Owner = VALUES(Owner)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set parameters for the PreparedStatement
            stmt.setString(1, car.getCarID());                  // CarID
            stmt.setString(2, car.getLevel());                  // Level
            stmt.setInt(3, car.getOriginalPrice());             // OriginalPrice
            stmt.setBoolean(4, car.isLemon());                  // Lemon
            stmt.setDouble(5, car.getDepreciationFactor());     // Depreciation Factor (DF)
            stmt.setInt(6, car.getCurrentValue());              // CurrentValue
            stmt.setInt(7, car.getRound());                     // Round
            stmt.setInt(8, car.getPrice());                     // Price
            stmt.setString(9, car.getOwner().name());          // Owner (convert enum to string)

            // Execute the SQL statement
            stmt.executeUpdate();
            System.out.println("Car successfully saved or updated in the database: " + car.getCarID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void saveInventoryToDatabase(String carID, int dealerID, Connection conn) {
        String sql = "INSERT INTO inventory (CarID, DealerID) " +
                "VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "DealerID = VALUES(DealerID)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carID);
            stmt.setInt(2, dealerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}