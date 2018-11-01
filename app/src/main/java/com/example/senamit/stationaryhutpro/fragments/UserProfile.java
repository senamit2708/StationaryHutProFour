package com.example.senamit.stationaryhutpro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.models.User;
import com.example.senamit.stationaryhutpro.viewModels.UserProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class UserProfile extends Fragment {

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmailId;
    private Button btnUpdate;
    private String mobileNumber;

    private DatabaseReference mUserDatabase;
    private String mFirebaseUser;
    private UserProfileViewModel mProfileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileViewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtEmailId = view.findViewById(R.id.txtEmail);
        btnUpdate = view.findViewById(R.id.btnUpdate);


        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProfileViewModel.getMyProfile(mFirebaseUser).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                txtFirstName.setText(user.getFirstName());
                txtLastName.setText(user.getLastName());
                txtEmailId.setText(user.getEmailId());
                mobileNumber = user.getMobileNumber();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String emailId = txtEmailId.getText().toString().trim();

                ((StationaryMainPage)getActivity()).hideSoftKeyboard(view);

                User userProfile = new User(mobileNumber, firstName, lastName, emailId);
                Map<String, Object> userProfileObject = userProfile.toUserProfileEntry();
                Map<String, Object> childUpdate = new HashMap<>();

                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser);
                childUpdate.put("profile", userProfileObject);
                mUserDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Navigation.findNavController(view).popBackStack(R.id.userProfile,true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}
