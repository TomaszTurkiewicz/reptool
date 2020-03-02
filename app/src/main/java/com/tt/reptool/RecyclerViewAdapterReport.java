package com.tt.reptool;

import android.content.Context;
import android.view.View;
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