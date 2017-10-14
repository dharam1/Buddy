package com.example.dharmendra.buddy1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class PcChatAdapterSearch extends BaseAdapter {

    ArrayList<String> mData;
    PcChat activity;
    ArrayList<Long> timelist;
    ArrayList<String> userlist;

    public PcChatAdapterSearch(ArrayList<String> search_list,ArrayList<Long> timelist,ArrayList<String> userlist,PcChat activity,Class<ChatMessage> modelClass,int modelLayout) {
        mData = new ArrayList();
        this.userlist=userlist;
        this.timelist=timelist;
        this.mData=search_list;
        this.activity=activity;


    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final View result;
        String user=userlist.get(position);
       if (user.equals(activity.getLoggedInUserName()))
            result = activity.getLayoutInflater().inflate(R.layout.item_out_message, parent, false);
        else
            result = activity.getLayoutInflater().inflate(R.layout.item_in_message, parent, false);

        String name=mData.get(position);
        Long messagetime=timelist.get(position);
        TextView messageuser=(TextView)result.findViewById(R.id.message_user);
        messageuser.setVisibility(View.INVISIBLE);
        TextView messageTime = (TextView) result.findViewById(R.id.message_time);
        TextView messageText = (TextView) result.findViewById(R.id.message_text);
        String nontime= DateFormat.format("HH:mm:ss", messagetime).toString();
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
        try{
            Date d = f1.parse(nontime);
            SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
            messageTime.setText(f2.format(d).toUpperCase());
        }
        catch (Exception e){

        }
        //messageText.setText(name);
        Spannable spannable = new SpannableString(name);
        ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {}}, new int[] { Color.YELLOW });
        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

        spannable.setSpan(highlightSpan, 0,name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageText.setText(spannable);;

        return result;
    }
}