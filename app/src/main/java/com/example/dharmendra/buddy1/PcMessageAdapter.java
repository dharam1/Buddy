package com.example.dharmendra.buddy1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PcMessageAdapter extends FirebaseListAdapter<ChatMessage> {
    ClipData myClip;
    ClipboardManager clipboard ;
    PcChat activity;
    String user;
    DatabaseReference mDatabase,mDatabase1;
    ArrayList<String> message_list=new ArrayList<>();
    boolean exist;
    Context context;
    String temp;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    String url;
    SparseIntArray map;
    ChatMessage prev = null;
    View prevItem = null;
    int prevPos = Integer.MAX_VALUE;
    int count = 0;
    TextView messageText,messageTime;
    public PcMessageAdapter(PcChat activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref,Context context) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.mDatabase1=ref;
        this.context=context;
        map = new SparseIntArray();
        user=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    @Override
    protected void populateView(View v, final ChatMessage model, final int position) {
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(position == 0){                                               //Set first one always as a date message
            model.setTimeMessage(true);
            String dateString = DateOperations.toString(model.getMessageTime());
            v.findViewById(R.id.message_date).setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.message_date)).setText(dateString);
        }


        if(map.get(position) == 1 || model.timeMessage){                 //Current view has been set as date message

            String dateString = DateOperations.toString(model.getMessageTime());

            v.findViewById(R.id.message_date).setVisibility(View.VISIBLE);

            ((TextView) v.findViewById(R.id.message_date)).setText(dateString);
            //Log.v("hit found", "set as timeMeessage");
        }

        int prev = position - 1;
        if(prev < 0) prev = position;
        {
            ChatMessage chatMessage1 = getItem(prev);

            Date thisDate = new Date(model.getMessageTime());
            Date thatDate = new Date(chatMessage1.getMessageTime());


            if (DateOperations.logComparision(thisDate, thatDate) == 1) {       //this date is ahead of that date, That is, set model as date message
                String dateString = DateOperations.toString(thisDate.getTime());
                model.setTimeMessage(true);
                v.findViewById(R.id.message_date).setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.message_date)).setText(dateString);
                map.put(position, 1);
            }
        }

        int next = position +1;
        if(next == getCount()) next = position;

        {
            ChatMessage chatMessage1 = getItem(next);

            Date thisDate = new Date(model.getMessageTime());
            Date thatDate = new Date(getItem(next).getMessageTime());


            if (DateOperations.logComparision(thisDate, thatDate) == -1) {       //that date is ahead of this date, That is, set next message as date message
                String dateString = DateOperations.toString(thisDate.getTime());
                chatMessage1.setTimeMessage(true);
                map.put(next, 1);
            }
        }




        messageText.setText(model.getMessageText());
        if(model.getMessageUserId().equals(user)){
            messageUser.setVisibility(View.GONE);
        }
        else
                messageUser.setVisibility(View.GONE);
        // Format the date before showing it
        //messageTime.setText(DateFormat.format("dd/MM/yyyy", model.getMessageTime()).toString());
        final String date = DateFormat.format("dd/MM/yyyy", model.getMessageTime()).toString();
        messageTime.setText(DateFormat.format("HH:mm", model.getMessageTime()).toString());
        clipboard=(ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String content =model.getMessageText();
                Log.d("CLip content"," "+model.getMessageText() + "\t" +position +"\tdate:" +date);
                Log.v("visibility", v.findViewById(R.id.message_date).getVisibility() == View.VISIBLE?"visible":"notvisible");
                myClip = ClipData.newPlainText("text",content);
                clipboard.setPrimaryClip(myClip);
                return true;
            }
        });
        this.prev = model;
        prevItem = v;
        prevPos = position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ChatMessage chatMessage = getItem(position);
        if(prev == null) prev = chatMessage;
        if(prevItem == null) prevItem = view;
        if (chatMessage.getMessageUserId().equals(activity.getLoggedInUserName()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

        //generating view
        populateView(view, chatMessage, position);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
