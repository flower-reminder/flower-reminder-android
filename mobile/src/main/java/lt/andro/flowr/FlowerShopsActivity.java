package lt.andro.flowr;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lt.andro.flowr.entity.ShopLocation;
import timber.log.Timber;

public class FlowerShopsActivity extends FragmentActivity implements ResultCallback<Status> {

    public static final int RADIUS = 200;
    @InjectView(R.id.activity_flower_shops_enable_button)
    ToggleButton enableToggleButton;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;
    private LocationRequest mLocationRequest;
    private Marker lastLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_shops);

        setUpMapIfNeeded();
        buildGoogleApiClient();
        ButterKnife.inject(this);

        createGeofenceList();
        createLocationRequest();

        enableToggleButton.setChecked(true);
        enableToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("isChecked", isChecked);
                if (isChecked) {
                    addGeofences();
                } else {
                    removeGeofences();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_flower_shops_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        List<? extends ShopLocation> locations = FlowerShopsLocations.getInstance().getLocations();
        for (ShopLocation location : locations) {
            float lat = location.getLatitude();
            float lon = location.getLongitude();
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                    .anchor(.5f, 1.0f)
                    .position(new LatLng(lat, lon)));
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .radius(RADIUS)
                    .strokeColor(0)
                    .fillColor(getResources().getColor(R.color.area_fill)));
        }

        LatLng vilnius = new LatLng(54.694021, 25.277058);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vilnius, 16));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Timber.d("connected");
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        showLastLocation();
                        monitorLocationChanges();
                        addGeofences();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Timber.d("suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Timber.d("failed");
                    }
                })
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void createGeofenceList() {
        List<? extends ShopLocation> locations = FlowerShopsLocations.getInstance().getLocations();
        for (ShopLocation location : locations) {
            float lat = location.getLatitude();
            float lon = location.getLongitude();
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(Integer.toString(location.getId()))
                    .setCircularRegion(lat, lon, RADIUS)
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void addGeofences() {
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    private void removeGeofences() {
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                // This is the same pending intent that was used in addGeofences().
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().
    }

    private void showLastLocation() {
        if (mLastLocation != null) {
            if (mMap != null) {
                if (lastLocationMarker != null) lastLocationMarker.remove();
                lastLocationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Last known location"));
            }
        }
    }

    private void monitorLocationChanges() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) return;

                mLastLocation = location;
                showLastLocation();
            }
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {
        Timber.d("onResult");
    }
}
