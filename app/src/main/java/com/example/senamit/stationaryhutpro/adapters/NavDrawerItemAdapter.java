package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.interfaces.ProductCategoryInterface;
import com.example.senamit.stationaryhutpro.models.ProductCategory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class NavDrawerItemAdapter extends RecyclerView.Adapter<NavDrawerItemAdapter.ViewHolder> {

    private static final String TAG = NavDrawerItemAdapter.class.getSimpleName();
    private Context context;
    private List<ProductCategory> productCategories;
    private ProductCategoryInterface mInterface;

    public NavDrawerItemAdapter(Context context, ProductCategoryInterface mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cardview_navdrawer_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (productCategories!= null){
            holder.txtItemName.setText(productCategories.get(position).getProductItem());
        }
    }

    @Override
    public int getItemCount() {
        if (productCategories!= null){
//            Log.i(TAG, "total item count in adapter is "+productCategories.size());
            return productCategories.size();
        }else {
            return 0;
        }
    }

    public void setProductItem(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            Log.i(TAG, "inside the viewholder ");
            txtItemName = itemView.findViewById(R.id.txtItemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemIndex = getAdapterPosition();
            String productCategory = productCategories.get(clickedItemIndex).getProductItem();
            Log.i(TAG, "the product category in adapter is "+productCategory);
            Bundle bundle = new Bundle();
            bundle.putString("productCategory", productCategory);
            mInterface.funSetProductCategory(productCategory);
            Navigation.findNavController(view).navigate(R.id.action_navDrawerStationaryItem_to_categoryProductView, bundle);
        }
    }
}
