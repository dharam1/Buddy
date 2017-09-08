package com.example.dharmendra.buddy1;

import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class AdapterSearch1 extends BaseAdapter {

    ArrayList<String> mData;
    Chat activity;
    ArrayList<Long> timelist;
    ArrayList<String> userlist;
    ArrayList<String> nicklist;

    public AdapterSearch1(ArrayList<String> search_list, ArrayList<Long> timelist, ArrayList<String> userlist,ArrayList<String> nicklist, Chat activity, Class<ChatMessage> modelClass, int modelLayout) {
        mData = new ArrayList();
        this.userlist=userlist;
        this.timelist=timelist;
        this.mData=search_list;
        this.activity=activity;
        this.nicklist=nicklist;


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
            result = activity.getLayoutInflater().inflate(R.layout.item_out_message_1, parent, false);
        else
            result = activity.getLayoutInflater().inflate(R.layout.item_in_message_1, parent, false);

        String name=mData.get(position);
        Long messagetime=timelist.get(position);
        String nickname=nicklist.get(position);
        TextView messageTime = (TextView) result.findViewById(R.id.message_time);
        TextView messageuser = (TextView) result.findViewById(R.id.message_user);
        TextView messageText = (TextView) result.findViewById(R.id.message_text);
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messagetime));
        messageText.setText(name);
        messageuser.setText(nickname);
        return result;
    }
}