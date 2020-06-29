package com.floyd.ecigmanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.activities.AromeActivity;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Arome;
import com.floyd.ecigmanagement.services.AromeService;
import com.floyd.ecigmanagement.uio.AromeUio;
import com.floyd.ecigmanagement.utils.Constants;
import com.floyd.ecigmanagement.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.floyd.ecigmanagement.enums.Level.ERROR;

public class AromeAdapter extends RecyclerView.Adapter<AromeAdapter.AromeViewHolder> {

    private static final String TAG = "AROME_ADAPTER";

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the arome in a list
    private List<AromeUio> aromeUioList;

    AromeService aromeService;

    //getting the context and arome list with constructor
    public AromeAdapter(Context mCtx, List<AromeUio> aromeUioList) {
        this.mCtx = mCtx;
        this.aromeUioList = aromeUioList;
    }

    @Override
    public AromeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_arome_item, null);
        return new AromeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AromeViewHolder holder, int position) {
        //getting the arome of the specified position
        AromeUio aromeUioItem = aromeUioList.get(position);

        //binding the data with the viewholder views
        String url = constructAromeImageUrl(aromeUioItem.getId());
        Picasso.get().load(url).placeholder(R.drawable.ic_menu_arome).into(holder.aromeImageView);

        holder.textViewQuantity.setText(String.valueOf(aromeUioItem.getQuantity()));
        holder.textViewNote.setText(String.valueOf(aromeUioItem.getNote()));
        holder.textViewCapacity.setText(String.valueOf(aromeUioItem.getCapacity()));
        holder.textViewBrand.setText(aromeUioItem.getBrand());
        holder.textViewTaste.setText(aromeUioItem.getTaste());

        // -- Initialize services
        aromeService = ClientInstance.getAromeService();

        // add button listener
        holder.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button Add arôme clicked; AromeID = " + aromeUioItem.getId());
                int newQuantity = aromeUioItem.getQuantity() + 1;
                Call<Arome> call = aromeService.updateAromeQuantity(newQuantity, aromeUioItem.getId());

                call.enqueue(new Callback<Arome>() {

                    @Override
                    public void onResponse(Call<Arome> call, Response<Arome> response) {
                        if(response.isSuccessful()) {
                            Arome aromeUpdated = response.body();
                            aromeUioList.get(position).setQuantity(aromeUpdated.getQuantity());
                            holder.textViewQuantity.setText(String.valueOf(aromeUioList.get(position).getQuantity()));
                            Log.d(TAG, "New quantity : " + aromeUpdated.getQuantity());
                        } else {
                            Log.d(TAG, response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Arome> call, Throwable t) {
                        Log.e(TAG, "Error when updating arome quantity : " + t);
                    }
                });
            }
        });

        holder.imageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button Remove arôme clicked; AromeID = " + aromeUioItem.getId());

                if (aromeUioItem.getQuantity() > 0 ) {
                    int newQuantity = aromeUioItem.getQuantity() - 1;
                    Call<Arome> call = aromeService.updateAromeQuantity(newQuantity, aromeUioItem.getId());

                    call.enqueue(new Callback<Arome>() {

                        @Override
                        public void onResponse(Call<Arome> call, Response<Arome> response) {
                            if(response.isSuccessful()) {
                                Arome aromeUpdated = response.body();
                                aromeUioList.get(position).setQuantity(aromeUpdated.getQuantity());
                                holder.textViewQuantity.setText(String.valueOf(aromeUioList.get(position).getQuantity()));
                                Log.d(TAG, "New quantity : " + aromeUpdated.getQuantity());
                            } else {
                                Log.d(TAG, response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Arome> call, Throwable t) {
                            Log.e(TAG, "Error when updating arome quantity : " + t);
                        }
                    });
                }

            }
        });
    }

    private String constructAromeImageUrl(int id) {
        return Constants.BACK_AROME_IMAGE_URL + id + ".jpg";
    }

    @Override
    public int getItemCount() {
        return aromeUioList.size();
    }


    class AromeViewHolder extends RecyclerView.ViewHolder {

        ImageView aromeImageView;
        TextView textViewTaste, textViewCapacity, textViewBrand, textViewNote, textViewQuantity;
        ImageButton imageButtonRemove, imageButtonAdd;

        public AromeViewHolder(View itemView) {
            super(itemView);

            aromeImageView = itemView.findViewById(R.id.aromeImageView);

            textViewTaste = itemView.findViewById(R.id.aromeTaste);
            textViewBrand = itemView.findViewById(R.id.aromeBrand);
            textViewCapacity = itemView.findViewById(R.id.aromeCapacity);
            textViewNote = itemView.findViewById(R.id.aromeNote);
            textViewQuantity = itemView.findViewById(R.id.aromeQuantity);

            imageButtonAdd = itemView.findViewById(R.id.aromeQuantityAdd);
            imageButtonRemove = itemView.findViewById(R.id.aromeQuantityRemove);

        }
    }
}
