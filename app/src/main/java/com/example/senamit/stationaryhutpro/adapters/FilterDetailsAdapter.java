package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.interfaces.FilterCheckedInterface;
import com.example.senamit.stationaryhutpro.models.FilterDetailModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterDetailsAdapter extends RecyclerView.Adapter<FilterDetailsAdapter.ViewHolder> {

    private static final String TAG = FilterDetailsAdapter.class.getSimpleName();
    private Context context;
    private FilterCheckedInterface mInterface;

    private List<FilterDetailModel> filterDetailsList;

    public FilterDetailsAdapter(Context context, FilterCheckedInterface mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_filter_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (filterDetailsList!=null){
//            holder.bind(position);
            holder.txtFilterDetail.setText(filterDetailsList.get(position).getItem());
//            Log.i(TAG, "the txtfilter data is "+filterDetailsList.get(position).getItem());
//            Log.i(TAG,"inside bindviewholder method "+filterDetailsList.get(position).getStatus());

            if (filterDetailsList.get(position).getStatus()){
//                Log.i(TAG,"inside bindviewholder method "+filterDetailsList.get(position).getStatus());
                holder.mCheckBox.setChecked(true);
            }else {
                holder.mCheckBox.setChecked(false);
//                Log.i(TAG,"inside bindviewholder method "+filterDetailsList.get(position).getStatus());

            }

        }
    }

    @Override
    public int getItemCount() {
        if (filterDetailsList!= null){
            return filterDetailsList.size();
        }else {
            return 0;
        }

    }

    public void setFilterDetails(List<FilterDetailModel> filterDetailModels) {
        if (filterDetailModels!= null){
            filterDetailsList = filterDetailModels;
            Log.i(TAG, "the size of filter details in adapter is "+filterDetailsList.size());
        }else {
            Log.i(TAG, "list is empty in adapter");
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtFilterDetail;
        CheckBox mCheckBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterDetail = itemView.findViewById(R.id.txtFilterDetail);
            mCheckBox = itemView.findViewById(R.id.checkBoxSelection);
            itemView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            Log.i(TAG, "inside onclick function");
//            Log.i(TAG,"status of filterdetailslist is"+filterDetailsList.get(adapterPosition).getStatus());
           if(view==itemView){
               Log.i(TAG, "inside onclick function");
               checkOption();
           }else if (view.getId()==R.id.checkBoxSelection){
               Log.i(TAG, "inside the checkbox selection");
               checkOption();
           }
        }

        private void checkOption() {
            int adapterPosition = getAdapterPosition();
            if (filterDetailsList.get(adapterPosition).getStatus()){
                mCheckBox.setChecked(false);
                filterDetailsList.get(adapterPosition).setStatus(false);
//                Log.i(TAG,"checkbox is false now");
                mInterface.funCheckedIndex(filterDetailsList.get(adapterPosition).getIndex(), false);
            }else {
                mCheckBox.setChecked(true);
                filterDetailsList.get(adapterPosition).setStatus(true);
//                Log.i(TAG, "checkbox is checked now");
                mInterface.funCheckedIndex(filterDetailsList.get(adapterPosition).getIndex(), true);

            }
        }

    }
}
