package com.example.pexelimages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PexelAdapter extends RecyclerView.Adapter<PexelAdapter.PexelViewHolder> {

    private Context context;
    private List<PexelImage> pexelImageList;
    private GridLayoutManager layoutManager;

    private final int GRID_SPAN_COUNT = 2;
    private final int LIST_SPAN_COUNT = 1;

    //constructor
    public PexelAdapter(Context context, List<PexelImage> pexelImageList, GridLayoutManager layoutManager) {
        this.context = context;
        this.pexelImageList = pexelImageList;
        this.layoutManager = layoutManager;
    }

    /**
     * detect what is the current layout the user is seeing
     * @param position position of item
     * @return 1 if list layout, 2 if currently showing grid layout
     */
    @Override
    public int getItemViewType(int position) {
        int spanCount = layoutManager.getSpanCount();
        if (spanCount == GRID_SPAN_COUNT) {
            return GRID_SPAN_COUNT;
        } else {
            return LIST_SPAN_COUNT;
        }
    }

    @NonNull
    @Override
    public PexelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==GRID_SPAN_COUNT){ //inflate different layout xml files for grid and list layout
            view = LayoutInflater.from(context).inflate(R.layout.grid_view, parent,false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.list_view, parent,false);
        }
        return new PexelViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull PexelViewHolder holder, int position) {
        PexelImage pexelImage = pexelImageList.get(position);
        String description = "Taken by: \n" + pexelImage.getPhotographer();

        Glide.with(context).load(pexelImage.getMediumUrl()).into(holder.imageView); //put image loaded from url to imageView
        holder.description.setText(description);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //go to new activity where bigger image and more descriptions can be found
                context.startActivity(new Intent(context,PexelDetails.class)
                        .putExtra("pexelImage", pexelImage)); //pass the pexelImage object that was clicked to the next activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return pexelImageList.size();
    }


    public class PexelViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView description;
        ConstraintLayout item;
        public PexelViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType == GRID_SPAN_COUNT){
                this.imageView = itemView.findViewById(R.id.imageGridView);
                this.description = itemView.findViewById(R.id.descriptionGridView);
                this.item = itemView.findViewById(R.id.gridViewItem);
            }
            else{
                this.imageView = itemView.findViewById(R.id.imageListView);
                this.description = itemView.findViewById(R.id.descriptionListView);
                this.item = itemView.findViewById(R.id.listViewItem);
            }

        }
    }
}
