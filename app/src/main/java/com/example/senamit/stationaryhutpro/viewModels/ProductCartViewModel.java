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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ProductCartViewModel extends AndroidViewModel {

    private static final String TAG = ProductCartViewModel.class.getSimpleName();

    private DatabaseReference mdataRef;
    private static DatabaseReference PRODUCT_IN_CART;
    private static DatabaseReference PRODUCT_DETAILS;
    private static DatabaseReference PRODUCT_REMOVE;
    private static DatabaseReference PRODUCT_COUNT;
    private FirebaseQueryLiveData cartFirebaseQueryLiveData ;
    private FirebaseQueryLiveData productFirebaseQueryLiveData;
    private FirebaseQueryLiveData cartCountQueryLiveData;

    private MediatorLiveData<List<UserCart>> cartLiveData;
    private MediatorLiveData<List<Product>>  productLiveData;
    private MediatorLiveData<List<UserCart>> cartCountLiveData;

    private MutableLiveData<List<UserCart>> orderedProductList = new MutableLiveData<>();

    private String mUserId;
    private Boolean productExist = false;




    public ProductCartViewModel(@NonNull Application application) {
        super(application);


    }

    public LiveData<List<UserCart>> getCartData(String userId){
        mUserId = userId;
        if (cartLiveData==null){
            Log.i(TAG, "inside if statement of livedata");
            loadCartLiveData(userId);
        }
        return cartLiveData;
    }

    private void loadProductPrice(List<UserCart> listProduct) {
        loadProductLiveDataTest(listProduct);
    }

    private void loadProductLiveDataTest(final List<UserCart> listProduct) {

        Log.i(TAG, "the size of cart is "+listProduct.size());

        if (listProduct.size()>0) {
            int size = listProduct.size();

            for (int i = 0; i < size; i++) {
                final int id= i;
                String productNumber = listProduct.get(i).getProductNumber();
                PRODUCT_DETAILS = FirebaseDatabase.getInstance().getReference("/products/" + productNumber);
                productFirebaseQueryLiveData = new FirebaseQueryLiveData(PRODUCT_DETAILS);

                cartLiveData.addSource(productFirebaseQueryLiveData, new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {

                            Product product = dataSnapshot.getValue(Product.class);
                            listProduct.get(id).setProductPrice(product.getProductPrice());
                            //for product available quantity:
                            listProduct.get(id).setAvailableQuantity(product.getProductQuantity());

                        }
                        cartLiveData.setValue(listProduct);
                    }
                });
            }
        }else {
            cartLiveData.postValue(null);
        }
    }

    private void loadCartLiveData(String userId) {

        PRODUCT_IN_CART= FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/cart");

        cartFirebaseQueryLiveData = new FirebaseQueryLiveData(PRODUCT_IN_CART);
        cartLiveData = new MediatorLiveData<>();
        cartLiveData.addSource(cartFirebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                List<UserCart> listProduct = new ArrayList<>();
                if (dataSnapshot!= null){
                    for (DataSnapshot cartProductDataSnapshot : dataSnapshot.getChildren()){
                        final UserCart cartProduct = cartProductDataSnapshot.getValue(UserCart.class);
                        listProduct.add(cartProduct);
                    }
                    loadProductPrice(listProduct);
                    cartLiveData.postValue(listProduct);
                }
                else {
                    cartLiveData.setValue(null);
                }
            }
        });

    }

    public void removeProductFromCart(String productNumber) {

        mdataRef = FirebaseDatabase.getInstance().getReference();
        mdataRef.child("users").child(mUserId).child("cart").child(productNumber).removeValue();

    }

    public void setOrderedProduct(List<UserCart> userCartProduct) {
        orderedProductList.setValue(userCartProduct);
    }
    public LiveData<List<UserCart>> getOrderedProduct(){
       return orderedProductList;
    }

    public void addProductQuantityToCart(String productNumber, int quantity, int price) {
        Log.i(TAG, "in view model the addquantity "+productNumber +"  quantity "+quantity);
        int totalPrice = quantity* price;
        mdataRef = FirebaseDatabase.getInstance().getReference();
        mdataRef.child("users").child(mUserId).child("cart").child(productNumber).child("quantity").setValue(quantity);
//        mdataRef.child("users").child(mUserId).child("cart").child(productNumber).child("totalPice").setValue(totalPrice);

    }

    public LiveData<List<UserCart>> getCartProductCount(String userId){
        if (cartCountLiveData==null){
            cartCount(userId);
        }
        return cartCountLiveData;
    }

    private void cartCount(String userId) {
        PRODUCT_COUNT = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/cart");

        cartCountQueryLiveData = new FirebaseQueryLiveData(PRODUCT_COUNT);
        cartCountLiveData = new MediatorLiveData<>();
        cartCountLiveData.addSource(cartCountQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                List<UserCart> listProduct = new ArrayList<>();
                if (dataSnapshot!= null){
                    for (DataSnapshot cartProductDataSnapshot : dataSnapshot.getChildren()){
                        final UserCart cartProduct = cartProductDataSnapshot.getValue(UserCart.class);
                        listProduct.add(cartProduct);
                    }
                    cartCountLiveData.postValue(listProduct);
                }
                else {
                    cartCountLiveData.setValue(null);
                }
            }
        });
    }

}
