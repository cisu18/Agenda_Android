package com.example.agendacaracter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CodigoReserva extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
<<<<<<< HEAD
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
		
		
=======
		setContentView(R.layout.activity_codigo_reserva);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView codigoreserva = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		codigoreserva.setTypeface(miPropiaTypeFace);

		TextView indicaciones = (TextView) findViewById(R.id.txv_descripcion_libro);
		indicaciones.setTypeface(miPropiaTypeFace);

		Button aceptar = (Button) findViewById(R.id.btn_reserva_aqui);
		aceptar.setTypeface(miPropiaTypeFace);

		Bundle bundle = getIntent().getExtras();
		Toast.makeText(
				getApplicationContext(),
				bundle.getString("idMultimedia")
						+ bundle.getString("idUsuario"), Toast.LENGTH_SHORT)
				.show();
>>>>>>> 5d9a9fff198101473f82e767906aa1eb9c47dfd8
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
