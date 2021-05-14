package com.tt.reptool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tt.reptool.R;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.Type;

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
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReport.VH holder, int position) {
        holder.reportDate.setText(repList.get(position).dateToString());
        holder.reportTimeIn.setText(repList.get(position).showTimeInToString());
        holder.reportTimeOut.setText(repList.get(position).showTimeOutToString());

        if(repList.get(position).getWorkReport().getType()== Type.WORK){
            holder.reportJobNumber1.setVisibility(View.VISIBLE);
            holder.reportName1.setVisibility(View.VISIBLE);
            holder.reportAddress1.setVisibility(View.VISIBLE);
            holder.reportJobNumber1.setText(repList.get(position).getWorkReport().getJob().getJobNumber());
            holder.reportName1.setText(repList.get(position).getWorkReport().getJob().getAddress().getName());
            holder.reportAddress1.setText(repList.get(position).getWorkReport().getJob().getAddress().fullAddress());
            holder.reportDescription1.setText(repList.get(position).getWorkReport().getDescription());
        }
        else {
            holder.reportJobNumber1.setVisibility(View.GONE);
            holder.reportName1.setVisibility(View.GONE);
            holder.reportAddress1.setVisibility(View.GONE);
        if(repList.get(position).getWorkReport().getType()==Type.TRAINING){
            holder.reportDescription1.setText(repList.get(position).getWorkReport().getType().name()+"\n"+
                    repList.get(position).getWorkReport().getDescription());
        }else{
            holder.reportDescription1.setText(repList.get(position).getWorkReport().getType().name());
        }
        }

        if(repList.get(position).getWorkReport2()!=null){

            holder.reportAddress2.setVisibility(View.VISIBLE);
            holder.reportDescription2.setVisibility(View.VISIBLE);
            holder.reportJobNumber2.setVisibility(View.VISIBLE);
            holder.reportName2.setVisibility(View.VISIBLE);


            if(repList.get(position).getWorkReport2().getType()== Type.WORK){
                holder.reportJobNumber2.setText(repList.get(position).getWorkReport2().getJob().getJobNumber());
                holder.reportName2.setText(repList.get(position).getWorkReport2().getJob().getAddress().getName());
                holder.reportAddress2.setText(repList.get(position).getWorkReport2().getJob().getAddress().fullAddress());
                holder.reportDescription2.setText(repList.get(position).getWorkReport2().getDescription());
            }
            else {
                holder.reportJobNumber2.setVisibility(View.GONE);
                holder.reportName2.setVisibility(View.GONE);
                holder.reportAddress2.setVisibility(View.GONE);
                if(repList.get(position).getWorkReport2().getType()==Type.TRAINING){
                    holder.reportDescription2.setText(repList.get(position).getWorkReport2().getType().name()+"\n"+
                            repList.get(position).getWorkReport2().getDescription());
                }else{
                    holder.reportDescription2.setText(repList.get(position).getWorkReport2().getType().name());
                }
            }


        }
        else{
            holder.reportAddress2.setVisibility(View.GONE);
            holder.reportDescription2.setVisibility(View.GONE);
            holder.reportJobNumber2.setVisibility(View.GONE);
            holder.reportName2.setVisibility(View.GONE);
        }


        if(repList.get(position).getWorkReport3()!=null){

            holder.reportAddress3.setVisibility(View.VISIBLE);
            holder.reportDescription3.setVisibility(View.VISIBLE);
            holder.reportJobNumber3.setVisibility(View.VISIBLE);
            holder.reportName3.setVisibility(View.VISIBLE);


            if(repList.get(position).getWorkReport3().getType()== Type.WORK){
                holder.reportJobNumber3.setText(repList.get(position).getWorkReport3().getJob().getJobNumber());
                holder.reportName3.setText(repList.get(position).getWorkReport3().getJob().getAddress().getName());
                holder.reportAddress3.setText(repList.get(position).getWorkReport3().getJob().getAddress().fullAddress());
                holder.reportDescription3.setText(repList.get(position).getWorkReport3().getDescription());
            }
            else {
                holder.reportJobNumber3.setVisibility(View.GONE);
                holder.reportName3.setVisibility(View.GONE);
                holder.reportAddress3.setVisibility(View.GONE);
                if(repList.get(position).getWorkReport3().getType()==Type.TRAINING){
                    holder.reportDescription3.setText(repList.get(position).getWorkReport3().getType().name()+"\n"+
                            repList.get(position).getWorkReport3().getDescription());
                }else{
                    holder.reportDescription3.setText(repList.get(position).getWorkReport3().getType().name());
                }
            }


        }
        else{
            holder.reportAddress3.setVisibility(View.GONE);
            holder.reportDescription3.setVisibility(View.GONE);
            holder.reportJobNumber3.setVisibility(View.GONE);
            holder.reportName3.setVisibility(View.GONE);
        }





        holder.reportAddress4.setVisibility(View.GONE);
        holder.reportAddress5.setVisibility(View.GONE);
        holder.reportDescription4.setVisibility(View.GONE);
        holder.reportDescription5.setVisibility(View.GONE);
        holder.reportJobNumber4.setVisibility(View.GONE);
        holder.reportJobNumber5.setVisibility(View.GONE);
        holder.reportName4.setVisibility(View.GONE);
        holder.reportName5.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }


