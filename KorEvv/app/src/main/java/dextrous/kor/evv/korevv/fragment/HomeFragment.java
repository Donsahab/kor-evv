package dextrous.kor.evv.korevv.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.DateAdapter;
import dextrous.kor.evv.korevv.adapter.ScheduleAdapter;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.model.DateModel;
import dextrous.kor.evv.korevv.model.RespiteUnitCalModel;
import dextrous.kor.evv.korevv.model.ScheduleModel;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;
import dextrous.kor.evv.korevv.retrofit.UploadEntraTime;
import dextrous.kor.evv.korevv.util.AppUtill;
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
public class HomeFragment extends Fragment implements DateAdapter.SingleClickListener, View.OnClickListener, ScheduleAdapter.WeekNextPreviousButtonClick {
    //  private static CheckBox lastChecked = null;
    private Button dailyViewButton, weeklyViewButton;
    private boolean dailyOrWeekly = false;
    private String weekDate, weekDateNew;
    private static int lastCheckedPos = 0;
    private CustomProgressDialog dialog;
    List<ScheduleModel> scheduleModelArrayList = new ArrayList<>();
    RecyclerView date_recycler, recyclerView;
    LinearLayout ll1, llCheckInternet;

    String month = "", year = "", month1 = "";
    TextView text_month_year, tryAgain;
    String array_year[] = new String[2];
    static String months[] =
            {
                    "January", "February", "March", "April", "May",
                    "June", "July", "August", "September", "October",
                    "November", "December"
            };
    View x;

    private LinearLayoutManager mLayoutManager;
    private DateAdapter mAdapter;
    ArrayList<DateModel> dateModelArrayList = new ArrayList<>();
    LoggedInUser loggedInUser;
    DatabaseHandler db;
    DateFormat dateFormat2, dateFormat1, dateFormat;
    Date date, date1, date2;
    ParseJasonLang parseJasonLang;
    TextView no_data_text;
    // view group date option select
    private LinearLayout dailylayout, weeklylayout;
    private ScheduleAdapter adapter;
    private int datePosition;
    private boolean isDateClicked;
    private boolean isNext = false, isActivityInsert = false;

    private LinearLayout button_layout;
    private Button nextButton, previousButton;
    private ImageView previousScrollImage, nextScrollImage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.showLog("MyFragment", this.getClass().getSimpleName());

        // Inflate the layout for this fragment
        x = inflater.inflate(R.layout.fragment_home, container, false);
        // find view group

        dailylayout = x.findViewById(R.id.daily_main_layout);
        dailyViewButton = x.findViewById(R.id.daily_button);
        weeklyViewButton = x.findViewById(R.id.weekly_button);
        button_layout = x.findViewById(R.id.button_layout);
        nextButton = x.findViewById(R.id.nextbutton);
        previousButton = x.findViewById(R.id.previousbutton);

