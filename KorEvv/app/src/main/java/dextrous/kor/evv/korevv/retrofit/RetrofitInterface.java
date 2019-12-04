package dextrous.kor.evv.korevv.retrofit;

import dextrous.kor.evv.korevv.constants.ApiTagConstants;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.model.DocumentCategoryListModel;
import dextrous.kor.evv.korevv.model.DocumentModel;
import dextrous.kor.evv.korevv.model.DocumentUploadModel;
import dextrous.kor.evv.korevv.model.RespiteUnitCalModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST(ApiUrl.GET_DOCUMENTS)
    Call<DocumentModel> getDocumentsList(@Field("userId")  String userId,
                                         @Field("token")  String token);
    @FormUrlEncoded
    @POST(ApiUrl.GET_DOCUMENTS_TYPE)
    Call<DocumentCategoryListModel> getDocCategoryList(@Field("userId")  String userId,
                                                       @Field("token")  String token);

    @FormUrlEncoded
    @POST(ApiUrl.UPLOAD_DOCUMENT)
    Call<DocumentUploadModel> uploadDocument(@Field("userId")  String userId,
                                             @Field("token")  String token,
                                             @Field("doc_type")  String doctype,
                                             @Field("doc_issue_date")  String issueDate,
                                             @Field("doc_expire_date")  String expiryDate,
                                             @Field("doc_name")  String document,
                                             @Field("doc_ext")  String docExtention,
                                             @Field("doc_id")  String docId);

    @FormUrlEncoded
    @POST(ApiUrl.UPLOAD_EXTRA_MINUT)
    Call<RespiteUnitCalModel> uploadExtraMinut(@Field("userId")  String userId,
                                               @Field("token")  String token,
                                               @Field("extra_respite_time")  String extraTime,
                                               @Field("extra_respite_time_date")  String extraTimeDate);
}
