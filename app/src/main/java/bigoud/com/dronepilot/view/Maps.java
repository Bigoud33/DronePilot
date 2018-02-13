package bigoud.com.dronepilot.view;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;

import bigoud.com.dronepilot.R;
import dji.internal.cache.component.FlightController;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener
{
    public class PhotoPos
    {
        public LatLng position;
        public Marker marker;
    }

    private GoogleMap mMap;
    private FusedLocationProviderClient mLocation;

    private double droneLocationLat = 181, droneLocationLng = 181;
    private Marker droneMarker = null;
    private FlightController mFlightController;

    private volatile ArrayList<PhotoPos> positions = new ArrayList();
    private Polygon polygon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mLocation = LocationServices.getFusedLocationProviderClient(this);

            mLocation.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task)
                {
                    if (task.isSuccessful() && task.getResult() != null)
                    {
                        Location res = task.getResult();

                        CameraUpdate move = CameraUpdateFactory.newLatLng(new LatLng(res.getLatitude(),
                                res.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

                        mMap.moveCamera(move);
                        mMap.animateCamera(zoom);
                    }
                    else
                    {
                        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                    }
                }
            });
        }
        else
        {
            LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        PhotoPos pos = new PhotoPos();
        pos.marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        pos.position = latLng;
        positions.add(pos);
        refreshPolygon();
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        for(PhotoPos i : positions)
        {
            if(i.marker.getId().equals(marker.getId()))
            {
                positions.remove(i);
                break;
            }
        }

        marker.remove();
        refreshPolygon();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {

    }

    @Override
    public void onMarkerDrag(Marker marker)
    {
        for(PhotoPos i : positions)
        {
            if(i.marker.getId().equals(marker.getId()))
            {
                i.position = marker.getPosition();
            }
        }

        refreshPolygon();
    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
        for(PhotoPos i : positions)
        {
            if(i.marker.getId().equals(marker.getId()))
            {
                i.position = marker.getPosition();
            }
        }

        refreshPolygon();
    }

    private void refreshPolygon()
    {
        for(int i = 0; i < positions.size() - 1; i++)
        {
            int minPos = i + 1;
            double min = calcDistance(positions.get(i).position, positions.get(minPos).position);

            for(int j = i + 2; j < positions.size(); j++)
            {
                double val = calcDistance(positions.get(i).position, positions.get(j).position);
                if(val < min)
                {
                    min = val;
                    minPos = j;
                }
            }

            if(minPos != i + 1)
                Collections.swap(positions, i + 1, minPos);
        }

        if(polygon != null)
        {
            polygon.remove();
            polygon = null;
        }

        if(positions.size() > 1)
        {
            PolygonOptions opt = new PolygonOptions();
            for(PhotoPos i : positions)
                opt = opt.add(i.position);

            polygon = mMap.addPolygon(opt);
        }
    }

    private double calcDistance(LatLng pos1, LatLng pos2)
    {
        return Math.sqrt(Math.pow(pos2.latitude - pos1.latitude, 2) + Math.pow(pos2.longitude - pos1.longitude, 2));
    }
}
