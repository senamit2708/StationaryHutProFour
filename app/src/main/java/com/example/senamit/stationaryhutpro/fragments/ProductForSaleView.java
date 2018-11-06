package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.CountDrawable;
import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.adapters.ProductForSaleAdapter;
import com.example.senamit.stationaryhutpro.interfaces.CheckInterneConnInterface;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductForSaleViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductForSaleView extends Fragment implements CheckInterneConnInterface {

    private static final String TAG = ProductForSaleView.class.getSimpleName();

    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_SEND = "product_Send";
    String key = null;

    private Context context;
    private String mUserId;



    private DatabaseReference mDatabase;

    TextView txtProductName;
    TextView txtProductNumber;
    Button mBtnTest;

    private ConstraintLayout mConstraint;
    private ConstraintLayout mInternetConstraint;
    private ConstraintLayout mainView;

    private Button btnCheckInternet;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductForSaleAdapter mAdapter;
    private ProductForSaleViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_product_for_sale_view, container, false);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mRecyclerView = view.findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(context, 2);
        mAdapter = new ProductForSaleAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //for snackbar getting the main view
        mainView = (ConstraintLayout) view.findViewById(R.id.constraint_main_layout);
        mConstraint = view.findViewById(R.id.view_coordinate);
        mInternetConstraint = view.findViewById(R.id.internet_constraint);
        mInternetConstraint.setVisibility(View.GONE);

        checkInternet();

        btnCheckInternet = view.findViewById(R.id.btnCheckInternet);


        mViewModel.getDataSnapshotLiveData().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products!= null){
//                    Collections.reverse(products);
                    Log.i(TAG, "inside the onchanged method, the size of product "+products.size());
                    mAdapter.setProduct(products);
                }
            }
        });


        btnCheckInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((StationaryMainPage)getActivity()).checkInternetConnection()){
                    mInternetConstraint.setVisibility(View.GONE);
                    mConstraint.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void checkInternet() {
        if (!((StationaryMainPage)getActivity()).checkInternetConnection()){
            mConstraint.setVisibility(View.GONE);
            mInternetConstraint.setVisibility(View.VISIBLE);

        }
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
                mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCartViewModel.getCartProductCount(mUserId).observe(this, new Observer<List<UserCart>>() {
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


    @Override
    public void funLoadSnackBar(Boolean connCheck) {
        if (connCheck==false){
            mConstraint.setVisibility(View.GONE);
            mInternetConstraint.setVisibility(View.VISIBLE);
//            showSnackbar(mainView, "No Internet Connection",Snackbar.LENGTH_SHORT);

        }
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
