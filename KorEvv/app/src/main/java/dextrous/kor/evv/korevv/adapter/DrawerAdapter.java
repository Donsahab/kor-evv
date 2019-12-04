package dextrous.kor.evv.korevv.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dextrous.kor.evv.korevv.R;
import dextrous.kor.evv.korevv.model.DrawerItem;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.preferences.MyPreferences;

/**
 * Created by darshanz on 7/6/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    public final static int TYPE_HEADER = 0;
    public final static int TYPE_MENU = 1;
    private Context context;
    private LoggedInUser loggedInUser;

    private ArrayList<DrawerItem> drawerMenuList;

    private OnItemSelecteListener mListener;

    public DrawerAdapter(ArrayList<DrawerItem> drawerMenuList, Context context) {
        this.drawerMenuList = drawerMenuList;
        this.context = context;
        loggedInUser = new LoggedInUser(context);

    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_drawer_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);
        }
        return new DrawerViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if (position == 0) {
            if (MyPreferences.getInstance(context).getUSERID().equals("")) {
                holder.header_email.setText(loggedInUser.getLocal_email());
                holder.headerText.setText(loggedInUser.getLocal_name());


//                Glide.with(context)
//                        .load(loggedInUser.getLocal_image())
//                        .fitCenter()
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .error(R.drawable.user) // will be displayed if the image cannot be loaded
//                        .into(holder.drawer_header_icon);

                byte[] decodedString = Base64.decode(loggedInUser.getLocal_image(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.drawer_header_icon.setImageBitmap(decodedByte);

            } else {
                holder.header_email.setText(MyPreferences.getInstance(context).getEMAIL());
                holder.headerText.setText(MyPreferences.getInstance(context).getFIRSTNAME() + " " + MyPreferences.getInstance(context).getLASTNAME());


//                Glide.with(context)
//                        .load(MyPreferences.getInstance(context).getIMAGE())
//                        .fitCenter()
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .error(R.drawable.user) // will be displayed if the image cannot be loaded
//                        .into(holder.drawer_header_icon);

                byte[] decodedString = Base64.decode(MyPreferences.getInstance(context).getIMAGE(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null)
                    holder.drawer_header_icon.setImageBitmap(decodedByte);
                else holder.drawer_header_icon.getResources().getDrawable(R.drawable.schimg);
                //    holder.drawer_header_icon.setImageBitmap(decodedByte);

            }
        } else {
            holder.title.setText(drawerMenuList.get(position - 1).getTitle());
            holder.icon.setImageResource(drawerMenuList.get(position - 1).getIcon());
//            if(position==5&&MyPreferences.getInstance(context).getUSER_ROLE().equals("2")){
//                holder.notification.setVisibility(View.VISIBLE);
//            }else {
//                holder.notification.setVisibility(View.GONE);
//            }

        }
//        Glide.with(context)
//                .load(MyPreferences.getInstance(context).getIMAGE())
//                .fitCenter()
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .error(R.drawable.login_seeker) // will be displayed if the image cannot be loaded
//                .into(holder.drawer_header_icon);
    }

    @Override
    public int getItemCount() {
        return drawerMenuList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_MENU;

    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView headerText, notification, header_email;
        ImageView change_password;
        ImageView icon;
        CircleImageView drawer_header_icon;

        public DrawerViewHolder(View itemView, int viewType) {
            super(itemView);


            if (viewType == 0) {
                headerText = (TextView) itemView.findViewById(R.id.headerText);
                header_email = (TextView) itemView.findViewById(R.id.header_email);
                drawer_header_icon = (CircleImageView) itemView.findViewById(R.id.drawer_header_icon);
                change_password = (ImageView) itemView.findViewById(R.id.change_password);
            } else {
                title = (TextView) itemView.findViewById(R.id.title);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                notification = (TextView) itemView.findViewById(R.id.notification);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());

                }
            });
        }

    }


    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemSelecteListener {
        public void onItemSelected(View v, int position);
    }

}