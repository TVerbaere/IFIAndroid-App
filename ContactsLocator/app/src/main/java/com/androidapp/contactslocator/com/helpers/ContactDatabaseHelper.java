package com.androidapp.contactslocator.com.helpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.model.ContactPOJO;
import com.androidapp.contactslocator.com.model.DefaultLocation;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * @author Thibaud VERBAERE
 * Classe Utile pour la base de données.
 */

public class ContactDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<ContactPOJO, Long> contacts;
    private Dao<DefaultLocation, Long> default_loc;

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                R.raw.ormlite_config);
    }

    /**
     * Méthode appelée lors de la création.
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // On créé une table contenant les contacts.
            TableUtils.createTable(connectionSource, ContactPOJO.class);
            TableUtils.createTable(connectionSource, DefaultLocation.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode appelée lors de changements.
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            // On supprime la table et on la recréée
            TableUtils.dropTable(connectionSource, ContactPOJO.class, false);
            TableUtils.dropTable(connectionSource, DefaultLocation.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne les instances de contacts en base.
     * @return
     * @throws SQLException
     */
    public Dao<ContactPOJO, Long> getDao() throws SQLException {
        if(contacts == null) {
            contacts = getDao(ContactPOJO.class);
        }
        return contacts;
    }

    /**
     * Retourne les instances de defaultlocation en base (cad une instance).
     * @return
     * @throws SQLException
     */
    public Dao<DefaultLocation, Long> getLocDao() throws SQLException {
        if(default_loc == null) {
            default_loc = getDao(DefaultLocation.class);
        }
        return default_loc;
    }

    /**
     * Retourne la position par défaut contenue en base.
     * @return
     * @throws SQLException
     */
    public DefaultLocation getDefaultLocation() throws SQLException {
        if(default_loc == null) {
            default_loc = getDao(DefaultLocation.class);
        }
        return default_loc.queryForId(new Long(1));
    }
}

