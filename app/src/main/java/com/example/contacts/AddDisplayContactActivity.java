package com.example.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddDisplayContactActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;

    ImageView imageViewProfile;
    EditText editTextFirstName, editTextLastName, editTextAddress, editTextEmail, editTextPhone;
    Button buttonSave, buttonCancel, buttonSelectImage;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_display_contact);

        imageViewProfile = findViewById(R.id.imageViewProfile);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonClose);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveContact() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String address = editTextAddress.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();

        // Pass the selected image URI to the MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("firstName", firstName);
        resultIntent.putExtra("lastName", lastName);
        resultIntent.putExtra("address", address);
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("phone", phone);
        if (selectedImage != null) {
            resultIntent.putExtra("imageUri", selectedImage.toString());
        }
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            imageViewProfile.setImageURI(selectedImage);
        }
    }
}