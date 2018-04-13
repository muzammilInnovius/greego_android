package com.greegoapp.Model;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import static android.support.v4.util.Preconditions.checkNotNull;


    public class TripHistoryModel {
        private String date;

        @SuppressLint("RestrictedApi")
        public TripHistoryModel(@NonNull String date) {
            date = checkNotNull(date);

        }

        public  String getDate() {
            return date;
        }

        public String setDate(String date) {
            this.date = date;
            return date;
        }

        public TripHistoryModel() {
        }
    }
