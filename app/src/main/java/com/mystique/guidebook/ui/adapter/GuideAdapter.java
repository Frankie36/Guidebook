package com.mystique.guidebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.mystique.guidebook.R;
import com.mystique.guidebook.model.Guide;

import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private Context context;
    private List<Guide> guideList;

    public GuideAdapter(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
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
        Glide.with(context)
                .load(guide.url)
                .placeholder(R.drawable.ic_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(0.7f)
                .into(holder.imgGuide);

        holder.tvName.setText(guide.name);
        holder.tvEndDate.setText(guide.endDate);
        holder.tvCity.setText(guide.venue.city);
        holder.tvState.setText(guide.venue.state);
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
}
