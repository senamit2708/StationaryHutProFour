package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class UsersAllOrdersViewModel extends AndroidViewModel {

    private static final String TAG = UsersAllOrdersViewModel.class.getSimpleName();

    private static Query USER_ADDRESS_REF;
    private static Query USER_NEW_ORDERS_REF;
    private static DatabaseReference SELECTED_CART_PRODUCT_REF;
    private MediatorLiveData<List<UserCart>> orderList;
    private MediatorLiveData<List<UserCart>> newOrderList;
    private MediatorLiveData<UserCart> cartProduct;
    private MediatorLiveData<Address> deliveryAddress;
    private FirebaseQueryLiveData mLiveData;
    private FirebaseQueryLiveData mNewOrderLiveData;
    private FirebaseQueryLiveData mCartProductLiveData;

    private String cartProductFirebaseKey;
    private String mUserId;

    public UsersAllOrdersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserCart>>getAllOrders(String userId){
        if (orderList==null){
            orderList = new MediatorLiveData<>();
            loadOrders(userId);
        }
        return orderList;
    }

    private void loadOrders(String userId) {
        USER_ADDRESS_REF = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/order").orderByKey();
        mLiveData = new FirebaseQueryLiveData(USER_ADDRESS_REF);

        orderList.addSource(mLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    List<UserCart> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.i(TAG,"the child is "+child);
                        Log.i(TAG, "the key of children is "+child.getKey());
                        UserCart order = child.child("product").getValue(UserCart.class);
                        Log.i(TAG, "the product is  "+order);
                        orders.add(order);
                    }
                    Collections.reverse(orders);
                    orderList.setValue(orders);
                    Log.i(TAG, "the orderlist is "+orders.size());
                }else {
                    orderList.setValue(null);
                }

            }
        });
    }

    public LiveData<List<UserCart>> getNewOrders(String userId){
        if(newOrderList ==null){
            newOrderList = new MediatorLiveData<>();
            loadNewOrders(userId);
        }
        return newOrderList;
    }

    private void loadNewOrders(String userId) {
        USER_NEW_ORDERS_REF = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/order").orderByChild("orderConfirmation").equalTo(1);
        mNewOrderLiveData = new FirebaseQueryLiveData(USER_NEW_ORDERS_REF);
        newOrderList.addSource(mNewOrderLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    List<UserCart> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.i(TAG, "the key of children is "+child.getKey());
                        Log.i(TAG, "the data snapshot is "+dataSnapshot);
                        UserCart order = child.child("product").getValue(UserCart.class);
                        Log.i(TAG, "the order is "+order);
                        orders.add(order);
                    }

                    newOrderList.setValue(orders);
                    Log.i(TAG, "the orderlist is "+orders.size());
                }else {
                    newOrderList.setValue(null);
                }
            }
        });
    }

    public void setSelectedCartProductForDesc(String cartProductKey) {
        cartProductFirebaseKey = cartProductKey;
    }

    public LiveData<UserCart> getSelectedCartProductForDesc(String userId) {
        Log.i(TAG, "inside getselectedprod for desc");
            cartProduct = new MediatorLiveData<>();
            deliveryAddress = new MediatorLiveData<>();
            Log.i(TAG, "mediatorlive data is createde");
            loadCartProductForDesc(userId);


        return cartProduct;

    }

    private void loadCartProductForDesc(String userId) {
        Log.i(TAG, "insided loadcart product for sale");
        SELECTED_CART_PRODUCT_REF = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/order/"+cartProductFirebaseKey);
        mCartProductLiveData = new FirebaseQueryLiveData(SELECTED_CART_PRODUCT_REF);
        Log.i(TAG, "the value of live data is "+mCartProductLiveData);
        cartProduct.addSource(mCartProductLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    Log.i(TAG, "data snapshot is not null");

//                    for (DataSnapshot child : dataSnapshot.getChildren()){
//                        Log.i(TAG,"the child is "+child);
//                    DataSnapshot child = dataSnapshot.getChildren().iterator().next();
                        UserCart product = dataSnapshot.child("product").getValue(UserCart.class);
                        Address address = dataSnapshot.child("address").getValue(Address.class);
                        Log.i(TAG, "the product is  "+product);
                        cartProduct.setValue(product);
                        deliveryAddress.setValue(address);
                        Log.i(TAG, "the cartProduct value is "+cartProduct);
//                    }
                }else {
                    Log.i(TAG, "inside the else block of cartproduct");
                    cartProduct.setValue(null);
                    deliveryAddress.setValue(null);
                }
            }
        });
    }

    public LiveData<Address> getAddressDelivery() {
        return deliveryAddress;
    }
}
