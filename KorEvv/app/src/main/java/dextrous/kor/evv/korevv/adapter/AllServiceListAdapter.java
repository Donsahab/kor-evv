package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.model.ServiceModel;
import dextrous.kor.evv.korevv.util.DatabaseHandler;

public class AllServiceListAdapter extends RecyclerView.Adapter<AllServiceListAdapter.ViewHolder> {

    private Context mContext;
    private List<ServiceModel> mDataSet;
    DatabaseHandler db ;

    public AllServiceListAdapter(Context context, List<ServiceModel> list){
        mContext = context;
        mDataSet = list;
        db = new DatabaseHandler(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //CardView card_view;
        TextView textView;//,location,date_Tv,status,view_deatils_button;
        ImageView imageView;
        LinearLayout ll1;
     //   CircleImageView profile_image;


        public ViewHolder(View v){
            super(v);
            // Get the widget reference from the custom layout
           // card_view = (CardView) v.findViewById(R.id.card_view);
            textView = (TextView) v.findViewById(R.id.textView);
            ll1 = (LinearLayout) v.findViewById(R.id.ll1);
           // date_Tv = (TextView) v.findViewById(R.id.date_Tv);
           // status = (TextView) v.findViewById(R.id.status);
           // view_deatils_button = (TextView) v.findViewById(R.id.view_deatils_button);
            imageView = (ImageView) v.findViewById(R.id.imageView);
           // map_icon = (ImageView) v.findViewById(R.id.map_icon);
           // profile_image = (CircleImageView) v.findViewById(R.id.profile_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_all_services,parent,false);
        ViewHolder vh = new ViewHolder(v);
        // Return the ViewHolder
        return vh;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        // Get the current color from the data set
        ServiceModel scheduleModel = mDataSet.get(position);

        holder.textView.setText(scheduleModel.getName());

        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(holder.imageView.getVisibility()==View.GONE) {
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.ll1.setBackgroundResource(R.drawable.button_pressed_background);
                    holder.ll1.setElevation(0);
                    db.addSelectedByUser(new ServiceModel(scheduleModel.getId(),scheduleModel.getName(),""));
                    holder.textView.setTextColor(Color.WHITE);
                }else {
                    holder.imageView.setVisibility(View.GONE);
                    db.deleteSelectedByID(scheduleModel.getId());
                    holder.ll1.setBackgroundResource(R.drawable.edit_text_rounded);
                    holder.textView.setTextColor(Color.BLACK);
                    holder.ll1.setElevation(5);
                }
            }
        });


    }

    @Override
    public int getItemCount(){
        // Count the items
        return mDataSet.size();
    }



}
