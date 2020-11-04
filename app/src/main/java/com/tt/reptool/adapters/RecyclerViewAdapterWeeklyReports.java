package com.tt.reptool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tt.reptool.R;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.Type;

import java.util.List;

public class RecyclerViewAdapterWeeklyReports extends RecyclerView.Adapter<RecyclerViewAdapterWeeklyReports.ViewH> {
    private List<DailyReport> wRepList;
    private Context context;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onDeleteWeeklyReportClick (int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecyclerViewAdapterWeeklyReports(Context context, List<DailyReport> wRepList) {
        this.wRepList = wRepList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_weekly_reports, parent, false);
        return new ViewH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterWeeklyReports.ViewH holder, int position) {
        holder.reportDate.setText(wRepList.get(position).dateToString());
        holder.reportTimeIn.setText(wRepList.get(position).showTimeInToString());
        holder.reportTimeOut.setText(wRepList.get(position).showTimeOutToString());

        if(wRepList.get(position).getWorkReport().getType()== Type.WORK){
            holder.reportJobNumber.setText(wRepList.get(position).getWorkReport().getJob().getJobNumber());
        }
        else{
            holder.reportJobNumber.setVisibility(View.GONE);
        }

        if(wRepList.get(position).getWorkReport().getType()== Type.WORK){
            holder.reportName.setText(wRepList.get(position).getWorkReport().getJob().getAddress().getName());
        }
        else{
            holder.reportName.setVisibility(View.GONE);
        }

        if(wRepList.get(position).getWorkReport().getType()== Type.WORK){
            holder.reportAddress.setText(wRepList.get(position).getWorkReport().getJob().getAddress().fullAddress());
        }
        else{
            holder.reportAddress.setVisibility(View.GONE);
        }

        if(wRepList.get(position).getWorkReport().getType()==Type.WORK) {
            holder.reportDescription.setText(wRepList.get(position).getWorkReport().getDescription());
        }
        else if(wRepList.get(position).getWorkReport().getType()==Type.TRAINING){
            holder.reportDescription.setText(wRepList.get(position).getWorkReport().getType().name()+"\n"+
                    wRepList.get(position).getWorkReport().getDescription());
        }else{
            holder.reportDescription.setText(wRepList.get(position).getWorkReport().getType().name());
        }

        if(wRepList.get(position).getWorkReport2()!=null){


            if(wRepList.get(position).getWorkReport2().getType()== Type.WORK){
                holder.reportJobNumber2.setText(wRepList.get(position).getWorkReport2().getJob().getJobNumber());
            }
            else{
                holder.reportJobNumber2.setVisibility(View.GONE);
            }

            if(wRepList.get(position).getWorkReport2().getType()== Type.WORK){
                holder.reportName2.setText(wRepList.get(position).getWorkReport2().getJob().getAddress().getName());
            }
            else{
                holder.reportName2.setVisibility(View.GONE);
            }

            if(wRepList.get(position).getWorkReport2().getType()== Type.WORK){
                holder.reportAddress2.setText(wRepList.get(position).getWorkReport2().getJob().getAddress().fullAddress());
            }
            else{
                holder.reportAddress2.setVisibility(View.GONE);
            }

            if(wRepList.get(position).getWorkReport2().getType()==Type.WORK) {
                holder.reportDescription2.setText(wRepList.get(position).getWorkReport2().getDescription());
            }
            else if(wRepList.get(position).getWorkReport2().getType()==Type.TRAINING){
                holder.reportDescription2.setText(wRepList.get(position).getWorkReport2().getType().name()+"\n"+
                        wRepList.get(position).getWorkReport2().getDescription());
            }else{
                holder.reportDescription.setText(wRepList.get(position).getWorkReport().getType().name());
            }





        }else{
            holder.jobNumberAndName2.setVisibility(View.GONE);
            holder.reportAddress2.setVisibility(View.GONE);
            holder.reportDescription2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return wRepList.size();
    }


    public class ViewH extends RecyclerView.ViewHolder {
        ConstraintLayout reportLayout;
        TextView reportDate, reportTimeIn, reportTimeOut, reportJobNumber, reportName, reportAddress, reportDescription;
        ImageView reportDeleteImage;
        LinearLayout jobNumberAndName2;
        TextView reportJobNumber2, reportName2, reportAddress2, reportDescription2;

        public ViewH(@NonNull View itemView) {
            super(itemView);
            reportLayout = itemView.findViewById(R.id.constraintLayoutLayoutWeeklyReport);
            reportDate = itemView.findViewById(R.id.dateLayoutWeeklyReport);
            reportTimeIn = itemView.findViewById(R.id.timeInLayoutWeeklyReport);
            reportTimeOut = itemView.findViewById(R.id.timeOutLayoutWeeklyReport);
            reportJobNumber = itemView.findViewById(R.id.jobNumberLayoutWeeklyReport);
            reportName = itemView.findViewById(R.id.nameLayoutWeeklyReport);
            reportAddress = itemView.findViewById(R.id.addressLayoutWeeklyReport);
            reportDescription = itemView.findViewById(R.id.descriptionLayoutWeeklyReport);
            reportDeleteImage = itemView.findViewById(R.id.imageDeleteLayoutWeeklyReport);
            jobNumberAndName2 = itemView.findViewById(R.id.jobNumberAndNameLinearLayoutLayoutWeeklyReport2);
            reportJobNumber2 = itemView.findViewById(R.id.jobNumberLayoutWeeklyReport2);
            reportName2 = itemView.findViewById(R.id.nameLayoutWeeklyReport2);
            reportAddress2 = itemView.findViewById(R.id.addressLayoutWeeklyReport2);
            reportDescription2 = itemView.findViewById(R.id.descriptionLayoutWeeklyReport2);

            reportDeleteImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onDeleteWeeklyReportClick(position);
                        }
                    }
                }
            });
        }
    }
}