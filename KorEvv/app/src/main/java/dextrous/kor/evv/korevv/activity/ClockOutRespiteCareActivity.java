package dextrous.kor.evv.korevv.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.RespiteCareActivitiesAdapter;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

public class ClockOutRespiteCareActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private ParseJasonLang parseJasonLang;
    RecyclerView services_recycler;
    LinearLayout clock_in_out;
    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    TextView title, activity_text, save_text;
    ImageView login_back;
    LoggedInUser loggedInUser;
    String name = "", id = "", reason = "";
    int flag_equal = 0;
    private String checkoutReson = "";
    private long respiteTime = 0,time = 0,personalAssignedTime = 0,respiteAssignedTime = 0;
    private String personalCaretime = "", respiteCaretime = "";
    private String[] personalCareActivities;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        setContentView(R.layout.activity_clock_out);
        db = new DatabaseHandler(ClockOutRespiteCareActivity.this);
        parseJasonLang = new ParseJasonLang(ClockOutRespiteCareActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getString("id");
            checkoutReson = bundle.getString("clockout_reason");
            respiteTime = bundle.getLong("respitetime");
            personalCaretime = bundle.getString("personalcaretime");
            if (personalCaretime==null){
                personalCaretime = "0";
            }
            respiteCaretime = bundle.getString("respitecaretime");
            if (respiteCaretime==null){
                respiteCaretime = "0";
            }
            personalCareActivities = bundle.getStringArray("personalcareactivities");
            time = bundle.getLong("time",0);
            if (personalCaretime.equals("0")){
              respiteTime =  time;
            }

            personalAssignedTime = bundle.getLong("personalcareassignedtime",0);
            respiteAssignedTime = bundle.getLong("respitecareassignedtime",0);

        }
        db.clearSelectedByUserRespiteCare();
        loggedInUser = new LoggedInUser(getApplicationContext());
        services_recycler = findViewById(R.id.services_recycler);
        clock_in_out = findViewById(R.id.sign_button);
        title = findViewById(R.id.title);
        login_back = findViewById(R.id.login_back);
        save_text = findViewById(R.id.save_text);
        activity_text = findViewById(R.id.activity_text);
        title.setText(name);
        activity_text.setText(parseJasonLang.getJsonToString("select_activities_for_respite_care"));
        save_text.setText(parseJasonLang.getJsonToString("save_next"));
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearSelectedByUserRespiteCare();
                finish();
            }
        });
        setDataToRecycler();
    }

    private void setDataToRecycler() {
        serviceModelArrayList.clear();
        serviceModelArrayList = db.getAllSelected(id);
        services_recycler.setHasFixedSize(true);

     /*   GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        services_recycler.setLayoutManager(manager);*/
        services_recycler.setLayoutManager(new LinearLayoutManager(ClockOutRespiteCareActivity.this));
        //creating recyclerview adapter
        RespiteCareActivitiesAdapter adapter = new RespiteCareActivitiesAdapter(ClockOutRespiteCareActivity.this, serviceModelArrayList);

        //setting adapter to recyclerview
        services_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        clock_in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selectedArray = new String[db.getAllSelected(id).size()];
                String[] selectedByUser = new String[db.getAllSelectedByUserRespiteCare().size()];
                for (int i = 0; i < selectedArray.length; i++) {
                    selectedArray[i] = db.getAllSelected(id).get(i).getId();
                    Log.v("selectedArray", selectedArray[i]);
                }
                for (int i = 0; i < selectedByUser.length; i++) {
                    selectedByUser[i] = db.getAllSelectedByUserRespiteCare().get(i).getId();
                    Log.v("selectedArray1", selectedByUser[i]);
                }
                if (selectedArray.length > selectedByUser.length || selectedArray.length < selectedByUser.length) {

                    if (personalCaretime.equalsIgnoreCase("0")) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClockOutRespiteCareActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(parseJasonLang.getJsonToString("service_validation2"));
                        alertDialog.setCancelable(false);

                        // Setting Dialog Message
                        alertDialog.setMessage(parseJasonLang.getJsonToString("service_validation_message2"));

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.korevv);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Write your code here to invoke YES event
                                Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("clockout_reason", checkoutReson);
                                intent.putExtra("respitetime", respiteTime);
                                intent.putExtra("personalcaretime", personalCaretime);
                                intent.putExtra("respitecaretime", respiteCaretime);
                                intent.putExtra("respitecareactivities", selectedByUser);
                                intent.putExtra("personalcareactivities", personalCareActivities);

                                intent.putExtra("respitecareassignedtime", respiteAssignedTime);
                                intent.putExtra("personalcareassignedtime",personalAssignedTime);
                                startActivity(intent);

                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("clockout_reason", checkoutReson);
                        intent.putExtra("respitetime", respiteTime);
                        intent.putExtra("personalcaretime", personalCaretime);
                        intent.putExtra("respitecaretime", respiteCaretime);
                        intent.putExtra("respitecareactivities", selectedByUser);
                        intent.putExtra("personalcareactivities", personalCareActivities);

                        intent.putExtra("respitecareassignedtime", respiteAssignedTime);
                        intent.putExtra("personalcareassignedtime",personalAssignedTime);
                        startActivity(intent);
                    }

                } else {

                    for (int i = 0; i < selectedArray.length; i++) {

                        for (int j = 0; j < selectedByUser.length; j++) {
                            if (selectedArray[i].equals(selectedByUser[j])) {
                                flag_equal++;
                            }

                        }


                    }
                    if (flag_equal < selectedByUser.length) {
                        if (personalCaretime.equalsIgnoreCase("0")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClockOutRespiteCareActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(parseJasonLang.getJsonToString("service_validation2"));
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            alertDialog.setMessage(parseJasonLang.getJsonToString("service_validation_message2"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.korevv);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(parseJasonLang.getJsonToString("continue"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    flag_equal = 0;
                                    // Write your code here to invoke YES event

                                    Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("clockout_reason", checkoutReson);
                                    intent.putExtra("respitetime", respiteTime);
                                    intent.putExtra("personalcaretime", personalCaretime);
                                    intent.putExtra("respitecaretime", respiteCaretime);
                                    intent.putExtra("respitecareactivities", selectedByUser);
                                    intent.putExtra("personalcareactivities", personalCareActivities);

                                    intent.putExtra("respitecareassignedtime", respiteAssignedTime);
                                    intent.putExtra("personalcareassignedtime",personalAssignedTime);
                                    startActivity(intent);


                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(parseJasonLang.getJsonToString("cancel"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("clockout_reason", checkoutReson);
                            intent.putExtra("respitetime", respiteTime);
                            intent.putExtra("personalcaretime", personalCaretime);
                            intent.putExtra("respitecaretime", respiteCaretime);
                            intent.putExtra("respitecareactivities", selectedByUser);
                            intent.putExtra("personalcareactivities", personalCareActivities);

                            intent.putExtra("respitecareassignedtime", respiteAssignedTime);
                            intent.putExtra("personalcareassignedtime",personalAssignedTime);
                            startActivity(intent);
                        }
                    } else {
                        flag_equal = 0;

                        Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("clockout_reason", checkoutReson);
                        intent.putExtra("respitetime", respiteTime);
                        intent.putExtra("personalcaretime", personalCaretime);
                        intent.putExtra("respitecaretime", respiteCaretime);
                        intent.putExtra("respitecareactivities", selectedByUser);
                        intent.putExtra("personalcareactivities", personalCareActivities);

                        intent.putExtra("respitecareassignedtime", respiteAssignedTime);
                        intent.putExtra("personalcareassignedtime",personalAssignedTime);
                        startActivity(intent);

                    }
                }
            }


        });
    }

    @Override
    public void onBackPressed() {
        db.clearSelectedByUserRespiteCare();
        finish();
    }
}
