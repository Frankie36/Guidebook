package com.mystique.guidebook.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mystique.guidebook.Constants;
import com.mystique.guidebook.R;
import com.mystique.guidebook.model.Guide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private List<Guide> guideList;

    @Inject
    public GuideAdapter() {
        this.guideList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_guide, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guide guide = guideList.get(position);

        //display image using glide library
        Glide.with(holder.itemView)
                .load(guide.icon)
                .placeholder(R.drawable.ic_placeholder)
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(0.7f)
                .into(holder.imgGuide);

        holder.tvName.setText(guide.name);
        holder.tvEndDate.setText(guide.endDate);

        //city and state don't always display data
        //display "N/A" i.e Not Available when they do not
        holder.tvCity.setText(!TextUtils.isEmpty(guide.venue.city) ? guide.venue.city : Constants.NA);
        holder.tvState.setText(!TextUtils.isEmpty(guide.venue.state) ? guide.venue.state : Constants.NA);
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEndDate, tvCity, tvState;
        ImageView imgGuide = itemView.findViewById(R.id.imgGuide);

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvState = itemView.findViewById(R.id.tvState);
        }
    }

    public void setData(List<Guide> guideList) {
        this.guideList.addAll(guideList);
        notifyDataSetChanged();
    }

}
