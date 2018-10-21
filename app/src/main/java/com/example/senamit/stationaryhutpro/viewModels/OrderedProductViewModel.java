package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class OrderedProductViewModel extends AndroidViewModel {

    private static final String TAG = OrderedProductViewModel.class.getSimpleName();

    private static Query ORDERED_PRODUCT;
    private DatabaseReference mDatabaseRef;
    private FirebaseQueryLiveData mFirebaseQueryLiveData;

    private MediatorLiveData<List<UserCart>> orderedProduct;

    private MutableLiveData<List<UserCart>> orderedProductLiveData;



    public OrderedProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserCart>> getOrderedProduct(String userId){
//        if (orderedProduct==null){
            orderedProduct = new MediatorLiveData<>();
        orderedProductLiveData = new MutableLiveData<>();
            loadOrderedProductFirebase(userId);
//        }
        return orderedProduct;
    }

    private void loadOrderedProductFirebase(String userId) {
        {
            final List<UserCart> listCart = new ArrayList<>();

            ORDERED_PRODUCT = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order").orderByKey();
            Log.i(TAG, "the ordered product is "+ORDERED_PRODUCT);
            mFirebaseQueryLiveData = new FirebaseQueryLiveData(ORDERED_PRODUCT);
            Log.i(TAG, "the firebase query live data is  "+mFirebaseQueryLiveData.toString());
            orderedProduct.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
                @Override
                public void onChanged(DataSnapshot dataSnapshot) {
                    for (DataSnapshot addressDataSnapshot : dataSnapshot.getChildren()){
//                        addressDataSnapshot.child("product");
                        UserCart cart = addressDataSnapshot.child("product").getValue(UserCart.class);
                        Log.i(TAG, "the product number is "+cart.getProductNumber());
                        listCart.add(cart);
                        orderedProduct.setValue(listCart);
                    }

                }
            });
        }
    }

//    private void loadOrderedProductFirebase(final List<String> keyList, String userId) {
//        Log.i(TAG, "the sizze of retrived product will be"+keyList.size());
//        if (keyList.size()>0){
//
//            final List<UserCart> orderedProductList = new ArrayList<>();
//
//
//            for (int i=0; i<keyList.size(); i++){
////
//
//                String productKey = keyList.get(i);
//                Log.i(TAG, "the product key is "+productKey);
//                ORDERED_PRODUCT = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order/"+productKey+"/product");
//                mFirebaseQueryLiveData = new FirebaseQueryLiveData(ORDERED_PRODUCT);
//
//                orderedProduct.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
//                    @Override
//                    public void onChanged(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot!= null){
//                            UserCart order = dataSnapshot.getValue(UserCart.class);
//                            orderedProductList.add(order);
//
//                            Log.i(TAG, "the number of keys are "+keyList.size());
//                            Log.i(TAG, "the ordered product is "+order.getProductNumber());
////                            List<UserCart> uniqueOrderedProductList = orderedProductList.stream().distinct().collect(Collectors.toList());
//                            orderedProduct.setValue(orderedProductList);
//
//
//                        }
////                        Log.i(TAG, "the size of ordered produtct in viewmodel is "+orderedProductList.size());
//
////                        orderedProductList.clear();
//
//                    }
//                });
//
//
//            }
//
//        }
//    }

}
