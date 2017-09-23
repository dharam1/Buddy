/**Hash Map Adapter**/
package com.example.dharmendra.buddy1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dharmendra on 02-06-2017.
 */

public class HashMapAdapter2 extends BaseAdapter {
    ArrayList<connection_class> con;
    private ArrayList mData;
    Context context;


    public HashMapAdapter2(LinkedHashMap<String, String> map, ArrayList<connection_class> con1, Context context) {
        Log.d("POPKLLL", con1.size() + "");
        mData = new ArrayList<>();
        mData.addAll(map.entrySet());
        con = new ArrayList<>();
        this.con = con1;
        this.context = context;

    }

    @Override
    public int getCount() {
        return con.size();
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
        Log.d("POPKLL", "POPKL_EXE");
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view, parent, false);
        } else {
            result = convertView;
        }

        LinkedHashMap.Entry<String, String> item = getItem(position);
        String name = con.get(position).getName();
        String url11 = con.get(position).getUrl();
        String lm = con.get(position).getMessage();
        String time = String.valueOf(con.get(position).getTime());
        String type = con.get(position).getType();
        CircleImageView typev = (CircleImageView) result.findViewById(R.id.type);
        Log.d("POPKLLL", name + "" + url11 + "" + lm + "" + time + "" + type);

        if (type.equals("facebook"))
            typev.setImageResource(R.drawable.ic_active_fb);
        else if (type.equals("buddy"))
            typev.setImageResource(R.mipmap.ic_launcher);

        String count = con.get(position).getCount();

        // TODO replace findViewById by ViewHolder
        //((TextView) result.findViewById(R.id.textView)).setText(String.valueOf(item.getKey()));
        ((TextView) result.findViewById(R.id.textView)).setText(name);
        ImageView imgv = (ImageView) result.findViewById(R.id.imageview);
        Picasso.with(context).load(url11).fit().centerCrop().into(imgv);
        TextView counter = (TextView) result.findViewById(R.id.counter);
        TextView lmv = (TextView) result.findViewById(R.id.textView1);
        TextView timev = (TextView) result.findViewById(R.id.textView2);
        LinearLayout ll = (LinearLayout) result.findViewById(R.id.ll);
        LinearLayout ll2 = (LinearLayout) result.findViewById(R.id.ll2);
        if (time.equals("0")) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.weight = 2.0f;
            ll.setLayoutParams(params);
            ll2.setVisibility(View.GONE);
            Log.d("hjk", "inside");
        } else {
            long t = Long.parseLong(time);
            String date = DateFormat.format("dd/MM/yyyy", t).toString();
            long c_date = new Date().getTime();
            String format = DateFormat.format("dd/MM/yyyy", c_date).toString();
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String yest = DateFormat.format("dd/MM/yyyy", cal.getTime()).toString();
            if (date.equals(format)) {
                String nontime = DateFormat.format("HH:mm:ss", t).toString();
                SimpleDateFormat f1 = new SimpleDateFormat("HH:mm");
                try {
                    Date d = f1.parse(nontime);
                    SimpleDateFormat f2 = new SimpleDateFormat("hh:mm");
                    timev.setText(f2.format(d).toUpperCase());
                } catch (Exception e) {

                }


            } else if (date.equals(yest)) {
                timev.setText("YESTERDAY");
            } else {
                timev.setText(DateFormat.format("dd/MM/yyyy", t));
                Log.d("hjk", "outside");
            }
        }
        if (count.equals("0")) {
            counter.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 8.0f;
            lmv.setLayoutParams(params);
            Log.d("hjk", "inside");
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lmv.getLayoutParams();
            params.weight = 7.0f;
            lmv.setLayoutParams(params);
            counter.setText(count);
            Log.d("hjk", "outside");
        }
        if (lm.equals("0")) {
            lmv.setVisibility(View.GONE);
            Log.d("hjk", "inside");
        } else {
            int size = lm.length();

            lmv.setText(lm);
        }

        Log.d("hjk", "outside");

        return result;
    }
}