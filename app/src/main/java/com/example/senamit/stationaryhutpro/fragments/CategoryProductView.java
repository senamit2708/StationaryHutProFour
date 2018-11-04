package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.CategoryProductAdapter;
import com.example.senamit.stationaryhutpro.models.FilterDetailModel;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProductView extends Fragment implements View.OnClickListener{

    private static final String TAG = CategoryProductView.class.getSimpleName();
    private static final String CATEGORY_KEY = "productCategory";
    private int FILTER_CHECK= 44;


    private Context context;
    private String mUserId;
    private String productCategory;
    private int filterCheck;
    private List<Product> filterProduct;
    private List<FilterDetailModel> mNewFilterSearch;

    private Button btnSort;
    private Button btnFilter;
    private Button btnClose;
    private FrameLayout mFrameLayout;
    private RadioGroup btnSortGroup;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryProductAdapter mAdapter;
    private ProductCategoryViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "inside oncreate method");
        productCategory = getArguments().getString(CATEGORY_KEY);
        Log.i(TAG, "the product category is "+productCategory);
        filterCheck = getArguments().getInt("filterChecked");
        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCategoryViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        Log.i(TAG, "inside oncreateview method");
        View view = inflater.inflate(R.layout.activity_category_product_view, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"inside onviewcreated method");
        mRecyclerView = view.findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(context, 2);
        mAdapter = new CategoryProductAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(productCategory);

        btnSort = view.findViewById(R.id.btnSort);
        btnFilter = view.findViewById(R.id.btnFilter);
        btnClose = view.findViewById(R.id.btnClose);
        mFrameLayout = view.findViewById(R.id.frameforSort);
        btnSortGroup = view.findViewById(R.id.btnSortGroup);

        mFrameLayout.setVisibility(View.INVISIBLE);

        btnSort.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        //lets try to put filter open-FF
        FILTER_CHECK = mViewModel.getFilterCheckStatus();

        mViewModel.getCategoryProduct(productCategory).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mAdapter.setProduct(products);
                filterProduct = new ArrayList<>();
                filterProduct.addAll(products);
            }
        });


        btnSortGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                mFrameLayout.setVisibility(View.INVISIBLE);
                RadioButton btnRb = (RadioButton) radioGroup.findViewById(checkedId);
                Log.i(TAG, "th selected radio button is "+btnRb);
                if (btnRb.getId()==R.id.btnPopularity){
                    Log.i(TAG, "inside popularity block");
                }
                switch (btnRb.getId()){
                    case R.id.btnPopularity:
                        sortTypeProduct(1);
                        break;
                    case R.id.btnPriceLtoH:
                        sortTypeProduct(2);
                        break;
                    case R.id.btnPriceHtoL:
                        sortTypeProduct(3);
                        break;
                    case R.id.btnNewest:
                        sortTypeProduct(4);
                        break;
                     default:
                         Log.i(TAG, "nothing selected");
                }
            }

            private void sortTypeProduct(int sortType) {


            }
        });

        Log.i(TAG, "filter check is "+filterCheck);
        Log.i(TAG, "FILTER_CHECK is "+FILTER_CHECK);

        if (filterCheck!= FILTER_CHECK){

            mViewModel.getFilterDetailsForProductView().observe(this, new Observer<List<FilterDetailModel>>() {
                @Override
                public void onChanged(List<FilterDetailModel> filterDetailModels) {
                    Log.i(TAG, "the filter detail model check ");
                    //here i m declaring it else mNewFilterSearch array will get null exception if its get called without entring the filterDetailModel
                    mNewFilterSearch = new ArrayList<>();
                    if (filterDetailModels!=null){
                        int detail_array_size = filterDetailModels.size();
                        for (int i=0; i<detail_array_size; i++){
                            if (filterDetailModels.get(i).getStatus()){
                                mNewFilterSearch.add(new FilterDetailModel(filterDetailModels.get(i).getCategoryName(),
                                        filterDetailModels.get(i).getType(), filterDetailModels.get(i).getItem()));
//                                Log.i(TAG, "the fiter item is "+mNewFilterSearch.get(i).getItem());
                            }
                        }
                        Log.i(TAG, "the size of mNewFilterSearch is "+mNewFilterSearch.size());



//                        for (int i=0; i<mNewFilterSearch.size();i++){
//                            Log.i(TAG, "category name "+mNewFilterSearch.get(i).getCategoryName()+", filter name "+
//                                    mNewFilterSearch.get(i).getItem()+
//                                    ", filter type "+mNewFilterSearch.get(i).getType());
//                        }


                    }


                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
           case R.id.btnSort:
               sortProduct();
               break;
           case R.id.btnFilter:
                productFilter();
                break;
            case R.id.btnClose:
                closeSortProduct();
                break;
        }
    }

    private void closeSortProduct() {
        mFrameLayout.setVisibility(View.INVISIBLE);
    }

    private void productFilter() {
        mViewModel.setProductCategoryName(productCategory);
        Navigation.findNavController(getActivity(), R.id.btnFilter).navigate(R.id.action_categoryProductView_to_productFilter);
    }

    private void sortProduct() {
        mFrameLayout.setVisibility(View.VISIBLE);
    }

}
