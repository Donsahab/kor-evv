package dextrous.kor.evv.korevv.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.adapter.AllServiceListAdapter;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

public class ClockOutActivity extends AppCompatActivity {

    RecyclerView services_recycler;
    LinearLayout clock_in_out;
    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    String name = "", id = "", reason = "";
    TextView title, activity_text, save_text;
    ImageView login_back;
    LoggedInUser loggedInUser;
    DatabaseHandler db;
    int flag_equal = 0;
    ParseJasonLang parseJasonLang;
    private long time;
    private String personalcareTime;
    private String respitecareTime;
    private long assignedTime = 0, respiteTime = 0, respiteTimeNew = 0;
    private String personaltimeString = "",respiteCareTimeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        setContentView(R.layout.activity_clock_out);
        db = new DatabaseHandler(ClockOutActivity.this);
        parseJasonLang = new ParseJasonLang(ClockOutActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getString("id");
            reason = bundle.getString("reason");
            time = bundle.getLong("time");
            personalcareTime = bundle.getString("personacaretime");
            respitecareTime = bundle.getString("respitecaretime");
        }
        db.clearSelectedByUser();
        loggedInUser = new LoggedInUser(getApplicationContext());
        services_recycler = findViewById(R.id.services_recycler);
        clock_in_out = findViewById(R.id.sign_button);
        title = findViewById(R.id.title);
        login_back = findViewById(R.id.login_back);
        save_text = findViewById(R.id.save_text);
        activity_text = findViewById(R.id.activity_text);
        title.setText(name);
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearSelectedByUser();
                finish();
            }
        });
        getService();
        activity_text.setText(parseJasonLang.getJsonToString("select_activities_for_personal_care"));
        save_text.setText(parseJasonLang.getJsonToString("save_next"));
        showScheduledTimeDialog();
    }

    private void showScheduledTimeDialog() {

        Dialog dialog = new Dialog(ClockOutActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) ClockOutActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.personal_respite_time_dialog, null, false);
        dialog.setCancelable(false);

        TextView scheduledTimeLebel, personalCareTimelebel, respitecareTimeLebel, totalTimeLebel, tvPersonalTime, tvRespiteTime, tvTotaltime;
        Button okButton;
        okButton = view.findViewById(R.id.ok_button);
        scheduledTimeLebel = view.findViewById(R.id.spent_hours_lebel);
        personalCareTimelebel = view.findViewById(R.id.personal_care_time_lebel);
        respitecareTimeLebel = view.findViewById(R.id.respite_time_lebel);
        totalTimeLebel = view.findViewById(R.id.total_time_lebel);
        tvPersonalTime = view.findViewById(R.id.tv_personalcare_time);
        tvRespiteTime = view.findViewById(R.id.tv_respite_care_time);
        tvTotaltime = view.findViewById(R.id.tv_total_time);


        okButton.setText(parseJasonLang.getJsonToString("ok"));
        scheduledTimeLebel.setText(parseJasonLang.getJsonToString("spent_hours"));
        personalCareTimelebel.setText(parseJasonLang.getJsonToString("personal_care"));
        respitecareTimeLebel.setText(parseJasonLang.getJsonToString("respite_care"));
        totalTimeLebel.setText(parseJasonLang.getJsonToString("total_spent_hours"));

        if (personalcareTime != null && !personalcareTime.isEmpty() && respitecareTime != null && !respitecareTime.isEmpty()) {
            assignedTime = AppUtill.getMilliesFromhours(personalcareTime);
            respiteTime = AppUtill.getMilliesFromhours(respitecareTime);
        }

        if (time > assignedTime) {
            tvPersonalTime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(assignedTime)) + " h");
            tvRespiteTime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(time - assignedTime)) + " h");
            tvTotaltime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(time)) + " h");
            respiteTimeNew = time - assignedTime;
            respiteCareTimeString = AppUtill.calculateHoursFromMillies(time - assignedTime);
            personaltimeString = AppUtill.calculateHoursFromMillies(assignedTime);

        } else {
            tvPersonalTime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(time)) + " h");
            tvRespiteTime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(respiteTime) )+ " h");
            tvTotaltime.setText(AppUtill.removeZero(AppUtill.calculateHoursFromMillies(time)) + " h");
            personaltimeString = AppUtill.calculateHoursFromMillies(time);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        (ClockOutActivity.this).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();

        Point displaySize = AppUtill.getDisplayDimensions(ClockOutActivity.this);
        int width = displaySize.x - 40 - 40;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        assert window != null;
        window.setLayout(width, height);


        window.setBackgroundDrawableResource(R.color.trasparentcolor);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    void getService() {
        serviceModelArrayList.clear();
        serviceModelArrayList = db.getAllSelected(id);
        services_recycler.setHasFixedSize(true);

     /*   GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        services_recycler.setLayoutManager(manager);*/
        services_recycler.setLayoutManager(new LinearLayoutManager(ClockOutActivity.this));
        //creating recyclerview adapter
        AllServiceListAdapter adapter = new AllServiceListAdapter(ClockOutActivity.this, serviceModelArrayList);

        //setting adapter to recyclerview
        services_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        clock_in_out.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String[] selectedArray = new String[db.getAllSelected(id).size()];
                                                String[] selectedByUser = new String[db.getAllSelectedByUser().size()];
                                                for (int i = 0; i < selectedArray.length; i++) {
                                                    selectedArray[i] = db.getAllSelected(id).get(i).getId();
                                                    Log.v("selectedArray", selectedArray[i]);
                                                }
                                                for (int i = 0; i < selectedByUser.length; i++) {
                                                    selectedByUser[i] = db.getAllSelectedByUser().get(i).getId();
                                                    Log.v("selectedArray1", selectedByUser[i]);
                                                }
                                                if (selectedArray.length > selectedByUser.length || selectedArray.length < selectedByUser.length) {

                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClockOutActivity.this);

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
                                                            if (respiteTimeNew <= 0) {
                                                                Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                                                                intent.putExtra("name", name);
                                                                intent.putExtra("clockout_reason", reason);
                                                                intent.putExtra("respitetime", respiteTimeNew);
                                                                intent.putExtra("personalcaretime", personaltimeString);
                                                                intent.putExtra("respitecaretime", respiteCareTimeString);
                                                                intent.putExtra("personalcareactivities",selectedByUser);

                                                                intent.putExtra("respitecareassignedtime", respiteTime);
                                                                intent.putExtra("personalcareassignedtime",assignedTime);
                                                                startActivity(intent);
                                                            } else {
                                                                Intent intent = new Intent(getApplicationContext(), ClockOutRespiteCareActivity.class);
                                                                intent.putExtra("name", name);
                                                                intent.putExtra("id", id);
                                                                intent.putExtra("clockout_reason", reason);
                                                                intent.putExtra("respitetime", respiteTimeNew);
                                                                intent.putExtra("personalcaretime", personaltimeString);
                                                                intent.putExtra("respitecaretime", respiteCareTimeString);
                                                                intent.putExtra("personalcareactivities",selectedByUser);

                                                                intent.putExtra("respitecareassignedtime", respiteTime);
                                                                intent.putExtra("personalcareassignedtime",assignedTime);
                                                                startActivity(intent);
                                                            }

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

                                                    for (int i = 0; i < selectedArray.length; i++) {

                                                        for (int j = 0; j < selectedByUser.length; j++) {
                                                            if (selectedArray[i].equals(selectedByUser[j])) {
                                                                flag_equal++;
                                                            }

                                                        }


                                                    }
                                                    if (flag_equal < selectedByUser.length) {

                                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClockOutActivity.this);

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
                                                                if (respiteTimeNew <= 0) {
                                                                    Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                                                                    intent.putExtra("name", name);
                                                                    intent.putExtra("clockout_reason", reason);
                                                                    intent.putExtra("respitetime", respiteTimeNew);
                                                                    intent.putExtra("personalcaretime", personaltimeString);
                                                                    intent.putExtra("respitecaretime", respiteCareTimeString);
                                                                    intent.putExtra("personalcareactivities",selectedByUser);

                                                                    intent.putExtra("respitecareassignedtime", respiteTime);
                                                                    intent.putExtra("personalcareassignedtime",assignedTime);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Intent intent = new Intent(getApplicationContext(), ClockOutRespiteCareActivity.class);
                                                                    intent.putExtra("name", name);
                                                                    intent.putExtra("id", id);
                                                                    intent.putExtra("clockout_reason", reason);
                                                                    intent.putExtra("respitetime", respiteTimeNew);
                                                                    intent.putExtra("personalcaretime", personaltimeString);
                                                                    intent.putExtra("respitecaretime", respiteCareTimeString);
                                                                    intent.putExtra("personalcareactivities",selectedByUser);

                                                                    intent.putExtra("respitecareassignedtime", respiteTime);
                                                                    intent.putExtra("personalcareassignedtime",assignedTime);
                                                                    startActivity(intent);
                                                                }

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
                                                        flag_equal = 0;
                                                        if (respiteTimeNew <= 0) {
                                                            Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                                                            intent.putExtra("name", name);
                                                            intent.putExtra("clockout_reason", reason);
                                                            intent.putExtra("respitetime", respiteTimeNew);
                                                            intent.putExtra("personalcaretime", personaltimeString);
                                                            intent.putExtra("respitecaretime", respiteCareTimeString);
                                                            intent.putExtra("personalcareactivities",selectedByUser);

                                                            intent.putExtra("respitecareassignedtime", respiteTime);
                                                            intent.putExtra("personalcareassignedtime",assignedTime);
                                                            startActivity(intent);
                                                        } else {
                                                            Intent intent = new Intent(ClockOutActivity.this, ClockOutRespiteCareActivity.class);
                                                            intent.putExtra("name", name);
                                                            intent.putExtra("id", id);
                                                            intent.putExtra("clockout_reason", reason);
                                                            intent.putExtra("respitetime", respiteTimeNew);
                                                            intent.putExtra("personalcaretime", personaltimeString);
                                                            intent.putExtra("respitecaretime", respiteCareTimeString);
                                                            intent.putExtra("personalcareactivities",selectedByUser);

                                                            intent.putExtra("respitecareassignedtime", respiteTime);
                                                            intent.putExtra("personalcareassignedtime",assignedTime);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            }
                                        }
        );
    }

    @Override
    public void onBackPressed() {
        db.clearSelectedByUser();
        finish();
    }
}
