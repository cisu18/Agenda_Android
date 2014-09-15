package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.reutilizables.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

public class ResultadoEvaluacion extends Activity implements OnClickListener {

	private int puntaje;
	TextView txvMensajeCarita;
	TextView txvMensajeResultadoEvaluacion;
	TextView txvMensajeRecomendacion;	
	
	Button btnCompartirResultado;	
	StringBuilder compartir = new StringBuilder();
	
	SharedPreferences preferencias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_evaluacion);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miContenidoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");
		
		txvMensajeCarita = (TextView) findViewById(R.id.txv_mensaje_carita);
		txvMensajeCarita.setTypeface(miPropiaTypeFace);
		
		txvMensajeResultadoEvaluacion = (TextView) findViewById(R.id.txv_mensaje_resultado_evaluacion);
		txvMensajeResultadoEvaluacion.setTypeface(miPropiaTypeFace);

		txvMensajeRecomendacion = (TextView) findViewById(R.id.txv_mensaje_recomendacion);
		txvMensajeRecomendacion.setTypeface(miContenidoTypeFace); 
		
		TextView txvRecomendacion = (TextView) findViewById(R.id.txv_recomendacion);
		txvRecomendacion.setTypeface(miContenidoTypeFace); 
				
		btnCompartirResultado = (Button) findViewById(R.id.btn_compartir_puntaje);
		btnCompartirResultado.setTypeface(miPropiaTypeFace);
		btnCompartirResultado.setOnClickListener(this);
		
		preferencias = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		cargaData();
	}

	public void cargaData() {
		Bundle bundle = getIntent().getExtras();
		puntaje = bundle.getInt("puntaje");		
		int idUsuario = Integer.parseInt(preferencias.getString("id", "0"));
		if (idUsuario > 0) {
			new ReadJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/puntaje_cualidad/puntaje/fecha/"
							+ Util.getFechaActual()
							+ "/usuario/"
							+ idUsuario
							+ "/puntaje/" + puntaje + "/format/json");
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
				Util.compartir(this, "Resultado Evaluación", compartir.toString());
			break;
		}
	}
	
	private void mostrarMensajeDiario(int estado, int puntajeMensual) {
		StringBuilder txtMensajeResultado = new StringBuilder();
		StringBuilder txtRecomendacion = new StringBuilder();
		StringBuilder txtMensajeCarita = new StringBuilder();
		int porcentaje = 0;
		ImageView img =(ImageView) findViewById(R.id.img_carita_resultado_evaluacion); 
		if (estado == 1) {
			porcentaje = puntajeMensual * 20;
			txtMensajeResultado.append("Tu avance personal de este mes es ");
			compartir.append("Mi avance personal de este mes es ");
			
		} else if (estado == 2) {

			porcentaje = puntaje * 20;
			txtMensajeResultado.append("Tu avance personal de hoy es: ");
			compartir.append("Mi avance personal de hoy es ");
			
		}
		switch (porcentaje) {
		case 20:
			txtMensajeResultado.append(porcentaje + "%");
			compartir.append(porcentaje
					+ "%\nEs la oportunidad iniciar mi cambio personal.");
			txtRecomendacion
					.append("Es una oportunidad de iniciar tu cambio personal.");
			txvMensajeRecomendacion.setText("");
			txtMensajeCarita.append("¡Vamos!");
			img.setImageResource(R.drawable.aceptar_20);
			break;
		case 40:
			txtMensajeResultado.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\nUn poco más de esfuerzo y veré una diferencia que formará mi carácter.");
			txtRecomendacion
					.append("Un poco más de esfuerzo y podrías ver una diferencia que formará tu carácter.");
			txtMensajeCarita.append("¡Sigue adelante!");
			img.setImageResource(R.drawable.compartir_42);
			break;
		case 60:
			txtMensajeResultado.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\n¿Cómo puedo hacerlo mejor? Hoy puedo trasmitir mi ejemplo a otras personas.");
			txtRecomendacion
					.append("¿Cómo podrías hacerlo mejor? Puedes trasmitir tu ejemplo a otras personas.");
			txtMensajeCarita.append("Vas bien");
			img.setImageResource(R.drawable.cara2);
			break;
		case 80:
			txtMensajeResultado.append(porcentaje + "%");
			compartir.append(porcentaje
					+ "%\nSe está formando un hábito fuerte en mí.");
			txtRecomendacion.append("Se está formando un hábito fuerte en ti.");
			txtMensajeCarita.append("Muy bien");
			break;
		case 100:
			txtMensajeResultado.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\nPuedo ser un mentor para alguien más, otras personas necesitan mi ayuda.");
			txtRecomendacion
					.append("Piensa en ser un mentor para alguien más. Recuerda otras personas también necesitan ayuda.");
			txtMensajeCarita.append("Excelente");
			img.setImageResource(R.drawable.cara1);
			break;
		}
		txvMensajeResultadoEvaluacion.setText(txtMensajeResultado);
		txvMensajeRecomendacion.setText(txtRecomendacion);
		txvMensajeCarita.setText(txtMensajeCarita);
		Editor editor = preferencias.edit();
		editor.putString("eval", Util.getFechaActual());		
		editor.commit();
	}	

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
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
				Log.e("onPostExecute", e.getLocalizedMessage());
			}
		}
	}

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.e("JSON", "No se ha podido descargar archivo");
			}
		} catch (Exception e) {
			Log.e("readJSONFeed", e.getLocalizedMessage());
		}

		return stringBuilder.toString();
	}
}
