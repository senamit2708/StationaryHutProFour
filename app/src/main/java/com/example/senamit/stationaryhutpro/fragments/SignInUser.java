package com.example.senamit.stationaryhutpro.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SignInUser extends Fragment {

    private static final String TAG = SignInUser.class.getSimpleName();
    private static final int SUCCESS_CODE = 99;
    private int RESULT_CODE= 0;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private DatabaseReference mDatabase;

    private EditText mPhoneNumberField;
    private EditText mPhoneOtp;
    private Button btnPhoneNumber;
    private Button btnSubmit;
    private Button btnResendOtp;
    private Context context;
//    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_sign_in_user, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();

//        fragmentView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mPhoneNumberField = view.findViewById(R.id.edt_phone_number);
        mPhoneOtp = view.findViewById(R.id.edt_phone_otp);
        btnPhoneNumber = view.findViewById(R.id.btn_phone_number_enter);
        btnSubmit = view.findViewById(R.id.btn_sign_in);
        btnResendOtp = view.findViewById(R.id.btn_resend_otp);

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhoneNumber()) {
                    Log.i(TAG, "inside the if statement");
                    return;
                }
                Log.i(TAG, "inside the else statement ");
                String phoneNumberField = mPhoneNumberField.getText().toString();
                String countryCode = "+91-";
                String phoneNumber = countryCode + phoneNumberField;
                Log.d(TAG, "phone number is " + phoneNumber);
                startPhoneNumberVerification(phoneNumber);
                Log.i(TAG, "outside result code and success code method");
                RESULT_CODE=10;
//                if (RESULT_CODE== SUCCESS_CODE){
//                    Log.i(TAG, "inside result code and success code method");
////                    Navigation.findNavController(view).navigate(R.id.action_signInUser_to_testFragmentOne, null);
//                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mPhoneOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mPhoneOtp.setError("Cannot be empty.");
                    return;
                }
                RESULT_CODE=20;
                verifyPhoneNumberWithCode(mVerificationId, code);

//                if (RESULT_CODE== SUCCESS_CODE){
//                    Navigation.findNavController(view).navigate(R.id.action_signInUser_to_testFragmentOne, null);
//                }
            }
        });
        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "inside resend otp method");
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
            }
        });



        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i(TAG, "inside onverification completed method");
//                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }


            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.i(TAG, "inside onverificationfailed method");
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number");
                    Log.i(TAG, "inside the exception one of verification failed");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.i(TAG, "inside the exception two of verification failed");

                    Toast.makeText(context, "Please try another way of login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "the code sent is " + verificationId);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

            }
        };

    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "login credential successful");
                            FirebaseUser user = task.getResult().getUser();
                            Log.i(TAG, "the user is " + user);
                            onAuthSuccess(user);
                        } else {
                            Log.i(TAG, "login failed inside signInwithphoneauthcredential");
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        writeNewUser(user.getUid(), phoneNumber);
        Log.i(TAG, "the RESULT_CODE number is "+ RESULT_CODE);
        if (RESULT_CODE==10){
            Log.i(TAG, "inside the result_code 10 ");
//            Navigation.findNavController((Activity) context, R.id.btn_phone_number_enter).navigate(R.id.action_signInUser_to_testFragmentOne);
        }
        if (RESULT_CODE==20){
            Log.i(TAG, "inside the result_code 20 ");
//            Navigation.findNavController((Activity) context, R.id.btn_sign_in).navigate(R.id.action_signInUser_to_testFragmentOne);

        }
//        Navigation.findNavController((Activity) context, R.id.btn_phone_number_enter).navigate(R.id.action_signInUser_to_testFragmentOne);
//        startActivity(new Intent(SignInActivity.this, MainActivityFirst.class));

    }

    private void writeNewUser(String userId, String phoneNumber) {
        User user = new User(phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i(TAG, "inside start phone number verification method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                (Activity)context,
                mCallback);
        mVerificationInProgress = true;

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        Log.i(TAG, "inside resend verification code method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                (Activity)context,
                mCallback,
                token);
        mVerificationInProgress = true;

    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("invalid mobile number");
            Log.i(TAG, "inside validate phone number");
            Toast.makeText(context, "invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!getContryCode(phoneNumber)) {
            return false;
        }
        return true;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
////        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
////        if (currentUser != null) {
////            Log.i(TAG, "user already avaibale");
////            Navigation.findNavController(getView()).navigate(R.id.action_signInUser_to_testFragmentOne);
////        }
//        Log.i(TAG, "inside the onstart method");
//    }

    private boolean getContryCode(String phoneNumber) {
        String countryCode;
        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getSimCountryIso();
        if(countryCodeValue.equals("in")){
            Log.i(TAG, "the country code is india");
            return true;
        }
        return false;

    }
}
