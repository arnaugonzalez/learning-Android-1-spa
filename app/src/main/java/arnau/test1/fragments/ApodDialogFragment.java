package arnau.test1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import arnau.test1.MainActivity;
import arnau.test1.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApodDialogFragment extends DialogFragment {


    @BindView(R.id.title_APOD) TextView titleApodT;
    @BindView(R.id.date_APOD) TextView dateApodT;
    @BindView(R.id.explanation_APOD) TextView explanationApodT;
    @BindView(R.id.copyright_APOD) TextView copyrightApodT;
    @BindView(R.id.url_APOD) ImageView urlApodIV;
    @BindView(R.id.lAPOD_mainRelative) View mainViewAPOD;

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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_DialogWhenLarge);
        ButterKnife.bind(this, mainViewAPOD);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.satellite)
                .setView(apodDialogView())
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
                        })
                .create();
        return dialog;
    }

        public View apodDialogView() {
            View view = getLayoutInflater().inflate(R.layout.layout_apod, null);
            titleApodT.setText(getArguments().getString("title"));
            dateApodT.setText(getArguments().getString("date"));
            copyrightApodT.setText(getArguments().getString("copyright"));
            explanationApodT.setText(getArguments().getString("explanation"));
            Picasso.with(this.getContext())
                    .load(getArguments().getString("url"))
                    .error(R.drawable.blocker)
                    .into(urlApodIV);
            return view;
        }


    }