package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.interfaces.CheckInterneConnInterface;
import com.example.senamit.stationaryhutpro.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ProductForSaleAdapter extends RecyclerView.Adapter<ProductForSaleAdapter.ViewHolder> {

    private static final String TAG = ProductForSaleAdapter.class.getSimpleName();
    private static final String PRODUCT_INDEX = "product_index";

    //    private ProductItemClickListerner mListener;
    private Context context;
    private List<Product> product;
    String productId;
    Bundle bundle;

    //for snackbar load in productForsale class
    CheckInterneConnInterface mConnInterface;

    private static final String PRODUCT_KEY = "product_key";

    public ProductForSaleAdapter(Context context) {
        this.context = context;
    }

    public ProductForSaleAdapter(Context context, CheckInterneConnInterface mConnInterface) {
        this.context = context;
        this.mConnInterface = mConnInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_product_for_sale, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (product!= null){
            holder.txtProductNumber.setText(product.get(position).getProductNumber());
            holder.txtProductName.setText(product.get(position).getProductName());
            holder.txtProductPrice.setText(Double.toString(product.get(position).getProductPrice()));
            String imageUrl = product.get(position).getImageUrl();
            Picasso.with(context).load(imageUrl)
                    .placeholder(R.color.placeHolderTwo).into(holder.imageProduct);
            Log.i(TAG, "the position is "+position);
            Log.i(TAG, "the imageUrl is  "+imageUrl);
            Log.i(TAG, "the product number is "+product.get(position).getProductNumber());
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
    public void setProduct(List<Product> mProduct){
//        Collections.reverse(mProduct);
        product = mProduct;
        notifyDataSetChanged();
    }

    public Boolean checkIntenet(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
//            bundle.putInt(PRODUCT_INDEX, clickedItemIndex);
            Log.i(TAG, "inside createonclicklistener recycler adapter, productId is"+productId);
            if (checkIntenet()){
                Log.i(TAG, "internet is connected");
                Navigation.findNavController(view).navigate(R.id.action_productForSaleView_to_productDescription, bundle);
            }else {
                mConnInterface.funLoadSnackBar(false);
                Log.i(TAG, "Sorry u dont have internet connection");
            }
        }
    }
}
