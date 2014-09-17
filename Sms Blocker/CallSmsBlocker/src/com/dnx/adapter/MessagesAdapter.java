package com.dnx.adapter;

import java.util.ArrayList;
import java.util.Date;

import com.dnx.callsmsblocker.R;
import com.dnx.model.BlockedMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessagesAdapter extends BaseAdapter {

	Context context;
	ArrayList<BlockedMessage> msgs;
	
	public MessagesAdapter(Context context, ArrayList<BlockedMessage> msgs){
		this.context = context;
		this.msgs = msgs;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgs.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return msgs.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.msg_layout, parent, false);
			holder = new ViewHolder();
			holder.message = (TextView) view.findViewById(R.id.tvMsg);
			holder.date = (TextView) view.findViewById(R.id.tvDataTime);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.message.setText(msgs.get(pos).getMessage());
		
		holder.date.setText(new Date(Long.parseLong(msgs.get(pos).getDataTime())).toString());
		
		return view;
	}
	
	class ViewHolder{
		TextView message, date;
	}

}
