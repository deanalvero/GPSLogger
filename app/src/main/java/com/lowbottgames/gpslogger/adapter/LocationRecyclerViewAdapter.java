package com.lowbottgames.gpslogger.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lowbottgames.gpslogger.R;
import com.lowbottgames.gpslogger.database.LocationInfo;

import java.util.Date;
import java.util.List;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocationInfo> items;
    private LocationRecyclerViewAdapterListener listener;

    public LocationRecyclerViewAdapter(){
    }

    public interface LocationRecyclerViewAdapterListener {
        void onClickLocationInfo(LocationInfo itemObject);
    }

    public void setItemList(List<LocationInfo> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void setLocationRecyclerViewAdapterListener(LocationRecyclerViewAdapterListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new DLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        DLocationViewHolder viewHolder = (DLocationViewHolder) holder;

        LocationInfo item = items.get(position);

        viewHolder.textViewTime.setText(DateFormat.format("MM/dd/yyyy hh:mm:ss a", new Date(item.getTime())).toString());
        viewHolder.textViewCoordinates.setText(String.format("%f, %f", item.getLatitude(), item.getLongitude()));
        viewHolder.textViewProvider.setText(item.getProvider());

        viewHolder.listener = new DLocationViewHolder.DLocationViewHolderListener() {
            @Override
            public void onClickItem() {
                if (LocationRecyclerViewAdapter.this.listener != null){
                    LocationRecyclerViewAdapter.this.listener.onClickLocationInfo(items.get(holder.getAdapterPosition()));
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public static class DLocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime, textViewCoordinates, textViewProvider;
        DLocationViewHolderListener listener;

        public DLocationViewHolder(View itemView) {
            super(itemView);

            textViewTime = (TextView) itemView.findViewById(R.id.textView_time);
            textViewCoordinates = (TextView) itemView.findViewById(R.id.textView_coordinates);
            textViewProvider = (TextView) itemView.findViewById(R.id.textView_provider);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClickItem();
                    }
                }
            });
        }

        public interface DLocationViewHolderListener {
            void onClickItem();
        }
    }
}
