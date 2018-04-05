package arnau.test1;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

import arnau.test1.fragments.DatePickerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerFragment.DatePickerFragmentListener {

    int scoreV;
    ImageView trigger, blocker,
            urlApodIV;
    TextView scoreT, savedT,
            titleApodT, dateApodT, explanationApodT, copyrightApodT;
    Button resetB, saveB;
    SharedPreferences prefs;
    LinearLayout linV;
    Vibrator vibrator;
    Toolbar toolbar;
    Random random = new Random();
    String dateDF;
    Point p = new Point();
    View mainViewAPOD;
    AlertDialog.Builder builder;
    APODdata apodData = new APODdata();
    String[] failString = {
            "Houston, we have a problem...",
            "Oh no! You entered the gravitatory field of the Black Hole!",
            "Elon Musk won't be proud of that",
            "You have seen too much Interestellar",
            "*** Aliens chuckling on the background ***"
    };

    @SuppressLint({"DefaultLocale", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateOptionsMenu((Menu) toolbar);
        setContentView(R.layout.activity_main);
        linV = findViewById(R.id.containerV);
        trigger = linV.findViewById(R.id.trigger);
        blocker = linV.findViewById(R.id.blocker);
        scoreT = findViewById(R.id.scoreT);
        resetB = findViewById(R.id.startB);
        saveB = findViewById(R.id.saveB);
        savedT = findViewById(R.id.savedT);
        builder = new AlertDialog.Builder(MainActivity.this);
        toolbar = findViewById(R.id.toolbar1);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addScore_bar:
                        showDatePickerDialog();
                        return true;
                    case R.id.apodData_bar:
                        renewAPOD();
                        mainViewAPOD = getLayoutInflater().inflate(R.layout.layout_apod, null);
                        titleApodT = mainViewAPOD.findViewById(R.id.title_APOD);
                        dateApodT = mainViewAPOD.findViewById(R.id.date_APOD);
                        explanationApodT = mainViewAPOD.findViewById(R.id.explanation_APOD);
                        copyrightApodT = mainViewAPOD.findViewById(R.id.copyright_APOD);
                        urlApodIV = mainViewAPOD.findViewById(R.id.url_APOD);
                        builder.setView(mainViewAPOD);
                        builder.setPositiveButton(R.string.dialogPOSITIVE,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Picasso.with(getApplicationContext()).load(apodData.getUrl_apod())
                                        .error(R.drawable.blocker).into(blocker);
                            }
                        });
                        builder.create().show();
                        return true;
                    default:
                        return this.onMenuItemClick(item);
                }
            }
        });
        prefs = this.getPreferences(Context.MODE_PRIVATE);
        scoreV = prefs.getInt("pkScore", 0);
        scoreT.setText(String.format("%04d", scoreV));
        autoMove(trigger, 750);
        autoMove(blocker, 1200);
        trigger.setOnClickListener(this);
        blocker.setOnClickListener(this);
        resetB.setOnClickListener(this);
        saveB.setOnClickListener(this);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        getWindowManager().getDefaultDisplay().getSize(p);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //valorar si es pot afegir funcio gif mentre intenta fer apod

    public void autoMove(final ImageView image, final int delay){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                image.setX(random.nextInt(linV.getWidth() - image.getWidth()));
                image.setY(random.nextInt(linV.getHeight() - image.getHeight()));
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @SuppressLint("DefaultLocale")
    public void scoreUpdate(int inPoints) {
        if(scoreV + inPoints < 0) scoreV = 0;
        else scoreV += inPoints; // numbers received as integer, not abs val
        scoreT.setText(String.format("%04d", scoreV));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.trigger:
                    Rect rTrigger = new Rect(
                            (int) trigger.getX(),
                            (int) trigger.getY(),
                            (int) trigger.getX() + trigger.getWidth(),
                            (int) trigger.getY() + trigger.getHeight());
                    Rect rBlocker = new Rect(
                            (int) blocker.getX(),
                            (int) blocker.getY(),
                            (int) blocker.getX() + blocker.getWidth(),
                            (int) blocker.getY() + blocker.getHeight());
                    if(Rect.intersects(rTrigger,rBlocker)) {
                        scoreUpdate(-3);
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
                            vibrator.vibrate(500);
                        else vibrator.vibrate(VibrationEffect.createOneShot(500,
                                VibrationEffect.DEFAULT_AMPLITUDE));
                            Toast.makeText(getApplicationContext(),
                                failString[random.nextInt(5)], Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
                          //  vibrator.vibrate(100);
                       // else vibrator.vibrate(VibrationEffect.createOneShot(100,
                         //       VibrationEffect.DEFAULT_AMPLITUDE));
                        scoreUpdate(1);
                    }
                    // trigger.setClickable(false);
                    break;
                case R.id.blocker:
                    scoreUpdate(-1);
                    break;
                case R.id.startB:
                    scoreUpdate(-scoreV);
                    renewAPOD();
                    break;
                case R.id.saveB:
                    //prefs.edit().clear().apply();
                    int scoreB4save = prefs.getInt("pkScore", 0);
                    if(scoreB4save == scoreV)
                        Toast.makeText(getApplicationContext(), getString(R.string.equal) +
                                " " + scoreB4save + "!", Toast.LENGTH_SHORT).show();
                    else {
                        SharedPreferences.Editor prefsEditor = prefs.edit();
                        prefsEditor.putInt("pkScore", scoreV).apply();
                        Toast.makeText(getApplicationContext(), "Score updated from " +
                                scoreB4save + " to " + scoreV, Toast.LENGTH_SHORT).show();
                        savedT.setText(getString(R.string.savedNEW) + " " + scoreV);
                        prefsEditor.putString("pkSaved", savedT.toString()).apply();
                    }
                    break;
            }
    }

    public void showDatePickerDialog(){
        DialogFragment newDFragment = new DatePickerFragment();
        newDFragment.show(this.getFragmentManager(), "datePicker");
    }

    public void renewAPOD() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApodInterface.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApodInterface apodI = retrofit.create(ApodInterface.class);
        Call<APODdata> call = apodI.getAPOD(dateDF);
        call.enqueue(new Callback<APODdata>() {
            @Override
            public void onResponse(Call<APODdata> call, retrofit2.Response<APODdata> response) {
                if(response.isSuccessful()){
                    apodData = response.body();
                    titleApodT.setText(apodData.getTitle_apod());
                    dateApodT.setText(apodData.getDate_apod());
                    explanationApodT.setText(apodData.getExplanation_apod());
                    copyrightApodT.setText(apodData.getCopyright_apod());
                    Picasso.with(getApplicationContext())
                            .load(apodData.getUrl_apod())
                            .error(R.drawable.blocker)
                            .into(urlApodIV);
                }
                else {
                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<APODdata> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Retrofit Error :( so sad", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFinishDatePickerDialog(String inputText)   {
        dateDF = inputText;
    }
}