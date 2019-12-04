package dextrous.kor.evv.korevv.retrofit;

import android.content.Context;

import java.util.PrimitiveIterator;

import dextrous.kor.evv.korevv.activity.DocumentUploadSuccess;
import dextrous.kor.evv.korevv.model.DocumentCategoryListModel;
import dextrous.kor.evv.korevv.model.DocumentModel;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DocCategoryListApiCall {
    private Context context;
    private CustomProgressDialog dialog;

    public DocCategoryListApiCall(Context context) {
        this.context = context;
        callApi();
    }

    private void callApi() {
        dialog = new CustomProgressDialog();
        dialog.startProgress(context);
        Call<DocumentCategoryListModel> docTyperesponce = AppUtill.callRetrofit(context, false).getDocCategoryList(
                MyPreferences.getInstance(context).getUSERID(), MyPreferences.getInstance(context).getUSER_TOKEN());
        docTyperesponce.enqueue(new Callback<DocumentCategoryListModel>() {
            @Override
            public void onResponse(Call<DocumentCategoryListModel> call, Response<DocumentCategoryListModel> response) {
                dialog.stopProgress();
                getResponce(response.body());
            }

            @Override
            public void onFailure(Call<DocumentCategoryListModel> call, Throwable t) {
                dialog.stopProgress();
            }
        });
    }

    public abstract void getResponce(DocumentCategoryListModel documentCategoryListModel);
}
