package com.example.whatsyourduing;

import java.util.ArrayList;
import java.util.HashMap;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WhatsYourDuing extends Activity {

	RelativeLayout loginView;
	EditText username;
	EditText password;
	TextView username_error;
	TextView password_error;
	TextView login_message;
	Button logIn;
	RelativeLayout registerView;
	EditText email;
	EditText confPassword;
	TextView register_message;
	Button register;
	Handler_sqlite helperDB;
	ArrayList<String> queryResult = new ArrayList<String>();
	String wantedService = "";
	HashMap<String, String> paramsForHttpPOST = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whats_your_duing);
		initLoginViewElements(loginView);
		helperDB = new Handler_sqlite(this);
		//queryResult = helperDB.getUserTableRows();
		//login_message.setText(String.valueOf(queryResult.size()));
		//new POSTConnection().execute("http://www.profe5.com/Profe5-WebService/app.php"); //AsyncTask
	}
	
	public void initLoginViewElements(View v){
		
		loginView = (RelativeLayout) findViewById(R.id.login_view);
		username = (EditText) findViewById(R.id.logUsername);
		username_error = (TextView) findViewById(R.id.username_error);
		password = (EditText) findViewById(R.id.logPassword);
		password_error = (TextView) findViewById(R.id.password_error);
		login_message = (TextView) findViewById(R.id.login_message);
		logIn = (Button) findViewById(R.id.logIn);
		register = (Button) findViewById(R.id.toRegister);
		registerView = (RelativeLayout) findViewById(R.id.register_view);
		registerView.setVisibility(View.GONE);
		loginView.setVisibility(View.VISIBLE);
		username_error.setText("");
		password_error.setText("");
		login_message.setText("");

	}
	
	public void initRegisterViewElements(View v){
		
		registerView = (RelativeLayout) findViewById(R.id.register_view);
		username = (EditText) findViewById(R.id.regUsername);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.regPassword);
		confPassword = (EditText) findViewById(R.id.confPassword);
		register_message = (TextView) findViewById(R.id.register_message);
		register = (Button) findViewById(R.id.register);
		logIn = (Button) findViewById(R.id.regLogIn);
		loginView = (RelativeLayout) findViewById(R.id.login_view);
		loginView.setVisibility(View.GONE);
		registerView.setVisibility(View.VISIBLE);
		register_message.setText("");

	}
	
	public void login(View v){
		
		helperDB.openDB();
		
		wantedService = "Login";
		if(isLoginFormFull(username.getText().toString(), password.getText().toString())){
			/*
			 * Con SQLite
			 * 
			if(helperDB.doesUserExists(username.getText().toString(), password.getText().toString())){
				login_message.setTextColor(Color.DKGRAY);
				login_message.setText(R.string.login_success);
			}else{
				login_message.setTextColor(Color.RED);
				login_message.setText(R.string.login_error);
			}
			*/
			/*
			 * Con HttpPOST
			 */
			paramsForHttpPOST.put("username", username.getText().toString());
			paramsForHttpPOST.put("code", password.getText().toString());
			new POSTConnection().execute("http://www.profe5.com/Profe5-WebService/app.php");
		}
		
		helperDB.closeDB();
	}
	
	public boolean isLoginFormFull(String username, String password){
		
		boolean isLoginFormFull = false;
		
		if(username.trim().length() == 0 && password.trim().length() == 0){
			username_error.setText(R.string.username_error);
			password_error.setText(R.string.password_error);
		}else if(username.trim().length() == 0 && password.trim().length() != 0){
			login_message.setText("");
			username_error.setText(R.string.username_error);
			password_error.setText("");
		}else if(username.trim().length() != 0 && password.trim().length() == 0){
			login_message.setText("");
			username_error.setText("");
			password_error.setText(R.string.password_error);
		}else{
			username_error.setText("");
			password_error.setText("");
			isLoginFormFull = true;
		}
		
		return isLoginFormFull;
		
	}
	
	public void register(View v){
		
		helperDB.openDB();
		
		wantedService = "Register";
		if(isRegisterFormFull(username.getText().toString(), email.getText().toString(), 
		   password.getText().toString(), confPassword.getText().toString())){
			if(isRegisterFormCorrect(email.getText().toString(), password.getText().toString(), 
			   confPassword.getText().toString())){
				/*
				 * Con SQLite
				 * 
				if(!helperDB.isUsernameAlreadyInUse(username.getText().toString())){
					  register_message.setTextColor(Color.DKGRAY);
					  register_message.setText(R.string.register_success);
					  helperDB.insertUser(email.getText().toString(), username.getText().toString(),          
									      password.getText().toString());
				}else{
					register_message.setTextColor(Color.RED);
					register_message.setText(R.string.register_error2);
				}
				*/
				/*
				 * Con HttpPOST
				 */
				paramsForHttpPOST.put("username", username.getText().toString());
				paramsForHttpPOST.put("email", email.getText().toString());
				paramsForHttpPOST.put("code", password.getText().toString());
				new POSTConnection().execute("http://www.profe5.com/Profe5-WebService/app.php");
			}else{
				login_message.setTextColor(Color.RED);
				register_message.setText(R.string.register_error3);
			}
			
		}
		
		helperDB.closeDB();
		
	}
	
	public boolean isRegisterFormFull(String username, String email, String password, String confPassword){
		
		boolean isRegisterFormFull = false;
		
		if(username.trim().length() == 0 || email.trim().length() == 0 || password.trim().length() == 0
		  || confPassword.trim().length() == 0){
			register_message.setTextColor(Color.RED);
			register_message.setText(R.string.register_error1);
		}else{
			isRegisterFormFull = true;
		}
		
		return isRegisterFormFull;
		
	}
	
	public boolean isRegisterFormCorrect(String email,String password, String confPassword){
		
		boolean isRegisterFormCorrect = false;
		
			if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && 
			   password.equals(confPassword)){
				isRegisterFormCorrect = true;
			}
		
		return isRegisterFormCorrect;
		
	}

	
	/*
	 * Esta clase es necesaria para hacer la conexión con el Servidor.
	 * Hay que hacer una tarea asincrónica para que se pueda realizar correctamente
	 * el setContentView().
	 */
	private class POSTConnection extends AsyncTask<String, Void, String>{
		 
		 private final ProgressDialog progressDialog = new ProgressDialog(WhatsYourDuing.this);
		
         protected String doInBackground(String...urls) {
            
        	String postText = "";
        	String url = urls[0];
     		HttpHandler httpHandler = new HttpHandler();
     		postText = httpHandler.setAndReceivePost(url, wantedService, paramsForHttpPOST);
     		return postText;
     		
         }
         
         protected void onPreExecute() {
        	 this.progressDialog.setMessage(getString(R.string.processing)); 
        	 this.progressDialog.show();
         }

         protected void onPostExecute(String postText) {
        	 
        	//login_message.setText(postText);
        	 
        	progressDialog.dismiss();
        	
        	if(wantedService.equals("Login")){
        		if(postText.equals("yes\n")){
        			login_message.setTextColor(Color.DKGRAY);
     				login_message.setText(R.string.login_success);
     			}else{
     				login_message.setTextColor(Color.RED);
     				login_message.setText(R.string.login_error);
     			}
        	}else if(wantedService.equals("Register")){
        		if(postText.equals("yes\n")){
        			register_message.setTextColor(Color.DKGRAY);
     				register_message.setText(R.string.register_success);
     			}else{
     				register_message.setTextColor(Color.RED);
     				register_message.setText(R.string.register_error2);
     			}
        	}
        	wantedService = "";
        	paramsForHttpPOST.clear();
         }
         
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.whats_your_duing, menu);
		return true;
	}

}
