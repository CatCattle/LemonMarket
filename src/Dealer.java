import java.util.Arrays;
import java.util.Random;

public class Dealer {
    private static int dealerCount = 0;
    private int dealerID;
    private Car[] inventory;
    private int balance;
    private int netProfit;
    private int inventoryCount;

    public Dealer() {
        this.dealerID = ++dealerCount;
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
        addCarToInventory(car);
        car.setOwner(Car.Owner.DEALER);
        balance -= car.getCurrentValue();
        netProfit -= car.getCurrentValue();
    }

    public void sellCar(Car car) {
        removeCarFromInventory(car);
        balance += car.getCurrentValue();
        netProfit += car.getCurrentValue();
    }

    public int bid(Car car) {
        // Implement the bidding logic here
        Random random = new Random();
        int randomInt = random.nextInt(30000) + 10000;
        return randomInt; // Replace with actual bid value
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(int netProfit) {
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

    @Override
    public String toString() {
        return String.format("Dealer{balance=%d, netProfit=%d, inventoryCount=%d}", balance, netProfit, inventoryCount);
    }

    public int getDealerID() {
        return this.dealerID;
    }

}