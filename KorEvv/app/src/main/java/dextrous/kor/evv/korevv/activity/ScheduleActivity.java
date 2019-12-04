package dextrous.kor.evv.korevv.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.DrawerAdapter;
import dextrous.kor.evv.korevv.adapter.ServiceAdapter;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.constants.CityState;
import dextrous.kor.evv.korevv.fragment.BillingFragment;
import dextrous.kor.evv.korevv.fragment.HelpDesk;
import dextrous.kor.evv.korevv.fragment.SettingsFragment;
import dextrous.kor.evv.korevv.model.CheckInModel;
import dextrous.kor.evv.korevv.model.DrawerItem;
import dextrous.kor.evv.korevv.model.QuestionModel;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.LocationTrack;
import dextrous.kor.evv.korevv.util.LocationTrack1;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;
import dextrous.kor.evv.korevv.util.Stopwatch;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static dextrous.kor.evv.korevv.activity.MainActivity.isConnected;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private TextView clockInLebel, durationLebel, tvClockIn, tvDuration, personalCareScheduleText, respiteCareScheduleText, tvPersonalCareTime, tvRespiteCareTime;
    private LinearLayout clockInLayout;
    private CustomProgressDialog dialog;
    ImageView login_back, image_title;
    LinearLayout clock_in_out, clock_in_out1, clock_inout_new;
    RecyclerView services_recycler;
    private RecyclerView mRecyclerView;
    private ArrayList<DrawerItem> mDrawerItemList;
    TextView location, phone_text, time_text, view_deatils_button, text_time, text_time1, timer_count, call_icon, additional_notes_text, et_notes;
    public static TextView title;
    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds, Hours;
    private boolean isStart;
    String name = "", location1 = "", phone = "", direction = "", id = "", time = "", pic = "", client_id = "",
            startDate = "", endDate = "", personalCareTime = "", respiteCareTiem = "";
    LinearLayout diection_layout;
    LoggedInUser loggedInUser;
    TextView activity_text;
    //ImageView image_title;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    final List<String> animalsList = new ArrayList();
    double latitude, longitude;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack appLocationService;
    DatabaseHandler db;
    String client_lat = "", client_long = "", additional_notes;
    ParseJasonLang parseJasonLang;
    private boolean flagIsSliderOpen = false;
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    public static int activieFragment = 1;
    private PopupWindow popupWin;
    CheckInModel checkInModel;
    ScrollView scroll_view;
    FrameLayout content_frame;
    LocationTrack1 locationTrack;

    double direction1;

    private DrawerLayout mDrawerLayout;

    LocationManager locationManager;

    long diffHours = 0;
    long diffMinutes;
    private String checkInLocation, checkinReson;
    private LinearLayout respitePersonalTimeLayout;
    private boolean isC0me;

    void getId() {
        login_back = findViewById(R.id.login_back);
        image_title = findViewById(R.id.image_title);
        call_icon = findViewById(R.id.call_icon);
        clock_in_out = findViewById(R.id.clock_in_out);
        clock_in_out1 = findViewById(R.id.clock_in_out1);
        clock_inout_new = findViewById(R.id.clockin_out_new);
        services_recycler = findViewById(R.id.services_recycler);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        phone_text = findViewById(R.id.phone_text);
        time_text = findViewById(R.id.time_text);

        view_deatils_button = findViewById(R.id.view_deatils_button);
        text_time = findViewById(R.id.text_time);
        text_time1 = findViewById(R.id.text_time1);
        timer_count = findViewById(R.id.timer_count);
        activity_text = findViewById(R.id.activity_text);
        diection_layout = findViewById(R.id.diection_layout);
        image_title = findViewById(R.id.image_title);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        content_frame = findViewById(R.id.content_frame);
        scroll_view = findViewById(R.id.scroll_view);
        et_notes = findViewById(R.id.et_notes);
        additional_notes_text = findViewById(R.id.additional_notes_text);

        clockInLebel = findViewById(R.id.clock_in_time_lebel);
        durationLebel = findViewById(R.id.duration_lebel);
        tvClockIn = findViewById(R.id.tv_clock_in_time);
        tvDuration = findViewById(R.id.tv_duration_lebel);
        clockInLayout = findViewById(R.id.clockin_time_lebel_layout);
        personalCareScheduleText = findViewById(R.id.personalcare_schedule_lebel);
        respiteCareScheduleText = findViewById(R.id.respitecare_schedule_lebel);
        personalCareScheduleText.setText(parseJasonLang.getJsonToString("personalcare_schedule_for"));
        respiteCareScheduleText.setText(parseJasonLang.getJsonToString("respite_care_schedule_for"));
        clockInLebel.setText(parseJasonLang.getJsonToString("clock_in_time"));
        durationLebel.setText(parseJasonLang.getJsonToString("duration"));
        tvPersonalCareTime = findViewById(R.id.tv_personalcare_schedule);
        tvRespiteCareTime = findViewById(R.id.tv_respitecare_schedule);
        tvPersonalCareTime.setText(personalCareTime + " h");
        tvRespiteCareTime.setText(respiteCareTiem + " h");
        respitePersonalTimeLayout = findViewById(R.id.respite_personal_time_layout);
    }

    private MyPreferences myPreferences;
    private long startTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        setContentView(R.layout.activity_schedule);
        loggedInUser = new LoggedInUser(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            location1 = bundle.getString("location");
            phone = bundle.getString("phone");
            direction = bundle.getString("direction");
            id = bundle.getString("id");
            time = bundle.getString("time");
            pic = bundle.getString("pic");
            client_id = bundle.getString("client_id");
            additional_notes = bundle.getString("additional_notes");
            startDate = bundle.getString("start_date");
            endDate = bundle.getString("end_date");
            personalCareTime = bundle.getString("personal_care_time");
            respiteCareTiem = bundle.getString("respite_care_time");
            isC0me = bundle.getBoolean("check");
        }

        db = new DatabaseHandler(ScheduleActivity.this);
        parseJasonLang = new ParseJasonLang(ScheduleActivity.this);

        getId();
