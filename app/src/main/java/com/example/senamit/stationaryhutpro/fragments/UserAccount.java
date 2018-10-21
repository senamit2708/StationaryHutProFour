package com.example.senamit.stationaryhutpro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.SignInActivity;
import com.example.senamit.stationaryhutpro.models.User;
import com.example.senamit.stationaryhutpro.viewModels.UserProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class UserAccount extends Fragment implements View.OnClickListener{

    private static final String TAG = UserAccount.class.getSimpleName();

    private Button btnViewAllOrderes;
    private Button btnMyAddresses;
    private Button btnEditProfile;
    private Button btnLogout;
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtMobileNumber;
    private TextView txtEmailId;
    private String mUserId;
    private FirebaseUser mFirebaseUser;
    private UserProfileViewModel mProfileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileViewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_account, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnViewAllOrderes = view.findViewById(R.id.btnviewAllOrders);
        btnMyAddresses = view.findViewById(R.id.btnMyAddresses);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnViewAllOrderes.setOnClickListener(this);
        btnMyAddresses.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtMobileNumber = view.findViewById(R.id.txtMobileNumber);
        txtEmailId = view.findViewById(R.id.txtEmail);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        mProfileViewModel.getMyProfile(mUserId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                txtFirstName.setText(user.getFirstName());
                txtLastName.setText(user.getLastName());
                txtMobileNumber.setText(user.getMobileNumber());
                txtEmailId.setText(user.getEmailId());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditProfile:
                Navigation.findNavController(view).navigate(R.id.action_userAccount_to_userProfile);
                break;
            case R.id.btnviewAllOrders:
                Navigation.findNavController(view).navigate(R.id.action_userAccount_to_userOrders);
                break;
            case R.id.btnMyAddresses:
                Log.i(TAG, "inside the btn of my address");
                Navigation.findNavController(getActivity(), R.id.btnMyAddresses).navigate(R.id.action_userAccount_to_userAccountAddress);
                break;
            case R.id.btnLogout:
                logoutOfThisApp();
                break;
            default:

            }
        }

    private void logoutOfThisApp() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);

    }
}
//}
