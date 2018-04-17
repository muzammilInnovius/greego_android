package com.greegoapp.Interface;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Android on 26-Feb-2018.
 */

public interface PickDropLocation {
    void onClick(LatLng sourceLatlng, LatLng destLatlng);
}
