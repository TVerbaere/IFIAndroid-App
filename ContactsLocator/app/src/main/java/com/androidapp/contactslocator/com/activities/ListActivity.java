package com.androidapp.contactslocator.com.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.helpers.ContactAdapter;
import com.androidapp.contactslocator.com.helpers.ContactAdapterListener;
import com.androidapp.contactslocator.com.helpers.ContactDatabaseHelper;
import com.androidapp.contactslocator.com.model.ContactPOJO;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Thibaud VERBAERE
 * Classe pour l'activité qui liste les contacts (activité principale).
 */

public class ListActivity extends AppCompatActivity implements ContactAdapterListener {

    private Button button_add;
    private Button button_locator;
    private Dao<ContactPOJO, Long> contactDao;

    // Les différents modes :
    final int DELETE = 2;
    final int MODIFY = 1;
    final int LOCAL = 3;
    final int CANCEL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        button_add = (Button) findViewById(R.id.button_add);
        button_locator = (Button) findViewById(R.id.button_locator);

        // On fabrique la liste de contacts.
        list();

        // Listener pour le bouton de création de contact.
        button_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });

        // Listener pour le bouton qui redirige vers l'activité de localisation.
        button_locator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this,LocatorActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet de lister les contacts figurant dans la base de données.
     */
    public void list() {

        // On crée le helper pour la base de données.
        List<ContactPOJO> contacts = new ArrayList<ContactPOJO>();
        ContactDatabaseHelper datahelper = OpenHelperManager.getHelper(this, ContactDatabaseHelper.class);

        try {
            // On récupère les contacts et on les tris.
            contactDao = datahelper.getDao();
            contacts = contactDao.queryForAll();
            Collections.sort(contacts);
        }
        catch (SQLException e) {

        }

        // Création de l'adapter.
        ContactAdapter adapter = new ContactAdapter(this, contacts);
        adapter.addListener(this);
        ListView list = (ListView)findViewById(R.id.contact_listView);
        list.setAdapter(adapter);

        TextView info = (TextView)findViewById(R.id.contact_info);

        // Si l'on a pas de contacts on l'écrit sinon on marque le nombre sur le bas de page.
        if (contacts.isEmpty()) {
            info.setText(getString(R.string.noContacts));
        }
        else {
            info.setText(contacts.size()+" "+getString(R.string.Contacts)+".");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // On recréé la liste lors de reprise de l'activité.
        list();
    }

    @Override
    public void onClickContact(final ContactPOJO item, int position) {
        // On créé le menu contextuel.
        CharSequence[] items = {getString(R.string.Cancel), getString(R.string.Modify), getString(R.string.Delete), getString(R.string.Local)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.getSurname() + " " + item.getName());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            // On place un listener.
            public void onClick(DialogInterface dialog, int num_menu) {
                doAction(num_menu, item);
            }

        });

        // On fait apparaitre le menu.
        builder.show();
    }

    public void doAction(int item,ContactPOJO contact) {

        try {
            // Cancel : on ne fait rien.
            if (item == CANCEL)
                ;
            // Delete : on supprime le contact.
            else if (item == DELETE) {
                contactDao.delete(contact);
                onResume();
            }
            // Modify : on créé l'activité avec le formulaire d'ajout et il faut envoyer le contact en paramètre.
            else if (item == MODIFY) {
                Intent intent = new Intent(ListActivity.this,AddContactActivity.class);

                Bundle b = new Bundle();
                b.putSerializable("contact",contact);
                b.putString("title",getString(R.string.ModifyContactTitle));
                intent.putExtras(b);

                startActivity(intent);

            }
            // Local : on créé l'activité de localisation du contact et il faut également envoyer le contact en question en paramètre.
            else if (item == LOCAL) {
                Intent intent = new Intent(ListActivity.this,LocalisationActivity.class);

                Bundle b = new Bundle();
                b.putSerializable("contact", contact);
                intent.putExtras(b);

                startActivity(intent);
            }

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
