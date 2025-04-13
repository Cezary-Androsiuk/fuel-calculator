package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androsiukcezary.fuelcalculator.data.EndTripRecordModel;
import com.androsiukcezary.fuelcalculator.data.FirstRecordModel;
import com.androsiukcezary.fuelcalculator.data.FuelRecordModel;
import com.androsiukcezary.fuelcalculator.data.FuelRecordType;
import com.androsiukcezary.fuelcalculator.data.PaymentRecordModel;
import com.androsiukcezary.fuelcalculator.data.RefuelingRecordModel;
import com.androsiukcezary.fuelcalculator.data.StartTripRecordModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FuelRecordRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private static final int TYPE_HEADER = -1;
    private ArrayList<FuelRecordModel> fuelRecordModels;

    public FuelRecordRecyclerViewAdapter(Context context, ArrayList<FuelRecordModel> fuelRecordModels){
        this.context = context;
        this.fuelRecordModels = fuelRecordModels;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        int normalizedPosition = position -1; // offset for the header
//        return fuelRecordModels.get(fuelRecordModels.size() - normalizedPosition-1).getFuelRecordType().ordinal();
        return fuelRecordModels.get(normalizedPosition).getFuelRecordType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER)
        {
            View headerView = inflater.inflate(R.layout.fuel_record_header_row, parent, false);
            return new HeaderViewHolder(headerView);
        }
        else if(viewType == FuelRecordType.FirstRecordType.ordinal())
        {
            View firstRecordView = inflater.inflate(R.layout.first_record_row, parent, false);
            return new FirstRecordViewHolder(firstRecordView);
        }
        else if(viewType == FuelRecordType.StartTripType.ordinal())
        {
            View startTripRecordView = inflater.inflate(R.layout.start_trip_record_row, parent, false);
            return new StartTripRecordViewHolder(startTripRecordView);
        }
        else if(viewType == FuelRecordType.EndTripType.ordinal())
        {
            View endTripRecordView = inflater.inflate(R.layout.end_trip_record_row, parent, false);
            return new EndTripRecordViewHolder(endTripRecordView);
        }
        else if(viewType == FuelRecordType.RefuelingType.ordinal())
        {
            View refuelingRecordView = inflater.inflate(R.layout.refueling_record_row, parent, false);
            return new RefuelingRecordViewHolder(refuelingRecordView);
        }
        else if(viewType == FuelRecordType.PaymentType.ordinal())
        {
            View paymentRecordView = inflater.inflate(R.layout.payment_record_row, parent, false);
            return new PaymentRecordViewHolder(paymentRecordView);
        }
        // else
        View view = inflater.inflate(R.layout.unknown_fuel_record_row, parent, false);
        return new UnknownRecordViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try{

            /// here implement info about how recyclerview changes his items
            if(holder instanceof HeaderViewHolder)
            {
                ///  Set button ClickListener to open method in Main Activity
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                MainActivity mainActivity = (MainActivity) context;
                headerViewHolder.addNewRecordButton.setOnClickListener(v -> mainActivity.askIfAddNewRecord());

                headerViewHolder.deleteNewestRecordButton.setVisibility(fuelRecordModels.size() > 1 ? View.VISIBLE : View.INVISIBLE);
                headerViewHolder.deleteNewestRecordButton.setOnClickListener(v -> mainActivity.askIfDeleteRecord(0));

                return;
            }

            int normalizedPosition = position -1; // offset for the header
//            FuelRecordModel frm = fuelRecordModels.get(fuelRecordModels.size() - normalizedPosition-1);
            FuelRecordModel frm = fuelRecordModels.get(normalizedPosition);

            if(frm.getFuelRecordType() == FuelRecordType.FirstRecordType) // holder instanceof FirstRecordViewHolder
            {
                FirstRecordViewHolder firstRecordViewHolder = (FirstRecordViewHolder) holder;
                FirstRecordModel firstRecordModel = (FirstRecordModel) frm;
                firstRecordViewHolder.balanceTextView.setText(
                        firstRecordViewHolder.balancePrefix +
                                String.format("%.2f", firstRecordModel.getInitialPLNValue()) +
                                "zł"
                );
                firstRecordViewHolder.fuelTextView.setText(
                        firstRecordViewHolder.fuelPrefix +
                                String.format("%.2f", firstRecordModel.getCurrentFuel()) +
                                "L"
                );
                firstRecordViewHolder.fuelPriceTextView.setText(
                        firstRecordViewHolder.fuelPricePrefix +
                                String.format("%.2f", firstRecordModel.getCurrentFuelPrice()) +
                                "zł"
                );
                return;
            }
            if(frm.getFuelRecordType() == FuelRecordType.StartTripType)
            {
                StartTripRecordViewHolder startTripRecordViewHolder = (StartTripRecordViewHolder) holder;
                StartTripRecordModel startTripRecordModel = (StartTripRecordModel) frm;
                startTripRecordViewHolder.fuelTextView.setText(
                        startTripRecordViewHolder.fuelPrefix +
                                String.format("%.2f", startTripRecordModel.getCurrentFuel()) +
                                "L"
                );
                return;
            }
            if(frm.getFuelRecordType() == FuelRecordType.EndTripType)
            {
                EndTripRecordViewHolder endTripRecordViewHolder = (EndTripRecordViewHolder) holder;
                EndTripRecordModel endTripRecordModel = (EndTripRecordModel) frm;
                endTripRecordViewHolder.fuelTextView.setText(
                        endTripRecordViewHolder.fuelPrefix +
                                String.format("%.2f", endTripRecordModel.getCurrentFuel()) +
                                "L"
                );

                return;
            }
            if(frm.getFuelRecordType() == FuelRecordType.RefuelingType)
            {
                RefuelingRecordViewHolder refuelingRecordViewHolder = (RefuelingRecordViewHolder) holder;
                RefuelingRecordModel refuelingRecordModel = (RefuelingRecordModel) frm;
                refuelingRecordViewHolder.refueledQuantityTextView.setText(
                        refuelingRecordViewHolder.refueledQuantityPrefix +
                                String.format("%.2f", refuelingRecordModel.getRefueledQuantity()) +
                                "L"
                );
                refuelingRecordViewHolder.fuelPriceTextView.setText(
                        refuelingRecordViewHolder.fuelPricePrefix +
                                String.format("%.2f", refuelingRecordModel.getFuelPrice()) +
                                "zł"
                );
                return;
            }
            if(frm.getFuelRecordType() == FuelRecordType.PaymentType)
            {
                PaymentRecordViewHolder paymentRecordViewHolder = (PaymentRecordViewHolder) holder;
                PaymentRecordModel paymentRecordModel = (PaymentRecordModel) frm;

                paymentRecordViewHolder.paymentTextView.setText(
                        paymentRecordViewHolder.paymentPrefix +
                                String.format("%.2f", paymentRecordModel.getMoneyPaid()) +
                                "zł"
                );
                return;
            }

            Log.i("FUEL_RECORD_RECYCLER_VIEW_ADAPTER_ACTIVITY_LOGS", "unknown Fuel Record Type");
            UnknownRecordViewHolder unknownRecordViewHolder = (UnknownRecordViewHolder) holder;
            // unknownRecordViewHolder // - no need to assign anything
        }
        catch (Exception e)
        {
            Log.i("FUEL_RECORD_RECYCLER_VIEW_ADAPTER_ACTIVITY_LOGS", "cast error: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return fuelRecordModels.size() +1; // +1 is for header
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        Button addNewRecordButton;
        ImageButton deleteNewestRecordButton;

        public HeaderViewHolder(View headerView) {
            super(headerView);

            addNewRecordButton = headerView.findViewById(R.id.addNewRecordButton);
            deleteNewestRecordButton = headerView.findViewById(R.id.deleteNewestRecordButton);
        }
    }

    public static class FirstRecordViewHolder extends RecyclerView.ViewHolder{
        TextView balanceTextView;
        String balancePrefix;
        TextView fuelTextView;
        String fuelPrefix;
        TextView fuelPriceTextView;
        String fuelPricePrefix;

        public FirstRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            balanceTextView = itemView.findViewById(R.id.balanceTextView);
            fuelTextView = itemView.findViewById(R.id.fuelTextView);
            fuelPriceTextView = itemView.findViewById(R.id.fuelPriceTextView);

            balancePrefix = String.valueOf(balanceTextView.getText());
            fuelPrefix = String.valueOf(fuelTextView.getText());
            fuelPricePrefix = String.valueOf(fuelPriceTextView.getText());
        }
    }

    public static class StartTripRecordViewHolder extends RecyclerView.ViewHolder{
        TextView fuelTextView;
        String fuelPrefix;

        public StartTripRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            fuelTextView = itemView.findViewById(R.id.fuelTextView);
            fuelPrefix = String.valueOf(fuelTextView.getText());
        }
    }

    public static class EndTripRecordViewHolder extends RecyclerView.ViewHolder{
        TextView fuelTextView;
        String fuelPrefix;
        public EndTripRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            fuelTextView = itemView.findViewById(R.id.fuelTextView);
            fuelPrefix = String.valueOf(fuelTextView.getText());
        }
    }

    public static class RefuelingRecordViewHolder extends RecyclerView.ViewHolder{
        TextView refueledQuantityTextView;
        String refueledQuantityPrefix;
        TextView fuelPriceTextView;
        String fuelPricePrefix;

        public RefuelingRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            refueledQuantityTextView = itemView.findViewById(R.id.refueledQuantityTextView);
            fuelPriceTextView = itemView.findViewById(R.id.fuelPriceTextView);

            refueledQuantityPrefix = String.valueOf(refueledQuantityTextView.getText());
            fuelPricePrefix = String.valueOf(fuelPriceTextView.getText());
        }
    }

    public static class PaymentRecordViewHolder extends RecyclerView.ViewHolder{
        TextView paymentTextView;
        String paymentPrefix;

        public PaymentRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentTextView = itemView.findViewById(R.id.paymentTextView);
            paymentPrefix = String.valueOf(paymentTextView.getText());
        }
    }

    public static class UnknownRecordViewHolder extends RecyclerView.ViewHolder{
        /// this class is an object for our fuel_record_row // i guess
        public UnknownRecordViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
