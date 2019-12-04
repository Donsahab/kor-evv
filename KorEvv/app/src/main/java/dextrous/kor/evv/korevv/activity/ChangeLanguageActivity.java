package dextrous.kor.evv.korevv.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.LangAdapter;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.constants.CityState;
import dextrous.kor.evv.korevv.model.LangModel;
import dextrous.kor.evv.korevv.model.QuestionModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

public class ChangeLanguageActivity extends AppCompatActivity implements LangAdapter.SingleClickListener{

    RelativeLayout select_language_layout;
    LoggedInUser loggedInUser;
    LinearLayout sign_button,ll1;
    RecyclerView recyclerView_lang;
    String lang="";
    ArrayList<LangModel> langModelArrayList = new ArrayList<>();
    private CustomProgressDialog dialog;
    LangAdapter adapter;
    public static boolean isConnected=true;
    TextView title,select_lang_text,save_language;
    ParseJasonLang parseJasonLang;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        setContentView(R.layout.activity_change_language);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            isConnected=bundle.getBoolean("isConnected");
        }
        loggedInUser= new LoggedInUser(getApplicationContext());
        parseJasonLang = new ParseJasonLang(ChangeLanguageActivity.this);
        db=new DatabaseHandler(ChangeLanguageActivity.this);
        select_language_layout=findViewById(R.id.select_language_layout);
        recyclerView_lang=findViewById(R.id.recyclerView_lang);
        sign_button=findViewById(R.id.sign_button);
        ll1=findViewById(R.id.ll1);
        title=findViewById(R.id.title);
        select_lang_text=findViewById(R.id.select_lang_text);
        save_language=findViewById(R.id.save_language);

        title.setText(parseJasonLang.getJsonToString("change_language"));

        select_lang_text.setText(parseJasonLang.getJsonToString("select_language"));

        save_language.setText(parseJasonLang.getJsonToString("Save"));



        select_language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   english_layout.setVisibility(View.VISIBLE);
              //  korean_layout.setVisibility(View.VISIBLE);
            }
        });

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)ChangeLanguageActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!lang.equals("")&&isConnected){
                    getLanguageResponse(lang);
                   // getAllQuestions(lang);
                }


            }
        });
        ImageView login_back=findViewById(R.id.login_back);
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action;
                ConnectivityManager cm =
                        (ConnectivityManager)ChangeLanguageActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();


                finish();
            }
        });
        adapter = new LangAdapter(ChangeLanguageActivity.this, langModelArrayList);
        adapter.setOnItemClickListener(this);
        getService_category("");
    }

    public void getLanguageResponse(String lang){


        CityState.service_cat_list =new HashMap<String,String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.label_language+"/"+lang+"/"+loggedInUser.getLocal_user_id(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();

                            Log.v("stateArray",String.valueOf(response));
                            loggedInUser.setLanguage_response(String.valueOf(response));
                            loggedInUser.setLocal_language(lang);


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("isConnected",isConnected);
                            startActivity(intent);
                            finish();
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray",String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray",String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    public void getService_category(String lang){

        langModelArrayList.clear();
        langModelArrayList.clear();
        dialog = new CustomProgressDialog();
        dialog.startProgress(ChangeLanguageActivity.this);


        CityState.service_cat_list =new HashMap<String,String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.appLanguage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            dialog.stopProgress();
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();



                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            // JSONArray heroArray = new JSONArray(response);
                            Log.v("stateArray",String.valueOf(response));

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String id=heroObject.getString("id");
                                String value=heroObject.getString("value");
                                String images=heroObject.getString("images");
                                LangModel langModel = new LangModel(id,value,images);
                                langModelArrayList.add(langModel);

                            }

                            if(langModelArrayList.isEmpty()){
                                ll1.setVisibility(View.VISIBLE);
                                recyclerView_lang.setVisibility(View.GONE);
                            }else {
                                ll1.setVisibility(View.GONE);
                                recyclerView_lang.setVisibility(View.VISIBLE);
                                recyclerView_lang.setHasFixedSize(true);
                                recyclerView_lang.setLayoutManager(new LinearLayoutManager(ChangeLanguageActivity.this));
                                //creating recyclerview adapter
                                adapter = new LangAdapter(ChangeLanguageActivity.this, langModelArrayList);
                                //setting adapter to recyclerview
                                recyclerView_lang.setAdapter(adapter);

                            }
                            db.clearServiceList();
                            db.clearSELECTEDLIST();
                            db.clearSCHEDULELIST();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.stopProgress();
                            Log.v("stateArray",String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        dialog.stopProgress();
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray",String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        adapter.selectedItem();
        lang=langModelArrayList.get(position).getId();


    }

    @Override
    public void onBackPressed() {
        ConnectivityManager cm =
                (ConnectivityManager)ChangeLanguageActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        finish();
    }

    public void getAllQuestions(String lang){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, loggedInUser.getBASE_URL()+"questionList"+"/"+lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();


                            db.clearQuestionTable();


                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            // JSONArray heroArray = new JSONArray(response);
                            Log.v("stateArray",String.valueOf(response));

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String question=heroObject.getString("question");

                                db.addQuestion(new QuestionModel(question));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray",String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getApplicationContext(), error);
                        Log.v("stateArray",String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getApplicationContext(), stringRequest);
    }
}
