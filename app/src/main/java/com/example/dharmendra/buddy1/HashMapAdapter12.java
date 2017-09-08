package com.example.dharmendra.buddy1;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class HashMapAdapter12 extends BaseAdapter {
    private final ArrayList mData;
    ArrayList activityname;
    ArrayList urllist;
    Context context;
    ArrayList timelist;
    private SparseBooleanArray mSelectedItemsIds;

    public HashMapAdapter12(LinkedHashMap<String, String> map,LinkedHashMap<String, String> map1 ,ArrayList<String> activityname1,ArrayList<String> timelist1,Context context) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        Collections.reverse(mData);
        activityname=new ArrayList();
        activityname.addAll(activityname1);
        Collections.reverse(activityname);
        urllist=new ArrayList();
        for ( Map.Entry<String, String> entry : map1.entrySet()) {
            urllist.add(entry.getValue());
        }
        Collections.reverse(urllist);
        timelist=new ArrayList<>();
        this.timelist=timelist1;
        Collections.reverse(timelist);
        this.context=context;
        mSelectedItemsIds = new  SparseBooleanArray();
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view12, parent, false);
        } else {
            result = convertView;
        }

        LinkedHashMap.Entry<String, String> item = getItem(position);
        String url=urllist.get(position).toString();
        String time=timelist.get(position).toString();
        ImageView imgv=(ImageView) result.findViewById(R.id.imageview);
        Picasso.with(context).load(url).fit().centerCrop().into(imgv);
        String actname=activityname.get(position).toString();
        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.textView)).setText(item.getValue()+" @ "+actname+" accepted your Request.");

        TextView timev=(TextView)result.findViewById(R.id.textView2);
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
                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                timev.setText("Today at "+f2.format(d).toUpperCase());
            }
            catch (Exception e){

            }


        }
        else if(date.equals(yest)){
            String nontime= DateFormat.format("HH:mm:ss",t).toString();
            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
            try{
                Date d = f1.parse(nontime);
                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                timev.setText("Yesterday at "+f2.format(d).toUpperCase());
            }catch (Exception e){

            }

        }

        else {
            String date1=DateFormat.format("dd-MM-yyyy",t).toString();
            String nontime= DateFormat.format("HH:mm:ss",t).toString();
            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
            try{
                Date d = f1.parse(nontime);
                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                timev.setText(date1+" at "+f2.format(d).toUpperCase());
            }
            catch (Exception e){

            }
        }
        /**String date1=DateFormat.format("dd-MM-yyyy",t).toString();
        String nontime= DateFormat.format("HH:mm:ss",t).toString();
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
        try{
            Date d = f1.parse(nontime);
            SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
            timev.setText(date1+" at "+f2.format(d).toUpperCase());
        }
        catch (Exception e){

        }**/
        //timev.setText(DateFormat.format("dd-MM-yyyy (HH:mm)", t));

        return result;
    }
    public void  toggleSelection(int position, ListView listView) {
        selectView(position, !mSelectedItemsIds.get(position),listView);
    }
    public void selectView(int position, boolean value,ListView listView) {
        if (value) {
            mSelectedItemsIds.put(position, value);
            Log.d("dfghj", "1");
            //change color
            listView.getChildAt(position).setBackgroundColor(Color.parseColor("#8CFFFFFF"));
        }
        else {
            mSelectedItemsIds.delete(position);
            Log.d("dfghj","2");
            listView.getChildAt(position).setBackgroundColor(Color.WHITE);
        }
        notifyDataSetChanged();
    }
    public void  removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public  SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}