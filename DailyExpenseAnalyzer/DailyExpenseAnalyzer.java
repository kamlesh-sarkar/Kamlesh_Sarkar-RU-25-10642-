import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PS 27 - Daily Expense Analyzer
 * Console application to record and analyze daily expenses.
 * Concepts: ArrayList, Sorting, Searching, Methods, File Handling.
 */

public class DailyExpenseAnalyzer {

    private static final String FILE_NAME = "expenses.txt";
    private static final ArrayList<Expense> expenses = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    // ---------- Main menu ----------
    public static void main(String[] args) {
        loadFromFile();
        int choice;
        do {
            printMenu();
            choice = readChoice();
            switch (choice) {
                case 1: addExpense();        break;
                case 2: displayAll();        break;
                case 3: sortByAmount();      break;
                case 4: searchByCategory();  break;
                case 5: totalExpense();      break;
                case 6: saveToFile();
                        System.out.println("Expenses saved. Goodbye!");
                        break;
                default: System.out.println("Invalid choice. Enter 1-6.");
            }
        } while (choice != 6);
    }

    static void printMenu() {
        System.out.println("\n==== Daily Expense Analyzer ====");
        System.out.println("1. Add Expense");
        System.out.println("2. Display All");
        System.out.println("3. Sort by Amount (high to low)");
        System.out.println("4. Search by Category");
        System.out.println("5. Total Expense");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    static int readChoice() {
        if (!sc.hasNextLine()) return 6;           // end of input -> exit safely
        String line = sc.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ---------- 1. Add ----------
    static void addExpense() {
        System.out.print("Category: ");
        String category = sc.nextLine().trim().replace(",", " ");
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }
        System.out.print("Amount: ");
        String text = sc.nextLine().trim();
        double amount;
        try {
            amount = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }
        Expense e = new Expense(nextId(), category, amount);
        expenses.add(e);
        saveToFile();
        System.out.println("Expense added. ID: " + e.getId() + " | "
                + e.getCategory() + " | " + fmt(e.getAmount()));
    }

    // First ID is X101; later IDs continue after the highest existing number.
    static String nextId() {
        int max = 100;
        for (Expense e : expenses) {
            int n = Integer.parseInt(e.getId().substring(1));
            if (n > max) max = n;
        }
        return "X" + (max + 1);
    }

    // ---------- 2. Display all ----------
    static void displayAll() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    // ---------- 3. Sort by amount (own insertion sort, descending) ----------
    static void sortByAmount() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        ArrayList<Expense> sorted = new ArrayList<>(expenses);   // keep original order intact
        insertionSortDescending(sorted);
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println((i + 1) + ". " + sorted.get(i));
        }
    }

    static void insertionSortDescending(ArrayList<Expense> list) {
        for (int i = 1; i < list.size(); i++) {
            Expense key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getAmount() < key.getAmount()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ---------- 4. Search by category (own linear search) ----------
    static void searchByCategory() {
        System.out.print("Category to search: ");
        String key = sc.nextLine().trim();
        boolean found = false;
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            if (e.getCategory().equalsIgnoreCase(key)) {
                System.out.println("Found: " + e);
                found = true;
            }
        }
        if (!found) System.out.println("No expense found for category: " + key);
    }

    // ---------- 5. Total and highest ----------
    static void totalExpense() {
        if (expenses.isEmpty()) {
            System.out.println("Total expense: 0 | Highest expense: None");
            return;
        }
        double total = 0;
        Expense highest = expenses.get(0);
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            total += e.getAmount();
            if (e.getAmount() > highest.getAmount()) highest = e;
        }
        System.out.println("Total expense: " + fmt(total) + " | Highest expense: "
                + highest.getCategory() + " (" + fmt(highest.getAmount()) + ")");
    }

    // ---------- File handling ----------
    static void saveToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                out.println(e.getId() + "," + e.getCategory() + "," + e.getAmount());
            }
        } catch (IOException ex) {
            System.out.println("Error saving file: " + ex.getMessage());
        }
    }

    static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;                      // first run: start empty
        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length != 3) continue;          // skip corrupt lines
                try {
                    double amt = Double.parseDouble(p[2].trim());
                    String id = p[0].trim();
                    Integer.parseInt(id.substring(1)); // validate ID format
                    if (amt > 0 && id.startsWith("X")) {
                        expenses.add(new Expense(id, p[1].trim(), amt));
                    }
                } catch (RuntimeException ex) {
                    // ignore invalid line
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }
    }

    // Prints 250.0 as "250" and 99.5 as "99.50"
    static String fmt(double v) {
        return (v == Math.floor(v)) ? String.valueOf((long) v) : String.format("%.2f", v);
    }
}

// ---------- Expense: one record = one object ----------
class Expense {
    private final String id;
    private final String category;
    private final double amount;

    Expense(String id, String category, double amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;
    }

    String getId()       { return id; }
    String getCategory() { return category; }
    double getAmount()   { return amount; }

    @Override
    public String toString() {
        return id + " | " + category + " | " + DailyExpenseAnalyzer.fmt(amount);
    }
}
