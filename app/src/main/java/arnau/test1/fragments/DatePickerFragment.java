package arnau.test1.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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



    //GET MISSING DATE FOR RESTORING SESSION FROM BUNDLEEEE()


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
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
        Intent i = new Intent(getActivity(), DatePickerFragment.class);
        i.putExtra("year", year);
        i.putExtra("month", month);
        i.putExtra("day", day);
        String datePicked = year + "-" + (month + 1) + "-" + day;
        listener.onFinishDatePickerDialog(datePicked);
    }
}