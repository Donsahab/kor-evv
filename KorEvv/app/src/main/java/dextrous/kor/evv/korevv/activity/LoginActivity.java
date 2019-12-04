package dextrous.kor.evv.korevv.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.constants.ApiTagConstants;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.constants.CityState;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.ConnectivityReceiver;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.FingerprintHandler;
import dextrous.kor.evv.korevv.util.MyApplication;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextView finger_text, forgot_password, sign_button, login_text;
    EditText et_agency, et_password, et_email;
    CustomProgressDialog dialog;
    LinearLayout remember_me_layout;
    AppCompatCheckBox rememberme_check;
    LoggedInUser loggedInUser;
    boolean isConnected = true;
    ParseJasonLang parseJasonLang;
    private boolean isValid = false;
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "korevv";
    private Cipher cipher;
    void getId() {
        login_text = findViewById(R.id.login_text);

        finger_text = (TextView) findViewById(R.id.finger_text);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        sign_button = (TextView) findViewById(R.id.sign_button);
        et_agency = findViewById(R.id.et_agency);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        remember_me_layout = findViewById(R.id.remember_me_layout);
        rememberme_check = findViewById(R.id.rememberme_check);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isConnected = bundle.getBoolean("isConnected");
        }
        getId();
        loggedInUser = new LoggedInUser(LoginActivity.this);

        parseJasonLang = new ParseJasonLang(LoginActivity.this);


        login_text.setText(parseJasonLang.getJsonToString("Login"));
        et_agency.setHint(parseJasonLang.getJsonToString("Agency"));
        et_email.setHint(parseJasonLang.getJsonToString("Email"));
        et_password.setHint(parseJasonLang.getJsonToString("Password"));
        rememberme_check.setText(parseJasonLang.getJsonToString("Remember_Me"));
        sign_button.setText(parseJasonLang.getJsonToString("Login"));
        forgot_password.setText(parseJasonLang.getJsonToString("Forgot_password"));
        finger_text.setText(parseJasonLang.getJsonToString("Login_with_fingerprint"));


        if (isConnected) {
            hitApiToken("1", ApiUrl.system_token);
        }
        finger_text.setPaintFlags(finger_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgot_password.setPaintFlags(forgot_password.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (!MyPreferences.getInstance(getApplicationContext()).getEMAIL().equals("")) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();

        }
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ForgotPassword.class);
                intent.putExtra("isConnected", isConnected);

                startActivity(intent);
                //       overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });
        et_agency.setText(loggedInUser.getEmail_seeker());
        et_email.setText(loggedInUser.getEmail());
        et_password.setText(loggedInUser.getPassword());
        ConnectivityManager cm =
                (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (checkValidation()) {
                    if (isConnected) {
                        hitApi("login", ApiUrl.Login);
                    } else {
                        if (checkValidation()) {
                            if (loggedInUser.getLocal_email().equals(et_email.getText().toString().trim())) {
                                if (loggedInUser.getLocal_password().equals(et_password.getText().toString().trim())) {
                                    if (loggedInUser.getLocal_agency().equals(et_agency.getText().toString().trim())) {
                                        if (rememberme_check.isChecked()) {
                                            loggedInUser.setEmail(et_email.getText().toString().trim());
                                            loggedInUser.setPassword(et_password.getText().toString().trim());
                                            loggedInUser.setEmail_seeker(et_agency.getText().toString().trim());
                                        } else {

                                            loggedInUser.setEmail("");
                                            loggedInUser.setPassword("");
                                            loggedInUser.setEmail_seeker("");
                                        }
                                        MyToast.showToast(getApplicationContext(), parseJasonLang.getJsonToString("logged_in_successfully"));
                                        startActivity(new Intent(LoginActivity.this,
                                                MainActivity.class));
                                        //       overridePendingTransition(R.anim.enter, R.anim.exit);
                                        finish();
                                    }else {
                                        MyToast.showToast(getApplicationContext(),"Invalid AgencyID");
                                    }
                                } else {
                                    MyToast.showToast(getApplicationContext(), "Invalid Password");
                                }
                            } else {
                                MyToast.showToast(getApplicationContext(), "Invalid Email");
                            }
                        }
                    }

                }
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Do something for lollipop and above versions
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {

                if (!fingerprintManager.isHardwareDetected()) {
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * Example:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
                    finger_text.setText("Your Device does not have a Fingerprint Sensor");
                } else {


                    // Checks whether fingerprint permission is set on manifest
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        finger_text.setText("Fingerprint authentication permission not enabled");
                    } else {
                        // Check whether at least one fingerprint is registered
                        if (!fingerprintManager.hasEnrolledFingerprints()) {
                            finger_text.setText("Register at least one fingerprint in Settings");
                        } else {
                            // Checks whether lock screen security is enabled or not
                            if (!keyguardManager.isKeyguardSecure()) {
                                finger_text.setText("Lock screen security not enabled in Settings");
                            } else {

                                generateKey();

                                if (cipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler helper = new FingerprintHandler(this);
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                }

                            }
                        }

                    }
                }


                // Check whether the device has a Fingerprint sensor.
            } else {
                // do something for phones running an SDK before lollipop
            }
        }
