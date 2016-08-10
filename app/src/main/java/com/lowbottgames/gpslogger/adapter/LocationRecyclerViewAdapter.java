package com.lowbottgames.gpslogger.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lowbottgames.gpslogger.R;
import com.lowbottgames.gpslogger.db.GPSData;

import java.util.Date;
import java.util.List;

/**
 * Created by dean on 31/05/16.
 */
public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GPSData> mItemList;
    private LocationRecyclerViewAdapterListener mListener;

    public LocationRecyclerViewAdapter(){
    }

    public interface LocationRecyclerViewAdapterListener {
        void onClickGPSDataItem(GPSData itemObject);
    }

    public void setItemList(List<GPSData> itemList){
        this.mItemList = itemList;
        notifyDataSetChanged();
    }

    public void setLocationRecyclerViewAdapterListener(LocationRecyclerViewAdapterListener listener){
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new DLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DLocationViewHolder viewHolder = (DLocationViewHolder) holder;

        final GPSData item = mItemList.get(position);

        viewHolder.textViewTime.setText(DateFormat.format("MM/dd/yyyy hh:mm:ss a", new Date(item.getTime())).toString());
        viewHolder.textViewCoordinates.setText(String.format("%f, %f", item.getLatitude(), item.getLongitude()));
        viewHolder.textViewProvider.setText(item.getProvider());

        viewHolder.listener = new DLocationViewHolder.DLocationViewHolderListener() {
            @Override
            public void onClickItem() {
                if (LocationRecyclerViewAdapter.this.mListener != null){
                    LocationRecyclerViewAdapter.this.mListener.onClickGPSDataItem(item);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) return 0;
        return mItemList.size();
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
