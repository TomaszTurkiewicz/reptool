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
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;

import java.util.List;

public class RecyclerViewAdapterShortJob extends RecyclerView.Adapter<RecyclerViewAdapterShortJob.ViewHolder> {

    private List<Job> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onSelectedItemClick(int position);
    }

    public void setOnSelectedItemClickListener (OnItemClickListener listener){
        mListener=listener;
    }

    public RecyclerViewAdapterShortJob(Context mContext, List<Job> mList){
        this.mList=mList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mList.get(position).getJobType()==null||mList.get(position).getJobType()== JobType.INSTALLATION) {
            holder.jobNumber.setText(mList.get(position).getJobNumber());
            holder.jobManager.setVisibility(View.VISIBLE);
            holder.jobManager.setText(mList.get(position).getProjectManager().nameAndSurnameToString());
        }else{
            holder.jobNumber.setText(mList.get(position).getJobType().toString());
            holder.jobManager.setVisibility(View.GONE);
        }
        holder.jobName.setText(mList.get(position).getAddress().getName());
        holder.jobPostcode.setText(mList.get(position).getAddress().getPostCode());
        holder.jobAddress.setText(mList.get(position).getAddress().getStreet());
        holder.jobDescription.setText(mList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView jobNumber;
        TextView jobManager;
        TextView jobPostcode;
        TextView jobAddress;
        TextView jobDescription;
        ConstraintLayout jobLayout;
        ImageView jobEditImage;
        ImageView jobDeleteImage;
        TextView jobName;
        ImageView jobFinished;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            layout = itemView.findViewById(R.id.constraintLayoutLayoutJob);
            jobNumber = itemView.findViewById(R.id.jobNumberLayoutJob);
            jobManager = itemView.findViewById(R.id.jobManagerLayoutJob);
            jobName = itemView.findViewById(R.id.jobNameLayoutJob);
            jobPostcode = itemView.findViewById(R.id.jobPostcodeLayoutJob);
            jobAddress = itemView.findViewById(R.id.jobAddressLayoutJob);
            jobDescription = itemView.findViewById(R.id.jobDescriptionLayoutJob);
            jobLayout = itemView.findViewById(R.id.constraintLayoutLayoutJob);
            jobEditImage = itemView.findViewById(R.id.imageEditLayoutJob);
            jobDeleteImage = itemView.findViewById(R.id.imageDeleteLayoutJob);

            jobFinished = itemView.findViewById(R.id.imageFinishedJob);
            jobFinished.setVisibility(View.GONE);
            jobEditImage.setVisibility(View.GONE);
            jobDeleteImage.setVisibility(View.GONE);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onSelectedItemClick(position);
                        }
                    }
                }
            });


        }
    }

}
