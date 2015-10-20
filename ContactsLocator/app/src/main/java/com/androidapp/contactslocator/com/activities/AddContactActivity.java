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
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.helpers.ContactDatabaseHelper;
import com.androidapp.contactslocator.com.model.ContactPOJO;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Thibaud VERBAERE
 * Classe pour l'activité se chargeant d'ajouter un contact (formulaire de contact + traitement).
 */

public class AddContactActivity extends AppCompatActivity {

    private EditText edit_street;
    private EditText edit_code;
    private EditText edit_town;
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_surname;
    private Button button_create;
    private Button button_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Si l'on a des paramètres lors du démarrage de l'activité alors c'est une modification de contact.
        if (getIntent().getExtras() != null) {

            // Dans ce cas on récupère le contact envoyé et on remplit les champs par les valeurs adéquates.
            ContactPOJO contact = ((ContactPOJO) getIntent().getExtras().getSerializable("contact"));
            ((EditText) findViewById(R.id.Addphone)).setText(contact.getPhone());
            ((EditText) findViewById(R.id.AddTown)).setText(contact.getTown());
            ((EditText) findViewById(R.id.AddCode)).setText(contact.getCode());
            ((EditText) findViewById(R.id.AddStreet)).setText(contact.getStreet());
            ((EditText) findViewById(R.id.Addname)).setText(contact.getName());
            ((EditText) findViewById(R.id.Addsurname)).setText(contact.getSurname());
            // On modifie aussi à la volée le texte du titre et du bouton.
            ((TextView) findViewById(R.id.form_contactTitle)).setText(getIntent().getExtras().getString("title"));
            ((Button) findViewById(R.id.Create)).setText(R.string.ButtonModidy);
        }

        button_create = (Button) findViewById(R.id.Create);
        button_reset = (Button) findViewById(R.id.Reset);

        // Listener du bouton d'ajout.
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

        // Listener du bouton de remise à zéro.
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData();
            }
        });

    }

    /**
     * Permet de remettre les champs du formulaire à zéro.
     */
    public void resetData() {
        edit_code.setText(null);
        edit_town.setText(null);
        edit_street.setText(null);
        edit_name.setText(null);
        edit_surname.setText(null);
        edit_phone.setText(null);
    }

    public void addData() {
        // On créé le helper pour la base.
        ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(AddContactActivity.this, ContactDatabaseHelper.class);
        CharSequence text = getString(R.string.AddContactOK);

        try {
            Dao<ContactPOJO, Long> contactDao = datahelper.getDao();

            // On récupère les champs pour pouvoir les ajouter en base.
            edit_street = (EditText) findViewById(R.id.AddStreet);
            edit_code = (EditText) findViewById(R.id.AddCode);
            edit_town = (EditText) findViewById(R.id.AddTown);
            edit_name = (EditText) findViewById(R.id.Addname);
            edit_phone = (EditText) findViewById(R.id.Addphone);
            edit_surname = (EditText) findViewById(R.id.Addsurname);

            // On vérifie d'abord que les champs sont valides (cad. non vides).
            // En cas d'invalidité on va envoyer un message d'erreur.
            if (edit_surname.length() == 0)
                text = getString(R.string.SurnameKO);
            else
                if (edit_name.length() == 0)
                    text = getString(R.string.NameKO);
                else
                    if (edit_street.length() == 0)
                        text = getString(R.string.StreetKO);
                    else
                        if (edit_code.length() == 0)
                            text = getString(R.string.CodeKO);
                        else
                            if (edit_town.length() == 0)
                                text = getString(R.string.TownKO);
                            else
                                if (edit_phone.length() == 0)
                                    text = getString(R.string.PhoneKO);

            // Si le message n'a pas été mis à jour alors tout est valide.
            if (text == getString(R.string.AddContactOK)) {
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
                // On créé le nouveau contact.
                ContactPOJO cnew = new ContactPOJO(edit_surname.getText().toString(), edit_name.getText().toString(),
                        edit_street.getText().toString(), edit_town.getText().toString(), edit_code.getText().toString(), edit_phone.getText().toString(), lat, lng);

                // Si on a des paramètres alors on oublie pas de modifier l'identifiant pour supprimer l'ancienne valeur.
                if (getIntent().getExtras() != null) {
                    cnew.setId(((ContactPOJO)getIntent().getExtras().getSerializable("contact")).getId());
                }

                contactDao.createOrUpdate(cnew);
                AddContactActivity.this.finish();
            }

            // On envoie le message.
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();

        }
        catch (SQLException e) {

        }
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
}
