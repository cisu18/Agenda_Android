package com.example.agendacaracter;

import android.app.Activity;
import android.content.Intent;
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

public class Evaluacion_diaria1 extends Activity implements OnClickListener {

	private Button btnSiguiente;
	public Double puntaje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria1);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);
		title.setTypeface(miPropiaTypeFace);

		TextView indicaciones = (TextView) findViewById(R.id.txt_introduccion);
		indicaciones.setTypeface(miPropiaTypeFace);

		TextView txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(miPropiaTypeFace);
		txvPregunta01
				.setText("¿Cuánto de esta cualidad aplicaste el día de hoy?");

		TextView txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(miPropiaTypeFace);
		txvPregunta02
				.setText("¿Cuánto de esta cualidad aplicaste el día de hoy?");

		btnSiguiente = (Button) findViewById(R.id.btn_siguiente);
		btnSiguiente.setTypeface(miPropiaTypeFace);
		btnSiguiente.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_siguiente:
			
			RadioGroup rdbgPregunta01 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_01);
			RadioGroup rdbgPregunta02 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_02);
		
			RadioButton rdbPregunta01 = (RadioButton) findViewById(rdbgPregunta01.getCheckedRadioButtonId());
			RadioButton rdbPregunta02 = (RadioButton) findViewById(rdbgPregunta02.getCheckedRadioButtonId());
			
			puntaje = Double.parseDouble(rdbPregunta01.getText().toString()) + Double.parseDouble(rdbPregunta02.getText().toString());
			//Toast.makeText(getApplicationContext(),puntaje+"", Toast.LENGTH_SHORT).show();
					
			
			Intent i = new Intent(this, Evaluacion_diaria2.class);
			i.putExtra("puntaje", puntaje);
			startActivity(i);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluacion_diaria1, menu);
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
