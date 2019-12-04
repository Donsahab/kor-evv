package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.activity.ScheduleActivity;
import dextrous.kor.evv.korevv.model.ScheduleModel;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.AppUtill;
import dextrous.kor.evv.korevv.util.ParseJasonLang;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context mContext;
    private List<ScheduleModel> mDataSet;
    LoggedInUser loggedInUser;
    ParseJasonLang parseJasonLang;
    private boolean dailyOrWeekly;
    private WeekNextPreviousButtonClick weekNextPreviousButtonClick;
    private boolean isNext = false;

    public interface WeekNextPreviousButtonClick {
        void nextClicked(boolean b) throws ParseException;
        void previousClicked(boolean b);
    }

    public ScheduleAdapter(Context context, List<ScheduleModel> list, boolean dailyOrWeekly) {
        mContext = context;
        mDataSet = list;
        loggedInUser = new LoggedInUser(context);
        parseJasonLang = new ParseJasonLang(context);
        this.dailyOrWeekly = dailyOrWeekly;

    }

    public void setOnItemClickListener(WeekNextPreviousButtonClick weekNextPreviousButtonClick) {
        this.weekNextPreviousButtonClick = weekNextPreviousButtonClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        TextView name_tv, location, date_Tv, status, view_deatils_button;
        ImageView call_icon, map_icon;
        CircleImageView profile_image;
        Button nextButton, previousButton;

        public ViewHolder(View v) {
            super(v);
            // Get the widget reference from the custom layout
            card_view = (CardView) v.findViewById(R.id.card_view);
            name_tv = (TextView) v.findViewById(R.id.name_tv);
            location = (TextView) v.findViewById(R.id.location);
            date_Tv = (TextView) v.findViewById(R.id.date_Tv);
            status = v.findViewById(R.id.status);
            view_deatils_button = v.findViewById(R.id.view_deatils_button);
            call_icon = v.findViewById(R.id.call_icon);
            map_icon = v.findViewById(R.id.map_icon);
            profile_image = v.findViewById(R.id.profile_image);
            nextButton = v.findViewById(R.id.nextbutton);
            previousButton = v.findViewById(R.id.previousbutton);
        }
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_schedule, parent, false);
        ScheduleAdapter.ViewHolder vh = new ScheduleAdapter.ViewHolder(v);
        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, int position) {
        // Get the current color from the data set
        final ScheduleModel scheduleModel = mDataSet.get(position);

        holder.name_tv.setText(scheduleModel.getName());
        if (dailyOrWeekly){
            String substring;
            if(Integer.parseInt(scheduleModel.getStartDate().substring(Math.max(scheduleModel.getStartDate().length() - 2, 0)))<10){
                substring =   (scheduleModel.getStartDate().substring(Math.max(scheduleModel.getStartDate().length() - 2, 0)).substring(1,2));
              //  substring =scheduleModel.getStartDate().substring(Math.max(scheduleModel.getStartDate().length() - 2, 0));
            }else {
                substring =   scheduleModel.getStartDate().substring(Math.max(scheduleModel.getStartDate().length() - 2, 0));
            }
            holder.date_Tv.setText(AppUtill.getDayFromDate(scheduleModel.getStartDate(),scheduleModel.getStart_time()) + " "+substring +", " + scheduleModel.getStart_time() + " To " + scheduleModel.getEnd_time());
        }
        else {
            holder.date_Tv.setText(AppUtill.getDayFromDate(scheduleModel.getStartDate(),scheduleModel.getStart_time()) + ", " + scheduleModel.getStart_time() + " To " + scheduleModel.getEnd_time());
        }
        holder.location.setText(scheduleModel.getLocation());
//        Glide.with(mContext)
//                .load(scheduleModel.getPic())
//                .fitCenter()
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .error(R.drawable.schimg) // will be displayed if the image cannot be loaded
//                .into(holder.profile_image);
        byte[] decodedString = Base64.decode(scheduleModel.getPic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null) {
            holder.profile_image.setImageBitmap(decodedByte);
        } else {
            holder.profile_image.getResources().getDrawable(R.drawable.user);
        }
        holder.view_deatils_button.setText(parseJasonLang.getJsonToString("view_detail"));
        if (scheduleModel.getStatus().equals("1")) {
            holder.status.setText(parseJasonLang.getJsonToString("personal"));
        }
        if (scheduleModel.getStatus().equals("2")) {
            holder.status.setText(parseJasonLang.getJsonToString("respite"));
        }
        holder.view_deatils_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action;
                Intent intent = new Intent(mContext, ScheduleActivity.class);
                intent.putExtra("location", scheduleModel.getLocation());
                intent.putExtra("id", scheduleModel.getId());
                intent.putExtra("time", scheduleModel.getStart_time() + " To " + scheduleModel.getEnd_time());
                intent.putExtra("name", scheduleModel.getName());
                intent.putExtra("phone", scheduleModel.getCall());
                intent.putExtra("pic", scheduleModel.getPic());
                intent.putExtra("client_id", scheduleModel.getClientId());
                intent.putExtra("direction", scheduleModel.getLat() + "," + scheduleModel.getLongi());
                intent.putExtra("additional_notes", scheduleModel.getAdditional_note());
                intent.putExtra("start_date", scheduleModel.getStartDate());
                intent.putExtra("end_date", scheduleModel.getEndDate());
                intent.putExtra("personal_care_time",scheduleModel.getPersonalCareTime());
                intent.putExtra("respite_care_time",scheduleModel.getRespiteCareTime());
                intent.putExtra("check",true);
                mContext.startActivity(intent);
            }
        });

        holder.call_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + scheduleModel.getCall()));
                mContext.startActivity(callIntent);
            }
        });
        //  String parts[]=scheduleModel.getDirection().split(",");
        final String lat = scheduleModel.getLat();
        final String longi = scheduleModel.getLongi();
        holder.map_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = Double.parseDouble(lat);
                String value;
                double longitude = Double.parseDouble(longi);
                String label = "I'm Here!";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                mContext.startActivity(mapIntent);

            }
        });
        holder.nextButton.setText(parseJasonLang.getJsonToString("next"));
        holder.previousButton.setText(parseJasonLang.getJsonToString("previous"));
        holder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    weekNextPreviousButtonClick.nextClicked(true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekNextPreviousButtonClick.previousClicked(false);
            }
        });

        if (AppUtill.isNetworkAvil(mContext)) {
            if (position == mDataSet.size() - 1 && dailyOrWeekly) {
                holder.nextButton.setVisibility(View.VISIBLE);
                holder.previousButton.setVisibility(View.VISIBLE);
                if (isNext) {
                    holder.nextButton.setEnabled(false);
                    holder.previousButton.setEnabled(true);
                   /* holder.nextButton.setVisibility(View.GONE);
                    holder.previousButton.setVisibility(View.VISIBLE);*/
                } else {
                    holder.nextButton.setEnabled(true);
                    holder.previousButton.setEnabled(false);


                   /* holder.nextButton.setVisibility(View.VISIBLE);
                    holder.previousButton.setVisibility(View.GONE);*/
                }

            } else {
                holder.nextButton.setVisibility(View.GONE);
                holder.previousButton.setVisibility(View.GONE);
            }
        } else {
            holder.nextButton.setVisibility(View.GONE);
            holder.previousButton.setVisibility(View.GONE);
        }
        holder.nextButton.setVisibility(View.GONE);
        holder.previousButton.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        // Count the items
        return mDataSet.size();
    }

    public void updateList(List<ScheduleModel> mDataSet, boolean dailyOrWeekly,boolean isNext) {
        this.mDataSet = mDataSet;
        this.dailyOrWeekly = dailyOrWeekly;
        this. isNext = isNext;
        notifyDataSetChanged();
    }
}
