package lt.andro.flowr;

import java.util.List;

import lt.andro.flowr.entity.ShopLocation;
import lt.andro.flowr.entity.foursquare.FoursquareVenues;
import lt.andro.flowr.utils.FileUtils;
import lt.andro.flowr.utils.GsonUtils;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 05:13.
 */
public class FlowerShopsLocations {

    static transient FlowerShopsLocations instance;
    private final List<? extends ShopLocation> locations;

    public FlowerShopsLocations() {
//        String locationsJson = FileUtils.readFile(R.raw.shop_locations);
//        locations = GsonUtils.gson.fromJson(locationsJson, ShopLocationsEntity.class).locations;

        String locationsJson = FileUtils.readFile(R.raw.foursquare_venues);
        locations = GsonUtils.gson.fromJson(locationsJson, FoursquareVenues.class).getLocations();
    }

    public static FlowerShopsLocations getInstance() {
        if (instance == null) {
            instance = new FlowerShopsLocations();
        }
        return instance;
    }

    public List<? extends ShopLocation> getLocations() {
        return locations;
    }
}
