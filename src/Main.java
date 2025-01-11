import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueLoop = true;

        // 初始化市场管理器
        MarketManager marketManager = new MarketManager();

        // 输入人数
        System.out.println("请输入玩家数量：");
        int playerCount = scanner.nextInt();
        scanner.nextLine();

        // 确定经销商数量
        for (int i = 0; i < playerCount; i++) {
            Dealer dealer = new Dealer();
            marketManager.addDealer(dealer);
        }

        // 虚拟个人卖家
        int virtualSellerCount = Math.min(playerCount * 3, 12);
        for (int i = 0; i < virtualSellerCount; i++) {
            IndividualSeller seller = new IndividualSeller();
            marketManager.addIndividualSeller(seller);
        }

        // 虚拟个人买家
        int virtualBuyerCount = Math.min(playerCount * 3, 12);
        for (int i = 0; i < virtualBuyerCount; i++) {
            IndividualBuyer buyer = new IndividualBuyer();
            marketManager.addIndividualBuyer(buyer);
        }

        // 开始市场
//        marketManager.startMarket();

        while (continueLoop) {
            // ... existing code for one round ...
            marketManager.startRound();
            System.out.println("Do you want to play another round? (y/N)");
            String userResponse = scanner.nextLine();

            if (!userResponse.equalsIgnoreCase("y")) {
                continueLoop = false;
            }
        }

//        System.out.println("Thank you for playing!");
        scanner.close();
    }
}