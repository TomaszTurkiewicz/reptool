package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private EditText managerName;
    private EditText managerSurname;
    private EditText managerEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Manager> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Manager");
        initManagerList();
    }

    private void initManagerList() {

        mList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    mList.add(manager);
                }
                initRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewManagers);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void saveManager(View view) {
        managerName = findViewById(R.id.managerName);
        managerSurname = findViewById(R.id.managerSurname);
        managerEmail = findViewById(R.id.managerEmail);
        final String name = managerName.getText().toString().trim();
        final String surname = managerSurname.getText().toString().trim();
        final String email = managerEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(surname)&&!TextUtils.isEmpty(email)) {

            final String mName = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
            final String mSurname = surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase();
            databaseReference.child(name + " " + surname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(ManagerActivity.this,"Already exists",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Manager manager = new Manager();
                        manager.setName(mName);
                        manager.setSurname(mSurname);
                        manager.setEmailAddress(email);
                        databaseReference.child(name + " " + surname).setValue(manager);
                        initManagerList();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
        else{
            Toast.makeText(ManagerActivity.this,"Fields empty",
                    Toast.LENGTH_LONG).show();
        }
        //TODO check if fields are not empty
        //TODO delete fields after uploading



    }
}
