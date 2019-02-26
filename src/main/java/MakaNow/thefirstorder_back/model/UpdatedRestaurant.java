package MakaNow.thefirstorder_back.model;

import lombok.Data;

@Data
public class UpdatedRestaurant {
    private String restaurantId;
    private String restaurantName;
    private String restaurantDescription;
    private String contactNumber;
    private String building;
    private String street;
    private String postalCode;
    private String cuisine;
    private String operatingHours;
    private String affordability;
    private String restaurantImg;

    public UpdatedRestaurant(String restaurantId,
                             String restaurantName,
                             String restaurantDescription,
                             String contactNumber,
                             String building,
                             String street,
                             String postalCode,
                             String cuisine,
                             String operatingHours,
                             String affordability,
                             String restaurantImg){
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantDescription = restaurantDescription;
        this.contactNumber = contactNumber;
        this.building = building;
        this.street = street;
        this.postalCode = postalCode;
        this.cuisine = cuisine;
        this.operatingHours = operatingHours;
        this.affordability = affordability;
        this.restaurantImg = restaurantImg;
    }

    public UpdatedRestaurant(Restaurant restaurant){
        this(restaurant.getRestaurantId(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantDescription(),
                restaurant.getRestaurantContactNumber(),
                restaurant.getBuilding(),
                restaurant.getStreet(),
                restaurant.getPostalCode(),
                restaurant.getCuisine(),
                restaurant.getRestaurantOpeningHours(),
                restaurant.getRestaurantPriceRange(),
                new String(restaurant.getRestaurantImgPath()));
    }
}
