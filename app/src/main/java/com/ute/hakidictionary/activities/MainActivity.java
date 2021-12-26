package com.ute.hakidictionary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.ute.hakidictionary.R;
import com.ute.hakidictionary.fragment.HomeFragment;
import com.ute.hakidictionary.fragment.NotificationFragment;
import com.ute.hakidictionary.fragment.UserFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "";
    //Button btnPush;
    //TextView txtTimer;
    String title, content, imageUrl, createdTime;
    ArrayList<String> arrayTitle, arrayContent, arrayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        bottomNavigation = findViewById(R.id.bottom_navigation);
        //bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.nav_history));
        //bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.nav_love));
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.nav_noti));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.nav_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.nav_person));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()){
                    case 1:
                        fragment = new NotificationFragment();
                        break;
                    case 2:
                        fragment = new HomeFragment();
                        break;
                    case 3:
                        fragment = new UserFragment();
                        break;
//                    case 4:
//                        fragment = new NotificationFragment();
//                        break;
//                    case 5:
//                        fragment = new UserFragment();
//                        break;
                }
                loadFragment(fragment);
            }
        });
        loadFragment(new HomeFragment());
        bottomNavigation.show(2,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
        timer();
    }
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Header,fragment)
                .commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
    public void timer(){
        //long duration = TimeUnit.MINUTES.toMillis(5);
        long duration = TimeUnit.SECONDS.toMillis(120);
        new CountDownTimer(duration,1){
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(l)
                        ,TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
            }

            @Override
            public void onFinish() {
                randomData();
                pushNotification();
                addData();
            }
        }.start();
    }

    public void pushNotification(){
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(), PendingIntent.FLAG_UPDATE_CURRENT);

        collapsedView.setTextViewText(R.id.text_view_collapsed_1, title);
        collapsedView.setTextViewText(R.id.text_view_collapsed_2, content);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_noti)
                .setCustomContentView(collapsedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(resultPendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(NOTIFICATION_ID, notification);
            //Toast.makeText(MainActivity.this, "oke", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("hang", "pushNotification: " + notification);
        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    public void addData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("content", content);
            jsonObject.put("imageUrl", imageUrl);
            jsonObject.put("createdTime", createdTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://ec2-3-144-36-186.us-east-2.compute.amazonaws.com:3030/api/announcement/createAnnouncement")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void randomData(){
        //Get random list title, list content, git imageURL
        String[] listTitle = getResources().getStringArray(R.array.listTitle);
        String[] listContent = getResources().getStringArray(R.array.listContent);
        String[] listImage = getResources().getStringArray(R.array.listImage);
        arrayTitle = new ArrayList<>(Arrays.asList(listTitle));
        arrayContent = new ArrayList<>(Arrays.asList(listContent));
        arrayImage = new ArrayList<>(Arrays.asList(listImage));

        Collections.shuffle(arrayTitle);
        Collections.shuffle(arrayContent);
        Collections.shuffle(arrayImage);

        title = arrayTitle.get(1);
        content = arrayContent.get(1);
        imageUrl = arrayImage.get(1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        createdTime = df.format(Calendar.getInstance().getTime());
        //Toast.makeText(MainActivity.this, createdTime.toString(), Toast.LENGTH_SHORT).show();
    }
}