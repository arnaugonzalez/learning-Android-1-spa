package arnau.test1.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import arnau.test1.MainActivity;
import arnau.test1.R;

/**
 * Created by arnau on 05-Mar-18.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static DatePickerFragment newInstance(int year, int month, int day) {

        DatePickerFragment datePicker = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("day", day);
        args.putInt("month", month);
        datePicker.setArguments(args);
        return datePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = getArguments().getInt("year");
        int month =getArguments().getInt("month");
        int day = getArguments().getInt("day");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                this, year, month, day);
        datePickerDialog.setTitle(R.string.dateDialogT);
        return datePickerDialog;
    }

    public interface DatePickerFragmentListener {
        void onFinishDatePickerDialog(String inputText);
    }
    private DatePickerFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DatePickerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DatePickerFragmentListener");
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", day);
        editor.apply();
        String datePicked = year + "-" + (month + 1) + "-" + day;
        listener.onFinishDatePickerDialog(datePicked);
    }
}