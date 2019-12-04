package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.DocumentUploadActivity;
import dextrous.kor.evv.korevv.activity.MainActivity;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.model.NotificationModel;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.MyToast;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ProductViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<NotificationModel> productList;
    String job_id;
    private CustomProgressDialog dialog;
    public static boolean isConnected = true;
    ParseJasonLang parseJasonLang;

    //getting the context and product list with constructor
    public NotificationAdapter(Context mCtx, List<NotificationModel> productList, String job_id) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.job_id = job_id;
        parseJasonLang = new ParseJasonLang(mCtx);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_notification, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final NotificationModel product = productList.get(position);

        //binding the data with the viewholder views

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.name_tv.setText(Html.fromHtml(product.getNotifi(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.name_tv.setText(Html.fromHtml(product.getNotifi()));
        }

        if (productList.get(position).getIsDoc().equalsIgnoreCase("0"))
        holder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppUtill.isNetworkAvil(mCtx)) {
                    postReply(product.getId(), holder,false,null,null);
                } else {
                    MyToast.showToast(mCtx, parseJasonLang.getJsonToString("please_connect_internet"));
                }
//                if(!product.getJob_id().equals("0")) {
//                    if(product.getNoti_rele().equals("1")) {
//
//                    }
//                    if(product.getNoti_rele().equals("2")) {
//                        Intent intent = new Intent(mCtx, SeekerJobsDetailActivity.class);
//                        intent.putExtra("jobId", product.getJob_id());
//                        intent.putExtra("type", "0");
//                        intent.putExtra("code_value", "");
//                        intent.putExtra("providerId",product.getProvider());
//                        mCtx.startActivity(intent);
//                    }
//                    if(product.getNoti_rele().equals("3")) {
//                        Intent intent = new Intent(mCtx, ProviderAcceptedJobs.class);
//                        intent.putExtra("jobId", product.getJob_id());
//                        intent.putExtra("type", "1");
//                        intent.putExtra("code_value", "");
//                        mCtx.startActivity(intent);
//                    }
//                    if(product.getNoti_rele().equals("4")) {
//                        Intent intent = new Intent(mCtx, SeekerJobsDetailActivity.class);
//                        intent.putExtra("jobId", product.getJob_id());
//                        intent.putExtra("type", "1");
//                        intent.putExtra("providerId",product.getProvider());
//                        intent.putExtra("code_value", "");
//                        mCtx.startActivity(intent);
//                    }
//                    if(product.getNoti_rele().equals("5")) {
//                        Intent intent = new Intent(mCtx, PaymentHistoryDetails.class);
//                        intent.putExtra("jobId", product.getJob_id());
//                        intent.putExtra("type", "2");
//                        intent.putExtra("providerId",product.getProvider());
//                        intent.putExtra("userId",product.getSeeker());
//                        intent.putExtra("code_value", "");
//                        mCtx.startActivity(intent);
//                    }
//
//                    if(product.getNoti_rele().equals("6")) {
//                        if(MyPreferences.getInstance(mCtx).getUSER_ROLE().equals("2")){
//                        Intent intent = new Intent(mCtx, JobDetailsActivity.class);
//                        intent.putExtra("jobId", product.getJob_id());
//                        intent.putExtra("type", "1");
//                        intent.putExtra("providerId",product.getProvider());
//                        intent.putExtra("userId",product.getSeeker());
//                        intent.putExtra("code_value", "");
//                        mCtx.startActivity(intent);
//                        }else {
//                            Intent intent = new Intent(mCtx, SeekerJobsDetailActivity.class);
//                            intent.putExtra("jobId", product.getJob_id());
//                            intent.putExtra("type", "1");
//                            intent.putExtra("providerId",product.getProvider());
//                            intent.putExtra("userId",product.getSeeker());
//                            intent.putExtra("code_value", "");
//                            mCtx.startActivity(intent);
//                        }
//                    }
//                }
            }
        });

        if (product.getView_status().equals("1")) {
            holder.rl1.setBackgroundResource(R.drawable.edit_text);
        }
        if (product.getView_status().equals("0")) {
            holder.rl1.setBackgroundResource(R.drawable.edit_text2);
        }
        if (productList.get(position).getIsDoc().equalsIgnoreCase("1")){
            holder.view.setVisibility(View.GONE);
            holder.doc_notification_layout.setVisibility(View.VISIBLE);
        }else {
            holder.view.setVisibility(View.VISIBLE);
            holder.name_tv.setVisibility(View.VISIBLE);
            holder.doc_notification_layout.setVisibility(View.GONE);
        }

        holder.updateDocument.setText(parseJasonLang.getJsonToString("update_document"));
        holder.updateDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtill.isNetworkAvil(mCtx)) {
                    postReply(product.getId(), holder,true, productList.get(position).getDocId(),productList.get(position).getDocTypeId());
                } else {
                    MyToast.showToast(mCtx, parseJasonLang.getJsonToString("please_connect_internet"));
                }
            }
        });

        holder.tvDate.setText(AppUtill.convertDateFormate(productList.get(position).getDate()));
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView name_tv, times_tv, cooommet, textViewPrice, reply_tv;
        private CircleImageView profile_image;
        private RelativeLayout rl1;
        public RelativeLayout viewBackground, viewForeground;
        public Button updateDocument;
        public TextView tvDate;
        public View view;
        public LinearLayout doc_notification_layout;


        public ProductViewHolder(View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.text_noti);
            rl1 = itemView.findViewById(R.id.rl1);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.rl1);
            updateDocument = itemView.findViewById(R.id.update_document);
            tvDate = itemView.findViewById(R.id.tv_date);
            view = itemView.findViewById(R.id.view);
            doc_notification_layout = itemView.findViewById(R.id.doc_notification_layout);
        }
    }

    private void postReply(final String id, final ProductViewHolder holder,boolean isDoc,String docId,String docTypeId) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiUrl.view_notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            MyLog.showLog(response.toString());
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("status").equals("1")) {
                                // Toast.makeText(mCtx,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                                holder.rl1.setBackgroundResource(R.drawable.edit_text);

                            } else {
                                holder.rl1.setBackgroundResource(R.drawable.edit_text2);
                                // Toast.makeText(mCtx,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                            }


                            if (isDoc){
                                Intent intent = new Intent(mCtx, DocumentUploadActivity.class);
                                intent.putExtra("docid", docId);
                                intent.putExtra("doctypeid", docTypeId);
                                intent.putExtra("isdoc", isDoc);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(intent);
                            }else {

                                Intent intent = new Intent(mCtx, MainActivity.class);
//                intent.putExtra("jobId", product.getJob_id());
//                intent.putExtra("type", "0");
//                intent.putExtra("code_value", "");
                                intent.putExtra("isConnected", AppUtill.isNetworkAvil(mCtx));
                               // intent.putExtra("isdoc", isDoc);

                                //naveen
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(intent);
                            }

                            //adapter.notifyDataSetChanged();

                            //MyLog.showLog(params.toString());

//

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProjectUtil.showErrorResponse(mCtx, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        ProjectUtil.setRequest(mCtx, stringRequest);
    }

    public void removeItem(int position) {
        productList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationModel item, int position) {
        productList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
