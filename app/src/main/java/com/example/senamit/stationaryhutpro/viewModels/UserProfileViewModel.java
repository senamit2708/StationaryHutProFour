package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class UserProfileViewModel extends AndroidViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();
    private static DatabaseReference USER_PROFILE_REF;
    private MediatorLiveData<User> userProfile;
    private FirebaseQueryLiveData mFirebaseQueryLiveData;


    public UserProfileViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<User> getMyProfile(String mUserId) {
        if (userProfile == null){
            userProfile = new MediatorLiveData<>();
            USER_PROFILE_REF = FirebaseDatabase.getInstance().getReference("users/"+mUserId+"/profile");
            mFirebaseQueryLiveData = new FirebaseQueryLiveData(USER_PROFILE_REF);
            loadMyProfile();
        }
        return userProfile;

    }

    private void loadMyProfile() {
        userProfile.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                User profile = dataSnapshot.getValue(User.class);
                userProfile.setValue(profile);
            }
        });
    }
}
