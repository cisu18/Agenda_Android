package com.example.agendacaracter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Evaluacion_diaria2 extends Activity implements OnClickListener {
	
	public int puntaje;
	AlertDialog alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria2);
		
		
		
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		

        TextView title = (TextView)findViewById(R.id.txt_cabecera);

        title.setTypeface(miPropiaTypeFace);
        
        TextView indicaciones = (TextView)findViewById(R.id.txt_especificaciones);
        indicaciones.setTypeface(miPropiaTypeFace);
        
        TextView tercerapregunta = (TextView)findViewById(R.id.txv_pregunta_03);
        tercerapregunta.setTypeface(miPropiaTypeFace);
        
        Button btnVerResultado = (Button)findViewById(R.id.btn_ver_resultado);
        btnVerResultado.setTypeface(miPropiaTypeFace);
        btnVerResultado.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ver_resultado:
			
			RadioGroup rdbgPregunta03 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_03);		
			RadioButton rdbPregunta03 = (RadioButton) findViewById(rdbgPregunta03.getCheckedRadioButtonId());			
			
			Bundle b = getIntent().getExtras();
			puntaje=b.getInt("puntaje");			
			puntaje =puntaje+Integer.parseInt(rdbPregunta03.getText().toString());
		
			alert = new AlertDialog.Builder(Evaluacion_diaria2.this).create();
			alert.setTitle("Resultado");
			//alert.setIcon(R.drawable.ic_action_accept);
			alert.setMessage("Su puntaje es "+puntaje);
			alert.setButton("Aceptar",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							alert.hide();
						}
					});
			alert.show();
			
			//Intent i = new Intent(this, Evaluacion_diaria2.class);
			//startActivity(i);
			break;
		}
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
