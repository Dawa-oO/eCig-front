package com.floyd.ecigmanagement.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.floyd.ecigmanagement.R;
import com.floyd.ecigmanagement.clients.ClientInstance;
import com.floyd.ecigmanagement.models.Preparation;
import com.floyd.ecigmanagement.services.PreparationService;
import com.floyd.ecigmanagement.uio.PreparationUio;
import com.floyd.ecigmanagement.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreparationAdapter extends RecyclerView.Adapter<PreparationAdapter.PreparationViewHolder> {

    private static final String TAG = "PREPARATION_ADAPTER";

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the preparation in a list
    private List<PreparationUio> preparationUioList;

    PreparationService preparationService;

    //getting the context and preparation list with constructor
    public PreparationAdapter(Context mCtx, List<PreparationUio> preparationUioList) {
        this.mCtx = mCtx;
        this.preparationUioList = preparationUioList;
    }

    @Override
    public PreparationAdapter.PreparationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_preparation_item, null);
        return new PreparationAdapter.PreparationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreparationAdapter.PreparationViewHolder holder, int position) {
        //getting the preparation of the specified position
        PreparationUio preparationUioItem = preparationUioList.get(position);

        //binding the data with the viewholder views
        String url = constructPreparationImageUrl(preparationUioItem.getAromeId());
        Picasso.get().load(url).placeholder(R.drawable.ic_menu_arome).into(holder.preparationImageView);

        holder.textViewTaste.setText(preparationUioItem.getArome());
        holder.textViewQuantity.setText(String.valueOf(preparationUioItem.getQuantity()));
        holder.textViewCapacity.setText(String.valueOf(preparationUioItem.getCapacity()));

        // -- Initialize services
        preparationService = ClientInstance.getPreparationService();

        // add button listener
        holder.imageButtonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Add preparation clicked; PreparationID = " + preparationUioItem.getId());
                int newQuantity = preparationUioItem.getQuantity() + 1;
                Call<Preparation> call = preparationService.updatePreparationQuantity(newQuantity, preparationUioItem.getId());

                call.enqueue(new Callback<Preparation>() {
                    @Override
                    public void onResponse(Call<Preparation> call, Response<Preparation> response) {
                        if(response.isSuccessful()) {
                            Preparation preparationUpdated = response.body();
                            preparationUioList.get(position).setQuantity(preparationUpdated.getQuantity());
                            holder.textViewQuantity.setText(String.valueOf(preparationUioList.get(position).getQuantity()));
                            Log.d(TAG, "New quantity : " + preparationUpdated.getQuantity());
                        } else {
                            Log.d(TAG, response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Preparation> call, Throwable t) {
                        Log.e(TAG, "Error when updating preparation quantity : " + t);
                    }
                });
            }
        });

        holder.imageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Remove preparation clicked; PreparationID = " + preparationUioItem.getId());

                if (preparationUioItem.getQuantity() >  0) {
                    int newQuantity = preparationUioItem.getQuantity() - 1;
                    Call<Preparation> call = preparationService.updatePreparationQuantity(newQuantity, preparationUioItem.getId());

                    call.enqueue(new Callback<Preparation>() {
                        @Override
                        public void onResponse(Call<Preparation> call, Response<Preparation> response) {
                            if (response.isSuccessful()) {
                                Preparation preparationUpdated = response.body();
                                preparationUioList.get(position).setQuantity(preparationUpdated.getQuantity());
                                holder.textViewQuantity.setText(String.valueOf(preparationUioList.get(position).getQuantity()));
                                Log.d(TAG, "New quantity : " + preparationUpdated.getQuantity());
                            } else {
                                Log.d(TAG, response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Preparation> call, Throwable t) {
                            Log.e(TAG, "Error when updating preparation quantity : " + t);
                        }
                    });
                }
            }
        });

        holder.imageButtonPreparationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button delete preparation clicked; PreparationID = " + preparationUioItem.getId());

                // -- Yes / No modal
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if (preparationUioItem.getId() > 0) {
                                    Call<Boolean> call = preparationService.deletePreparation(preparationUioItem.getId());

                                    call.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (response.isSuccessful()) {
                                                Boolean responseDelete = response.body();
                                                if (responseDelete.booleanValue()) {
                                                    Log.d(TAG, "Preparation is deleted");
                                                    preparationUioList.remove(position);
                                                    notifyItemRemoved(position);
                                                } else {
                                                    Log.d(TAG, "An error occured when deleting preparation");
                                                }
                                            } else {
                                                Log.d(TAG, response.errorBody().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Log.e(TAG, "Error when deleting preparation quantity : " + t);
                                        }
                                    });
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setMessage("Êtes-vous sûr ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private String constructPreparationImageUrl(int id) {
        return Constants.BACK_AROME_IMAGE_URL + id + ".jpg";
    }

    @Override
    public int getItemCount() {
        return preparationUioList.size();
    }


    class PreparationViewHolder extends RecyclerView.ViewHolder {

        ImageView preparationImageView;
        TextView textViewTaste, textViewCapacity, textViewQuantity;
        ImageButton imageButtonRemove, imageButtonAdd, imageButtonPreparationDelete;

        public PreparationViewHolder(View itemView) {
            super(itemView);

            preparationImageView = itemView.findViewById(R.id.preparationImageView);

            textViewTaste = itemView.findViewById(R.id.preparationTaste);
            textViewCapacity = itemView.findViewById(R.id.preparationCapacity);
            textViewQuantity = itemView.findViewById(R.id.preparationQuantity);

            imageButtonAdd = itemView.findViewById(R.id.preparationQuantityAdd);
            imageButtonRemove = itemView.findViewById(R.id.preparationQuantityRemove);
            imageButtonPreparationDelete = itemView.findViewById(R.id.preparationDelete);
        }

    }
}
