package arnau.test1.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.DatePicker;

import java.util.Calendar;

import arnau.test1.MainActivity;
import arnau.test1.R;

/**
 * Created by arnau on 05-Mar-18.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
        int year, month, day;

    public static DatePickerFragment newInstance(int year, int month, int day) {

        DatePickerFragment dater = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("day", day);
        args.putInt("month", month);
        dater.setArguments(args);
        return dater;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
                if(getArguments().getInt("year") == 0) {
                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            this, year, month, day);
                    datePickerDialog.setTitle(R.string.dateDialogT);
                    return datePickerDialog;
                }
                else {
                    year = getArguments().getInt("year");
                    month = getArguments().getInt("month");
                    day = getArguments().getInt("day");
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            this, year, month, day);
                    datePickerDialog.setTitle(R.string.dateDialogT);
                    return datePickerDialog;
                }
    }

    /*public void actualizar (int year1, int month1, int day1) {
        if (year1 != 0) {
            year = year1;
            day = day1;
            month = month1;
            dater = new DatePickerDialog(getActivity(),
                    this, year, month, day);
            dater.setTitle(R.string.dateDialogT);
        }
    }*/

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
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

    public void onDateSet(DatePicker view, int year1, int month1, int day1) {
        Intent i = new Intent(getActivity(), DatePickerFragment.class);
        i.putExtra("year", year1);
        i.putExtra("month", month1);
        i.putExtra("day", day1);
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("year",year1).apply();
        editor.putInt("month",month1).apply();
        editor.putInt("day",day1).apply();
        editor.commit();
        String datePicked = year1 + "-" + (month1 + 1) + "-" + day1;
        listener.onFinishDatePickerDialog(datePicked);

    }
}