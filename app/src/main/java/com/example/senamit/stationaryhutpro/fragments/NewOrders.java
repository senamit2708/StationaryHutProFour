package com.example.senamit.stationaryhutpro.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.ProductOrderedAdapter;
import com.example.senamit.stationaryhutpro.interfaces.CheckInterneConnInterface;
import com.example.senamit.stationaryhutpro.interfaces.OrderedProductDescInterface;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.OrderedProductViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UsersAllOrdersViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewOrders extends Fragment implements OrderedProductDescInterface, CheckInterneConnInterface {

    private static final String TAG = NewOrders.class.getSimpleName();
    private final int NEW_ORDER_CHECK=32;
    private int NEW_ORDER_STATUS;

    private Context context;
    private UserAddressViewModel mAddressViewModel;
    private ProductCartViewModel mProductCardViewModel;
    private OrderedProductViewModel mOrderedProductViewModel;
    private UsersAllOrdersViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductOrderedAdapter mAdapter;

    private Address address;
    private List<UserCart> orderedProduct;
    private List<String> keyList;
    String orderNumberPartOne;

    private DatabaseReference mDatabase;
    String userId;

    private int totalProductQuanityt;

    ConstraintLayout mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NEW_ORDER_STATUS = getArguments().getInt("newOrderCheck");
       mAddressViewModel = ViewModelProviders.of(getActivity()).get(UserAddressViewModel.class);
       mProductCardViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
       mOrderedProductViewModel = ViewModelProviders.of(getActivity()).get(OrderedProductViewModel.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(UsersAllOrdersViewModel.class);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_order_details, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
         userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //for snackbar getting the main view
        mainView = (ConstraintLayout) view.findViewById(R.id.constraint_main_layout);


       address= mAddressViewModel.getAddress().getValue();
       orderedProduct = mProductCardViewModel.getOrderedProduct().getValue();
         orderNumberPartOne = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault()).format(new Date());

         Log.i(TAG, "NEW_ORDER_STATUS: "+NEW_ORDER_STATUS);
         Log.i(TAG,"NEW_ORDER_CHECK: "+NEW_ORDER_CHECK);
         if (NEW_ORDER_STATUS==NEW_ORDER_CHECK){
             Log.i(TAG, "inside the NEW_ORDER_STATUS==NEW_ORDER_CHECK");
             writeNewPost(orderedProduct);
             NEW_ORDER_STATUS++;
         }


        mRecyclerView = view.findViewById(R.id.recycler_order);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new ProductOrderedAdapter(context,this, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }



    private void writeNewPost(List<UserCart> orderedProduct) {
        final int count = orderedProduct.size();
         keyList = new ArrayList<>();
         final List<String> productNumberList = new ArrayList<>();


        Map<String, Object> addressValue = address.toMap();
        for (int i=0; i<count; i++){
            final int total = i+1;
            final UserCart userCart = orderedProduct.get(i);
            userCart.setOrderStatus("CONFIRMED");
            final String productNumber = userCart.getProductNumber();
            String orderNumber = orderNumberPartOne + productNumber.substring(1,4)+total;
            userCart.setOrderNumber(orderNumber);
            userCart.setPaymentMode("Cash on delivery");
            final String keyOrder = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order").push().getKey();
            userCart.setCartProductKey(keyOrder);

            userCart.setDate( new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));


            productNumberList.add(keyOrder);
            Map<String, Object> productValues = userCart.toMapFinalOrderEntry();
            Map<String, Object> childUpdate = new HashMap<>();
            final Map<String, Object> childUpdateAddress = new HashMap<>();
            FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order/"+keyOrder).child("orderConfirmation").setValue(1);
            childUpdate.put("/users/"+userId+"/order/"+keyOrder+"/product", productValues);
            childUpdateAddress.put("/users/"+userId+"/order/"+keyOrder+"/address", addressValue);
//            Log.i(TAG, "inside the writeNewPost for loop "+i);

            mDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    mDatabase.updateChildren(childUpdateAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
//                            Log.i(TAG, "inside the onComplete method of updatechildren "+keyOrder);

                            // update product quantity after user has done the shopping

                           mDatabase.child("products").child(productNumber).child("productQuantity")
                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                              totalProductQuanityt= dataSnapshot.getValue(Integer.class);
                                              int quantity = userCart.getQuantity();
                                              int remainingQuantity = totalProductQuanityt-quantity;
                                          mDatabase.child("products").child(productNumber).child("productQuantity")
                                                  .setValue(remainingQuantity);
//                                          Log.i(TAG, "the remaining quantity is "+remainingQuantity);
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                      }
                                  });
                            keyList.add(keyOrder);
//                            Log.i(TAG, "the size of keyList is "+keyList.size());
//                            Log.i(TAG, "count is "+count);
//                            Log.i(TAG, "total is "+total);
                            if (total==count){
//                                Log.i(TAG, "the size of final keyList is "+keyList.size());
                                productDetails(productNumberList);
                            }




                        }
                    });

                }
            });


        }
    }

    private void productDetails(final List<String> searchProductNumber) {
        mDatabase.child("users").child(userId).child("cart").removeValue();
        mViewModel.getNewOrders(userId).observe(this, new Observer<List<UserCart>>() {
            List<UserCart> cartList;
            @Override
            public void onChanged(List<UserCart> userCarts) {
             if (userCarts!= null){
                 cartList = new ArrayList<>();
                 for (String productNumber : searchProductNumber){
                     for (UserCart cartProduct : userCarts  ){
                         if (cartProduct.getCartProductKey().equals(productNumber)){
                             cartList.add(cartProduct);
                         }
                     }
                 }
                 mAdapter.setOrderedProduct(cartList);
             }
            }
        });
    }

    @Override
    public void funOrderdProductSelection(String cartProductKey) {
        mViewModel.setSelectedCartProductForDesc(cartProductKey);
    }

    @Override
    public void funLoadSnackBar(Boolean connCheck) {
        if (connCheck==false){
            showSnackbar(mainView, R.string.internetIssue1, Snackbar.LENGTH_SHORT);
        }
    }

    //snackbar
    public void showSnackbar(final View view, int message, int duration)
    {
        Snackbar snackbar = Snackbar.make(view, message, duration).setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar1 =Snackbar.make(view, R.string.internetIssue2, Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
//        View snackView = snackbar.getView();
        snackbar.show();

    }


//
}
