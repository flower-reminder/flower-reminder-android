package lt.andro.flowr.location.entity.foursquare;

import java.util.ArrayList;
import java.util.List;

import lt.andro.flowr.location.FlowerShopsLocations;
import lt.andro.flowr.location.entity.ShopLocation;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 23:17.
 */
public class FoursquareVenues {
    public List<Venue> venues;

    public List<ShopLocation> getLocations() {
        List<ShopLocation> shops = new ArrayList<>();
        for (Venue venue : venues)
            shops.add(createShopLocation(venue));
        return shops;
    }

    private ShopLocation createShopLocation(final Venue venue) {
        return new ShopLocation() {
            @Override
            public String getName() {
                return venue.getName();
            }

            @Override
            public float getLatitude() {
                return venue.getLatitude();
            }

            @Override
            public float getLongitude() {
                return venue.getLongitude();
            }

            @Override
            public int getId() {
                return FlowerShopsLocations.getInstance().getLocations().indexOf(this);
            }
        };
    }
}
