package com.androidapp.contactslocator.com.helpers;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Thibaud VERBAERE
 * Classe regroupant des fonctionnalités utiles au programme.
 */

public class Tools {

    /**
     * Permet de mesurer la distance (à vol d'oiseau) entre deux localisations.
     * @param latLng1 la première localisation
     * @param latLng2 la seconde localisation
     * @return la distance en kilomètre
     */
    public static float distanceBetween(LatLng latLng1, LatLng latLng2) {

        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude); // Première localisation.
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude); // Deuxième localisation.
        loc2.setLongitude(latLng2.longitude);

        return loc1.distanceTo(loc2)/1000; // Division par 1000 pour renvoyer des kilomètres.
    }

    /**
     * Affiche la distance entre deux localisations.
     * @param latLng1 la première localisation
     * @param latLng2 la seconde localisation
     * @return une chaine de caractères contenant la distance entre les deux localisations.
     */
    public static String printDistance(LatLng latLng1, LatLng latLng2) {
        return distanceBetween(latLng1,latLng2)+" km.";
    }
}
