package com.androidapp.contactslocator.com.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.helpers.ContactDatabaseHelper;
import com.androidapp.contactslocator.com.helpers.Tools;
import com.androidapp.contactslocator.com.model.ContactPOJO;
import com.androidapp.contactslocator.com.model.DefaultLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;


/**
 * @author Thibaud VERBAERE
 * Classe pour l'activité se chargeant de localiser un contact sur une map.
 */

public class LocalisationActivity extends AppCompatActivity {

    private Button button_me;
    private Button button_other;

    private LatLng me;
    private LatLng pos;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        button_me = (Button)findViewById(R.id.button_me);
        button_other = (Button)findViewById(R.id.button_other);

        // On récupère le contact à localiser.
        ContactPOJO contact = ((ContactPOJO) getIntent().getExtras().getSerializable("contact"));

        button_other.setText(contact.getName());

        // On récupère les informations sur le contact.
        ((TextView) findViewById(R.id.name_contact)).setText(contact.getFullName());
        ((TextView) findViewById(R.id.address_contact)).setText(contact.getFullAddress());
        ((TextView) findViewById(R.id.phone_contact)).setText(contact.getPhone());

        // On créé la map.
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFrag.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);

        // On créé la position du contact.
        double lat = contact.getLat();
        double lng = contact.getLng();
        String title = contact.getFullName();
        pos = new LatLng(lat, lng);

        // On essaye de créer la position de l'utilisateur.
        UpdateLocation();

        ((TextView) findViewById(R.id.dist_contact)).setText(getString(R.string.distance) + " " + Tools.printDistance(me,pos));

        // On créé les marqueurs sur la map.
        map.addMarker(new MarkerOptions().position(pos).title(title));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        map.addMarker(new MarkerOptions().position(me).icon(bitmapDescriptor).title(getString(R.string.Me)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

        // Listener pour le bouton de zoom sur sa position.
        button_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 15));
            }
        });

        // Listener pour le bouton de zoom sur la position du contact.
        button_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Met a jour la position de l'utilisateur (localisation GPS du téléphone).
     */
    public void UpdateLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            me = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (SecurityException e) {
            ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(LocalisationActivity.this, ContactDatabaseHelper.class);
            try {
                DefaultLocation def = datahelper.getDefaultLocation();
                me = new LatLng(def.getDeflat(),def.getDeflng());
            }
            catch (Exception e2) {
                me = new LatLng(0,0);
            }
        }
    }

}
