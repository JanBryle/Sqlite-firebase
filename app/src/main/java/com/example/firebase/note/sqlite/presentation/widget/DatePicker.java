package com.example.firebase.note.sqlite.presentation.widget;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;
import java.util.Calendar;
public class DatePicker {
    public static void showDatePickerDialog(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year1, monthOfYear, dayOfMonth1) -> textView.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth1), year, month, dayOfMonth);
        datePickerDialog.show();
    }
}