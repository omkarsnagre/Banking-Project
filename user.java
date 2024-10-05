package banking.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// The User class handles user registration and login functionality in the banking system.
public class User {
    
    // Class variables to store the database connection and input scanner.
    private Scanner scanner;
    private Connection connection;

    // Constructor to initialize the User class with a database connection and a scanner for input.
    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    // Method to register a new user by taking their full name, email, and password.
    public void register() {
        // Clears the scanner buffer.
        scanner.nextLine();

        // Input for full name, email, and password.
        System.out.print("ENTER FULL NAME : ");
        String fullName = scanner.nextLine();
        System.out.print("ENTER EMAIL : ");
        String email = scanner.nextLine();
        System.out.print("ENTER PASSWORD : ");
        String password = scanner.nextLine();

        // Check if the user already exists using the provided email.
        if (user_exist(email)) {
            System.out.println("USER ALREADY FOR THE EMAIL ADDRESS..!!");
            return; // If user exists, stop the registration process.
        }

        // SQL query to insert a new user record in the database.
        String register_query = "INSERT INTO user(fullName, email, password) values(?, ?, ?)";

        try {
            // Prepare the SQL statement with user details.
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            
            // Execute the SQL update and check if the insertion was successful.
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("REGISTRATION SUCCESSFUL..!!");
            } else {
                System.out.println("REGISTRATION FAILED..!!");
            }
        } catch (SQLException e) {
            // Print error details if any SQLException occurs during registration.
            e.printStackTrace();
        }
    }
    
    // Method for user login. It checks if the email and password match the database record.
    public String login() {
        // Clears the scanner buffer.
        scanner.nextLine();

        // Input for email and password.
        System.out.print("ENTER EMAIL: ");
        String email = scanner.nextLine();
        System.out.print("ENTER PASSWORD: ");
        String password = scanner.nextLine();

        // SQL query to check if a user exists with the given email and password.
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            // Prepare the SQL statement with the email and password provided by the user.
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query to retrieve the result.
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // If the email and password are correct, return the email, indicating a successful login.
            if (resultSet.next()) {
                return email;
            } else {
                // Return null if the login fails (i.e., no matching record found).
                return null;
            }
        } catch (SQLException e) {
            // Print error details if any SQLException occurs during login.
            e.printStackTrace();
        }
        return null; // Return null if any other issues occur.
    }

    // Method to check if a user already exists in the system based on the email provided.
    public boolean user_exist(String email) {
        // SQL query to check if there is already a record with the given email.
        String query = "SELECT * FROM user WHERE email = ?";

        try {
            // Prepare the SQL statement with the email to search for the user.
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query to check if the result set contains any data.
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Return true if a record with the email exists.
                return true;
            } else {
                // Return false if no matching user is found.
                return false;
            }
        } catch (SQLException e) {
            // Print error details if any SQLException occurs during user existence check.
            e.printStackTrace();
        }
        return false; // Return false if any other issues occur.
    }
}
