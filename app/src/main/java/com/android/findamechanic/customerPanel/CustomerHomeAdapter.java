package com.android.findamechanic.customerPanel;

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
import com.android.findamechanic.ViewAdd;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mContext;
    private List<UpdateAdModel>updateAdModelList;
    DatabaseReference databaseReference;
    public final static String ADD_RANDOM_ID =  "addRandomId";
    public final static String SPARE_ID =  "spareId";

    public CustomerHomeAdapter(Context context, List<UpdateAdModel>updateAdModelList)
    {
        this.updateAdModelList = updateAdModelList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.customer_ad_cardview, parent, false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {


        final UpdateAdModel updateAdModel = updateAdModelList.get(position);
        Glide.with(mContext).load(updateAdModel.getImageUri()).into(holder.imageView);

        holder.addTitle.setText(updateAdModel.getTitle());
        updateAdModel.getRandomUid();
        updateAdModel.getSpareId();
        holder.price.setText("Price: R " + updateAdModel.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewAdd.class);
                intent.putExtra(ADD_RANDOM_ID, updateAdModel.getRandomUid());
                intent.putExtra(SPARE_ID, updateAdModel.getSpareId());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return updateAdModelList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView addTitle, price;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addTitle = itemView.findViewById(R.id.adTitle);
            imageView = itemView.findViewById(R.id.add_image);
            price = itemView.findViewById(R.id.adPrice);
        }
    }
}
