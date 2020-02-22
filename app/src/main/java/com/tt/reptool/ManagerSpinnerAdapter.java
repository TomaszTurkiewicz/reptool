package com.tt.reptool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ManagerSpinnerAdapter extends ArrayAdapter<Manager> {


    public ManagerSpinnerAdapter(Context context, List<Manager> spinnerData) {
        super(context, 0, spinnerData);


    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myManagerSpinnerView(position,convertView,parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myManagerSpinnerView(position,convertView,parent);
    }

    private View myManagerSpinnerView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_manager_item,parent,false
            );
        }

        TextView managerSpinnerName = (TextView)convertView.findViewById(R.id.spinner_manager_name);
        TextView managerSpinnerSurname = (TextView)convertView.findViewById(R.id.spinner_manager_surname);
        Manager currentItem = getItem(position);
        if(currentItem!=null) {
            managerSpinnerName.setText(currentItem.getName());
            managerSpinnerSurname.setText(currentItem.getSurname());
        }
        return convertView;
    }
}
