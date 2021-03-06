package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {
    private static final String TAG = CategoryProductAdapter.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_INDEX = "product_index";

    private Context context;
    private List<Product> product;
    String productId;
    Bundle bundle;

    public CategoryProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_product_for_sale, parent, false);
        return new CategoryProductAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (product!= null){
            holder.txtProductNumber.setText(product.get(position).getProductNumber());
            holder.txtProductName.setText(product.get(position).getProductName());
            holder.txtProductPrice.setText(Double.toString(product.get(position).getProductPrice()));
            String imageUrl = product.get(position).getImageUrl();
            Picasso.with(context).load(imageUrl).into(holder.imageProduct);
//            Log.i(TAG, "the position is "+position);
//            Log.i(TAG, "the imageUrl is  "+imageUrl);
//            Log.i(TAG, "the product number is "+product.get(position).getProductNumber());
        }else {
            holder.txtProductNumber.setText("no product found");
            holder.txtProductName.setText("no product found");
            holder.txtProductPrice.setText("no product found");
        }
    }

    @Override
    public int getItemCount() {
        if (product!= null){
//            Log.i(TAG, "the size of product is "+product.size());
            return product.size();
        }else{
            return 0;
        }
    }

    public void setProduct(List<Product> products) {
        product = products;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtProductNumber;
        TextView txtProductName;
        TextView txtProductPrice;
        ImageView imageProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemIndex= getAdapterPosition();
            productId= product.get(clickedItemIndex).getProductNumber();
            bundle = new Bundle();
            bundle.putString(PRODUCT_KEY, productId);
            bundle.putInt(PRODUCT_INDEX, clickedItemIndex);
//            Log.i(TAG, "inside createonclicklistener recycler adapter, productId is"+productId);
            Navigation.findNavController(view).navigate(R.id.action_categoryProductView_to_productDescription, bundle);
        }
    }
}
