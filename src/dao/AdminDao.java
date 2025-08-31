package dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Admin;
import model.Item;
import model.User;

public class AdminDao {
	OnlyConnection connProvider = new OnlyConnection();
	
	 public Admin getAdminPassword() {
	        Admin admin = null;
	        try (Connection conn = connProvider.getConnection()) {
	            String query = "SELECT password FROM admin LIMIT 1";
	            PreparedStatement ps = conn.prepareStatement(query);
	            ResultSet rs = ps.executeQuery();
	            
	            if (rs.next()) {
	                admin = new Admin(rs.getString("password"));
	            }
	        } catch (Exception e) {
	            System.out.println("Error fetching admin password: " + e.getMessage());
	        }
	        return admin;
	    }
	
	public List<Item> getAllItem() throws SQLException {
	    List<Item> itemList = new ArrayList<>();
		Connection con = connProvider.getConnection();
		String query = "SELECT * from items";
		PreparedStatement pst = con.prepareStatement(query);
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
        return itemList;
	}
	
	public List<Item> getLostItem() throws SQLException {
	    List<Item> itemList = new ArrayList<>();
		Connection con = connProvider.getConnection();
		String query = "SELECT * FROM items WHERE status='lost' ";
		PreparedStatement pst = con.prepareStatement(query);
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
        return itemList;
	}
	
	public List<Item> getFoundItem() throws SQLException {
	    List<Item> itemList = new ArrayList<>();
		Connection con = connProvider.getConnection();
		String query = "SELECT * FROM items WHERE status='Found' ";
		PreparedStatement pst = con.prepareStatement(query);
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
        return itemList;  
	}

	public boolean deleteById(int deleteId) throws SQLException {
		String query = "delete from items where item_id=?";
		try(Connection conn = connProvider.getConnection()){
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, deleteId);
	        int affectedRows = pst.executeUpdate();

	        return affectedRows > 0;
		} catch(SQLException  e) {
			System.out.println(e.getMessage());
		}	catch (Exception e) {
	        System.out.println("❌ Unexpected Error: " + e.getMessage());
	    }
		return false;
	}

}
