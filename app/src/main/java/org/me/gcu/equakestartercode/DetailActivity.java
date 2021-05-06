package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    card card;
    TextView Date, Location, Magnitude, Depth, LatLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        card = getIntent().getParcelableExtra("card");

        Date = findViewById(R.id.date);
        Location = findViewById(R.id.location);
        Magnitude = findViewById(R.id.magnitude);
        Depth = findViewById(R.id.depth);
        LatLong = findViewById(R.id.latlon);

        Date.setText("Date: " + card.getPubDate());
        Location.setText("Location: " + card.getLocation());
        Magnitude.setText("Magnitude: " + card.getMagnitude());
        Depth.setText("Depth: " + card.getDepth() + " km");
        LatLong.setText("Latitude, Longitude: " + card.getLatitude() + ", " + card.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(card.getLatitude(), card.getLongitude());
        googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(card.getLocation())
                        .icon(getMarkerIcon(getPinColor(card.getMagnitude()))));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


    public int getPinColor(double magnitude) {
        if (magnitude >= 2.6) {
            return ContextCompat.getColor(DetailActivity.this, R.color.strong);
        } else if (magnitude <= 2.5 && magnitude >= 1.6) {
            return ContextCompat.getColor(DetailActivity.this, R.color.moderate);
        } else if (magnitude <= 1.5 && magnitude >= 0.5) {
            return ContextCompat.getColor(DetailActivity.this, R.color.light);
        } else {
            return ContextCompat.getColor(DetailActivity.this, R.color.minor);
        }
    }
}
