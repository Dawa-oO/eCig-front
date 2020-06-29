package com.floyd.ecigmanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Booster;
import com.floyd.ecigmanagement.services.BoosterService;
import com.floyd.ecigmanagement.uio.BoosterUio;
import com.floyd.ecigmanagement.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoosterAdapter extends RecyclerView.Adapter<BoosterAdapter.BoosterViewHolder> {

    private static final String TAG = "BOOSTER_ADAPTER";

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the arome in a list
    private List<BoosterUio> boosterUioList;

    BoosterService boosterService;

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
        String url = constructBoosterImageUrl(boosterUioItem.getId());
        Picasso.get().load(url).placeholder(R.drawable.ic_menu_booster).into(holder.boosterImageView);

        holder.textViewQuantity.setText(String.valueOf(boosterUioItem.getQuantity()));
        holder.textViewCapacity.setText(String.valueOf(boosterUioItem.getCapacity()));
        holder.textViewPgVg.setText(boosterUioItem.getPgvg());

        // -- Initialize services
        boosterService = ClientInstance.getBoosterService();

        // add button listener
        holder.imageButtonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Add booster clicked; BoosterID = " + boosterUioItem.getId());
                int newQuantity = boosterUioItem.getQuantity() + 1;
                Call<Booster> call = boosterService.updateBoosterQuantity(newQuantity, boosterUioItem.getId());

                call.enqueue(new Callback<Booster>() {
                    @Override
                    public void onResponse(Call<Booster> call, Response<Booster> response) {
                        if(response.isSuccessful()) {
                            Booster boosterUpdated = response.body();
                            boosterUioList.get(position).setQuantity(boosterUpdated.getQuantity());
                            holder.textViewQuantity.setText(String.valueOf(boosterUioList.get(position).getQuantity()));
                            Log.d(TAG, "New quantity : " + boosterUpdated.getQuantity());
                        } else {
                            Log.d(TAG, response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Booster> call, Throwable t) {
                        Log.e(TAG, "Error when updating arome quantity : " + t);
                    }
                });
            }
        });

        holder.imageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Remove booster clicked; BoosterID = " + boosterUioItem.getId());

                if (boosterUioItem.getQuantity() >  0) {
                    int newQuantity = boosterUioItem.getQuantity() - 1;
                    Call<Booster> call = boosterService.updateBoosterQuantity(newQuantity, boosterUioItem.getId());

                    call.enqueue(new Callback<Booster>() {
                        @Override
                        public void onResponse(Call<Booster> call, Response<Booster> response) {
                            if (response.isSuccessful()) {
                                Booster boosterUpdated = response.body();
                                boosterUioList.get(position).setQuantity(boosterUpdated.getQuantity());
                                holder.textViewQuantity.setText(String.valueOf(boosterUioList.get(position).getQuantity()));
                                Log.d(TAG, "New quantity : " + boosterUpdated.getQuantity());
                            } else {
                                Log.d(TAG, response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Booster> call, Throwable t) {
                            Log.e(TAG, "Error when updating arome quantity : " + t);
                        }
                    });
                }
            }
        });
    }

    private String constructBoosterImageUrl(int id) {
        return Constants.BACK_BOOSTER_IMAGE_URL + id + ".jpg";
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
