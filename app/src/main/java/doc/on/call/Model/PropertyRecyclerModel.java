package doc.on.call.Model;

/**
 * Created by wolfsoft1 on 8/3/18.
 */

public class PropertyRecyclerModel {

    String propertyName,street1,street2,amount,bedCount,carParking,swimmingpool;

    public PropertyRecyclerModel(String propertyName, String street1, String street2, String amount, String bedCount, String carParking, String swimmingpool) {
        this.propertyName = propertyName;
        this.street1 = street1;
        this.street2 = street2;
        this.amount = amount;
        this.bedCount = bedCount;
        this.carParking = carParking;
        this.swimmingpool = swimmingpool;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBedCount() {
        return bedCount;
    }

    public void setBedCount(String bedCount) {
        this.bedCount = bedCount;
    }

    public String getCarParking() {
        return carParking;
    }

    public void setCarParking(String carParking) {
        this.carParking = carParking;
    }

    public String getSwimmingpool() {
        return swimmingpool;
    }

    public void setSwimmingpool(String swimmingpool) {
        this.swimmingpool = swimmingpool;
    }
}
