package com.example.agendacaracter;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookActivity extends FragmentActivity {
	private UiLifecycleHelper uiHelper;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);	
				
		//setContentView(R.layout.activity_facebook);
	}
	
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {			
		}
	};

	public void setUserData() {
		Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
			  @Override
			  public void onCompleted(GraphUser user, Response response) {
			    if (user != null) {
			    	Toast.makeText(FacebookActivity.this,
							"Datos "+user, Toast.LENGTH_LONG).show();
			    	Log.e("Usuario:",""+user);
			    }else{
			    	
			    }
			  }
			}).executeAsync();
	}
	
	public boolean checkPermissions() {
		Session s = Session.getActiveSession();
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}

	public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}

	@Override
	public void onResume() {		
		super.onResume();			
		uiHelper.onResume();
		if(Session.getActiveSession().isOpened()){
			setUserData();
		}
		else if(Session.getActiveSession()!=null){
			Session.getActiveSession().openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			setUserData();
		}else{
			i = new Intent(this, Login.class);
			Toast.makeText(FacebookActivity.this,
					"Error al iniciar sesión.", Toast.LENGTH_LONG).show();
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}
	

}
