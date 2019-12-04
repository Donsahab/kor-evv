package dextrous.kor.evv.korevv.fragment;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.ScheduleActivity;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.util.ProjectUtil;
import dextrous.kor.evv.korevv.util.TextJustification;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static dextrous.kor.evv.korevv.activity.MainActivity.main_title;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDesk extends Fragment {


    View x;

    TextView contact_tilte,agency_title,email_title,website_title;

    TextView contact_no,tv_agency_name,tv_address,tv_email,tv_website;
    LoggedInUser loggedInUser;
    CustomProgressDialog dialog;

    DatabaseHandler db;
    ParseJasonLang parseJasonLang;
    private LinearLayout llCheckInternet;


    void GetID(){
        contact_tilte=x.findViewById(R.id.contact_tilte);
        agency_title=x.findViewById(R.id.agency_title);
       // address_title=x.findViewById(R.id.address_title);
        email_title=x.findViewById(R.id.email_title);
        website_title=x.findViewById(R.id.website_title);

        contact_no=x.findViewById(R.id.contact_no);
        tv_agency_name=x.findViewById(R.id.tv_agency_name);
        tv_address=x.findViewById(R.id.tv_address);
        tv_email=x.findViewById(R.id.tv_email);
        tv_website=x.findViewById(R.id.tv_website);

        llCheckInternet = x.findViewById(R.id.llCheckInternet);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.showLog("MyFragment", this.getClass().getSimpleName());

        // Inflate the layout for this fragment
        x=inflater.inflate(R.layout.fragment_help_desk, container, false);

        GetID();
        loggedInUser = new LoggedInUser(getContext());
        parseJasonLang= new ParseJasonLang(getContext());
        db= new DatabaseHandler(getContext());
        contact_tilte.setText(parseJasonLang.getJsonToString("contact_no"));
        agency_title.setText(parseJasonLang.getJsonToString("Agency"));
        //address_title.setText(parseJasonLang.getJsonToString("address"));
        email_title.setText(parseJasonLang.getJsonToString("Email")+":");
        website_title.setText(parseJasonLang.getJsonToString("website")+":");
        getBillingDetails(loggedInUser.getLocal_agency());
        main_title.setText(parseJasonLang.getJsonToString("help_desk"));
        if(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(),"0").size()>0) {
            ScheduleActivity.title.setText(parseJasonLang.getJsonToString("help_desk"));
        }
        tv_website.setPaintFlags(tv_website.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv_website.setMovementMethod(LinkMovementMethod.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_address.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }else {
            TextJustification.justify(tv_address);
        }

        if (AppUtill.isNetworkAvil(getContext())){
            llCheckInternet.setVisibility(View.GONE);
        }else {
            llCheckInternet.setVisibility(View.VISIBLE);
        }

       // contact_no.setPaintFlags(contact_no.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
      //  contact_no.setMovementMethod(LinkMovementMethod.getInstance());
        return x;
    }
    public void getBillingDetails(String agency_id ){

        dialog = new CustomProgressDialog();
        dialog.startProgress(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.get_help_desk+"/"+agency_id,
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

                            dialog.stopProgress();
                            Log.v("stateArray",String.valueOf(response));

                            //now looping through all the elements of the json array

                            String contact=jsonObject.getString("contact");
                            String agency_email=jsonObject.getString("agency_email");
                            String website=jsonObject.getString("website");
                            String address=jsonObject.getString("address");
                            String agency_name=jsonObject.getString("agency_name");
                            contact_no.setText(contact);
                            contact_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:"+contact));
                                    startActivity(callIntent);
                                }
                            });
                            tv_website.setText(website);
                            tv_email.setText(agency_email);
                            tv_address.setText(address.trim().replaceAll(" +", " "));
                            tv_agency_name.setText(agency_name);

                            tv_website.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("https://"+website));
                                    getActivity().startActivity(i);
                                }
                            });



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
                        ProjectUtil.showErrorResponse(getContext(), error);
                        dialog.stopProgress();
                        Log.v("stateArray",String.valueOf(error));
                    }
                });

        //creating a request queue
        ProjectUtil.setRequest(getContext(), stringRequest);
    }
}
