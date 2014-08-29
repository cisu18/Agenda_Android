package com.example.agendacaracter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class CrearCuenta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);
		
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		
		EditText crearcuenta = (EditText)findViewById(R.id.editText1);
		crearcuenta.setTypeface(miPropiaTypeFace);
		
		EditText usuario = (EditText)findViewById(R.id.txtUserName);
		usuario.setTypeface(miPropiaTypeFace);
		
		EditText contrasenia = (EditText)findViewById(R.id.EditText01);
		contrasenia.setTypeface(miPropiaTypeFace);
		
		EditText repitecontr = (EditText)findViewById(R.id.txtPass);
		repitecontr.setTypeface(miPropiaTypeFace);
		
		Button registrarse = (Button)findViewById(R.id.btnCrearcuenta);
		registrarse.setTypeface(miPropiaTypeFace);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_cuenta, menu);
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
