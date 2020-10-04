package com.tt.reptool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt.reptool.R;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;

import java.util.List;

public class JobSpinnerAdapter extends ArrayAdapter<Job> {

    public JobSpinnerAdapter(Context context, List<Job> spinnerJobData){
        super(context,0,spinnerJobData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myJobSpinnerView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myJobSpinnerView(position,convertView,parent);
    }

    private View myJobSpinnerView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_job_item,parent,false
            );
        }
        TextView jobNumberSpinner = (TextView)convertView.findViewById(R.id.spinner_job_number);
        TextView jobName = (TextView)convertView.findViewById(R.id.spinner_job_name);

        Job currentItem = getItem(position);
        if(currentItem!=null){
            jobNumberSpinner.setVisibility(View.VISIBLE);
            jobName.setVisibility(View.VISIBLE);
            if(currentItem.getJobType()==null||currentItem.getJobType()== JobType.INSTALLATION){
            jobNumberSpinner.setText(currentItem.getJobNumber());
            jobName.setText(currentItem.getAddress().getName());
            }
            else{
                jobNumberSpinner.setText(currentItem.getJobType().toString());
                jobName.setText(currentItem.getAddress().getName());
            }
        }
        else{
            jobNumberSpinner.setVisibility(View.GONE);
            jobName.setVisibility(View.GONE);
        }
        return convertView;
    }



}
