package dextrous.kor.evv.korevv.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dextrous.kor.evv.korevv.constants.ApiUrl;
import dextrous.kor.evv.korevv.retrofit.RetrofitInterface;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppUtill {

    public static String getWeekStartDate(String date) throws ParseException {
       /* DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, df);
        while (localDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            localDate = localDate.minusDays(1);
        }
        return localDate.format(df);*/

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //int day = 1, month = 7, year = 2018;

        Date date1 = sdf.parse(date);
      /*  year = date1.getYear();
        month = date1.getMonth();
        day = date1.getDay();*/
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        assert date1 != null;
        c.setTime(date1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        return sdf1.format(Objects.requireNonNull(sdf1.parse(sdf1.format(c.getTime()))));//change de day to monday

        // return sdf.format(c.getTime());
    }

    public static String getWeekEndDate(String date) {
       /* DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, df);
        while (localDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            localDate = localDate.minusDays(1);
        }
        return localDate.format(df);*/
        String datenew = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        //int day = 1, month = 7, year = 2018;

        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      /*  year = date1.getYear();
        month = date1.getMonth();
        day = date1.getDay();*/
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        assert date1 != null;
        c.setTime(date1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            datenew = sdf1.format(Objects.requireNonNull(sdf1.parse(sdf1.format(c.getTime()))));//change de day to monday
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datenew;
       // return datenew.substring(0,8)+(Integer.parseInt(datenew.substring(8,10))+2);
        // return sdf.format(c.getTime());
    }

    public static String getDayFromDate(String date, String startTime) {
        String day = null;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt1 = null;
        try {
            dt1 = format1.parse(date + " " + startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEEE");
        if (dt1 != null) {
            day = format2.format(dt1);
        }
        assert day != null;
        return day.substring(0, 3);
    }

    @NonNull
    public static Point getDisplayDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // find out if status bar has already been subtracted from screenHeight
        display.getRealMetrics(metrics);
        int physicalHeight = metrics.heightPixels;
        int statusBarHeight = getStatusBarHeight(context);
        int navigationBarHeight = getNavigationBarHeight(context);
        int heightDelta = physicalHeight - screenHeight;
        if (heightDelta == 0 || heightDelta == navigationBarHeight) {
            screenHeight -= statusBarHeight;
        }

        return new Point(screenWidth, screenHeight);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return (resourceId > 0) ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return (resourceId > 0) ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat(format);
        return mdformat.format(calendar.getTime());

    }

    public static String getNextWeekDate() throws ParseException {
        String currentDate = getCurrentDate("dd-MM-yyyy");
        String weekStartDate = getWeekStartDate(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(weekStartDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        return sdf1.format(c.getTime());
    }

    public static String getPreviousWeekDate() throws ParseException {
        String date = getNextWeekDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        return sdf1.format(c.getTime());
    }

    public static boolean isNetworkAvil(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


    }

    public static RetrofitInterface callRetrofit(Context context, boolean isFile) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(240, TimeUnit.SECONDS)
                .connectTimeout(240, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .addInterceptor(logging)
                // .cache(cache)
                .build();
// add your other interceptors …
// add logging as last interceptor
//        httpClient.addInterceptor(logging);  // <-- this is the important line!
//        String credentials = Credentials.basic("admin", "admin");
        Retrofit retrofit;


        retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();


        RetrofitInterface anInterface = retrofit.create(RetrofitInterface.class);
        return anInterface;
    }

    public static int compareDateWithCurrentDate(String date) {
        int z;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
   /*     Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));*/
        int x = 0;
        try {
            x = strDate.compareTo(sdf.parse(sdf.format(new Date(System.currentTimeMillis()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Long y = 86400000L;
        if (x == 1) {
            z = 1;
        } else {
            if (x == -1) {
                z = 2;
            } else {
                z = 0;
            }
        }
        return z;

        //      return System.currentTimeMillis() > (strDate != null ? strDate.getTime() : 0);
    }

    public static String getfileExtension(Uri uri, Context context) {
        String extension;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    public static String bitmapTobase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    public static Bitmap uriToImage(Uri uri, Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //use the bitmap as you like
        return bitmap;
    }

    public static String changeDateFormate(String date) {

        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        try {
            date1 = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(date1);

      /*  String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(date.replaceAll("-", "/")));*/
    }

    public static String changeDateFormateNew(String date) {

        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        try {
            date1 = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(date1);

      /*  String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(date.replaceAll("-", "/")));*/
    }

    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getTimeFromMillis(String milliseconds) {

        String time = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(milliseconds));

        time = String.format("%2d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));

        if (cal.get(Calendar.AM_PM) == 0)
            time = time + " AM";
        else
            time = time + " PM";

        return time;

    }

    public static int compareDates(String firstDate, String secondDate, String firstTime, String secondTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(firstDate );
            date2 = sdf.parse(secondDate);

            long differenceInMilliseconds = date1.getTime() - date2.getTime();
            long diffenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);
            int days = (int) diffenceInDays;
            if (days == 0) {
                boolean b = checktimings(firstTime, secondTime);
                if (b)
                    return -1;
                else return 1;
            }
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getMillis(String givenTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {
            Date mDate = sdf.parse(givenTime);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int compareDatesNew(String firstDate, String secondDate, String firstTime, String secondTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(firstDate);
            date2 = sdf.parse(secondDate);

            long differenceInMilliseconds = date2.getTime() - date1.getTime();
            long diffenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);
            int days = (int) diffenceInDays;
            if (days > 0)
                return 1;
            else {
                if (days == 0)
                    return 0;
                else
                    return -1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean checktimings(String time, String endtime) {

        String pattern = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if (date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    //|| "home".equalsIgnoreCase(type)
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/Documents/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

               /* final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);*/

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads",
                };
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        e.fillInStackTrace();
                    }
                }
                String mPath = "";
                try {
                    File file = FileUtil.from(context, uri);
                    Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                    mPath = file.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mPath;

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
            //drive path
            else if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }

        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;

    }

    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };


        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String convertFileToByteArray(String path) {
        File file = new File(path);

        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String base64 = Base64.encodeBase64String(byteArray);
        String str = "";
        if (byteArray != null) {
            str = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //  str = new String(encodedBytes);
        } else {
            str = "";
        }

//        String str = "";
//        str = Base64.encodeBase64String(byteArray);

//        try {
//            str = new String(byteArray, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return str;
    }

    public static String calculateHoursFromMillies(long millis) {

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    public static long getMilliesFromhours(String hours) {
        return TimeUnit.HOURS.toMillis(Long.parseLong(hours));
    }

    public static String convertDateFormate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format1 = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        String datenew = null;
        try {
            Date date1 = format.parse(date);
            datenew = format1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datenew;
    }

    public static String convertDateFormateNew(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        String datenew = null;
        try {
            Date date1 = format.parse(date);
            datenew = format1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datenew;
    }

    public static String removeZero(String str) {
        // Count leading zeros
        int i = 0;
        while (i < 1 && str.charAt(i) == '0')
            i++;

        // Convert str into StringBuffer as Strings
        // are immutable.
        StringBuffer sb = new StringBuffer(str);

        // The  StringBuffer replace function removes
        // i characters from given index (0 here)
        sb.replace(0, i, "");

        return sb.toString();  // return in String
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide        }
        }
    }
}
