package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.BillingSummaryOnInternet;
import dextrous.kor.evv.korevv.model.BillingModel;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {

    private Context mContext;
    private List<BillingModel> mDataSet;

    public BillingAdapter(Context context, List<BillingModel> list){
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView card_view;
        TextView name_tv,location,date_Tv;//status,view_deatils_button;
       // ImageView call_icon,map_icon;
        CircleImageView profile_image;


        public ViewHolder(View v){
            super(v);
            // Get the widget reference from the custom layout
            card_view = (CardView) v.findViewById(R.id.card_view);
            name_tv = (TextView) v.findViewById(R.id.name_tv);
            location = (TextView) v.findViewById(R.id.location);
            date_Tv = (TextView) v.findViewById(R.id.date_Tv);
          //  status = (TextView) v.findViewById(R.id.status);
          //  view_deatils_button = (TextView) v.findViewById(R.id.view_deatils_button);
          //  call_icon = (ImageView) v.findViewById(R.id.call_icon);
           // map_icon = (ImageView) v.findViewById(R.id.map_icon);
            profile_image = (CircleImageView) v.findViewById(R.id.profile_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_billing,parent,false);
        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Get the current color from the data set
        BillingModel scheduleModel = mDataSet.get(position);

        holder.name_tv.setText(scheduleModel.getName());
        holder.date_Tv.setText(scheduleModel.getCall());
        holder.location.setText(scheduleModel.getLocation());


        byte[] decodedString = Base64.decode(scheduleModel.getPic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null) {
            holder.profile_image.setImageBitmap(decodedByte);
        } else {
            holder.profile_image.getResources().getDrawable(R.drawable.user);
        }
      //  holder.profile_image.setImageBitmap(decodedByte);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String action;
                Intent intent = new Intent(mContext, BillingSummaryOnInternet.class);
                intent.putExtra("billId",scheduleModel.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        // Count the items
        return mDataSet.size();
    }

}
