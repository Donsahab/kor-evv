package dextrous.kor.evv.korevv.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.api_content.AppController;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.preferences.MyPreferences;


/**
 * Created by Rakesh on 19-04-2017.
 */

public class ProjectUtil {

    private static AlertDialog.Builder dialog;
    public static int startrecord = 1;
    public static int endrecord = 10;

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean mobileValidator(String mobile) {
        Pattern pattern;
        Matcher matcher;
        final String Mobile_PATTERN = "^[0-9]*$";
        pattern = Pattern.compile(Mobile_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void replaceFragment(Context context, Fragment fragment) {
        try {
            String backStackName = fragment.getClass().getName();
            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
            boolean flag = fm.popBackStackImmediate(backStackName, 0);

            if (!flag) {
                fm.beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack(backStackName).commit();
            } else {
                fm.popBackStackImmediate(backStackName, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearBackStack(Context context) {
        FragmentManager fm = ((AppCompatActivity) context)
                .getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fm.getBackStackEntryAt(0);
            fm.popBackStack(first.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void closeFragment(Context context) {
        ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
    }

    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

    public static void showAlertDialog(Context context, String message) {
        try {
            dialog = new AlertDialog.Builder(context, R.style.alertDialog);
            dialog.setMessage(message);
//            dialog.setCancelable(false);
            dialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } catch (Exception e) {
        }
    }

    public static void setFontFaceEdit(Context context, EditText editText) {
        Typeface type = Typeface.createFromAsset(context.getAssets(),
                "Royal Crescent demo.otf");
        editText.setTypeface(type);
    }

    public static void setFontFace(Context context, TextView textView) {
        Typeface type = Typeface.createFromAsset(context.getAssets(),
                "Royal Crescent demo.otf");
        textView.setTypeface(type);
    }

    public static void showErrorResponse(Context context, VolleyError error) {
        try {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                MyToast.showToast(context, "Time out: No Connection found");

            } else if (error instanceof AuthFailureError) {
                MyToast.showToast(context, "Authentication failure");

            } else if (error instanceof ServerError) {
                MyToast.showToast(context, "Server Error");

            } else if (error instanceof NetworkError) {
                MyToast.showToast(context, "Network Error");

            } else if (error instanceof ParseError) {
                MyToast.showToast(context, "Json Parsing Error");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void sessionOut(Context context, JSONObject object) {
        try {
            MyToast.showToast(context, object.getString("message"));
            MyPreferences.getInstance(context).setISLOGIN(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void setRequest(Context context, StringRequest postRequest) {
        postRequest.setShouldCache(false);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(ApiUrl.API_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance(context).addToRequestQueue(postRequest);
    }

//    public static String findJSONFromUrl(String url) {
//        String result = "";
//        try {
//            int TIMEOUT_MILLISEC = 100000; // = 10 seconds
//            HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(url);
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            int status = httpResponse.getStatusLine().getStatusCode();
//
//            InputStream inputStream = httpResponse.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//
//            }
//
//            inputStream.close();
//            result = sb.toString();
////            Log.e("result in jsonparser", result);
//
//        } catch (Exception e) {
//            Log.e("exception in jsonparser", "class");
//            e.printStackTrace();
//        }
//        return result;
//    }
}
