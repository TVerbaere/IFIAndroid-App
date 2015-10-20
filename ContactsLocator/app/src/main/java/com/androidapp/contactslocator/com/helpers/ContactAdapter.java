package com.androidapp.contactslocator.com.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidapp.contactslocator.R;
import com.androidapp.contactslocator.com.model.ContactPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thibaud VERBAERE
 * Classe pour permettre l'affichage de la liste de contacts.
 */

public class ContactAdapter extends BaseAdapter {

    private List<ContactPOJO> contacts;

    private Context mContext;

    private LayoutInflater mInflater;

    private ArrayList<ContactAdapterListener> mListListener = new ArrayList<ContactAdapterListener>();

    /**
     * Création de l'adapter.
     * @param context contexte de l'activité
     * @param aList la liste des contacts
     */
    public ContactAdapter(Context context, List<ContactPOJO> aList) {
        this.mContext = context;
        this.contacts = aList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    /**
     * Retourne le nombre de contacts créés en base de données.
     * @return le nombre de contactsS
     */
    public int getCount() {
        return this.contacts.size();
    }

    /**
     * Permet de recupèrer le contact situé à une position précise en base.
     * @param position le numéro de position du contact
     * @return le contact situé à cette position
     */
    public ContactPOJO getItem(int position) {
        return this.contacts.get(position);
    }

    /**
     * Permet de recupèrer l'identifiant du contact situé à une position précise en base.
     * @param position le numéro de position du contact
     * @return l'identifiant du contact situé à cette position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Permet la création de la vue associée au contact situé à une position précise en base.
     * @param position le numéro de position du contact
     * @param convertView
     * @param parent la vue mère
     * @return la vue créée
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            // On recupère le layout contact_layout.
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.contact_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        // On récupère les champs du layout que l'on doit remplir.
        TextView surname = (TextView)layoutItem.findViewById(R.id.contact_surname);
        TextView address_l1 = (TextView)layoutItem.findViewById(R.id.contact_address_l1);
        TextView address_l2 = (TextView)layoutItem.findViewById(R.id.contact_address_l2);
        TextView phone = (TextView)layoutItem.findViewById(R.id.contact_phone);

        // Remplissage des champs par rapport à la position passé en paramètre.
        surname.setText(contacts.get(position).getFullName());
        address_l1.setText(contacts.get(position).getStreet());
        address_l2.setText(contacts.get(position).getCode()+" "+contacts.get(position).getTown());
        phone.setText(contacts.get(position).getPhone());

        layoutItem.setTag(position);
        // La vue est cliquable :
        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                sendListener(contacts.get(position), position);
            }

        });
        return layoutItem;
    }

    /**
     * Ajoute un listener à la liste.
     * @param aListener le listener à ajouter
     */
    public void addListener(ContactAdapterListener aListener) {
        mListListener.add(aListener);
    }

    private void sendListener(ContactPOJO item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickContact(item, position);
        }
    }


}
