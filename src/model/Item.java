package model;

import java.time.LocalDate;

public class Item {
    private int itemId;
    private int userId;
    private String itemName;
    private String itemType;
    private String description;
    private String locationLost;
    private String locationFound;
    private String status; // "Lost" or "Found"
    private LocalDate reportedDate;
    private String contactInfo;

    // Constructors
    public Item() {
    }

    public Item(int userId, String itemName, String itemType, String description,
                String locationLost,String locationFound, String status, LocalDate reportedDate, String contactInfo) {
        this.userId = userId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.description = description;
        this.locationLost = locationLost;
        this.locationFound = locationFound;
        this.status = status;
        this.reportedDate = reportedDate;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationLost() {
        return locationLost;
    }

    public void setLocationLost(String locationLost) {
        this.locationLost = locationLost;
    }
    
    public String getLocationFound() {
        return locationFound;
    }

    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // toString (optional, for debugging)
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", userId=" + userId +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", description='" + description + '\'' +
                ", locationLost='" + locationLost + '\'' +
                ", locationLost='" + locationFound + '\'' +
                ", status='" + status + '\'' +
                ", reportedDate=" + reportedDate +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
