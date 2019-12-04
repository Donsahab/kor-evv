package dextrous.kor.evv.korevv.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.model.CheckInModel;
import dextrous.kor.evv.korevv.model.ScheduleModel;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.CustomProgressDialog;
import dextrous.kor.evv.korevv.util.DatabaseHandler;
import dextrous.kor.evv.korevv.util.LocationTrack;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static dextrous.kor.evv.korevv.activity.MainActivity.isConnected;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockInSchedule extends Fragment {

    View x;
    private CustomProgressDialog dialog;
    ImageView image_title;
    LinearLayout clock_in_out;
    RecyclerView services_recycler;
    TextView location,phone_text,time_text,view_deatils_button,text_time,timer_count,call_icon;
    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds,Hours ;
    private boolean isStart;
    String name="",location1="",phone="",direction="",id="",time="",pic="",client_id;
    LinearLayout diection_layout;
    LoggedInUser loggedInUser;
    TextView activity_text;
    //ImageView image_title;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    final List<String> animalsList = new ArrayList();
    double latitude,longitude;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack appLocationService;
    DatabaseHandler db ;
    String client_lat="",client_long="";
    ParseJasonLang parseJasonLang;
    CheckInModel checkInModel;
    ScheduleModel scheduleModel;
    void getId(View x){
      //  login_back=findViewById(R.id.login_back);
        image_title=x.findViewById(R.id.image_title);
        call_icon=x.findViewById(R.id.call_icon);
        clock_in_out=x.findViewById(R.id.clock_in_out);
        services_recycler=x.findViewById(R.id.services_recycler);
       // title=findViewById(R.id.title);
        location=x.findViewById(R.id.location);
        phone_text=x.findViewById(R.id.phone_text);
        time_text=x.findViewById(R.id.time_text);
        view_deatils_button=x.findViewById(R.id.view_deatils_button);
        text_time=x.findViewById(R.id.text_time);
        timer_count=x.findViewById(R.id.timer_count);
        activity_text=x.findViewById(R.id.activity_text);
        diection_layout=x.findViewById(R.id.diection_layout);
        image_title=x.findViewById(R.id.image_title);


    }
    public ClockInSchedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.showLog("MyFragment", this.getClass().getSimpleName());

        // Inflate the layout for this fragment
        x=inflater.inflate(R.layout.fragment_clock_in_schedule, container, false);
        loggedInUser = new LoggedInUser(getContext());

        db = new DatabaseHandler(getContext());
        parseJasonLang= new ParseJasonLang(getContext());

        getId();
//        db.clearCheckIn();
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
//        if(isConnected) {
//            getService_category1("en", id);
//        }else {
//            getService();
//        }


        call_icon.setText(parseJasonLang.getJsonToString("make_call"));
        view_deatils_button.setText(parseJasonLang.getJsonToString("direction"));
        activity_text.setText(parseJasonLang.getJsonToString("activities"));




        time_text.setText(time);

        scheduleModel = db.getSchedule(db.getAllCompleteCheckIn(loggedInUser.getLocal_user_id(),"0").get(0).getUser_id());

        name=scheduleModel.getName();
        location1=scheduleModel.getLocation();
        phone=scheduleModel.getCall();
        direction=scheduleModel.getLat() +","+scheduleModel.getLongi();
        id=scheduleModel.getUser_id();
        client_id=scheduleModel.getClientId();
        time=scheduleModel.getStart_time()+" To "+scheduleModel.getEnd_time();
        pic=scheduleModel.getPic();


        appLocationService = new LocationTrack(
                getContext());
        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image_title.setImageBitmap(decodedByte);

        String parts[]= direction.split(",");

        client_lat=parts[0];
        client_long = parts[1];


        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        //permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        return x;
    }

}
