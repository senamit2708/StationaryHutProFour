package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.UserAllOrdersAdapter;
import com.example.senamit.stationaryhutpro.interfaces.OrderedProductDescInterface;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.UsersAllOrdersViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserOrders extends Fragment implements OrderedProductDescInterface {

    private static final String TAG = UserOrders.class.getSimpleName();
    private Context context;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAllOrdersAdapter mAdapter;
    private UsersAllOrdersViewModel mViewModel;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UsersAllOrdersViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_orders, container,false);
        context = container.getContext();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = view.findViewById(R.id.recycler_order);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new UserAllOrdersAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel.getAllOrders(userId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(List<UserCart> userCarts) {
                if (userCarts!= null){
//                    List<UserCart> userCartsReverse = new ArrayList<>();
//                    Collections.reverse(userCarts);
                    mAdapter.setOrderList(userCarts);
                }

            }
        });


    }

    @Override
    public void funOrderdProductSelection(String cartProductKey) {
        Log.i(TAG, "the cart product key is  "+cartProductKey);
        mViewModel.setSelectedCartProductForDesc(cartProductKey);
    }
}
