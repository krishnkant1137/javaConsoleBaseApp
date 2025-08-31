package controller;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

import model.User;
import service.UserService;

public class LoginController {

    public static User login() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== User Login ===");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your contact: ");
        String contact = sc.nextLine();

        // Simulate loading
        try {
            System.out.print("Verifying");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(500);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Generate OTP
        Random rand = new Random();
        int otp = 1000 + rand.nextInt(9000); // 4-digit OTP
        System.out.println("Your OTP is: " + otp);

        int attempts = 3;

        while (attempts > 0) {
            try {
                System.out.print("Enter the OTP to continue: ");
                int userOtp = Integer.parseInt(sc.nextLine().trim()); 

                if (userOtp == otp) {
                    // OTP verified
                    UserService userService = new UserService();
                    User user = userService.loginOrRegister(name, contact);

                    if (user != null) {
                        System.out.println("✅ Login successful! Welcome, " + user.getName() + "!");
                    } else {
                        System.out.println("❌ Login failed due to system error.");
                    }
                    return user;
                } else {
                    attempts--;
                    System.out.println("❌ Incorrect OTP. Attempts left: " + attempts);
                }

            } catch (NumberFormatException e) {
                attempts--;
                System.out.println("⚠️ Please enter a valid numeric OTP. Attempts left: " + attempts);
            } catch (Exception e) {
                attempts--;
                System.out.println("⚠️ Unexpected error: " + e.getMessage() + ". Attempts left: " + attempts);
            }
        }

        // After 3 failed attempts
        System.out.println("🔒 Too many failed OTP attempts. Login failed.");
        return null;
    }
}
