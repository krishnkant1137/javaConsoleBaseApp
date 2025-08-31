package service;

import model.Item;

import java.sql.SQLException;

import java.util.List;

import dao.ItemDao;

public class ItemService {
	ItemDao dao = new ItemDao();
	
	public void addItem(Item item) throws SQLException {
	    boolean saved = dao.saveIten(item); // spelling fix later to saveItem()
	    if (!saved) {
	        System.out.println("❌ Failed to save item.");
	        return;
	    }

	    List<Item> matches = dao.findPotentialMatches(item);
	    if (!matches.isEmpty()) {
	        System.out.println("\nPossible Matches Found:");
	        for (Item match : matches) {
	            System.out.println("- " + match.getItemName() + " (" + match.getLocationLost() + ")");
	        }
	    } else {
	        System.out.println("\nNo potential matches found.");
	    }
	}

	
	public boolean reportItem(Item item) throws SQLException {
	    return dao.saveIten(item); 
	}
	
    public List<Item> getItemsByContact(String contactInfo) throws SQLException {
		ItemDao dao = new ItemDao();
    	return dao.getItemsByContact(contactInfo);    	 
    }

	public boolean deleteById(int deleteId) throws SQLException {
		return dao.deleteById(deleteId);
	}

	public boolean updatedById(Item newItem, int updatedId) throws SQLException {
		return dao.updatedById(newItem, updatedId);
	}	
}
