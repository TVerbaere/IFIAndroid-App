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
import android.widget.EditText;

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
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;


/**
 * @author Thibaud VERBAERE
 * Classe pour l'activité se chargeant de localiser les contacts dans un périmètre spécifié
 * par l'utilisateur.
 */

public class LocatorActivity extends AppCompatActivity {

    private GoogleMap map;
    private LatLng me;
    private Button button_launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        button_launch = (Button)findViewById(R.id.button_launch);

        // Configuration de la map.
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        map = mapFrag.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);

        // On positionne un pointeur pour la position de l'utilisateur.
        UpdateLocation();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        map.addMarker(new MarkerOptions().position(me).icon(bitmapDescriptor).title(getString(R.string.Me)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 13));


        button_launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TODO();
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
            ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(LocatorActivity.this, ContactDatabaseHelper.class);
            try {
                DefaultLocation def = datahelper.getDefaultLocation();
                me = new LatLng(def.getDeflat(),def.getDeflng());
            }
            catch (Exception e2) {
                me = new LatLng(0,0);
            }
        }
    }

    public void TODO() {
        // On créé un helpeur pour la base.
        ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(LocatorActivity.this, ContactDatabaseHelper.class);

        // On retire les marqueurs de la map et on la remet à jour.
        map.clear();
        UpdateLocation();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        map.addMarker(new MarkerOptions().position(me).icon(bitmapDescriptor).title(getString(R.string.Me)));

        EditText edit = (EditText)findViewById(R.id.edit_km);

        // Si l'utilisateur à saisit une valeur on doit placer les marqueurs sur la carte.
        if (edit.length() != 0) {
            try {
                double value = Double.parseDouble(edit.getText().toString());
                
                Dao<ContactPOJO, Long> contactDao = datahelper.getDao();
                CloseableIterator<ContactPOJO> iterator = contactDao.iterator();

                // Pour chaque contact en base on regarde sa position par rapport à celle de l'utilisateur.
                while (iterator.hasNext()) {
                    ContactPOJO contact = iterator.next();
                    LatLng pos_contact = new LatLng(contact.getLat(), contact.getLng());
                    double dist = Tools.distanceBetween(me,pos_contact);

                    // On peux placer un marqueur si la distance est plus petit à celle saisie.
                    if (dist <= value) {
                        String title = contact.getFullName();
                        map.addMarker(new MarkerOptions().position(pos_contact).title(title));
                    }
                }
                        
            } catch (Exception e) {

            }
        }
    }
}
