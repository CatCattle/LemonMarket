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

    public MarketManager() {
        individualSellers = new ArrayList<>();
        individualBuyers = new ArrayList<>();
        dealers = new ArrayList<>();
        cars = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
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

    public void generateSellerSupply(IndividualSeller seller) {
        if (seller.getSoldCar()) {
            Car newCar = new Car();
            cars.add(newCar);
            seller.setCarSupply(newCar);
        }
    }

    public void generateBuyerDemand(IndividualBuyer buyer) {
        if (buyer.getBoughtCar()) {
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

    public void startRound() {
        System.out.println("Starting a new round...");

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
                cars.remove(car);
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
                    dealer.sellCar(car, buyer);
                    buyer.updateDemand();
                    cars.remove(car);
                }
            }
        }
        for (Car car : cars) {
            car.reduceDepreciationFactor();
        }

        System.out.println("Round completed.");

        // 保存买家信息到数据库
        try (Connection conn = connectToDatabase()) {
            for (IndividualBuyer buyer : individualBuyers) {
                saveIndividualBuyerToDatabase(buyer, conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String sql = "INSERT INTO buyers (BuyerID, DemandPrice, BoughtCar, TargetLevel) VALUES (?, ?, ?, ?)";
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
        String sql = "INSERT INTO sellers (SellerID, SellPrice, SoldCar, SellCarID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seller.getSellerID());
            stmt.setInt(2, seller.getSupplyPrice());
            stmt.setBoolean(3, seller.getSoldCar());
            stmt.setString(4, String.valueOf(seller.getSellCarID()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}