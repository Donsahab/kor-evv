package dextrous.kor.evv.korevv.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

import static dextrous.kor.evv.korevv.constants.ApiTagConstants.OPENGALLARY;
import static dextrous.kor.evv.korevv.constants.ApiTagConstants.TAKE_PICTURE;

public class DocumentUploadActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button nextButton;
    private TextView openGallary, openCamera, select_lebel, takepicture_lebel, fileSupport_lebel, oneImageLebel, documentName;
    private Bitmap currentImage;
    private ImageView selectedImage, crossIcon;
    private ParseJasonLang parseJasonLang;
    private Uri imageUri;
    private ContentValues values;
    private String imageurl, filename, docId = "", docTypeId;
    private CardView docCardView;
    private Group groupSelectgromGallary, grouptakePicture;
    private boolean isTakeOrSelect;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        parseJasonLang = new ParseJasonLang(DocumentUploadActivity.this);
        toolbar = findViewById(R.id.toolber_new);

        setSupportActionBar(toolbar);
      //  Objects.requireNonNull(getSupportActionBar()).setTitle(parseJasonLang.getJsonToString("upload_documents"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView titletoolbar = toolbar.findViewById(R.id.toolbartitle);
        titletoolbar.setText(parseJasonLang.getJsonToString("upload_documents"));
        groupSelectgromGallary = findViewById(R.id.group_select_gallery);
        grouptakePicture = findViewById(R.id.group_take_picture);
        nextButton = findViewById(R.id.next_button);
        openCamera = findViewById(R.id.preview);
        openGallary = findViewById(R.id.select_picture);
        selectedImage = findViewById(R.id.selected_image);
        docCardView = findViewById(R.id.doc_card);
        select_lebel = findViewById(R.id.document_upload_lebel);
        takepicture_lebel = findViewById(R.id.take_picture_from_camera_lebel);
        fileSupport_lebel = findViewById(R.id.file_support_lebel);
        oneImageLebel = findViewById(R.id.only_one_file_lebel);
        crossIcon = findViewById(R.id.cross_icon);
        documentName = findViewById(R.id.pdf_name);

        if (imageUri != null) {
            selectedImage.setVisibility(View.VISIBLE);
        } else {
            selectedImage.setVisibility(View.GONE);
        }
        if (filename != null) {
            documentName.setVisibility(View.VISIBLE);
        } else {
            documentName.setVisibility(View.GONE);
        }

        if (documentName.getVisibility() == View.VISIBLE) {
            crossIcon.setVisibility(View.VISIBLE);
        } else {
            crossIcon.setVisibility(View.GONE);
        }

        nextButton.setOnClickListener(this);
        openGallary.setOnClickListener(this);
        openCamera.setOnClickListener(this);
        crossIcon.setOnClickListener(this);
        nextButton.setText(parseJasonLang.getJsonToString("next"));
        select_lebel.setText(parseJasonLang.getJsonToString("select_from_gallery"));
        takepicture_lebel.setText(parseJasonLang.getJsonToString("take_picture_from_camera"));
        openGallary.setText(parseJasonLang.getJsonToString("browse"));
        openCamera.setText(parseJasonLang.getJsonToString("capture"));
        fileSupport_lebel.setText(parseJasonLang.getJsonToString("file_support_format"));
        oneImageLebel.setText(parseJasonLang.getJsonToString("one_image_per_document"));
        nextButton.setText(parseJasonLang.getJsonToString("next"));

        getIntetenData();

    }

    private void getIntetenData() {
        docId = getIntent().getStringExtra("docid");
        docTypeId = getIntent().getStringExtra("doctypeid");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                if (AppUtill.isNetworkAvil(DocumentUploadActivity.this)) {
                    if (imageUri != null) {
                        Intent intent = new Intent(this, DocumentEnterDate.class);
                        intent.putExtra("image", imageUri);
                        intent.putExtra("imageformate", AppUtill.getfileExtension(imageUri, DocumentUploadActivity.this) == null ?
                                MimeTypeMap.getFileExtensionFromUrl(imageUri.toString()) : AppUtill.getfileExtension(imageUri, DocumentUploadActivity.this));
                        intent.putExtra("name", filename);
                        intent.putExtra("docid", docId);
                        intent.putExtra("doctypeid", docTypeId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Please Select Image To Proceed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preview:
                isTakeOrSelect = false;
                if (Build.VERSION.SDK_INT >= 23) {
                    checkStoragePermission();
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PICTURE);
                }

                break;
            case R.id.select_picture:
                isTakeOrSelect = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    checkStoragePermission();
                } else {
                    selectPic();
                /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*|application/pdf|doc/*");
                startActivityForResult(photoPickerIntent, OPENGALLARY);*/

                }
                break;

            case R.id.cross_icon:
                docCardView.setVisibility(View.GONE);
                selectedImage.setVisibility(View.GONE);
                documentName.setVisibility(View.GONE);
                crossIcon.setVisibility(View.GONE);
                nextButton.setEnabled(false);
                filename=null;
                imageUri = null;
                grouptakePicture.setVisibility(View.VISIBLE);
                groupSelectgromGallary.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes =
                {"image/*", "application/pdf", "application/msword"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), OPENGALLARY);
    }

    public void checkStoragePermission() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission
                .CAMERA);
        int result1 = ActivityCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
           /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PICTURE);*/
            if (isTakeOrSelect) {
                selectPic();
            } else takePic();
        } else {
            ActivityCompat.requestPermissions(DocumentUploadActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    TAKE_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                  /*  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PICTURE);*/
                    if (isTakeOrSelect) {
                        selectPic();
                    } else takePic();

                } else {
                    if (isTakeOrSelect) {
                        selectPic();
                    } else takePic();
                    // Toast.makeText(this, "Deny", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void takePic() {
       /* values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);*/

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        count++;
        String file = dir + count + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        imageUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, TAKE_PICTURE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE) {
            nextButton.setEnabled(true);
            groupSelectgromGallary.setVisibility(View.GONE);
            try {
                docCardView.setVisibility(View.VISIBLE);
                selectedImage.setImageDrawable(getDrawable(R.drawable.camera_capture_icon));

                selectedImage.setVisibility(View.VISIBLE);
                documentName.setVisibility(View.VISIBLE);
                crossIcon.setVisibility(View.VISIBLE);
                currentImage = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                //  selectedImage.setImageBitmap(currentImage);
                imageurl = getRealPathFromURI(imageUri);
                //   selectedImage.setImageURI(imageUri);
                filename = AppUtill.getFileName(imageUri, DocumentUploadActivity.this);
                if (filename != null)
                    documentName.setText(filename);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (resultCode == RESULT_OK && requestCode == OPENGALLARY) {
            docCardView.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            grouptakePicture.setVisibility(View.GONE);
            assert data != null;
            imageUri = data.getData();
            String fileExtention = AppUtill.getfileExtension(imageUri, DocumentUploadActivity.this);
            // imageurl = getRealPathFromURI(imageUri);
            filename = AppUtill.getFileName(imageUri, DocumentUploadActivity.this);
            if (fileExtention.equalsIgnoreCase("pdf") || fileExtention.equalsIgnoreCase("doc")) {
                documentName.setVisibility(View.VISIBLE);
                documentName.setText(filename);
                crossIcon.setVisibility(View.VISIBLE);
                selectedImage.setVisibility(View.VISIBLE);
                selectedImage.setImageDrawable(getDrawable(R.drawable.file_icon));
            } else if (fileExtention.equalsIgnoreCase("jpg") || fileExtention.equalsIgnoreCase("jpeg") ||
                    fileExtention.equalsIgnoreCase("png")) {
                currentImage = AppUtill.uriToImage(data.getData(), DocumentUploadActivity.this);
                selectedImage.setImageDrawable(getDrawable(R.drawable.camera_capture_icon));

                if (currentImage != null) {
                    selectedImage.setVisibility(View.VISIBLE);
                    documentName.setVisibility(View.VISIBLE);
                    documentName.setText(filename);
                    crossIcon.setVisibility(View.VISIBLE);
                    //  selectedImage.setImageBitmap(currentImage);
                }
            } else {
                Toast.makeText(this, "File format is not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
