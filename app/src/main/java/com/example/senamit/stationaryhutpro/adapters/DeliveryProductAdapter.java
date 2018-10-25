package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryProductAdapter extends RecyclerView.Adapter<DeliveryProductAdapter.ViewHolder> {

    private static final String TAG = DeliveryProductAdapter.class.getSimpleName();
    private Context context;
    private List<UserCart> cartProductList;
    private List<Product> productList;
    private CartProductAdapter.ButtonClickInterface btnClickinterface;

    public DeliveryProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.cardview_order_deliverey,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cartProductList!= null){
            Log.i(TAG, "inside onBindViewHolder "+cartProductList.get(position).getProductNumber());
            holder.txtProductNumber.setText(cartProductList.get(position).getProductNumber());
            holder.txtProductPrice.setText(Integer.toString(cartProductList.get(position).getProductPrice()));
            holder.txtProductName.setText(cartProductList.get(position).getProductName());
            holder.txtProductQuantity.setText(Integer.toString(cartProductList.get(position).getQuantity()));
            Picasso.with(context).load(cartProductList.get(position).getImageUrl()).into(holder.imageProduct);
            int quantity = cartProductList.get(position).getQuantity();
            int price = cartProductList.get(position).getProductPrice();
            int totalPrice = (quantity * price);
            Log.i(TAG,"the total price is "+totalPrice);
            holder.txtTotalQuantity.setText("Total price of ("+quantity+" items)");
            holder.txtTotalPrice.setText(Integer.toString(totalPrice));

        }
    }

    @Override
    public int getItemCount() {
        if (cartProductList != null){
            return cartProductList.size();
        }else {
            return 0;
        }
    }

    public void setCartProduct(List<UserCart> cartProductList){
        this.cartProductList = cartProductList;
        notifyDataSetChanged();
        Log.i(TAG, "inside setCartProduct method");
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageProduct;
        TextView txtProductName;
        TextView txtProductNumber;
        TextView txtProductPrice;
        TextView txtProductQuantity;
        TextView txtTotalPrice;
        TextView txtTotalQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTotalQuantity = itemView.findViewById(R.id.txtTotalQuantity);
        }
    }
}
