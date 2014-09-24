package com.example.agendacaracter;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.Toast;

import android.widget.TextView;

public class ResultadoEvaluacion extends Activity implements OnClickListener {

	private int intPuntaje;
	private TextView txvMensajeCarita;
	private TextView txvResultadoEvaluacion;
	private TextView txvRecomendacion;	
	
	private Button btnCompartirResultado;	
	private StringBuilder stbCompartir = new StringBuilder();
	
	private SharedPreferences preferencias;
	private int idUsuario=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_evaluacion);

		Typeface tfHelveticaLTStdCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		
		Typeface tfHelveticaBoldLTStdCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");


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
		
		preferencias = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		cargaData();
	}

	public void cargaData() {
		Bundle bundle = getIntent().getExtras();
		intPuntaje = bundle.getInt("puntaje");		
		 idUsuario = Integer.parseInt(preferencias.getString("id", "0"));
		if (idUsuario > 0) {
			new ReadJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/puntaje_cualidad/puntaje/fecha/"
							+ Util.getFechaActual()
							+ "/usuario/"
							+ idUsuario
							+ "/puntaje/" + intPuntaje + "/format/json");
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
				Util.compartir(this, "Resultado Evaluación", stbCompartir.toString());
			break;
		}
	}
	
	private void mostrarMensajeDiario(int estado, int intPorcentaje) {
		StringBuilder stbMensajeResultado = new StringBuilder();
		StringBuilder stbRecomendacion = new StringBuilder();
		StringBuilder stbMensajeCarita = new StringBuilder();
		
		ImageView img =(ImageView) findViewById(R.id.img_carita_resultado_evaluacion); 
		if (estado == 1) {
			intPorcentaje = intPorcentaje * 25;
			stbMensajeResultado.append("Tu avance personal de este mes es ");
			stbCompartir.append("Mi avance personal de este mes es ");
			
		} else if (estado == 2) {

			intPorcentaje = intPorcentaje * 25;
			stbMensajeResultado.append("Tu avance personal de hoy es: ");
			stbCompartir.append("Mi avance personal de hoy es ");
			
		}
		switch (intPorcentaje) {
		case 0:
			stbMensajeResultado.append(intPorcentaje + "%");
			stbCompartir.append(intPorcentaje
					+ "%\nEs la oportunidad iniciar mi cambio personal.");
			stbRecomendacion
					.append("Es una oportunidad de iniciar tu cambio personal.");
			txvRecomendacion.setText("");
			stbMensajeCarita.append("¡Vamos!");
			img.setImageResource(R.drawable.cara_01);
			break;
		case 25:
			stbMensajeResultado.append(intPorcentaje + "%");
			stbCompartir
					.append(intPorcentaje
							+ "%\nUn poco más de esfuerzo y veré una diferencia que formará mi carácter.");
			stbRecomendacion
					.append("Un poco más de esfuerzo y podrías ver una diferencia que formará tu carácter.");
			stbMensajeCarita.append("¡Sigue adelante!");
			img.setImageResource(R.drawable.cara_02);
			break;
		case 50:
			stbMensajeResultado.append(intPorcentaje + "%");
			stbCompartir
					.append(intPorcentaje
							+ "%\n¿Cómo puedo hacerlo mejor? Hoy puedo trasmitir mi ejemplo a otras personas.");
			stbRecomendacion
					.append("¿Cómo podrías hacerlo mejor? Puedes trasmitir tu ejemplo a otras personas.");
			stbMensajeCarita.append("¡Vas bien!");
			img.setImageResource(R.drawable.cara_03);
			break;
		case 75:
			stbMensajeResultado.append(intPorcentaje + "%");
			stbCompartir.append(intPorcentaje
					+ "%\nSe está formando un hábito fuerte en mí.");
			stbRecomendacion.append("Se está formando un hábito fuerte en ti.");
			stbMensajeCarita.append("¡Muy bien!");
			img.setImageResource(R.drawable.cara_04);
			break;
		case 100:
			stbMensajeResultado.append(intPorcentaje + "%");
			stbCompartir
					.append(intPorcentaje
							+ "%\nPuedo ser un mentor para alguien más, otras personas necesitan mi ayuda.");
			stbRecomendacion
					.append("Piensa en ser un mentor para alguien más. Recuerda otras personas también necesitan ayuda.");
			stbMensajeCarita.append("¡Excelente!");
			img.setImageResource(R.drawable.cara_05);
			break;
		}
		txvResultadoEvaluacion.setText(stbMensajeResultado);
		txvRecomendacion.setText(stbRecomendacion);
		txvMensajeCarita.setText(stbMensajeCarita);
		Val.setEvaluated(getApplicationContext(), idUsuario+"");		
	}	

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0],getApplicationContext());
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				datos = jsonArray.getJSONObject(0);				
				mostrarMensajeDiario(
						Integer.parseInt(datos.getString("estado")),
						Integer.parseInt(datos.getString("puntaje")));
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "ResultadoEvaluación: Error Interno -> onPostExecute. "+e.getMessage(), Toast.LENGTH_SHORT).show();				
			}
		}
	}	
}
