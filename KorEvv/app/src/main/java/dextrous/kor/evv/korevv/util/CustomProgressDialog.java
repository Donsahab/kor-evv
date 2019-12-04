package dextrous.kor.evv.korevv.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import dextrous.kor.evv.korevv.R;


/**
 * Created by my on 6/20/2016.
 */
public class CustomProgressDialog {
    private Dialog dialog;

    public void startProgress(Context context) {
        if ((dialog == null)) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }

        if (!(dialog.isShowing())) {
            dialog.show();
        }

    }
    public void stopProgress() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
