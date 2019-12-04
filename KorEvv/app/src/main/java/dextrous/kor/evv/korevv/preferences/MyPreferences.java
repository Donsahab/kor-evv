package dextrous.kor.evv.korevv.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by Dextrous on 12/16/2017.
 */

public class MyPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static MyPreferences object = null;

    private final String USER_PREFS = "user_prefs";
    private final String USERID = "userid";
    private final String SIGNTYPE = "signtype";
    private final String USERNAME = "username";
    private final String FIRSTNAME = "firstname";
    private final String LASTNAME = "lastname";
    private final String SERVICE_CATEGORY="service_category";
    private final String LANGUAGE="language";
    private final String BIOGRAPHY="biography";
    private final String USER_FATHER_NAME = "user_father_name";
    private final String USER_SCHOOL_NAME="user_school_name";
    private final String USER_CLASS="user_class";
    private final String USER_IMAGE="user_image";
    private final String USER_CITY="user_city";
    private final String USER_TOKEN="user_token";
    private final String USER_CLASS_ID="user_class_id";
    private final String USER_STATE_ID="user_state_id";
    private final String CART_ITEM="cart_item";
    private final String USER_CITY_ID="user_city_ID";
    private final String USER_STATE="user_state";
    private final String IS_SOCIAL="is_social";
    private final String EMAIL = "email";
    private final String MOBILE = "mobile";
    private final String TITLE_VALUE="title_value";
    private final String TITLE_ID = "title_id";
    private final String IMAGE = "image";
    private final String SOCIAL_ID = "social_id";
    private final String SESSION_TOKEN = "session_token";
    private final String ISLOGIN = "isLogin";
    private final String PREFERENCE = "preference";
    private final String RESULTID="result_id";
    private final String USER_ROLE="user_role";
    private final String TOKEN_TYPE="token_type";
    private final String STRIPE_ACCOUNT="stripe_account";

    private final String SAVE_EMAIL="save_email";
    private final String SAVE_PASSWORD="save_password";

    private final String BANK_NAME = "bank_name";
    private final String HOLDER_NAME = "holder_name";
    private final String ACCOUNT_NUMBER="account_number";
    private final String EDUCATION="education";
    private final String EXPERIENCE="experience";
    private final String NOTIFICATION="notification";
    private final String STOPWATCHTIME = "stopwatchtime";
    private final String STARTTIME ="starttime";
    private final String STARTDATE = "startdate";
    private final String PERSONALCARETIME ="personalcaretime";
    private final String RESPITECARETIME = "respitecaretime";
    private final String RESPITECARETIMEVALUE = "respitecaretimevalue";
    private final String RESPITECARETIMEDATE = "respitecaretimedate";

    public void setRespitecaretimeDate(String respitecaretimevalue) {
        editor.putString(RESPITECARETIMEDATE, respitecaretimevalue).apply();
    }

    public String getRespitecaretimeDate() {
        return sharedPreferences.getString(RESPITECARETIMEDATE, "0");
    }

    public void setRespitecaretimevalue(String respitecaretimevalue) {
        editor.putString(RESPITECARETIMEVALUE, respitecaretimevalue).apply();
    }

    public String getRespitecaretimevalue() {
        return sharedPreferences.getString(RESPITECARETIMEVALUE, "0");
    }

    public void setStartDate(String startdate) {
        editor.putString(STARTDATE, startdate).apply();
    }

    public String getStartDate() {
        return sharedPreferences.getString(STARTDATE, "");
    }

    public void setPesonalCareTime(String personalCareTime) {
        editor.putString(PERSONALCARETIME, personalCareTime).apply();
    }

    public String getPersonalCareTime() {
        return sharedPreferences.getString(PERSONALCARETIME, "");
    }

    public void setRespiteCareTime(String respiteCareTime) {
        editor.putString(RESPITECARETIME, respiteCareTime).apply();
    }

    public String getRespiteCareTime() {
        return sharedPreferences.getString(RESPITECARETIME, "");
    }

    public long getStartTime() {
     return   sharedPreferences.getLong(STARTTIME,0);
    }

    public void setStartTime(long starttime) {
        sharedPreferences.edit().putLong(STARTTIME,starttime).apply();
    }

    public long getStopWatchTime() {
        return  sharedPreferences.getLong(STOPWATCHTIME,0);
    }

    public void setStopWatchTime(long time) {
          sharedPreferences.edit().putLong(STOPWATCHTIME,time).apply();
    }

    public MyPreferences(Context context) {
        try {
            sharedPreferences = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
            this.editor = sharedPreferences.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyPreferences getInstance(Context context) {
        if (object == null) {
            return new MyPreferences(context);
        } else {
            return object;
        }
    }

    public void setISLOGIN(boolean login) {
        editor.putBoolean(ISLOGIN, login).apply();
    }

    public boolean getISLOGIN() {
        return sharedPreferences.getBoolean(ISLOGIN, false);
    }

    public void setUSERID(String userid) {
        editor.putString(USERID, userid).apply();
    }

    public String getUSERID() {
        return sharedPreferences.getString(USERID, "");
    }

    public void setSTRIPE_ACCOUNT(String stripe_account) {
        editor.putString(STRIPE_ACCOUNT, stripe_account).apply();
    }

    public String getSTRIPE_ACCOUNT() {
        return sharedPreferences.getString(STRIPE_ACCOUNT, "");
    }



    public void setUSER_TOKEN(String user_token) {
        editor.putString(USER_TOKEN, user_token).apply();
    }

    public String getUSER_TOKEN() {
        return sharedPreferences.getString(USER_TOKEN, "");
    }



    public void setSAVE_EMAIL(String save_email) {
        editor.putString(SAVE_EMAIL, save_email).apply();
    }

    public String getSAVE_EMAIL() {
        return sharedPreferences.getString(SAVE_EMAIL, "");
    }

    public void setSAVE_PASSWORD(String save_password) {
        editor.putString(SAVE_PASSWORD, save_password).apply();
    }

    public String getSAVE_PASSWORD() {
        return sharedPreferences.getString(SAVE_PASSWORD, "");
    }

    public void setNOTIFICATION(String notification) {
        editor.putString(NOTIFICATION, notification).apply();
    }

    public String getNOTIFICATION() {
        return sharedPreferences.getString(NOTIFICATION, "");
    }


    public void setSOCIAL_ID(String social_id) {
        editor.putString(SOCIAL_ID, social_id).apply();
    }

    public String getSOCIAL_ID() {
        return sharedPreferences.getString(SOCIAL_ID, "");
    }


    public void setUSER_ROLE(String user_role) {
        editor.putString(USER_ROLE, user_role).apply();
    }

    public String getUSER_ROLE() {
        return sharedPreferences.getString(USER_ROLE, "");
    }



    public void setUSERNAME(String username) {
        editor.putString(USERNAME, username).apply();
    }

    public String getUSERNAME() {
        return sharedPreferences.getString(USERNAME, "");
    }

    public void setLANGUAGE(String language) {
        editor.putString(LANGUAGE, language).apply();
    }

    public String getLANGUAGE() {
        return sharedPreferences.getString(LANGUAGE, "");
    }

    public void setBIOGRAPHY(String biography) {
        editor.putString(BIOGRAPHY, biography).apply();
    }

    public String getBIOGRAPHY() {
        return sharedPreferences.getString(BIOGRAPHY, "");
    }


    public void setACCOUNT_NUMBER(String account_number) {
        editor.putString(ACCOUNT_NUMBER, account_number).apply();
    }

    public String getACCOUNT_NUMBER() {
        return sharedPreferences.getString(ACCOUNT_NUMBER, "");
    }


    public void setBANK_NAME(String bank_name) {
        editor.putString(BANK_NAME, bank_name).apply();
    }

    public String getBANK_NAME() {
        return sharedPreferences.getString(BANK_NAME, "");
    }


    public void setHOLDER_NAME(String holder_name) {
        editor.putString(HOLDER_NAME, holder_name).apply();
    }

    public String getHOLDER_NAME() {
        return sharedPreferences.getString(HOLDER_NAME, "");
    }


    public void setEDUCATION(String education) {
        editor.putString(EDUCATION, education).apply();
    }

    public String getEDUCATION() {
        return sharedPreferences.getString(EDUCATION, "");
    }


    public void setEXPERIENCE(String experience) {
        editor.putString(EXPERIENCE, experience).apply();
    }

    public String getEXPERIENCE() {
        return sharedPreferences.getString(EXPERIENCE, "");
    }



    public void setLASTNAME(String lastname) {
        editor.putString(LASTNAME, lastname).apply();
    }

    public String getLASTNAME() {
        return sharedPreferences.getString(LASTNAME, "");
    }


    public void setSERVICE_CATEGORY(String service_category) {
        editor.putString(SERVICE_CATEGORY, service_category).apply();
    }

    public String getSERVICE_CATEGORY() {
        return sharedPreferences.getString(SERVICE_CATEGORY, "");
    }

    public void setFIRSTNAME(String firstname) {
        editor.putString(FIRSTNAME, firstname).apply();
    }

    public String getFIRSTNAME() {
        return sharedPreferences.getString(FIRSTNAME, "");
    }



    public void setUSER_FATHER_NAME(String user_father_name) {
        editor.putString(USER_FATHER_NAME, user_father_name).apply();
    }

    public String getUSER_FATHER_NAME() {
        return sharedPreferences.getString(USER_FATHER_NAME, "");
    }

    public void setCART_ITEM(String cart_item) {
        editor.putString(CART_ITEM, cart_item).apply();
    }

    public String getCART_ITEM() {
        return sharedPreferences.getString(CART_ITEM, "");
    }


    public void setUSER_SCHOOL_NAME(String user_school_name) {
        editor.putString(USER_SCHOOL_NAME, user_school_name).apply();
    }

    public String getUSER_SCHOOL_NAME() {
        return sharedPreferences.getString(USER_SCHOOL_NAME, "");
    }

    public void setTITLE_VALUE(String title_value) {
        editor.putString(TITLE_VALUE, title_value).apply();
    }

    public String getTITLE_VALUE() {
        return sharedPreferences.getString(TITLE_VALUE, "");
    }


    public void setTITLE_ID(String title_id) {
        editor.putString(TITLE_ID, title_id).apply();
    }

    public String getTITLE_ID() {
        return sharedPreferences.getString(TITLE_ID, "");
    }


    public void setRESULTID(String resultid) {
        editor.putString(RESULTID, resultid).apply();
    }

    public String getRESULTID() {
        return sharedPreferences.getString(RESULTID, "");
    }



    public void setIMAGE(String image) {
        editor.putString(IMAGE, image).apply();
    }

    public String getIMAGE() {
        return sharedPreferences.getString(IMAGE, "");
    }




    public void setUSER_CLASS(String user_class) {
        editor.putString(USER_CLASS, user_class).apply();
    }

    public String getUSER_CLASS() {
        return sharedPreferences.getString(USER_CLASS, "");
    }



    public void setUSER_CITY(String user_city) {
        editor.putString(USER_CITY, user_city).apply();
    }

    public String getUSER_CITY() {
        return sharedPreferences.getString(USER_CITY, "");
    }


    public void setUSER_STATE(String user_state) {
        editor.putString(USER_STATE, user_state).apply();
    }

    public String getUSER_STATE() {
        return sharedPreferences.getString(USER_STATE, "");
    }


    public void setUSER_IMAGE(String user_image) {
        editor.putString(USER_IMAGE, user_image).apply();
    }

    public String getUSER_IMAGE() {
        return sharedPreferences.getString(USER_IMAGE, "");
    }


    public void setMOBILE(String mobile) {
        editor.putString(MOBILE, mobile).apply();
    }

    public String getMOBILE() {
        return sharedPreferences.getString(MOBILE, "");
    }


    public void setUSER_CLASS_ID(String user_class_id) {
        editor.putString(USER_CLASS_ID, user_class_id).apply();
    }

    public String getUSER_CLASS_ID() {
        return sharedPreferences.getString(USER_CLASS_ID, "");
    }


    public void setUSER_STATE_ID(String user_state_id) {
        editor.putString(USER_STATE_ID, user_state_id).apply();
    }

    public String getUSER_STATE_ID() {
        return sharedPreferences.getString(USER_STATE_ID, "");
    }


    public void setIS_SOCIAL(String is_social) {
        editor.putString(IS_SOCIAL, is_social).apply();
    }

    public String getIS_SOCIAL() {
        return sharedPreferences.getString(IS_SOCIAL, "");
    }


    public void setUSER_CITY_ID(String user_city_id) {
        editor.putString(USER_CITY_ID, user_city_id).apply();
    }

    public String getUSER_CITY_ID() {
        return sharedPreferences.getString(USER_CITY_ID, "");
    }




    public void setEMAIL(String email) {
        editor.putString(EMAIL, email).apply();
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void setSIGNTYPE(String signtype) {
        editor.putString(SIGNTYPE, signtype).apply();
    }

    public String getSIGNTYPE() {
        return sharedPreferences.getString(SIGNTYPE, "");
    }


    public void setSESSION_TOKEN(String session_token) {
        editor.putString(SESSION_TOKEN, session_token).apply();
    }

    public String getSESSION_TOKEN() {
        return sharedPreferences.getString(SESSION_TOKEN, "");
    }

    public void setPREFERENCE(String preference) {
        editor.putString(PREFERENCE, preference).apply();
    }

    public void deleteSharedPrefrenceByKey(){
        sharedPreferences.edit().remove(STOPWATCHTIME).commit();
    }
    public String getPREFERENCE() {
        return sharedPreferences.getString(PREFERENCE, "");
    }

    public  void logOut(Context context)
    {
        sharedPreferences.edit().clear().commit();
        deleteCache(context);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
