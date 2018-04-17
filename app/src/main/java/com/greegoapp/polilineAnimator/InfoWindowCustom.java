package com.greegoapp.polilineAnimator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.greegoapp.R;

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {
    Context context;
    LayoutInflater inflater;

    public InfoWindowCustom(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // R.layout.echo_info_window is a layout in my
        // res/layout folder. You can provide your own
        View v = inflater.inflate(R.layout.echo_info_window, null);

        TextView title = v.findViewById(R.id.info_window_title);
        title.setText(marker.getTitle());
        return v;
    }
}