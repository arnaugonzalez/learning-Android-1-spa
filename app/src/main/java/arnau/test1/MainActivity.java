package arnau.test1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import arnau.test1.fragments.ApodDialogFragment;
import arnau.test1.fragments.DatePickerFragment;
import arnau.test1.interfaces.ApodInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        DatePickerFragment.DatePickerFragmentListener {

    //BINDINGS
    @BindView(R.id.trigger) ImageView trigger;
    public @BindView(R.id.blocker) ImageView blocker;
    @BindView(R.id.containerV) LinearLayout linV;
    @BindView(R.id.scoreT) TextView scoreT;
    @BindView(R.id.savedT) TextView savedT;
    @BindView(R.id.resetGame) Button resetGame;
    @BindView(R.id.saveGame) Button saveGame;

    @BindView(R.id.activityMainLayout) RelativeLayout mainLayout;
    @BindView(R.id.toolbar1) Toolbar toolbar;

    //DECLARATIONS
    ApodDialogFragment apodDialogFrag;
    int scoreV;
    SharedPreferences prefs;
    Vibrator vibrator;
    Random random = new Random();
    String dateDF;
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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.setSupportActionBar(toolbar);
        prefs = this.getPreferences(Context.MODE_PRIVATE);
        scoreV = prefs.getInt("pkScore", 0);
        scoreT.setText(String.format("%04d", scoreV));
        autoMove(trigger, 750);
        autoMove(blocker, 1200);
        vibrator = (Vibrator) getBaseContext().getSystemService(VIBRATOR_SERVICE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

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


    //BUTTER KNIFE ONCLICK
    @OnClick(R.id.trigger)
    public void trigger() {
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
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                vibrator.vibrate(500);
            else vibrator.vibrate(VibrationEffect.createOneShot(500,
                    VibrationEffect.DEFAULT_AMPLITUDE));
            Toast.makeText(getApplicationContext(),
                    failString[random.nextInt(5)], Toast.LENGTH_SHORT).show();
        }
        else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                vibrator.vibrate(100);
            else vibrator.vibrate(VibrationEffect.createOneShot(100,
                    VibrationEffect.DEFAULT_AMPLITUDE));
            scoreUpdate(1);
        }
        // trigger.setClickable(false);
    }

    @OnClick(R.id.blocker)
    public void blocker(){
        scoreUpdate(-1);
    }

    @OnClick(R.id.resetGame)
    public void resetGame(){
        scoreUpdate(-scoreV);
    }

    @OnClick(R.id.saveGame)
    public void saveGame(){
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
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.datePicker:
                showDatePickerDialog();
                return true;
            case R.id.apodDialog:
                new TaskAPOD().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showDatePickerDialog(){
        DatePickerFragment newDFragment = new DatePickerFragment();
        newDFragment.show(getFragmentManager(), "datePicker");
    }

    public APODdata renewAPOD() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApodInterface.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApodInterface apodI = retrofit.create(ApodInterface.class);
        Call<APODdata> call = apodI.getAPOD(dateDF);
        Response<APODdata> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }
        else {
            Log.e("Error Code", String.valueOf(response.code()));
            Log.e("Error Body", response.body().toString());
            return null;
        }
    }

    public class TaskAPOD extends AsyncTask<Void, Void, APODdataDEF> {
        ProgressDialog progressDialog;
        APODdata dataTask;
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog  = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();

        }

        @Override
        protected APODdataDEF doInBackground(Void... voids) {
            try {
                dataTask = renewAPOD();
            } catch (IOException e) { e.printStackTrace(); }
            try {
                URL urlB = new URL(dataTask.getUrl_apod());
                bitmap = BitmapFactory.decodeStream(urlB.openConnection().getInputStream());
            } catch (IOException e) { e.printStackTrace(); }
            return APODdataDEF.newDefinitiveAPOD(dataTask, bitmap);
        }

        @Override
        protected void onPostExecute(APODdataDEF apodData) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            apodDialogFrag = ApodDialogFragment.newInstance(apodData);
            apodDialogFrag.show(getSupportFragmentManager(),"dialog");
        }
    }
    @Override
    public void onFinishDatePickerDialog(String inputText)   {
        dateDF = inputText;
    }
}