//        db.clearCheckIn();
        ConnectivityManager cm =
                (ConnectivityManager) ScheduleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        getService();

        call_icon.setText(parseJasonLang.getJsonToString("make_call"));
        view_deatils_button.setText(parseJasonLang.getJsonToString("direction"));
        activity_text.setText(parseJasonLang.getJsonToString("activities"));

        additional_notes_text.setText(parseJasonLang.getJsonToString("additional_notes_text"));
        if (additional_notes.equals("")) {
            et_notes.setHint(parseJasonLang.getJsonToString("et_notes"));
        } else {
            et_notes.setText(additional_notes);
        }

        // if(et_notes.getText().toString())
        if (startDate != null)
            MyPreferences.getInstance(ScheduleActivity.this).setStartDate(startDate);

        if (startDate == null) {
            startDate = MyPreferences.getInstance(ScheduleActivity.this).getStartDate();
        }
        if (!startDate.equalsIgnoreCase("") && !startDate.isEmpty()) {
            String substring;
            if (Integer.parseInt(startDate.substring(Math.max(startDate.length() - 2, 0))) < 10) {
                substring = (startDate.substring(Math.max(startDate.length() - 2, 0)).substring(1, 2));
            } else {
                substring = startDate.substring(Math.max(startDate.length() - 2, 0));
            }
            time_text.setText(AppUtill.getDayFromDate(startDate, time.substring(0, 4)) + " " + substring + ", " + time);
        } else
            time_text.setText(time);

        appLocationService = new LocationTrack(
                ScheduleActivity.this);
        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null)
            image_title.setImageBitmap(decodedByte);
        else image_title.getResources().getDrawable(R.drawable.schimg);

        String parts[] = direction.split(",");

        client_lat = parts[0];
        client_long = parts[1];

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "0").size() > 0) {

            login_back.setImageResource(R.drawable.menuicon);
            setDrawerGuest();
            content_frame.setVisibility(View.GONE);
            clock_in_out.setVisibility(View.GONE);
            clockInLayout.setVisibility(View.VISIBLE);
            respitePersonalTimeLayout.setVisibility(View.GONE);
            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
            clock_in_out1.setVisibility(View.VISIBLE);
            scroll_view.setVisibility(View.VISIBLE);
            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                checkInModel = db.getCheckIn(id);
                Log.v("check_in", "True" + checkInModel.getComplete_status());
                if (checkInModel.getComplete_status().equals("0")) {
                    Log.v("check_in", "True Ture");
                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                    clock_in_out.setVisibility(View.GONE);
                    clockInLayout.setVisibility(View.VISIBLE);
                    respitePersonalTimeLayout.setVisibility(View.GONE);

                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                    clock_in_out1.setVisibility(View.VISIBLE);


                }
            }
            if (db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).isEmpty()) {
                clock_in_out1.setVisibility(View.GONE);
                clockInLayout.setVisibility(View.INVISIBLE);


            } else {
                clock_in_out1.setVisibility(View.VISIBLE);
                clockInLayout.setVisibility(View.VISIBLE);
                respitePersonalTimeLayout.setVisibility(View.GONE);
                tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());

            }
        } else {
            if (db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).isEmpty()) {
                clock_in_out.setVisibility(View.GONE);
                clockInLayout.setVisibility(View.VISIBLE);
                respitePersonalTimeLayout.setVisibility(View.GONE);

                MyPreferences.getInstance(ScheduleActivity.this).getStartTime();

            } else {
                clock_in_out.setVisibility(View.VISIBLE);
                clockInLayout.setVisibility(View.INVISIBLE);

            }
            content_frame.setVisibility(View.GONE);
            // clock_in_out.setVisibility(View.VISIBLE);
            clock_in_out1.setVisibility(View.GONE);
            clockInLayout.setVisibility(View.INVISIBLE);
            if (!startDate.equalsIgnoreCase(AppUtill.getCurrentDate("yyyy-MM-dd"))) {
                clock_in_out.setVisibility(View.GONE);
            } else {
                clock_in_out.setVisibility(View.VISIBLE);
            }
            scroll_view.setVisibility(View.VISIBLE);
            login_back.setImageResource(R.drawable.back_arrow);
            login_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Log.v("check_in", "false");
            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
        }
        //   MyPreferences.getInstance(getApplicationContext()).setUSER_ROLE("1");
        // handler = new Handler() ;

        title.setText(name);
        location.setText(location1);
        phone_text.setText(phone);
        // et_notes.setText(additional_notes);

        getClockInClick();
        call_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });
        diection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = Double.parseDouble(client_lat);
                String value;
                double longitude = Double.parseDouble(client_long);
                String label = "I'm Here!";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(mapIntent);
            }
        });
        if (isC0me)
            MyPreferences.getInstance(ScheduleActivity.this).deleteSharedPrefrenceByKey();

        startTime = MyPreferences.getInstance(ScheduleActivity.this).getStopWatchTime();

        if (startTime == 0) {
            mHandler.sendEmptyMessage(MSG_START_TIMER);
        } else {
            mHandler.sendEmptyMessage(MSG_START_TIMER);
        }

        if (personalCareTime != null && respiteCareTiem != null) {
            MyPreferences.getInstance(ScheduleActivity.this).setPesonalCareTime(personalCareTime);
            MyPreferences.getInstance(ScheduleActivity.this).setRespiteCareTime(respiteCareTiem);
        }
    }

    void getService() {

        serviceModelArrayList.clear();
        if (isConnected) {
            if (loggedInUser.getLocal_language().equals("")) {
                getService_categorySelectedList("en", id);
            } else {
                getService_categorySelectedList(loggedInUser.getLocal_language(), id);
            }

        } else {
            serviceModelArrayList = db.getAllSelected(id);
            services_recycler.setHasFixedSize(true);
            services_recycler.setLayoutManager(new LinearLayoutManager(ScheduleActivity.this));
            //creating recyclerview adapter
            ServiceAdapter adapter = new ServiceAdapter(ScheduleActivity.this, serviceModelArrayList);

            //setting adapter to recyclerview
            services_recycler.setAdapter(adapter);
        }

    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ScheduleActivity.this);

        alertDialog.setTitle(provider + parseJasonLang.getJsonToString("Setting"));

        alertDialog
                .setMessage(provider + parseJasonLang.getJsonToString("is_not_enabled_Want_to_go_to_settings_menu"));

        alertDialog.setPositiveButton(parseJasonLang.getJsonToString("Setting"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ScheduleActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel(parseJasonLang.getJsonToString("location_permission"),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ScheduleActivity.this)
                .setMessage(message)
                .setPositiveButton(parseJasonLang.getJsonToString("ok"), okListener)
                .setNegativeButton(parseJasonLang.getJsonToString("cancel"), null)
                .create()
                .show();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void getService_category1(String lang, String id) {
        db.clearSELECTEDLIST();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.avail_acitivity + "/" + id + "/" + lang,
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

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String id = heroObject.getString("code_value");
                                String code_desc = heroObject.getString("code_desc");
                                ServiceModel serviceModel = new ServiceModel(id, code_desc, "");
                                db.addSelectedList(serviceModel);
                            }
                            getService();


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

    public void getService_category(final String startDate, String startTime, String client_lat, String client_long, final int position) {

        dialog = new CustomProgressDialog();
        dialog.startProgress(ScheduleActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkin_start,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        //getting the whole json object from the response
                        //we have the array named hero inside the object
                        //so here we are getting that json array
                        //JSONObject jsonObject = new JSONObject(response);
                        dialog.stopProgress();
                        clock_in_out.setVisibility(View.GONE);
                        clock_in_out1.setVisibility(View.VISIBLE);
                        clockInLayout.setVisibility(View.VISIBLE);
                        respitePersonalTimeLayout.setVisibility(View.GONE);

                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());

                        Log.v("direction", String.valueOf(direction));
                        //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));
                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                        db.addCheckInDetails(new CheckInModel("1", "0", id,
                                loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime,
                                String.valueOf(latitude) + "," + String.valueOf(longitude), checkInLocation, checkinReson));

                               /* String clockId=jsonObject.getString("clockId");
                                String status=jsonObject.getString("status");
                                String message=jsonObject.getString("message");
*/

                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {
                            login_back.setImageResource(R.drawable.menuicon);
                            setDrawerGuest();
                            content_frame.setVisibility(View.GONE);
                            clock_in_out1.setVisibility(View.VISIBLE);
                            clock_in_out.setVisibility(View.GONE);
                            clockInLayout.setVisibility(View.VISIBLE);
                            respitePersonalTimeLayout.setVisibility(View.GONE);

                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                            scroll_view.setVisibility(View.VISIBLE);
                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                checkInModel = db.getCheckIn(id);
                                Log.v("check_in", "True" + checkInModel.getComplete_status());
                                if (checkInModel.getComplete_status().equals("0")) {
                                    Log.v("check_in", "True Ture");
                                    text_time.setText(parseJasonLang.getJsonToString("clock_out1"));
                                    clock_in_out.setVisibility(View.GONE);
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);
                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                    //  clock_in_out.setPadding(10,20,10,20);
                                }
                            }
                        } else {
                            content_frame.setVisibility(View.GONE);
                            clock_in_out.setVisibility(View.VISIBLE);
                            clock_in_out1.setVisibility(View.GONE);
                            clockInLayout.setVisibility(View.VISIBLE);
                            respitePersonalTimeLayout.setVisibility(View.GONE);

                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                            scroll_view.setVisibility(View.VISIBLE);
                            login_back.setImageResource(R.drawable.back_arrow);
                            login_back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            Log.v("check_in", "false");
                            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                            clock_in_out.setBackgroundResource(R.drawable.circle_background);
                            //clock_in_out.setBackgroundResource(R.drawable.circle_background);
                        }

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
////
                        // scheduleModelArrayList=db.getAllImages();

                        Log.v("stateArray", String.valueOf(response));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        dialog.stopProgress();
                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(ScheduleActivity.this, error);

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
                if (checkInLocation == null) {
                    checkInLocation = "";
                }
                if (checkinReson == null) {
                    checkinReson = "";
                }

                params.put("checkinLocation", checkInLocation);
                params.put("checkinReason", checkinReson);
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
        ProjectUtil.setRequest(ScheduleActivity.this, stringRequest);
    }

    void getClockInClick() {
        clock_in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) ScheduleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                long timeConvertAm = 0;
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
                String formattedDate = df.format(c.getTime());
                String formattedtime = df1.format(c.getTime());
                if (isConnected) {
                    locationTrack = new LocationTrack1(ScheduleActivity.this);
                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        direction1 = distance(Double.parseDouble(client_lat), Double.parseDouble(client_long), latitude, longitude);
                        getLocation();
                        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    } else {
                        locationTrack.showSettingsAlert();
                    }
                } else {
                    Location gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);

                    if (gpsLocation != null) {
                        latitude = gpsLocation.getLatitude();
                        longitude = gpsLocation.getLongitude();
                        direction1 = distance(Double.parseDouble(client_lat), Double.parseDouble(client_long), latitude, longitude);
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "Mobile Location (GPS): \nLatitude: " + latitude
//                                    + "\nLongitude: " + longitude+ formattedDate + " "+formattedDate1,
//                            Toast.LENGTH_LONG).show();                    } else {
                        // showSettingsAlert("GPS");
                    } else {
                        showSettingsAlert("GPS");
                    }
                    Log.v("direction", String.valueOf(direction1));
                }
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                Date d1 = null;
                Date d2 = null;
                String timeStart = null;
                if (db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).size() > 0)
                    timeStart = db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getStart_time();
