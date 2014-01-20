package com.example.whatsyourduing;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;


public class Handler_sqlite extends SQLiteOpenHelper{
	
	public Handler_sqlite(Context ctx){
		
		super(ctx, "DuingDB", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String query = "CREATE TABLE user (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"email TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL);";
		db.execSQL(query);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		String query = "DROP TABLE IF EXISTS user";
		db.execSQL(query);
		onCreate(db);
		
	}
	
	public boolean doesUserExists(String username, String password){
		
		boolean doesUserExists = false;
		String query = "";
		query = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + 
				password + "'";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst()){  //indica que existe por lo menos un registro respuesta
			doesUserExists = true;
		}
		
		return doesUserExists;
	}
	
	public boolean isUsernameAlreadyInUse(String username){
		
		boolean isUsernameAlreadyInUse = true;
		String query = "";
		query = "SELECT * FROM user WHERE username = '" + username + "'";
		Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
		
		if(!cursor.moveToFirst()){  //indica que no existe nisiquiera un registro respuesta
			isUsernameAlreadyInUse = false;
		}
		
		return isUsernameAlreadyInUse;
	}
	
	public void insertUser(String email, String username, String password){
		
		ContentValues values = new ContentValues();
		values.put("email", email);
		values.put("username", username);
		values.put("password", password);
		this.getWritableDatabase().insert("user", null, values);
		
	}
	
	public void deleteUser(String email, String username, String password){
		
		String whereClause = "";
		whereClause = "email = '" + email + "' AND username = '" + username + "' AND password = '" 
					  + password + "'";
		this.getWritableDatabase().delete("user", whereClause, null);
		
	}
	
	public ArrayList<String> getUserTableRows(){
		
		ArrayList<String> result = new ArrayList<String>();
		int index_id, index_email, index_username, index_password;
		String register = "";
		String columns[] = {_ID, "email", "username", "password"};
		Cursor cursor = this.getReadableDatabase().query("user", columns, null, null, null, null,
						null);
		
		index_id = cursor.getColumnIndex(_ID);
		index_email = cursor.getColumnIndex("email");
		index_username = cursor.getColumnIndex("username");
		index_password = cursor.getColumnIndex("password");
		
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			register  = cursor.getString(index_id) + " " + cursor.getString(index_email) + " " + 
						cursor.getString(index_username) + " " + cursor.getString(index_password) + "\n";
			result.add(register);
		}
		
		return result;
		
	}
	
	public void openDB(){
		
		this.getWritableDatabase();
		
	}
	
	public void closeDB(){
		
		this.close();
		
	}



}
