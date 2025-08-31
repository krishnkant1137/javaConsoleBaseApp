package service;

import dao.ItemDao;
import dao.OnlyConnection;
import model.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchingService {

	OnlyConnection connProvider = new OnlyConnection();
    private final ItemDao itemDao;
    private static final double MATCH_THRESHOLD = 0.6; // 60% match required
    private static final double CATEGORY_BOOST = 0.1; // +10% boost if category matches
    private static final double LOCATION_BOOST = 0.1; // +10% boost if location matches

    public MatchingService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    /**
     * Finds matching items from the database for a given item.
     * @param newItem The newly reported item (lost or found)
     * @return List of items that match the criteria
     * @throws SQLException 
     */
    public List<Item> findMatchingItems(Item newItem) throws SQLException {
        List<Item> allItems = itemDao.getAllItems(); // Fetch all items from DB
        List<Item> matches = new ArrayList<>();

        for (Item existingItem : allItems) {
            // Lost matches with found, and found matches with lost
        	
            if (!existingItem.getStatus().equalsIgnoreCase(newItem.getStatus())) {

                double score = calculateMatchScore(newItem, existingItem);

                if (score >= MATCH_THRESHOLD) {
                    matches.add(existingItem);
                }
            }
        }

        return matches;
    }

    /**
     * Calculates similarity score between two items
     */
    private double calculateMatchScore(Item item1, Item item2) {
        double nameSimilarity = stringSimilarity(
            safeString(item1.getItemName()), 
            safeString(item2.getItemName())
        );

        double descSimilarity = stringSimilarity(
            safeString(item1.getDescription()), 
            safeString(item2.getDescription())
        );

        double avgScore = (nameSimilarity + descSimilarity) / 2;

        // Boost score if category matches (case-insensitive)
        if (safeEqualsIgnoreCase(item1.getItemType(), item2.getItemType())) {
            avgScore += CATEGORY_BOOST;
        }

        // Boost score if location matches (case-insensitive)
        if (safeEqualsIgnoreCase(item1.getLocationFound(), item2.getLocationFound())) {
            avgScore += LOCATION_BOOST;
        }

        return Math.min(avgScore, 1.0); // Max score 1.0
    }

    // Helper: safe null-to-empty
    private String safeString(String str) {
        return (str == null) ? "" : str;
    }

    // Helper: safe equalsIgnoreCase
    private boolean safeEqualsIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    /**
     * Calculates string similarity using Levenshtein distance
     */
    private double stringSimilarity(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());

        return (maxLength == 0) ? 1.0 : (1.0 - (double) distance / maxLength);
    }

    /**
     * Levenshtein Distance Algorithm
     */
    private int levenshteinDistance(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];

        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[s2.length()];
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

}
