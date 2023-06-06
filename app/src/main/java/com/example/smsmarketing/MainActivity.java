package com.example.smsmarketing;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

public class MainActivity extends Activity {

    // declarartion des variables

    Intent mServiceIntent;
    private YourService mYourService;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String id;
    String object1;
    SharedPreferences sharedpreferences;
    String url;
    String status;
    String numero;
    String message;
    boolean stopExecution;
    TextView textview1;
    TextView textview2;
    TextView textView3;
    TextView textview4;
    LottieAnimationView animationView1;
    LottieAnimationView animationView2;
    LottieAnimationView animationView3;
    LottieAnimationView animationView4;
    ImageView simblocked ;

    //declaration des methodes


    public String getStatus(String status) {
        return status;
    }

    public String getObject1(String object1) {
        return object1;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void onCreate(Bundle savedInstanceState) {


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);

        }

        // les permissions

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS))
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "Manifest.permission.READ_SMS") ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, "Manifest.permission.READ_SMS")) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{"Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS"}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {

        }

        // les variables

        textview1 = (TextView) findViewById(R.id.textview1);
        textview2 = (TextView) findViewById(R.id.textview2);
        textView3 = (TextView) findViewById(R.id.textview3);
        textview4 = (TextView) findViewById(R.id.textview4);
        animationView1 = (LottieAnimationView) findViewById(R.id.animation_view1);
        animationView2 = (LottieAnimationView) findViewById(R.id.animation_view2);
        animationView3 = (LottieAnimationView) findViewById(R.id.animation_view3);
        animationView4 = (LottieAnimationView) findViewById(R.id.animation_view4);
        simblocked =(ImageView)findViewById(R.id.simblocked);

        // les methodes

        Object networkstatut = getNetworkState();
        Object modeavionstatut = getmodeavion();
        Object wifistatut = getwifistatut();
        Object cardsimstatut = getcardsimstatut();

        // création de button power

        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        imgView.setTag(1);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int) imgView.getTag() == 1) {
                    stopExecution = false;
                    Toast.makeText(getBaseContext(), "on", Toast.LENGTH_SHORT).show();
                    imgView.setImageResource(R.drawable.on);
                    imgView.setTag(2);
                } else {
                    stopExecution = true;
                    Toast.makeText(getBaseContext(), "off", Toast.LENGTH_SHORT).show();
                    imgView.setImageResource(R.drawable.off);
                    imgView.setTag(1);
                }
            }
        });

        // appel de methode dotheAutoRefresh

        doTheAutoRefresh();

        // tester la condition

        if(networkstatut.equals(true)&&modeavionstatut.equals(true)&&wifistatut.equals(true)&&cardsimstatut.equals(true)&&stopExecution==false){

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    new sms().execute(url);
                }
            }, 0, 20, TimeUnit.SECONDS);


        }
        else{
            Toast.makeText(getBaseContext(), "Vérifier vos paramétres", Toast.LENGTH_SHORT).show();
        }

    }


  // création de la methode dotheAutoRefresh

    private final Handler handler = new Handler();

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Object networkstatut = getNetworkState();
                Object modeavionstatut = getmodeavion();
                Object wifistatut = getwifistatut();
                Object cardsimstatut = getcardsimstatut();


                if (networkstatut.equals(true)){
                    textview1.setText("Network existe");
                    animationView2.setAnimation("thenetwork.json");
                    animationView2.playAnimation();
                    animationView2.loop(true);
                }
                else{
                    textview1.setText("Network n'existe pas");
                    animationView2.setAnimation("nonetwork.json");
                    animationView2.playAnimation();
                    animationView2.loop(true);
                }

                if (modeavionstatut.equals(true)){

                    textview2.setText("Mode avion off");
                    animationView4.setAnimation("modeoff.json");
                    animationView4.playAnimation();
                    animationView4.loop(true);
                }
                else{
                    textview2.setText("Mode avion on");
                    animationView4.setAnimation("modeonair.json");
                    animationView4.playAnimation();
                    animationView4.loop(true);

                }
                if (wifistatut.equals(true)){

                    animationView1.setAnimation("wificonnect.json");
                    animationView1.playAnimation();
                    animationView1.loop(true);
                    textView3.setText("connexion existe");
                }
                else{
                    animationView1.setAnimation("no-connection.json");
                    animationView1.playAnimation();
                    animationView1.loop(true);
                    textView3.setText("vérifier votre connexion");

                }

                if(cardsimstatut.equals(true)){
                    simblocked.setVisibility(View.INVISIBLE);
                    animationView3.setAnimation("sim.json");
                    animationView3.playAnimation();
                    animationView3.loop(true);
                }
                else{
                    simblocked.setVisibility(animationView4.INVISIBLE);
                    simblocked.setVisibility(View.VISIBLE);
                    simblocked.setImageResource(R.drawable.blockedsim);
                }


                doTheAutoRefresh();
            }
        }, 3000);
    }


    //network function

    public boolean getNetworkState() {
        boolean network = false;
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = connectivity.getAllNetworkInfo();
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].isAvailable()) {
                network = true;
            }
        }

        if (network){

            return true;
        }
        else{
            return false ;
        }

    }

    //modeavion function

    public boolean getmodeavion() {

        if (Settings.System.getInt(MainActivity.this.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {

            return true;
        } else {
            return false;
        }
    }

    //wifistatut function

    public boolean getwifistatut() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();


        if (is3g || isWifi) {

            return true;

        } else {

            return false;

        }
    }

    //cardsimstatut function

    public boolean getcardsimstatut() {

        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();

        if (simState == TelephonyManager.SIM_STATE_ABSENT){
            textview4.setText("carte SIM n'existe pas");
            return false;
        }
        else if(simState ==TelephonyManager.SIM_STATE_NETWORK_LOCKED) {
            textview4.setText("Nécessite un code PIN réseau pour déverrouiller ");
            return false;
        }
        else if(simState == TelephonyManager.SIM_STATE_PIN_REQUIRED) {
            textview4.setText("Nécessite un code PIN de la carte SIM pour déverrouiller");
            return false;
        }
        else if(simState == TelephonyManager.SIM_STATE_READY ) {
            textview4.setText("Carte SIM existe");
            return true;
        }
        else {
            textview4.setText("Erreur inconnue");
            return false ;
        }

    }

    // service function

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }


    // onDestroy function

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        url = "https://sms-testing.tus-appli.com/getsms.php";


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {


                new sms().execute(url);

            }
        }, 0, 20, TimeUnit.SECONDS);
    }


    //SMS function


    public class sms extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String s = "https://sms-testing.tus-appli.com/getsms.php";
            String content = httpULRConnect.getData(s);
            return content;

        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String s) {
            Object networkstatut = getNetworkState();
            Object modeavionstatut = getmodeavion();
            Object wifistatut = getwifistatut();
            Object cardsimstatut = getcardsimstatut();
            JSONObject object = null;

            if(networkstatut.equals(true)&&modeavionstatut.equals(true)&&wifistatut.equals(true)&&cardsimstatut.equals(true)){

                try {
                    object = new JSONObject(s);


                    JSONArray jArray = object.getJSONArray("message");
                    id = jArray.getString(0);
                    SharedPreferences.Editor myEdit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    myEdit.putString("id", id);
                    myEdit.apply();

                    numero = jArray.getString(1);
                    message = jArray.getString(2);
                    System.out.println("id " + id);
                    System.out.println("numero " + numero);
                    System.out.println("message " + message);

                        if (stopExecution == false) {

                            MultipleSMS(numero, message);

                        } else {
                            System.out.println("yyyy");
                            return;
                        }


                    }

                 catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else{
                Toast.makeText(getBaseContext(), "vérifier vos paramétres", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //sendSMS function

    private void MultipleSMS(String phoneNo, String message) {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(DELIVERED), 0);


        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:


                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("address", phoneNo);
                        values.put("body", message);
                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);

                        status = "1";
                        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        object1 = sharedpreferences.getString("id", "id");
                        updateClass.getid(object1);
                        updateClass.getstatus(status);
                        new updateClass().execute(object1, status);

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();

                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNo, null, message, sentPI, deliveredPI);
    }
}










