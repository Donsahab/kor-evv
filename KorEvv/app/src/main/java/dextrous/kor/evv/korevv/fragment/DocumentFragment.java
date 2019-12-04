package dextrous.kor.evv.korevv.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Objects;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.DocumentUploadActivity;
import dextrous.kor.evv.korevv.adapter.DocumentAdapter;
import dextrous.kor.evv.korevv.model.DocumentModel;
import dextrous.kor.evv.korevv.retrofit.CallDocumentApi;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.MyLog;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

import static dextrous.kor.evv.korevv.activity.MainActivity.isConnected;
import static dextrous.kor.evv.korevv.activity.MainActivity.main_title;


public class DocumentFragment extends Fragment implements DocumentAdapter.UpdateDocuments {
    private View view;
    private Button uploadDocument;
    private RecyclerView documentRecycler;
    private ParseJasonLang parseJasonLang;
    private DocumentAdapter documentAdapter;
    private TextView noData,tryAgain;
    private LinearLayout llCheckInternet;
    private String docId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_document, container, false);
        MyLog.showLog("MyActivity", this.getClass().getSimpleName());

        parseJasonLang = new ParseJasonLang(getContext());
        uploadDocument = view.findViewById(R.id.upload_document);
        documentRecycler = view.findViewById(R.id.document_recycler);
        noData = view.findViewById(R.id.no_data_text);
        tryAgain = view.findViewById(R.id.tryAgain);
        llCheckInternet = view.findViewById(R.id.llCheckInternet);

        noData.setText(parseJasonLang.getJsonToString("no_document_available"));
        documentAdapter = new DocumentAdapter(getContext());
        documentAdapter.setOnItemClickListener(this);
        documentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        documentRecycler.setAdapter(documentAdapter);
        uploadDocument.setText(parseJasonLang.getJsonToString("upload_new_documents"));
        uploadDocument.setOnClickListener(v -> startActivity(new Intent(getContext(), DocumentUploadActivity.class)));

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppUtill.isNetworkAvil(Objects.requireNonNull(getContext()))) {
                    try {
                       getDatafromApi();
                       llCheckInternet.setVisibility(View.GONE);
                        uploadDocument.setVisibility(View.VISIBLE);

                        documentRecycler.setVisibility(View.VISIBLE);
                    } catch (Exception ignored) {
                    }
                }
            }
        });
        main_title.setText(parseJasonLang.getJsonToString("documents_list"));
        if (AppUtill.isNetworkAvil(getContext())) {
            getDatafromApi();
            documentRecycler.setVisibility(View.GONE);
        }
        else
        {
            documentRecycler.setVisibility(View.GONE);
            llCheckInternet.setVisibility(View.VISIBLE);
            uploadDocument.setVisibility(View.GONE);
        }
        return view;
    }

    private void getDatafromApi() {
        new CallDocumentApi(getContext()) {
            @Override
            public void getDocurmntResponce(DocumentModel documentModels) {
                sortListData();
                documentAdapter.updateDocumentList(documentModels.getResult());
                if (documentModels.getResult()!=null){
                    if (documentModels.getResult().size()<1){
                        documentRecycler.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }else {
                        documentRecycler.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    }}else {
                    documentRecycler.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        };

    }

    private void sortListData() {

    }

    @Override
    public void updateDocument(String docId,String docTypeId) {
        this.docId = docId;
        Intent intent = new Intent(getContext(),DocumentUploadActivity.class);
      /*  if (docId==null)
            docId = "";*/
        intent.putExtra("docid",docId!=null?docId:"");
        intent.putExtra("doctypeid",docTypeId);
        startActivity(intent);
    }
}
