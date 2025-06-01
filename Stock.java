package codealpha;
import java.util.*;
public class Stock {
	String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public void updatePrice() {
        // Random price fluctuation
        double change = (Math.random() * 10) - 5;
        price = Math.max(1, price + change);
    }
}

class Transaction {
    String type;
    Stock stock;
    int quantity;
    double price;
    Date date;

    public Transaction(String type, Stock stock, int quantity, double price) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.date = new Date();
    }

    public String toString() {
        return date + " - " + type + " " + quantity + " of " + stock.symbol + " at $" + price;
    }
}

class User {
    String name;
    double balance;
    Map<String, Integer> portfolio;
    List<Transaction> history;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (balance >= cost) {
            balance -= cost;
            portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
            history.add(new Transaction("BUY", stock, quantity, stock.price));
            System.out.println("Bought " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (owned >= quantity) {
            balance += stock.price * quantity;
            portfolio.put(stock.symbol, owned - quantity);
            if (portfolio.get(stock.symbol) == 0) portfolio.remove(stock.symbol);
            history.add(new Transaction("SELL", stock, quantity, stock.price));
            System.out.println("Sold " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- Portfolio of " + name + " ---");
        double total = 0;
        for (String symbol : portfolio.keySet()) {
            int qty = portfolio.get(symbol);
            double price = market.get(symbol).price;
            System.out.println(symbol + ": " + qty + " shares @ $" + price + " each = $" + (qty * price));
            total += qty * price;
        }
        System.out.println("Cash Balance: $" + balance);
        System.out.println("Total Portfolio Value: $" + (balance + total));
    }

    public void showHistory() {
        System.out.println("\n--- Transaction History ---");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}

 class StockMarketSimulator {
    private static Map<String, Stock> market = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static User user;

    public static void main(String[] args) {
        setupMarket();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        user = new User(name, 10000);  // Starting balance

        boolean running = true;
        while (running) {
            updateMarketPrices();
            System.out.println("\n=== Stock Market Menu ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // clear buffer

            switch (choice) {
                case 1: viewMarket(); break;
                case 2: buyStock(); break;
                case 3: sellStock(); break;
                case 4: user.showPortfolio(market); break;
                case 5: user.showHistory(); break;
                case 6: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
        System.out.println("Exiting... Thanks for trading!");
    }

    private static void setupMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        market.put("GOOG", new Stock("GOOG", "Alphabet Inc.", 2800.0));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.0));
        market.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3300.0));
    }

    private static void updateMarketPrices() {
        for (Stock stock : market.values()) {
            stock.updatePrice();
        }
    }

    private static void viewMarket() {
        System.out.println("\n--- Market Prices ---");
        for (Stock stock : market.values()) {
            System.out.printf("%s (%s): $%.2f%n", stock.name, stock.symbol, stock.price);
        }
    }

    private static void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        if (market.containsKey(symbol)) {
            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();
            scanner.nextLine();  // clear buffer
            user.buyStock(market.get(symbol), qty);
        } else {
            System.out.println("Stock not found.");
        }
    }

    private static void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        if (market.containsKey(symbol)) {
            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();
            scanner.nextLine();  // clear buffer
            user.sellStock(market.get(symbol), qty);
        } else {
            System.out.println("Stock not found.");
        }
    }
}
