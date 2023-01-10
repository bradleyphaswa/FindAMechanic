package com.android.findamechanic.sparePanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.findamechanic.R;
import com.android.findamechanic.UpdateAdModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class SpareHomeAdapter extends RecyclerView.Adapter<SpareHomeAdapter.ViewHolder> {

    private Context mContext;
    private List<UpdateAdModel> updateAdModelList;
    DatabaseReference databaseReference;
    public final static String ADD_RANDOM_ID =  "addRandomId";

    public SpareHomeAdapter(Context context, List<UpdateAdModel> updateAdModelList) {
        this.updateAdModelList = updateAdModelList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public SpareHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.spare_ad_cardview, parent, false);
        return new SpareHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpareHomeAdapter.ViewHolder holder, int position) {

        final UpdateAdModel updateAdModel = updateAdModelList.get(position);
        Glide.with(mContext).load(updateAdModel.getImageUri()).into(holder.imageView);

        holder.title.setText(updateAdModel.getTitle());
        updateAdModel.getRandomUid();
        holder.price.setText("Price: R " + updateAdModel.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateDelete_Ad.class);
                intent.putExtra(ADD_RANDOM_ID, updateAdModel.getRandomUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateAdModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.s_ad_image);
            title = itemView.findViewById(R.id.s_ad_title);
            price = itemView.findViewById(R.id.s_ad_price);
        }
    }
}
