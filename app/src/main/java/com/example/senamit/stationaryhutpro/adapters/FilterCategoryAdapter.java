package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.interfaces.FilterCategoryInterface;
import com.example.senamit.stationaryhutpro.models.FilterCategoryModel;

import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

    private static final String TAG = FilterCategoryAdapter.class.getSimpleName();
    private Context context;

    private List<FilterCategoryModel> productCategoryList;
    private FilterCategoryInterface filterCategoryInterface;

    public FilterCategoryAdapter(Context context, FilterCategoryInterface filterCategoryInterface) {
        this.context = context;
        this.filterCategoryInterface = filterCategoryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_filter_category, parent, false);
        return new FilterCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (productCategoryList!=null){
            Log.i(TAG, "the category items are "+productCategoryList.get(position).getType());
            holder.txtCategory.setText(productCategoryList.get(position).getType());
        }

    }

    @Override
    public int getItemCount() {
        if (productCategoryList!= null){
            Log.i(TAG,"the size of productCategoryList is "+productCategoryList.size());
            return productCategoryList.size();
        }else {
            return 0;
        }

    }

    public void setCategory(List<FilterCategoryModel> filterCategoryModels) {
        if (filterCategoryModels!= null){
            productCategoryList = filterCategoryModels;

        }else {
            Log.w(TAG,"filterCategorymodels is empty");
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            String filterCategorySelection = productCategoryList.get(clickedItemIndex).getType();
            filterCategoryInterface.funFilterCategoty(filterCategorySelection);
            Log.i(TAG, "the selected product category is  "+filterCategorySelection);



        }
    }
}