public class VH extends RecyclerView.ViewHolder{
    ConstraintLayout reportLayout;
    TextView reportDate,reportTimeIn, reportTimeOut, reportJobNumber1, reportName1, reportAddress1, reportDescription1,
    reportJobNumber2, reportName2, reportAddress2, reportDescription2,
            reportJobNumber3, reportName3, reportAddress3, reportDescription3,
            reportJobNumber4, reportName4, reportAddress4, reportDescription4,
            reportJobNumber5, reportName5, reportAddress5, reportDescription5;


    public VH(@NonNull View itemView) {
        super(itemView);
        reportLayout = itemView.findViewById(R.id.constraintLayoutLayoutReport);
        reportDate = itemView.findViewById(R.id.dateLayoutReport);
        reportTimeIn = itemView.findViewById(R.id.timeInLayoutReport);
        reportTimeOut = itemView.findViewById(R.id.timeOutLayoutReport);

        reportJobNumber1 = itemView.findViewById(R.id.jobNumberLayoutReport1);
        reportName1 = itemView.findViewById(R.id.nameLayoutReport1);
        reportAddress1 = itemView.findViewById(R.id.addressLayoutReport1);
        reportDescription1 = itemView.findViewById(R.id.descriptionLayoutReport1);

        reportJobNumber2 = itemView.findViewById(R.id.jobNumberLayoutReport2);
        reportName2 = itemView.findViewById(R.id.nameLayoutReport2);
        reportAddress2 = itemView.findViewById(R.id.addressLayoutReport2);
        reportDescription2 = itemView.findViewById(R.id.descriptionLayoutReport2);

        reportJobNumber3 = itemView.findViewById(R.id.jobNumberLayoutReport3);
        reportName3 = itemView.findViewById(R.id.nameLayoutReport3);
        reportAddress3 = itemView.findViewById(R.id.addressLayoutReport3);
        reportDescription3 = itemView.findViewById(R.id.descriptionLayoutReport3);

        reportJobNumber4 = itemView.findViewById(R.id.jobNumberLayoutReport4);
        reportName4 = itemView.findViewById(R.id.nameLayoutReport4);
        reportAddress4 = itemView.findViewById(R.id.addressLayoutReport4);
        reportDescription4 = itemView.findViewById(R.id.descriptionLayoutReport4);

        reportJobNumber5 = itemView.findViewById(R.id.jobNumberLayoutReport5);
        reportName5 = itemView.findViewById(R.id.nameLayoutReport5);
        reportAddress5 = itemView.findViewById(R.id.addressLayoutReport5);
        reportDescription5 = itemView.findViewById(R.id.descriptionLayoutReport5);
    }
}
}