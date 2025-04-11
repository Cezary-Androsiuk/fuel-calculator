package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FuelRecordRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context m_context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<FuelRecordModel> m_fuelRecordModels;

    public FuelRecordRecyclerViewAdapter(Context context, ArrayList<FuelRecordModel> fuelRecordModels){
        m_context = context;
        m_fuelRecordModels = fuelRecordModels;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(m_context);
        if (viewType == TYPE_HEADER) {
            View headerView = inflater.inflate(R.layout.fuel_record_header_row, parent, false);

            Button addNewRecordButton = headerView.findViewById(R.id.addNewRecordButton);
            MainActivity mainActivity = (MainActivity) m_context;
            addNewRecordButton.setOnClickListener(v -> mainActivity.addNewRecord());

            return new HeaderViewHolder(headerView);
        } else {
            View view = inflater.inflate(R.layout.fuel_record_row, parent, false);
            return new FuelRecordRecyclerViewAdapter.ItemViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /// here implement info about how recyclerview changes his items
        if(holder instanceof ItemViewHolder)
        {
            int normalizedPosition = position -1; // we are skipping header
            FuelRecordModel frm = m_fuelRecordModels.get(normalizedPosition);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.fromFuelTextView.setText( Double.toString(frm.getFromFuel()) );
            itemViewHolder.toFuelTextView.setText( Double.toString(frm.getToFuel()) );
        }
    }

    @Override
    public int getItemCount() {
        return m_fuelRecordModels.size() +1; // +1 is for header
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        /// this class is an object for our fuel_record_row // i guess
        TextView fromFuelTextView, toFuelTextView;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            fromFuelTextView = itemView.findViewById(R.id.fromFuelTextView);
            toFuelTextView = itemView.findViewById(R.id.toFuelTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
