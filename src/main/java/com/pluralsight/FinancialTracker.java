package com.pluralsight;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FinancialTracker {

    private static ArrayList<Transaction> ledger = new ArrayList<>();

    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addTransaction(scanner,"Deposit");
                case "P" -> addTransaction(scanner, "Payment");
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        try {
            ledger.clear();

            Path filePath = Path.of(fileName);

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String date = parts[0];
                String time = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
                ledger.add(newTransaction);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addTransaction(Scanner scanner, String transactionType) {
        String date = null;
        String time = null;
        String description = null;
        String vendor = null;
        double amount = 0.00;

        while (true) {
            try {
                System.out.printf("Please write the date + time you would like to make the %s (yyyy-MM-dd HH:mm:ss): \n", transactionType.toLowerCase());
                String dateTime = scanner.nextLine();
                LocalDateTime dateTimeObj = LocalDateTime.parse(dateTime, DATETIME_FMT);

                date = dateTimeObj.format(DATE_FMT);
                time = dateTimeObj.format(TIME_FMT);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error: invalid date/time format.");
            }
        }

        while (true) {
            try {
                System.out.printf("%s description: ", transactionType);
                description = scanner.nextLine();
                if (!description.isEmpty()) {
                    break;
                }
                System.out.println("Error: Description cannot be empty");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid input.");
            }
        }

        while (true) {
            try {
                System.out.print("Vendor name: ");
                vendor = scanner.nextLine();
                if (!vendor.isEmpty()) {
                    break;
                }
                System.out.println("Error: Vendor cannot be empty");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid input.");
            }
        }

        while (true) {
            try {
                System.out.printf("%s amount: ", transactionType);
                amount = Double.parseDouble(scanner.nextLine());
                if (amount > 0) {
                    amount = transactionType.equals("Payment")
                            ? -Math.abs(amount)
                            : amount;
                    break;
                }
                System.out.printf("Error: Amount must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid input.");
            }
        }

        Transaction newTransaction =
                new Transaction(date, time, description, vendor, amount);


        writeToFile(newTransaction);

        ledger.add(newTransaction);
    }

    private static void writeToFile(Transaction transaction){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
            bufferedWriter.newLine();
            bufferedWriter.write(transaction.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\nLedger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger(null, null);
                case "D" -> displayLedger("Deposit", null);
                case "P" -> displayLedger("Payment", null);
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void displayLedger(String transactionFilter, String dateFilter) {
        List<Transaction> filteredLedger = List.of();

        if (dateFilter != null){
            filteredLedger = filterTransactionsByDate(ledger, dateFilter);
        }


        List<Transaction> sortedLedger = new ArrayList<>(ledger);

        sortedLedger.sort(
                Comparator.comparing((Transaction transaction) ->
                                LocalDateTime.parse(
                                        transaction.getDate() + " " + transaction.getTime(),
                                        DATETIME_FMT))
                        .reversed()
        );

        System.out.printf("\n%-12s %-12s %-30s %-20s %-10s%n",
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Transaction transaction : filteredLedger) {
            double amount = transaction.getAmount();
            if ("Deposit".equals(transactionFilter) && amount <= 0) {
                continue;
            }

            if ("Payment".equals(transactionFilter) && amount >= 0) {
                continue;
            }

            System.out.printf("%-12s %-12s %-30s %-20s $%-10.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getdescription(),
                    transaction.getvendor(),
                    amount);
        }
    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\nReports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> displayLedger(null, "MonthToDate");
                case "2" -> displayLedger(null, "PrevMonth");
                case "3" -> displayLedger(null, "YearToDate");
                case "4" -> displayLedger(null, "PrevYear");
                // case "5" -> filterTransactionsByDate();
                // case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    // private static ArrayList<Transaction> filterByTransactions(List<Transaction> transactions,
    //                                                           String transactionFilter) {

        //return();
    //}


    private static ArrayList<Transaction> filterTransactionsByDate(
            List<Transaction> transactions,
            String dateFilter) {

        LocalDate startDate = null;
        LocalDate endDate = null;

        switch (dateFilter) {
            case "MonthToDate":
                startDate = LocalDate.now().withDayOfMonth(1);
                endDate = LocalDate.now();
                break;
            case "PrevMonth":
                startDate = LocalDate.now()
                        .minusMonths(1)
                        .withDayOfMonth(1);
                endDate = LocalDate.now()
                        .withDayOfMonth(1)
                        .minusDays(1);
                break;
            case "YearToDate":
                startDate = LocalDate.now().withDayOfYear(1);
                endDate = LocalDate.now();
                break;
            case "PrevYear":
                startDate = LocalDate.now()
                        .minusYears(1)
                        .withDayOfYear(1);
                endDate = LocalDate.now()
                        .withDayOfYear(1)
                        .minusDays(1);
                break;
        }

        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {

            LocalDate transactionDate =
                    LocalDate.parse(transaction.getDate(), DATE_FMT);

            if (transactionDate.isBefore(startDate)
                    || transactionDate.isAfter(endDate)) {
                continue;
            }

            filteredTransactions.add(transaction);
        }

        return filteredTransactions;
    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        /* TODO – return LocalDate or null */
        return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
}
