package com.dnx.adapter;

import java.util.ArrayList;

import com.dnx.callsmsblocker.R;
import com.dnx.model.Person;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NumbersListAdapter extends BaseAdapter {

	Context context;
	ArrayList<Person> persons;
	DatabaseHelper db;
	
	public NumbersListAdapter(Context c, ArrayList<Person> p){
		this.context = c;
		persons = p;
		db = new DatabaseHelper(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return persons.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return persons.get(pos);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int pos, View view, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.msg_contacts_listitem, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.tvName);
			holder.number = (TextView) view.findViewById(R.id.tvNumber);
			holder.count = (TextView) view.findViewById(R.id.tvCount);
			holder.delete = (ImageView) view.findViewById(R.id.ivDelete);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		holder.name.setText(persons.get(pos).getName());
		holder.number.setText(persons.get(pos).getNumber());
		holder.count.setText(String.valueOf(db.getMessagesCount(persons.get(pos).getId())));		
		holder.delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Builder(context)
				.setTitle("Delete Sender!")
				.setMessage("Are you sure you want to delete this sender?"+
						" All the messages associated with this number will be deleted from blocked list.")
				.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setPositiveButton("Delete", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						db.deleteMsgNumber(persons.get(pos).getId());
						db.deleteMessages(persons.get(pos).getId());
						persons.remove(pos);
						notifyDataSetChanged();
					}
				})
				.create()
				.show();
				
			}
		});
		
		return view;
	}
	
	class ViewHolder{
		TextView name, number, count;
		ImageView delete;
	}

}
