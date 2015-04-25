package lt.andro.flowr;

import android.app.Application;

import timber.log.Timber;

/**
 * @author Vilius Kraujutis
 * @since 2015-04-25 04:34.
 */
public class MainApplication extends Application {
public static    Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
