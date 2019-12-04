package dextrous.kor.evv.korevv.retrofit;

import android.content.Context;
import android.widget.Toast;

import dextrous.kor.evv.korevv.model.DocumentUploadModel;
import dextrous.kor.evv.korevv.model.RespiteUnitCalModel;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class UploadEntraTime {
    private Context context;
    private CustomProgressDialog dialog;
    private String  extraTime, extraTimeDate;

    public UploadEntraTime(Context context, String extraTime, String extraTimeDate) {
        this.context = context;
        this.extraTime = extraTime;
        this.extraTimeDate = extraTimeDate;
        getDataFromApi();
    }

    private void getDataFromApi() {
        dialog = new CustomProgressDialog();
        dialog.startProgress(context);
        Call<RespiteUnitCalModel> uploadDocument = AppUtill.callRetrofit(context, false).uploadExtraMinut(
                MyPreferences.getInstance(context).getUSERID(), MyPreferences.getInstance(context).getUSER_TOKEN(), extraTime,
                extraTimeDate);
        uploadDocument.enqueue(new Callback<RespiteUnitCalModel>() {
            @Override
            public void onResponse(Call<RespiteUnitCalModel> call, Response<RespiteUnitCalModel> response) {
                dialog.stopProgress();
                if (response.body() != null)
                    getResponce(response.body());
            }

            @Override
            public void onFailure(Call<RespiteUnitCalModel> call, Throwable t) {
                dialog.stopProgress();
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public abstract void getResponce(RespiteUnitCalModel uploadDocmodel);
}