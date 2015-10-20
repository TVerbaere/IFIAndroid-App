package com.androidapp.contactslocator.com.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.helpers.ContactDatabaseHelper;
import com.androidapp.contactslocator.com.model.DefaultLocation;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Thibaud VERBAERE
 * Classe se chargeant de proposer des paramètrages utilisateur en cas de soucis avec la géolocalisation.
 */
public class SettingsActivity extends AppCompatActivity {

    private EditText edit_street;
    private EditText edit_code;
    private EditText edit_town;

    private Button saveDefLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveDefLoc = (Button) findViewById(R.id.DefaultLocation);

        saveDefLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
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
     * Permet de sauvegarder la nouvelle position par défaut.
     */
    public void save() {
        // On créé le helper pour la base.
        ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(SettingsActivity.this, ContactDatabaseHelper.class);
        CharSequence text = getString(R.string.SaveOK);

        try {
            Dao<DefaultLocation, Long> locationDao = datahelper.getLocDao();

            // On récupère les champs pour pouvoir les ajouter en base.
            edit_street = (EditText) findViewById(R.id.AddStreet);
            edit_code = (EditText) findViewById(R.id.AddCode);
            edit_town = (EditText) findViewById(R.id.AddTown);

            // On vérifie d'abord que les champs sont valides (cad. non vides).
            // En cas d'invalidité on va envoyer un message d'erreur.

            if (edit_street.length() == 0)
                text = getString(R.string.StreetKO);
            else
            if (edit_code.length() == 0)
                text = getString(R.string.CodeKO);
            else
            if (edit_town.length() == 0)
                text = getString(R.string.TownKO);


            // Si le message n'a pas été mis à jour alors tout est valide.
            if (text == getString(R.string.SaveOK)) {
                double lat = 0;
                double lng = 0;
                try {
                    // On essaye de remplir les champs latitude et longitude de la base grâce à l'API Geocoder.
                    Geocoder gc=new Geocoder(this);
                    List<Address> addresses;
                    addresses = gc.getFromLocationName(edit_street.getText().toString() + " " + edit_code.getText().toString() + " " + edit_town.getText().toString(), 1);
                    lat = addresses.get(0).getLatitude();
                    lng = addresses.get(0).getLongitude();
                }
                catch (IOException e) {

                }

                //

                // On créé le nouveau contact.
                DefaultLocation defloc = new DefaultLocation(lat,lng);

                // Si on a déja une position par défaut on la change donc on doit lui donner l'id 1.
                if (locationDao.countOf() != 0) {
                    defloc.setId(1);
                }

                locationDao.createOrUpdate(defloc);
                SettingsActivity.this.finish();
            }

            // On envoie le message.
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();

        }
        catch (SQLException e) {

        }
    }
}
