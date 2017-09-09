package com.example.dharmendra.buddy1;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HashMapAdapter2 extends BaseAdapter {
    private final ArrayList mData;
    ArrayList url1;
    Context context;
    ArrayList countlist;
    ArrayList mData1;
    ArrayList lastmessage;
    ArrayList messagetime;

    public HashMapAdapter2(LinkedHashMap<String, String> map, LinkedHashMap<String, String> map2,LinkedHashMap<String, String> map3,LinkedHashMap<String, String> map4 ,LinkedHashMap<String,String > map1, Context context) {
        countlist=new ArrayList();
        //mData1.addAll(map1.entrySet());
        for ( Map.Entry<String, String> entry : map1.entrySet()) {
            countlist.add(entry.getValue());
        }
        Collections.reverse(countlist);
        mData = new ArrayList();
        mData.addAll(map.entrySet());
       Collections.reverse(mData);
        url1 = new ArrayList();
        for ( Map.Entry<String, String> entry1 : map2.entrySet()) {
            url1.add(entry1.getValue());
        }
        Collections.reverse(url1);
        this.context=context;
        lastmessage=new ArrayList();
        for ( Map.Entry<String, String> entry : map3.entrySet()) {
            lastmessage.add(entry.getValue());
        }
        Collections.reverse(lastmessage);
        messagetime=new ArrayList();
        for ( Map.Entry<String, String> entry : map4.entrySet()) {
            messagetime.add(entry.getValue());
        }
        Collections.reverse(messagetime);
       /** countlist = new ArrayList();
        countlist.addAll(countlist1);
        Collections.reverse(countlist);**/


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
        String lm=lastmessage.get(position).toString();
        String time=messagetime.get(position).toString();
          String count;
        if(countlist.isEmpty()){
            count="0";
        }
        else if(countlist.size()-1>(position)){
            count = countlist.get(position).toString();
            Log.d("qwerty","inside");
        }
        else if(countlist.size()-1==(position)){
            count = countlist.get(position).toString();
            Log.d("qwerty","outside");
        }
        else {
            count = "0";
        }
        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue());
        ImageView imgv=(ImageView) result.findViewById(R.id.imageview);
        Picasso.with(context).load(url11).fit().centerCrop().into(imgv);
        TextView counter=(TextView)result.findViewById(R.id.counter);
        TextView lmv=(TextView)result.findViewById(R.id.textView1);
        TextView timev=(TextView)result.findViewById(R.id.textView2);
        if(time.equals("0")){
            timev.setVisibility(View.GONE);
            Log.d("hjk","inside");
        }
        else {
            long t=Long.parseLong(time);
            String date=DateFormat.format("dd-MM-yyyy", t).toString();
            long c_date=new Date().getTime();
            String format=DateFormat.format("dd-MM-yyyy", c_date).toString();
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String yest=DateFormat.format("dd-MM-yyyy",cal.getTime()).toString();
            if(date.equals(format)){
               String nontime= DateFormat.format("HH:mm:ss",t).toString();
                SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
                try{
                     Date d = f1.parse(nontime);
                    SimpleDateFormat f2 = new SimpleDateFormat("hh:mm");
                    timev.setText(f2.format(d).toUpperCase());
                }
                catch (Exception e){

                }


            }
            else if(date.equals(yest)){
                timev.setText("Yesterday");
            }
            else {
                timev.setText(DateFormat.format("dd-MM-yyyy", t));
                Log.d("hjk", "outside");
            }
        }
        if(count.equals("0")){
            counter.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 8.0f;
            lmv.setLayoutParams(params);
            Log.d("hjk","inside");
        }
        else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 7.0f;
            lmv.setLayoutParams(params);
            counter.setText(count);
            Log.d("hjk","outside");
        }
        if(lm.equals("0")){
            lmv.setVisibility(View.GONE);
            Log.d("hjk","inside");
        }
        else {
            int size=lm.length();
            /**if(size>25){
                String sub=lm.substring(0,25);
                lmv.setText(sub+"...");
            }**/
           // else {
                lmv.setText(lm);
           // }

            Log.d("hjk","outside");
        }
        return result;
    }
}