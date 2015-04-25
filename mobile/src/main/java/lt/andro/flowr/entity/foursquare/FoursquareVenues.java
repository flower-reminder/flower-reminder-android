package lt.andro.flowr.entity.foursquare;

import java.util.List;

import lt.andro.flowr.entity.ShopLocation;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 23:17.
 */
public class FoursquareVenues {
    public List<Venue> venues;

    public List<? extends ShopLocation> getLocations() {
        return venues;
    }
}
