package banking.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// AccountManager class manages the operations related to bank accounts such as crediting, debiting, transferring money, and checking balance.
public class AccountManager {
    
    // Class variables to store the database connection and input scanner.
    private Connection connection;
    private Scanner scanner;

    // Constructor to initialize the AccountManager class with a database connection and a scanner for input.
    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to credit money into the user's account.
    public void credit_money(long accNumber) throws SQLException {
        // Clears the scanner buffer and prompts user to input the amount and security pin.
        scanner.nextLine();
        System.out.print("ENTER THE AMOUNT : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("ENTER THE SECURITY PIN : ");
        String securityPin = scanner.nextLine();

        try {
            // Start transaction by disabling auto-commit.
            connection.setAutoCommit(false);
            if (accNumber != 0) {
                // SQL query to verify account number and security pin.
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNTS WHERE accNumber = ? and securityPin = ?");
                preparedStatement.setLong(1, accNumber);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                // If the account and pin match, update the account balance.
                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE accNumber = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, accNumber);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs. " + amount + " CREDITED SUCCESSFULLY..!!");
                        connection.commit(); // Commit the transaction.
                        connection.setAutoCommit(true); // Enable auto-commit again.
                        return;
                    } else {
                        System.out.println("TRANSACTION FAILED..!!");
                        connection.rollback(); // Rollback in case of failure.
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("INVALID SECURITY PIN..!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception.
        }
        connection.setAutoCommit(true); // Enable auto-commit if any exception occurs.
    }

    // Method to debit money from the user's account.
    public void debit_money(long accNumber) throws SQLException {
        // Clears the scanner buffer and prompts user to input the amount and security pin.
        scanner.nextLine();
        System.out.print("ENTER THE AMOUNT : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("ENTER THE SECURITY PIN : ");
        String securityPin = scanner.nextLine();

        try {
            // Start transaction by disabling auto-commit.
            connection.setAutoCommit(false);
            if (accNumber != 0) {
                // SQL query to verify account number and security pin.
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE accNumber = ? and securityPin = ?");
                preparedStatement.setLong(1, accNumber);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                // If account and pin match, check if sufficient balance is available.
                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");

                    // Check if the requested amount is less than or equal to current balance.
                    if (amount <= current_balance) {
                        // SQL query to update the account balance.
                        String debit_query = "UPDATE ACCOUNTS SET balance = balance - ? WHERE accNumber = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, accNumber);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs. " + amount + " DEBITED SUCCESSFULLY..!!");
                            connection.commit(); // Commit the transaction.
                            connection.setAutoCommit(true); // Enable auto-commit again.
                            return;
                        } else {
                            System.out.println("TRANSACTION FAILED..!!");
                            connection.rollback(); // Rollback in case of failure.
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("INSUFFICIENT BALANCE..!!");
                    }

                } else {
                    System.out.println("INVALID PIN..!!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception.
        }
        connection.setAutoCommit(true); // Enable auto-commit if any exception occurs.
    }

    // Method to transfer money from sender's account to receiver's account.
    public void transfer_money(long sender_accNumber) throws SQLException {
        // Clears the scanner buffer and prompts user to input the receiver's account number, amount, and security pin.
        scanner.nextLine();
        System.out.print("ENTER RECEIVER'S ACCOUNT NUMBER : ");
        long receiver_accNumber = scanner.nextLong();
        System.out.print("ENTER AMOUNT : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("ENTER SECURITY PIN : ");
        String securityPin = scanner.nextLine();
        try {
            // Start transaction by disabling auto-commit.
            connection.setAutoCommit(false);
            if (sender_accNumber != 0 && receiver_accNumber != 0) {
                // SQL query to verify sender's account number and security pin.
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE accNumber = ? and securityPin = ?");
                preparedStatement.setLong(1, sender_accNumber);
                preparedStatement.setString(2, securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                // If sender's account and pin match, check if sufficient balance is available for transfer.
                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        // SQL query to debit the sender's account.
                        String debit_query = "UPDATE ACCOUNTS SET balance = balance - ? WHERE accNumber = ?";
                        // SQL query to credit the receiver's account.
                        String credit_query = "UPDATE ACCOUNTS SET balance = balance + ? WHERE accNumber = ?";
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_accNumber);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_accNumber);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("TRANSACTION SUCCESSFUL..!!");
                            System.out.println("Rs. " + amount + " TRANSFERRED SUCCESSFULLY..!!");
                            connection.commit(); // Commit the transaction.
                            connection.setAutoCommit(true); // Enable auto-commit again.
                            return;
                        } else {
                            System.out.println("TRANSACTION FAILED..!!");
                            connection.rollback(); // Rollback in case of failure.
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("INVALID SECURITY PIN..!!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception.
        }
        connection.setAutoCommit(true); // Enable auto-commit if any exception occurs.
    }

    // Method to get the current balance of the account.
    public void getBalance(long accNumber) {
        // Clears the scanner buffer and prompts user to input the security pin.
        scanner.nextLine();
        System.out.print("ENTER SECURITY PIN : ");
        String securityPin = scanner.nextLine();
        try {
            // SQL query to retrieve the balance of the account based on account number and security pin.
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE accNumber = ? and securityPin = ?");
            preparedStatement.setLong(1, accNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Display the current balance if the security pin matches.
                double balance = resultSet.getDouble("balance");
                System.out.println("BALANCE : " + balance);
            } else {
                System.out.println("INVALID PIN..!!");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception.
        }
    }
}
