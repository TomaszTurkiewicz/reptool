package com.tt.reptool;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerDialog extends AppCompatDialogFragment {
    private EditText editTextManagerName;
    private EditText editTextManagerSurname;
    private EditText editTextManagerEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String name;
    private String surname;
    private String email;

    public ManagerDialog(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_manager, null);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Manager");

        builder.setView(view)
                .setTitle("Change")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String managerName = editTextManagerName.getText().toString().trim();
                        String managerSurname = editTextManagerSurname.getText().toString().trim();
                        String managerEmail = editTextManagerEmail.getText().toString().trim();

                        databaseReference.child(name + " " + surname).removeValue();
                        //TODO adding new manager
                        if(!TextUtils.isEmpty(managerName)&&!TextUtils.isEmpty(managerSurname)&&!TextUtils.isEmpty(managerEmail)) {

                            final String mName = managerName.substring(0,1).toUpperCase()+managerName.substring(1).toLowerCase();
                            final String mSurname = managerSurname.substring(0,1).toUpperCase()+managerSurname.substring(1).toLowerCase();
                            databaseReference.child(mName + " " + mSurname).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Toast.makeText(getContext(),"Already exist", Toast.LENGTH_LONG).show();
                                    }else{
                                        Manager manager = new Manager();
                                        manager.setName(mName);
                                        manager.setSurname(mSurname);
                                        manager.setEmailAddress(email);
                                        databaseReference.child(mName + " " + mSurname).setValue(manager);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(getContext(),"Fields empty",
                                    Toast.LENGTH_LONG).show();
                        }



                    }
                });

        editTextManagerName = view.findViewById(R.id.editManagerName);
        editTextManagerSurname = view.findViewById(R.id.editManagerSurname);
        editTextManagerEmail = view.findViewById(R.id.editManagerEmail);

        editTextManagerName.setText(name);
        editTextManagerSurname.setText(surname);
        editTextManagerEmail.setText(email);

        return builder.create();
    }

}
