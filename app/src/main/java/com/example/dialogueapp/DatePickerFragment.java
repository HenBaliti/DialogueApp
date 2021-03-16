package com.example.dialogueapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener onDateSet;
    private boolean isModal = false;

    public static DatePickerFragment newInstance()
    {
        DatePickerFragment frag = new DatePickerFragment();
        frag.isModal = true; // WHEN FRAGMENT IS CALLED AS A DIALOG SET FLAG
        return frag;
    }

    public DatePickerFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(isModal) // AVOID REQUEST FEATURE CRASH
        {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        else {
            View rootView = inflater.inflate(R.layout.fragment_schedule_student, container, false);
            return rootView;
        }
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener onDate) {
        onDateSet = onDate;
    }

}