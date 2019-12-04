package dextrous.kor.evv.korevv.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Dextrous on 11/24/2017.
 */

public class MyToast {

    public static void showToast(Context context, String string) {
        Toast.makeText(context, "" + string, Toast.LENGTH_LONG).show();
    }
}
