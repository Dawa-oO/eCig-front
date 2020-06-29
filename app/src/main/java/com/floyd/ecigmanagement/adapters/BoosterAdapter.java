package com.floyd.ecigmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.uio.BoosterUio;

import java.util.List;

public class BoosterAdapter extends RecyclerView.Adapter<BoosterAdapter.BoosterViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the arome in a list
    private List<BoosterUio> boosterUioList;

    //getting the context and arome list with constructor
    public BoosterAdapter(Context mCtx, List<BoosterUio> boosterUioList) {
        this.mCtx = mCtx;
        this.boosterUioList = boosterUioList;
    }

    @Override
    public BoosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_booster_item, null);
        return new BoosterAdapter.BoosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoosterViewHolder holder, int position) {
        //getting the booster of the specified position
        BoosterUio boosterUioItem = boosterUioList.get(position);

        //binding the data with the viewholder views
        holder.boosterImageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_menu_booster, null));

        holder.textViewQuantity.setText(String.valueOf(boosterUioItem.getQuantity()));
        holder.textViewCapacity.setText(String.valueOf(boosterUioItem.getCapacity()));
        holder.textViewPgVg.setText(boosterUioItem.getPgvg());
    }

    @Override
    public int getItemCount() {
        return boosterUioList.size();
    }


    class BoosterViewHolder extends RecyclerView.ViewHolder {

        ImageView boosterImageView;
        TextView textViewPgVg, textViewCapacity, textViewQuantity;
        ImageButton imageButtonRemove, imageButtonAdd;

        public BoosterViewHolder(View itemView) {
            super(itemView);

            boosterImageView = itemView.findViewById(R.id.boosterImageView);

            textViewPgVg = itemView.findViewById(R.id.boosterPgVg);
            textViewCapacity = itemView.findViewById(R.id.boosterCapacity);
            textViewQuantity = itemView.findViewById(R.id.boosterQuantity);

            imageButtonAdd = itemView.findViewById(R.id.boosterQuantityAdd);
            imageButtonRemove = itemView.findViewById(R.id.boosterQuantityRemove);
        }

    }
}
