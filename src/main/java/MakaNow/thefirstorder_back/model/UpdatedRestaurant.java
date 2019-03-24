package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class UpdatedRestaurant {
    @JsonView(View.MainView.class)
    private String restaurantId;

    @JsonView(View.MainView.class)
    private String restaurantName;

    @JsonView(View.MainView.class)
    private String restaurantDescription;

    @JsonView(View.MainView.class)
    private String contactNumber;

    @JsonView(View.MainView.class)
    private String building;

    @JsonView(View.MainView.class)
    private String street;

    @JsonView(View.MainView.class)
    private String postalCode;

    @JsonView(View.MainView.class)
    private String cuisine;

    @JsonView(View.MainView.class)
    private String operatingHours;

    @JsonView(View.MainView.class)
    private String affordability;

    @JsonView(View.MainView.class)
    private String restaurantImg;

    @JsonView(View.MainView.class)
    private boolean gst;

    @JsonView(View.MainView.class)
    private boolean serviceCharge;

    @JsonView(View.MainView.class)
    private double moneyToPointsConversionRate;

    @JsonView(View.MainView.class)
    private double pointsToMoneyConversionRate;

    public UpdatedRestaurant(Restaurant restaurant){
        byte[] img = restaurant.getRestaurantImgPath();
        String image = "";
        if(img != null){
            image = new String(img);
        }

        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.restaurantDescription = restaurant.getRestaurantDescription();
        this.contactNumber = restaurant.getRestaurantContactNumber();
        this.building = restaurant.getBuilding();
        this.street = restaurant.getStreet();
        this.postalCode = restaurant.getPostalCode();
        this.cuisine = restaurant.getCuisine();
        this.operatingHours = restaurant.getRestaurantOpeningHours();
        this.affordability = restaurant.getRestaurantPriceRange();
        this.restaurantImg = image;
        this.gst = restaurant.getGst();
        this.serviceCharge = restaurant.getServiceCharge();
        this.moneyToPointsConversionRate = restaurant.getMoneyToPointsConversionRate();
        this.pointsToMoneyConversionRate = restaurant.getPointsToMoneyConversionRate();
    }
}
