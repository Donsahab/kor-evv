package dextrous.kor.evv.korevv.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.NotificationAdapter;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.model.NotificationModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;
import dextrous.kor.evv.korevv.util.RecyclerItemTouchHelper;

import static dextrous.kor.evv.korevv.activity.MainActivity.noti_count;

public class NotificationActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    ImageView imageSwitcher;
    Button nextButton;
    private static final String TAG = NotificationActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int imageSwitcherImages[] =
            {R.drawable.switch_off, R.drawable.switch_on};

    int switcherImageLength = imageSwitcherImages.length;
    int counter = 1;
    private CustomProgressDialog dialog;
    List<NotificationModel> productList = new ArrayList<>();
    LinearLayout ll1;
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    Animation aniOut,aniIn;
    ImageView back_button,home_button;
    LoggedInUser loggedInUser;
    public static boolean isConnected=true;
    ParseJasonLang parseJasonLang;
    TextView noti_setting,title;
    private String isDoc,date;
    private String docId,docTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        setContentView(R.layout.activity_notification);

        imageSwitcher = findViewById(R.id.off_on_button);
        title=findViewById(R.id.title);
        noti_setting=findViewById(R.id.noti_setting);
        parseJasonLang= new ParseJasonLang(NotificationActivity.this);
        //   nextButton = (Button) findViewById(R.id.button);

        aniOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        aniIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        back_button=(ImageView)findViewById(R.id.back_button) ;
        // home_button=(ImageView)findViewById(R.id.home_button);
        noti_setting.setText(parseJasonLang.getJsonToString("notification_settings"));
        title.setText(parseJasonLang.getJsonToString("notification"));

        loggedInUser= new LoggedInUser(getApplicationContext());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager cm =
                        (ConnectivityManager) NotificationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("isConnected",isConnected);
                startActivity(intent);
                finish();
            }
        });

        home_button=findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("isConnected",isConnected);
                startActivity(intent);
                finish();
            }
        });

        if(loggedInUser.getIsNotificationOn().equals("")){
            loggedInUser.setIsNotificationOn("1");
        }
        ll1=findViewById(R.id.ll1);
        setRecyclerData();

        //  hitApi();
        if(loggedInUser.getIsNotificationOn().equals("1")){
            imageSwitcher.setImageResource(R.drawable.green_switch);
            hitApi(ApiUrl.notification+"/"+loggedInUser.getLocal_user_id());
        }else {
            imageSwitcher.setImageResource(R.drawable.red_switch);
            hitApi("");
        }
        hitApi();
        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) NotificationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected) {
                    if (loggedInUser.getIsNotificationOn().equals("1")) {
                       // imageSwitcher.setAnimation(aniIn);
                        imageSwitcher.setImageResource(R.drawable.red_switch);
                        hitApi("");
                        loggedInUser.setIsNotificationOn("0");
                    } else if (loggedInUser.getIsNotificationOn().equals("0")) {
                       // imageSwitcher.setAnimation(aniOut);
                        imageSwitcher.setImageResource(R.drawable.green_switch);
                        loggedInUser.setIsNotificationOn("1");
                        hitApi(ApiUrl.notification + "/" + loggedInUser.getLocal_user_id());
                    }
                }else {
                    MyToast.showToast(NotificationActivity.this,parseJasonLang.getJsonToString("please_connect_internet"));
                }
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSwitcher =  findViewById(R.id.off_on_button);
        //   nextButton = (Button) findViewById(R.id.button);

        aniOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        aniIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        back_button=findViewById(R.id.back_button) ;
        // home_button=(ImageView)findViewById(R.id.home_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("isConnected",isConnected);
                startActivity(intent);
                finish();
            }
        });

        home_button=findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("isConnected",isConnected);
                startActivity(intent);
                finish();
            }
        });









        ll1=(LinearLayout)findViewById(R.id.ll1);
        setRecyclerData();

        //  hitApi();
        if(loggedInUser.getIsNotificationOn().equals("1")){
            imageSwitcher.setImageResource(R.drawable.green_switch);
            hitApi(ApiUrl.notification+"/"+loggedInUser.getLocal_user_id());
        }else {
            imageSwitcher.setImageResource(R.drawable.red_switch);
            hitApi("");
        }
        hitApi();
        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedInUser.getIsNotificationOn().equals("1")){
                    //imageSwitcher.setAnimation(aniIn);
                    imageSwitcher.setImageResource(R.drawable.red_switch);
                    hitApi("");
                    loggedInUser.setIsNotificationOn("0");
                }
                else if(loggedInUser.getIsNotificationOn().equals("0")){
                   // imageSwitcher.setAnimation(aniOut);
                    imageSwitcher.setImageResource(R.drawable.green_switch);
                    loggedInUser.setIsNotificationOn("1");
                    hitApi(ApiUrl.notification +"/"+loggedInUser.getLocal_user_id());
                }
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void setRecyclerData(){


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));

    }

    private void hitApi(String url) {
        dialog = new CustomProgressDialog();
        dialog.startProgress(NotificationActivity.this);
        productList.clear();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.stopProgress();
                            MyLog.showLog(response.toString());
                            JSONObject jsonObject1=new JSONObject(response);
                            if(jsonObject1.has("notification")){
                                JSONArray object = jsonObject1.getJSONArray("notification");
                                recyclerView.setVisibility(View.VISIBLE);
                                ll1.setVisibility(View.GONE);
                                for (int i = 0; i < object.length(); i++) {
                                    JSONObject jsonObject = object.getJSONObject(i);
                                    //  String title=jsonObject.getString("title");
                                    String notifi,view_status,id;
                                    if(jsonObject.has("notifi")) {
                                         notifi = jsonObject.getString("notifi");
                                    }else {
                                        notifi="";
                                    }
                                    if(jsonObject.has("view_status")) {
                                        view_status = jsonObject.getString("view_status");
                                    }else {
                                        view_status="";
                                    }
                                    if(jsonObject.has("id")) {
                                        id = jsonObject.getString("id");
                                    }else {
                                        id="";
                                    }

                                    if(jsonObject.has("is_doc")) {
                                        isDoc = jsonObject.getString("is_doc");
                                    }else {
                                        isDoc="";
                                    }
                                    if(jsonObject.has("created_on")) {
                                        date = jsonObject.getString("created_on");
                                    }else {
                                        date="";
                                    }

                                    if(jsonObject.has("doc_id")) {
                                        docId = jsonObject.getString("doc_id");
                                    }else {
                                        docId="";
                                    }
                                    if(jsonObject.has("doc_type")) {
                                        docTypeId = jsonObject.getString("doc_type");
                                    }else {
                                        docTypeId="";
                                    }
                                  //  String view_status = jsonObject.getString("view_status");
                                   // String id=jsonObject.getString("id");
                                    // String job_id=jsonObject.getString("job_id");
                                    // String noti_rele=jsonObject.getString("noti_rele");
                                    //  String provider=jsonObject.getString("provider");
                                    //  String seeker=jsonObject.getString("seeker");

                                    NotificationModel dashboardModel = new NotificationModel(notifi,view_status,id,"",
                                            "","","",isDoc,date,docId,docTypeId);
                                    productList.add(dashboardModel);

                                }
                                if (productList != null && productList.size() > 0) {
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setHasFixedSize(true);
                                    //creating recyclerview adapter
                                    adapter = new NotificationAdapter(getApplicationContext(), productList,"");
                                    dialog.stopProgress();
                                    //setting adapter to recyclerview
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    dialog.stopProgress();
                                    recyclerView.setVisibility(View.GONE);
                                    ll1.setVisibility(View.VISIBLE);

                                }
                            }else {
                                dialog.stopProgress();
                                recyclerView.setVisibility(View.GONE);
                                ll1.setVisibility(View.VISIBLE);
                            }



//                            if (object.getString("status").
//                                    equalsIgnoreCase(ApiTagConstants.SUCCESS)) {
//
//                                if (TAG.equalsIgnoreCase(ApiTagConstants.DASHBOARD)) {
//                                    setData(object);
//                                }
//
//                            } else if (object.get("status").
//                                    equals(ApiTagConstants.FAILURE)) {
//                                MyToast.showToast(mainActivity,
//                                        object.getString("message"));
//
//                            } else if (object.get("status").
//                                    equals(ApiTagConstants.SESSION_OUT)) {
//                                MyToast.showToast(mainActivity,
//                                        object.getString("message"));
//                                MyPreferences.getInstance(mainActivity).setISLOGIN(false);
//                                startActivity(new Intent(mainActivity, LoginActivity.class));
//                                mainActivity.overridePendingTransition(
//                                        R.anim.enter, R.anim.exit);
//                                mainActivity.finish();
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.stopProgress();
                            recyclerView.setVisibility(View.GONE);
                            ll1.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.stopProgress();
                ProjectUtil.showErrorResponse(getApplicationContext(), error);
                recyclerView.setVisibility(View.GONE);

                ll1.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", loggedInUser.getLocal_user_id());

//
//                params.put("holderName", et_bank_number.getText().toString().trim());
//                params.put("language", lang);
//                params.put("experience", tot_exp);
//                params.put("serviceCategory", service_cat);
//                params.put("profileImg",encoded_image);
//
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
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.ProductViewHolder) {
            // get the removed item name to display it in snack bar
            String name = productList.get(viewHolder.getAdapterPosition()).getNotifi();

            // backup of removed item for undo purpose
            final NotificationModel deletedItem = productList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            postReply(deletedItem.getId());
            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }

    private void hitApi() {


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUrl.userInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            MyLog.showLog(response.toString());

                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.getString("status").equals("1")) {

                                String count= jsonObject.getString("count");
                                noti_count.setText(count);
                            }else {
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
                params.put("userId",loggedInUser.getLocal_user_id());

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

    private void postReply(final String id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUrl.view_notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            MyLog.showLog(response.toString());
                            JSONObject jsonObject1=new JSONObject(response);
                            if(jsonObject1.getString("status").equals("1")){
                                // Toast.makeText(mCtx,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                                hitApi();

                            }else {

                                // Toast.makeText(mCtx,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                            }

//                            adapter.notifyDataSetChanged();



//

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProjectUtil.showErrorResponse(NotificationActivity.this, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("delete", "1");
                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        ProjectUtil.setRequest(NotificationActivity.this, stringRequest);
    }

    @Override
    public void onBackPressed() {
        ConnectivityManager cm =
                (ConnectivityManager) NotificationActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("isConnected",isConnected);
        startActivity(intent);
        finish();
    }
}
