package com.example.agendacaracter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		
		EditText usuario = (EditText)findViewById(R.id.txtUserName);
		usuario.setTypeface(miPropiaTypeFace);
        
        EditText contrasenia = (EditText)findViewById(R.id.txtPass);
        contrasenia.setTypeface(miPropiaTypeFace);
        
        TextView logincon = (TextView)findViewById(R.id.textView1);
        logincon.setTypeface(miPropiaTypeFace);
        
        Button iniciosesion = (Button)findViewById(R.id.btnRegister);
        iniciosesion.setTypeface(miPropiaTypeFace);
        
        Button crearcuenta = (Button)findViewById(R.id.btnCrearcuenta);
        crearcuenta.setTypeface(miPropiaTypeFace);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
