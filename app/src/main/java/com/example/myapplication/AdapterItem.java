package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<dataUser> dataUserArrayList;
    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY",id);

    public AdapterItem(Context context, ArrayList<dataUser> dataUserArrayList) {
        this.context = context;
        this.dataUserArrayList = dataUserArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
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
        TextView license,
                parking,
                check_in,
                check_out;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            license = itemView.findViewById(R.id.license);
            parking = itemView.findViewById(R.id.parking);
            check_in = itemView.findViewById(R.id.check_in);
            check_out = itemView.findViewById(R.id.check_out);
        }

        public void viewBind(dataUser dataUser) {
            license.setText(dataUser.getLicense());
            parking.setText(dataUser.getParking());
            check_out.setText(dataUser.getCheck_out());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            check_in.setText(dataUser.getCheck_in());
        }
    }
}