        //next or previous image
        previousScrollImage = x.findViewById(R.id.left_date_scroll);
        nextScrollImage = x.findViewById(R.id.right_date_scroll);
        previousScrollImage.setOnClickListener(this);
        nextScrollImage.setOnClickListener(this);
        dailyViewButton.setOnClickListener(this);
        weeklyViewButton.setOnClickListener(this);
        dailyOrWeekly = false;
        if (dailyOrWeekly) {
            weeklyViewButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
            weeklyViewButton.setTextColor(getResources().getColor(R.color.white));

            dailyViewButton.setBackground(getResources().getDrawable(R.color.white));
            dailyViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            weeklyViewButton.setBackground(getResources().getDrawable(R.color.white));
            weeklyViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));

            dailyViewButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
            dailyViewButton.setTextColor(getResources().getColor(R.color.white));
        }

        date_recycler = x.findViewById(R.id.date_recycler);
        loggedInUser = new LoggedInUser(getContext());
        db = new DatabaseHandler(getContext());
      //  db.clearSELECTEDLIST();
        ll1 = x.findViewById(R.id.ll1);
        llCheckInternet = x.findViewById(R.id.llCheckInternet);
        tryAgain = x.findViewById(R.id.tryAgain);
        text_month_year = x.findViewById(R.id.text_month_year);
        no_data_text = x.findViewById(R.id.no_data_text);

        DateAdapter.sSelected = -1;
        DateAdapter.date_flag = 0;

        getCurrentDate();
        parseJasonLang = new ParseJasonLang(getContext());
        recyclerView = x.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
        adapter.setOnItemClickListener(this);

        dateFormat1 = new SimpleDateFormat("yyyy");
        Date date1 = new Date();
        for (int i = 0; i < 2; i++) {
            array_year[i] = String.valueOf(Integer.parseInt(dateFormat1.format(date1)) + i);
        }

        for (int i = 0; i < array_year.length; i++) {
            if (Integer.parseInt(dateFormat1.format(date1)) == Integer.parseInt(array_year[i])) {
                year = array_year[i];
            }
        }
        no_data_text.setText(parseJasonLang.getJsonToString("no_schedule"));

        main_title.setText(parseJasonLang.getJsonToString("Schedule"));

        weeklyViewButton.setText(parseJasonLang.getJsonToString("weekly"));
        dailyViewButton.setText(parseJasonLang.getJsonToString("daily"));

        dateFormat = new SimpleDateFormat("MM");
        date = new Date();

        for (int i = 0; i < months.length; i++) {
            if (Integer.parseInt(dateFormat.format(date)) == i + 1) {
                month = months[i];
                month1 = dateFormat.format(date);
            }
        }
        text_month_year.setText(month.substring(0, 3) + " " + year);
        text_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        dateModelArrayList.clear();

        if (month.equals("June") || month.equals("April") || month.equals("September") || month.equals("November")) {
            for (int i = 0; i < 30; i++) {
                DateModel dateModel;
                dateModel = new DateModel("1", String.valueOf(i + 1));
                dateModelArrayList.add(dateModel);

            }
        }
        if (month.equals("February")) {
            for (int i = 0; i < 28; i++) {
                DateModel dateModel;
                dateModel = new DateModel("1", String.valueOf(i + 1));
                dateModelArrayList.add(dateModel);
            }
        }
        if (month.equals("January") || month.equals("March") || month.equals("May") || month.equals("July") || month.equals("August") || month.equals("October") || month.equals("December")) {
            for (int i = 0; i < 31; i++) {
                DateModel dateModel;
                dateModel = new DateModel("1", String.valueOf(i + 1));
                dateModelArrayList.add(dateModel);
            }
        }
        mLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        date_recycler.setLayoutManager(mLayoutManager);
        mAdapter = new DateAdapter(getContext(), dateModelArrayList);
        //DateModel dateModel = dateModelArrayList.get(position);
        dateFormat2 = new SimpleDateFormat("dd");
        date2 = new Date();
        Log.v("Month", dateFormat2.format(date2));
        Log.v("Month1", month);
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

        // Set an adapter for RecyclerView
        date_recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        if (isConnected) {
            String date = dateFormat2.format(date2) + "-" + month1 + "-" + year;
            try {
                date = AppUtill.getWeekStartDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
          /*  if (loggedInUser.getLocal_language().equals("")) {
                getService_categoryWeek("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0, true, date);
            } else {
                getService_categoryWeek(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0, true, date);
            }*/
        }

        if (isConnected) {
            if (loggedInUser.getLocal_language().equals("")) {
                getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            } else {
                getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            }
        } else {

            scheduleModelArrayList = getDataFromDataBase(AppUtill.getCurrentDate("yyyy-MM-dd"));

            //creating recyclerview adapter
            //adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
            if (scheduleModelArrayList != null)
                shortAarraylist();

            adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
            if (scheduleModelArrayList.size() < 1) {
                ll1.setVisibility(View.VISIBLE);
                if (button_layout.getVisibility() == View.VISIBLE)
                    button_layout.setVisibility(View.GONE);
            } else {
                ll1.setVisibility(View.GONE);

            }
        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                    try {

                        if (loggedInUser.getLocal_language().equals("")) {
                            getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                        } else {
                            getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                        }
                    } catch (Exception e) {

                    }


                } else {
                    scheduleModelArrayList = db.getAllImages(loggedInUser.getLocal_user_id());
                    //creating recyclerview adapter
                    //  adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                    if (scheduleModelArrayList != null)
                        shortAarraylist();


                    adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }
            }
        });
