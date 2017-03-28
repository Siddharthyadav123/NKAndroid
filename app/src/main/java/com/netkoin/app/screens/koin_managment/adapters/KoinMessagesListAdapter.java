package com.netkoin.app.screens.koin_managment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.entities.Message;
import com.netkoin.app.screens.koin_managment.fragments.KoinMessagesFragment;
import com.netkoin.app.utils.DateTimeUtils;

import java.util.ArrayList;

/**
 * Created by ashishkumarpatel on 1/23/2017.
 */
public class KoinMessagesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Message> messages = null;
    private int currentSegmentFilter;

    public KoinMessagesListAdapter(Context context, ArrayList<Message> messages, int currentSegmentFilter) {
        this.context = context;
        this.messages = messages;
        this.currentSegmentFilter = currentSegmentFilter;
    }

    @Override
    public int getCount() {
        if (messages != null)
            return messages.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_messages, null);
        }

        TextView lblNoOfKoinTransferedReceived = (TextView) convertView.findViewById(R.id.lblNoOfKoinTransferedReceived);
        TextView lblEmailToFrom = (TextView) convertView.findViewById(R.id.lblEmailToFrom);
        TextView textKoinCount = (TextView) convertView.findViewById(R.id.textKoinCount);
        TextView textEmailToFrom = (TextView) convertView.findViewById(R.id.textEmailToFrom);
        TextView activityTimeTextView = (TextView) convertView.findViewById(R.id.activityTimeTextView);
        TextView textMessage = (TextView) convertView.findViewById(R.id.textMessage);

        Message message = messages.get(position);

        textKoinCount.setText(message.getKoins() + "");
        textMessage.setText(message.getMessage());
        activityTimeTextView.setText(DateTimeUtils.getComparedDateText(message.getCreated()));

        if (currentSegmentFilter == KoinMessagesFragment.TYPE_SENT) {
            lblNoOfKoinTransferedReceived.setText("No of Koin Transferred:");
            lblEmailToFrom.setText("Transfer To:");
            textEmailToFrom.setText(message.getReceiver().getEmail());
        } else {
            lblNoOfKoinTransferedReceived.setText("No of Koin Received:");
            lblEmailToFrom.setText("Received From:");
            textEmailToFrom.setText(message.getSender().getEmail());
        }
        return convertView;
    }

    public void refreshAdapter(ArrayList<Message> messages, int currentSegmentFilter) {
        this.messages = messages;
        this.currentSegmentFilter = currentSegmentFilter;
        notifyDataSetChanged();
    }
}
