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
		
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");		

		TextView codigoreserva = (TextView)findViewById(R.id.txv_cabecera);

		codigoreserva.setTypeface(miPropiaTypeFace);
		
		TextView indicaciones = (TextView)findViewById(R.id.txt_descripcion_libro);
		indicaciones.setTypeface(miPropiaTypeFace);
		
		Button aceptar = (Button)findViewById(R.id.btn_reserva_aqui);
		aceptar.setTypeface(miPropiaTypeFace);
		
		
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
