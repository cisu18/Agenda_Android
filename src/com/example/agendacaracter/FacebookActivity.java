package com.example.agendacaracter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.reutilizables.Util;
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
		Util.MostrarDialog(this);
		Log.e("Estado", "On create");
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_facebook);

	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
		}
	};

	public void setUserData() {
		Request.newMeRequest(Session.getActiveSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							Log.e("Usuario:", "" + user);
							SharedPreferences prefe = getSharedPreferences(
									"user", Context.MODE_PRIVATE);
							Editor editor = prefe.edit();
							editor.putString("id", "1");
							editor.putString("username", "usuario");
							editor.putString("useremail", "email");
							editor.commit();
							Intent in = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(in);
							finish();
						} else {

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
		if (Session.getActiveSession().isOpened()) {
			setUserData();
		} else if (Session.getActiveSession() != null) {
			Session.getActiveSession().openForRead(
					new Session.OpenRequest(this).setCallback(statusCallback));
			setUserData();
		} else {
			i = new Intent(this, Login.class);			
			startActivity(i);
			finish();
			Toast.makeText(FacebookActivity.this, "Error al iniciar sesión.",
					Toast.LENGTH_LONG).show();
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
