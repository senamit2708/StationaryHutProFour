package com.example.senamit.stationaryhutpro.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import androidx.appcompat.app.AppCompatActivity;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import com.example.senamit.stationaryhutpro.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SignInActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private DatabaseReference mDatabase;

    private EditText mPhoneNumberField;
    private EditText mPhoneOtp;
    private TextView mTxtResendOtp;
    private TextView mTxtSignInHeadline;
    private Button btnPhoneNumber;
    private Button btnSubmit;
    private Button btnResendOtp;
    private Context context;
    private TextInputLayout txtStyleEditPhoneNumber;
    private TextInputLayout txtStylePhoneOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.i(TAG, "inside oncreate method");
        mPhoneNumberField = findViewById(R.id.edt_phone_number);
        mPhoneOtp = findViewById(R.id.edt_phone_otp);
        mTxtResendOtp = findViewById(R.id.txtResendOtp);
        mTxtSignInHeadline = findViewById(R.id.txtSignInHeadline);
        btnPhoneNumber = findViewById(R.id.btn_phone_number_enter);
        btnSubmit = findViewById(R.id.btn_sign_in);
        btnResendOtp = findViewById(R.id.btn_resend_otp);
        txtStyleEditPhoneNumber = findViewById(R.id.txtStyleEditPhoneNumber);
        txtStylePhoneOtp = findViewById(R.id.txtStylePhoneOtp);
        context = this;

        btnPhoneNumber.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Rubik-Bold.ttf");

        mTxtSignInHeadline.setTypeface(customFont);


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
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    mPhoneNumberField.setError("Invalid phone number  "+((FirebaseAuthInvalidCredentialsException) e).getErrorCode());
                    Log.i(TAG, "inside the exception one of verification failed");
                }else if (e instanceof FirebaseTooManyRequestsException){
                    Log.i(TAG, "inside the exception two of verification failed "+e.getMessage());

                    Toast.makeText(SignInActivity.this, "Please try another way of login", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "inside onverification failed last portion  "+e.getMessage());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "the code sent is "+verificationId);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

            }
        };

    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "login credential successful");
                            FirebaseUser user = task.getResult().getUser();
                            Log.i(TAG, "the user is "+user);
                            onAuthSuccess(user);
                        }
                        else {
                            mPhoneOtp.setError("Invalid OTP number");
                            Log.i(TAG, "login failed inside signInwithphoneauthcredential");
                        }

                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        writeNewUser(user.getUid(), phoneNumber);
        startActivity(new Intent(SignInActivity.this, StationaryMainPage.class));
        finish();


    }

    private void writeNewUser(String userId, String phoneNumber) {
        User user = new User(phoneNumber.trim());
        mDatabase.child("users").child(userId).child("profile").setValue(user);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i(TAG, "inside start phone number verification method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
        mVerificationInProgress = true;

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        Log.i(TAG, "inside resend verification code method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback,
                token);
        mVerificationInProgress = true;

    }

    private boolean validatePhoneNumber( ) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)){
            mPhoneNumberField.setError("invalid mobile number");
            Log.i(TAG, "inside validate phone number");
            Toast.makeText(SignInActivity.this, "invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!getContryCode(phoneNumber)){
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null){
            Log.i(TAG, "user already avaibale");
            startActivity(new Intent(SignInActivity.this, StationaryMainPage.class));
            finish();
        }
        Log.i(TAG, "inside the onstart method");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_phone_number_enter:
                if(!validatePhoneNumber()){
                    Log.i(TAG, "inside the if statement");
                    return;
                }
                Log.i(TAG, "inside the else statement ");
                String phoneNumberField = mPhoneNumberField.getText().toString();
                String countryCode = "+91-";
                String phoneNumber = countryCode +phoneNumberField;
                Log.d(TAG, "phone number is "+phoneNumber);
                startPhoneNumberVerification(phoneNumber);
                txtStyleEditPhoneNumber.setVisibility(View.GONE);
                btnPhoneNumber.setVisibility(View.GONE);
                txtStylePhoneOtp.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                mTxtResendOtp.setVisibility(View.VISIBLE);
                btnResendOtp.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_sign_in:
                String code = mPhoneOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mPhoneOtp.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btn_resend_otp:
                Log.i(TAG, "inside resend otp method");
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            default:
                Log.i(TAG, "no button found of such type");

        }

    }

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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
