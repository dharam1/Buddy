package com.example.dharmendra.buddy1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
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
    TextView messageText,messageTime;
    public PcMessageAdapter(PcChat activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref,Context context) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.mDatabase1=ref;
        this.context=context;
        user=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }


    @Override
    protected void populateView(View v, final ChatMessage model, int position) {
         messageText = (TextView) v.findViewById(R.id.message_text);
        //TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        //final ImageView img=(ImageView)v.findViewById(R.id.loadimage);
        messageTime = (TextView) v.findViewById(R.id.message_time);

        String message=model.getMessageText();
        TextView messageuser=(TextView)v.findViewById(R.id.message_user);
        messageuser.setVisibility(View.INVISIBLE);
            String date=DateFormat.format("dd-MM-yyyy", model.getMessageTime()).toString();


        //messageTime.setText(DateFormat.format("HH:mm", model.getMessageTime()).toString());
        String date1=DateFormat.format("dd-MM-yyyy", model.getMessageTime()).toString();
        String nontime= DateFormat.format("HH:mm:ss",model.getMessageTime()).toString();
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
        try{
            Date d = f1.parse(nontime);
            SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
            messageTime.setText(f2.format(d).toUpperCase());
        }
        catch (Exception e){

        }
        messageText.setText(model.getMessageText());

        clipboard=(ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String content =model.getMessageText();
                Log.d("ronaldo",content);
                myClip = ClipData.newPlainText("text",content);
                clipboard.setPrimaryClip(myClip);
                return true;
            }
        });

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ChatMessage chatMessage = getItem(position);
        if (chatMessage.getMessageUserId().equals(activity.getLoggedInUserName()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message_pc, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message_pc, viewGroup, false);

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
