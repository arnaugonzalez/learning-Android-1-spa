package arnau.test1.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import arnau.test1.APODdata;
import arnau.test1.APODdataDEF;
import arnau.test1.MainActivity;
import arnau.test1.R;

public class ApodDialogFragment extends DialogFragment {

    TextView titleApodT, dateApodT, copyrightApodT, explanationApodT;
    ImageView urlApodIV;
    View mainViewAPOD;
    static APODdataDEF apodDEF;

    public static ApodDialogFragment newInstance(APODdataDEF parsedApodDEF) {

        ApodDialogFragment apodFrag = new ApodDialogFragment();
        apodDEF = parsedApodDEF;
        Bundle args = new Bundle();
        args.putString("title", parsedApodDEF.getTitle_apod());
        args.putString("date", parsedApodDEF.getDate_apod());
        args.putString("copyright", parsedApodDEF.getCopyright_apod());
        args.putString("explanation", parsedApodDEF.getExplanation_apod());
        //args.putString("url", parsedApodDEF.getUrl_apod());
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


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* Picasso.with(this.getContext())
                .load(getArguments().getString("url"))
                .error(R.drawable.blocker)
                .into(urlApodIV); */
        //Caravaggio(getArguments().getString("url"));
        urlApodIV.setImageBitmap(apodDEF.getUrlBitmap());
        titleApodT.setText(apodDEF.getParsedAPOD().getTitle_apod());
        dateApodT.setText(apodDEF.getParsedAPOD().getDate_apod());
        copyrightApodT.setText(apodDEF.getParsedAPOD().getCopyright_apod());
        explanationApodT.setText(apodDEF.getParsedAPOD().getExplanation_apod());
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_DialogWhenLarge);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.satellite)
                .setPositiveButton("replace",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Picasso.with(getContext()).load(getArguments().getString("url"))
                                        .error(R.drawable.blocker)
                                        .into(((MainActivity) getActivity()).blocker);
                            }
                        })
                .setNegativeButton("date", //provisional
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                ((MainActivity) getActivity()).showDatePickerDialog();
                            }
                        });
        builder.setView(mainViewAPOD);
        return builder.create();
    }
}