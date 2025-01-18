package banking.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

// BankingApp class serves as the entry point for the banking application, handling user registration, login, and account management.
public class BankingApp {

    // Database connection details
    private static String url = "jdbc:mysql://localhost:3306/banking";
    private static String username = "root";
    private static String password = "root";

    // Main method to start the application
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle exception if driver is not found
        }
        
        try {
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in); // Create a scanner for user input
            User user = new User(connection, scanner); // Create User object for user operations
            Accounts accounts = new Accounts(connection, scanner); // Create Accounts object for account operations
            AccountManager accountManager = new AccountManager(connection, scanner); // Create AccountManager for account transactions
            
            String email; // Variable to store user email
            long accNumber; // Variable to store account number
            
            while (true) {
                // Display welcome message and options
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. REGISTER");
                System.out.println("2. LOGIN");
                System.out.println("3. EXIT");
                System.out.print("ENTER YOUR CHOICE : ");
                int choice1 = scanner.nextInt(); // Get user choice
                
                // Handle user choices
                switch (choice1) {
                    case 1:
                        user.register(); // Call register method for user registration
                        break;

                    case 2:
                        email = user.login(); // Call login method and get user email
                        if (email != null) {
                            System.out.println();
                            System.out.println("USER LOGGED IN ");
                            // Check if user already has an account
                            if (!accounts.account_exists(email)) {
                                System.out.println();
                                System.out.println("1. OPEN NEW ACCOUNT");
                                System.out.println("2. EXIT ");
                                if (scanner.nextInt() == 1) {
                                    accNumber = accounts.openAccount(email); // Open a new account
                                    System.out.println("ACCOUNT CREATED SUCCESSFULLY..!!");
                                    System.out.println("YOUR ACCOUNT NUMBER IS : " + accNumber);
                                } else {
                                    break; // Exit if user does not want to open an account
                                }
                            }
                            accNumber = accounts.getAccountNumber(email); // Get account number for logged-in user
                            int choice2 = 0; // Choice for account operations
                            while (choice2 != 5) { // Loop for account operations
                                System.out.println();
                                System.out.println("1. DEBIT MONEY");
                                System.out.println("2. CREDIT MONEY");
                                System.out.println("3. TRANSFER MONEY");
                                System.out.println("4. CHECK BALANCE");
                                System.out.println("5. LOG OUT ");
                                choice2 = scanner.nextInt(); // Get user choice for account operations
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(accNumber); // Call method to debit money
                                        break;
                                    case 2:
                                        accountManager.credit_money(accNumber); // Call method to credit money
                                        break;
                                    case 3:
                                        accountManager.transfer_money(accNumber); // Call method to transfer money
                                        break;
                                    case 4:
                                        accountManager.getBalance(accNumber); // Call method to check balance
                                        break;
                                    case 5:
                                        break; // Log out
                                    default:
                                        System.out.println("ENTER A VALID CHOICE..!!"); // Invalid choice message
                                        break;
                                }
                            }
                        } else {
                            System.out.println("INCORRECT EMAIL OR PASSWORD..!!"); // Invalid login message
                        }
                        break; // End of case 2

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM..!!");
                        System.out.println("EXITING SYSTEM..!!");
                        return; // Exit the application
                    default:
                        System.out.println("ENTER A VALID CHOICE..!! "); // Invalid choice message
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }
}
