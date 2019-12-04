package dextrous.kor.evv.korevv.fragment;


import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.ScheduleActivity;
import dextrous.kor.evv.korevv.adapter.BillingAdapter;
import dextrous.kor.evv.korevv.adapter.DateAdapter1;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.model.BillingModel;
import dextrous.kor.evv.korevv.model.DateModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;

import static dextrous.kor.evv.korevv.activity.MainActivity.isConnected;
import static dextrous.kor.evv.korevv.activity.MainActivity.main_title;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingFragment extends Fragment implements DateAdapter1.SingleClickListener, View.OnClickListener {

    private static int lastCheckedPos = 0;
    private CustomProgressDialog dialog;
    private List<BillingModel> scheduleModelArrayList = new ArrayList<>();
    private RecyclerView date_recycler,recyclerView;
    private String month="",year="";
    private TextView text_month_year;
    String array_year[]=new String[2];
    private static String months[] =
            {
                  "January" , "February" , "March" , "April", "May",
                    "June", "July", "August", "September", "October",
                    "November", "December"
            };
    private LinearLayoutManager mLayoutManager;
    private DateAdapter1 mAdapter;
    private ArrayList<DateModel> dateModelArrayList= new ArrayList<>();
    private LoggedInUser loggedInUser;
    private DatabaseHandler db ;
    private DateFormat dateFormat2,dateFormat1,dateFormat;
    private Date date,date1,date2;
    private ParseJasonLang parseJasonLang;
    private View x;
    private LinearLayout ll1,llCheckInternet;
    private TextView tryAgain;
    LoggedInUser loggedInuser;
    private  String month1="";
    TextView no_data_text;
    private ImageView previousScrollImage, nextScrollImage;

    public BillingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        x=inflater.inflate(R.layout.fragment_billing, container, false);
        MyLog.showLog("MyFragment", this.getClass().getSimpleName());
        date_recycler=x.findViewById(R.id.date_recycler);
        text_month_year=x.findViewById(R.id.text_month_year);
        //next or previous image
        previousScrollImage = x.findViewById(R.id.left_date_scroll);
        nextScrollImage = x.findViewById(R.id.right_date_scroll);
        previousScrollImage.setOnClickListener(this);
        nextScrollImage.setOnClickListener(this);

        loggedInuser = new LoggedInUser(getContext());
        recyclerView=x.findViewById(R.id.recyclerView);
        ll1=x.findViewById(R.id.ll1);
        llCheckInternet=x.findViewById(R.id.llCheckInternet);
        tryAgain=x.findViewById(R.id.tryAgain);
        db = new DatabaseHandler(getContext());
        loggedInUser= new LoggedInUser(getContext());
        text_month_year=x.findViewById(R.id.text_month_year);
        no_data_text=x.findViewById(R.id.no_data_text);
        getCurrentDate(x);
        parseJasonLang=new ParseJasonLang(getContext());

        dateFormat1 = new SimpleDateFormat("YYYY");
        Date date1 = new Date();
        DateAdapter1.sSelected=-1;
        DateAdapter1.date_flag=0;
        for (int i =0;i<2;i++){

                array_year[i]=String.valueOf(Integer.parseInt(dateFormat1.format(date1))-i);

        }

        for(int i = 0; i<array_year.length;i++){
            if(Integer.parseInt(dateFormat1.format(date1))==Integer.parseInt(array_year[i])){
                year=array_year[i];
            }
        }


        dateFormat = new SimpleDateFormat("MM");
        date = new Date();

        for(int i = 0; i<months.length;i++){
            if(Integer.parseInt(dateFormat.format(date))==i+1){
                month=months[i];
                month1=dateFormat.format(date);
            }
        }
        text_month_year.setText(month.substring(0,3) +" "+year);
        text_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        main_title.setText(parseJasonLang.getJsonToString("completed_summary"));
        no_data_text.setText(parseJasonLang.getJsonToString("no_schedule"));
        if(db.getAllCompleteCheckIn(loggedInuser.getLocal_user_id(),"0").size()>0) {
            ScheduleActivity.title.setText(parseJasonLang.getJsonToString("Billing"));
        }
        dateModelArrayList.clear();

        if(month.equals("June")||month.equals("April")||month.equals("September")||month.equals("November")){
            for(int i = 0;i<30;i++){

                DateModel dateModel;

                dateModel = new DateModel("1",String.valueOf(i+1));

                dateModelArrayList.add(dateModel);


            }
        }
        if(month.equals("February")){
            for(int i = 0;i<28;i++){

                DateModel dateModel;

                dateModel = new DateModel("1",String.valueOf(i+1));

                dateModelArrayList.add(dateModel);


            }
        }


        if(month.equals("January")||month.equals("March")||month.equals("May")||month.equals("July")||month.equals("August")||month.equals("October")||month.equals("December")){
            for(int i = 0;i<31;i++){

                DateModel dateModel;

                dateModel = new DateModel("1",String.valueOf(i+1));

                dateModelArrayList.add(dateModel);


            }
        }

        mLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        date_recycler.setLayoutManager(mLayoutManager);

        mAdapter = new DateAdapter1(getContext(),dateModelArrayList);

        // Set an adapter for RecyclerView
        date_recycler.setAdapter(mAdapter);
        dateFormat2 = new SimpleDateFormat("dd");
        date2 = new Date();
        Log.d("Month",dateFormat2.format(date2));
        if (Integer.parseInt(dateFormat2.format(date2)) >= 4) {
            if (Integer.parseInt(dateFormat2.format(date2)) < 26) {
                date_recycler.smoothScrollToPosition(Integer.parseInt(dateFormat2.format(date2)) - 3);
                date_recycler.scrollToPosition((Integer.parseInt(dateFormat2.format(date2)) - 3));
            } else {
                date_recycler.smoothScrollToPosition(Integer.parseInt(dateFormat2.format(date2)) - 1);
                date_recycler.scrollToPosition((Integer.parseInt(dateFormat2.format(date2)) - 1));
            }
        } else {
            date_recycler.smoothScrollToPosition(0);
            date_recycler.scrollToPosition(0);
        }
        mAdapter.setOnItemClickListener(this);

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            if(loggedInUser.getLocal_language().equals("")) {
                getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            }else {
                getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            }
        }else {
            llCheckInternet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);

        }


        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected) {
                    if(loggedInUser.getLocal_language().equals("")) {
                        getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    }else {
                        getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    }

                }else {
                    llCheckInternet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    ll1.setVisibility(View.GONE);
                }
            }
        });
        return x;
    }
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_year_month);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        ImageView login_back = (ImageView) dialog.findViewById(R.id.login_back);
        Spinner spinner_year = (Spinner) dialog.findViewById(R.id.spinner_year);
        Spinner spinner_month = (Spinner) dialog.findViewById(R.id.spinner_month);
        TextView sign_up_button = (TextView) dialog.findViewById(R.id.sign_up_button);
        TextView select_date_text=dialog.findViewById(R.id.select_date_text);
        select_date_text.setText(parseJasonLang.getJsonToString("select_month_year"));
        ArrayAdapter adapter_year = new ArrayAdapter(getContext(),R.layout.custom_spinner,array_year);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DateFormat dateFormat1 = new SimpleDateFormat("YYYY");
        Date date1 = new Date();
        //Setting the ArrayAdapter data on the Spinner
        spinner_year.setAdapter(adapter_year);
        for(int i = 0; i<array_year.length;i++){
            if(Integer.parseInt(dateFormat1.format(date1))==Integer.parseInt(array_year[i])){
                spinner_year.setSelection(i);
            }
        }

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                year=array_year[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        ArrayAdapter adapter_month = new ArrayAdapter(getContext(),R.layout.custom_spinner,months);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_month.setAdapter(adapter_month);
        spinner_month.setSelection(Integer.parseInt(dateFormat.format(date))-1);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=months[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
        sign_up_button.setText(parseJasonLang.getJsonToString("done"));
        // if decline button is clicked, close the custom dialog
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                text_month_year.setText(month.substring(0,3)+" / "+year);
                for(int i = 0; i<months.length;i++){
                    if(month.equals(months[i])){
                        // month=months[i];
                        month1=String.valueOf(i+1);
                    }
                }
                if(isConnected) {
                    if(loggedInUser.getLocal_language().equals("")) {
                        getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    }else {
                        getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    }
                }

                dateModelArrayList.clear();



                if(month.equals("June")||month.equals("April")||month.equals("September")||month.equals("November")){
                    for(int i = 0;i<30;i++){

                        DateModel dateModel;

                        dateModel = new DateModel("1",String.valueOf(i+1));

                        dateModelArrayList.add(dateModel);


                    }
                }
                if(month.equals("February")){
                    for(int i = 0;i<28;i++){

                        DateModel dateModel;

                        dateModel = new DateModel("1",String.valueOf(i+1));

                        dateModelArrayList.add(dateModel);


                    }
                }


                if(month.equals("January")||month.equals("March")||month.equals("May")||month.equals("July")||month.equals("August")||month.equals("October")||month.equals("December")){
                    for(int i = 0;i<31;i++){

                        DateModel dateModel;

                        dateModel = new DateModel("1",String.valueOf(i+1));

                        dateModelArrayList.add(dateModel);


                    }
                }
                mAdapter.notifyDataSetChanged();



                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getService_category(final String lang, final String date, final int position){

        final DateFormat dateFormat2 = new SimpleDateFormat("dd");
        final Date date2 = new Date();
        Log.d("Month",dateFormat2.format(date2));
        scheduleModelArrayList.clear();
        //  category_name_list.clear();

        dialog= new CustomProgressDialog();
        dialog.startProgress(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.billing_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            dialog.stopProgress();




                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            for(int i =0;i<heroArray.length();i++){
                                JSONObject jsonObject1 = heroArray.getJSONObject(i);

                                String id=jsonObject1.getString("id");
                                String firstName=jsonObject1.getString("firstName");
                                String lastName=jsonObject1.getString("lastName");
//                                String startTime=jsonObject1.getString("startTime");
                               // String endTime=jsonObject1.getString("endTime");
                                String address=jsonObject1.getString("address");
                                String mobileNo=jsonObject1.getString("mobileNo");
                               // String unitRate=jsonObject1.getString("unitRate");
                               // String clientId=jsonObject1.getString("clientId");
                                String latitude=jsonObject1.getString("latitude");
                                String longitude=jsonObject1.getString("longitude");
                               // String sechudule_status=jsonObject1.getString("sechudule_status");
                                String profileImg=jsonObject1.getString("profileImg");
                              //  String caregiverId=jsonObject1.getString("caregiverId");



                                BillingModel scheduleModel = new BillingModel(id,profileImg,firstName+" "+lastName,address,mobileNo);


                                scheduleModelArrayList.add(scheduleModel);
                            }
                            // scheduleModelArrayList=db.getAllImages();

                            if(!scheduleModelArrayList.isEmpty()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                ll1.setVisibility(View.GONE);
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", "not empty");
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                //creating recyclerview adapter
                                BillingAdapter adapter = new BillingAdapter(getContext(), scheduleModelArrayList);

                                //setting adapter to recyclerview
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                //  adapter.notifyDataSetChanged();


                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                ll1.setVisibility(View.VISIBLE);
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", " empty");
                            }
                            Log.v("stateArray", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.stopProgress();
                            recyclerView.setVisibility(View.GONE);
                            ll1.setVisibility(View.VISIBLE);
                            Log.v("stateArray",String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        dialog.stopProgress();
                       ProjectUtil.showErrorResponse(getContext(), error);

                        Log.v("stateArray",String.valueOf(error));
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId",loggedInUser.getLocal_user_id());
                params.put("date",date);
                params.put("agencyId",loggedInUser.getAgency_id());
                //params.put("token", MyPreferences.getInstance(getApplicationContext()).getSESSION_TOKEN());


//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
//                params.put("deviceType", "2");

                MyLog.showLog(params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                // This is where you specify the content type
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        //creating a request queue
        ProjectUtil.setRequest(getContext(), stringRequest);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        mAdapter.selectedItem();
        DateFormat dateFormat2 = new SimpleDateFormat("dd");
        Date date2 = new Date();
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d("Month",dateFormat2.format(date2));
        if(isConnected) {
            recyclerView.setVisibility(View.VISIBLE);
            llCheckInternet.setVisibility(View.GONE);
            //recyclerView.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            if(loggedInUser.getLocal_language().equals("")){
                getService_category("en",dateModelArrayList.get(position).getDate()+"-"+month1+"-"+year,position);

            }else {
                getService_category(loggedInUser.getLocal_language(),dateModelArrayList.get(position).getDate()+"-"+month1+"-"+year,position);

            }

        }else {
            llCheckInternet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
        }
    }

    public void getCurrentDate(View view) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = "Current Date : " + mdformat.format(calendar.getTime());
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_date_scroll:
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition() <
                        mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
                    date_recycler.smoothScrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition() -
                            (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                    date_recycler.scrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition() -
                            (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                } else {
                    date_recycler.smoothScrollToPosition(0);
                    date_recycler.scrollToPosition(0);
                }
                break;

            case R.id.right_date_scroll:
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition() < dateModelArrayList.size() - mLayoutManager.findLastCompletelyVisibleItemPosition()) {
                    date_recycler.smoothScrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() +
                            (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                    date_recycler.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() +
                            (mLayoutManager.findLastCompletelyVisibleItemPosition() - mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                } else {
                    date_recycler.smoothScrollToPosition(dateModelArrayList.size() - 1);
                    date_recycler.scrollToPosition(dateModelArrayList.size() - 1);
                }
                break;
        }
    }
}
