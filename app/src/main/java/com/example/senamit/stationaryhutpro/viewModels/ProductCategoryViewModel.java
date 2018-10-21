package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.fragments.FilterDetails;
import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.FilterCategoryModel;
import com.example.senamit.stationaryhutpro.models.FilterDetailModel;
import com.example.senamit.stationaryhutpro.models.Product;
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

public class ProductCategoryViewModel extends AndroidViewModel {

    private static final String TAG = ProductCategoryViewModel.class.getSimpleName();

    private String productCategoryName;
    private String filterCategorySelection;

    private static Query PRODUCT_FOR_SALE ;
    private static Query PRODUCT_CATEGORY;
    private static  DatabaseReference PRODUCT_FOR_SEARCH ;
    private static DatabaseReference PRODUCT_CATEGORY_FOR_SEARCH;
    private FirebaseQueryLiveData liveData ;
    private FirebaseQueryLiveData categoryLiveData;

//    private FirebaseQueryLiveData productDescriptionLiveData;
    private MediatorLiveData<List<Product>> productLiveData;
    private MediatorLiveData<List<FilterCategoryModel>> filterCategoryLiveData;
    private MediatorLiveData<List<FilterDetailModel>> filterItemLiveData;

    public ProductCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getCategoryProduct(String productCategory) {
        productCategoryName = productCategory;
       loadProductLiveData(productCategory);
        return productLiveData;
    }

    private void loadProductLiveData(String productCategory) {
        PRODUCT_FOR_SALE = FirebaseDatabase
                .getInstance().getReference("/products").orderByChild("category").equalTo(productCategory);
        Log.i(TAG, "PRODUCT_FOR_SALE "+PRODUCT_FOR_SALE);
        productLiveData = new MediatorLiveData<>();
        liveData = new FirebaseQueryLiveData(PRODUCT_FOR_SALE);
        productLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    List<Product> productList = new ArrayList<>();
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()){
                        Product product = productDataSnapshot.getValue(Product.class);
                        productList.add(product);
                        Log.i(TAG, "inside loadproduct live data"+product);

                    }
                    Collections.reverse(productList);
                    productLiveData.setValue(productList);
                }
                else {
                    productLiveData.setValue(null);
                }
            }
        });
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public LiveData<List<FilterCategoryModel>> getFilterCategory(String productCategory) {
        loadFilterCategory(productCategory);
        return filterCategoryLiveData;
    }

    private void loadFilterCategory(String productCategory) {
        PRODUCT_CATEGORY = FirebaseDatabase.getInstance().getReference("/Filter/FirstFilter/"+productCategory);
        Log.i(TAG, "the reference of product category is "+PRODUCT_CATEGORY);
        filterCategoryLiveData = new MediatorLiveData<>();
        filterItemLiveData = new MediatorLiveData<>();
        categoryLiveData = new FirebaseQueryLiveData(PRODUCT_CATEGORY);
        Log.i(TAG, "just before the add source method");
        filterCategoryLiveData.addSource(categoryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Log.i(TAG, "inside onchange method of load filter category method");
                if (dataSnapshot!= null){
                    List<FilterCategoryModel> filterCategoryList = new ArrayList<>();
                    List<FilterDetailModel> filterDetailList = new ArrayList<>();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        FilterCategoryModel category = categorySnapshot.getValue(FilterCategoryModel.class);
                        Log.i(TAG, "the category of the filter is "+category.getType());
                        filterCategoryList.add(category);
                       for (DataSnapshot itemSnapshot : categorySnapshot.child("SecondFilter").getChildren()){
                           FilterDetailModel detail = itemSnapshot.getValue(FilterDetailModel.class);
                           FilterDetailModel completeDetail = new FilterDetailModel(detail.getItem(), category.getType());
                           Log.i(TAG, "the item of the category is "+detail.getItem());
                           filterDetailList.add(completeDetail);
                       }
                    }
                    filterCategoryLiveData.setValue(filterCategoryList);
                    filterItemLiveData.setValue(filterDetailList);
                }
                else {
                    Log.i(TAG, "the datasnapshot is null");
                }
            }
        });

    }

    public void setFilterCategorySelected(String filterCategorySelection) {
        Log.i(TAG, "the selected category is "+filterCategorySelection);
        this.filterCategorySelection = filterCategorySelection;
    }

//    public void getSortTypeProduct(int sortType) {
//        if (productLiveData!= null){
//
//        }
//    }
}
