package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PaymentSelection extends Fragment {

    private static final String TAG = PaymentSelection.class.getSimpleName();
    private Context context;
    private RadioGroup btnRadioPayment;
    private RadioButton btnCOD;
    private RadioButton btnNetBanking;
    private RadioButton btnPhonePe;
    private Button btnContinue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_payment_selection, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRadioPayment= view.findViewById(R.id.btnRadioPayment);
        btnContinue= view.findViewById(R.id.btnContinue);
        btnCOD= view.findViewById(R.id.btnCOD);
        btnNetBanking= view.findViewById(R.id.btnNetBanking);
        btnPhonePe = view.findViewById(R.id.btnPhonePe);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPaymentMethod = btnRadioPayment.getCheckedRadioButtonId();
                Log.i(TAG, "the selected radio button id is"+selectedPaymentMethod);



                if (btnCOD.getId()==selectedPaymentMethod){
                    Log.i(TAG, "the selected radio button is "+btnCOD.getText());
                    Navigation.findNavController(view).navigate(R.id.action_paymentSelection_to_orderDetails);
                }
                if (btnNetBanking.getId()==selectedPaymentMethod){
                    Log.i(TAG, "the selected radio button is net banking");
                    Toast.makeText(context, "Sorry Net Banking facility is not available",Toast.LENGTH_SHORT).show();
                }
                    if (btnPhonePe.getId() ==selectedPaymentMethod){
                    Toast.makeText(context, "Sorry PhonePe facility is not available",Toast.LENGTH_SHORT).show();
                }
                if (selectedPaymentMethod==0){
                        Toast.makeText(context, "Please select any payment gateway",Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }
}
