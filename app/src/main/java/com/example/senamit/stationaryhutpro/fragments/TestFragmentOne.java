package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragmentOne extends Fragment {

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_test_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Navigation.findNavController((Activity) context, R.id.linear1).navigate(R.id.action_testFragmentOne_to_signInUser);
    }
}
//    private void loadOrderedProductFirebase(List<String> keyList, String userId) {
//        Log.i(TAG, "the sizze of retrived product will be"+keyList.size());
//        if (keyList.size()>0){
//
//
//            for (int i=0; i<keyList.size(); i++){
//                final List<UserCart> orderedProductList = new ArrayList<>();
//                String productKey = keyList.get(i);
//                Log.i(TAG, "the product key is "+productKey);
//                ORDERED_PRODUCT = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order/"+productKey+"/product");
//                mFirebaseQueryLiveData = new FirebaseQueryLiveData(ORDERED_PRODUCT);
//
//                orderedProduct.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
//                    @Override
//                    public void onChanged(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot!= null){
//                            UserCart order = dataSnapshot.getValue(UserCart.class);
//                            Log.i(TAG, "the ordered product is "+order.getProductNumber());
//                            orderedProductList.add(order);
//
//                        }
//                        Log.i(TAG, "the size of ordered produtct in viewmodel is "+orderedProductList.size());
//
//                        orderedProduct.setValue(orderedProductList);
//
//                    }
//                });
//
//            }
//        }
//    }