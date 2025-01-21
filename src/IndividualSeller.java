import java.util.Random;

public class IndividualSeller {
    private static int lastSellerID = 0; // 静态变量，用于生成唯一的卖家ID
    private int sellerID;
    private int supplyPrice;         // 出售价格
    private boolean soldCar;         // 是否已卖出车
    private String sellCarID;        // 车辆ID，用于访问数据库中的 cars 表

    // 构造函数
    public IndividualSeller() {
        this.sellerID = ++lastSellerID; // 自增ID，确保每个卖家ID唯一
        this.supplyPrice = 0;          // 初始化供给价格为 0
        this.soldCar = false;          // 初始化为未出售状态
        this.sellCarID = "";           // 初始化车辆ID为空
    }

    // 获取卖家ID
    public int getSellerID() {
        return sellerID;
    }

    // 检查卖家是否卖出了车
    public boolean getSoldCar() {
        return soldCar;
    }

    // 设置卖家卖出车的状态
    public void setSoldCar(boolean soldCar) {
        this.soldCar = soldCar;
    }

    // 获取供给价格
    public int getSupplyPrice() {
        return supplyPrice;
    }

    // 设置供给价格
    public void setSupplyPrice() {
        // 假设 Car 类有一个静态方法 getCarByID，根据 carID 获取 Car 对象
        Car car = Car.getCarByID(this.sellCarID); // 根据车辆ID获取车辆对象
        if (car != null) {
            int currentValue = car.getCurrentValue();   // 获取当前价值
            int rounds = car.getRound();              // 获取车辆已使用轮数
            double depreciationFactor = car.getDepreciationFactor(); // 获取折旧系数
            // 计算供给价格
            this.supplyPrice = (int) (currentValue * Math.pow(rounds, 0.9) * (1 - depreciationFactor));
        } else {
            System.out.println("车辆供给为空，无法设置供给价格！");
        }
    }

    // 设置车辆ID
    public void setSellCarID(String carID) {
        this.sellCarID = carID;
    }

    // 获取车辆ID
    public String getSellCarID() {
        return sellCarID;
    }

    // 更新车辆供给
    public void updateCarSupply() {
        if (!this.sellCarID.isEmpty()) {
            setSupplyPrice(); // 更新供给价格
            System.out.println("车辆供给已更新: " + this.toString());
        } else {
            System.out.println("车辆供给信息缺失，无法更新！");
        }
    }

    // 设置车辆供给
    public void setCarSupply(Car newCar) {
        if (newCar != null) {
            this.sellCarID = newCar.getCarID(); // 设置车辆ID
            setSupplyPrice();                  // 更新供给价格
            System.out.println("车辆供给已设置: " + this.toString());
        } else {
            System.out.println("新车辆为空，无法设置供给！");
        }
    }

    // 出价逻辑
    public int bid(Car car) {
        Random random = new Random();
        if (car != null) {
            // 假设出价逻辑基于车辆的当前价值，并加入一定的随机浮动
            int basePrice = car.getCurrentValue();
            int randomOffset = random.nextInt(20000) + 10000; // 随机波动范围 [10000, 30000)
            int finalBid = basePrice + randomOffset;
            System.out.println("对车辆ID为 " + car.getCarID() + " 的出价为: " + finalBid);
            return finalBid;
        }
        System.out.println("无效的车辆信息，无法出价！");
        return 0; // 出价失败返回0
    }

    @Override
    public String toString() {
        return String.format("IndividualSeller{sellerID=%d, supplyPrice=%d, sellCarID='%s', soldCar=%b}",
                sellerID, supplyPrice, sellCarID, soldCar);
    }
}
