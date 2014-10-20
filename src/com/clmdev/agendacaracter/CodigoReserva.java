package com.clmdev.agendacaracter;

import com.clmdev.agendacaracter.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CodigoReserva extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_codigo_reserva);

		Typeface tfHelveticaCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface HelveticaBoldTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		TextView codigoreserva = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		codigoreserva.setTypeface(tfHelveticaCond);

		TextView indicaciones = (TextView) findViewById(R.id.txv_descripcion_libro);
		indicaciones.setTypeface(tfHelveticaCond);

		Button aceptar = (Button) findViewById(R.id.btn_reserva_aqui);
		aceptar.setTypeface(HelveticaBoldTypeFace);

		Bundle bundle = getIntent().getExtras();
		Toast.makeText(
				getApplicationContext(),
				bundle.getString("idMultimedia")
						+ bundle.getString("idUsuario"), Toast.LENGTH_SHORT)
				.show();
	}
}
