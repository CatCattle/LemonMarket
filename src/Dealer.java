import java.util.Arrays;

public class Dealer {
    private Car[] inventory;
    private long balance;
    private long netProfit;
    private int inventoryCount;

    public Dealer() {
        this.inventory = new Car[10];
        this.balance = 500000;
        this.netProfit = 0;
        this.inventoryCount = 0;
    }

    private void updateInventoryCount() {
        this.inventoryCount = (int) Arrays.stream(inventory).filter(car -> car != null).count();
    }

    public void buyCar(Car car) {
        // Implementation for buying a car
    }

    public double bid(Car car) {
        // Implement the bidding logic here
        return 0; // Replace with actual bid value
    }

    public void priceCarsForSale() {
        // Implement the logic for pricing cars for sale
    }

    public boolean addCarToInventory(Car car) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = car;
                updateInventoryCount();
                return true;
            }
        }
        System.out.println("库存已满，无法添加新车！");
        return false;
    }

    public boolean removeCarFromInventory(Car car) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == car) {
                inventory[i] = null;
                updateInventoryCount();
                return true;
            }
        }
        System.out.println("库存中未找到该车！");
        return false;
    }

    public Car[] getInventory() {
        return inventory;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(long netProfit) {
        this.netProfit = netProfit;
    }

    public int getInventoryCount() {
        updateInventoryCount();
        return inventoryCount;
    }

    public Car getCarForSale() {
        for (Car car : inventory) {
            if (car != null) {
                return car;
            }
        }
        return null; // No car available for sale
    }

    public void sellCar(Car car, IndividualBuyer buyer) {
    }

    @Override
    public String toString() {
        return String.format("Dealer{balance=%d, netProfit=%d, inventoryCount=%d}", balance, netProfit, inventoryCount);
    }

}