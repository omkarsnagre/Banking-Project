package banking.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// The Accounts class handles the creation of bank accounts, retrieval of account numbers,
// and checking if an account already exists in the system.
public class Accounts {
	
    // Class variables to store the database connection and input scanner.
	private Connection connection;
	private Scanner scanner;

    // Constructor to initialize the Accounts class with a database connection and a scanner for input.
	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
    // Method to open a new account. It accepts the user's email and prompts for other details like full name,
    // initial balance, and security PIN.
    public long openAccount(String email) {
        // Check if an account already exists for the provided email.
		if (!account_exists(email)) {
			// SQL query to insert a new account record in the database.
			String open_account_query = "INSERT INTO accounts(accNumber, fullName, email, balance, securityPin) VALUES(?, ?, ?, ?, ?)";

            // Input for full name, initial balance, and security PIN.
			scanner.nextLine();
			System.out.print("ENTER FULL NAME: ");
			String fullName = scanner.nextLine();
			System.out.print("ENTER INITIAL AMOUNT: ");
			Double balance = scanner.nextDouble();
			scanner.nextLine(); // Clear the buffer.
			System.out.print("ENTER SECURITY PIN: ");
			String securityPin = scanner.nextLine();
			
			try {
				// Generate a new account number.
				long accNumber = generateAccountNumber();

				// Prepare the SQL statement with account details.
				PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
				preparedStatement.setLong(1, accNumber);
				preparedStatement.setString(2, fullName);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4, balance);
				preparedStatement.setString(5, securityPin);
				
                // Execute the SQL update and check if the account creation was successful.
				int affectedRows = preparedStatement.executeUpdate();
				if (affectedRows > 0) {
					return accNumber; // Return the generated account number.
				} else {
					System.out.println("ACCOUNT CREATION FAILED.");
				}
				
			} catch (SQLException e) {
				// Print error details if any SQLException occurs during account creation.
				e.printStackTrace();
			}
		}
        // If account already exists, throw a runtime exception.
		throw new RuntimeException("ACCOUNT ALREADY EXISTS!");
	}
	
    // Method to retrieve the account number associated with a specific email.
	public long getAccountNumber(String email) {
		// SQL query to retrieve the account number for the given email.
		String query = "SELECT accNumber FROM accounts WHERE email = ?";
		try {
			// Prepare the SQL statement with the email.
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			// Execute the query and get the result set.
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("accNumber"); // Return the account number if found.
			}
		} catch (SQLException e) {
			// Print error details if any SQLException occurs during retrieval.
			e.printStackTrace();
		}
        // Throw a runtime exception if the account number is not found.
		throw new RuntimeException("ACCOUNT NUMBER DOESN'T EXIST..!!");
	}
	
    // Method to generate a new account number. It retrieves the last created account number and increments it.
	private long generateAccountNumber() {
		// SQL query to get the last created account number, ordered in descending order.
		String query = "SELECT accNumber FROM accounts ORDER BY accNumber DESC LIMIT 1";
		try {
			// Create a SQL statement.
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			// If there is an account, increment the last account number by 1.
			if (resultSet.next()) {
				long last_account_number = resultSet.getLong("accNumber");
				return last_account_number + 1;
			} else {
				// If no account exists, start with an initial account number.
				return 868310100;
			}
		} catch (SQLException e) {
			// Print error details if any SQLException occurs during account number generation.
			e.printStackTrace();
		}
        // Return the initial account number in case of errors.
		return 868310100;		
	}
	
    // Method to check if an account exists for a given email.
	public boolean account_exists(String email) {
		// SQL query to check if there is already an account with the given email.
		String query = "SELECT accNumber FROM accounts WHERE email = ?";
		try {
			// Prepare the SQL statement with the email.
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			// Execute the query to check if the result set contains any data.
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true; // Return true if the account exists.
			} else {
				return false; // Return false if no matching account is found.
			}
		} catch (SQLException e) {
			// Print error details if any SQLException occurs during account existence check.
			e.printStackTrace(); 
		}
		return false; // Return false if any other issues occur.
	}
}
