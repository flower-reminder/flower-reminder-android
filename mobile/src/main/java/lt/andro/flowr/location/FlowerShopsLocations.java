package lt.andro.flowr.location;

import java.util.List;

import lt.andro.flowr.R;
import lt.andro.flowr.location.entity.ShopLocation;
import lt.andro.flowr.location.entity.foursquare.FoursquareVenues;
import lt.andro.flowr.utils.FileUtils;
import lt.andro.flowr.utils.GsonUtils;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 05:13.
 */
public class FlowerShopsLocations {

    static transient FlowerShopsLocations instance;
    private final List<ShopLocation> locations;

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

    public List<ShopLocation> getLocations() {
        return locations;
    }
}
