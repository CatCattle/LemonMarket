import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.sql.*;

public class Car {
    private static final Set<String> usedIds = new HashSet<>();
    private static final int[] PRICES = {100000, 200000, 400000};

    public enum Owner {
        DEALER, SELLER
    }
    private String id;
    private String level;
    private int originalPrice;
    private boolean lemon;
    private double depreciationFactor;
    private int currentValue;
    private int round;
    private int price;
    private Owner owner;

    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/lemon_market";
    private static final String USER = "root";
    private static final String PASSWORD = "15818530035";

    // 构造函数
    public Car() {
        this.id = generateUniqueId();
        this.level = setLevel();
        this.originalPrice = setOriginalPrice();
        this.lemon = setLemon();
        this.depreciationFactor = generateDepreciationFactor();
        this.currentValue = calculateCurrentValue();
        this.round = 0;
        this.price = 0;
        this.owner = Owner.SELLER;
    }

    public Car(Owner owner) {
        this();
        this.owner = owner;
    }

    public static Car getCarByID(String sellCarID) {
        String sql = "SELECT * FROM cars WHERE CarID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sellCarID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("CarID");
                    String level = rs.getString("Level");
                    int originalPrice = rs.getInt("OriginalPrice");
                    boolean lemon = rs.getBoolean("Lemon");
                    double depreciationFactor = rs.getDouble("DF");
                    int currentValue = rs.getInt("CurrentValue");
                    int round = rs.getInt("Round");
                    int price = rs.getInt("Price");


                    Car car = new Car();
                    car.id = id;
                    car.level = level;
                    car.originalPrice = originalPrice;
                    car.lemon = lemon;
                    car.depreciationFactor = depreciationFactor;
                    car.currentValue = currentValue;
                    car.round = round;
                    car.price = price;
                    return car;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 如果没有找到对应的 Car 对象，返回 null
    }

    // 生成唯一车辆编号
    private String generateUniqueId() {
        Random random = new Random();
        String id;
        do {
            id = "" + (char) (random.nextInt(26) + 'A') + (char) (random.nextInt(26) + 'A');
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    // 设置车辆级别
    private String setLevel() {
        String[] levels = {"A", "B", "C"};
        double[] probabilities = {0.5, 0.3, 0.2};
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < levels.length; i++) {
            cumulativeProbability += probabilities[i];
            if (p <= cumulativeProbability) {
                return levels[i];
            }
        }
        return "A"; // 默认返回A级
    }

    // 设置车辆原始价格
    private int setOriginalPrice() {
        switch (this.level) {
            case "A":
                return PRICES[0];
            case "B":
                return PRICES[1];
            case "C":
                return PRICES[2];
            default:
                return PRICES[0];
        }
    }

    // 设置柠檬车标记
    private boolean setLemon() {
        double lemonProbability = 0.1; // 生成柠檬车的概率为0.1
        return Math.random() < lemonProbability;
    }

    // 生成折旧系数，介于0.400和0.800之间，随机分布并保留三位小数
    private double generateDepreciationFactor() {
        Random random = new Random();
        double factor = 0.400 + (0.800 - 0.400) * random.nextDouble();
        return Math.round(factor * 1000.0) / 1000.0;
    }

    // 计算车辆当前价值
    private int calculateCurrentValue() {
        double value = this.originalPrice * this.depreciationFactor;
        if (this.lemon) {
            value *= 0.2;
        }
        return (int) value;
    }

    // 获取车辆当前价值
    public int getCurrentValue() {
        return this.currentValue;
    }

    // 获取轮次
    public int getRound() {
        return this.round;
    }

   // 车的贬值方法
   public void depreciate() {
        // 每轮贬值10%
        this.currentValue *= 0.9;
    }

    public String getCarID() {
        return this.id;
    }

    public double getDepreciationFactor() {
        return this.depreciationFactor;
    }

    public void reduceDepreciationFactor() {
    }

    @Override
    public String toString() {
        return String.format("Car{id='%s', level=%s, originalPrice=%d, lemon=%b, depreciationFactor=%.3f, currentValue=%d, round=%d, price=%d}",
                id, level, originalPrice, lemon, depreciationFactor, currentValue, round, price);
    }


    public String getLevel() {
        return this.level;
    }

    public int getOriginalPrice() {
        return this.originalPrice;
    }

    public boolean isLemon() {
        return this.lemon;
    }

    public int getPrice() {
        return this.price;
    }

    public void nextRound() {
        this.round++;
    }

    public Car.Owner getOwner() {
        return owner;
    }

    public void setOwner(Car.Owner owner) {
        this.owner = owner;
    }
}
