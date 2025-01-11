public class Controller {
    private MarketManager marketManager;

    public Controller(MarketManager marketManager) {
        this.marketManager = marketManager;
    }

    public void runIterations(int iterations) {
        for (int i = 0; i < iterations; i++) {
            System.out.println("Starting iteration " + (i + 1));
            marketManager.startRound();
            printCarInfo();
            System.out.println("Iteration " + (i + 1) + " completed.\n");
        }
    }

    private void printCarInfo() {
        for (Car car : marketManager.getCars()) {
            System.out.println(car);
        }
    }
}