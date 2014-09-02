package com.example.agendacaracter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class Evaluacion_diaria2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria2);
		
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		
        TextView title = (TextView)findViewById(R.id.txt_cabecera);
        title.setTypeface(miPropiaTypeFace);
        
        TextView indicaciones = (TextView)findViewById(R.id.textView5);
        indicaciones.setTypeface(miPropiaTypeFace);
        
        TextView tercerapregunta = (TextView)findViewById(R.id.textView2);
        tercerapregunta.setTypeface(miPropiaTypeFace);
        
        Button resultado = (Button)findViewById(R.id.button1);
        resultado.setTypeface(miPropiaTypeFace);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluacion_diaria2, menu);
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