//        if(isConnected) {
//            if(loggedInUser.getLocal_language().equals("")){
//                getService_category1("en",)
//            }
//
//        }


        if (dailyOrWeekly) {
            button_layout.setVisibility(View.GONE);
        } else {
            button_layout.setVisibility(View.GONE);
        }

        nextButton.setText(parseJasonLang.getJsonToString("next_week"));
        previousButton.setText(parseJasonLang.getJsonToString("previous_week"));
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        if (AppUtill.isNetworkAvil(getContext()) && dailyOrWeekly) {
            button_layout.setVisibility(View.VISIBLE);
        } else {
            button_layout.setVisibility(View.GONE);
        }
       /* if (AppUtill.isNetworkAvil(getContext()))
            setRespiteMinutToServer();*/

        return x;
    }

    private void setRespiteMinutToServer() {
        new UploadEntraTime(getContext(), MyPreferences.getInstance(getContext()).getRespitecaretimevalue(), AppUtill.getCurrentDate("yyyy/MM/dd")) {
            @Override
            public void getResponce(RespiteUnitCalModel respiteUnitCalModel) {
                MyPreferences.getInstance(getContext()).setRespitecaretimevalue(respiteUnitCalModel.getResult().getExtraRespiteTime());
                MyPreferences.getInstance(getContext()).setRespitecaretimeDate(respiteUnitCalModel.getResult().getExtraRespiteTimeDate());
            }
        };
    }

    private List<ScheduleModel> getDataFromDataBase(String date) {
        if (dailyOrWeekly) {
            return db.getAllImages(loggedInUser.getLocal_user_id());
        } else {
            return db.getAllImages(loggedInUser.getLocal_user_id(), date);
        }
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
        TextView select_date_text = dialog.findViewById(R.id.select_date_text);
        select_date_text.setText(parseJasonLang.getJsonToString("select_month_year"));
        DateFormat dateFormat1 = new SimpleDateFormat("YYYY");
        Date date1 = new Date();

        ArrayAdapter adapter_year = new ArrayAdapter(getContext(), R.layout.custom_spinner, array_year);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_year.setAdapter(adapter_year);
        for (int i = 0; i < array_year.length; i++) {
            if (Integer.parseInt(dateFormat1.format(date1)) == Integer.parseInt(array_year[i])) {
                spinner_year.setSelection(i);
            }
        }

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                year = array_year[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        ArrayAdapter adapter_month = new ArrayAdapter(getContext(), R.layout.custom_spinner, months);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_month.setAdapter(adapter_month);
        spinner_month.setSelection(Integer.parseInt(dateFormat.format(date)) - 1);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = months[position];

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
                text_month_year.setText(month.substring(0, 3) + " / " + year);
                for (int i = 0; i < months.length; i++) {
                    if (month.equals(months[i])) {
                        // month=months[i];
                        month1 = String.valueOf(i + 1);
                    }
                }
                if (isConnected) {
                    if (dailyOrWeekly) {
                        String date = dateFormat2.format(date2) + "-" + month1 + "-" + year;
                        try {
                            date = AppUtill.getWeekStartDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date != null)
                            getWeeklyData(date);
                    } else {
                        try {
                            if (loggedInUser.getLocal_language().equals("")) {
                                getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                            } else {
                                getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                dateModelArrayList.clear();

                if (month.equals("June") || month.equals("April") || month.equals("September") || month.equals("November")) {
                    for (int i = 0; i < 30; i++) {

                        DateModel dateModel;

                        dateModel = new DateModel("1", String.valueOf(i + 1));

                        dateModelArrayList.add(dateModel);


                    }
                }
                if (month.equals("February")) {
                    for (int i = 0; i < 28; i++) {

                        DateModel dateModel;

                        dateModel = new DateModel("1", String.valueOf(i + 1));

                        dateModelArrayList.add(dateModel);


                    }
                }

                if (month.equals("January") || month.equals("March") || month.equals("May") || month.equals("July") || month.equals("August") || month.equals("October") || month.equals("December")) {
                    for (int i = 0; i < 31; i++) {

                        DateModel dateModel;

                        dateModel = new DateModel("1", String.valueOf(i + 1));

                        dateModelArrayList.add(dateModel);


                    }
                }
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size() > 0) {
            Log.v("isCheckin", "not present +" + String.valueOf(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size()));
            for (int j = 0; j < db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size(); j++) {
                String sch_id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(j).getSch_id();
                String[] parts = db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_location().split(",");
                String client_lat = parts[0];
                String client_long = parts[1];
                postClockIn1(sch_id,
                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_date(),
                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_time(),
                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason_location(),
                        db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason(),
                        client_lat, client_long, 0);
            }


        }
        if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
            for (int i = 0; i < db.getAllCheckOut(loggedInUser.getLocal_user_id()).size(); i++) {

                getClockOutApi1(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCaregiver_sign(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_yn(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_yn(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_yn(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_yn(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_note(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_note(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_note(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_note(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getScheduleId(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign_name(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_date(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_time(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_latitude(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_longitude(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getAdditional_comments(),
                        0,
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckOutReason(),

                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareTime(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareTime(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareActivities(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareActivities(),
                        db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getTotalUnit());

            }

        }
       /* if (AppUtill.isNetworkAvil(getContext()))
            setRespiteMinutToServer();*/

        isActivityInsert = false;
    }

    @Override
    public void onItemClickListener(int position, View view) {
        isDateClicked = true;
        mAdapter.selectedItem();
        DateFormat dateFormat2 = new SimpleDateFormat("dd");
        Date date2 = new Date();
        datePosition = position;

        Log.d("Month", dateFormat2.format(date2));
        if (AppUtill.isNetworkAvil(Objects.requireNonNull(getContext()))) {
            recyclerView.setVisibility(View.VISIBLE);
            llCheckInternet.setVisibility(View.GONE);
            //recyclerView.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            if (loggedInUser.getLocal_language().equals("")) {
                try {
                    getService_category("en", dateModelArrayList.get(position).getDate() + "-" + month1 + "-" + year, position);
                } catch (Exception e) {

                }

            } else {
                try {

                    getService_category(loggedInUser.getLocal_language(), dateModelArrayList.get(position).getDate() + "-" + month1 + "-" + year, position);

                } catch (Exception e) {

                }

            }
            if (db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size() > 0) {
                Log.v("isCheckin", "not present +" + String.valueOf(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size()));
                for (int j = 0; j < db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").size(); j++) {
                    String sch_id = db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(j).getSch_id();
                    String[] parts = db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_location().split(",");
                    String client_lat = parts[0];
                    String client_long = parts[1];
                    postClockIn1(sch_id,
                            db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_date(),
                            db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getCheckin_time(),
                            db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason_location(),
                            db.getAllCheckIn(loggedInUser.getLocal_user_id(), sch_id).get(0).getClockin_reason(),
                            client_lat, client_long, 0);
                }


            }
            if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                for (int i = 0; i < db.getAllCheckOut(loggedInUser.getLocal_user_id()).size(); i++) {

                    getClockOutApi1(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCaregiver_sign(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_yn(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_yn(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_yn(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_yn(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion1_note(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion2_note(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion3_note(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getQuestion4_note(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getScheduleId(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getClient_sign_name(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_date(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckout_time(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_latitude(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheck_out_longitude(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getAdditional_comments(),
                            position,
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getCheckOutReason(),

                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareTime(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareTime(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getPersonalCareActivities(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getRespiteCareActivities(),
                            db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(i).getTotalUnit());
                }

            }
        } else {
            if (true) {
                String day = null;
                if (position < 10) {
                    day = "0" + (position + 1);
                } else {
                    day = position + 1 + "";
                }
                String date = year + "-" + month1 + "-" + day;

                //  String date =  day + "-" + month1 + "-" + year;
                // String date =  dateFormat2.format(date2) + "-" + month1 + "-" + year;
                scheduleModelArrayList = getDataFromDataBase(date);
                //creating recyclerview adapter
                // adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                if (scheduleModelArrayList != null)
                    shortAarraylist();
                adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                llCheckInternet.setVisibility(View.GONE);
                //recyclerView.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                if (scheduleModelArrayList.size() < 1) {
                    ll1.setVisibility(View.VISIBLE);
                    button_layout.setVisibility(View.GONE);

                } else {
                    ll1.setVisibility(View.GONE);

                }
            } else {
                recyclerView.setVisibility(View.GONE);
                llCheckInternet.setVisibility(View.VISIBLE);
                button_layout.setVisibility(View.GONE);
                //recyclerView.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
            }
        }
    }

    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        weekDate = mdformat.format(calendar.getTime());
        String strDate = "Current Date : " + mdformat.format(calendar.getTime());
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));
    }

    public void getService_category(final String lang, final String date, final int position) {

        final DateFormat dateFormat2 = new SimpleDateFormat("dd");
        final Date date2 = new Date();
        Log.d("Month", dateFormat2.format(date2));
        scheduleModelArrayList.clear();
        //  category_name_list.clear();
       /* if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
            Log.d("Month", "clear");
            db.clearSCHEDULELIST();

        }*/
        dialog = new CustomProgressDialog();
        dialog.startProgress(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.scheduleList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            if (AppUtill.isNetworkAvil(getContext()) && dailyOrWeekly)
                                button_layout.setVisibility(View.VISIBLE);
                            else
                                button_layout.setVisibility(View.GONE);
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            dialog.stopProgress();
                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            List<ScheduleModel> scheduleModels = db.getAllImages(loggedInUser.getLocal_user_id());
                            if (dailyOrWeekly && isActivityInsert && !isNext)
                                db.clearSELECTEDLIST();
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject jsonObject1 = heroArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String firstName = jsonObject1.getString("firstName");
                                String lastName = jsonObject1.getString("lastName");
                                String startTime = jsonObject1.getString("startTime");
                                String endTime = jsonObject1.getString("endTime");
                                String address = jsonObject1.getString("address");
                                String mobileNo = jsonObject1.getString("mobileNo");
                                String unitRate = jsonObject1.getString("unitRate");
                                String clientId = jsonObject1.getString("clientId");
                                String latitude = jsonObject1.getString("latitude");
                                String longitude = jsonObject1.getString("longitude");
                                String sechudule_status = jsonObject1.getString("sechudule_status");
                                String profileImg = jsonObject1.getString("profileImg");
                                String caregiverId = jsonObject1.getString("caregiverId");
                                String additional_note = jsonObject1.getString("additional_note");
                                String startDate = jsonObject1.getString("startDate");
                                String endDate = jsonObject1.getString("endDate");
                                String personalCareTime = jsonObject1.getString("personal_care_hour");
                                String respiteCareTime = jsonObject1.getString("respite_care_hour");
                                ScheduleModel scheduleModel = new ScheduleModel(clientId, unitRate, id, profileImg, firstName + " " + lastName,
                                        sechudule_status, address, startTime, endTime, latitude,
                                        longitude, mobileNo, caregiverId, additional_note, startDate, endDate, personalCareTime, respiteCareTime);
                                try {
                                    if (db.getAllSelected(id).size() <= 0 || dailyOrWeekly)
                                        getService_categorySelectedList(lang, id);

                                    if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                        if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                                            if (!id.equals(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(0).getScheduleId()) || !id.equals(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(0).getSch_id())) {
                                                db.addScheduleList(scheduleModel);
                                                scheduleModelArrayList.add(scheduleModel);
                                                Log.v("stateArray", id);
                                                // getService_categorySelectedList(lang, id);
                                            }
                                        } else {
                                            if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                                db.addScheduleList(scheduleModel);
                                                // getService_categorySelectedList(lang, id);
                                                scheduleModelArrayList.add(scheduleModel);
                                            }
                                        }

                                    } else {
                                        scheduleModelArrayList.add(scheduleModel);
                                    }

                                } catch (Exception e) {
                                    if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                        db.addScheduleList(scheduleModel);
                                        scheduleModelArrayList.add(scheduleModel);
                                        // getService_categorySelectedList(lang, id);
                                    }
                                }
                            }
                            // scheduleModelArrayList=db.getAllImages();

                            if (!scheduleModelArrayList.isEmpty()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                ll1.setVisibility(View.GONE);
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", "not empty");
                                //creating recyclerview adapter
                                //  adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                                if (scheduleModelArrayList != null)
                                    shortAarraylist();

                                adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);

                                //setting adapter to recyclerview
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //  adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                if (scheduleModelArrayList.size() < 1) {
                                    ll1.setVisibility(View.VISIBLE);
                                    //  button_layout.setVisibility(View.GONE);

                                } else {
                                    ll1.setVisibility(View.GONE);

                                }
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", " empty");
                            }
                            Log.v("stateArray", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.stopProgress();
                            recyclerView.setVisibility(View.GONE);
                            if (scheduleModelArrayList.size() < 1) {
                                ll1.setVisibility(View.VISIBLE);
                                //  button_layout.setVisibility(View.GONE);

                            } else {
                                ll1.setVisibility(View.GONE);

                            }
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        dialog.stopProgress();
                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(getContext(), error);
                        if (Integer.parseInt(dateFormat2.format(date2)) == position + 1) {
                            scheduleModelArrayList = db.getAllImages(loggedInUser.getLocal_user_id());

                            //creating recyclerview adapter
                            // adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                            if (scheduleModelArrayList != null)
                                shortAarraylist();

                            adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            llCheckInternet.setVisibility(View.GONE);


                            //recyclerView.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            llCheckInternet.setVisibility(View.VISIBLE);
                            button_layout.setVisibility(View.GONE);
                            //recyclerView.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                        }
                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("date", dailyOrWeekly ? weekDateNew : date);
                params.put("agencyId", loggedInUser.getAgency_id());
                params.put("type", dailyOrWeekly ? "week" : "day");
                params.put("lang", lang);
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

  /*  public void getService_categoryWeek(final String lang, final String date, final int position, boolean b, String dateNew) {

        final DateFormat dateFormat2 = new SimpleDateFormat("dd");
        final Date date2 = new Date();
        Log.d("Month", dateFormat2.format(date2));
        scheduleModelArrayList.clear();
        //  category_name_list.clear();
       *//* if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
            Log.d("Month", "clear");
            db.clearSCHEDULELIST();

        }*//*
        dialog = new CustomProgressDialog();
        dialog.startProgress(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.scheduleList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            if (AppUtill.isNetworkAvil(getContext()) && dailyOrWeekly)
                                button_layout.setVisibility(View.VISIBLE);
                            else
                                button_layout.setVisibility(View.GONE);
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            dialog.stopProgress();
                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            List<ScheduleModel> scheduleModels = db.getAllImages(loggedInUser.getLocal_user_id());
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject jsonObject1 = heroArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String firstName = jsonObject1.getString("firstName");
                                String lastName = jsonObject1.getString("lastName");
                                String startTime = jsonObject1.getString("startTime");
                                String endTime = jsonObject1.getString("endTime");
                                String address = jsonObject1.getString("address");
                                String mobileNo = jsonObject1.getString("mobileNo");
                                String unitRate = jsonObject1.getString("unitRate");
                                String clientId = jsonObject1.getString("clientId");
                                String latitude = jsonObject1.getString("latitude");
                                String longitude = jsonObject1.getString("longitude");
                                String sechudule_status = jsonObject1.getString("sechudule_status");
                                String profileImg = jsonObject1.getString("profileImg");
                                String caregiverId = jsonObject1.getString("caregiverId");
                                String additional_note = jsonObject1.getString("additional_note");
                                String startDate = jsonObject1.getString("startDate");
                                String endDate = jsonObject1.getString("endDate");
                                String personalCareTime = jsonObject1.getString("personal_care_hour");
                                String respiteCareTime = jsonObject1.getString("respite_care_hour");
                                ScheduleModel scheduleModel = new ScheduleModel(clientId, unitRate, id, profileImg, firstName + " " + lastName,
                                        sechudule_status, address, startTime, endTime, latitude,
                                        longitude, mobileNo, caregiverId, additional_note, startDate, endDate, personalCareTime, respiteCareTime);
                                try {
                                    if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                        if (db.getAllCheckOut(loggedInUser.getLocal_user_id()).size() > 0) {
                                            if (!id.equals(db.getAllCheckOut(loggedInUser.getLocal_user_id()).get(0).getScheduleId()) || !id.equals(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(), "1").get(0).getSch_id())) {
                                                db.addScheduleList(scheduleModel);
                                                scheduleModelArrayList.add(scheduleModel);
                                                Log.v("stateArray", id);
                                              //  getService_categorySelectedList(lang, id);
                                            }
                                        } else {
                                            if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                                db.addScheduleList(scheduleModel);
                                                getService_categorySelectedList(lang, id);
                                                scheduleModelArrayList.add(scheduleModel);
                                            }
                                        }

                                    } else {
                                        scheduleModelArrayList.add(scheduleModel);
                                    }

                                } catch (Exception e) {
                                    if (date.equals(dateFormat2.format(date2) + "-" + month1 + "-" + year)) {
                                        db.addScheduleList(scheduleModel);
                                        scheduleModelArrayList.add(scheduleModel);
                                        getService_categorySelectedList(lang, id);
                                    }
                                }
                            }
                            // scheduleModelArrayList=db.getAllImages();

                            if (!scheduleModelArrayList.isEmpty()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                ll1.setVisibility(View.GONE);
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", "not empty");
                                //creating recyclerview adapter
                                //  adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                                if (scheduleModelArrayList != null)
                                    shortAarraylist();

                                adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);

                                //setting adapter to recyclerview
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //  adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                if (scheduleModelArrayList.size() < 1) {
                                    ll1.setVisibility(View.VISIBLE);
                                    button_layout.setVisibility(View.GONE);

                                } else {
                                    ll1.setVisibility(View.GONE);

                                }
                                llCheckInternet.setVisibility(View.GONE);
                                Log.v("stateArray", " empty");
                            }
                            Log.v("stateArray", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.stopProgress();
                            recyclerView.setVisibility(View.GONE);
                            if (scheduleModelArrayList.size() < 1) {
                                ll1.setVisibility(View.VISIBLE);
                                button_layout.setVisibility(View.GONE);

                            } else {
                                ll1.setVisibility(View.GONE);

                            }
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        dialog.stopProgress();
                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(getContext(), error);
                        if (Integer.parseInt(dateFormat2.format(date2)) == position + 1) {
                            scheduleModelArrayList = db.getAllImages(loggedInUser.getLocal_user_id());

                            //creating recyclerview adapter
                            // adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                            if (scheduleModelArrayList != null)
                                shortAarraylist();

                            adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            llCheckInternet.setVisibility(View.GONE);


                            //recyclerView.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            llCheckInternet.setVisibility(View.VISIBLE);
                            button_layout.setVisibility(View.GONE);
                            //recyclerView.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                        }
                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("date", dateNew);
                params.put("agencyId", loggedInUser.getAgency_id());
                params.put("type", b ? "week" : "day");
                params.put("lang", lang);
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
    }*/

    private void shortAarraylist() {
        if (scheduleModelArrayList != null) {
            Collections.sort(scheduleModelArrayList, new Comparator<ScheduleModel>() {
                @Override
                public int compare(ScheduleModel o1, ScheduleModel o2) {
                    return AppUtill.compareDates(o1.getStartDate(), o2.getStartDate(), o1.getStart_time(), o2.getStart_time());
                }
            });
        }
    }

    public void getService_categorySelectedList(String lang, String id1) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl.avail_acitivity + "/" + id1 + "/" + lang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        // db.clearSELECTEDLIST();

                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);
                            //dialog.stopProgress();

                            JSONArray heroArray = jsonObject.getJSONArray("result");
                            // JSONArray heroArray = new JSONArray(response);
                            Log.v("stateArray", String.valueOf(response));
                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                String id = heroObject.getString("code_value");
                                String code_desc = heroObject.getString("code_desc");
                                ServiceModel serviceModel = new ServiceModel(id, code_desc, id1);
                                db.addSelectedList(serviceModel);
                                // Log.v("stateArray",db.getAllSelected(id1).get(i).getId());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        ProjectUtil.showErrorResponse(getContext(), error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                });
        //creating a request queue
        ProjectUtil.setRequest(getContext(), stringRequest);
    }

    public void postClockIn1(String sch_id, final String startDate, String startTime, String clcckInLocatio,
                             String clockInReason, String client_lat, String client_long, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkin_start,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);

                            Log.v("stateArray", String.valueOf(response));

                            //   Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));
                            // text_time.setText(parseJasonLang.getJsonToString("clock_out"));

//                            String clockId=jsonObject.getString("clockId");
                            // String status=jsonObject.getString("status");
                            // String message=jsonObject.getString("message");


                            //   String startTime=jsonObject1.getString("startTime");
//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//
                            // scheduleModelArrayList=db.getAllImages();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(getContext(), error);
                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("latitude", client_lat);
                params.put("scheduleId", sch_id);
                params.put("checkinDate", startDate);

                params.put("agencyId", loggedInUser.getAgency_id());
                params.put("checkinTime", startTime);
                params.put("longitude", client_long);
                params.put("checkinLocation", clcckInLocatio);
                params.put("checkinReason", clockInReason);
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

    public void getClockOutApi1(String client_sign, String caregiver_sign, String question1_yn, String question2_yn, String question3_yn, String question4_yn, String question1_note,
                                String question2_note, String question3_note, String question4_note, String sch_id, String client_signName,
                                final String startDate, String startTime, String client_lat, String client_long, String notes, int position, String clockOutReson,
                                String personal_care_hours,
                                String respite_care_hours,
                                String personal_care_activities,
                                String respite_care_activities,
                                String total_calculate_units) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.checkout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        try {
                            //getting the whole json object from the response
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = new JSONObject(response);

                            // Log.v("direction",String.valueOf(direction));
                            //db.addCheckInDetails(new CheckInModel(id, loggedInUser.getLocal_agency(), client_id, loggedInUser.getLocal_user_id(), startDate, startTime, String.valueOf(latitude) + "," + String.valueOf(longitude)));

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            String action;
                            if (loggedInUser.getLocal_language().equals("")) {
                                try {
                                    getService_category("en", dateModelArrayList.get(position).getDate() + "-" + month1 + "-" + year, position);
                                } catch (Exception e) {

                                }

                            } else {
                                try {

                                    getService_category(loggedInUser.getLocal_language(), dateModelArrayList.get(position).getDate() + "-" + month1 + "-" + year, position);

                                } catch (Exception e) {

                                }
                            }
                            String billId = jsonObject.getString("billId");

                            //   db.clearSELECTEDLIST();
                            db.clearSelectedByUser();
                            db.clearSelectedByUserRespiteCare();
                            db.clearCheckOut();
                            db.deleteCheckIn(sch_id);

//                                String endTime=jsonObject1.getString("endTime");
//                                String address=jsonObject1.getString("address");
//                                String mobileNo=jsonObject1.getString("mobileNo");
//                                String unitRate=jsonObject1.getString("unitRate");
//                                String clientId=jsonObject1.getString("clientId");
//                                String latitude=jsonObject1.getString("latitude");
//                                String longitude=jsonObject1.getString("longitude");
//                                String sechudule_status=jsonObject1.getString("sechudule_status");
//                                String profileImg=jsonObject1.getString("profileImg");
//
//

                            // scheduleModelArrayList=db.getAllImages();

                            Log.v("stateArray_clock", String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.v("stateArray", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        DateFormat dateFormat2 = new SimpleDateFormat("dd");
                        Date date2 = new Date();
                        Log.d("Month", dateFormat2.format(date2));
                        ProjectUtil.showErrorResponse(getContext(), error);

                        Log.v("stateArray", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("token", loggedInUser.getLocal_session_toekn());
                params.put("userId", loggedInUser.getLocal_user_id());
                params.put("check_out_latitude", client_lat);
                params.put("scheduleId", sch_id);
                params.put("checkout_date", startDate);
                params.put("client_sign", client_sign);
                params.put("checkout_time", startTime);
                params.put("check_out_longitude", client_long);

                params.put("personal_care_hours", personal_care_hours == null ? "" : personal_care_hours);
                params.put("respite_care_hours", respite_care_hours == null ? "" : respite_care_hours);
                params.put("personal_care_activities", personal_care_activities == null ? "" : personal_care_activities);
                params.put("respite_care_activities", respite_care_activities == null ? "" : respite_care_activities);
                params.put("total_calculate_units", total_calculate_units == null ? "" : total_calculate_units);
                params.put("checkout_reason", clockOutReson == null ? "" : clockOutReson);
//                params.put("deviceId", Settings.Secure.getString(
//                        getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("question1_yn", question1_yn);
                params.put("question2_yn", question2_yn);
                params.put("question3_yn", question3_yn);
                params.put("question4_yn", question4_yn);
                params.put("question1_note", question1_note);
                params.put("question2_note", question2_note);
                params.put("question3_note", question3_note);
                params.put("question4_note", question4_note);
                params.put("additional_comments", notes);
                params.put("client_sign_name", client_signName);
                params.put("caregiver_sign", caregiver_sign);


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daily_button:
                if (AppUtill.isNetworkAvil(getContext()))
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

                if (AppUtill.isNetworkAvil(getContext()))
                    mAdapter.updateDateFlag(0, -1);
                previousButton.setEnabled(false);
                nextButton.setEnabled(true);
                isNext = false;
                isActivityInsert = false;
                dailyViewButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
                dailyViewButton.setTextColor(getResources().getColor(R.color.white));
                weeklyViewButton.setBackground(getResources().getDrawable(R.color.white));
                weeklyViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                dailylayout.setVisibility(View.VISIBLE);
                dailyOrWeekly = false;
                if (isConnected) {
                    if (loggedInUser.getLocal_language().equals("")) {
                        getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    } else {
                        getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
                    }
                } else {
                    String date = null;
                    String day = null;
                    if (isDateClicked) {
                        if (datePosition < 10) {
                            day = "0" + (datePosition + 1);
                        } else {
                            day = datePosition + 1 + "";
                        }
                        date = year + "-" + month1 + "-" + day;
                    } else {
                        date = AppUtill.getCurrentDate("yyyy-MM-dd");
                    }
                    scheduleModelArrayList = getDataFromDataBase(date);

                    //creating recyclerview adapter
                    // adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
                    if (scheduleModelArrayList != null)
                        shortAarraylist();
                    adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);
                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                    if (scheduleModelArrayList.size() < 1) {
                        ll1.setVisibility(View.VISIBLE);
                        // button_layout.setVisibility(View.GONE);

                    } else {
                        ll1.setVisibility(View.GONE);
                    }
                    button_layout.setVisibility(View.GONE);
                }

                break;
            case R.id.weekly_button:
                isNext = false;
                isActivityInsert = true;
                previousButton.setEnabled(false);
                nextButton.setEnabled(true);

                String date = dateFormat2.format(date2) + "-" + month1 + "-" + year;
                try {
                    date = AppUtill.getWeekStartDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null)
                    getWeeklyData(date);
                break;
            case R.id.nextbutton:
                previousButton.setEnabled(true);
                nextButton.setEnabled(false);
                try {
                    nextClicked(true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.previousbutton:
                nextButton.setEnabled(true);
                previousButton.setEnabled(false);
                previousClicked(false);
                break;
            case R.id.left_date_scroll:
                if (  mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()<
                        mLayoutManager.findFirstCompletelyVisibleItemPosition()){
                    date_recycler.smoothScrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition()-
                            (mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                    date_recycler.scrollToPosition(mLayoutManager.findFirstCompletelyVisibleItemPosition()-
                            (mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                }else {
                    date_recycler.smoothScrollToPosition(0);
                    date_recycler.scrollToPosition(0);
                }
                break;

            case R.id.right_date_scroll:
                if ( mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()< dateModelArrayList.size()-mLayoutManager.findLastCompletelyVisibleItemPosition()){
                    date_recycler.smoothScrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition()+
                            (mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()) );
                    date_recycler.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition()+
                            (mLayoutManager.findLastCompletelyVisibleItemPosition()- mLayoutManager.findFirstCompletelyVisibleItemPosition()));
                }else {
                    date_recycler.smoothScrollToPosition(dateModelArrayList.size()-1);
                    date_recycler.scrollToPosition(dateModelArrayList.size()-1);
                }
                break;
        }
    }

    private void getWeeklyData(String weekDate1) {

        weeklyViewButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
        weeklyViewButton.setTextColor(getResources().getColor(R.color.white));

        dailyViewButton.setBackground(getResources().getDrawable(R.color.white));
        dailyViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        weekDateNew = weekDate1;

        dailylayout.setVisibility(View.GONE);
        dailyOrWeekly = true;

        if (isConnected) {
            if (loggedInUser.getLocal_language().equals("")) {
                getService_category("en", dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            } else {
                getService_category(loggedInUser.getLocal_language(), dateFormat2.format(date2) + "-" + month1 + "-" + year, 0);
            }
        } else {
            scheduleModelArrayList = getDataFromDataBase(AppUtill.getCurrentDate("yyyy-MM-dd"));

            //creating recyclerview adapter
            // adapter = new ScheduleAdapter(getContext(), scheduleModelArrayList, dailyOrWeekly);
            ArrayList<ScheduleModel> scheduleModels = new ArrayList<>();
            if (scheduleModelArrayList != null)
                shortAarraylist();

            if (!isNext) {
                scheduleModels.clear();
                for (ScheduleModel scheduleModel : scheduleModelArrayList) {
                    if (AppUtill.compareDatesNew(scheduleModel.getStartDate(),
                            AppUtill.getWeekEndDate(AppUtill.getCurrentDate("yyyy-MM-dd hh:mm a")), 0 + "", 0 + "") >= 0) {
                        //Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                        scheduleModels.add(scheduleModel);
                    }
                }
            } else {
                scheduleModels.clear();
                for (ScheduleModel scheduleModel : scheduleModelArrayList) {
                    if ((AppUtill.compareDatesNew(scheduleModel.getStartDate(),
                            AppUtill.getWeekEndDate(AppUtill.getCurrentDate("yyyy-MM-dd hh:mm a")).substring(0, 8) +
                                    (Integer.parseInt(AppUtill.getWeekEndDate(AppUtill.getCurrentDate("yyyy-MM-dd hh:mm a")).substring(8, 10)) + 7),
                            0 + "", 0 + "") >=0
                    ) && (AppUtill.compareDatesNew(AppUtill.getWeekEndDate(AppUtill.getCurrentDate("yyyy-MM-dd hh:mm a")),
                            scheduleModel.getStartDate(), 0 + "", 0 + "") > 0)
                    ) {
                        // Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                        scheduleModels.add(scheduleModel);
                    }
                }
            }
            if (isActivityInsert)
                adapter.updateList(scheduleModels, dailyOrWeekly, isNext);
            else
                adapter.updateList(scheduleModelArrayList, dailyOrWeekly, isNext);

            if (isActivityInsert && !(scheduleModels.size() > 0)) {
                ll1.setVisibility(View.VISIBLE);
            }

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);

            if (scheduleModelArrayList.size() < 1) {
                ll1.setVisibility(View.VISIBLE);
                button_layout.setVisibility(View.VISIBLE);
            } else {
                ll1.setVisibility(View.GONE);
                button_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void nextClicked(boolean b) throws ParseException {
        isNext = b;
        getWeeklyData(AppUtill.getNextWeekDate());
    }

    @Override
    public void previousClicked(boolean b) {
        try {
            isNext = b;
            getWeeklyData(AppUtill.getPreviousWeekDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