//                if(timeStart.contains("AM")){
//                    String time[] = timeStart.split("A");
//                     timeConvertAm = Integer.parseInt(time[0].trim().split(":")[0]) ;
//                    timeStart= String.valueOf(timeConvertAm)+ ":"+time[0].split(":")[1];
//                }
//                if(timeStart.contains("PM")){
//
//                    String time[] = timeStart.split("P");
//                    if(Long.parseLong(time[0].split(":")[0])==12){
//                        timeConvertAm = Long.parseLong(time[0].split(":")[0]);
//                    }else {
//                        timeConvertAm = Long.parseLong(time[0].split(":")[0]) + 12;
//                    }
//
//                    timeStart = String.valueOf(timeConvertAm)+":"+time[0].split(":")[1];
//                }
                try {
                    if (timeStart!=null)
                    d1 = format.parse(formattedDate + " " + timeStart.trim());
                    d2 = format.parse(formattedDate + " " + formattedtime);

                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    diffMinutes = diff / (60 * 1000) % 60;
                    diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    if (diffHours > 0) {
                        diffMinutes = (diffHours * 60) + diffMinutes;
                    } else {
                        diffMinutes = diffMinutes;
                    }
                    System.out.print(diffDays + " days, ");
                    System.out.print(diffHours + " hours, ");
                    System.out.print(diffMinutes + " minutes, ");
                    System.out.print(diffSeconds + " seconds.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (diffMinutes > 8) {
                    if (direction1 < .80) {
                        if (isConnected) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("late_check_in") + " " + parseJasonLang.getJsonToString("clock_in_message"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    // getService_category(formattedDate, formattedtime, String.valueOf(latitude), String.valueOf(longitude), 0);
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString(parseJasonLang.getJsonToString("late_check_in") + " " + "clock_in_message"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (checkInLocation == null || checkinReson == null) {
                                        showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    } else {
                                        Log.v("direction", String.valueOf(direction1));
                                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {
                                            text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clock_in_out.setVisibility(View.GONE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                        } else {
                                            text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clock_in_out.setVisibility(View.GONE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                            db.addCheckInDetails(new CheckInModel("1", "0", id,
                                                    loggedInUser.getLocal_agency(), client_id,
                                                    loggedInUser.getLocal_user_id(), formattedDate, formattedtime,
                                                    String.valueOf(latitude) + "," + String.valueOf(longitude),
                                                    checkInLocation, checkinReson));
                                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {
                                                login_back.setImageResource(R.drawable.menuicon);
                                                setDrawerGuest();
                                                content_frame.setVisibility(View.GONE);
                                                clock_in_out.setVisibility(View.VISIBLE);
                                                clockInLayout.setVisibility(View.INVISIBLE);
                                                scroll_view.setVisibility(View.VISIBLE);
                                                if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                                    checkInModel = db.getCheckIn(id);
                                                    Log.v("check_in", "True" + checkInModel.getComplete_status());
                                                    if (checkInModel.getComplete_status().equals("0")) {
                                                        Log.v("check_in", "True Ture");
                                                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                                        clock_in_out1.setVisibility(View.VISIBLE);
                                                        clockInLayout.setVisibility(View.VISIBLE);
                                                        respitePersonalTimeLayout.setVisibility(View.GONE);

                                                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());

                                                        clock_in_out.setVisibility(View.GONE);
                                                    }
                                                }
                                            } else {
                                                content_frame.setVisibility(View.GONE);
                                                clock_in_out1.setVisibility(View.GONE);
                                                clockInLayout.setVisibility(View.INVISIBLE);
                                                clock_in_out.setVisibility(View.VISIBLE);
                                                scroll_view.setVisibility(View.VISIBLE);
                                                login_back.setImageResource(R.drawable.back_arrow);
                                                login_back.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        finish();
                                                    }
                                                });
                                                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                                Log.v("check_in", "false");
                                                text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                                                clock_in_out.setBackgroundResource(R.drawable.circle_background);
                                            }
                                        }
                                        mHandler.sendEmptyMessage(MSG_START_TIMER);
                                        if (startTime == 0)
                                            MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());

                                    }
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    } else {
                        if (isConnected) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("late_check_in") + " " + parseJasonLang.getJsonToString("location_mismatch_clockin"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    // getService_category(formattedDate, formattedtime, String.valueOf(latitude), String.valueOf(longitude), 0);
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();


                        } else {


                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString(parseJasonLang.getJsonToString("late_check_in") + " " + "location_mismatch_clockin"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   /* clock_in_out.setVisibility(View.GONE);
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);*/
                                    if (checkInLocation == null && checkinReson == null) {
                                        showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    } else {
                                        Log.v("direction", String.valueOf(direction));
                                        db.addCheckInDetails(new CheckInModel("0", "0", id, loggedInUser.getLocal_agency(),
                                                client_id, loggedInUser.getLocal_user_id(),
                                                formattedDate, formattedtime, String.valueOf(latitude) + "," +
                                                String.valueOf(longitude), checkInLocation,
                                                checkinReson));
                                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                                            login_back.setImageResource(R.drawable.menuicon);

                                            setDrawerGuest();
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out.setVisibility(View.GONE);
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                            scroll_view.setVisibility(View.VISIBLE);
                                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                                checkInModel = db.getCheckIn(id);
                                                Log.v("check_in", "True" + checkInModel.getComplete_status());
                                                if (checkInModel.getComplete_status().equals("0")) {
                                                    Log.v("check_in", "True Ture");
                                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));


                                                }
                                            }
                                        } else {
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out.setVisibility(View.VISIBLE);
                                            clock_in_out1.setVisibility(View.GONE);
                                            clockInLayout.setVisibility(View.INVISIBLE);

                                            scroll_view.setVisibility(View.VISIBLE);
                                            login_back.setImageResource(R.drawable.back_arrow);
                                            login_back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            });
                                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                            Log.v("check_in", "false");
                                            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                                            clock_in_out.setBackgroundResource(R.drawable.circle_background);
                                        }
                                        mHandler.sendEmptyMessage(MSG_START_TIMER);
                                        if (startTime == 0)
                                            MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();


                        }
                    }
                } else {
                    if (direction1 < .80) {
                        if (isConnected) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("clock_in_message"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mHandler.sendEmptyMessage(MSG_START_TIMER);
                                    if (startTime == 0)
                                        MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                                    getService_category(formattedDate, formattedtime, String.valueOf(latitude),
                                            String.valueOf(longitude), 0);
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("clock_in_message"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    Log.v("direction", String.valueOf(direction1));
                                    if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {
                                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                        clock_in_out1.setVisibility(View.VISIBLE);
                                        clockInLayout.setVisibility(View.VISIBLE);
                                        respitePersonalTimeLayout.setVisibility(View.GONE);

                                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                        clock_in_out.setVisibility(View.GONE);


                                    } else {
                                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                        clock_in_out1.setVisibility(View.VISIBLE);
                                        clockInLayout.setVisibility(View.VISIBLE);
                                        respitePersonalTimeLayout.setVisibility(View.GONE);

                                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                        clock_in_out.setVisibility(View.GONE);
                                        db.addCheckInDetails(new CheckInModel("1", "0", id, loggedInUser.getLocal_agency(),
                                                client_id, loggedInUser.getLocal_user_id(),
                                                formattedDate, formattedtime,
                                                String.valueOf(latitude) + "," + String.valueOf(longitude)
                                                , checkInLocation, checkinReson));

                                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                                            login_back.setImageResource(R.drawable.menuicon);

                                            setDrawerGuest();
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out.setVisibility(View.VISIBLE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                            scroll_view.setVisibility(View.VISIBLE);
                                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                                checkInModel = db.getCheckIn(id);
                                                Log.v("check_in", "True" + checkInModel.getComplete_status());
                                                if (checkInModel.getComplete_status().equals("0")) {
                                                    Log.v("check_in", "True Ture");
                                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                                    clock_in_out1.setVisibility(View.VISIBLE);
                                                    clockInLayout.setVisibility(View.VISIBLE);
                                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                                    clock_in_out.setVisibility(View.GONE);
                                                }
                                            }
                                        } else {
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out1.setVisibility(View.GONE);
                                            clockInLayout.setVisibility(View.INVISIBLE);

                                            clock_in_out.setVisibility(View.VISIBLE);
                                            scroll_view.setVisibility(View.VISIBLE);
                                            login_back.setImageResource(R.drawable.back_arrow);
                                            login_back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            });
                                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                            Log.v("check_in", "false");
                                            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                                            clock_in_out.setBackgroundResource(R.drawable.circle_background);
                                        }
                                    }

                                    mHandler.sendEmptyMessage(MSG_START_TIMER);
                                    if (startTime == 0)
                                        MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    } else {

                        if (isConnected) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("location_mismatch_clockin"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    // getService_category(formattedDate, formattedtime, String.valueOf(latitude), String.valueOf(longitude), 0);
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();


                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_in"));
                            alertDialog.setCancelable(false);
                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("location_mismatch_clockin"));
                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);
                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("yes"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (checkInLocation == null || checkinReson == null) {
                                        showReasonDialog(ScheduleActivity.this, formattedDate, formattedtime);
                                    } else {
                                        clock_in_out.setVisibility(View.GONE);
                                        clock_in_out1.setVisibility(View.VISIBLE);
                                        clockInLayout.setVisibility(View.VISIBLE);
                                        respitePersonalTimeLayout.setVisibility(View.GONE);

                                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                        Log.v("direction", String.valueOf(direction));
                                        db.addCheckInDetails(new CheckInModel("0", "0", id, loggedInUser.getLocal_agency(),
                                                client_id, loggedInUser.getLocal_user_id(),
                                                formattedDate, formattedtime,
                                                String.valueOf(latitude) + "," + String.valueOf(longitude), checkInLocation, checkinReson));
                                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                                            login_back.setImageResource(R.drawable.menuicon);

                                            setDrawerGuest();
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out.setVisibility(View.GONE);
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                            scroll_view.setVisibility(View.VISIBLE);
                                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                                checkInModel = db.getCheckIn(id);
                                                Log.v("check_in", "True" + checkInModel.getComplete_status());
                                                if (checkInModel.getComplete_status().equals("0")) {
                                                    Log.v("check_in", "True Ture");
                                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                                }
                                            }
                                        } else {
                                            content_frame.setVisibility(View.GONE);
                                            clock_in_out.setVisibility(View.VISIBLE);
                                            clock_in_out1.setVisibility(View.GONE);
                                            clockInLayout.setVisibility(View.INVISIBLE);

                                            scroll_view.setVisibility(View.VISIBLE);
                                            login_back.setImageResource(R.drawable.back_arrow);
                                            login_back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            });
                                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                            Log.v("check_in", "false");
                                            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                                            clock_in_out.setBackgroundResource(R.drawable.circle_background);
                                        }
                                        mHandler.sendEmptyMessage(MSG_START_TIMER);
                                        if (startTime == 0)
                                            MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                                    }
                                }
                            });
                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("no"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        }
                    }
                }
                MyPreferences.getInstance(ScheduleActivity.this).setStartTime(System.currentTimeMillis());
            }
        });
        clock_inout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeConvertAm = 0;
                ConnectivityManager cm =
                        (ConnectivityManager) ScheduleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
                String formattedDate = df.format(c.getTime());
                String formattedtime = df1.format(c.getTime());
                if (isConnected) {
                    locationTrack = new LocationTrack1(ScheduleActivity.this);

                    if (locationTrack.canGetLocation()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        direction1 = distance(Double.parseDouble(client_lat), Double.parseDouble(client_long), latitude, longitude);
                        getLocation();
                        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    } else {

                        locationTrack.showSettingsAlert();
                    }
                } else {
                    Location gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);

                    if (gpsLocation != null) {
                        latitude = gpsLocation.getLatitude();
                        longitude = gpsLocation.getLongitude();
                        direction1 = distance(Double.parseDouble(client_lat), Double.parseDouble(client_long), latitude, longitude);

//                    Toast.makeText(
//                            getApplicationContext(),
//                            "Mobile Location (GPS): \nLatitude: " + latitude
//                                    + "\nLongitude: " + longitude+ formattedDate + " "+formattedDate1,
//                            Toast.LENGTH_LONG).show();
                    } else {
                        showSettingsAlert("GPS");
                    }

                    Log.v("direction", String.valueOf(direction1));

                }

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

                Date d1 = null;
                Date d2 = null;
                String timeStart = null;
                if (db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).size()>0)
                    timeStart= db.getScheduleOnSchid(loggedInUser.getLocal_user_id(), id).get(0).getEnd_time();

                try {
                    d1 = format.parse(formattedDate + " " + timeStart);
                    d2 = format.parse(formattedDate + " " + formattedtime);

                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    diffMinutes = diff / (60 * 1000) % 60;
                    diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    System.out.print(diffDays + " days, ");
                    System.out.print(diffHours + " hours, ");
                    System.out.print(diffMinutes + " minutes, ");
                    System.out.print(diffSeconds + " seconds.");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (diffMinutes > 8) {
                    if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                            checkInModel = db.getCheckIn(id);

                            if (checkInModel.getComplete_status().equals("0")) {
                                if (direction1 < .80) {

                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);
                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                    clock_in_out.setVisibility(View.GONE);

                            /*        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                                    // Setting Dialog Title
                                    alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_out"));
                                    alertDialog.setCancelable(false);

                                    // Setting Dialog Message
                                    alertDialog.setMessage(parseJasonLang.getJsonToString("clock_out_confirm") + " " + parseJasonLang.getJsonToString("late_check_out"));

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.korevv);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            text_time1.setText(parseJasonLang.getJsonToString("clock_out1"));
                                            text_time1.setText(parseJasonLang.getJsonToString("clock_out1"));
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clockInLayout.setVisibility(View.VISIBLE);

                                            clock_in_out.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), ClockOutActivity.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();*/

                                    showCustomDialogClockOut(ScheduleActivity.this,parseJasonLang.getJsonToString("clock_out_confirm") + " " + parseJasonLang.getJsonToString("late_check_out"));


                                } else {

                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);
                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                    clock_in_out.setVisibility(View.GONE);

                                    /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                                    // Setting Dialog Title
                                    alertDialog.setTitle(parseJasonLang.getJsonToString("Location_Mismatched"));
                                    alertDialog.setCancelable(false);

                                    // Setting Dialog Message
                                    alertDialog.setMessage(parseJasonLang.getJsonToString("late_check_out") + " " + parseJasonLang.getJsonToString("location_message"));

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.korevv);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            showCustomDialogClockOut();
                                            // Write your code here to invoke YES event
                                         *//*   Intent intent = new Intent(getApplicationContext(), ClockOutActivity.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("id", id);
                                            startActivity(intent);*//*
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();
*/

                                    showCustomDialogClockOut(ScheduleActivity.this,parseJasonLang.getJsonToString("late_check_out") + " " + parseJasonLang.getJsonToString("location_message"));
                                }


                            }
                        }
                    }
                } else {
                    if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                            checkInModel = db.getCheckIn(id);

                            if (checkInModel.getComplete_status().equals("0")) {
                                if (direction1 < .80) {


                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);
                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                    clock_in_out.setVisibility(View.GONE);


                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                                    // Setting Dialog Title
                                    alertDialog.setTitle(parseJasonLang.getJsonToString("confirm_clock_out"));
                                    alertDialog.setCancelable(false);

                                    // Setting Dialog Message
                                    alertDialog.setMessage(parseJasonLang.getJsonToString("clock_out_confirm"));

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.korevv);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                          /*  mHandler.sendEmptyMessage(MSG_STOP_TIMER);
                                            myPreferences.setStopWatchTime(0);*/
                                            text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                            clock_in_out1.setVisibility(View.VISIBLE);
                                            clockInLayout.setVisibility(View.VISIBLE);
                                            respitePersonalTimeLayout.setVisibility(View.GONE);

                                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                            clock_in_out.setVisibility(View.GONE);

                                            if (MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime().equalsIgnoreCase("0")) {
                                                Intent intent = new Intent(getApplicationContext(), ClockOutRespiteCareActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("id", id);
                                                intent.putExtra("time", timer.getElapsedTime());
                                                intent.putExtra("personacaretime", MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime());
                                                intent.putExtra("respitecaretime", MyPreferences.getInstance(ScheduleActivity.this).getRespiteCareTime());
                                                startActivity(intent);
                                            } else {

                                                Intent intent = new Intent(getApplicationContext(), ClockOutActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("id", id);
                                                intent.putExtra("time", timer.getElapsedTime());
                                                intent.putExtra("personacaretime", MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime());
                                                intent.putExtra("respitecaretime", MyPreferences.getInstance(ScheduleActivity.this).getRespiteCareTime());
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();


                                } else {

                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                    clock_in_out1.setVisibility(View.VISIBLE);
                                    clockInLayout.setVisibility(View.VISIBLE);
                                    respitePersonalTimeLayout.setVisibility(View.GONE);

                                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                                    clock_in_out.setVisibility(View.GONE);

                                  /*  AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                                    // Setting Dialog Title
                                    alertDialog.setTitle(parseJasonLang.getJsonToString("Location_Mismatched"));
                                    alertDialog.setCancelable(false);

                                    // Setting Dialog Message
                                    alertDialog.setMessage(parseJasonLang.getJsonToString("location_message"));

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.korevv);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            // Write your code here to invoke YES event
                                            Intent intent = new Intent(getApplicationContext(), ClockOutActivity.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();*/
                                    showCustomDialogClockOut(ScheduleActivity.this,parseJasonLang.getJsonToString("location_message"));

                                }


                            }
                        }
                    }
                }


            }
        });
    }

    private void showCustomDialogClockOut(Context context,String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        dialog.setCancelable(false);
        View view = inflater.inflate(R.layout.clockout_reason_dialog, null, false);
        Button continueButtonCheckOut, cancleButton;
        TextView checkoutTitleLebel, checkoutConfirmMessage, checkout_reason;
        EditText enterClockOutReson;

        continueButtonCheckOut = view.findViewById(R.id.buttoncheckout_continue);
        cancleButton = view.findViewById(R.id.checkout_cancel_button);
        checkoutTitleLebel = view.findViewById(R.id.confirm_clockout_lebel);
        checkout_reason = view.findViewById(R.id.enter_clockout_reason_lebel);
        checkoutConfirmMessage = view.findViewById(R.id.enter_checkoutmessage_lebel);
        enterClockOutReson = view.findViewById(R.id.et_clockout_reason);

        continueButtonCheckOut.setText(parseJasonLang.getJsonToString("continue"));
        cancleButton.setText(parseJasonLang.getJsonToString("cancel"));
        checkoutTitleLebel.setText(parseJasonLang.getJsonToString("clock_out1"));
        checkout_reason.setText(parseJasonLang.getJsonToString("enter_reason"));
        enterClockOutReson.setHint(parseJasonLang.getJsonToString("enter_reason"));
        checkoutConfirmMessage.setText(message);
        continueButtonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enterClockOutReson.getText().toString().trim() != null && !enterClockOutReson.getText().toString().trim().isEmpty()
                        && enterClockOutReson.getText().toString().trim() != "") {
                 /*   mHandler.sendEmptyMessage(MSG_STOP_TIMER);
                    myPreferences.setStopWatchTime(0);*/
                    AppUtill.hideKeyboard(ScheduleActivity.this);
                    dialog.dismiss();

                    if (MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime().equalsIgnoreCase("0")) {
                        Intent intent = new Intent(getApplicationContext(), ClockOutRespiteCareActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", id);
                        intent.putExtra("time", timer.getElapsedTime());
                        intent.putExtra("personacaretime", MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime());
                        intent.putExtra("respitecaretime", MyPreferences.getInstance(ScheduleActivity.this).getRespiteCareTime());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ClockOutActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", id);
                        intent.putExtra("reason", enterClockOutReson.getText().toString());
                        intent.putExtra("time", timer.getElapsedTime());
                        intent.putExtra("personacaretime", MyPreferences.getInstance(ScheduleActivity.this).getPersonalCareTime());
                        intent.putExtra("respitecaretime", MyPreferences.getInstance(ScheduleActivity.this).getRespiteCareTime());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, parseJasonLang.getJsonToString("please_enter_reason"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtill.hideKeyboard(ScheduleActivity.this);
                dialog.dismiss();
            }
        });

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();

        Point displaySize = AppUtill.getDisplayDimensions(context);
        int width = displaySize.x - 40 - 40;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        assert window != null;
        window.setLayout(width, height);
        window.setBackgroundDrawableResource(R.color.trasparentcolor);
        window.setGravity(Gravity.CENTER);
        dialog.show();
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
        item8.setTitle(parseJasonLang.getJsonToString("Billing"));
        mDrawerItemList.add(item8);

        DrawerItem item9 = new DrawerItem();
        item9.setIcon(R.drawable.settings);
        item9.setTitle(parseJasonLang.getJsonToString("Setting"));
        mDrawerItemList.add(item9);

        DrawerItem item3 = new DrawerItem();
        item3.setIcon(R.drawable.settings);
        item3.setTitle(parseJasonLang.getJsonToString("help_desk"));
        mDrawerItemList.add(item3);

        DrawerItem item10 = new DrawerItem();
        item10.setIcon(R.drawable.logout);
        item10.setTitle(parseJasonLang.getJsonToString("Logout"));
        mDrawerItemList.add(item10);

        mRecyclerView = findViewById(R.id.left_drawer);

        DrawerAdapter adapter = new DrawerAdapter(mDrawerItemList, ScheduleActivity.this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mDrawerLayout.openDrawer(Gravity.LEFT);
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
                            (ConnectivityManager) ScheduleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    content_frame.setVisibility(View.GONE);
                    clock_in_out.setVisibility(View.GONE);
                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                    clockInLayout.setVisibility(View.VISIBLE);
                    respitePersonalTimeLayout.setVisibility(View.GONE);

                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                    clock_in_out1.setVisibility(View.VISIBLE);
                    scroll_view.setVisibility(View.VISIBLE);
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
                    content_frame.setVisibility(View.VISIBLE);
                    clock_in_out.setVisibility(View.GONE);
                    clockInLayout.setVisibility(View.INVISIBLE);

                    clock_in_out1.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.GONE);
                    Fragment fragment = new BillingFragment();
                    ProjectUtil.replaceFragment(ScheduleActivity.this, fragment);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);


                }
                if (position == 3) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    content_frame.setVisibility(View.VISIBLE);
                    clock_in_out.setVisibility(View.GONE);
                    clockInLayout.setVisibility(View.INVISIBLE);

                    clock_in_out1.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.GONE);
                    Fragment fragment = new SettingsFragment();
                    ProjectUtil.replaceFragment(ScheduleActivity.this, fragment);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }
                if (position == 4) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Fragment fragment = new HelpDesk();
                    ProjectUtil.replaceFragment(ScheduleActivity.this, fragment);
