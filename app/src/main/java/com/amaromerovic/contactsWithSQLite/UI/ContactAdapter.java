package com.amaromerovic.contactsWithSQLite.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amaromerovic.contactsWithSQLite.R;
import com.amaromerovic.contactsWithSQLite.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(Context context, ArrayList<Contact> users) {
        super(context, R.layout.activity_main, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.label);
        // Populate the data into the template view using the data object
        tvName.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
        // Return the completed view to render on screen
        return convertView;
    }
}
