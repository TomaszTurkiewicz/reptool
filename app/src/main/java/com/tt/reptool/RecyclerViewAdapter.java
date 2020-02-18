package com.tt.reptool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Manager> mList = new ArrayList<>(); //TODO why redundant?
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<Manager> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manager,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.managerName.setText(mList.get(position).getName());
            holder.managerSurname.setText(mList.get(position).getSurname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView managerName;
        TextView managerSurname;
        LinearLayout managerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            managerName = itemView.findViewById(R.id.managerNameLayoutManager);
            managerSurname = itemView.findViewById(R.id.managerSurnameLayoutManager);
            managerLayout = itemView.findViewById(R.id.manager_layout);
        }
    }

}
//TODO change recyclerView (has to show that can be scrolled)
//TODO change single item in recylerview (frame, background color etc)