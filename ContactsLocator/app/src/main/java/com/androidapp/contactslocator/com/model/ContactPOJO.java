package com.androidapp.contactslocator.com.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author Thibaud VERBAERE
 * Classe permettant la modélisation d'un contact.
 */

@DatabaseTable(tableName = "tablecontact")
public class ContactPOJO implements Comparable,Serializable {

    @DatabaseField(columnName = "id", generatedId = true)
    private long id; // Identifiant du contact
    @DatabaseField(columnName = "phone", canBeNull = false)
    private String phone; // Numéro de téléphone du contact
    @DatabaseField(columnName = "surname", canBeNull = false)
    private String surname; // Nom du contact.
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name; // Prénom du contact
    @DatabaseField(columnName = "street", canBeNull = false)
    private String street; // Numéro et Nom de la rue ou est localisé le contact.
    @DatabaseField(columnName = "town", canBeNull = false)
    private String town; // Ville du contact.
    @DatabaseField(columnName = "code", canBeNull = false)
    private String code; // Code postal de la ville.
    @DatabaseField(columnName = "lat", canBeNull = true)
    private double lat; // Latitude permettant la localisation.
    @DatabaseField(columnName = "lng", canBeNull = true)
    private double lng; // Longitude permettant la localisation.


    public ContactPOJO() {}

    /**
     * Permet de créer un contact.
     * @param surname le nom du contact
     * @param name le prénom du contact
     * @param street le numéro et le nom de voirie
     * @param town la ville de résidence
     * @param code le code postal associée à la ville
     * @param phonenumber le numéro de téléphone
     * @param lat la latitude
     * @param lng la longitude
     */
    public ContactPOJO(String surname, String name, String street, String town, String code, String phonenumber, double lat, double lng) {
        this.surname = surname;
        this.name = name;
        this.street = street;
        this.code = code;
        this.town = town;
        this.phone = phonenumber;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Retourne le numéro de téléphone du contact.
     * @return le numéro sous forme de String.
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Retourne le prénom du contact.
     * @return le prénom sous forme de String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retourne le nom du contact.
     * @return le nom sous forme de String.
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Retourne la ville du contact.
     * @return la ville sous forme de String.
     */
    public String getTown() {
        return this.town;
    }

    /**
     * Retourne la rue du contact.
     * @return la rue sous forme de String.
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Retourne le code postal du contact.
     * @return le code postal sous forme de String.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Retourne l'identifiant associé au contact.
     * @return l'identifiant, unique.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Retourne la latitude correspondant à l'adresse du contact.
     * @return la latitude sous forme de double.
     */
    public double getLat() {
        return this.lat;
    }

    /**
     * Retourne la longitude correspondant à l'adresse du contact.
     * @return la longitude sous forme de double.
     */
    public double getLng() {
        return this.lng;
    }

    /**
     * Retourne l'association nom+prénom du contact.
     * @return la chaine de caractères ainsi formée
     */
    public String getFullName() {
        return this.name+" "+this.surname;
    }

    /**
     * Retourne l'association rue+code postal+ville du contact.
     * @return la chaine de caractères ainsi formée
     */
    public String getFullAddress() {
        return this.street+" "+this.code+" "+this.town;
    }

    /**
     * Permet de changer l'identifiant du contact.
     * @param nid le nouvel identifiant
     */
    public void setId(long nid) {
        this.id = nid;
    }

    /**
     * Permet de comparer deux contacts (utile pour le tri par ordre alphabétique).
     * @param another le contact à comparer
     * @return 0/-1/1
     */
    @Override
    public int compareTo(Object another) {
        if (another instanceof ContactPOJO) {
            ContactPOJO oc = (ContactPOJO)another;
            int res = surname.compareTo(oc.getSurname());
            if (res == 0)
                return name.compareTo(oc.getName());
            else
                return res;
        }
        else
            return -1;
    }
}

