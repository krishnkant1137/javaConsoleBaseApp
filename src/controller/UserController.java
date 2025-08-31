package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import model.Item;
import model.User;
import service.ItemService;
import service.MatchingService;
import dao.ItemDao;

public class UserController {
	private final MatchingService matchingService;	 
	private final ItemService service;

	public UserController() {
	    ItemDao itemDao = new ItemDao();
	    this.service = new ItemService(); // yaha dao pass karo
	    this.matchingService = new MatchingService(itemDao);
	}
    
    
    public void showUserMenu() throws SQLException {
        User user = LoginController.login();
        if (user == null) {
            System.out.println("Exiting...");
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n========== User Menu =================================");
                System.out.println("1. Report Lost Item");
                System.out.println("2. Report Found Item");
                System.out.println("3. View My Reported Items by Contact Info");
                System.out.println("4. update you rejister item by id");
                System.out.println("5. delete you rejister item by id");
                System.out.println("6. Exit");
                System.out.println("==========================================================================");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine().trim()); // safe input

                switch (choice) {
                    case 1:
                        System.out.println("=== Report Lost Item ===");

                        System.out.print("Enter item name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter item type (e.g. Phone, Wallet): ");
                        String type = sc.nextLine();

                        System.out.print("Enter description: ");
                        String description = sc.nextLine();

                        System.out.print("Enter location lost: ");
                        String location = sc.nextLine();

                        System.out.print("Enter your contact info: ");
                        String contact = sc.nextLine();

                        Item item = new Item();
                        item.setItemName(name);
                        item.setItemType(type);
                        item.setDescription(description);
                        item.setLocationLost(location);
                        item.setStatus("Lost");
                        item.setUserId(user.getUserId());
                        item.setReportedDate(LocalDate.now());
                        item.setContactInfo(contact);

                        boolean success = service.reportItem(item);
                        if (success) {
                            System.out.println("✅ Item reported successfully!");
                            
                            // Fuzzy matching suggestion
                            List<Item> matches = matchingService.findMatchingItems(item);
                            if (!matches.isEmpty()) {
                                System.out.println("\n🔍 Possible matches found in Found Items:");
                                for (Item match : matches) {
                                    System.out.println("- " + match.getItemName() + " | Location: " + match.getLocationLost());
                                }
                            } else {
                                System.out.println("No similar found items in the system.");
                            }
                            
                        } else {
                            System.out.println("❌ Failed to report item.");
                        }

                        break;

                    case 2:
                        System.out.println("=== Report Found Item ===");

                        System.out.print("Enter item name: ");
                        String foundName = sc.nextLine();

                        System.out.print("Enter item type (e.g., Bag, Wallet): ");
                        String foundType = sc.nextLine();

                        System.out.print("Enter description: ");
                        String foundDescription = sc.nextLine();

                        System.out.print("Enter location where found: ");
                        String foundLocation = sc.nextLine();

                        System.out.print("Enter your contact info: ");
                        String foundContact = sc.nextLine();

                        Item foundItem = new Item();
                        foundItem.setItemName(foundName);
                        foundItem.setItemType(foundType);
                        foundItem.setDescription(foundDescription);
                        foundItem.setLocationFound(foundLocation);
                        foundItem.setStatus("Found");
                        foundItem.setContactInfo(foundContact);
                        foundItem.setReportedDate(LocalDate.now());

                        // IMPORTANT: Set user_id from logged in user
                        foundItem.setUserId(user.getUserId());

                        boolean foundSuccess = service.reportItem(foundItem);
                        if (foundSuccess) {
                            System.out.println("✅ Found item reported successfully!");
                        } else {
                            System.out.println("❌ Failed to report found item.");
                        }
                        break;


                    case 3:
                        System.out.println("🔍 Enter your contact info to view your items:");
                        String searchContact = sc.nextLine();

                        List<Item> allItems = service.getItemsByContact(searchContact);
                        if (allItems.isEmpty()) {
                            System.out.println("❌ No items found with this contact.");
                        } else {
                            System.out.println("✅ Items found:");
                            for (Item it : allItems) {
                                System.out.println(it);
                            }
                        }
                        break;
                        
                    case 4:
                    	try {
                            System.out.print("Enter item name: ");
                            String newName = sc.nextLine();

                            System.out.print("Enter item type (e.g. Phone, Wallet): ");
                            String newType = sc.nextLine();

                            System.out.print("Enter description: ");
                            String newDescription = sc.nextLine();

                            System.out.print("Enter location lost: ");
                            String newLocation = sc.nextLine();
                            
                            System.out.print("Enter your contact info: ");
                            String newContact = sc.nextLine();
                            
                            

                            Item NewItem = new Item();
                            int updatedId = Integer.parseInt(sc.nextLine());
                            NewItem.setItemName(newName);
                            NewItem.setItemType(newType);
                            NewItem.setDescription(newDescription);
                            NewItem.setLocationLost(newLocation);
                            NewItem.setContactInfo(newContact);
                            boolean check = service.updatedById(NewItem, updatedId); 
                        if (check) {
                            System.out.println("✅ Items updated:");
                        } else {
                            System.out.println("❌ No items found with this id.\"");  
                       }
                    	} catch(Exception e) {
                    		System.out.println(e);
                    	}
                        break;
                        
                    case 5:
                    	try {
                        System.out.println("🔍 Enter your id for delete items:");
                        int deleteId = Integer.parseInt(sc.nextLine());
                        boolean check = service.deleteById(deleteId); 
                        if (check) {
                            System.out.println("✅ Items deleted:");
                        } else {
                            System.out.println("❌ No items found with this id.\"");  
                       }
                    	} catch(Exception e) {
                    		System.out.println(e);
                    	}
                        break;

                    case 6:
                        System.out.println("👋 Thank you for using Lost & Found Tracker!");
                        return;

                    default:
                        System.out.println("⚠️ Invalid choice. Please select 1 to 4.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number (1-4).");
            } catch (Exception e) {
                System.out.println("⚠️ Something went wrong: " + e.getMessage());
            }
        }
    }
}