//                    Fragment fragment= new HowToUse();
//                    ProjectUtil.replaceFragment(MainActivity.this, fragment);
                    // Intent intent = new Intent(getApplicationContext(),HowToUseActivity.class);
                    // intent.putExtra("key",key);
                    // startActivity(intent);

                }
                if (position == 5) {
                    flagIsSliderOpen = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    content_frame.setVisibility(View.VISIBLE);
                    clock_in_out.setVisibility(View.GONE);
                    clockInLayout.setVisibility(View.INVISIBLE);

                    clock_in_out1.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.GONE);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(parseJasonLang.getJsonToString("Logout"));

                    // Setting Dialog Message
                    alertDialog.setMessage(parseJasonLang.getJsonToString("logout_message"));

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

                            MyPreferences.getInstance(ScheduleActivity.this).logOut(getApplicationContext());
                            MyPreferences.getInstance(getApplicationContext()).setSIGNTYPE("");
                            MyPreferences.getInstance(getApplicationContext()).setPREFERENCE("");
                            MyPreferences.getInstance(ScheduleActivity.this).setISLOGIN(false);
                            MyPreferences.getInstance(ScheduleActivity.this).setUSER_ROLE("");
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


                }


            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected) {
            if (loggedInUser.getLocal_language().equals("")) {
                getAllQuestions("en");
            } else {
                getAllQuestions(loggedInUser.getLocal_language());
            }
        }
    }

    public void getAllQuestions(String lang) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.questionList + "/" + lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            db.clearQuestionTable();
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();


                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            // JSONArray heroArray = new JSONArray(response);
                            Log.v("stateArray", String.valueOf(response));

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String question = heroObject.getString("question");

                                db.addQuestion(new QuestionModel(question));


                            }


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

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getService_categorySelectedList(String lang, String id1) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.avail_acitivity + "/" + id1 + "/" + lang,
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

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String id = heroObject.getString("code_value");
                                String code_desc = heroObject.getString("code_desc");
                                ServiceModel serviceModel = new ServiceModel(id, code_desc, id1);
                                // db.addSelectedList(serviceModel);
                                serviceModelArrayList.add(serviceModel);
                                // Log.v("stateArray",db.getAllSelected(id1).get(i).getId());
                            }

                            services_recycler.setHasFixedSize(true);
                            services_recycler.setLayoutManager(new LinearLayoutManager(ScheduleActivity.this));
                            //creating recyclerview adapter
                            ServiceAdapter adapter = new ServiceAdapter(ScheduleActivity.this, serviceModelArrayList);

                            //setting adapter to recyclerview
                            services_recycler.setAdapter(adapter);

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
                        ProjectUtil.showErrorResponse(ScheduleActivity.this, error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(ScheduleActivity.this, stringRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            int flag_count = 0;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            direction1 = distance(Double.parseDouble(client_lat), Double.parseDouble(client_long), latitude, longitude);
            Geocoder geocoder = new Geocoder(ScheduleActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.isEmpty()) {
                MyToast.showToast(getApplicationContext(), "Your location can't be dedected");
            } else {
                Log.v("location", String.valueOf(addresses));
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); //
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                if (CityState.districtName != null)
                    for (int i = 0; i < CityState.districtName.length; i++) {
                        if (CityState.districtName[i].equals(city)) {
                            flag_count++;
                        }
                    }//                if (flag_count > 0) {
//                    place_text.setText(address);
//                    address_final = address;
//                } else {
//                    address_final = "";
//                    place_text.setText("Service is not available at this location");
//                }
            }

        } catch (Exception e) {
            Log.d("error", "onLocationChanged: " + e.getMessage());
        }
        {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {

    }

    public void showReasonDialog(final Context context, final String formattedDate, final String formattedtime) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.clockin_reason_dialog, null, false);
        dialog.setCancelable(false);
        TextView clockInLebel, enterLocationLabel, enterReasonLebel;
        EditText enterLocation, enterReason;
        Button yesButton, noButton;
        clockInLebel = view.findViewById(R.id.spent_hours_lebel);
        enterLocationLabel = view.findViewById(R.id.personal_care_time_lebel);
        enterReasonLebel = view.findViewById(R.id.respite_time_lebel);
        enterLocation = view.findViewById(R.id.et_clockin_enter_location);
        enterReason = view.findViewById(R.id.et_clockin_reason);
        yesButton = view.findViewById(R.id.ok_button);
        noButton = view.findViewById(R.id.checkin_cancel_button);
        clockInLebel.setText(parseJasonLang.getJsonToString("confirm_clock_in"));
        enterLocationLabel.setText(parseJasonLang.getJsonToString("enter_location"));
        enterReasonLebel.setText(parseJasonLang.getJsonToString("enter_reason"));
        enterLocation.setHint(parseJasonLang.getJsonToString("enter_location"));
        enterReason.setHint(parseJasonLang.getJsonToString("enter_reason"));
        yesButton.setText(parseJasonLang.getJsonToString("yes"));
        noButton.setText(parseJasonLang.getJsonToString("no"));

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();

        Point displaySize = AppUtill.getDisplayDimensions(context);
        int width = displaySize.x - 40 - 40;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        assert window != null;
        window.setLayout(width, height);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !enterReason.getText().toString().trim().isEmpty()) {
                    dialog.dismiss();
                    AppUtill.hideKeyboard(ScheduleActivity.this);
                    clock_in_out.setVisibility(View.GONE);
                    clock_in_out1.setVisibility(View.VISIBLE);
                    clockInLayout.setVisibility(View.VISIBLE);
                    respitePersonalTimeLayout.setVisibility(View.GONE);

                    tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                    checkInLocation = enterLocation.getText().toString().trim() !=null?enterLocation.getText().toString():" ";
                    checkinReson = enterReason.getText().toString();
                    mHandler.sendEmptyMessage(MSG_START_TIMER);
                    if (startTime == 0)
                        MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                    if (AppUtill.isNetworkAvil(ScheduleActivity.this)) {
                        getService_category(formattedDate, formattedtime, String.valueOf(latitude), String.valueOf(longitude), 0);
                    } else {
                        clock_in_out.setVisibility(View.GONE);
                        clock_in_out1.setVisibility(View.VISIBLE);

                        clockInLayout.setVisibility(View.VISIBLE);
                        respitePersonalTimeLayout.setVisibility(View.GONE);

                        tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                        Log.v("direction", String.valueOf(direction));
                        db.addCheckInDetails(new CheckInModel("0", "0", id, loggedInUser.getLocal_agency(),
                                client_id, loggedInUser.getLocal_user_id(),
                                formattedDate, formattedtime,
                                String.valueOf(latitude) + "," + String.valueOf(longitude), checkInLocation, checkinReson));
                        text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                        if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).size() > 0) {

                            login_back.setImageResource(R.drawable.menuicon);

                            setDrawerGuest();
                            content_frame.setVisibility(View.GONE);
                            clock_in_out.setVisibility(View.GONE);
                            clock_in_out1.setVisibility(View.VISIBLE);
                            clockInLayout.setVisibility(View.VISIBLE);
                            respitePersonalTimeLayout.setVisibility(View.GONE);

                            tvClockIn.setText((AppUtill.getTimeFromMillis(MyPreferences.getInstance(ScheduleActivity.this).getStartTime() + "")).trim());
                            scroll_view.setVisibility(View.VISIBLE);
                            if (db.getAllCheckIn(loggedInUser.getLocal_user_id(), id).get(0).getSch_id().equals(id)) {
                                checkInModel = db.getCheckIn(id);
                                Log.v("check_in", "True" + checkInModel.getComplete_status());
                                if (checkInModel.getComplete_status().equals("0")) {
                                    Log.v("check_in", "True Ture");
                                    text_time1.setText(parseJasonLang.getJsonToString("clock_out"));
                                }
                            }
                        } else {
                            content_frame.setVisibility(View.GONE);
                            clock_in_out.setVisibility(View.VISIBLE);
                            clock_in_out1.setVisibility(View.GONE);
                            clockInLayout.setVisibility(View.INVISIBLE);

                            scroll_view.setVisibility(View.VISIBLE);
                            login_back.setImageResource(R.drawable.back_arrow);
                            login_back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            Log.v("check_in", "false");
                            text_time.setText(parseJasonLang.getJsonToString("clock_In"));
                            clock_in_out.setBackgroundResource(R.drawable.circle_background);
                        }
                        mHandler.sendEmptyMessage(MSG_START_TIMER);
                        if (startTime == 0)
                            MyPreferences.getInstance(ScheduleActivity.this).setStopWatchTime(System.currentTimeMillis());
                    }
                } else {
                   // enterReason.setError(null);
                    Toast.makeText(context, parseJasonLang.getJsonToString("please_enter_reason"), Toast.LENGTH_SHORT).show();
                }


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppUtill.hideKeyboard(ScheduleActivity.this);

            }
        });

        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.trasparentcolor);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;

    Stopwatch timer = new Stopwatch();
    final int REFRESH_RATE = 100;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    if (startTime == 0)
                        timer.start();
                    else
                        timer.start(startTime);//start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;
                case MSG_UPDATE_TIMER:
                    //     tvDuration.setText(""+ timer.getElapsedTimeMin());
                    tvDuration.setText(Stopwatch.parseTime(timer.getElapsedTime()) + " h");
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    // tvDuration.setText(""+ timer.getElapsedTimeMin());
                    tvDuration.setText(Stopwatch.parseTime(timer.getElapsedTime()) + " h");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.stop();
    }
}
