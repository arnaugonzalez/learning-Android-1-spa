package arnau.test1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import arnau.test1.APODdata;
import arnau.test1.MainActivity;
import arnau.test1.R;

public class ApodDialogFragment extends DialogFragment {

    TextView titleApodT, dateApodT, copyrightApodT, explanationApodT;
    ImageView urlApodIV;
    View mainViewAPOD;

    public static ApodDialogFragment newInstance(String title, String date,
                               String copyright, String explanation, String url) {

        ApodDialogFragment apodFrag = new ApodDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("date", date);
        args.putString("copyright", copyright);
        args.putString("explanation", explanation);
        args.putString("url", url);
        apodFrag.setArguments(args);

        return apodFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewAPOD = getActivity().getLayoutInflater().inflate(R.layout.layout_apod, null);
        titleApodT = mainViewAPOD.findViewById(R.id.title_APOD);
        dateApodT = mainViewAPOD.findViewById(R.id.date_APOD);
        explanationApodT = mainViewAPOD.findViewById(R.id.explanation_APOD);
        copyrightApodT = mainViewAPOD.findViewById(R.id.copyright_APOD);
        urlApodIV = mainViewAPOD.findViewById(R.id.url_APOD);

    }


    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        titleApodT.setText(getArguments().getString("title"));
        dateApodT.setText(getArguments().getString("date"));
        copyrightApodT.setText(getArguments().getString("copyright"));
        explanationApodT.setText(getArguments().getString("explanation"));
        Picasso.with(this.getContext())
                .load(getArguments().getString("url"))
                .error(R.drawable.blocker)
                .into(urlApodIV);
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_DialogWhenLarge);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.satellite)
                .setPositiveButton("replace",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Picasso.with(getContext()).load(getArguments().getString("url"))
                                        .error(R.drawable.blocker).into(((MainActivity)getActivity()).blocker);
                            }
                        })
                .setNegativeButton("date" , //provisional
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((MainActivity)getActivity()).showDatePickerDialog();
                            }
                        });
        builder.setView(mainViewAPOD);
        return builder.create();
    }
    private class MyTask extends AsyncTask<Void, Void, APODdata> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(APODdata apoDdata) {
            super.onPostExecute(apoDdata);
        }

        @Override
        protected APODdata doInBackground(Void... voids) {
            return null;
        }
    }
}