public class IndividualSeller {
    private static int lastSellerID = 0;
    private int sellerID;
    private int supplyPrice;         // 出售价格
    private boolean soldCar;
    private String sellCarID;        // 车辆ID，用于访问数据库中的cars表

    public IndividualSeller() {
        this.sellerID = ++lastSellerID;
        this.supplyPrice = 0;          // 初始化供给价格为0
        this.soldCar = false;
        this.sellCarID = "";
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
        Car car = Car.getCarByID(this.sellCarID);
        if (car != null) {
            int currentValue = car.getCurrentValue();
            int rounds = car.getRounds();
            this.supplyPrice = (int) (currentValue * Math.pow(rounds, 0.9) * (1 - car.getDepreciationFactor()));
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
        // 更新供给逻辑
    }

    @Override
    public String toString() {
        return String.format("IndividualSeller{sellerID=%d, supplyPrice=%d, sellCarID=%s}",
                sellerID, supplyPrice, sellCarID);
    }

    // 出价逻辑
    public boolean bid(Car car) {
        // 出价逻辑
        return true;
    }

    public void setCarSupply(Car newCar) {

    }
}