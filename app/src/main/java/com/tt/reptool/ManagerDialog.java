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
    private DialogCallback callback;
    private Manager oManager;
    private Manager nManager;

    public ManagerDialog(String name, String surname, String email, DialogCallback callback) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_manager, null);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference(getString(R.string.firebasepath_manager));
        editTextManagerName = view.findViewById(R.id.editManagerName);
        editTextManagerSurname = view.findViewById(R.id.editManagerSurname);
        editTextManagerEmail = view.findViewById(R.id.editManagerEmail);

        oManager = new Manager();
        nManager = new Manager();

        oManager.setName(name);
        oManager.setSurname(surname);
        oManager.setEmailAddress(email);

        editTextManagerName.setText(name);
        editTextManagerSurname.setText(surname);
        editTextManagerEmail.setText(email);

        builder.setView(view)
                .setTitle(getString(R.string.change))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String managerName = editTextManagerName
                                .getText()
                                .toString()
                                .trim();

                        String managerSurname = editTextManagerSurname
                                .getText()
                                .toString()
                                .trim();

                        final String managerEmail = editTextManagerEmail
                                .getText()
                                .toString()
                                .trim();

                        // check if fields are not empty

                        if(!TextUtils.isEmpty(managerName)&&
                                !TextUtils.isEmpty(managerSurname)&&
                                !TextUtils.isEmpty(managerEmail)) {

                            nManager.setName(managerName);
                            nManager.setSurname(managerSurname);
                            nManager.setEmailAddress(managerEmail);
                            databaseReference.child(oManager.getName() +
                                    " " + oManager.getSurname() +
                                    " " + oManager.getEmailAddress()
                                    .replace(".","(dot)"))
                                    .removeValue();
                            databaseReference.child(nManager.getName() +
                                    " " +
                                    nManager.getSurname() +
                                    " " +
                                    nManager.getEmailAddress().replace(".","(dot)"))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                        if(dataSnapshot.exists()){
                                            databaseReference.child(oManager.getName()+
                                                    " "+
                                                    oManager.getSurname()+
                                                    " "+
                                                    oManager.getEmailAddress().replace(".","(dot)")).setValue(oManager);
                                        }
                                        else{
                                            databaseReference.child(nManager.getName()+
                                                    " "+
                                                    nManager.getSurname()+
                                                    " "+
                                                    nManager.getEmailAddress().replace(".","(dot)")).setValue(nManager);
                                        }




                                        //notify recyclerview about changes

                                        callback.onDialogCallback();
                                        dialog.dismiss();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(getContext(),getString(R.string.empty_fields),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

        return builder.create();
    }

    public interface DialogCallback{
        void onDialogCallback();
    }

}
// TODO inputs only single line