// Initializing both Android Keyguard Manager and Fingerprint Manager


        LoggedInUser loggedInUser = new LoggedInUser(LoginActivity.this);
        ParseJasonLang parseJasonLang = new ParseJasonLang(LoginActivity.this);


    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

    }
    private boolean checkValidation() {
        isValid = true;
        if (et_email.getText().toString()
                .equalsIgnoreCase("")) {
            isValid = false;
            et_email.requestFocus();
            ProjectUtil.showSoftKeyboard(this);
            MyToast.showToast(this,
                    parseJasonLang.getJsonToString("plz_enter_email"));

        } else if (!(ProjectUtil.isValidEmail(
                et_email.getText().toString())) && et_email.getText().toString().length() < 10) {
            isValid = false;
            et_email.requestFocus();
            ProjectUtil.showSoftKeyboard(this);
            MyToast.showToast(this,
                    parseJasonLang.getJsonToString("email_validation"));

        } else if (et_password.getText().toString()
                .equalsIgnoreCase("")) {
            isValid = false;
            et_password.requestFocus();
            ProjectUtil.showSoftKeyboard(this);
            MyToast.showToast(this, parseJasonLang.getJsonToString("plz_enter_pass"));

        } else if (et_password.getText().toString().length() < 6) {
            isValid = false;
            et_password.requestFocus();
            ProjectUtil.showSoftKeyboard(this);
            MyToast.showToast(this,
                    parseJasonLang.getJsonToString("pass_validation"));
        } else if (et_agency.getText().toString()
                .equalsIgnoreCase("")) {
            isValid = false;
            et_agency.requestFocus();
            ProjectUtil.showSoftKeyboard(this);
            MyToast.showToast(this, parseJasonLang.getJsonToString("plz_enter_agency"));

        }
        return isValid;
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

                                if (TAG.equalsIgnoreCase(ApiTagConstants.LOGIN)) {

                                    MyPreferences.getInstance(LoginActivity.this)
                                            .setISLOGIN(true);

                                    MyPreferences.getInstance(LoginActivity.this).setUSER_TOKEN(object.getString("user_token"));
                                    loggedInUser.setLocal_session_toekn(object.getString("user_token"));
                                    MyPreferences.getInstance(getApplicationContext()).setNOTIFICATION("1");
                                    //  MyToast.showToast(getApplicationContext(),);

                                    MyPreferences.getInstance(LoginActivity.this).setHOLDER_NAME(object.getString("agencyId"));
                                    loggedInUser.setAgency_id(object.getString("agencyId"));
                                    // MyPreferences.getInstance(LoginActivity.this).setBANK_NAME(object.getString("bankName"));
                                    // MyPreferences.getInstance(LoginActivity.this).setACCOUNT_NUMBER(object.getString("accountNo"));
                                    // MyPreferences.getInstance(LoginActivity.this).setEDUCATION(object.getString("education"));
                                    // MyPreferences.getInstance(LoginActivity.this).setEXPERIENCE(object.getString("experience"));
                                    // MyPreferences.getInstance(LoginActivity.this).setSERVICE_CATEGORY(object.getString("serviceCategory"));
                                    MyPreferences.getInstance(LoginActivity.this).setLANGUAGE(object.getString("appLanguage"));
                                    loggedInUser.setLocal_language(object.getString("appLanguage"));
                                    MyPreferences.getInstance(LoginActivity.this).setFIRSTNAME(object.getString("firstName"));
                                    MyPreferences.getInstance(LoginActivity.this).setLASTNAME(object.getString("lastName"));
                                    MyPreferences.getInstance(LoginActivity.this).setUSERID(object.getString("userId"));
                                    loggedInUser.setLocal_name(object.getString("firstName") + " " + object.getString("lastName"));
                                    loggedInUser.setLocal_user_id(object.getString("userId"));
                                    MyPreferences.getInstance(LoginActivity.this).setEMAIL(object.getString("email"));
                                    loggedInUser.setLocal_email(object.getString("email"));
                                    //  MyPreferences.getInstance(LoginActivity.this).setMOBILE(object.getString("mobile"));
                                    MyPreferences.getInstance(LoginActivity.this).setUSER_SCHOOL_NAME(object.getString("agency"));
                                    loggedInUser.setLocal_agency(object.getString("agency"));
                                    loggedInUser.setLocal_password(et_password.getText().toString().trim());
                                    MyPreferences.getInstance(LoginActivity.this).setIMAGE(object.getString("image"));
                                    loggedInUser.setLocal_image(object.getString("image"));
                                    //MyPreferences.getInstance(LoginActivity.this).setBIOGRAPHY(object.getString("biography"));
                                    MyToast.showToast(LoginActivity.this,
                                            object.getString("message"));
                                    if (loggedInUser.getLocal_language().equals("")) {
                                        getLanguageResponse(object.getString("appLanguage"));
                                    } else {
                                        getLanguageResponse(object.getString("appLanguage"));
                                    }

                                    //  MyPreferences.getInstance(LoginActivity.this).setUSER_STATE(object.getString("district"));
                                    // MyPreferences.getInstance(LoginActivity.this).setUSER_CITY(object.getString("location"));
                                    // MyPreferences.getInstance(LoginActivity.this).setLANGUAGE(object.getString("language"));

                                    if (rememberme_check.isChecked()) {
                                        loggedInUser.setEmail(et_email.getText().toString().trim());
                                        loggedInUser.setPassword(et_password.getText().toString().trim());
                                        loggedInUser.setEmail_seeker(et_agency.getText().toString().trim());
                                    } else {
                                        loggedInUser.setEmail("");
                                        loggedInUser.setPassword("");
                                        loggedInUser.setEmail_seeker("");
                                    }
                                }

                            } else if (object.has("status") &&
                                    object.getString("status")
                                            .equals(ApiTagConstants.FAILURE)) {
                                dialog.stopProgress();
                                MyToast.showToast(LoginActivity.this,
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
                        dialog.stopProgress();
                        ProjectUtil.showErrorResponse(LoginActivity.this, error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (TAG.equalsIgnoreCase(ApiTagConstants.LOGIN)) {
//                    params.put("option", ApiTagConstants.LOGIN);
                    params.put("emailId", et_email.getText().toString());
                    params.put("password", et_password.getText().toString());
                    params.put("agencyId", et_agency.getText().toString().trim());
                    if (MyPreferences.getInstance(getApplicationContext()).getSESSION_TOKEN().equals("")) {
                        params.put("token", loggedInUser.getSession_toekn());
                    } else {
                        params.put("token", MyPreferences.getInstance(getApplicationContext()).getSESSION_TOKEN());
                    }
                }

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
        ProjectUtil.setRequest(LoginActivity.this, postRequest);
    }
    private void hitApiToken(final String TAG, String Url) {


        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            MyLog.showLog(response.toString());
                            JSONObject object = new JSONObject(response);

                            if (object.has("status") &&
                                    object.getString("status")
                                            .equalsIgnoreCase(ApiTagConstants.SUCCESS)) {

                                if (TAG.equalsIgnoreCase(ApiTagConstants.SESSION_IN)) {

                                    MyPreferences.getInstance(LoginActivity.this).setSESSION_TOKEN(object.getString("token"));
                                    // MyPreferences.getInstance(LoginActivity.this).setLANGUAGE(object.getString("language"));


                                }


                            } else if (object.has("status") &&
                                    object.getString("status")
                                            .equals(ApiTagConstants.FAILURE)) {
                                MyToast.showToast(LoginActivity.this,
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

                        ProjectUtil.showErrorResponse(LoginActivity.this, error);
                    }
                }
        );
        ProjectUtil.setRequest(LoginActivity.this, postRequest);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
    }
    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
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

                            startActivity(new Intent(LoginActivity.this,
                                    MainActivity.class));
                            //       overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
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
}
