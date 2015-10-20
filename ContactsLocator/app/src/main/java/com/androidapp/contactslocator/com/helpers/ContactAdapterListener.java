package com.androidapp.contactslocator.com.helpers;

import com.androidapp.contactslocator.com.model.ContactPOJO;

/**
 * @author Thibaud VERBAERE
 */

public interface ContactAdapterListener {

    public void onClickContact(ContactPOJO item, int position);
}
