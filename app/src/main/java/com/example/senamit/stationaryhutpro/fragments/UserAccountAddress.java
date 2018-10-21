package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.UserAccountAddressAdapter;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAccountAddress extends Fragment implements UserAccountAddressAdapter.AddressButtonClickInterface {

    private static final String TAG = UserAddressView.class.getSimpleName();
    private UserAddressViewModel mViewModel;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAccountAddressAdapter mAdapter;

    private Context context;
    private Button btnAddNewAddress;
    private Address address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel= ViewModelProviders.of(getActivity()).get(UserAddressViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_account_address, container,false);
        context= container.getContext();
        mFirebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = mFirebaseAuth.getCurrentUser();


        btnAddNewAddress = view.findViewById(R.id.btnAddNewAddress);

        mRecyclerView = view.findViewById(R.id.recycler_address);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new UserAccountAddressAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel.getAddressList(currentUser.getUid()).observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if (addresses!=null){
                    Log.i(TAG, "inside viewmodel of not null address");
                    mAdapter.setAddress(addresses);
                }
            }
        });


        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.setAddressForEdit(null, null);
                Navigation.findNavController(view).navigate(R.id.action_userAccountAddress_to_userAddressEntry);

            }
        });
    }



    @Override
    public void funEditAddress(Address address, String key) {
        Log.i(TAG, "inside  funEditAddress "+key);
        mViewModel.setAddressForEdit(address, key);
//        Navigation.findNavController(view).navigate(R.id.action_userAddressView_to_userAddressEntry);
        Navigation.findNavController(getActivity(), R.id.btnAddNewAddress).navigate(R.id.action_userAccountAddress_to_userAddressEntry);


    }

    @Override
    public void funDeleteAddress(Address address) {
        Log.i(TAG, "inside funDeleteAddress ");
        mViewModel.deleteAddress(address, currentUser.getUid());
    }


}


