package com.demo.retrofit_database_demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.retrofit_database_demo.R;
import com.demo.retrofit_database_demo.models.MovieResponseBean;

import java.util.List;

/**
 * Created by ucreateuser on 26-Sep-17.
 */

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    Context mContext;
    List<MovieResponseBean.Result> mDataList;

    public UserRecyclerAdapter(Context ctx, List<MovieResponseBean.Result> mList) {
        mContext = ctx;
        mDataList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.user_list_items, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvTitle.setText(mDataList.get(position).getTitle());
        holder.tvReleaseDate.setText(mDataList.get(position).getReleaseDate());
        holder.tvDes.setText(mDataList.get(position).getOverview());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDes, tvReleaseDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_value);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des_value);
            tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date_value);
        }
    }


}
