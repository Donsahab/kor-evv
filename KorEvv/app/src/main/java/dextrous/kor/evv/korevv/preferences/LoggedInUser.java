package dextrous.kor.evv.korevv.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by yash on 7/13/2017.
 */

public class LoggedInUser {
    Context context;
    SharedPreferences sharedPreferences;

   private String lang_array[];
   String language_response,session_toekn,email,password,email_seeker,password_seeker,local_email,local_password,local_agency,local_session_toekn,local_user_id,local_name,
           local_image,local_language,agency_id,isNotificationOn,BASE_URL;
   int poition;

    public String[] getLang_array() {
        lang_array[poition]=sharedPreferences.getString("lang_array","");
        return lang_array;
    }
    public void setLang_array(String[] lang_array, int poition) {
        this.poition=poition;
        this.lang_array = lang_array;
        sharedPreferences.edit().putString("lang_array",lang_array[poition]).commit();
    }

    public String getEmail() {
        email=sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).commit();
    }


    public String getBASE_URL() {
        BASE_URL=sharedPreferences.getString("BASE_URL","");
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        sharedPreferences.edit().putString("BASE_URL",BASE_URL).commit();
    }


    public String getIsNotificationOn() {
        isNotificationOn=sharedPreferences.getString("isNotificationOn","");
        return isNotificationOn;
    }

    public void setIsNotificationOn(String isNotificationOn) {
        this.isNotificationOn = isNotificationOn;
        sharedPreferences.edit().putString("isNotificationOn",isNotificationOn).commit();
    }


    public String getAgency_id() {
        agency_id=sharedPreferences.getString("agency_id","");
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
        sharedPreferences.edit().putString("agency_id",agency_id).commit();
    }



    public String getLanguage_response() {
        language_response=sharedPreferences.getString("language_response","");
        return language_response;
    }

    public void setLanguage_response(String language_response) {
        this.language_response = language_response;
        sharedPreferences.edit().putString("language_response",language_response).commit();
    }


    public String getSession_toekn() {
        session_toekn=sharedPreferences.getString("session_toekn","");
        return session_toekn;
    }

    public void setSession_toekn(String session_toekn) {
        this.session_toekn = session_toekn;
        sharedPreferences.edit().putString("session_toekn",session_toekn).commit();
    }

    public String getLocal_language() {
        local_language=sharedPreferences.getString("local_language","");
        return local_language;
    }

    public void setLocal_language(String local_language) {
        this.local_language = local_language;
        sharedPreferences.edit().putString("local_language",local_language).commit();
    }


    public String getLocal_email() {
        local_email=sharedPreferences.getString("local_email","");
        return local_email;
    }

    public void setLocal_email(String local_email) {
        this.local_email = local_email;
        sharedPreferences.edit().putString("local_email",local_email).commit();
    }


    public String getLocal_password() {
        local_password=sharedPreferences.getString("local_password","");
        return local_password;
    }

    public void setLocal_password(String local_password) {
        this.local_password = local_password;
        sharedPreferences.edit().putString("local_password",local_password).commit();
    }

    public String getLocal_name() {
        local_name=sharedPreferences.getString("local_name","");
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
        sharedPreferences.edit().putString("local_name",local_name).commit();
    }


    public String getLocal_image() {
        local_image=sharedPreferences.getString("local_image","");
        return local_image;
    }

    public void setLocal_image(String local_image) {
        this.local_image = local_image;
        sharedPreferences.edit().putString("local_image",local_image).commit();
    }



    public String getLocal_agency() {
        local_agency=sharedPreferences.getString("local_agency","");
        return local_agency;
    }

    public void setLocal_agency(String local_agency) {
        this.local_agency = local_agency;
        sharedPreferences.edit().putString("local_agency",local_agency).commit();
    }


    public String getLocal_user_id() {
        local_user_id=sharedPreferences.getString("local_user_id","");
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
        sharedPreferences.edit().putString("local_user_id",local_user_id).commit();
    }


    public String getLocal_session_toekn() {
        local_session_toekn=sharedPreferences.getString("local_session_token","");
        return local_session_toekn;
    }

    public void setLocal_session_toekn(String local_session_toekn) {
        this.local_session_toekn = local_session_toekn;
        sharedPreferences.edit().putString("local_session_token",local_session_toekn).commit();
    }



    public String getPassword() {
        password=sharedPreferences.getString("password","");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("password",password).commit();
    }


    public String getEmail_seeker() {
        email_seeker=sharedPreferences.getString("email_seeker","");
        return email_seeker;
    }

    public void setEmail_seeker(String email_seeker) {
        this.email_seeker = email_seeker;
        sharedPreferences.edit().putString("email_seeker",email_seeker).commit();
    }

    public String getPassword_seeker() {
        password_seeker=sharedPreferences.getString("password_seeker","");
        return password_seeker;
    }

    public void setPassword_seeker(String password_seeker) {
        this.password_seeker = password_seeker;
        sharedPreferences.edit().putString("password_seeker",password_seeker).commit();
    }

    private String user_type;

    public LoggedInUser(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }

    public void logOut(Context context)
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
