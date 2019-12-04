package dextrous.kor.evv.korevv.viewholder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dextrous.kor.evv.korevv.R;

public class DocumentViewHolder extends RecyclerView.ViewHolder {

    public TextView updateButton,docnamelebel,expiryDateLebel,statusLebel,docType,expiryDate,tvstatus;
    public DocumentViewHolder(@NonNull View itemView) {
        super(itemView);

        updateButton = itemView.findViewById(R.id.update_button);
        docnamelebel = itemView.findViewById(R.id.document_name_lebel);
        expiryDateLebel = itemView.findViewById(R.id.expiry_date_lebel);
        statusLebel = itemView.findViewById(R.id.status_lebel);
        docType = itemView.findViewById(R.id.document_type);
        expiryDate = itemView.findViewById(R.id.expiry_date);
        tvstatus = itemView.findViewById(R.id.tv_status);




    }
}
