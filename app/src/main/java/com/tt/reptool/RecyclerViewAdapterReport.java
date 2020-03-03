package com.tt.reptool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterReport extends RecyclerView.Adapter<RecyclerViewAdapterReport.VH> {
    private List<DailyReport> repList;
    private Context mContext;

    public RecyclerViewAdapterReport(Context mContext, List<DailyReport> repList){
        this.mContext = mContext;
        this.repList = repList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reports, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReport.VH holder, int position) {
        holder.reportDate.setText(repList.get(position).dateToString());
        holder.reportTimeIn.setText(repList.get(position).showTimeInToString());
        holder.reportTimeOut.setText(repList.get(position).showTimeOutToString());
        holder.reportJobNumber.setText(repList.get(position).getJob().getJobNumber());
        holder.reportName.setText(repList.get(position).getJob().getAddress().getName());
        holder.reportAddress.setText(repList.get(position).getJob().getAddress().getFullAddress());
        holder.reportDescription.setText(repList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    //TODO finish RecyclerView !!!!!!!!!!

public class VH extends RecyclerView.ViewHolder{
    ConstraintLayout reportLayout;
    TextView reportDate,reportTimeIn, reportTimeOut, reportJobNumber, reportName, reportAddress, reportDescription;


    public VH(@NonNull View itemView) {
        super(itemView);
        reportLayout = itemView.findViewById(R.id.constraintLayoutLayoutReport);
        reportDate = itemView.findViewById(R.id.dateLayoutReport);
        reportTimeIn = itemView.findViewById(R.id.timeInLayoutReport);
        reportTimeOut = itemView.findViewById(R.id.timeOutLayoutReport);
        reportJobNumber = itemView.findViewById(R.id.jobNumberLayoutReport);
        reportName = itemView.findViewById(R.id.nameLayoutReport);
        reportAddress = itemView.findViewById(R.id.addressLayoutReport);
        reportDescription = itemView.findViewById(R.id.descriptionLayoutReport);

    }
}
}