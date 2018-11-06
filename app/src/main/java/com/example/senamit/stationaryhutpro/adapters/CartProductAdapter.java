package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.interfaces.CheckInterneConnInterface;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private static final String TAG = CartProductAdapter.class.getSimpleName();

    private Context context;
    private List<UserCart> cartProductList;
    private List<Product> productList;
    private ButtonClickInterface btnClickinterface;
    private String productNumberCheck;

    CheckInterneConnInterface mConnInterface;


    public CartProductAdapter(Context context) {
        this.context = context;
    }

//    public CartProductAdapter(Context context, ButtonClickInterface btnClickinterface) {
//        this.context = context;
//        this.btnClickinterface = btnClickinterface;
//    }

    public CartProductAdapter(Context context, ButtonClickInterface btnClickinterface, CheckInterneConnInterface mConnInterface) {
        this.context = context;
        this.btnClickinterface = btnClickinterface;
        this.mConnInterface = mConnInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cardview_cart_product,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cartProductList!= null){
//            Log.i(TAG, "inside onBindViewHolder "+cartProductList.get(position).getProductNumber());
            holder.txtProductNumber.setText(cartProductList.get(position).getProductNumber());
            holder.txtProductPrice.setText(Integer.toString(cartProductList.get(position).getProductPrice()));
            holder.txtProductName.setText(cartProductList.get(position).getProductName());
            holder.txtProductQA.setText(Integer.toString(cartProductList.get(position).getQuantity()));
            holder.txtMinimumQty.setText(Integer.toString(cartProductList.get(position).getMinimumOrder()));
            Picasso.with(context).load(cartProductList.get(position).getImageUrl()).into(holder.imageProduct);
            int quantity = cartProductList.get(position).getQuantity();
            int price = cartProductList.get(position).getProductPrice();
            int totalPrice = (quantity * price);
//            Log.i(TAG,"the total price is "+totalPrice);
            holder.txtTotalQuantity.setText("Total price of ("+quantity+" items)");
            holder.txtTotalPrice.setText(Integer.toString(totalPrice));
        }

        else {
//            Log.i(TAG, "the cartProductList is null");
            holder.txtProductNumber.setText("No Product found");
            holder.txtProductPrice.setText("price not loaded");
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
//        Log.i(TAG, "inside setCartProduct method");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageProduct;
        TextView txtProductName;
        TextView txtProductNumber;
        TextView txtProductPrice;
        TextView txtTotalPrice;
        TextView txtTotalQuantity;
        TextView txtMinimumQty;
        TextView txtOutOfStock;
        EditText txtProductQA;
        CardView cardView;
        Button btnRemove;
        Button btnSaveForLater;
        Button btnAddQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductQA = itemView.findViewById(R.id.txtProductQA);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTotalQuantity = itemView.findViewById(R.id.txtTotalQuantity);
            txtMinimumQty = itemView.findViewById(R.id.txtMinimumOrder);
            txtOutOfStock = itemView.findViewById(R.id.txtOutOfStock);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnSaveForLater = itemView.findViewById(R.id.btnSaveForLater);
            btnAddQuantity = itemView.findViewById(R.id.btnQuantity);
            cardView = itemView.findViewById(R.id.cardviewupdate);
            btnRemove.setOnClickListener(this);
            btnAddQuantity.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position;
            String productNumber;
            switch (view.getId()){
                case R.id.btnRemove:
                    if (checkIntenet()){
                        position = getAdapterPosition();
                        productNumber = cartProductList.get(position).getProductNumber();
                        btnClickinterface.funRemoveBtnClick(productNumber, position);
                    }else {
                        mConnInterface.funLoadSnackBar(false);
                    }

                    break;
                case R.id.btnQuantity:
                    position = getAdapterPosition();
                    if (checkIntenet()){
                    productNumber = cartProductList.get(position).getProductNumber();
                  int quantity = Integer.parseInt(txtProductQA.getText().toString());
                  int price = Integer.parseInt(txtProductPrice.getText().toString());
                  int minimumOrder = cartProductList.get(position).getMinimumOrder();
                  int availableQuantity = cartProductList.get(position).getAvailableQuantity();



                  if (quantity<=availableQuantity){
//                      Log.i(TAG, "the available quantity of product is "+availableQuantity);
                      if (quantity>minimumOrder){
                          btnClickinterface.funAddProductQuantity(productNumber, quantity,minimumOrder, price);
                      }else {
                          txtProductQA.setText(Integer.toString(minimumOrder));
                          btnClickinterface.funAddProductQuantity(productNumber, quantity,minimumOrder, price);
//                          Log.i(TAG, "quantity is less than the minimumorder");
                      }
                  }else {
//                      Log.i(TAG, "the available quantity of product is "+availableQuantity);
                      txtProductQA.setText(Integer.toString(minimumOrder));
                      btnClickinterface.funAvailableQuantity(false);
//                      Log.i(TAG, "sorry, we dont have such a huge number of products available");
                  }
                    }else {
                        int minimumOrder = cartProductList.get(position).getMinimumOrder();
                        txtProductQA.setText(Integer.toString(minimumOrder));
                        mConnInterface.funLoadSnackBar(false);
                    }
                    break;
                default:
//                    Log.i(TAG, "select any other option");
            }
        }
    }


    public interface ButtonClickInterface{
        void funRemoveBtnClick(String productNumber, int position);
        void funAddProductQuantity(String productNumber, int quantity,int minimumOrder, int price);
        void funAvailableQuantity(Boolean check);
    }

    public Boolean checkIntenet(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
