package service;

import java.sql.SQLException;
import java.util.List;

import dao.AdminDao;
import model.Admin;
import model.Item;

public class AdminService {
	AdminDao dao = new AdminDao();
	public List<Item> getAllItem() throws SQLException {
		return dao.getAllItem();
	}
	
	public List<Item> getLostItem() throws SQLException {
		return dao.getLostItem();
	}
	
	public List<Item> getFoundItem() throws SQLException {
		return dao.getFoundItem();
	}
	
	public boolean validateAdmin(String enteredPassword) throws SQLException {
	    Admin storedAdmin = dao.getAdminPassword();
	    if (storedAdmin == null) {
	        System.out.println("Admin password not set in DB.");
	        return false;
	    }
	    return enteredPassword.equals(storedAdmin.getPassword());
	    }

	public boolean deleteById(int deleteId) throws SQLException {
		return dao.deleteById(deleteId);
		
	}
}
