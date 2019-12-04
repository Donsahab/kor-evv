package dextrous.kor.evv.korevv.retrofit;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import dextrous.kor.evv.korevv.model.DocumentModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallDocumentApi {
    private Context context;
    private CustomProgressDialog dialog;

    public CallDocumentApi(Context context) {
        this.context = context;
        callApi();
    }

    private void callApi() {

        dialog = new CustomProgressDialog();
        dialog.startProgress(context);
        Call<DocumentModel> docresponce = AppUtill.callRetrofit(context, false).getDocumentsList(
                MyPreferences.getInstance(context).getUSERID(), MyPreferences.getInstance(context).getUSER_TOKEN());

        docresponce.enqueue(new Callback<DocumentModel>() {
            @Override
            public void onResponse(Call<DocumentModel> call, Response<DocumentModel> response) {
              //  Toast.makeText(context, "Responce"+response, Toast.LENGTH_SHORT).show();
                dialog.stopProgress();
                getDocurmntResponce(response.body());
            }

            @Override
            public void onFailure(Call<DocumentModel> call, Throwable t) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                dialog.stopProgress();
            }
        });
    }

    public abstract void getDocurmntResponce(DocumentModel documentModels);
}
