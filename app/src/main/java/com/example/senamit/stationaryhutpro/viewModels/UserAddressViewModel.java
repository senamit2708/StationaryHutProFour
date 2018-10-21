package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class UserAddressViewModel extends AndroidViewModel {

    private static final String TAG = UserAddressViewModel.class.getSimpleName();

    private static DatabaseReference USER_ADDRESS_REF;
    private static DatabaseReference DELIVERY_ADDRESS_REF;
    private static DatabaseReference mReference;
    private MediatorLiveData<List<Address>> addressList;
    private MediatorLiveData<Address> deliveryAddress;
    private FirebaseQueryLiveData liveData;
    private FirebaseQueryLiveData deliveryAddressLiveData;
    private Address editAddress;
    private String firebaseKey;

//    private Address address;
    private MutableLiveData<Address> addressMutableLiveData= new MutableLiveData<Address>();



    public UserAddressViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Address>> getAddressList(String uId){
        if (addressList==null){
            Log.i(TAG, "address live data is null");
            addressList = new MediatorLiveData<>();
            USER_ADDRESS_REF = FirebaseDatabase.getInstance().getReference("/users/"+uId+"/address");
            liveData= new FirebaseQueryLiveData(USER_ADDRESS_REF);
            loadAddressLiveData();
        }
        return addressList;
    }

    private void loadAddressLiveData() {
        addressList.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot !=null){
                    List<Address> addresses = new ArrayList<>();
                    for (DataSnapshot addressDataSnapshot : dataSnapshot.getChildren()){
                        Address address = addressDataSnapshot.getValue(Address.class);
                        addresses.add(address);
                        Log.i(TAG, "inside loadAddress live data"+address);
                    }
                    addressList.setValue(addresses);
                }else{
                    addressList.setValue(null);
                }
            }
        });
    }

    public void setPaymentAddress(Address address) {
//        this.address = address;
        Log.i(TAG, "the address mobile number is "+address.getMobileNumber());

        addressMutableLiveData.setValue(address);
        Log.i(TAG, "the address in setAddress is "+addressMutableLiveData);
    }

    public LiveData<Address> getAddress() {
        Log.i(TAG, "the address in getAddress is "+addressMutableLiveData);
        return addressMutableLiveData;

    }

    public LiveData<Address> getRecentlyUsedAddress(String uId) {
        if (deliveryAddress == null){
            deliveryAddress = new MediatorLiveData<>();
            DELIVERY_ADDRESS_REF = FirebaseDatabase.getInstance().getReference("/users/"+uId+"/address");
            deliveryAddressLiveData = new FirebaseQueryLiveData(DELIVERY_ADDRESS_REF.limitToFirst(1));
            loadDeliveryAddress();
        }
        return deliveryAddress;
    }

    private void loadDeliveryAddress() {
        deliveryAddress.addSource(deliveryAddressLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    Address address = dataSnapshot.getValue(Address.class);

                    deliveryAddress.setValue(address);
                }else {
                    deliveryAddress.setValue(null);
                }

            }
        });
    }



    public void deleteAddress(Address address, String uid) {
        mReference = FirebaseDatabase.getInstance().getReference();
        String key = address.getFirebaseKey();
        Log.i(TAG, "inside delete address viewmodel");
        mReference.child("users").child(uid).child("address").child(key).removeValue();
    }

    public void setAddressForEdit(Address address, String key) {
        editAddress = address;
        firebaseKey = key;
    }

    public Address getSelectedAddressForEdit() {
        return editAddress;
    }

    public String getSelectedAddressFirebaseKey() {
        return firebaseKey;
    }
}
