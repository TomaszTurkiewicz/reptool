package com.tt.reptool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterJob extends RecyclerView.Adapter<RecyclerViewAdapterJob.ViewHolder> {

    private List<Job> mList;
    private Context mContext;
    private OnItemClickListener mListener;



    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecyclerViewAdapterJob(Context mContext, List<Job> mList){
        this.mList = mList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterJob.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterJob.ViewHolder holder, int position) {
        holder.jobNumber.setText(mList.get(position).getJobNumber());
        holder.jobManager.setText(mList.get(position).getProjectManager().nameAndSurnameToString());
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

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            jobNumber = itemView.findViewById(R.id.jobNumberLayoutJob);
            jobManager = itemView.findViewById(R.id.jobManagerLayoutJob);
            jobPostcode = itemView.findViewById(R.id.jobPostcodeLayoutJob);
            jobAddress = itemView.findViewById(R.id.jobAddressLayoutJob);
            jobDescription = itemView.findViewById(R.id.jobDescriptionLayoutJob);
            jobLayout = itemView.findViewById(R.id.constraintLayoutLayoutJob);
            jobEditImage = itemView.findViewById(R.id.imageEditLayoutJob);
            jobDeleteImage = itemView.findViewById(R.id.imageDeleteLayoutJob);

            jobEditImage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onEditClick(position);
                        }
                    }
                }
            });
            jobDeleteImage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });


        }
    }

}
