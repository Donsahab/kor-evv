package dextrous.kor.evv.korevv.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.constants.ApiTagConstants;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.constants.CityState;
import dextrous.kor.evv.korevv.model.QuestionModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.Config;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.NotificationUtils;
import dextrous.kor.evv.korevv.util.ProjectUtil;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplaseActivity extends AppCompatActivity implements InstallStateUpdatedListener {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    LoggedInUser loggedInUser;
    DatabaseHandler db;
    boolean isConnected;
    private static final String TAG = SplaseActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int MY_REQUEST_CODE = 100;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splase);

        loggedInUser = new LoggedInUser(getApplicationContext());
        db = new DatabaseHandler(SplaseActivity.this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);

        ConnectivityManager cm =
                (ConnectivityManager) SplaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (isConnected) {

                    // MyToast.showToast(getApplicationContext(),"connected");
                    hitApi("1", ApiUrl.system_token);
                    if (loggedInUser.getLocal_language().equals("")) {
                        getAllQuestions("en");
                    } else {
                        getAllQuestions(loggedInUser.getLocal_language());
                    }
                    Log.v("isCheckin", "not present + connect");
                    if (!loggedInUser.getLocal_language().equals("")) {
                        if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size() > 0) {
                            Log.v("isCheckin", "not present +" + String.valueOf(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size()));
                            for (int j = 0; j < db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size(); j++) {
                                String sch_id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(j).getSch_id();
                                String[] parts = db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_location().split(",");
                                String client_lat = parts[0];
                                String client_long = parts[1];
                                postClockIn1(sch_id,
                                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_date(),
                                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_time(),
                                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason_location(),
                                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason(),
                                        client_lat,
                                        client_long,
                                        0);
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

                } else {
                    // MyToast.showToast(getApplicationContext(),"discon");
                    
                    if (MyPreferences.getInstance(SplaseActivity.this).getISLOGIN()) {
                        Intent intent = new Intent(SplaseActivity.this,
                                MainActivity.class);
                        intent.putExtra("isConnected", isConnected);
                        startActivity(intent);
                        //       overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }else {
                        Toast.makeText(SplaseActivity.this, "Network is required for first login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, 3000);


        if (loggedInUser.getLocal_language().equals("")) {
            getLanguageResponse("en");

        } else {
            getLanguageResponse(loggedInUser.getLocal_language());

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

        if (isConnected) {
            appUpdateManager = AppUpdateManagerFactory.create(SplaseActivity.this);

// Returns an intent object that you use to check for an update.
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    // Request the update.

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                IMMEDIATE,
                                // The current activity making the update request.
                                this,
                                // Include a request code to later monitor this update request.
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        displayFirebaseRegId();
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

    private void displayFirebaseRegId() {
        String newToken = "";
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplaseActivity.this, new OnSuccessListener<InstanceIdResult>() {
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

        Log.e(TAG, "Firebase reg id: " + regId);
//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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
    private void hitApi(final String TAG, String Url) {
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            MyLog.showLog(response.toString());
                            JSONObject object = new JSONObject(response);

                            if (object.has("status") &&
                                    object.getString("status")
                                            .equalsIgnoreCase(ApiTagConstants.SUCCESS)) {

                                if (TAG.equalsIgnoreCase(ApiTagConstants.SESSION_IN)) {

                                    MyPreferences.getInstance(SplaseActivity.this).setSESSION_TOKEN(object.getString("token"));
                                    // MyPreferences.getInstance(LoginActivity.this).setLANGUAGE(object.getString("language"));
                                    loggedInUser.setSession_toekn(object.getString("token"));
                                    startActivity(new Intent(SplaseActivity.this,
                                            LoginActivity.class));
                                    //       overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }


                            } else if (object.has("status") &&
                                    object.getString("status")
                                            .equals(ApiTagConstants.FAILURE)) {
                                MyToast.showToast(SplaseActivity.this,
                                        object.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ProjectUtil.showErrorResponse(SplaseActivity.this, error);
                    }
                }
        );
        ProjectUtil.setRequest(SplaseActivity.this, postRequest);
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
                            //dialog.stopProgress();

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
    public void getAllQuestions(String lang) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.questionList + "/" + lang,
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


                            db.clearQuestionTable();

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
    public void postClockIn1(String sch_id, final String startDate,
                             String startTime,String clockInReasonLocation,String clockInReson, String client_lat, String client_long, final int position) {

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
                            // String status=jsonObject.getString("status");
                            // String message=jsonObject.getString("message");


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
                        ProjectUtil.showErrorResponse(SplaseActivity.this, error);

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
                params.put("checkinLocation",clockInReasonLocation);
                params.put("checkinReason",clockInReson);
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
        ProjectUtil.setRequest(SplaseActivity.this, stringRequest);
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

                            String action;
                            Intent intent = new Intent(SplaseActivity.this,

                                    LoginActivity.class);
                            intent.putExtra("isConnected", isConnected);
                            startActivity(intent);
                            //       overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                            String billId = jsonObject.getString("billId");

                            //  db.clearSELECTEDLIST();
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
                        ProjectUtil.showErrorResponse(SplaseActivity.this, error);
                        Intent intent = new Intent(SplaseActivity.this,
                                LoginActivity.class);
                        intent.putExtra("isConnected", isConnected);
                        startActivity(intent);
                        //       overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
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
        ProjectUtil.setRequest(SplaseActivity.this, stringRequest);
    }
}

