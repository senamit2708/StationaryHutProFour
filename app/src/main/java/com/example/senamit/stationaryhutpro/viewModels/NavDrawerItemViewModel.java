package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.ProductCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class NavDrawerItemViewModel extends AndroidViewModel {

    private static final String TAG = NavDrawerItemViewModel.class.getSimpleName();
    private static Query CATEGORY_ITEM;
    private FirebaseQueryLiveData mFirebaseQueryLiveData;

    private MediatorLiveData<List<ProductCategory>> mLiveData;

    public NavDrawerItemViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ProductCategory>> getNavDrawerItemList(String catergoryName){
        if (mLiveData==null){
            Log.i(TAG, "inside the if statement of view model");
            navDrawerItemList(catergoryName);
        }

        return mLiveData;
    }

    private void navDrawerItemList(String catergoryName) {


        CATEGORY_ITEM = FirebaseDatabase.getInstance().getReference("/productCategory/"+catergoryName);
        mFirebaseQueryLiveData = new FirebaseQueryLiveData(CATEGORY_ITEM);
        mLiveData = new MediatorLiveData<>();
       mLiveData.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
           @Override
           public void onChanged(DataSnapshot dataSnapshot) {
               List<ProductCategory> items = new ArrayList<>();
              for (DataSnapshot itemsnapSnapshot : dataSnapshot.getChildren()){
                  Log.i(TAG, "size of snapshot is "+dataSnapshot.exists());
                  ProductCategory item = itemsnapSnapshot.getValue(ProductCategory.class);
//                  Log.i(TAG, "the item is "+item.getProductItem());
                  items.add(item);
                  mLiveData.setValue(items);
              }

           }
       });
    }


}
