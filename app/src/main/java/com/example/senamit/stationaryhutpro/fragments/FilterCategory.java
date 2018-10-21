package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.FilterCategoryAdapter;
import com.example.senamit.stationaryhutpro.interfaces.FilterCategoryInterface;
import com.example.senamit.stationaryhutpro.models.FilterCategoryModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCategoryViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterCategory extends Fragment implements FilterCategoryInterface {

    private static final String TAG = FilterCategory.class.getSimpleName();
    private Context context;
    private String productCategory;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FilterCategoryAdapter mAdapter;
    private ProductCategoryViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //keep it in mind to get the context, might be i m wrong here
        context= getActivity();
        Log.i(TAG, "the context is "+context.toString());
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCategoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_filter_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.reycler_category);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new FilterCategoryAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        productCategory = mViewModel.getProductCategoryName();
        mViewModel.getFilterCategory(productCategory).observe(this, new Observer<List<FilterCategoryModel>>() {
            @Override
            public void onChanged(List<FilterCategoryModel> filterCategoryModels) {
                if (filterCategoryModels!= null){
                    Log.i(TAG, "the size of list is "+filterCategoryModels.size());
                    mAdapter.setCategory(filterCategoryModels);
                }else {
                    Log.i(TAG, "the size of list is null");
                }
            }
        });
    }

    @Override
    public void funFilterCategoty(String filterCategorySelection) {
        mViewModel.setFilterCategorySelected(filterCategorySelection);
    }
}
