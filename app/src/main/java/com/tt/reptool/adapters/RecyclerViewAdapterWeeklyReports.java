package com.tt.reptool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        void onEditWeeklyReportClick (int position);
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
        ViewH holder = new ViewH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterWeeklyReports.ViewH holder, int position) {
        holder.reportDate.setText(wRepList.get(position).dateToString());
        holder.reportTimeIn.setText(wRepList.get(position).showTimeInToString());
        holder.reportTimeOut.setText(wRepList.get(position).showTimeOutToString());
//        holder.reportJobNumber.setText(wRepList.get(position).getJob().getJobNumber());

        if(wRepList.get(position).getType()== Type.WORK){
            holder.reportJobNumber.setText(wRepList.get(position).getWorkReport().getJob().getJobNumber());
        }
        else{
            holder.reportJobNumber.setVisibility(View.GONE);
        }

//        holder.reportName.setText(wRepList.get(position).getJob().getAddress().getName());

        if(wRepList.get(position).getType()== Type.WORK){
            holder.reportName.setText(wRepList.get(position).getWorkReport().getJob().getAddress().getName());
        }
        else{
            holder.reportName.setVisibility(View.GONE);
        }


//        holder.reportAddress.setText(wRepList.get(position).getJob().getAddress().getFullAddress());

        if(wRepList.get(position).getType()== Type.WORK){
            holder.reportAddress.setText(wRepList.get(position).getWorkReport().getJob().getAddress().getFullAddress());
        }
        else{
            holder.reportAddress.setVisibility(View.GONE);
        }

//        holder.reportDescription.setText(wRepList.get(position).getDescription());

        if(wRepList.get(position).getType()== Type.WORK){
            holder.reportDescription.setText(wRepList.get(position).getWorkReport().getDescription());
        }
        else{
            holder.reportDescription.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return wRepList.size();
    }


    public class ViewH extends RecyclerView.ViewHolder {
        ConstraintLayout reportLayout;
        TextView reportDate, reportTimeIn, reportTimeOut, reportJobNumber, reportName, reportAddress, reportDescription;
        ImageView reportDeleteImage, reportEditImage;

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
            reportEditImage = itemView.findViewById(R.id.imageEditLayoutWeeklyReport);

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

            reportEditImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onEditWeeklyReportClick(position);
                        }
                    }
                }
            });

        }
    }
}