package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;


public class ProductForSaleViewModel extends AndroidViewModel {

    private static final String TAG = ProductForSaleViewModel.class.getSimpleName();

    private static String mProductNumber;


    private static final DatabaseReference PRODUCT_FOR_SALE = FirebaseDatabase
            .getInstance().getReference("/products");
    private static  DatabaseReference PRODUCT_FOR_SEARCH ;


    private FirebaseQueryLiveData liveData ;
    private FirebaseQueryLiveData productDescriptionLiveData;
    private FirebaseQueryLiveData cartProductLiveData;

    private MediatorLiveData<List<Product>> productLiveData;
    private MediatorLiveData<List<UserCart>> cartLiveData;

    public ProductForSaleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getDataSnapshotLiveData(){

        if (productLiveData==null){
            Log.i(TAG, "product live data is null");
            productLiveData = new MediatorLiveData<>();
            liveData = new FirebaseQueryLiveData(PRODUCT_FOR_SALE);

            loadProductLiveData();
        }
        Log.w(TAG, "the value for activity is "+productLiveData.toString());
        return productLiveData;
    }

    private void loadProductLiveData() {

        productLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<Product> productList = new ArrayList<>();
                            for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()){
                                Product product = productDataSnapshot.getValue(Product.class);
                                productList.add(product);
                                Log.i(TAG, "inside loadproduct live data"+product);

                            }
                            Collections.reverse(productList);
                            productLiveData.postValue(productList);
                        }
                    }).start();
                }else {
                    productLiveData.setValue(null);
                }
            }
        });
    }


    public LiveData<DataSnapshot> getProductMutableLiveData(String productNumber) {
        if (productDescriptionLiveData==null){
            mProductNumber=productNumber;
            PRODUCT_FOR_SEARCH = FirebaseDatabase
                    .getInstance().getReference("/products/"+ mProductNumber);
            productDescriptionLiveData=new FirebaseQueryLiveData(PRODUCT_FOR_SEARCH);
        }

        return productDescriptionLiveData;
    }

}
