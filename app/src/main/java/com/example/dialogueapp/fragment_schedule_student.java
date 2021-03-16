package com.example.dialogueapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class fragment_schedule_student extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_schedule_student, container, false);

        ImageButton clickScheduleBtn = (ImageButton)view.findViewById(R.id.btn_schedule_picker);


        // Using an onclick listener on the editText to show the datePicker
        clickScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment().newInstance();
                dpf.setCallBack(onDate);
                dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
            }
        });


        //----------------------------------------------------
        // Get open TimePickerDialog button.
        ImageButton timePickerDialogButton = (ImageButton)view.findViewById(R.id.btn_time_picker);
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
//                        strBuf.append("You select time is ");
                        strBuf.append(String.valueOf(hour));
                        strBuf.append(":");
                        strBuf.append(String.valueOf(minute));
//                        Log.d("Tagggg",strBuf.toString());

                        TextView timePickerValueTextView = (TextView)getActivity().findViewById(R.id.txt_picked_time);
                        timePickerValueTextView.setText(strBuf.toString());
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24Hour);

                timePickerDialog.setIcon(R.drawable.ic_history);
                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });



        return view;
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            TextView tvDate = (TextView) getActivity().findViewById(R.id.txt_picked_date);
            tvDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };


}