package dextrous.kor.evv.korevv.retrofit;

import android.content.Context;
import android.widget.Toast;

import dextrous.kor.evv.korevv.model.DocumentCategoryListModel;
import dextrous.kor.evv.korevv.model.DocumentUploadModel;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class UploadDocumentApiCall {
    private Context context;
    private CustomProgressDialog dialog;
    private String docType,issueDate,expiryDate,document,docExtention,docId;

    public UploadDocumentApiCall(Context context,String docType, String issueDate,String expiryDate,String document,String docExtention,String docId) {
        this.context = context;
        this.docType = docType;
        this.issueDate = AppUtill.changeDateFormateNew(issueDate);
        this.expiryDate = AppUtill.changeDateFormateNew(expiryDate);
        this.document = document;
        this.docExtention = docExtention;
        this.docId = docId;
        getDataFromApi();
    }

    private void getDataFromApi() {
        dialog = new CustomProgressDialog();
        dialog.startProgress(context);
        Call<DocumentUploadModel> uploadDocument = AppUtill.callRetrofit(context, false).uploadDocument(
                MyPreferences.getInstance(context).getUSERID(), MyPreferences.getInstance(context).getUSER_TOKEN(),docType,
                AppUtill.changeDateFormate(expiryDate) , AppUtill.changeDateFormate(issueDate),document,docExtention,docId);
        uploadDocument.enqueue(new Callback<DocumentUploadModel>() {
            @Override
            public void onResponse(Call<DocumentUploadModel> call, Response<DocumentUploadModel> response) {
                dialog.stopProgress();
                if (response.body() != null && response.body().getResult().getStatus())
                    getResponce(response.body());
            }

            @Override
            public void onFailure(Call<DocumentUploadModel> call, Throwable t) {
                dialog.stopProgress();
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public abstract void getResponce(DocumentUploadModel uploadDocmodel);
}
