package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.CountDrawable;
import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductForSaleViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class ProductDescription extends Fragment implements View.OnClickListener {

    private static final String TAG = ProductDescription.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
//    private static final String PRODUCT_INDEX = "product_index";

    private Context context;
    private String mProductNumber;
    private String mProductName;
    private double mProductPrice;
    private String mImageUrl;
//    private int clickedItemIndex;
    private Product product;
    private String userId;
    private String date;
    private int mMinimumOrder;

    private TextView mTxtProductName;
    private TextView mTxtProductPrice;
    private TextView mTxtProductNumber;
    private ImageView mProductImage;
    private TextView mTxtStock;
    private Button mBtnAddToCart;
    private Button mBtnBuyNow;

    private ProductForSaleViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;
    private LiveData<DataSnapshot> liveData;
    private FirebaseUser mFirebaseUser;

    private boolean showToast = false;
    private int stock;
    //for snackbar getting the view
    ConstraintLayout mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductNumber = getArguments().getString(PRODUCT_KEY);
//        clickedItemIndex = getArguments().getInt(PRODUCT_INDEX);
        Log.i(TAG, "inside oncreate product description "+mProductNumber);
        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_product_description, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtProductName = view.findViewById(R.id.txtProductName);
        mTxtProductPrice = view.findViewById(R.id.txtProductPrice);
        mTxtProductNumber = view.findViewById(R.id.txtProductNumber);
        mTxtStock = view.findViewById(R.id.txtStock);
        mProductImage = view.findViewById(R.id.imageProduct);
        mBtnAddToCart = view.findViewById(R.id.btnAddToCart);
        mBtnBuyNow = view.findViewById(R.id.btnBuyNow);
        //for snackbar getting the main view
        mainView = (ConstraintLayout) view.findViewById(R.id.constraint_main_layout);

        mBtnBuyNow.setOnClickListener(this);
        mBtnAddToCart.setOnClickListener(this);

         date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);

        liveData= mViewModel.getProductMutableLiveData(mProductNumber);

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    Log.i(TAG, "inside onChanged method of livedata observer of product desc");
                    product= dataSnapshot.getValue(Product.class);
                    Log.i(TAG, "the product is "+product);
                    mImageUrl = product.getImageUrl();
                    Picasso.with(context).load(product.getImageUrl()).into(mProductImage);
                    mProductName = product.getProductName();
                    mProductPrice = product.getProductPrice();
                    mMinimumOrder = product.getMinimumOrder();
                    stock = product.getProductQuantity();
                    mTxtProductName.setText(product.getProductName());
                    mTxtProductPrice.setText(Double.toString(product.getProductPrice()));
                    mTxtProductNumber.setText(product.getProductNumber());
                    //showing instock and out of stock option
                    if (stock>0){
                        mTxtStock.setText(R.string.inStock);
                        mTxtStock.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                        mBtnAddToCart.setEnabled(true);
                        mBtnBuyNow.setEnabled(true);
                    }else {
                        mTxtStock.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                        mTxtStock.setTextColor(getResources().getColor(R.color.red));
                        mTxtStock.setText(R.string.outOfStock);
                        mBtnAddToCart.setEnabled(false);
                        mBtnBuyNow.setEnabled(false);
                    }



                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddToCart:
                if (((StationaryMainPage)getActivity()).checkInternetConnection()){
                    showToast = true;
                    pushProductToCart();
                    Log.i(TAG, "internet is connected");
                }else {
                    showSnackbar(mainView, "No Internet Connection",Snackbar.LENGTH_SHORT);
                }

                break;
            case R.id.btnBuyNow:
                if (((StationaryMainPage)getActivity()).checkInternetConnection()) {
                    pushProductToCart();
                    Navigation.findNavController(view).navigate(R.id.action_productDescription_to_cartProduct);
                }else {
                    showSnackbar(mainView, "No Internet Connection",Snackbar.LENGTH_SHORT);
                }

            default:
                Log.i(TAG, "Select any other click option");
        }
    }
    private void pushProductToCart() {
        Log.i(TAG, "inside pushproducttocart method ");
        Query mdataRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("cart").child(mProductNumber);

        mdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "inside ondatachange method "+dataSnapshot.getKey());
                if (dataSnapshot.exists()){
                    Log.i(TAG, "inside the if true statement "+mProductNumber);
                    if (showToast==true){
                        Toast.makeText(context, "Product is already in the cart",Toast.LENGTH_SHORT).show();
                        showToast=false;
                    }
                }
                else {
                    Log.i(TAG, "product is not avaiable, trying to load the product");
                    UserCart cart = new UserCart(mProductNumber, date, mProductPrice, mProductName, mImageUrl, mMinimumOrder);
                    cart.setQuantity(mMinimumOrder);
                    cart.setMinimumOrder(mMinimumOrder);
                    cart.setAvailableQuantity(50);
                    Map<String, Object> cartValue = cart.toMap();
                    Map<String, Object> childUpdate = new HashMap<>();
                    Log.i(TAG, "username is "+mFirebaseUser.getUid());
                    mUserDatabase = mDatabase.child("users").child(userId).child("cart");
                    childUpdate.put("/"+mProductNumber+"/", cartValue);
                    mUserDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (showToast==true){
                                Toast.makeText(context, "Product added to the cart", Toast.LENGTH_SHORT).show();
                                showToast=false;
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i(TAG, "inside oncreate option menu in cartproduct");
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.cartProduct);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        final CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }
        mCartViewModel.getCartProductCount(userId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(List<UserCart> userCarts) {
                if (userCarts!=null){
                    int size= userCarts.size();
                    badge.setCount(Integer.toString(size));
                }
            }
        });

        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    //snackbar
    public void showSnackbar(final View view, String message, int duration)
    {
        Snackbar snackbar = Snackbar.make(view, message, duration).setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar1 =Snackbar.make(view, "Sorry, No Connection", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
//        View snackView = snackbar.getView();
        snackbar.show();

    }


}
