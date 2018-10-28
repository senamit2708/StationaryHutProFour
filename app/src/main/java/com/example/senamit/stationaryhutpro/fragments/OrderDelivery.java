package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.DeliveryProductAdapter;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDelivery extends Fragment {
    private static final String TAG = OrderDelivery.class.getSimpleName();

    private Context context;
    private String mUserId;
    //    private UserCart userCart;

    private Button btnPayment;
    private TextView txtTotalPrice;
    private TextView txtTotalItemCount;
    private TextView txtItemFinalPrice;
    private TextView txtDeliveryPrice;
    private TextView txtFinalPrice;

    private int deliveryCharge = 50;
    private int minTotalPrice = 500;
    private TextView txtShippingHint;
//    private Button btnAddress;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DeliveryProductAdapter mAdapter;
    List<UserCart> userCartProduct;

    private ProductCartViewModel mViewModel;
    private UserAddressViewModel mViewModelUserAddress;


    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
        mViewModelUserAddress = ViewModelProviders.of(getActivity()).get(UserAddressViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context= container.getContext();
        View view = inflater.inflate(R.layout.activity_order_delivery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        btnPayment =  view.findViewById(R.id.btnPayment);
//        btnAddress = view.findViewById(R.id.btnAddress);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        txtTotalItemCount = view.findViewById(R.id.txtTotalItemCount);
        txtItemFinalPrice = view.findViewById(R.id.txtItemFinalPrice);
        txtDeliveryPrice = view.findViewById(R.id.txtDeliveryPrice);
        txtFinalPrice = view.findViewById(R.id.txtFinalPrice);
        txtShippingHint = view.findViewById(R.id.txtShippingHint);

        mRecyclerView = view.findViewById(R.id.recycler_cart);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new DeliveryProductAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mViewModel.getCartData(mUserId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(@Nullable List<UserCart> userCarts) {
                if (userCarts!= null){
                    userCartProduct = new ArrayList<>();
                    userCartProduct.addAll(userCarts);
                    Log.i(TAG, "the size of cart is "+userCarts.size());
                    mAdapter.setCartProduct(userCarts);
                    int size  = userCartProduct.size();
                    int totalPrice =0;
                    for (int i=0;i<size; i++){
                        int quantity = userCartProduct.get(i).getQuantity();
                        //i think here issue occurs if internet is slow...cross check i have to do here
                        //i m thinking to use try catch block or something else...
                        int productPrice = userCartProduct.get(i).getProductPrice();
                        int price = quantity * productPrice;
                        totalPrice= totalPrice+price;
                    }
                    txtTotalItemCount.setText("Price("+size+" items)");
                    txtItemFinalPrice.setText(String.valueOf(totalPrice));
                    if (totalPrice<minTotalPrice){
                        txtDeliveryPrice.setText(String.valueOf(deliveryCharge));
                        int total = totalPrice+deliveryCharge;
                        txtTotalPrice.setText(String.valueOf(total));
                        txtFinalPrice.setText(String.valueOf(total));
                        txtShippingHint.setText("Total price above "+getString(R.string.Rs)+""+minTotalPrice+" is of free delivery");
                        txtShippingHint.setVisibility(View.VISIBLE);
                    }else {
                        txtTotalPrice.setText(String.valueOf(totalPrice));
                        txtFinalPrice.setText(String.valueOf(totalPrice));
                        txtDeliveryPrice.setText("Free");
                        txtShippingHint.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mViewModelUserAddress.getRecentlyUsedAddress(mUserId).observe(this, new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                if (address!= null){
                    Log.i(TAG, "the adress is  "+address.getDate());
                }

            }
        });


        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.setOrderedProduct(userCartProduct);
                Navigation.findNavController(view).navigate(R.id.action_orderDelivery_to_userAddressView);
            }
        });

//        btnAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_orderDelivery_to_userAddressView);
//            }
//        });
    }

}
