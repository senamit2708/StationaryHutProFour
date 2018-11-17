package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.OrderedProductViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UsersAllOrdersViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class OrderedProductDescription extends Fragment {

    private static final String TAG = OrderedProductViewModel.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";

    private Context context;
    private UsersAllOrdersViewModel mViewModel;
    private UserCart cartProduct;
    private String userId;
    private String productNumber;

    private TextView txtProductName;
    private TextView txtProductNumber;
    private TextView txtTotalPrice;
    private TextView txtOrderNumber;
    private TextView txtOrderStatus;
    private TextView txtOrderDate;
    private TextView txtQuantity;
    private TextView txtTotalQuantity;
    private ImageView imageProduct;
    private TextView txtAddress;
    private TextView txtPaymentMode;
    private ConstraintLayout mainView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UsersAllOrdersViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_ordered_product_description, container, false);
       context = container.getContext();
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductNumber = view.findViewById(R.id.txtProductNumber);
        txtOrderNumber = view.findViewById(R.id.txtOrderNumber);
        txtOrderStatus = view.findViewById(R.id.txtOrderStatus);
        txtOrderDate = view.findViewById(R.id.txtOrderDate);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        txtTotalQuantity = view.findViewById(R.id.txtTotalQuantity);
        imageProduct = view.findViewById(R.id.imageProduct);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPaymentMode = view.findViewById(R.id.txtPaymentMode);

        mainView = (ConstraintLayout) view.findViewById(R.id.constraint_main_layout);

        mViewModel.getSelectedCartProductForDesc(userId).observe(this, new Observer<UserCart>() {
         @Override
         public void onChanged(UserCart userCart) {
             Log.i(TAG, "userCart is null");
             if (userCart!=null){
                 Log.i(TAG, "userCart is not null");
                 setProductDetails(userCart);
             }
         }
     });

    mViewModel.getAddressDelivery().observe(this, new Observer<Address>() {
        @Override
        public void onChanged(Address address) {
            Log.i(TAG, "the address is "+address.getMobileNumber());
            setAddress(address);
        }
    });

    imageProduct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((StationaryMainPage)getActivity()).checkInternetConnection()){
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_KEY, productNumber);
                Navigation.findNavController(view).navigate(R.id.action_orderedProductDescription_to_productDescription,bundle);

            }else {
                showSnackbar(mainView, "No Internet Connection",Snackbar.LENGTH_SHORT);

            }

        }
    });

    }

    private void setAddress(Address address) {
        String name = address.getFullName();
        String mobileNo = address.getMobileNumber();
        String addressPartOne = address.getAddressPartOne();
        String addressPartTwo = address.getAddressPartTwo();
        String city = address.getCity();
        String state = address.getState();
        String pincode = address.getPincode();

        String finalAddress = name +"\n"+addressPartOne+"\n"+addressPartTwo+"\n"+city+","+state+" "+pincode+"\n"+"India"+"\n"+"Phone number: "+mobileNo;
        txtAddress.setText(finalAddress);
    }

    private void setProductDetails(UserCart userCart) {
        Log.i(TAG, "teh product name is "+userCart.getProductName());
        Picasso.with(context).load(userCart.getImageUrl()).fit().into(imageProduct);

        txtProductName.setText(userCart.getProductName());
        txtProductNumber.setText(userCart.getProductNumber());
        txtQuantity.setText(Integer.toString(userCart.getQuantity()));
        txtOrderNumber.setText(userCart.getOrderNumber());
        txtOrderStatus.setText(userCart.getOrderStatus());
        txtOrderDate.setText(userCart.getDate());
        txtPaymentMode.setText(userCart.getPaymentMode());
//        txtProductTotalPrice.setText(userCart.getProductPrice());
        int quantity = userCart.getQuantity();
        double price = userCart.getProductPrice();
        double totalPrice = (quantity * price);
        Log.i(TAG, "total price of product is "+totalPrice);
        Log.i(TAG,"the total price is "+totalPrice);
        txtTotalQuantity.setText("Total price of ("+quantity+" items)");
        txtTotalPrice.setText(Double.toString(totalPrice));

        productNumber= userCart.getProductNumber();
    }

    //snackbar
    public void showSnackbar(final View view, String message, int duration)
    {
        Snackbar snackbar = Snackbar.make(view, message, duration).setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar1 =Snackbar.make(view, "Sorry, No Connection", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
//        View snackView = snackbar.getView();
        snackbar.show();

    }

}
