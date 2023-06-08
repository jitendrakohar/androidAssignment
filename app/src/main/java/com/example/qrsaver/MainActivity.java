package com.example.qrsaver;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrsaver.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<String> dataList;
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        dataList = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        binding.scanqr.setOnClickListener(v -> {
            scanQr();
        });


    }

    public void scanQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR code");
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.setOrientationLocked(true); // Enable rotation
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrCodeValue = result.getContents();
                Toast.makeText(this, "qr value: " + qrCodeValue, Toast.LENGTH_SHORT).show();
                //                binding.tvresult.setText(qrCodeValue);
                dataList.add(qrCodeValue);
                save((ArrayList<String>) dataList);

            } else {
                Toast.makeText(this, "something is wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void save(ArrayList<String> dataList) {


        // Add more key-value pairs as needed
        HashMap<String, Object> dataMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            String documentId = "document" + i; // Unique document ID
            String value = dataList.get(i);
            dataMap.put(documentId, value);
        }

        // Add a new document with a generated ID
        db.collection(getUserUniqueId())

                .add(dataMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "document stored successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this, "storing failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getUserUniqueId() {

        String userId = null;
        if (currentUser != null) {
            userId = currentUser.getUid();
            // Use the userId as needed in your code
        } else {
            // No user is currently authenticated
        }

        return userId;
    }

    @Override
    protected void onStart() {
        super.onStart();

        listAllData();
    }

    public void listAllData() {


        CollectionReference collectionRef = db.collection(getUserUniqueId());

        collectionRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentId = documentSnapshot.getId();
                        Map<String, Object> data = documentSnapshot.getData();

                        // Access the data fields
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            // Use the key-value pairs as needed
                            dataList.add(value.toString());
                        }
                    }
                    setAdapter();

                    Toast.makeText(this, "data has been added", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during retrieval
                    Toast.makeText(this, "data retrieval error", Toast.LENGTH_SHORT).show();
                });
        Toast.makeText(this, "listall data", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.logout) {
            // Handle menu item 1 click
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       // Log out the current user
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this,loginActivity.class));

    }
    public void setAdapter(){
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

}