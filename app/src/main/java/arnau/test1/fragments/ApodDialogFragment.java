package arnau.test1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import arnau.test1.APODdata;
import arnau.test1.APODdataDEF;
import arnau.test1.MainActivity;

import arnau.test1.R;

public class ApodDialogFragment extends DialogFragment {

    private TextView titleApodT, dateApodT, copyrightApodT, explanationApodT;
    private ImageView urlApodIV;
    private View mainViewAPOD;
    private static APODdataDEF apodDEF;

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
        if(apodDEF.getParsedAPOD().getMedia_type().equals("image"))
            urlApodIV = mainViewAPOD.findViewById(R.id.image_APOD);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(apodDEF.getParsedAPOD().getMedia_type().equals("image"))
            urlApodIV.setImageBitmap(apodDEF.getUrlBitmap());
        titleApodT.setText(apodDEF.getParsedAPOD().getTitle_apod());
        dateApodT.setText(apodDEF.getParsedAPOD().getDate_apod());
        copyrightApodT.setText(apodDEF.getParsedAPOD().getCopyright_apod());
        explanationApodT.setText(apodDEF.getParsedAPOD().getExplanation_apod());
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_DialogWhenLarge);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.satellite)
                .setPositiveButton(R.string.dialogPOSITIVE,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((MainActivity)getActivity()).
                                        blocker.setImageBitmap(apodDEF.getUrlBitmap());
                                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("changed", 1).apply();
                                editor.commit();

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