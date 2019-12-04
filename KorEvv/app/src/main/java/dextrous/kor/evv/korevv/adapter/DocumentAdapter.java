package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.DocumentUploadActivity;
import dextrous.kor.evv.korevv.model.ResultModel;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.ParseJasonLang;
import dextrous.kor.evv.korevv.viewholder.DocumentViewHolder;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentViewHolder> {
    private Context context;
    private ParseJasonLang parseJasonLang;
    private List<ResultModel> resultModel = new ArrayList<>();
    private UpdateDocuments updateDocuments;
    public interface UpdateDocuments{
         void updateDocument(String docId,String docTypeId);
    }
    public DocumentAdapter(Context context) {
        this.context = context;
        parseJasonLang = new ParseJasonLang(context);
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_recycler_item, viewGroup, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder documentViewHolder, int position) {
        documentViewHolder.docnamelebel.setText(parseJasonLang.getJsonToString("document_type"));
        documentViewHolder.expiryDateLebel.setText(parseJasonLang.getJsonToString("expiry_date"));
        documentViewHolder.statusLebel.setText(parseJasonLang.getJsonToString("status"));
        documentViewHolder.updateButton.setText(parseJasonLang.getJsonToString("update"));
        documentViewHolder.docType.setText(resultModel.get(position).getDocType());
        documentViewHolder.expiryDate.setText(AppUtill.convertDateFormateNew(resultModel.get(position).getDocExpireDate()));

        if (AppUtill.compareDateWithCurrentDate(resultModel.get(position).getDocExpireDate())==2) {
            documentViewHolder.tvstatus.setText(parseJasonLang.getJsonToString("expired"));
            documentViewHolder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.color_3));
            documentViewHolder.updateButton.setVisibility(View.VISIBLE);
        } else {
            if (AppUtill.compareDateWithCurrentDate(resultModel.get(position).getDocExpireDate())==1) {
                documentViewHolder.tvstatus.setText(parseJasonLang.getJsonToString("active"));
                documentViewHolder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.black));
                documentViewHolder.updateButton.setVisibility(View.GONE);
            }else {
                if (AppUtill.compareDateWithCurrentDate(resultModel.get(position).getDocExpireDate())==0) {
                    documentViewHolder.tvstatus.setText(parseJasonLang.getJsonToString("will_be_expired"));
                    documentViewHolder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.color_3));
                    documentViewHolder.updateButton.setVisibility(View.VISIBLE);
                }
            }
        }
        documentViewHolder.updateButton.setOnClickListener(v -> updateDocuments.updateDocument(resultModel.get(position).getDocId(),resultModel.get(position).getDocTypeId()));
    }

    @Override
    public int getItemCount() {
        if (resultModel != null)
            return resultModel.size();
        else
            return 0;
    }

    public void updateDocumentList(List<ResultModel> resultModels) {
        resultModel = resultModels;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(UpdateDocuments updateDocuments) {
        this.updateDocuments = updateDocuments;
    }
}
