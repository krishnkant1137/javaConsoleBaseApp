package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import service.AdminService;
import model.Item;

public class AdminController {
    AdminService service = new AdminService();
    Scanner sc = new Scanner(System.in);

    public void showAdminMenu() throws SQLException {
        System.out.println("Only Real admin know the secret key :");
        String key = sc.nextLine();

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

        if (service.validateAdmin(key)) {
            System.out.println("Login Successful! You are the real admin ✅");
        } else {
            System.out.println("Invalid OTP. You are not the admin ❌");
            return;
        }

        int choice;

        while (true) {
            System.out.println("\n======= Admin Panel ========================");
            System.out.println("1. View All Lost Items");
            System.out.println("2. View All Found Items");
            System.out.println("3. View All Items");
            System.out.println("4. Delete an Item");
            System.out.println("6. Exit");
            System.out.println("============================================");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1:
                        System.out.println("📦 Lost items:");
                        List<Item> lostItems = service.getLostItem();
                        if (lostItems.isEmpty()) {
                            System.out.println("No lost items found.");
                        } else {
                            for (Item item : lostItems) {
                                System.out.println(item);
                            }
                        }
                        break;

                    case 2:
                        System.out.println("📦 Found items:");
                        List<Item> foundItems = service.getFoundItem();
                        if (foundItems.isEmpty()) {
                            System.out.println("No found items available.");
                        } else {
                            for (Item item : foundItems) {
                                System.out.println(item);
                            }
                        }
                        break;

                    case 3:
                        System.out.println("📦 All items:");
                        List<Item> allItems = service.getAllItem();
                        if (allItems.isEmpty()) {
                            System.out.println("No items in the system.");
                        } else {
                            for (Item item : allItems) {
                                System.out.println(item);
                            }
                        }
                        break;

                    case 4:
                        try {
                            System.out.print("Enter Item ID to delete: ");
                            int deleteId = Integer.parseInt(sc.nextLine().trim());
                            boolean isDeleted = service.deleteById(deleteId);
                            if (isDeleted) {
                                System.out.println("✅ Item deleted successfully.");
                            } else {
                                System.out.println("❌ Item not found with ID: " + deleteId);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Please enter a valid number.");
                        } catch (Exception e) {
                            System.out.println("❌ Error occurred while deleting item: " + e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.println("🚪 Exiting Admin Panel...");
                        return;

                    default:
                        System.out.println("⚠️ Invalid choice. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter numbers only.");
            }
        }
    }
}
