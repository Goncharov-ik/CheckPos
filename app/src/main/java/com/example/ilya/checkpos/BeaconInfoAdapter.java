package com.example.ilya.checkpos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ilya.checkpos.activities.BeaconInfo;

import java.util.ArrayList;

/**
 * Created by ar_no on 26.10.2017.
 */

public class BeaconInfoAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BeaconInfo> beaconData;

    public BeaconInfoAdapter(Context context, ArrayList<BeaconInfo> items) {
        beaconData = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beaconData.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.list_item_beacon_info, parent, false);

        TextView uniqueId = (TextView) rowView.findViewById(R.id.uniqueIdView);
        TextView place = (TextView) rowView.findViewById(R.id.placeView);
        TextView coordX = (TextView) rowView.findViewById(R.id.coordXView);
        TextView coordY = (TextView) rowView.findViewById(R.id.coordYView);

        BeaconInfo info = beaconData.get(position);

        uniqueId.setText("Unique ID: " + info.getUniqueId());
        place.setText("Place: " + info.getPlace());
        coordX.setText("coordX: " + info.getCoordX());
        coordY.setText("coordY: " + info.getCoordY());

        return rowView;
    }

}

