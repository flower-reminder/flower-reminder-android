package lt.andro.flowr.entity;

import lt.andro.flowr.FlowerShopsLocations;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 05:34.
 */
public class MyShopLocation implements ShopLocation {
    public String name;
    public String address;
    public float latitude;
    public float longitude;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getLatitude() {
        return latitude;
    }

    @Override
    public float getLongitude() {
        return longitude;
    }

    @Override
    public int getId() {
        return FlowerShopsLocations.getInstance().getLocations().indexOf(this);
    }
}
