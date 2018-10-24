package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.NavDrawerItemAdapter;
import com.example.senamit.stationaryhutpro.interfaces.ProductCategoryInterface;
import com.example.senamit.stationaryhutpro.models.ProductCategory;
import com.example.senamit.stationaryhutpro.viewModels.NavDrawerItemViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCategoryViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NavDrawerStationaryItem extends Fragment implements ProductCategoryInterface{

    private static final String TAG = NavDrawerStationaryItem.class.getSimpleName();

    private Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NavDrawerItemAdapter mAdapter;
    private NavDrawerItemViewModel mViewModel;
    private ProductCategoryViewModel mCategoryViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(NavDrawerItemViewModel.class);
        mCategoryViewModel = ViewModelProviders.of(getActivity()).get(ProductCategoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_navdrawer_stationaryitem, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new NavDrawerItemAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel.getNavDrawerItemList("stationaryItem").observe(this, new Observer<List<ProductCategory>>() {
            @Override
            public void onChanged(List<ProductCategory> productCategories) {
                Log.i(TAG, "size in page is "+productCategories.size());
                mAdapter.setProductItem(productCategories);
            }
        });

    }

    @Override
    public void funSetProductCategory(String productCategory) {
//        mCategoryViewModel.setProductCategoryName(productCategory);
    }
}
