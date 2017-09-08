package com.example.dharmendra.buddy1;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class AdapterSearch extends BaseAdapter {

    ArrayList<String> mData;
    PcChat activity;
    ArrayList<Long> timelist;
    ArrayList<String> userlist;

    public AdapterSearch(ArrayList<String> search_list,ArrayList<Long> timelist,ArrayList<String> userlist,PcChat activity,Class<ChatMessage> modelClass,int modelLayout) {
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
            result = activity.getLayoutInflater().inflate(R.layout.item_out_message_pc_1, parent, false);
        else
            result = activity.getLayoutInflater().inflate(R.layout.item_in_message_pc_1, parent, false);

        String name=mData.get(position);
        Long messagetime=timelist.get(position);
        TextView messageTime = (TextView) result.findViewById(R.id.message_time);
        TextView messageText = (TextView) result.findViewById(R.id.message_text);
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messagetime));
        messageText.setText(name);

        return result;
    }
}