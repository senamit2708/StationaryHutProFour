package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Address;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.ViewHolder> {

    private static final String TAG = UserAddressAdapter.class.getSimpleName();

    private List<Address> addressList = new ArrayList<>();
    private Context context;
    private int lastSelectedPosition = -1;
    private AddressButtonClickInterface mInterface;

    public UserAddressAdapter(Context context, AddressButtonClickInterface mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    public UserAddressAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_user_address, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (addressList!=null){
            holder.txtFullName.setText(addressList.get(position).getFullName());
            holder.txtMobileNumber.setText(addressList.get(position).getMobileNumber());
            holder.txtAddressPartOne.setText(addressList.get(position).getAddressPartOne());
            holder.txtAddressPartTwo.setText(addressList.get(position).getAddressPartTwo());
            holder.txtCity.setText(addressList.get(position).getCity());
            holder.txtState.setText(addressList.get(position).getState());
            holder.txtPincode.setText(addressList.get(position).getPincode());
            holder.btnSelected.setChecked(lastSelectedPosition== position);
            if (lastSelectedPosition==position){
                holder.btnEdit.setVisibility(View.VISIBLE);
            }else {
                holder.btnEdit.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (addressList!= null){
            return addressList.size();
        }else {
            return 0;
        }
    }

    public void setAddress(List<Address> addressList){
        if (addressList!= null){
            this.addressList = addressList;
            notifyDataSetChanged();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtFullName;
        TextView txtMobileNumber;
        TextView txtAddressPartOne;
        TextView txtAddressPartTwo;
        TextView txtCity;
        TextView txtState;
        TextView txtPincode;
        RadioButton btnSelected;
        Button btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtMobileNumber = itemView.findViewById(R.id.txtMobileNumber);
            txtAddressPartOne = itemView.findViewById(R.id.txtAddressPartOne);
            txtAddressPartTwo = itemView.findViewById(R.id.txtAddressPartTwo);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtState = itemView.findViewById(R.id.txtState);
            txtPincode = itemView.findViewById(R.id.txtPincode);
            btnSelected = itemView.findViewById(R.id.btnSelected);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnSelected.setOnClickListener(this);
            itemView.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String key;

            Address address;
            if (view==itemView){
                selectaddress();

            }
            switch (view.getId()){

                case R.id.btnSelected:
                    selectaddress();
                    break;
                case R.id.btnEdit:
                  int position = getAdapterPosition();
                key  = addressList.get(position).getFirebaseKey();
                    address = addressList.get(position);
                  mInterface.funEditAddress(address, key);
                  break;
                default:
                    Log.i(TAG, "select any other option");

            }
        }

        private void selectaddress() {
            lastSelectedPosition = getAdapterPosition();
            notifyDataSetChanged();
            Log.i(TAG, "the selected item is "+lastSelectedPosition);
          Address address = addressList.get(lastSelectedPosition);
//          btnEdit.setVisibility(View.VISIBLE);
          mInterface.funAddressSelected(address, lastSelectedPosition);
        }

    }

    public interface AddressButtonClickInterface{
        void funAddressSelected(Address address, int position);
        void funEditAddress(Address address, String key);
    }
}
