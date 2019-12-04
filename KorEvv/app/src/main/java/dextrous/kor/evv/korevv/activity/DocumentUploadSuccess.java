package dextrous.kor.evv.korevv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

public class DocumentUploadSuccess extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button done;
    private TextView successTextLebel;
    private ParseJasonLang parseJasonLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_success);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());
        parseJasonLang = new ParseJasonLang(DocumentUploadSuccess.this);
        toolbar = findViewById(R.id.toolber_new);
        setSupportActionBar(toolbar);

        done = findViewById(R.id.done);
        successTextLebel = findViewById(R.id.success_text_lebel);
        successTextLebel.setText(parseJasonLang.getJsonToString("document_upload_success"));
        done.setText(parseJasonLang.getJsonToString("done"));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(parseJasonLang.getJsonToString("upload_documents"));
        done.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
