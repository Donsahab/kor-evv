package dextrous.kor.evv.korevv.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.constants.ApiTagConstants;
import dextrous.kor.evv.korevv.model.DocumentCategoryListModel;
import dextrous.kor.evv.korevv.model.DocumentUploadModel;
import dextrous.kor.evv.korevv.model.ResultDocCategoryListModel;
import dextrous.kor.evv.korevv.retrofit.DocCategoryListApiCall;
import dextrous.kor.evv.korevv.retrofit.UploadDocumentApiCall;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

public class DocumentEnterDate extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button upload;
    private EditText expiryDateEt, validDateEt;
    private TextView uploadDocLebel, selctDocLebel, documentname, selectIssueDateLebel, selectExpiryDateLebel, preview;
    private ParseJasonLang parseJasonLang;
    private Spinner selectDocSpinner;
    private String documentType;
    private List<ResultDocCategoryListModel> categoryListModels;
    private Uri imageUri;
    private String imageExtention;
    private int doctyId;
    private String string64;
    private String fileName;
    private String docId = "";
    private String docTypeId;
    private ImageView  imageIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_enter_date);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        parseJasonLang = new ParseJasonLang(DocumentEnterDate.this);

        toolbar = findViewById(R.id.toolber_new);
        expiryDateEt = findViewById(R.id.expiry_date);
        validDateEt = findViewById(R.id.issue_date);

        uploadDocLebel = findViewById(R.id.document_upload_lebel);
        selctDocLebel = findViewById(R.id.select_doc_spinner_lebel);
        documentname = findViewById(R.id.tv_docname);
        upload = findViewById(R.id.upload_button);
        preview = findViewById(R.id.preview);
        imageIcon = findViewById(R.id.image_icon);
        selectDocSpinner = findViewById(R.id.select_document_spinner);
        selectIssueDateLebel = findViewById(R.id.selelct_issue_date_lebel);
        selectExpiryDateLebel = findViewById(R.id.select_expiry_date_lebel);
        expiryDateEt.setHint(parseJasonLang.getJsonToString("please_select_valid_date"));
        validDateEt.setHint(parseJasonLang.getJsonToString("please_select_expiry_date"));
        uploadDocLebel.setText(parseJasonLang.getJsonToString("uploaded_documents"));
        selctDocLebel.setText(parseJasonLang.getJsonToString("select_document_name"));
        selectIssueDateLebel.setText(parseJasonLang.getJsonToString("select_issue_date"));
        selectExpiryDateLebel.setText(parseJasonLang.getJsonToString("select_expiry_date"));
        upload.setText(parseJasonLang.getJsonToString("submit"));
        preview.setText(parseJasonLang.getJsonToString("preview"));
        setSupportActionBar(toolbar);
     //   Objects.requireNonNull(getSupportActionBar()).setTitle(parseJasonLang.getJsonToString("upload_documents"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView titletoolbar = toolbar.findViewById(R.id.toolbartitle);
        titletoolbar.setText(parseJasonLang.getJsonToString("upload_documents"));

        upload.setOnClickListener(this);
        validDateEt.setOnClickListener(this);
        expiryDateEt.setOnClickListener(this);
        preview.setOnClickListener(this);
        categoryListModels = new ArrayList<>();
        getDataFromApi();
        getDatafromIntent();

    }

    private void getDatafromIntent() {
        Intent intent = getIntent();
        imageUri = intent.getParcelableExtra("image");
        imageExtention = intent.getStringExtra("imageformate");
        fileName = intent.getStringExtra("name");
        docId = intent.getStringExtra("docid");
        docTypeId = intent.getStringExtra("doctypeid");
        if (fileName != null)
            documentname.setText(fileName);
        else {
            documentname.setText("Default.jpg");

        }

        assert imageExtention != null;
        if (imageExtention.equalsIgnoreCase("jpg") || imageExtention.equalsIgnoreCase("jpeg") || imageExtention.equalsIgnoreCase("png")) {
            try {
                string64 = AppUtill.bitmapTobase64(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                imageIcon.setImageDrawable(getDrawable(R.drawable.camera_capture_icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            String file_location = AppUtill.getPath(DocumentEnterDate.this, imageUri);
            if (file_location!=null) {
                if (AppUtill.convertFileToByteArray(file_location).equals("")) {
                    //  Utility.showToast(mContext, "File content not found.");
                    Toast.makeText(this, "File content not found.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    string64 = AppUtill.convertFileToByteArray(file_location);
                    imageIcon.setImageDrawable(getDrawable(R.drawable.file_icon));
                }
            }/*else {
                Toast.makeText(this, "File content not found.", Toast.LENGTH_SHORT).show();
                return;
            }*/

        }

    }

    private void getDataFromApi() {
        new DocCategoryListApiCall(DocumentEnterDate.this) {
            @Override
            public void getResponce(DocumentCategoryListModel documentCategoryListModel) {
                categoryListModels = documentCategoryListModel.getResult();
                setDateTOSpinner();
            }
        };
    }

    private void setDateTOSpinner() {
        ResultDocCategoryListModel resultDocCategoryListModel = new ResultDocCategoryListModel();
        resultDocCategoryListModel.setDocType(parseJasonLang.getJsonToString("select_document_type"));
        resultDocCategoryListModel.setDocTypeId(0);
        categoryListModels.add(0, resultDocCategoryListModel);
        ArrayAdapter<ResultDocCategoryListModel> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, categoryListModels);
        selectDocSpinner.setAdapter(adapter);
        if (docId != null && !docId.equalsIgnoreCase(""))
            for (int i = 0; i < categoryListModels.size(); i++) {
                if (categoryListModels.get(i).getDocTypeId() == (Integer.parseInt(docTypeId))) {
                    selectDocSpinner.setSelection(i);
                    selectDocSpinner.setEnabled(false);
                }
            }

        // this will set list of values to spinner
        //  selectDocSpinner.setSelection(strings.indexOf(<value you want to show selected>)));
        selectDocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    documentType = selectDocSpinner.getSelectedItem().toString();
                    doctyId = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_button:
                if (AppUtill.isNetworkAvil(DocumentEnterDate.this)) {
                    if (isAllSelected()) {
                        new UploadDocumentApiCall(DocumentEnterDate.this,
                                categoryListModels.get(doctyId).getDocTypeId() + "",
                                validDateEt.getText().toString(),
                                expiryDateEt.getText().toString(),
                                string64,
                                imageExtention, docId
                        ) {
                            @Override
                            public void getResponce(DocumentUploadModel uploadDocmodel) {
                                startActivity(new Intent(DocumentEnterDate.this, DocumentUploadSuccess.class));
                            }
                        };

                    } else {
                        if (selectDocSpinner.getSelectedItemPosition() <= 0)
                            Toast.makeText(this, parseJasonLang.getJsonToString("select_document_type"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Internet Connection is required", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.expiry_date:
                openDatePicker(expiryDateEt, false);
                break;
            case R.id.issue_date:
                openDatePicker(validDateEt, true);
                break;
            case R.id.preview:
                if (imageExtention.equalsIgnoreCase("jpg") ||
                        imageExtention.equalsIgnoreCase("jpeg") ||
                        imageExtention.equalsIgnoreCase("png")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(imageUri, "image/*");
                    startActivity(intent);

                  /*  Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    myIntent.setData(imageUri);
                    Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
                    startActivity(j);*/
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(imageUri, "application/pdf");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void openDatePicker(EditText editText, boolean b) {
        Calendar calendar = Calendar.getInstance();
        final Calendar startCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                view.setMinDate(System.currentTimeMillis() - 1000);

                String myformat = ApiTagConstants.DATE_FORMAT;

                android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();

                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String date = (String) dateFormat.format(myformat, startCalendar.getTime());
                editText.setText(date);


            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(DocumentEnterDate.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (b)
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        else
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    private boolean isAllSelected() {

        if (selectDocSpinner.getSelectedItemPosition() <= 0) {
            return false;
        } else if (expiryDateEt.getText().toString().trim().isEmpty()) {
            expiryDateEt.setError("Please enter expiry date");
            return false;
        } else if (validDateEt.getText().toString().isEmpty()) {
            expiryDateEt.setError(null);
            validDateEt.setError("Please enter valid date");
            return false;
        }


        expiryDateEt.setError(null);
        validDateEt.setError(null);
        return true;
    }
}
