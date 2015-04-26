package lt.andro.flowr;

import android.app.Application;

import java.util.List;

import lt.andro.flowr.location.FlowerShopsLocations;
import lt.andro.flowr.location.entity.ShopLocation;
import timber.log.Timber;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 04:34.
 */
public class MainApplication extends Application {
    public static Application context;
    public static List<ShopLocation> locations;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        locations = FlowerShopsLocations.getInstance().getLocations();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
