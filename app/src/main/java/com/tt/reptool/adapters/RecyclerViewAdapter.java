package com.tt.reptool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tt.reptool.R;
import com.tt.reptool.javaClasses.Manager;

import java.util.List;

import static android.graphics.Color.TRANSPARENT;

/*
    RecyclerViewAdapter for managers
 */



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Manager> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecyclerViewAdapter(Context mContext, List<Manager> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manager,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.managerName.setText(mList.get(position).getName());
            holder.managerSurname.setText(mList.get(position).getSurname());
            if(!mList.get(position).isWorking()){
                holder.managerLayout.setBackgroundResource(R.color.colorPINK);
            }
            else {
                holder.managerLayout.setBackgroundResource(TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView managerName;
        TextView managerSurname;
        RelativeLayout managerLayout;
        ImageView managerDeleteImage;
        ImageView managerEditImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            managerName = itemView.findViewById(R.id.managerNameLayoutManager);
            managerSurname = itemView.findViewById(R.id.managerSurnameLayoutManager);
            managerLayout = itemView.findViewById(R.id.manager_layout);
            managerDeleteImage = itemView.findViewById(R.id.imageDeleteLayoutManager);
            managerEditImage = itemView.findViewById(R.id.imageEditLayoutManager);

            managerDeleteImage.setOnClickListener(new View.OnClickListener(){
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

            managerEditImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onEditClick(position);
                        }
                    }
                }
            });

        }
    }

}
