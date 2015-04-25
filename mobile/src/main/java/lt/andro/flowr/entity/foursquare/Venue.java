package lt.andro.flowr.entity.foursquare;

import lt.andro.flowr.FlowerShopsLocations;
import lt.andro.flowr.entity.ShopLocation;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 23:18.
 */
public class Venue implements ShopLocation {
    public String id;
    public String name;
    public FoursquareLocation location;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getLatitude() {
        return location.lat;
    }

    @Override
    public float getLongitude() {
        return location.lng;
    }

    @Override
    public int getId() {
        return FlowerShopsLocations.getInstance().getLocations().indexOf(this);
    }
}
