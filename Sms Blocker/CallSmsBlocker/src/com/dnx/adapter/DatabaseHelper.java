package com.dnx.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.dnx.callsmsblocker.MainActivity;
import com.dnx.model.BlockedMessage;
import com.dnx.model.Person;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "blocker.db";
	
	private static final String TABLE_CALL_BLOCK_NUMBERS = "cbnumbers";
	private static final String TABLE_MSG_BLOCK_NUMBERS = "mbnumbers";
	private static final String TABLE_BLOCKED_MESSAGES = "messages";
	private static final String TABLE_LOG = "log";
	
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_NUMBER = "number";
	private static final String KEY_SENDER_ID = "sender_id";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_DATETIME = "datetime";
	
	
	private static final String[] NUMBER_COLUMNS = {KEY_ID, KEY_NAME, KEY_NUMBER};
	private static final String[] MESSAGE_COLUMNS = {"id", "sender_id", "message", "datetime"};
	
	Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String numbersQuery = "create table " + 
				TABLE_MSG_BLOCK_NUMBERS + " ( " +
				KEY_ID + " integer primary key autoincrement, " + 
				KEY_NAME + " text, "+
				KEY_NUMBER + " text )";
		
		String messagesQuery = "create table " + 
				TABLE_BLOCKED_MESSAGES + " ( " +
				KEY_ID + " integer primary key autoincrement, " +
				KEY_SENDER_ID + " text, " +
				KEY_MESSAGE + " text, "+
				KEY_DATETIME + " text )";
		
		String logQuery = "create table " + 
				TABLE_LOG + " ( " +
				KEY_ID + " integer primary key autoincrement, " +
				KEY_MESSAGE + " text )";
		
		db.execSQL(numbersQuery);
		db.execSQL(messagesQuery);
		db.execSQL(logQuery);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG_BLOCK_NUMBERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKED_MESSAGES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
		
		this.onCreate(db);

	}
	
	public void addMsgNumber(Person p){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, p.getName());
		values.put(KEY_NUMBER, p.getNumber());
		
		db.insert(TABLE_MSG_BLOCK_NUMBERS, null, values);
		
		db.close();
		addLog("Blocked number added '" + p.getName() + "'");
	}
	
	public void deleteMsgNumber(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String name = getPerson(id).getName();
		db.delete(TABLE_MSG_BLOCK_NUMBERS, "id = ?", new String[]{String.valueOf(id)});
		db.close();
		addLog("Blocked Person '" + name + "' deleted");
	}
	
	public boolean isExists(String number){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_MSG_BLOCK_NUMBERS + 
				" WHERE number = ? or number = ?", 
				new String[]{number, MainActivity.toggleNumber(context, number)});
		if(cur != null){
			if(cur.getCount() != 0)
				return true;
		}
		return false;
	}
	
	public Person getPerson(String number){
		SQLiteDatabase db = this.getReadableDatabase();
		Person p = null;
		Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_MSG_BLOCK_NUMBERS + " WHERE number = ?", new String[]{number});
		if(cur != null){
			p = new Person(cur.getInt(0), cur.getString(1), cur.getString(2));	
		}
		return p;
	}
	
	public ArrayList<Person> getAllMsgNumbers(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Person> persons = new ArrayList<Person>();
		Cursor cur = db.rawQuery("SELECT * FROM " +
				TABLE_MSG_BLOCK_NUMBERS , 
				null);
		if(cur.moveToFirst()){
			do{
				Person p = new Person(cur.getInt(0), cur.getString(1), cur.getString(2));
				persons.add(p);
			}while(cur.moveToNext());
		}
		return persons;
	}
	
	public void addMessage(BlockedMessage msg){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SENDER_ID, msg.getSenderId());
		values.put(KEY_MESSAGE, msg.getMessage());
		values.put(KEY_DATETIME, msg.getDataTime());
		db.insert(TABLE_BLOCKED_MESSAGES, null, values);
		db.close();
		addLog("Message blocked, sent from " + getPerson(msg.getSenderId()).getName());
	}
	
	public void deleteMessage(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BLOCKED_MESSAGES, "id = ?", new String[]{String.valueOf(id)});
		db.close();
		addLog("Message deleted, id = " + String.valueOf(id));
	}
	
	public void deleteMessages(int senderId){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BLOCKED_MESSAGES, "sender_id = ?", new String[]{String.valueOf(senderId)});
		db.close();
		addLog("Messages deleted sent from " + getPerson(senderId).getName());
	}
	
	public ArrayList<BlockedMessage> getMessages(int senderId){
		ArrayList<BlockedMessage> msgs = new ArrayList<BlockedMessage>();
		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor cur = db.rawQuery("select * from messages where sender_id = " + String.valueOf(senderId), null);
		Cursor cur = db.query(TABLE_BLOCKED_MESSAGES, MESSAGE_COLUMNS, KEY_SENDER_ID + " = ?", new String[]{
				String.valueOf(senderId)
		}, null, null, KEY_DATETIME + " desc");
		
		if(cur.moveToFirst()){
			do{
				BlockedMessage msg = new BlockedMessage(cur.getInt(0), 
						cur.getInt(1), cur.getString(2), cur.getString(3));
				msgs.add(msg);
			}while(cur.moveToNext());
		}
		return msgs;
	}
	
	public int getMessagesCount(int senderId){
		return getMessages(senderId).size();
	}
	
	public int getSenderId(String sender){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery("select * from " + 
				TABLE_MSG_BLOCK_NUMBERS + " where " + 
				KEY_NUMBER + " = '" + sender + "' or " + 
				KEY_NUMBER + " = '" + MainActivity.toggleNumber(context, sender) + "'", null);
		if(cur.moveToFirst())
			return cur.getInt(0);
		return 0;
	}
	
	public Person getPerson(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery("select * from " + 
				TABLE_MSG_BLOCK_NUMBERS + " where " + 
				KEY_ID + " = " + id + "", null);
		Person p = new Person(0, "", "");
		if(cur.moveToFirst()){
			p = new Person(cur.getInt(0), cur.getString(1), cur.getString(2));
		}
		return p;
	}
	
	public void addLog(String message){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, message + " at " + new Date(Calendar.getInstance().getTimeInMillis()).toString());
		db.insert(TABLE_LOG, null, values);
		db.close();
	}
	
	public ArrayList<String> getLog(){
		ArrayList<String> log = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + TABLE_LOG + " order by id desc", null);
		if(c.moveToFirst()){
			do{
				log.add(c.getString(1));
			}while(c.moveToNext());
		}
		return log;
	}
	
	public void clearLog(){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_LOG, null, null);
		db.close();
	}
}
