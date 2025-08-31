package controller;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
         int trials = 3;

   	 System.out.println("Welcome to Lost & Found Tracker");
     
while(trials > 0) {
    try {
        System.out.println("Login as: \n1. Admin\n2. User");
        System.out.print("Enter your choice (1 or 2): ");

        int choice = Integer.parseInt(sc.nextLine().trim()); 

     if (choice == 1) {
         AdminController adminController = new AdminController();
         adminController.showAdminMenu(); 
         break;
     } else if (choice == 2) {
             UserController userController = new UserController();
             userController.showUserMenu(); 
             break;
     } else {
    	 trials--;
         System.out.println("❌ Invalid choice. Trials left: " + trials);
     }
     } catch(NumberFormatException  e) {
         trials--;
         System.out.println("⚠️ Please enter a valid number (1 or 2). Trials left: " + trials);
     } catch (Exception e) {
         trials--;
         System.out.println("⚠️ An error occurred: " + e.getMessage() + ". Trials left: " + trials);
     }
}
    if (trials == 0) {
        System.out.println("🔒 All login attempts used. Exiting program...");
    }
}
    
}








