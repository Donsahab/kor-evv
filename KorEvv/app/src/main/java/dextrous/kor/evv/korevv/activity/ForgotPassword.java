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
import android.widget.Toast;

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
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class ForgotPassword extends AppCompatActivity {

    ImageView login_back;
    TextView title,login_text,forgot_passwordText;
    EditText et_agency,et_email;
    private LinearLayout sign_button;
    ParseJasonLang parseJasonLang;
    CustomProgressDialog dialog;
    boolean isConnected=true;

    void getID(){
        login_back=findViewById(R.id.login_back);
        title=findViewById(R.id.title);
        login_text=findViewById(R.id.login_text);
        forgot_passwordText=findViewById(R.id.forgot_passwordText);
        et_email=findViewById(R.id.et_email);
        et_agency=findViewById(R.id.et_agency);
        sign_button=findViewById(R.id.sign_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        setContentView(R.layout.activity_forgot_password);
        parseJasonLang = new ParseJasonLang(ForgotPassword.this);
        getID();


        title.setText(parseJasonLang.getJsonToString("Forgot_password"));
        forgot_passwordText.setText(parseJasonLang.getJsonToString("Forgot_password"));
        login_text.setText(parseJasonLang.getJsonToString("Forgot_password"));

        et_email.setHint(parseJasonLang.getJsonToString("Email"));
        et_agency.setHint(parseJasonLang.getJsonToString("Agency"));

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)ForgotPassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(et_agency.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),parseJasonLang.getJsonToString("please_enter_agency_id"),Toast.LENGTH_LONG).show();
                }else {
                    if(et_email.getText().toString().trim().equals("")){
                        MyToast.showToast(getApplicationContext(),parseJasonLang.getJsonToString("please_enter_email_id"));
                    }else {
                        hitApi("", ApiUrl.verify);
                    }
                }


            }
        });


        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)ForgotPassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                Intent intent =new Intent(ForgotPassword.this,
                        LoginActivity.class);
                intent.putExtra("isConnected",isConnected);

                startActivity(intent);
                //       overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        ConnectivityManager cm =
                (ConnectivityManager)ForgotPassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Intent intent =new Intent(ForgotPassword.this,
                LoginActivity.class);
        intent.putExtra("isConnected",isConnected);

        startActivity(intent);
        //       overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    private void hitApi(final String TAG, String Url) {
        dialog = new CustomProgressDialog();
        dialog.startProgress(this);

        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                "http://admin.korevv.com/api/verify",
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

                                MyToast.showToast(ForgotPassword.this,object.getString("message"));

                                    Intent intent =new Intent(ForgotPassword.this,
                                            LoginActivity.class);
                                    intent.putExtra("isConnected",isConnected);

                                    startActivity(intent);
                                    //       overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();



                            }else {
                                MyToast.showToast(ForgotPassword.this,object.getString("message"));
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
                        ProjectUtil.showErrorResponse(ForgotPassword.this, error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                    params.put("forgotPwd", "1");
                    params.put("emailID", et_email.getText().toString().trim());
                    //params.put("password", et_password.getText().toString());
                    params.put("agencyId", et_agency.getText().toString().trim());


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
        ProjectUtil.setRequest(ForgotPassword.this, postRequest);
    }
}
