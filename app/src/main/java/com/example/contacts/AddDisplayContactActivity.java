package com.example.contacts;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class AddDisplayContactActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 101;

    ImageView imageViewProfile;
    EditText editTextFirstName, editTextLastName, editTextAddress, editTextEmail, editTextPhone;
    Button buttonSave, buttonCancel, buttonDelete, buttonSelectImage;
    int contactId = -1; // Default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_display_contact);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        }

        imageViewProfile = findViewById(R.id.imageViewProfile);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonClose);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        buttonSave.setOnClickListener(v -> saveContact());
        buttonCancel.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent.hasExtra("contactId")) {
            contactId = intent.getIntExtra("contactId", -1);
            displayContactDetails(contactId);
            buttonDelete.setVisibility(View.VISIBLE);
            buttonDelete.setOnClickListener(v -> deleteContact(contactId));
        } else {
            buttonDelete.setVisibility(View.GONE);
        }

        buttonSelectImage.setOnClickListener(v -> openGallery());
    }

    private void saveContact() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String address = editTextAddress.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String imageUri = getImageUriFromImageView(imageViewProfile);

        MyDBHelper dbHelper = new MyDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FirstName", firstName);
        values.put("LastName", lastName);
        values.put("Address", address);
        values.put("email", email);
        values.put("PhoneNumber", phone);
        values.put("ImageUri", imageUri);

        if (contactId == -1) {
            db.insert("Contacts", null, values);
        } else {
            db.update("Contacts", values, "_id = ?", new String[]{String.valueOf(contactId)});
        }

        db.close();
        dbHelper.close();

        finish();
    }

    private void deleteContact(int contactId) {
        MyDBHelper dbHelper = new MyDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("Contacts", "_id = ?", new String[]{String.valueOf(contactId)});

        db.close();
        dbHelper.close();

        Toast.makeText(this, "Contact deleted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void displayContactDetails(int contactId) {
        // Implement your code to display contact details here
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageViewProfile.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    private String getImageUriFromImageView(ImageView imageView) {
        Uri imageUri = null;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            imageUri = getImageUri(this, bitmap);
        }
        return (imageUri != null) ? imageUri.toString() : null;
    }

    private Uri getImageUri(AppCompatActivity context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "ContactImage", null);
        if (path != null) {
            return Uri.parse(path);
        } else {
            return null;
        }
    }
}