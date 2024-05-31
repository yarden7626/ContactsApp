package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CardAdapter.ItemClickListener, CardAdapter.ItemLongClickListener {

    private static final int ADD_CONTACT_REQUEST = 1;

    ArrayList<CardModel> models = new ArrayList<>();
    CardAdapter adapter;
    MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDBHelper(this);

        // Load content from the database
        models = (ArrayList<CardModel>) dbHelper.getAllEntriesFromDB();

        RecyclerView recyclerView = findViewById(R.id.rcView);
        adapter = new CardAdapter(this, this, this, models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddDisplayContactActivity.class);
            startActivityForResult(intent, ADD_CONTACT_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            String firstName = data.getStringExtra("firstName");
            String lastName = data.getStringExtra("lastName");
            String address = data.getStringExtra("address");
            String email = data.getStringExtra("email");
            String phone = data.getStringExtra("phone");
            String imageUri = data.getStringExtra("imageUri");

            Log.d("MainActivity", "Received data: " + firstName + " " + lastName + " " + address + " " + email + " " + phone + " " + imageUri);

            CardModel newContact = new CardModel(firstName, lastName, imageUri, address, phone, false);
            dbHelper.addEntry(newContact);

            models.clear();
            models.addAll(dbHelper.getAllEntriesFromDB());
            adapter.notifyDataSetChanged();
            RecyclerView recyclerView = findViewById(R.id.rcView);
            recyclerView.scrollToPosition(models.size() - 1);
        }
    }

    @Override
    public void onItemClick(int position) {
        CardModel selectedContact = models.get(position);
        Intent intent = new Intent(MainActivity.this, AddDisplayContactActivity.class);
        intent.putExtra("contactId", selectedContact.getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        CardModel selectedContact = models.get(position);
        dbHelper.deleteEntry(selectedContact.getId());
        models.remove(position);
        adapter.notifyItemRemoved(position);
    }
}