package arnau.test1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                                ((MainActivity)getActivity()).
                                        blocker.setImageBitmap(apodDEF.getUrlBitmap());
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