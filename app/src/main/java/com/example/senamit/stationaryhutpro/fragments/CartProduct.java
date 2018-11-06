package com.example.senamit.stationaryhutpro.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.adapters.CartProductAdapter;
import com.example.senamit.stationaryhutpro.interfaces.CheckInterneConnInterface;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartProduct extends Fragment implements CartProductAdapter.ButtonClickInterface, CheckInterneConnInterface {

    private static final String TAG = CartProduct.class.getSimpleName();

    private Context context;
    private String mUserId;
    private Typeface customFont;
    //    private UserCart userCart;

    private Button btnPayment;
    private Button btnStartBuying;
    private TextView txtTotalPrice;
    private TextView txtTotalItemCount;
    private TextView txtItemFinalPrice;
    private TextView txtDeliveryPrice;
    private TextView txtFinalPrice;

    private int deliveryCharge = 50;
    private int minTotalPrice = 500;
    private TextView txtShippingHint;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartProductAdapter mAdapter;
    List<UserCart> userCartProduct;
    private ConstraintLayout mConstraint;
    private ConstraintLayout mEmptyConstraint;

    private ProductCartViewModel mViewModel;

    private FirebaseUser mFirebaseUser;
    ConstraintLayout mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context= container.getContext();
        View view = inflater.inflate(R.layout.activity_cart_product, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        btnPayment =  view.findViewById(R.id.btnPayment);
        btnStartBuying = view.findViewById(R.id.btnStartBuying);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        txtTotalItemCount = view.findViewById(R.id.txtTotalItemCount);
        txtItemFinalPrice = view.findViewById(R.id.txtItemFinalPrice);
        txtDeliveryPrice = view.findViewById(R.id.txtDeliveryPrice);
        txtFinalPrice = view.findViewById(R.id.txtFinalPrice);
        txtShippingHint = view.findViewById(R.id.txtShippingHint);

        mConstraint = view.findViewById(R.id.view_coordinate);
        mEmptyConstraint = view.findViewById(R.id.emptyView);
        mEmptyConstraint.setVisibility(View.GONE);

        mRecyclerView = view.findViewById(R.id.recycler_cart);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new CartProductAdapter(context, this, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //for snackbar getting the main view
        mainView = (ConstraintLayout) view.findViewById(R.id.constraint_main_layout);

        customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Bold.ttf");




        mViewModel.getCartData(mUserId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(@Nullable List<UserCart> userCarts) {
                if (userCarts!= null){
                    if (userCarts.size()>0){
                        mEmptyConstraint.setVisibility(View.GONE);
                        mConstraint.setVisibility(View.VISIBLE);

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
//                            txtShippingHint.setVisibility(View.VISIBLE);
                        }else {
                            txtTotalPrice.setText(String.valueOf(totalPrice));
                            txtFinalPrice.setText(String.valueOf(totalPrice));
                            txtDeliveryPrice.setText("Free");
                            txtShippingHint.setText("Yay! Free Deliver on this order");

//                            txtShippingHint.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        Log.i(TAG,"inside else statement of user cart");
                        mConstraint.setVisibility(View.GONE);
                        mEmptyConstraint.setVisibility(View.VISIBLE);

                    }

                }
                else{


                }
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((StationaryMainPage)getActivity()).checkInternetConnection()){
                    mViewModel.setOrderedProduct(userCartProduct);
                    Navigation.findNavController(view).navigate(R.id.action_cartProduct_to_orderDelivery);
                }else {
                    showSnackbar(mainView, R.string.internetIssue1, Snackbar.LENGTH_SHORT);
                }

            }
        });

        btnStartBuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_cartProduct_to_productForSaleView);
            }
        });
    }

    @Override
    public void funRemoveBtnClick(String productNumber, int position) {
        Log.i(TAG, "the product number fo product is  "+productNumber +"  position is "+ position);
        mViewModel.removeProductFromCart(productNumber);
    }

    @Override
    public void funAddProductQuantity(String productNumber, int quantity,int minimumOrder, int price) {
        Log.i(TAG, "inside funaddproductqunaity "+ productNumber +"quantity"+quantity);
        if (quantity>minimumOrder){
            mViewModel.addProductQuantityToCart(productNumber, quantity, price);
        }else {
            Toast.makeText(context, "Sorry, Minimum quantity is"+minimumOrder, Toast.LENGTH_SHORT).show();
        }

        showSoftwareKeyboard(false);

    }

    @Override
    public void funAvailableQuantity(Boolean check) {
        Toast.makeText(context, "Sorry, available product is less than your requirement", Toast.LENGTH_SHORT).show();
        showSoftwareKeyboard(false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        showSoftwareKeyboard(false);
    }
    protected void showSoftwareKeyboard(boolean showKeyboard){
        final Activity activity = getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void funLoadSnackBar(Boolean connCheck) {
        if (connCheck==false){
            showSnackbar(mainView, R.string.internetIssue1, Snackbar.LENGTH_SHORT);
            showSoftwareKeyboard(false);
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
}

