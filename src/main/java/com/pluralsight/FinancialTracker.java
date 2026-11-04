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


    // Main menu plus silly drawings because why not
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("""
════════════════════════════════════════════════════════════════════
                         🚚 MateTruck 🚚
      Empanadas • Yerba Mate • Milanesas • Pizzas • Helado
════════════════════════════════════════════════════════════════════

                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⢸⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⢟⣥⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠈⢿⣿⣷⣄⠀⠀⠀⠀⠀⢀⣴⣿⣴⠟⣡⡾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠹⣿⣿⣷⣄⠀⠀⠀⣸⣿⣿⣿⡾⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠈⠻⠿⣿⣷⣄⠰⡿⠛⠛⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⣠⣌⠻⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⢀⣀⣀⣀⣀⣉⣉⣉⣀⣉⣉⣁⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⢸⠏⢉⣉⡉⠉⣉⣉⠉⣉⣉⠉⣉⣉⡉⠹⡇⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠘⠀⠿⠿⠃⠸⠿⠿⠀⠿⠿⠀⠹⠿⠿⠀⠁⢸⠿⠿⠷⠦⠀⠀⠀⠀⠀⠀⠀
                ⠀⢰⡆⠀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡆⢸⠀⣶⣶⣶⣆⠀⠀⠀⠀⠀⠀
                ⠀⢸⡇⢠⣤⣥⣤⣤⣤⣤⣤⣤⣤⣤⣤⡄⢸⡇⢸⠀⠛⠛⠛⠛⠃⠀⠀⠀⠀⠀
                ⠀⢸⣧⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣤⣼⡇⢸⣿⣿⣿⣿⣿⣿⣿⣶⣶⡄⠀
                ⠀⠘⢿⣿⣿⡿⠛⠛⠻⣿⣿⣿⣿⣿⣿⣿⣿⡇⢸⣿⣿⣿⣿⡿⠛⠛⠛⢿⡇⠀
                ⠀⠀⠀⠈⠉⠀⣾⣿⡆⠈⠉⠉⠉⠉⠉⠉⠉⠁⠈⠉⠉⠉⠉⠀⢾⣿⡇⠈⠁⠀
                ⠀⠀⠀⠀⠀⠀ ⠈⠉⠀⠀    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⠀⠀⠀
        
                  Welcome to MateTruck's Ledger!
              Track revenues, expenses and reports.

════════════════════════════════════════════════════════════════════
""");

        while (running) {
            System.out.println("\n════════════ 🚚 Truck Menu 🚚 ════════════");
            System.out.println("What would you like to do?");
            System.out.println("1) Record Revenue (Deposits)");
            System.out.println("2) Record Expense (Payments)");
            System.out.println("3) Open Ledger Menu");
            System.out.println("0) Close Truck");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "1" -> addTransaction(scanner,"Deposit");
                case "2" -> addTransaction(scanner, "Payment");
                case "3" -> ledgerMenu(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();

        System.out.println("""
════════════════════════════════════════════════════════════════════

                  🚚 MateTruck is closing up! 🚚

            Safe travels and see you on the next route!!

════════════════════════════════════════════════════════════════════
""");
    }


    // Loads transactions.csv and parses it to a ledger array list
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

    // Adds new transaction to the ledger, works for both deposits and payments based on the transactionType value
    private static void addTransaction(Scanner scanner, String transactionType) {
        String date = null;
        String time = null;
        String description = null;
        String vendor = null;
        double amount = 0.00;
        boolean scheduling;

        while (true) {
            System.out.printf("\nWould you like to make this %s now or schedule it for a later date? \n", transactionType.toLowerCase());
            System.out.println("1) Now");
            System.out.println("2) Schedule");
            System.out.println("0) Back to Ledger Menu");
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "1" -> scheduling = false;
                case "2" -> scheduling = true;
                case "0" -> {
                    return;
                }
                default -> {
                    System.out.println("\nInvalid option\n");
                    continue;
                }
            }

            break;
        }

        LocalDateTime dateTimeObj;

        if (scheduling) {
            try {
                System.out.printf(
                        "\nInput desired date of %s (YYYY-MM-DD): ",
                        transactionType.toLowerCase());

                String dateInput = scanner.nextLine();

                System.out.print("Input desired time of day (HH-MM-SS): ");
                String timeInput = scanner.nextLine();
                timeInput = timeInput.replace('-', ':');

                LocalDate datePart =
                        LocalDate.parse(dateInput, DATE_FMT);

                LocalTime timePart =
                        LocalTime.parse(timeInput, TIME_FMT);

                dateTimeObj = LocalDateTime.of(datePart, timePart);

            } catch (DateTimeParseException e) {
                System.out.println("\nError: invalid date or time.");
                return;
            }
        } else {
            dateTimeObj = LocalDateTime.now();
        }

        date = dateTimeObj.format(DATE_FMT);
        time = dateTimeObj.format(TIME_FMT);

        while (true) {
            try {
                System.out.printf("\n%s description: ", transactionType);
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
                System.out.print("\nVendor name: ");
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
                System.out.printf("\n%s amount: ", transactionType);
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

        System.out.printf("\n%s made successfully!\n", transactionType);
    }


    // Updates transactions.csv file with new added deposits/payments
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

    // displays menu and calls fitting method based on user input
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n════════════ \uD83D\uDCD2 Ledger Menu \uD83D\uDCD2 ════════════");
            System.out.println("What would you like to do?");
            System.out.println("1) Display all transactions");
            System.out.println("2) Display revenues (Deposits)");
            System.out.println("3) Display expenses (Payments)");
            System.out.println("4) Open Reports Menu");
            System.out.println("0) Truck Menu");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "1" -> displayLedger(null, null, null, null, scanner);
                case "2" -> displayLedger("Deposits", null, null, null, scanner);
                case "3" -> displayLedger("Payments", null, null, null, scanner);
                case "4" -> reportsMenu(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }


    // Single method for displaying the ledger, receives multiple arguments based on the filters selected by the user, then calls their respective helper methods. Sends you back to menu if no matches
    private static void displayLedger(String transactionFilter, String dateFilter, String descriptionFilter, String vendorFilter, Scanner scanner) {
        List<Transaction> filteredLedger = new ArrayList<>(ledger);

        if (transactionFilter != null){
            filteredLedger = filterByTransaction(filteredLedger, transactionFilter);
        }

        if (dateFilter != null){
            filteredLedger = filterTransactionsByDate(filteredLedger, dateFilter);
        }

        if (descriptionFilter != null){
            filteredLedger = filterTransactionsByDescription(filteredLedger, descriptionFilter);
        }

        if (vendorFilter != null){
            filteredLedger = filterTransactionsByVendor(filteredLedger, vendorFilter, scanner);
        }

        if (filteredLedger.isEmpty()) {
            System.out.println("\nNo matching transactions found.");
            return;
        }

        filteredLedger.sort(
                Comparator.comparing((Transaction transaction) ->
                                LocalDateTime.parse(
                                        transaction.getDate() + " " + transaction.getTime(),
                                        DATETIME_FMT))
                        .reversed()
        );

        System.out.printf("\n%-12s %-12s %-34s %-30s %-10s%n",
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════════════════════════");

        for (Transaction transaction : filteredLedger) {
            System.out.printf("%-12s %-12s %-34s %-30s $%-10.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
    }


    // Pretty similar to ledgerMenu in terms of function
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n════════════ \uD83D\uDCCA Reports Menu \uD83D\uDCCA ════════════");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back to Ledger Menu");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> displayLedger(null, "MonthToDate", null, null, scanner);
                case "2" -> displayLedger(null, "PrevMonth", null, null, scanner);
                case "3" -> displayLedger(null, "YearToDate", null, null, scanner);
                case "4" -> displayLedger(null, "PrevYear", null, null, scanner);
                case "5" -> displayLedger(null, null, null, "DefaultFilter", scanner);
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    // receives ledger array lists and filters it to keep only deposits or payments depending on user input, if no matches found prints error message.
    private static ArrayList<Transaction> filterByTransaction(
            List<Transaction> transactions,
            String transactionFilter) {

        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if ("Deposits".equals(transactionFilter) && amount <= 0) {
                continue;
            }

            if ("Payments".equals(transactionFilter) && amount >= 0) {
                continue;
            }

            filteredTransactions.add(transaction);
        }

        if (filteredTransactions.isEmpty()) {
            System.out.printf("\nNo %s found.\n", transactionFilter.toLowerCase());
        }

        return filteredTransactions;
    }


    // receives ledger array list and filters transactions to keep only the ones within the either preselected date ranges or a custom one chosen by user, if no matches found prints error message.
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
            default:
                String[] dates = dateFilter.split("\\|");

                startDate = LocalDate.parse(dates[0], DATE_FMT);
                endDate = LocalDate.parse(dates[1], DATE_FMT);
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

        if (filteredTransactions.isEmpty()) {
            System.out.println("\nNo matches found within that date range.");
        }

        return filteredTransactions;
    }

    // receives ledger array list and keeps only the transactions with the matching vendor inputted by user.
    // If user already specified the vendor in customSearch it filters automatically. If not it prompts them to specify vendor now. If no matches found it prints error message
    private static ArrayList<Transaction> filterTransactionsByVendor(List<Transaction> transactions, String vendorFilter, Scanner scanner) {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        if (vendorFilter.equals("DefaultFilter")) {
            System.out.print("\nVendor name: ");
            vendorFilter = scanner.nextLine().trim();
        }

        for (Transaction transaction : transactions) {

            if (!transaction.getVendor().equalsIgnoreCase(vendorFilter)) {
                continue;
            }

            filteredTransactions.add(transaction);
        }

        if (filteredTransactions.isEmpty()) {
            System.out.println("\nNo vendor match found.");
        }

        return filteredTransactions;
    }

    private static ArrayList<Transaction> filterTransactionsByDescription(List<Transaction> transactions, String descriptionFilter) {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (!transaction.getDescription().equalsIgnoreCase(descriptionFilter)) {
                continue;
            }
            filteredTransactions.add(transaction);
        }

        if (filteredTransactions.isEmpty()) {
            System.out.println("\nNo description match found.");
        }

        return filteredTransactions;
    }

    // Prompts user to specify any desired transaction, date, description and vendor parameters, then calls displayLedger() with all the specified filters as arguments.
    private static void customSearch(Scanner scanner) {
        System.out.println("\nCustom search");
        String transactionFilter = null;
        String dateFilter = null;
        String descriptionFilter = null;
        String vendorFilter = null;

        while (true) {
            System.out.println("Choose transaction type: ");
            System.out.println("1) All Transactions");
            System.out.println("2) Revenues (Deposits)");
            System.out.println("3) Expenses (Payments)");
            System.out.println("0) Back to Reports Menu");
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "1" -> transactionFilter = null;
                case "2" -> transactionFilter = "Deposits";
                case "3" -> transactionFilter = "Payments";
                case "0" -> {
                    return;
                }
                default -> {
                    System.out.println("\nInvalid option\n");
                    continue;
                }
            }

            break;
        }

        while (true) {
            System.out.println("\nChoose date range:");
            System.out.println("1) All Dates");
            System.out.println("2) Month To Date");
            System.out.println("3) Previous Month");
            System.out.println("4) Year To Date");
            System.out.println("5) Previous Year");
            System.out.println("6) Custom Date Range");
            System.out.println("0) Back to Reports Menu");
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "1" -> dateFilter = null;
                case "2" -> dateFilter = "MonthToDate";
                case "3" -> dateFilter = "PrevMonth";
                case "4" -> dateFilter = "YearToDate";
                case "5" -> dateFilter = "PrevYear";
                case "6" -> dateFilter = customDateInput(scanner);
                case "0" -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid option, try again.");
                    continue;
                }
            }

            break;
        }

        if ("Invalid input".equals(dateFilter)) {
            return;
        }

        System.out.print("\nDescription: ");
        String descriptionInput = scanner.nextLine().trim();
        if (!descriptionInput.isEmpty()) {
            descriptionFilter = descriptionInput;
        }

        System.out.print("\nVendor: ");
        String vendorInput = scanner.nextLine().trim();
        if (!vendorInput.isEmpty()) {
            vendorFilter = vendorInput;
        }

        displayLedger(transactionFilter, dateFilter, descriptionFilter, vendorFilter, scanner);
    }


    // used in customSearch(), makes sure user input fits into date format, then turns it back into a string so it can be passed as a filter argument
    private static String customDateInput(Scanner scanner) {
        try {
            System.out.println("\nCustom date range");

            System.out.print("Input start date (yyyy-MM-dd): ");
            String startDate = scanner.nextLine().trim();

            System.out.print("Input end date (yyyy-MM-dd): ");
            String endDate = scanner.nextLine().trim();

            LocalDate.parse(startDate, DATE_FMT);
            LocalDate.parse(endDate, DATE_FMT);

            return startDate + "|" + endDate;

        } catch (DateTimeParseException e) {
            System.out.println("\nInvalid date format.");
            return "Invalid input";
        }
    }

}
