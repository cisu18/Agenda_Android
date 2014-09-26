package com.example.agendacaracter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class CodigoReserva extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codigo_reserva);	
		
		Typeface tfHelveticaCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");	
		
		Typeface HelveticaBoldTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		TextView codigoreserva = (TextView)findViewById(R.id.txv_cabecera_descripcion);
		codigoreserva.setTypeface(tfHelveticaCond);
		
		TextView indicaciones = (TextView)findViewById(R.id.txv_descripcion_libro);
		indicaciones.setTypeface(tfHelveticaCond);
		
		Button aceptar = (Button)findViewById(R.id.btn_reserva_aqui);
		aceptar.setTypeface(HelveticaBoldTypeFace);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.codigo_reserva, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
