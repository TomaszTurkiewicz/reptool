package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerActivity extends AppCompatActivity {

    private EditText managerName;
    private EditText managerSurname;
    private EditText managerEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void saveManager(View view) {
        managerName = findViewById(R.id.managerName);
        managerSurname = findViewById(R.id.managerSurname);
        managerEmail = findViewById(R.id.managerEmail);
        final String name = managerName.getText().toString().trim();
        final String surname = managerSurname.getText().toString().trim();
        String email = managerEmail.getText().toString().trim();

        final Manager manager = new Manager();
        manager.setName(name);
        manager.setSurname(surname);
        manager.setEmailAddress(email);

        databaseReference=firebaseDatabase.getReference("Manager");
        databaseReference.child(name+" "+surname).setValue(manager);

        //TODO check if fields are not empty
        //TODO delete fields after uploading



    }
}
