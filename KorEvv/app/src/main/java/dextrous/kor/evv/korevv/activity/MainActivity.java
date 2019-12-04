package dextrous.kor.evv.korevv.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.DrawerAdapter;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.constants.CityState;
import dextrous.kor.evv.korevv.fragment.BillingFragment;
import dextrous.kor.evv.korevv.fragment.DocumentFragment;
import dextrous.kor.evv.korevv.fragment.HelpDesk;
import dextrous.kor.evv.korevv.fragment.HomeFragment;
import dextrous.kor.evv.korevv.fragment.SettingsFragment;
import dextrous.kor.evv.korevv.model.DrawerItem;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.Config;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.ForceUpdateChecker;
import dextrous.kor.evv.korevv.util.LocationTrack;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.NotificationUtils;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class MainActivity extends AppCompatActivity implements InstallStateUpdatedListener, ForceUpdateChecker.OnUpdateNeededListener {
    private boolean flagIsSliderOpen = false;
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    public static int activieFragment = 1;
    private PopupWindow popupWin;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerAdapter mDrawerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<DrawerItem> mDrawerItemList;
    public static TextView main_title;
    public static ImageView notification_button;
    public static ImageView button_search;
    ArrayList<String> category_id_list = new ArrayList<>();
    ArrayList<String> category_name_list = new ArrayList<>();
    ParseJasonLang parseJasonLang;
    private CustomProgressDialog dialog;
    private DrawerLayout mDrawerLayout;
    ImageView nav_button;
    int MY_REQUEST_CODE = 100;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    final List<String> animalsList = new ArrayList();
    double latitude, longitude;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack appLocationService;

    LinearLayout nav_button_ll;
    // LoginManager mLoginManager;
    String key;
    public static TextView noti_count;
    LoggedInUser loggedInUser;
    private ActionBarDrawerToggle mDrawerToggle;
    public static boolean isConnected = true;
    DatabaseHandler db;
    String id = "";
    RelativeLayout notification_layout;
    private static final String TAG = SplaseActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            isConnected = bundle.getBoolean("isConnected");
            isDoc = bundle.getBoolean("isdoc");
        }
        mToolbar = findViewById(R.id.toolbar);
        db = new DatabaseHandler(MainActivity.this);
        notification_layout = findViewById(R.id.notification_layout);

        loggedInUser = new LoggedInUser(getApplicationContext());

        if (AppUtill.isNetworkAvil(MainActivity.this)) {
            ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        parseJasonLang = new ParseJasonLang(MainActivity.this);

        popupWin = new PopupWindow(this);
        setSupportActionBar(mToolbar);
        noti_count = (TextView) findViewById(R.id.noti_count);
        nav_button = (ImageView) findViewById(R.id.nav_button);
        nav_button_ll = (LinearLayout) findViewById(R.id.nav_button_ll);
        notification_button = (ImageView) findViewById(R.id.notification_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_title = (TextView) findViewById(R.id.main_title);
        button_search = (ImageView) findViewById(R.id.button_search);
        parseJasonLang = new ParseJasonLang(MainActivity.this);

        popupWin = new PopupWindow(this);
        setSupportActionBar(mToolbar);
        noti_count = (TextView) findViewById(R.id.noti_count);

        nav_button = (ImageView) findViewById(R.id.nav_button);
        nav_button_ll = (LinearLayout) findViewById(R.id.nav_button_ll);
        notification_button = (ImageView) findViewById(R.id.notification_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_title = (TextView) findViewById(R.id.main_title);
        button_search = (ImageView) findViewById(R.id.button_search);
        if (!isDoc) {
            if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "0").size() > 0) {
                Log.v("isCheckin", "present");
                if (isConnected) {

                    Log.v("isCheckin", "present + connect");
                    if (loggedInUser.getLocal_language().equals("")) {
                        getLanguageResponse("en");
                    } else {
                        getLanguageResponse(loggedInUser.getLocal_language());
                    }

                    id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "0").get(0).getSch_id();
                    String parts[] = db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getCheckin_location().split(",");
                    String client_lat = parts[0];
                    String client_long = parts[1];
                    postClockIn(db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getCheckin_date(), db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getCheckin_time(),
                            client_lat, client_long, 0);
                    if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                        for (int i = 0; i < db.getAllCheckOut(loggedInUser.getLocal_user_id()).size(); i++) {

                            getClockOutApi(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCaregiver_sign(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getScheduleId(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign_name(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_date(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_time(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_latitude(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_longitude(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getAdditional_comments(),

                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckOutReason(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareTime(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareTime(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareActivities(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareActivities(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getTotalUnit());

                        }
                    }
                } else {
                    Log.v("isCheckin", "present + disconnect");

                    id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "0").get(0).getSch_id();
                    String parts[] = db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getCheckin_location().split(",");
                    String client_lat = parts[0];
                    String client_long = parts[1];
                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                    intent.putExtra("location", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLocation());
                    intent.putExtra("id", id);
                    intent.putExtra("time", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getStart_time() + " To " + db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getEnd_time());
                    intent.putExtra("name", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getName());
                    intent.putExtra("phone", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getCall());
                    intent.putExtra("pic", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getPic());
                    intent.putExtra("client_id", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getClientId());
                    intent.putExtra("additional_notes", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getAdditional_note());
                    intent.putExtra("direction", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLat() + "," + db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLongi());
                    startActivity(intent);

                }

            } else {
                Log.v("isCheckin", "not_present + connect");
                parseJasonLang = new ParseJasonLang(MainActivity.this);

                popupWin = new PopupWindow(this);
                setSupportActionBar(mToolbar);
                noti_count = (TextView) findViewById(R.id.noti_count);
                nav_button = (ImageView) findViewById(R.id.nav_button);
                nav_button_ll = (LinearLayout) findViewById(R.id.nav_button_ll);
                notification_button = (ImageView) findViewById(R.id.notification_button);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                main_title = (TextView) findViewById(R.id.main_title);
                button_search = (ImageView) findViewById(R.id.button_search);
                if (isConnected) {
                    Log.v("isCheckin", "not present + connect");
                    if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size() > 0) {
                        for (int j = 0; j < db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size(); j++) {
                            String sch_id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(j).getSch_id();
                            String[] parts = db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_location().split(",");
                            String client_lat = parts[0];
                            String client_long = parts[1];
                            postClockIn1(sch_id, db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_date(), db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_time(),
                                    client_lat, client_long, 0);
                        }


                    }
                    if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                        for (int i = 0; i < db.getAllCheckOut(loggedInUser.getLocal_user_id()).size(); i++) {

                            getClockOutApi1(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCaregiver_sign(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_yn(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_note(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getScheduleId(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign_name(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_date(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_time(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_latitude(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_longitude(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getAdditional_comments(),

                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckOutReason(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareTime(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareTime(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareActivities(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareActivities(),
                                    db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getTotalUnit());
                        }

                    } else {
                        Fragment fragment = new HomeFragment();
                        ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    }


                } else {
                    Fragment fragment = new HomeFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                }


            }
        }else {
            Fragment fragment = new DocumentFragment();
            ProjectUtil.replaceFragment(MainActivity.this, fragment);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //   txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
        setDrawerGuest();
        notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        //db.addRespiteTimeValue();


    }

    private void displayFirebaseRegId() {
        String newToken = "";
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("regId", newToken);
                editor.commit();
            }
        });


        String regId = pref.getString("regId", null);
        postFireBaseIdToServer(regId);

        Log.e(TAG, "Firebase reg id: " + regId);
//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm =
                (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        setDrawerGuest();

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
          /*  if (loggedInUser.getLocal_language().equals("")) {
                getService_category("en");
            } else {
                getService_category(loggedInUser.getLocal_language());
            }*/
            hitApi();

        }
      /*  Fragment fragment = new HomeFragment();
        ProjectUtil.replaceFragment(MainActivity.this, fragment);*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    void setDrawerGuest() {
        mDrawerItemList = new ArrayList<DrawerItem>();
//        DrawerItem item = new DrawerItem();
//        item.setIcon(R.drawable.home);
//        item.setTitle("Home");
//        mDrawerItemList.add(item);

        DrawerItem item2 = new DrawerItem();
        item2.setIcon(R.drawable.schedule_drawer);
        item2.setTitle(parseJasonLang.getJsonToString("Schedule"));
        mDrawerItemList.add(item2);


        DrawerItem item8 = new DrawerItem();
        item8.setIcon(R.drawable.billing);
        item8.setTitle(parseJasonLang.getJsonToString("completed_summary"));
        mDrawerItemList.add(item8);

        DrawerItem item9 = new DrawerItem();
        item9.setIcon(R.drawable.settings);
        item9.setTitle(parseJasonLang.getJsonToString("Setting"));
        mDrawerItemList.add(item9);

        DrawerItem item3 = new DrawerItem();
        item3.setIcon(R.drawable.help_desk_icon);
        item3.setTitle(parseJasonLang.getJsonToString("help_desk"));
        mDrawerItemList.add(item3);

        DrawerItem item11 = new DrawerItem();
        item11.setIcon(R.drawable.file_icon);
        item11.setTitle(parseJasonLang.getJsonToString("documents"));
        mDrawerItemList.add(item11);

        DrawerItem item10 = new DrawerItem();
        item10.setIcon(R.drawable.logout);
        item10.setTitle(parseJasonLang.getJsonToString("Logout"));
        mDrawerItemList.add(item10);


        mRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);

        DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList, MainActivity.this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                flagIsSliderOpen = true;
            }
        });
        nav_button_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                flagIsSliderOpen = true;
            }
        });


        adapter.setOnItemClickLister(new DrawerAdapter.OnItemSelecteListener() {
            @Override
            public void onItemSelected(View v, int position) {
                if (position == 1) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    ConnectivityManager cm =
                            (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size() > 0) {
                            Log.v("isCheckin", "not present +" + String.valueOf(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size()));
                            for (int j = 0; j < db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size(); j++) {
                                String sch_id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(j).getSch_id();
                                String[] parts = db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_location().split(",");
                                String client_lat = parts[0];
                                String client_long = parts[1];
                                postClockIn1(sch_id, db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_date(), db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_time(),
                                        client_lat, client_long, 0);
                            }


                        }
                        if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                            for (int i = 0; i < db.getAllCheckOut(loggedInUser.getLocal_user_id()).size(); i++) {

                                getClockOutApi1(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCaregiver_sign(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_yn(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_yn(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_yn(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_yn(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_note(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_note(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_note(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_note(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getScheduleId(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign_name(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_date(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_time(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_latitude(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_longitude(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getAdditional_comments(),

                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckOutReason(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareTime(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareTime(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareActivities(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareActivities(),
                                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getTotalUnit());

                            }

                        }
                    }
                    Fragment fragment = new HomeFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    notification_layout.setVisibility(View.VISIBLE);

                }

//                if(position==1){
//                    flagIsSliderOpen=false;
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
////                    Fragment fragment= new ViewServiceFragment();
////                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
//                   // Intent intent = new Intent(getApplicationContext(),ViewServiceActivity.class);
//                    //intent.putExtra("key",key);
//                   // startActivity(intent);
//
//                }


//                if(position==3){
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
//                    MyToast.showToast(getApplicationContext(),"Coming Soon");
//
//                }
                if (position == 2) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new BillingFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    notification_layout.setVisibility(View.VISIBLE);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }

                if (position == 3) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new SettingsFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    notification_layout.setVisibility(View.VISIBLE);

//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);                    notification_layout.setVisibility(View.GONE);

                }

                if (position == 4) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new HelpDesk();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    notification_layout.setVisibility(View.VISIBLE);

//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }

                if (position == 5) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new DocumentFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    notification_layout.setVisibility(View.GONE);
                }

                if (position == 6) {

                    if (AppUtill.isNetworkAvil(getApplicationContext())) {
                        flagIsSliderOpen = false;
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(parseJasonLang.getJsonToString("Logout"));

                        // Setting Dialog Message
                        alertDialog.setMessage(parseJasonLang.getJsonToString("logged_out"));

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.korevv);
                        alertDialog.setCancelable(false);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                flagIsSliderOpen = false;
                                mDrawerLayout.closeDrawer(Gravity.LEFT);

                                //mLoginManager.logOut();
                                //   FirebaseAuth.getInstance().signOut();

                                MyPreferences.getInstance(MainActivity.this).logOut(getApplicationContext());
                                MyPreferences.getInstance(getApplicationContext()).setSIGNTYPE("");
                                MyPreferences.getInstance(getApplicationContext()).setPREFERENCE("");
                                MyPreferences.getInstance(MainActivity.this).setISLOGIN(false);
                                MyPreferences.getInstance(MainActivity.this).setUSER_ROLE("");
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("key", key);
                                startActivity(intent);
                                finish();
                                // Write your code here to invoke YES event
                                //  Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                flagIsSliderOpen = false;
                                mDrawerLayout.closeDrawer(Gravity.LEFT);
                                mDrawerLayout.closeDrawer(Gravity.LEFT);
                                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }else {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Toast.makeText(MainActivity.this, "Connection Is Required.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    void setDrawerGuestK() {
        mDrawerItemList = new ArrayList<DrawerItem>();
//        DrawerItem item = new DrawerItem();
//        item.setIcon(R.drawable.home);
//        item.setTitle("Home");
//        mDrawerItemList.add(item);

        DrawerItem item2 = new DrawerItem();
        item2.setIcon(R.drawable.schedule_drawer);
        item2.setTitle(getApplicationContext().getString(R.string.Schedule1));
        mDrawerItemList.add(item2);


        DrawerItem item8 = new DrawerItem();
        item8.setIcon(R.drawable.billing);
        item8.setTitle(getApplicationContext().getString(R.string.Billing1));
        mDrawerItemList.add(item8);

        DrawerItem item9 = new DrawerItem();
        item9.setIcon(R.drawable.settings);
        item9.setTitle(getApplicationContext().getString(R.string.Setting1));
        mDrawerItemList.add(item9);

        DrawerItem item10 = new DrawerItem();
        item10.setIcon(R.drawable.logout);
        item10.setTitle(getApplicationContext().getString(R.string.Logout1));
        mDrawerItemList.add(item10);


        mRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);

        DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList, MainActivity.this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                flagIsSliderOpen = true;
            }
        });
        nav_button_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                flagIsSliderOpen = true;
            }
        });


        adapter.setOnItemClickLister(new DrawerAdapter.OnItemSelecteListener() {
            @Override
            public void onItemSelected(View v, int position) {
                if (position == 1) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new HomeFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);

                }

//                if(position==1){
//                    flagIsSliderOpen=false;
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
////                    Fragment fragment= new ViewServiceFragment();
////                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
//                   // Intent intent = new Intent(getApplicationContext(),ViewServiceActivity.class);
//                    //intent.putExtra("key",key);
//                   // startActivity(intent);
//
//                }


//                if(position==3){
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
//                    MyToast.showToast(getApplicationContext(),"Coming Soon");
//
//                }
                if (position == 2) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new BillingFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }
                if (position == 3) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new SettingsFragment();
                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }
                if (position == 4) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Logout !");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure want to Logout?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.korevv);
                    alertDialog.setCancelable(false);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            flagIsSliderOpen = false;
                            mDrawerLayout.closeDrawer(Gravity.LEFT);

                            //mLoginManager.logOut();
                            //   FirebaseAuth.getInstance().signOut();

                            MyPreferences.getInstance(MainActivity.this).logOut(getApplicationContext());
                            MyPreferences.getInstance(getApplicationContext()).setSIGNTYPE("");
                            MyPreferences.getInstance(getApplicationContext()).setPREFERENCE("");
                            MyPreferences.getInstance(MainActivity.this).setISLOGIN(false);
                            MyPreferences.getInstance(MainActivity.this).setUSER_ROLE("");
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("key", key);
                            startActivity(intent);
                            finish();
                            // Write your code here to invoke YES event
                            //  Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            flagIsSliderOpen = false;
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }


            }
        });
    }

    public void getService_category(String lang) {
        db.clearServiceList();
        category_id_list.clear();
        category_name_list.clear();

        CityState.service_cat_list = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.serviceList + "/" + lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();


                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            // JSONArray heroArray = new JSONArray(response);
                            Log.v("stateArray", String.valueOf(response));
                            category_id_list.add("");
                            category_name_list.add("Select Service Category");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String id = heroObject.getString("code_value");
                                String code_desc = heroObject.getString("code_desc");
                                CityState.service_cat_list.put(code_desc, id);
                                category_id_list.add(id);
                                category_name_list.add(code_desc);
                                db.addService(new ServiceModel(id, code_desc, ""));
                            }
                            CityState.service_categoryId = category_id_list.toArray(new String[category_id_list.size()]);
                            CityState.service_categoryName = category_name_list.toArray(new String[category_name_list.size()]);
                            //creating custom adapter object


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    public void postClockIn(final String startDate, String startTime, String client_lat, String client_long, final int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkin_start,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);


                            Log.v("stateArray", String.valueOf(response));

                            //   Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));
                            // text_time.setText(parseJasonLang.getJsonToString("clock_out"));

//                            String clockId=jsonObject.getString("clockId");
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "0").get(0).getSch_id();
                            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                            intent.putExtra("location", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLocation());
                            intent.putExtra("id", id);
                            intent.putExtra("time", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getStart_time() + " To " + db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getEnd_time());
                            intent.putExtra("name", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getName());
                            intent.putExtra("phone", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getCall());
                            intent.putExtra("pic", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getPic());
                            intent.putExtra("client_id", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getClientId());
                            intent.putExtra("additional_notes", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getAdditional_note());
                            intent.putExtra("direction", db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLat() + "," + db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getLongi());
                            startActivity(intent);
                            //   String startTime=jsonObject1.getString("startTime");
//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//


                            // scheduleModelArrayList=db.getAllImages();


                        } catch (JSONException e) {
                            e.printStackTrace();


                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(MainActivity.this, error);

                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("latitude", client_lat);
                params.put("scheduleId", id);
                params.put("checkinDate", startDate);

                params.put("agencyId", loggedInUser.getAgency_id());
                params.put("checkinTime", startTime);
                params.put("longitude", client_long);


//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
//                params.put("deviceType", "2");

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(MainActivity.this, stringRequest);
    }

    public void postClockIn1(String sch_id, final String startDate, String startTime, String client_lat, String client_long, final int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkin_start,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);


                            Log.v("stateArray", String.valueOf(response));

                            //   Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));
                            // text_time.setText(parseJasonLang.getJsonToString("clock_out"));

//                            String clockId=jsonObject.getString("clockId");
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");


                            //   String startTime=jsonObject1.getString("startTime");
//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//


                            // scheduleModelArrayList=db.getAllImages();


                        } catch (JSONException e) {
                            e.printStackTrace();


                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(MainActivity.this, error);

                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("latitude", client_lat);
                params.put("scheduleId", sch_id);
                params.put("checkinDate", startDate);

                params.put("agencyId", loggedInUser.getAgency_id());
                params.put("checkinTime", startTime);
                params.put("longitude", client_long);


//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
//                params.put("deviceType", "2");

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(MainActivity.this, stringRequest);
    }

    public void getClockOutApi(String client_sign, String caregiver_sign, String question1_yn, String question2_yn, String question3_yn, String question4_yn, String question1_note,
                               String question2_note, String question3_note, String question4_note, String sch_id, String client_signName,
                               final String startDate, String startTime,
                               String client_lat, String client_long,
                               String notes, String clockoutReason,
                               String personal_care_hours,
                               String respite_care_hours,
                               String personal_care_activities,
                               String respite_care_activities,
                               String total_calculate_units) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);

                            // Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));


                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            String billId = jsonObject.getString("billId");


                            db.clearSelectedByUser();
                            db.clearSelectedByUserRespiteCare();
                            db.clearCheckOut();
                            db.deleteCheckIn(sch_id);


//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//


                            // scheduleModelArrayList=db.getAllImages();

                            Log.v("stateArray_clock", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(MainActivity.this, error);

                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("check_out_latitude", client_lat);
                params.put("scheduleId", sch_id);
                params.put("checkout_date", startDate);

                params.put("client_sign", client_sign);
                params.put("checkout_time", startTime);
                params.put("check_out_longitude", client_long);

                params.put("personal_care_hours",personal_care_hours==null?"":personal_care_hours);
                params.put("respite_care_hours", respite_care_hours==null?"":respite_care_hours);
                params.put("personal_care_activities",   personal_care_activities==null?"":personal_care_activities);
                params.put("respite_care_activities",  respite_care_activities==null?"":respite_care_activities);
                params.put("total_calculate_units",  total_calculate_units==null?"":total_calculate_units);
                params.put("checkout_reason",clockoutReason==null?"":clockoutReason);

//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));

                params.put("question1_yn", question1_yn);
                params.put("question2_yn", question2_yn);
                params.put("question3_yn", question3_yn);
                params.put("question4_yn", question4_yn);
                params.put("question1_note", question1_note);
                params.put("question2_note", question2_note);
                params.put("question3_note", question3_note);
                params.put("question4_note", question4_note);
                params.put("additional_comments", notes);
                params.put("client_sign_name", client_signName);
                params.put("caregiver_sign", caregiver_sign);

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(MainActivity.this, stringRequest);
    }

    public void getClockOutApi1(String client_sign, String caregiver_sign, String question1_yn, String question2_yn, String question3_yn, String question4_yn, String question1_note,
                                String question2_note, String question3_note, String question4_note, String sch_id, String client_signName,
                                final String startDate, String startTime, String client_lat, String client_long, String notes,
                                String clockoutReason,
                                String personal_care_hours,
                                String respite_care_hours,
                                String personal_care_activities,
                                String respite_care_activities,
                                String total_calculate_units) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);

                            // Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));


                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Fragment fragment = new HomeFragment();
                            ProjectUtil.replaceFragment(MainActivity.this, fragment);
                            String billId = jsonObject.getString("billId");

                           // db.clearSELECTEDLIST();
                            db.clearSelectedByUser();
                            db.clearSelectedByUserRespiteCare();
                            db.clearCheckOut();
                            db.deleteCheckIn(sch_id);


//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//


                            // scheduleModelArrayList=db.getAllImages();

                            Log.v("stateArray_clock", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(MainActivity.this, error);

                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("check_out_latitude", client_lat);
                params.put("scheduleId", sch_id);
                params.put("checkout_date", startDate);

                params.put("client_sign", client_sign);
                params.put("checkout_time", startTime);
                params.put("check_out_longitude", client_long);

                params.put("personal_care_hours",personal_care_hours==null?"":personal_care_hours);
                params.put("respite_care_hours", respite_care_hours==null?"":respite_care_hours);
                params.put("personal_care_activities",   personal_care_activities==null?"":personal_care_activities);
                params.put("respite_care_activities",  respite_care_activities==null?"":respite_care_activities);
                params.put("total_calculate_units",  total_calculate_units==null?"":total_calculate_units);
                params.put("checkout_reason",clockoutReason==null?"":clockoutReason);


//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("question1_yn", question1_yn);
                params.put("question2_yn", question2_yn);
                params.put("question3_yn", question3_yn);
                params.put("question4_yn", question4_yn);
                params.put("question1_note", question1_note);
                params.put("question2_note", question2_note);
                params.put("question3_note", question3_note);
                params.put("question4_note", question4_note);
                params.put("additional_comments", notes);
                params.put("client_sign_name", client_signName);
                params.put("caregiver_sign", caregiver_sign);
                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(MainActivity.this, stringRequest);
    }

    public void postFireBaseIdToServer(String regId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://admin.korevv.com/api/insert_device_id",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("deviceType", "1");
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("registration_id", regId);


//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
//                params.put("deviceType", "2");

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    @Override
    public void onBackPressed() {

    }

    private void hitApi() {


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUrl.userInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            MyLog.showLog(response.toString());

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("1")) {

                                String count = jsonObject.getString("count");
                                noti_count.setText(count);

                            } else {
                                //    noti_count.setVisibility(View.GONE);
                            }


//
                        } catch (JSONException e) {

                            //noti_count.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  noti_count.setVisibility(View.GONE);
                ProjectUtil.showErrorResponse(getApplicationContext(), error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", loggedInUser.getLocal_user_id());

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    public void getLanguageResponse(String lang) {


        CityState.service_cat_list = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.label_language + "/" + lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);


                            Log.v("stateArray", String.valueOf(response));
                            loggedInUser.setLanguage_response(String.valueOf(response));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.v("Update", String.valueOf(resultCode));
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {

    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setCancelable(false)
                .setIcon(R.drawable.korevv)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
