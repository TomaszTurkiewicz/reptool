package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.tt.reptool.adapters.RecyclerViewAdapter;
import com.tt.reptool.fragments.ManagerDialog;
import com.tt.reptool.javaClasses.Manager;
import com.tt.reptool.javaClasses.ManagerComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private EditText managerName;
    private EditText managerSurname;
    private EditText managerEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Manager> mList = new ArrayList<>();
    private Manager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        manager = new Manager();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference(getString(R.string.firebasepath_manager));
        initManagerList();
    }


    //clear list and fill with new data
    private void initManagerList() {

        mList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Manager tmanager = ps.getValue(Manager.class);
                    mList.add(tmanager);
                }
                Collections.sort(mList,new ManagerComparator());


                initRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    //init recyclerview with data
    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewManagers);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
                builder.setCancelable(true);
                builder.setTitle(R.string.deleting);
                builder.setMessage(getString(R.string.are_you_sure_you_want_delete)+"\n"+
                        mList.get(position).getName()+" "+
                        mList.get(position).getSurname()+"?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeManager(position);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();




            }

            @Override
            public void onEditClick(int position) {
                editManager(position);
            }
        });
    }

    //change existing manager data
    private void editManager(int position) {
        openDialog(position);
    }

    // open dialog for editing manager data
    // callback for refreshing recyclerview
    private void openDialog(int position) {
        ManagerDialog managerDialog =
                new ManagerDialog(mList.get(position).getName(),
                        mList.get(position).getSurname(),
                        mList.get(position).getEmailAddress(),
                        mList.get(position).isWorking(),
                        new ManagerDialog.DialogCallback() {
                            @Override
                            public void onDialogCallback() {
                                initManagerList();
                            }
                        });
        managerDialog.show(getSupportFragmentManager(),"manager dialog");
    }

    // delete manager and refresh recycler view
    private void removeManager(int position) {
        String dName = mList.get(position).getName();
        String dSurname = mList.get(position).getSurname();
        String dEmail = mList.get(position).getEmailAddress();
        String drEmail = dEmail.replace(".","(dot)");
        databaseReference.child(dName + " " + dSurname + " " + drEmail).removeValue();
        initManagerList();
    }

    // add new manager

    public void saveManager(View view) {
        managerName = findViewById(R.id.managerName);
        managerSurname = findViewById(R.id.managerSurname);
        managerEmail = findViewById(R.id.managerEmail);
        final String name = managerName.getText().toString().trim();
        final String surname = managerSurname.getText().toString().trim();
        final String email = managerEmail.getText().toString().trim();

        //check if fields are not empty

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(surname)&&!TextUtils.isEmpty(email)) {
            manager.setName(name);
            manager.setSurname(surname);
            manager.setEmailAddress(email);

            final String mEmail = email.replace(".","(dot)");
            databaseReference.child(manager.getName() + " " + manager.getSurname() + " " + mEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //check if new manager already exists

                    if(dataSnapshot.exists()){
                        Toast.makeText(ManagerActivity.this,getString(R.string.already_exists),
                                Toast.LENGTH_LONG).show();
                    }else{


                        databaseReference.child(manager.getName() + " " + manager.getSurname() + " " + mEmail).setValue(manager);
                        initManagerList();
                        managerName.setText(getString(R.string.empty));
                        managerSurname.setText(getString(R.string.empty));
                        managerEmail.setText(getString(R.string.empty));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            Toast.makeText(ManagerActivity.this,getString(R.string.empty_fields),
                    Toast.LENGTH_LONG).show();
        }

    }
}
