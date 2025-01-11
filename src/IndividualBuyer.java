public class IndividualBuyer {
    private static int lastBuyerID = 0;
    private int buyerID;
    private double z;
    private CarDemand carDemand; // Z属性，初始值为0.000
    private boolean boughtCar;

    public IndividualBuyer() {
        this.buyerID = ++lastBuyerID;
        this.z = 0.000;               // 初始化Z为0.000
        this.boughtCar = false;
        this.carDemand = new CarDemand(100000, 'C', 0.5);
    }

    public static class CarDemand {
        private int demandPrice;
        private char targetLevel;
        private double deprecationFactor;

        public CarDemand(int price, char level, double defactor) {
            this.demandPrice = price;
            this.targetLevel = level;
            this.deprecationFactor = defactor;
        }

        public int getDemandPrice() {
            return demandPrice;
        }

        public char getTargetLevel() {
            return targetLevel;
        }

        public double getDeprecationFactor() {
            return deprecationFactor;
        }

        public void setDemandPrice(int demandPrice) {
            this.demandPrice = demandPrice;
        }

        public void setTargetLevel(char targetLevel) {
            this.targetLevel = targetLevel;
        }

        public void setDeprecationFactor(double deprecationFactor) {
            this.deprecationFactor = deprecationFactor;
        }

        @Override
        public String toString() {
            return String.format("CarDemand{demandPrice=%d, targetLevel=%s, deprecationFactor=%.3f}",
                    demandPrice, targetLevel, deprecationFactor);
        }
    }

    // 获取个人买家ID
    public int getBuyerID() {
        return buyerID;
    }

    // 检查买家是否购买了车辆
    public boolean getBoughtCar() {
        return boughtCar;
    }

    // 设置买家购买车辆的状态
    public void setBoughtCar(boolean boughtCar) {
        this.boughtCar = boughtCar;
    }

    // 添加车辆需求
    public void setCarDemand(char targetLevel, int demandPrice, double deprecationFactor) {
        if (!getBoughtCar()) {
            this.carDemand.setTargetLevel(targetLevel);
            this.carDemand.setDemandPrice(demandPrice);
            this.carDemand.setDeprecationFactor(deprecationFactor);
            System.out.println("车辆需求已设置！");
        } else {
            System.out.println("车辆已购买，无法添加新车需求！");
        }
    }

    // 获取车辆需求
    public CarDemand getCarDemand() {
        return carDemand;
    }

    // 设置Z属性
    public void setZ(double z) {
        this.z = z;
    }

    // 获取Z属性
    public double getZ() {
        return z;
    }

    // 更新需求逻辑
    public void updateDemand() {
        // 更新需求逻辑
    }

    @Override
    public String toString() {
        return String.format("IndividualBuyer{buyerID=%d, z=%.3f, carDemand=%s, boughtCar=%b}",
                buyerID, z, carDemand.toString(), boughtCar);
    }

    // 出价逻辑
    public boolean bid(Car car) {
        // 出价逻辑
        return true;
    }
}