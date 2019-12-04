package dextrous.kor.evv.korevv.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.constants.ApiTagConstants;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class ChangePassword extends AppCompatActivity {
    CustomProgressDialog dialog;
    private EditText et_agency,et_email,et_old_pass;
    LinearLayout sign_button;
    LoggedInUser loggedInUser;
    TextView title,login_text,save;
    ParseJasonLang parseJasonLang;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        setContentView(R.layout.activity_change_password);
        ImageView login_back=findViewById(R.id.login_back);

        loggedInUser = new LoggedInUser(ChangePassword.this);
        parseJasonLang = new ParseJasonLang(ChangePassword.this);

        et_agency=findViewById(R.id.et_agency);
        et_email=findViewById(R.id.et_email);
        et_old_pass=findViewById(R.id.et_old_pass);
        sign_button=findViewById(R.id.sign_button);
        title=findViewById(R.id.title);
        login_text=findViewById(R.id.login_text);
        save=findViewById(R.id.save);

        title.setText(parseJasonLang.getJsonToString("change_password"));
        login_text.setText(parseJasonLang.getJsonToString("change_password"));
        save.setText(parseJasonLang.getJsonToString("Save"));

        et_agency.setHint(parseJasonLang.getJsonToString("Password"));
        et_email.setHint(parseJasonLang.getJsonToString("confirm_password"));
        et_old_pass.setHint(parseJasonLang.getJsonToString("old_pass"));


        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)ChangePassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                MainActivity.isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                finish();
            }
        });

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_agency.getText().toString().equals("")){
                    MyToast.showToast(ChangePassword.this,parseJasonLang.getJsonToString("please_enter_new_password"));
                }else {
                    if(et_email.getText().toString().equals("")) {
                        MyToast.showToast(ChangePassword.this,parseJasonLang.getJsonToString("please_enter_confirm_password"));
                    }else {
                        if(et_old_pass.getText().toString().trim().equals("")){
                            MyToast.showToast(ChangePassword.this,parseJasonLang.getJsonToString("please_enter_old_password"));
                        }else {
                            if (et_agency.getText().toString().trim().equals(et_email.getText().toString().trim())) {

                                hitApi("", ApiUrl.changePwd);
                            }else {
                                MyToast.showToast(ChangePassword.this,parseJasonLang.getJsonToString("password_not_matched"));
                            }
                        }
                    }
                }
            }
        });
    }


    private void hitApi(final String TAG, String Url) {
        dialog = new CustomProgressDialog();
        dialog.startProgress(this);

        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.stopProgress();
                            MyLog.showLog(response.toString());
                            JSONObject object = new JSONObject(response);

                            if (object.has("status") &&
                                    object.getString("status")
                                            .equalsIgnoreCase(ApiTagConstants.SUCCESS)) {


                                    startActivity(new Intent(ChangePassword.this,
                                            MainActivity.class));
                                    //       overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();

                                }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.stopProgress();
                        ProjectUtil.showErrorResponse(ChangePassword.this, error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                    params.put("userId", loggedInUser.getLocal_user_id());
                    params.put("oldPwd", et_old_pass.getText().toString());
                    params.put("newPwd", et_email.getText().toString());
//                    if(MyPreferences.getInstance(getApplicationContext()).getSESSION_TOKEN().equals("")){
//                        params.put("token", loggedInUser.getSession_toekn());
//                    }else {
//                        params.put("token", MyPreferences.getInstance(getApplicationContext()).getSESSION_TOKEN());
//                    }


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
        ProjectUtil.setRequest(ChangePassword.this, postRequest);
    }

    @Override
    public void onBackPressed() {
        ConnectivityManager cm =
                (ConnectivityManager)ChangePassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        MainActivity.isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        finish();
    }
}
