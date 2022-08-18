package com.amaromerovic.contactsWithSQLite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.amaromerovic.contactsWithSQLite.Data.DataBaseHandler;
import com.amaromerovic.contactsWithSQLite.UI.ContactAdapter;
import com.amaromerovic.contactsWithSQLite.databinding.ActivityMainBinding;
import com.amaromerovic.contactsWithSQLite.model.Contact;
import com.amaromerovic.contactsWithSQLite.util.Util;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private DataBaseHandler dataBaseHandler;
    private ArrayList<Contact> contacts;
    private ContactAdapter adapter;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        dataBaseHandler = new DataBaseHandler(MainActivity.this);

        contacts = dataBaseHandler.getAllContacts();
        adapter = new ContactAdapter(this, contacts);
        mainBinding.listView.setAdapter(adapter);

        mainBinding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Contact contact = (Contact) adapterView.getAdapter().getItem(i);

            Intent updateDelete = new Intent(getApplicationContext(), NewUpdate.class);
            updateDelete.putExtra(Util.ID_KEY, String.valueOf(contact.getId()));
            updateDelete.putExtra(Util.FIRST_NAME_KEY, contact.getFirstName());
            updateDelete.putExtra(Util.LAST_NAME_KEY, contact.getLastName());
            updateDelete.putExtra(Util.PHONE_NUMBER_KEY, contact.getPhoneNumber());
            updateContactActivityResultLauncher.launch(updateDelete);
        });



        mainBinding.addNewContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewUpdate.class);
            newContactActivityResultLauncher.launch(intent);
        });


    }

    ActivityResultLauncher<Intent> newContactActivityResultLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    String lastName = data.getStringExtra(Util.LAST_NAME_KEY);
                    String firstName = data.getStringExtra(Util.FIRST_NAME_KEY);
                    String phoneName = data.getStringExtra(Util.PHONE_NUMBER_KEY);

                    if (firstName.isEmpty() || lastName.isEmpty() || phoneName.isEmpty()){
                        setSnackBar("Contact creation was unsuccessful!", ContextCompat.getColor(MainActivity.this, R.color.purple_500));

                    } else {
                        dataBaseHandler.addContact(new Contact(firstName, lastName, phoneName));
                        setSnackBar("Contact was created successfully!", ContextCompat.getColor(MainActivity.this, R.color.purple_500));


                        refresh();
                    }

                }
            } );

    ActivityResultLauncher<Intent> updateContactActivityResultLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Intent data = result.getData();

                    String id = data.getStringExtra(Util.ID_KEY);
                    String lastName = data.getStringExtra(Util.LAST_NAME_KEY);
                    String firstName = data.getStringExtra(Util.FIRST_NAME_KEY);
                    String phoneName = data.getStringExtra(Util.PHONE_NUMBER_KEY);

                    if (firstName.isEmpty() || lastName.isEmpty() || phoneName.isEmpty()){
                        setSnackBar("Contact update was unsuccessful!", ContextCompat.getColor(MainActivity.this, R.color.purple_500));
                    } else {
                        Contact contact = dataBaseHandler.getContact(Integer.parseInt(id));
                        contact.setFirstName(firstName);
                        contact.setLastName(lastName);
                        contact.setPhoneNumber(phoneName);
                        dataBaseHandler.updateContact(contact);
                        setSnackBar("Contact was updated successfully!", ContextCompat.getColor(MainActivity.this, R.color.purple_500));

                        refresh();
                    }

                } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {
                    Intent data = result.getData();
                    String id = data.getStringExtra(Util.ID_KEY);
                    Contact contact = dataBaseHandler.getContact(Integer.parseInt(id));
                    dataBaseHandler.deleteContact(contact);
                    setSnackBar("Contact was deleted successfully!", ContextCompat.getColor(MainActivity.this, R.color.purple_500));
                    refresh();
                } else {
                    finish();
                }
            });

    public void refresh() {
        adapter.clear();
        adapter.addAll(dataBaseHandler.getAllContacts());
        mainBinding.listView.setAdapter(adapter);
    }

    public void setSnackBar(String text, int color) {
        if (snackbar != null)
            snackbar.dismiss();
        snackbar = Snackbar.make(mainBinding.listView, text, Snackbar.LENGTH_SHORT).setBackgroundTint(color);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width=FrameLayout.LayoutParams.WRAP_CONTENT;
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        if(tv!=null) {
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
        }
        view.setLayoutParams(params);
        snackbar.show();
    }



}