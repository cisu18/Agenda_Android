package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.reutilizables.Social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;

public class ResultadoEvaluacion extends Activity implements OnClickListener {

	// String mensajeCompartir; 
	private int puntaje;
	TextView txvResultadoEvaluacion;
	TextView txvReferenteResultado;
	TextView txtMensaje_redes;
	TextView txvRecomendacion;
	Button btnCompartirResultado;
	TextView txvtitle;
	StringBuilder compartir = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_evaluacion);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miContenidoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");
		
		txvResultadoEvaluacion = (TextView) findViewById(R.id.txv_mensaje_resultado_evaluacion);
		txvResultadoEvaluacion.setTypeface(miPropiaTypeFace);

		txvReferenteResultado = (TextView) findViewById(R.id.txv_mensaje_referente_resultado);
		txvReferenteResultado.setTypeface(miContenidoTypeFace); 
		
		txvRecomendacion = (TextView) findViewById(R.id.txv_recomendacion);
		txvRecomendacion.setTypeface(miContenidoTypeFace); 
		
		txtMensaje_redes = (TextView) findViewById(R.id.txv_mensaje_redes);
		txtMensaje_redes.setTypeface(miContenidoTypeFace); 		

		btnCompartirResultado = (Button) findViewById(R.id.btn_compartir_puntaje);
		btnCompartirResultado.setTypeface(miPropiaTypeFace);
		/*btnCompartirResultado.setOnClickListener(this);
		cargaData();*/
	}

	public void cargaData() {
		Bundle bundle = getIntent().getExtras();
		puntaje = bundle.getInt("puntaje");
		SharedPreferences prefe = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
		if (idUsuario > 0) {
			new ReadJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/puntaje_cualidad/puntaje/fecha/"
							+ getFechaActual()
							+ "/usuario/"
							+ idUsuario
							+ "/puntaje/" + (int) puntaje + "/format/json");
		} else {
			Intent i = new Intent(this, Login.class);
			startActivity(i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_compartir_puntaje:
				Social.compartir(this, "Resultado Evaluación", compartir.toString());
			break;
		}
	}
	
	private void mostrarMensajeDiario(int estado, int puntajeMensual) {
		StringBuilder mensaje = new StringBuilder();
		StringBuilder recomendacion = new StringBuilder();
		StringBuilder titulo = new StringBuilder();
		int porcentaje = 0;
		if (estado == 1) {
			porcentaje = puntajeMensual * 20;
			mensaje.append("Tu avance personal de este mes es: ");
			compartir.append("Mi avance personal de este mes es ");

		} else if (estado == 2) {

			porcentaje = (int) puntaje * 20;
			mensaje.append("Tu avance personal de hoy es: ");
			compartir.append("Mi avance personal de hoy es ");
		}
		switch (porcentaje) {
		case 20:
			mensaje.append(porcentaje + "%");
			compartir.append(porcentaje
					+ "%\nEs la oportunidad iniciar mi cambio personal.");
			recomendacion
					.append("Es una oportunidad de iniciar tu cambio personal.");
			txvReferenteResultado.setText("");
			titulo.append("¡Vamos!");
			break;
		case 40:
			mensaje.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\nUn poco más de esfuerzo y veré una diferencia que formará mi carácter.");
			recomendacion
					.append("Un poco más de esfuerzo y podrías ver una diferencia que formará tu carácter.");
			titulo.append("¡Sigue adelante!");
			break;
		case 60:
			mensaje.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\n¿Cómo puedo hacerlo mejor? Hoy puedo trasmitir mi ejemplo a otras personas.");
			recomendacion
					.append("¿Cómo podrías hacerlo mejor? Puedes trasmitir tu ejemplo a otras personas.");
			titulo.append("Vas bien");
			break;
		case 80:
			mensaje.append(porcentaje + "%");
			compartir.append(porcentaje
					+ "%\nSe está formando un hábito fuerte en mí.");
			recomendacion.append("Se está formando un hábito fuerte en ti.");
			titulo.append("Muy bien");
			break;
		case 100:
			mensaje.append(porcentaje + "%");
			compartir
					.append(porcentaje
							+ "%\nPuedo ser un mentor para alguien más, otras personas necesitan mi ayuda.");
			recomendacion
					.append("Piensa en ser un mentor para alguien más. Recuerda otras personas también necesitan ayuda.");
			titulo.append("Excelente");
			break;
		}
		compartir.append("\n\nAgenda Carácter");
		txvResultadoEvaluacion.setText(mensaje);
		txvReferenteResultado.setText(recomendacion);
		txvtitle.setText(titulo);
	}

	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
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
