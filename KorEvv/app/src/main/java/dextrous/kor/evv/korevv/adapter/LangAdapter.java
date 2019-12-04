package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.model.LangModel;

public class LangAdapter extends RecyclerView.Adapter<LangAdapter.ViewHolder>{

    private Context mContext;
    private List<LangModel> mDataSet;
    private static SingleClickListener sClickListener;
    private static int sSelected = -1;
    public LangAdapter(Context context, List<LangModel> list){
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout english_layout;
        TextView lang_text,location,date_Tv;//status,view_deatils_button;
        // ImageView call_icon,map_icon;
        ImageView lang_logo;


        public ViewHolder(View v){
            super(v);
            // Get the widget reference from the custom layout
            english_layout = v.findViewById(R.id.english_layout);
            lang_text = (TextView) v.findViewById(R.id.lang_text);

            lang_logo = v.findViewById(R.id.lang_logo);
            english_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sSelected = getAdapterPosition();
                    sClickListener.onItemClickListener(getAdapterPosition(), v);

                }
            });
        }
    }
    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(SingleClickListener clickListener) {
        sClickListener = clickListener;
    }
    public interface SingleClickListener {
        void onItemClickListener(int position, View view);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_language,parent,false);
        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Get the current color from the data set
        LangModel scheduleModel = mDataSet.get(position);

        holder.lang_text.setText(scheduleModel.getValue());

        byte[] decodedString = Base64.decode(scheduleModel.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.lang_logo.setImageBitmap(decodedByte);
        if (sSelected == position) {
            holder.english_layout.setBackgroundResource(R.drawable.button_bachground_red);
        }else {
            holder.english_layout.setBackgroundResource(R.drawable.edit_text_job);
        }
    //    holder.
      //  holder.location.setText(scheduleModel.getLocation());




    }

    @Override
    public int getItemCount(){
        // Count the items
        return mDataSet.size();
    }


}
