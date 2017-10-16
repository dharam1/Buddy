package com.example.dharmendra.buddy1;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class HashMapAdapter2s extends BaseAdapter {
    private final ArrayList mData;
    ArrayList url1;
    Context context;
    ArrayList lastmessage,message_time;

    public HashMapAdapter2s(LinkedHashMap<String, String> map, ArrayList<String> url,ArrayList<String> lastmessage1 ,ArrayList message_time1,Context context) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        Collections.reverse(mData);
        url1 = new ArrayList();
        url1.addAll(url);
        Collections.reverse(url1);
        this.context=context;
        lastmessage=new ArrayList<>();
        this.lastmessage=lastmessage1;
        message_time=new ArrayList();
        this.message_time=message_time1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LinkedHashMap.Entry<String, String> getItem(int position) {
        return (LinkedHashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view, parent, false);
        } else {
            result = convertView;
        }

        LinkedHashMap.Entry<String, String> item = getItem(position);

        String url11=url1.get(position).toString();

        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.counter)).setVisibility(View.GONE);
        ((TextView) result.findViewById(R.id.textView1)).setText(lastmessage.get(position).toString());
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
        ImageView imgv=(ImageView) result.findViewById(R.id.imageview);
        TextView timev=(TextView) result.findViewById(R.id.textView2);
        Picasso.with(context).load(url11).fit().centerCrop().noFade().into(imgv);
        String time=String.valueOf(message_time.get(position));
        long t = Long.parseLong(time);
        String date = DateFormat.format("dd/MM/yyyy", t).toString();
        long c_date = new Date().getTime();
        String format = DateFormat.format("dd/MM/yyyy", c_date).toString();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yest = DateFormat.format("dd/MM/yyyy", cal.getTime()).toString();
        if (date.equals(format)) {
            String nontime = DateFormat.format("HH:mm:ss", t).toString();
            SimpleDateFormat f1 = new SimpleDateFormat("kk:mm");
            try {
                Date d = f1.parse(nontime);
                SimpleDateFormat f2 = new SimpleDateFormat("kk:mm");
                timev.setText(f2.format(d).toUpperCase());
            } catch (Exception e) {

            }


        } else if (date.equals(yest)) {
            timev.setText("YESTERDAY");
        } else {
            timev.setText(DateFormat.format("dd/MM/yyyy", t));
            Log.d("hjk", "outside");
        }
        return result;
    }
}