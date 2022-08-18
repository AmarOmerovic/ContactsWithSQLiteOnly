package com.amaromerovic.contactsWithSQLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amaromerovic.contactsWithSQLite.util.Util;

public class NewUpdate extends AppCompatActivity {
    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;
    private EditText firstNameView;
    private EditText lastNameView;
    private EditText phoneNumberView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update);

        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.savebutton);
        deleteButton = findViewById(R.id.deleteButton);
        firstNameView = findViewById(R.id.firstNameView);
        lastNameView = findViewById(R.id.lastNameView);
        phoneNumberView = findViewById(R.id.phoneNumberView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            firstNameView.setHint(bundle.getString(Util.FIRST_NAME_KEY));
            lastNameView.setHint(bundle.getString(Util.LAST_NAME_KEY));
            phoneNumberView.setHint(bundle.getString(Util.PHONE_NUMBER_KEY));
        } else {
            deleteButton.setVisibility(View.GONE);
        }



        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });


        saveButton.setOnClickListener(view -> {
            if (bundle != null){
                Intent intent = getIntent();

                String id = bundle.getString(Util.ID_KEY);
                String firstName = firstNameView.getText().toString().trim();
                String lastName = lastNameView.getText().toString().trim();
                String phoneNumber = phoneNumberView.getText().toString().trim();

                intent.putExtra(Util.ID_KEY, id);
                intent.putExtra(Util.FIRST_NAME_KEY, firstName);
                intent.putExtra(Util.LAST_NAME_KEY, lastName);
                intent.putExtra(Util.PHONE_NUMBER_KEY, phoneNumber);
                setResult(RESULT_CANCELED, intent);
                finish();
            } else {
                Intent intent = getIntent();

                String firstName = firstNameView.getText().toString().trim();
                String lastName = lastNameView.getText().toString().trim();
                String phoneNumber = phoneNumberView.getText().toString().trim();



                intent.putExtra(Util.FIRST_NAME_KEY, firstName);
                intent.putExtra(Util.LAST_NAME_KEY, lastName);
                intent.putExtra(Util.PHONE_NUMBER_KEY, phoneNumber);
                setResult(RESULT_OK, intent);
                finish();
            }

        });

        deleteButton.setOnClickListener(view -> {
            Intent intent = getIntent();
            String id = bundle.getString(Util.ID_KEY);
            intent.putExtra(Util.ID_KEY, id);
            setResult(RESULT_FIRST_USER, intent);
            finish();
        });

    }
}