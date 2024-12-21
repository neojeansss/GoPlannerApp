package com.example.goplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PanelNoList extends Fragment {

    private String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedDate = getArguments().getString("DATE", "No Date Selected");
        } else {
            selectedDate = "No Date Selected12234";
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_no_list, container, false);
        TextView panelDateTV = view.findViewById(R.id.panelDateTV);
        if (getArguments() != null) {
            String selectedDate = getArguments().getString("SELECTED_DATE");
            if (selectedDate != null) {
                panelDateTV.setText(formatDate(selectedDate));

            }
        }
        return view;
    }

    private String formatDate(String selectedDate) {
        try {
            SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            originalDateFormat.setLenient(false);
            Date parsedDate = originalDateFormat.parse(selectedDate);

            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return desiredDateFormat.format(parsedDate);
        } catch (ParseException | IllegalArgumentException e) {
            Log.w("PanelList", "Invalid date format: " + selectedDate, e);
            return "Invalid Date";
 }
}

}