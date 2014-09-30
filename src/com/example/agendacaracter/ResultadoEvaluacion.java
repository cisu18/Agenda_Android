package com.example.agendacaracter;

import com.example.reutilizables.Util;
import com.example.reutilizables.Val;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

public class ResultadoEvaluacion extends Activity implements OnClickListener {

	private int intPuntaje;
	private TextView txvMensajeCarita;
	private TextView txvResultadoEvaluacion;
	private TextView txvRecomendacion;
	private Button btnCompartirResultado;
	private StringBuilder stbCompartir = new StringBuilder();
	private SharedPreferences preferencias;
	private int idUsuario = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_evaluacion);

		Typeface tfHelveticaLTStdCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface tfHelveticaBoldLTStdCond = Typeface.createFromAsset(
				getAssets(), "fonts/HelveticaLTStd-BoldCond.otf");

		Typeface tfGeosansLight2 = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		txvMensajeCarita = (TextView) findViewById(R.id.txv_mensaje_carita);
		txvMensajeCarita.setTypeface(tfHelveticaBoldLTStdCond);

		txvResultadoEvaluacion = (TextView) findViewById(R.id.txv_resultado_evaluacion);
		txvResultadoEvaluacion.setTypeface(tfHelveticaLTStdCond);

		txvRecomendacion = (TextView) findViewById(R.id.txv_recomendacion);
		txvRecomendacion.setTypeface(tfGeosansLight2);

		TextView txvLabelRecomendacion = (TextView) findViewById(R.id.txv_label_recomendacion);
		txvLabelRecomendacion.setTypeface(tfGeosansLight2);

		btnCompartirResultado = (Button) findViewById(R.id.btn_compartir_puntaje);
		btnCompartirResultado.setTypeface(tfHelveticaBoldLTStdCond);
		btnCompartirResultado.setOnClickListener(this);

		preferencias = getSharedPreferences("user", Context.MODE_PRIVATE);
		cargaData();
	}

	public void cargaData() {
		Bundle bundle = getIntent().getExtras();
		intPuntaje = bundle.getInt("puntaje") * 25;
		idUsuario = Integer.parseInt(preferencias.getString("id", "0"));
		int idCualidad = Integer.parseInt(preferencias.getString("idCualidad",
				"0"));
		final String url = getResources().getString(R.string.url_web_service);
		if (idUsuario > 0) {
			new ReadJSONFeedTask().execute(url
					+ "puntaje_cualidad/puntaje/fecha/" + Util.getFechaActual()
					+ "/usuario/" + idUsuario + "/puntaje/" + intPuntaje
					+ "/cualidad/" + idCualidad + "/format/json");
			mostrarMensajeDiario();
		} else {
			Intent i = new Intent(this, Login.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_compartir_puntaje:
			Util.compartir(this, "Resultado Evaluaci�n",
					stbCompartir.toString());
			break;
		}
	}

	private void mostrarMensajeDiario() {
		StringBuilder stbMensajeResultado = new StringBuilder();
		StringBuilder stbRecomendacion = new StringBuilder();
		StringBuilder stbMensajeCarita = new StringBuilder();

		ImageView img = (ImageView) findViewById(R.id.img_carita_resultado_evaluacion);
		stbMensajeResultado.append("Tu avance personal de hoy es: ");
		stbCompartir.append("Mi avance personal de hoy es ");

		switch (intPuntaje) {
		case 0:
			stbMensajeResultado.append(intPuntaje + "%");
			stbCompartir.append(intPuntaje
					+ "%\nEs la oportunidad de iniciar mi cambio personal.");
			stbRecomendacion
					.append("Es una oportunidad de iniciar t� cambio personal.");
			txvRecomendacion.setText("");
			stbMensajeCarita.append("�Vamos!");
			img.setImageResource(R.drawable.cara_01);
			break;
		case 25:
			stbMensajeResultado.append(intPuntaje + "%");
			stbCompartir
					.append(intPuntaje
							+ "%\nUn poco m�s de esfuerzo y ver� una diferencia que formar� mi car�cter.");
			stbRecomendacion
					.append("Un poco m�s de esfuerzo y podr�as ver una diferencia que formar� tu car�cter.");
			stbMensajeCarita.append("�Sigue adelante!");
			img.setImageResource(R.drawable.cara_02);
			break;
		case 50:
			stbMensajeResultado.append(intPuntaje + "%");
			stbCompartir
					.append(intPuntaje
							+ "%\n�C�mo puedo hacerlo mejor? Hoy puedo trasmitir mi ejemplo a otras personas.");
			stbRecomendacion
					.append("�C�mo podr�as hacerlo mejor? Puedes trasmitir tu ejemplo a otras personas.");
			stbMensajeCarita.append("�Vas bien!");
			img.setImageResource(R.drawable.cara_03);
			break;
		case 75:
			stbMensajeResultado.append(intPuntaje + "%");
			stbCompartir.append(intPuntaje
					+ "%\nSe est� formando un h�bito fuerte en m�.");
			stbRecomendacion.append("Se est� formando un h�bito fuerte en ti.");
			stbMensajeCarita.append("�Muy bien!");
			img.setImageResource(R.drawable.cara_04);
			break;
		case 100:
			stbMensajeResultado.append(intPuntaje + "%");
			stbCompartir
					.append(intPuntaje
							+ "%\nPuedo ser un mentor para alguien m�s, otras personas necesitan mi ayuda.");
			stbRecomendacion
					.append("Piensa en ser un mentor para alguien m�s. Recuerda otras personas tambi�n necesitan ayuda.");
			stbMensajeCarita.append("�Excelente!");
			img.setImageResource(R.drawable.cara_05);
			break;
		}
		txvResultadoEvaluacion.setText(stbMensajeResultado);
		txvRecomendacion.setText(stbRecomendacion);
		txvMensajeCarita.setText(stbMensajeCarita);
		Val.setEvaluated(getApplicationContext(), idUsuario + "");
	}

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}
	}
}
