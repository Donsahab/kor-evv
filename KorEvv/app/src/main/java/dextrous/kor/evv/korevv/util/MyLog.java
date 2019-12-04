package dextrous.kor.evv.korevv.util;

import android.util.Log;

/**
 * Created by Dextrous on 2/22/2018.
 */

public class MyLog {

    public static void showLog(String json) {
        Log.e("@@@@@", json.toString());
    }

    public static void showLog(String TAG, String json) {
        Log.e(TAG, json.toString());
    }
}
