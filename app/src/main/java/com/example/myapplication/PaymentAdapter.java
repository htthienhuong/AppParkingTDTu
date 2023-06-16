package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ItemViewHolder> {
    Context context;
    ArrayList<Payment> dataUserArrayList;
    Locale id = new Locale("in","ID");

    public PaymentAdapter(Context context, ArrayList<Payment> dataUserArrayList) {
        this.context = context;
        this.dataUserArrayList = dataUserArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.viewBind(dataUserArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataUserArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView amount,
                parking,

                check_out;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            parking = itemView.findViewById(R.id.parking_payment);
            check_out = itemView.findViewById(R.id.check_in_payment);
        }

        public void viewBind(Payment dataUser) {
            amount.setText("-" +dataUser.getAmount().toString());
            parking.setText(dataUser.getParking());
            check_out.setText(dataUser.getCheck_out());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        }
    }
}
