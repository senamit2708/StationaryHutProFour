package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.FilterDetailsAdapter;
import com.example.senamit.stationaryhutpro.interfaces.FilterCheckedInterface;
import com.example.senamit.stationaryhutpro.models.FilterDetailModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterDetails extends Fragment implements FilterCheckedInterface {

    private static final String TAG = FilterDetails.class.getSimpleName();
    private final int FILTER_CHECK= 52;

    private Context context;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FilterDetailsAdapter mAdapter;

    private List<FilterDetailModel> filterDetailModels;
    private List<FilterDetailModel> indexbooleanStore;

    private ProductCategoryViewModel mViewModel;
    private Button btnApplyFilter;
    private Bundle bundle;
    private int count=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "inside oncreate method");
        context = getActivity();
        count++;
        Log.i(TAG,"count is "+count);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCategoryViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       Log.i(TAG, "inside oncreateview method");
        View view = inflater.inflate(R.layout.activity_filter_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "inside onviewcreated method");

        btnApplyFilter = view.findViewById(R.id.btnApplyFilter);

        mRecyclerView = view.findViewById(R.id.recycler_filter_details);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mViewModel.getCompleteFilterCategory().observe(this, new Observer<List<FilterDetailModel>>() {
            @Override
            public void onChanged(List<FilterDetailModel> filterList) {
                if (filterList!= null){
                    Log.i(TAG, "inside the new arraylist");
                    filterDetailModels = new ArrayList<>();
                    indexbooleanStore = new ArrayList<>();
                    //doing trail
                    for (int i=0;i<filterList.size();i++){
                        indexbooleanStore.add(
                                new FilterDetailModel(filterList.get(i).getIndex(),
                                        filterList.get(i).getStatus())
                        );
                    }
                    filterDetailModels.addAll(filterList);
                    Log.i(TAG, "the size of array list in fragment is "+filterDetailModels.size());
//                    mAdapter.setFilterDetails(filterDetailModels);
                }else {
                    Log.i(TAG, "array list is empty, it means no filter details available");
                }

            }
        });
        mViewModel.getFilterFromFilterCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String filterCategory) {
                Log.i(TAG, "the selected category is "+filterCategory);
                adjustFilterDetails(filterCategory);
            }

            private void adjustFilterDetails(String filterCategory) {
                Toast.makeText(context, "selected category is "+filterCategory, Toast.LENGTH_SHORT).show();
                if (filterCategory!= null) {
                    List<FilterDetailModel> list;
                    if (filterDetailModels!=null){
                        int size = filterDetailModels.size();
//                        Log.i(TAG, "the size of complete list is " + size);
                        list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            if (filterDetailModels.get(i).getType().equals(filterCategory)) {
                                list.add(new FilterDetailModel(filterDetailModels.get(i).getItem(),
                                        filterDetailModels.get(i).getType(),
                                        filterDetailModels.get(i).getIndex(),
                                        filterDetailModels.get(i).getStatus()));
                                indexbooleanStore.add(new FilterDetailModel(
                                        filterDetailModels.get(i).getIndex(),
                                        filterDetailModels.get(i).getStatus()
                                ));
                            }
                        }
//                        Log.i(TAG, "the size of list is " + list.size());
                        mAdapter = new FilterDetailsAdapter(context, FilterDetails.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.setFilterDetails(list);
                    }
                }
            }
        });

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.setFilterCheckStatus(FILTER_CHECK);
                mViewModel.setIndexBoolean(indexbooleanStore);
                mViewModel.setCompleteFilterCategory(filterDetailModels);
                Navigation.findNavController(view).popBackStack(R.id.productFilter, true);
            }
        });
    }

    @Override
    public void funCheckedIndex(int indexPosition, boolean option) {
        filterDetailModels.get(indexPosition).setStatus(option);
        indexbooleanStore.get(indexPosition).setStatus(option);
//        Log.i(TAG, "position and option is "+indexPosition+"  "+option);
    }


//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.i(TAG, "inside on save instance state");
//
//
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        Log.i(TAG,"inside viewstaterestored");
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        count=3;
//        Log.i(TAG, "on pause method is called");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.i(TAG, "inside onstop method");
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.i(TAG, "inside ondestroy method");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i(TAG, "on resume method is called, count is "+count);
//    }
}
