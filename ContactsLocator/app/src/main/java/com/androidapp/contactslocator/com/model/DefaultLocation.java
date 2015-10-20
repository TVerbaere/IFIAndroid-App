package com.androidapp.contactslocator.com.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Thibaud VERBAERE
 * Classe permettant la position par défaut de l'utilisateur en cas de soucis avec le réseau (pour la géolocalisation)
 *  -> permet de rendre l'application tout à fait fonctionnelle même sans la géolocalisation du téléphone.
 */

@DatabaseTable(tableName = "tablelocation")
public class DefaultLocation {

    @DatabaseField(columnName = "id", generatedId = true)
    private long id; // Identifiant
    @DatabaseField(columnName = "deflat", canBeNull = false)
    private double deflat; // Latitude permettant la localisation.
    @DatabaseField(columnName = "deflng", canBeNull = false)
    private double deflng; // Longitude permettant la localisation.

    public DefaultLocation() {}

    /**
     * Permet de créer une nouvelle position par défaut.
     * @param lat la latitude de cette position
     * @param lng la longitude de cette position
     */
    public DefaultLocation(double lat, double lng) {
        this.deflat = lat;
        this.deflng = lng;
    }

    /**
     * Retourne l'identifiant de la position par défaut (dans notre cas il n'y en aura qu'une id = 1)
     * @return 1
     */
    public long getId() {
        return id;
    }

    /**
     * Permet de changer l'identifiant de la position par défaut.
     * @param id le nouvel identifiant
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retourne la longitude de la position par défaut.
     * @return la longitude
     */
    public double getDeflng() {
        return deflng;
    }

    /**
     * Permet de modifier la longitude de la position par défaut.
     * @param deflng la nouvelle longitude
     */
    public void setDeflng(double deflng) {
        this.deflng = deflng;
    }

    /**
     * Retourne la latitude de la position par défaut.
     * @return la latitude
     */
    public double getDeflat() {
        return deflat;
    }

    /**
     * Permet de modifier la latitude de la position par défaut.
     * @param deflat la nouvelle latitude
     */
    public void setDeflat(double deflat) {
        this.deflat = deflat;
    }
}
