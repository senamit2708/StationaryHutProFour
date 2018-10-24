package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.fragments.FilterDetails;
import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.FilterCategoryModel;
import com.example.senamit.stationaryhutpro.models.FilterDetailModel;
import com.example.senamit.stationaryhutpro.models.Product;
import com.google.android.gms.common.util.ArrayUtils;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ProductCategoryViewModel extends AndroidViewModel {

    private static final String TAG = ProductCategoryViewModel.class.getSimpleName();

    private String productCategoryNameNew;
    private String productCategoryNameOld;
    private String productCategoryName;
    private String productCategoryCheck;

//    private String filterCategorySelection;
    MutableLiveData<String> filterMutableData = new MutableLiveData<>();


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

    private List<FilterDetailModel> indexbooleanStore = new ArrayList<>();

    public ProductCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getCategoryProduct(String productCategory) {
//        productCategoryName = productCategory;
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

    //why we ned this here..I think no need to get from here
    public String getProductCategoryName() {
        return productCategoryName;
    }

    public LiveData<List<FilterCategoryModel>> getFilterCategory(String productCategory) {

        if (filterCategoryLiveData==null){
            loadFilterCategory(productCategory);

        }else {
            loadFilterCategory(productCategory);
        }

        return filterCategoryLiveData;
    }

    private void loadFilterCategory(String productCategory) {
        PRODUCT_CATEGORY = FirebaseDatabase.getInstance().getReference("/Filter/FirstFilter/"+productCategory);
        filterCategoryLiveData = new MediatorLiveData<>();
        filterItemLiveData = new MediatorLiveData<>();
        categoryLiveData = new FirebaseQueryLiveData(PRODUCT_CATEGORY);

        filterCategoryLiveData.addSource(categoryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                     List<FilterCategoryModel> filterCategoryList = new ArrayList<>();
                     List<FilterDetailModel> filterDetailList = new ArrayList<>();
                     int index= 0;
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        FilterCategoryModel category = categorySnapshot.getValue(FilterCategoryModel.class);
                        filterCategoryList.add(category);
                       for (DataSnapshot itemSnapshot : categorySnapshot.child("SecondFilter").getChildren()){
                           FilterDetailModel detail = itemSnapshot.getValue(FilterDetailModel.class);
                           FilterDetailModel completeDetail;

                           if ((productCategoryNameOld.equals(productCategoryNameNew)) && (productCategoryName.equals(productCategoryCheck))){
                               completeDetail = new FilterDetailModel(productCategoryName,detail.getItem(), category.getType(), index,
                                        indexbooleanStore.get(index).getStatus());
                           }else {
                               completeDetail = new FilterDetailModel(productCategoryName,detail.getItem(), category.getType(), index, false);

                           }



                           filterDetailList.add(completeDetail);
                           index++;
//                           Log.i(TAG, "the size of array inside the loop is "+filterDetailList.size());
                       }
                    }
                    Log.i(TAG, "inside mutable live data loading method");
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
//        Log.i(TAG, "the selected category is "+filterCategorySelection);
        filterMutableData.setValue(filterCategorySelection);
    }

    public LiveData<String> getFilterFromFilterCategory() {
        return filterMutableData;
    }

    public LiveData<List<FilterDetailModel>> getCompleteFilterCategory() {
        return filterItemLiveData;
    }

    public void setCompleteFilterCategory(List<FilterDetailModel> filterDetailModels) {
        productCategoryCheck= filterDetailModels.get(0).getCategoryName();
        Log.i(TAG,"THE SIZE OF ARRAY IN COMPLETECATEGORY IS "+filterDetailModels.size());
        filterItemLiveData.setValue(filterDetailModels);
        Log.i(TAG,"THE SIZE OF ARRAY IN COMPLETECATEGORY IS "+filterItemLiveData.getValue().size());


    }

    public void setIndexBoolean(List<FilterDetailModel> indexbooleanStore) {
        this.indexbooleanStore = indexbooleanStore;
    }

    //this method is called from NavDrawerStationaryItem to set the product category
    public void setProductCategoryName(String productCategory) {
        if (productCategoryNameOld==null){
            productCategoryNameOld=productCategory;
            productCategoryNameNew= productCategory;
        }
        if (productCategoryNameOld!= null){
                productCategoryNameOld=productCategoryNameNew;
                productCategoryNameNew= productCategory;

        }
        productCategoryName= productCategory;

    }

//    public void getSortTypeProduct(int sortType) {
//        if (productLiveData!= null){
//
//        }
//    }
}
