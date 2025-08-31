package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;


import model.Item;

public class ItemDao {
	OnlyConnection connProvider = new OnlyConnection();

	public boolean saveIten(Item item) throws SQLException{
		
	    String insert = "INSERT INTO items (item_name, item_type, description, location_lost, location_Found, status, reported_date, contact_info, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = connProvider.getConnection();
	         PreparedStatement pst = con.prepareStatement(insert)) {

	    	    pst.setString(1, item.getItemName());
	    	    pst.setString(2, item.getItemType());
	    	    pst.setString(3, item.getDescription());
	    	    pst.setString(4, item.getStatus().equals("Lost") ? item.getLocationLost() : null);
	    	    pst.setString(5, item.getStatus().equals("Found") ? item.getLocationFound() : null);
	    	    pst.setString(6, item.getStatus());
	    	    pst.setDate(7, Date.valueOf(item.getReportedDate()));
	    	    pst.setString(8, item.getContactInfo());
	    	    pst.setInt(9, item.getUserId());

	        int rows = pst.executeUpdate();
	        return rows > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public List<Item> getAllItems() throws SQLException {	
	    List<Item> itemList = new ArrayList<>();
	    String sql = "SELECT * FROM items";
	    try (Connection con = connProvider.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {

	        while (rs.next()) {
	            Item item = new Item();
	            item.setItemId(rs.getInt("item_id"));
	            item.setItemName(rs.getString("item_name"));
	            item.setItemType(rs.getString("item_type"));
	            item.setDescription(rs.getString("description"));
	            item.setLocationLost(rs.getString("location_lost"));
	            item.setLocationFound(rs.getString("location_found"));
	            item.setStatus(rs.getString("status"));
	            item.setReportedDate(rs.getDate("reported_date").toLocalDate());
	            item.setContactInfo(rs.getString("contact_info"));
	            itemList.add(item);
	        }
	    }
	    return itemList;
	}


	
	public List<Item> getItemsByContact(String contactInfo) throws SQLException {
	    List<Item> itemList = new ArrayList<>();
	    String sql = "SELECT * FROM items WHERE contact_info = ?";
	    try (Connection con = connProvider.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	         
	        pst.setString(1, contactInfo);
	        ResultSet rs = pst.executeQuery();
	        
	        while (rs.next()) {
	            Item item = new Item();
	            item.setItemId(rs.getInt("item_id"));
	            item.setItemName(rs.getString("item_name"));
	            item.setItemType(rs.getString("item_type"));
	            item.setDescription(rs.getString("description"));
	            item.setLocationLost(rs.getString("location_lost"));
	            item.setLocationFound(rs.getString("location_found"));
	            item.setStatus(rs.getString("status"));
	            item.setReportedDate(rs.getDate("reported_date").toLocalDate());
	            item.setContactInfo(rs.getString("contact_info"));
	            itemList.add(item);
	        }
	    }
	    return itemList;
	}

	public boolean deleteById(int deleteId) throws SQLException {
		String query = "delete from items where item_id=?";
		try(Connection conn = connProvider.getConnection()){
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, deleteId);
			int rows = pst.executeUpdate();
			return rows > 0;
		} catch(Exception e) {
			
		}
		return false;
	}

	public boolean updatedById(Item newItem, int updatedId) throws SQLException  {
		String query = "update items set item_name = ?, item_type=?, description=?, location_lost=?, contact_info=? where item_id=?"; 
		try(Connection conn = connProvider.getConnection()){
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, newItem.getItemName());
			pst.setString(2, newItem.getItemType());
			pst.setString(3, newItem.getDescription());
			pst.setString(4, newItem.getLocationLost());
			pst.setString(5, newItem.getContactInfo());
			pst.setInt(6, updatedId);
			
		} catch (Exception e) {
			
		}
		return false;
	}
	
	// Add in ItemDao.java

	public List<Item> findPotentialMatches(Item newItem) throws SQLException {
	    List<Item> matches = new ArrayList<>();
	    String sql = "SELECT * FROM items WHERE item_type = ? AND item_id != ?";
	    try (Connection con = connProvider.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, newItem.getItemType());
	        pst.setInt(2, newItem.getItemId() == 0 ? -1 : newItem.getItemId());
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            Item existingItem = new Item();
	            existingItem.setItemId(rs.getInt("item_id"));
	            existingItem.setItemName(rs.getString("item_name"));
	            existingItem.setItemType(rs.getString("item_type"));
	            existingItem.setDescription(rs.getString("description"));
	            existingItem.setLocationLost(rs.getString("location_lost"));
	            existingItem.setLocationFound(rs.getString("location_found"));
	            existingItem.setStatus(rs.getString("status"));
	            existingItem.setContactInfo(rs.getString("contact_info"));

	            double score = calculateMatchScore(newItem, existingItem);
	            if (score >= 0.6) {
	                matches.add(existingItem);
	            }
	        }
	    }
	    return matches;
	}

	private double calculateMatchScore(Item item1, Item item2) {
	    double score = 0.0;

	    // Name match
	    if (item1.getItemName() != null && item2.getItemName() != null &&
	        item1.getItemName().equalsIgnoreCase(item2.getItemName())) {
	        score += 0.5;
	    }

	    // Description match
	    if (item1.getDescription() != null && item2.getDescription() != null &&
	        item1.getDescription().equalsIgnoreCase(item2.getDescription())) {
	        score += 0.2;
	    }

	    // Location match
	    if (item1.getLocationLost() != null && item2.getLocationFound() != null &&
	        item1.getLocationLost().equalsIgnoreCase(item2.getLocationFound())) {
	        score += 0.3;
	    }

	    return score;
	}


	// String similarity using Levenshtein
	private double similarity(String s1, String s2) {
	    if (s1 == null || s2 == null) return 0.0;
	    s1 = s1.toLowerCase().trim();
	    s2 = s2.toLowerCase().trim();
	    int maxLen = Math.max(s1.length(), s2.length());
	    if (maxLen == 0) return 1.0;
	    return (maxLen - levenshtein(s1, s2)) / (double) maxLen;
	}

	private int levenshtein(String s1, String s2) {
	    int[] prev = new int[s2.length() + 1];
	    for (int j = 0; j <= s2.length(); j++) prev[j] = j;

	    for (int i = 1; i <= s1.length(); i++) {
	        int[] curr = new int[s2.length() + 1];
	        curr[0] = i;
	        for (int j = 1; j <= s2.length(); j++) {
	            int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
	            curr[j] = Math.min(
	                Math.min(curr[j - 1] + 1, prev[j] + 1),
	                prev[j - 1] + cost
	            );
	        }
	        prev = curr;
	    }
	    return prev[s2.length()];
	}


}
