package dextrous.kor.evv.korevv.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import dextrous.kor.evv.korevv.model.CheckInModel;
import dextrous.kor.evv.korevv.model.CheckOutModel;
import dextrous.kor.evv.korevv.model.QuestionModel;
import dextrous.kor.evv.korevv.model.ScheduleModel;
import dextrous.kor.evv.korevv.model.ServiceModel;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 17; //old 16
    private static final String DATABASE_NAME = "korevv";
    private static final String SCHEDULELIST = "scheduleList";
    private static final String SERVICELIST = "servicelist";
    private static final String SELECTEDLIST = "selectedlist";
    private static final String CHECKIN = "checkin";
    private static final String SELECTED_BY_USER = "selected_by_user";
    private static final String SELECTED_BY_USER_RESPITECARE = "selected_by_user_respitecare";
    private static final String CHECKOUT = "checkout";
    private static final String QUESTIONTABLE = "questiontable";
    private static final String RESPITE_TIME_CONTAINER = "respitetimecontainer";
    private static final String IMAGE_ID = "image_id";
    private static final String IMAGE_NAME = "image_name";
    private static final String KEY_CATEGORYID = "categoryid1";
    private static final String KEY_BUDGET = "budget1";
    private static final String KEY_UNIT = "unit1";
    private static final String KEY_UNITCODE = "unitcode1";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + SCHEDULELIST + "( sch_id TEXT PRIMARY KEY,  name TEXT , profileImg TEXT ," +
                " startTime TEXT , endTime TEXT , address TEXT , mobileNo TEXT , latitude TEXT , longitude TEXT , sechudule_status TEXT ," +
                " unitRate TEXT , clientId TEXT , user_id TEXT , additional_note TEXT, startDate TEXT, endDate TEXT," +
                "personalCareTime TEXT, respiteCareTime TEXT )";

        String CREATE_SERVICE_LIST = "CREATE TABLE " + SERVICELIST + "( code_value TEXT PRIMARY KEY , code_desc TEXT )";

        String CREATESELECT = "CREATE TABLE " + SELECTEDLIST + "( code_value TEXT , code_desc TEXT , id TEXT )";

        String SELECTEDBYUSER = "CREATE TABLE " + SELECTED_BY_USER + "( code_value TEXT PRIMARY KEY , code_desc TEXT )";
        String SELECTEDBYUSERRESPITECARE = "CREATE TABLE " + SELECTED_BY_USER_RESPITECARE + "( code_value TEXT PRIMARY KEY , code_desc TEXT )";

        String QUESTIONS = "CREATE TABLE " + QUESTIONTABLE + "( questions TEXT PRIMARY KEY )";

      //  String RESPITETIMECONTAINER = "CREATE TABLE " + RESPITE_TIME_CONTAINER + "( respitetimecontainer TEXT PRIMARY KEY )";

        String CREATE_CHECK_OUT = "CREATE TABLE " + CHECKOUT + "( scheduleId TEXT PRIMARY KEY , userId TEXT , check_out_latitude TEXT , " +
                " check_out_longitude TEXT , checkout_date TEXT , checkout_time TEXT , question1_yn TEXT , question2_yn TEXT , question3_yn TEXT ," +
                " question4_yn TEXT , question1_note TEXT , question2_note TEXT , question3_note TEXT , question4_note TEXT , " +
                " client_sign TEXT , client_sign_name TEXT , caregiver_sign TEXT , additional_comments TEXT ,checkout_reason TEXT," +
                "personalcare_time TEXT , respitecare_time TEXT , " +
                "personalcare_activities TEXT , respitecare_activities TEXT ,total_unit TEXT)";

        String CREATECHECKIN = "CREATE TABLE " + CHECKIN + "( sch_id TEXT PRIMARY KEY , agency_id TEXT , clientId TEXT , user_id TEXT , checkin_time TEXT ," +
                " checkin_date TEXT , checkin_location TEXT , location_status TEXT , complete_status TEXT, clockin_reason_location TEXT, clockin_reason TEXT )";

        db.execSQL(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATECHECKIN);
        db.execSQL(CREATE_SERVICE_LIST);
        db.execSQL(CREATESELECT);
        db.execSQL(SELECTEDBYUSER);
        db.execSQL(SELECTEDBYUSERRESPITECARE);
        db.execSQL(CREATE_CHECK_OUT);
        db.execSQL(QUESTIONS);
       // db.execSQL(RESPITETIMECONTAINER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULELIST);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTEDLIST);
        db.execSQL("DROP TABLE IF EXISTS " + SERVICELIST);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKIN);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_BY_USER);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_BY_USER_RESPITECARE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONTABLE);
       // db.execSQL("DROP TABLE IF EXISTS " + RESPITE_TIME_CONTAINER);
        // Create tables again
        onCreate(db);
        this.db = db;
    }

    public void clearSCHEDULELIST() {
        db = getReadableDatabase();
        db.execSQL("delete from " + SCHEDULELIST);
        //  db.execSQL("delete from " + IMAGE);
    }

    public void clearQuestionTable() {
        db = getReadableDatabase();
        db.execSQL("delete from " + QUESTIONTABLE);
        //  db.execSQL("delete from " + IMAGE);
    }

    public void clearCheckOut() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CHECKOUT);
        //  db.execSQL("delete from " + IMAGE);
    }

    public void clearSELECTEDLIST() {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("delete from " + SELECTEDLIST);
    }

    public void clearSelectedByUser() {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("delete from " + SELECTED_BY_USER);
    }

    public void clearSelectedByUserRespiteCare() {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("delete from " + SELECTED_BY_USER_RESPITECARE);
    }

    public void clearServiceList() {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("delete from " + SERVICELIST);
    }

    public void clearCheckIn() {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("delete from " + CHECKIN);
    }

    public void addRespiteTimeValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("respitetimecontainer", "0");
        // Inserting Row
        db.insert(RESPITE_TIME_CONTAINER, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void updateRespiteTimevalue(String time) {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("UPDATE RESPITE_TIME_CONTAINER SET respitetimecontainer = " + time);
    }

    public void updateCheckInColumn(String sch_id) {
        db = getReadableDatabase();
        //db.execSQL("delete from " + RATE12);
        db.execSQL("UPDATE CHECKIN SET complete_status= '1'  WHERE sch_id= " + sch_id);
    }

    public void addCheckOutDetails(CheckOutModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleId", contact.getScheduleId());
        values.put("userId", contact.getUserId());
        values.put("check_out_latitude", contact.getCheck_out_latitude());
        values.put("check_out_longitude", contact.getCheck_out_longitude()); // Contact Phone
        values.put("checkout_date", contact.getCheckout_date());
        values.put("checkout_time", contact.getCheckout_time());

        values.put("question1_yn", contact.getQuestion1_yn());
        values.put("question2_yn", contact.getQuestion2_yn());
        values.put("question3_yn", contact.getQuestion3_yn());

        values.put("question4_yn", contact.getQuestion4_yn());
        values.put("question1_note", contact.getQuestion1_note());
        values.put("question2_note", contact.getQuestion2_note());
        values.put("question3_note", contact.getQuestion3_note());
        values.put("question4_note", contact.getQuestion4_note());
        values.put("client_sign", contact.getClient_sign());
        values.put("client_sign_name", contact.getClient_sign_name());
        values.put("caregiver_sign", contact.getCaregiver_sign());
        values.put("additional_comments", contact.getAdditional_comments());// Contact Phone
        values.put("checkout_reason", contact.getCheckOutReason());

        values.put("personalcare_time", contact.getPersonalCareTime());
        values.put("respitecare_time", contact.getRespiteCareTime());
        values.put("personalcare_activities", contact.getPersonalCareActivities());
        values.put("respitecare_activities", contact.getRespiteCareActivities());// Contact Phone
        values.put("total_unit", contact.getTotalUnit());
        // Inserting Row
        db.insert(CHECKOUT, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addCheckInDetails(CheckInModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sch_id", contact.getSch_id());
        values.put("agency_id", contact.getAgency_id());
        values.put("clientId", contact.getClient_id());
        values.put("user_id", contact.getUser_id()); // Contact Phone
        values.put("checkin_time", contact.getCheckin_time());
        values.put("checkin_date", contact.getCheckin_date());

        values.put("checkin_location", contact.getCheckin_location());
        values.put("location_status", contact.getLocation_status());
        values.put("complete_status", contact.getComplete_status());// Contact Phone
        values.put("clockin_reason_location", contact.getClockin_reason_location());
        values.put("clockin_reason", contact.getClockin_reason());
        // Inserting Row
        db.insert(CHECKIN, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to add the new contact
    public void addScheduleList(ScheduleModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sch_id", contact.getId());
        values.put("name", contact.getName());
        values.put("profileImg", contact.getPic());
        values.put("startTime", contact.getStart_time()); // Contact Phone
        values.put("endTime", contact.getEnd_time());
        values.put("address", contact.getLocation());
        values.put("mobileNo", contact.getCall()); // Contact Phone
        values.put("latitude", contact.getLat());
        values.put("longitude", contact.getLongi());
        values.put("sechudule_status", contact.getStatus());
        values.put("unitRate", contact.getUnitRate());
        values.put("clientId", contact.getClientId());
        values.put("user_id", contact.getUser_id());
        values.put("additional_note", contact.getAdditional_note());
        values.put("startDate", contact.getStartDate() + "");
        values.put("endDate", contact.getEndDate() + "");
        values.put("personalCareTime", contact.getPersonalCareTime() + "");
        values.put("respiteCareTime", contact.getRespiteCareTime() + "");

        // Inserting Row
        db.insert(SCHEDULELIST, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addService(ServiceModel imageModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code_value", imageModel.getId());
        values.put("code_desc", imageModel.getName());


        // Inserting Row
        db.insert(SERVICELIST, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addQuestion(QuestionModel imageModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("questions", imageModel.getQuestions());
        // values.put("code_desc", imageModel.getName());


        // Inserting Row
        db.insert(QUESTIONTABLE, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addSelectedList(ServiceModel imageModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code_value", imageModel.getId());
        values.put("code_desc", imageModel.getName());
        values.put("id", imageModel.getStatus());


        // Inserting Row
        db.insert(SELECTEDLIST, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addSelectedByUser(ServiceModel imageModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code_value", imageModel.getId());
        values.put("code_desc", imageModel.getName());


        // Inserting Row
        db.insert(SELECTED_BY_USER, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addSelectedByUserRespiteCare(ServiceModel imageModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code_value", imageModel.getId());
        values.put("code_desc", imageModel.getName());


        // Inserting Row
        db.insert(SELECTED_BY_USER_RESPITECARE, null, values);
        // Log.v("ID",RATE.toString());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public String getRespiteTimeValue() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + RESPITE_TIME_CONTAINER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String s = null;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                s = cursor.getString(0);
                //contact.setName(cursor.getString(1));

                // Adding contact to list

            } while (cursor.moveToNext());
        }

        // return contact list
        return s;
    }

    public ScheduleModel getSchedule(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SCHEDULELIST, new String[]{"sch_id", "name", "profileImg", "startTime", "endTime", "address", "mobileNo",
                        "latitude", "longitude", "sechudule_status", "unitRate", "clientId", "user_id", "additional_note", "startDate", "endDate", "personalCareTime", "respiteCareTime"}, "sch_id" + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ScheduleModel contact = new ScheduleModel(
                cursor.getString(11),
                cursor.getString(10),
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getString(14),
                cursor.getString(15),
                cursor.getString(16),
                cursor.getString(17));
        // return contact
        return contact;
    }

    public CheckInModel getCheckIn(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CHECKIN, new String[]{"sch_id", "agency_id", "clientId", "user_id", "checkin_time", "checkin_date", "checkin_location",
                        "location_status", "complete_status", "clockin_reason_location", "clockin_reason"}, "sch_id" + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CheckInModel contact = new CheckInModel(
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(9),
                cursor.getString(10)
        );
        // return contact
        return contact;
    }

    ServiceModel getSelected(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SELECTEDLIST, new String[]{"code_value", "code_desc"
                }, "code_value" + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ServiceModel contact = new ServiceModel(cursor.getString(0),
                cursor.getString(1), "");
        // return contact
        return contact;
    }

    ServiceModel getAllServices(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SERVICELIST, new String[]{"code_value", "code_desc"
                }, "code_value" + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ServiceModel contact = new ServiceModel(cursor.getString(0),
                cursor.getString(1), "");
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public ArrayList<ServiceModel> getAllContacts() {
        ArrayList<ServiceModel> contactList = new ArrayList<ServiceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SERVICELIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceModel contact = new ServiceModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<QuestionModel> getAllQuestions() {
        ArrayList<QuestionModel> contactList = new ArrayList<QuestionModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + QUESTIONTABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuestionModel contact = new QuestionModel();
                contact.setQuestions(cursor.getString(0));
                //contact.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<CheckOutModel> getAllCheckOut(String user_id) {
        ArrayList<CheckOutModel> contactList = new ArrayList<CheckOutModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CHECKOUT + " WHERE userId = '" + user_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckOutModel contact = new CheckOutModel();
                contact.setScheduleId(cursor.getString(0));
                contact.setUserId(cursor.getString(1));
                contact.setCheck_out_latitude(cursor.getString(2));
                contact.setCheck_out_longitude(cursor.getString(3));
                contact.setCheckout_date(cursor.getString(4));
                contact.setCheckout_time(cursor.getString(5));
                contact.setQuestion1_yn(cursor.getString(6));
                contact.setQuestion2_yn(cursor.getString(7));
                contact.setQuestion3_yn(cursor.getString(8));

                contact.setQuestion4_yn(cursor.getString(9));
                contact.setQuestion1_note(cursor.getString(10));
                contact.setQuestion2_note(cursor.getString(11));
                contact.setQuestion3_note(cursor.getString(12));
                contact.setQuestion4_note(cursor.getString(13));

                contact.setClient_sign(cursor.getString(14));
                contact.setClient_sign_name(cursor.getString(15));
                contact.setCaregiver_sign(cursor.getString(16));
                contact.setAdditional_comments(cursor.getString(17));
                contact.setCheckOutReason(cursor.getString(18));


                contact.setPersonalCareTime(cursor.getString(19));
                contact.setRespiteCareTime(cursor.getString(20));
                contact.setPersonalCareActivities(cursor.getString(21));
                contact.setRespiteCareActivities(cursor.getString(22));
                contact.setTotalUnit(cursor.getString(23));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<CheckInModel> getAllCheckIn(String user_id, String sch_id) {
        ArrayList<CheckInModel> contactList = new ArrayList<CheckInModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CHECKIN + " WHERE user_id = " + user_id + " AND sch_id = " + sch_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckInModel contact = new CheckInModel();
                contact.setSch_id(cursor.getString(0));
                contact.setAgency_id(cursor.getString(1));
                contact.setClient_id(cursor.getString(2));
                contact.setUser_id(cursor.getString(3));
                contact.setCheckin_time(cursor.getString(4));
                contact.setCheckin_date(cursor.getString(5));
                contact.setCheckin_location(cursor.getString(6));
                contact.setLocation_status(cursor.getString(7));
                contact.setComplete_status(cursor.getString(8));
                contact.setClockin_reason_location(cursor.getString(9));
                contact.setClockin_reason(cursor.getString(10));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<CheckInModel> getAllCompleteCheckIn(String user_id, String complete_status) {
        ArrayList<CheckInModel> contactList = new ArrayList<CheckInModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CHECKIN + " WHERE user_id = '" + user_id + "' AND complete_status = '" + complete_status + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CheckInModel contact = new CheckInModel();
                contact.setSch_id(cursor.getString(0));
                contact.setAgency_id(cursor.getString(1));
                contact.setClient_id(cursor.getString(2));
                contact.setUser_id(cursor.getString(3));
                contact.setCheckin_time(cursor.getString(4));
                contact.setCheckin_date(cursor.getString(5));
                contact.setCheckin_location(cursor.getString(6));
                contact.setLocation_status(cursor.getString(7));
                contact.setComplete_status(cursor.getString(8));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<ScheduleModel> getAllImages(String user_id) {
        ArrayList<ScheduleModel> contactList = new ArrayList<ScheduleModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SCHEDULELIST + " WHERE  user_id = '" + user_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScheduleModel contact = new ScheduleModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setPic(cursor.getString(2));
                contact.setStart_time(cursor.getString(3));
                contact.setEnd_time(cursor.getString(4));
                contact.setLocation(cursor.getString(5));
                contact.setCall(cursor.getString(6));
                contact.setLat(cursor.getString(7));
                contact.setLongi(cursor.getString(8));
                contact.setStatus(cursor.getString(9));
                contact.setUnitRate(cursor.getString(10));
                contact.setClientId(cursor.getString(11));
                contact.setUser_id(cursor.getString(12));
                contact.setAdditional_note(cursor.getString(13));
                contact.setStartDate(cursor.getString(14));
                contact.setEndDate(cursor.getString(15));
                contact.setPersonalCareTime(cursor.getString(16));
                contact.setRespiteCareTime(cursor.getString(17));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public ArrayList<ScheduleModel> getAllImages(String user_id, String date) {
        ArrayList<ScheduleModel> contactList = new ArrayList<ScheduleModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SCHEDULELIST + " WHERE  user_id = '" + user_id + "' AND startDate = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScheduleModel contact = new ScheduleModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setPic(cursor.getString(2));
                contact.setStart_time(cursor.getString(3));
                contact.setEnd_time(cursor.getString(4));
                contact.setLocation(cursor.getString(5));
                contact.setCall(cursor.getString(6));
                contact.setLat(cursor.getString(7));
                contact.setLongi(cursor.getString(8));
                contact.setStatus(cursor.getString(9));
                contact.setUnitRate(cursor.getString(10));
                contact.setClientId(cursor.getString(11));
                contact.setUser_id(cursor.getString(12));
                contact.setAdditional_note(cursor.getString(13));
                contact.setStartDate(cursor.getString(14));
                contact.setEndDate(cursor.getString(15));
                contact.setPersonalCareTime(cursor.getString(16));
                contact.setRespiteCareTime(cursor.getString(17));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public ArrayList<ScheduleModel> getScheduleOnSchid(String user_id, String sch_id) {
        ArrayList<ScheduleModel> contactList = new ArrayList<ScheduleModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SCHEDULELIST + " WHERE  user_id = " + user_id + " AND sch_id = " + sch_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScheduleModel contact = new ScheduleModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setPic(cursor.getString(2));
                contact.setStart_time(cursor.getString(3));
                contact.setEnd_time(cursor.getString(4));
                contact.setLocation(cursor.getString(5));
                contact.setCall(cursor.getString(6));
                contact.setLat(cursor.getString(7));
                contact.setLongi(cursor.getString(8));
                contact.setStatus(cursor.getString(9));
                contact.setUnitRate(cursor.getString(10));
                contact.setClientId(cursor.getString(11));
                contact.setUser_id(cursor.getString(12));
                contact.setAdditional_note(cursor.getString(13));
                contact.setStartDate(cursor.getString(14));
                contact.setEndDate(cursor.getString(15));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<ServiceModel> getAllSelected(String id) {
        ArrayList<ServiceModel> contactList = new ArrayList<ServiceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SELECTEDLIST + " WHERE id = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceModel contact = new ServiceModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setStatus(cursor.getString(2));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<ServiceModel> getAllSelectedByUser() {
        ArrayList<ServiceModel> contactList = new ArrayList<ServiceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SELECTED_BY_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceModel contact = new ServiceModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<ServiceModel> getAllSelectedByUserRespiteCare() {
        ArrayList<ServiceModel> contactList = new ArrayList<ServiceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SELECTED_BY_USER_RESPITECARE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceModel contact = new ServiceModel();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
//    public boolean updateContact(String id, String category, String category_code, String budget, String unit, String unit_code) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_CATEGORY, category);
//        values.put(KEY_CATEGORYID, category_code);
//        values.put(KEY_BUDGET, budget);
//        values.put(KEY_UNIT,unit);
//        values.put(KEY_UNITCODE,unit_code);
//        Log.v("ID+sql",id);
//        db.update(RATE12, values, KEY_ID + " = ?",
//                new String[] { id });
//        // updating row
//        return true;
//    }
//
//    public boolean updateImage(String id, String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(IMAGE_NAME, name);
//
//        Log.v("ID",id);
//        db.update(IMAGE, values, IMAGE_ID + " = ?",
//                new String[] { id });
//        // updating row
//        return true;
//    }
//
//    // Deleting single contact
    public void deleteSelectedByID(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SELECTED_BY_USER, "code_value" + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteSelectedByIDRespiteCare(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SELECTED_BY_USER_RESPITECARE, "code_value" + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteSchBySchId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SCHEDULELIST, "sch_id" + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteCheckIn(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHECKIN, "sch_id" + " = ?",
                new String[]{id});
        db.close();
    }
//
//
//    public void deleteImage(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(IMAGE, IMAGE_ID + " = ?",
//                new String[] { id });
//        db.close();
//    }
//
//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + RATE12;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
}
