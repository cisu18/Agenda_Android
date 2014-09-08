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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
	public double puntaje;
	AlertDialog alert;
	String mensajeCompartir;

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
		indicaciones.setText("¿Quieres desarrollar esta cualidad en tu vida?, responde las siguientes preguntas:");

		TextView txtEspecificaciones = (TextView) findViewById(R.id.txt_especificaciones);
		txtEspecificaciones.setText("1 Nada\t\t2 Muy poco\t\t3 Algo\t\t4 Casi siempre\t\t5 Siempre");
		
		TextView txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(miPropiaTypeFace);
		//txvPregunta01
		//		.setText("¿Cuánto de esta cualidad manifestaste a las personas el día de hoy?");

		TextView txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(miPropiaTypeFace);
		//txvPregunta02
		//		.setText("¿Existió hoy alguna situación o circunstancia en la que aplicaste esta cualidad?");

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

			RadioButton rdbPregunta01 = (RadioButton) findViewById(rdbgPregunta01
					.getCheckedRadioButtonId());
			RadioButton rdbPregunta02 = (RadioButton) findViewById(rdbgPregunta02
					.getCheckedRadioButtonId());

			puntaje = Double.parseDouble(rdbPregunta01.getText().toString())
					+ Double.parseDouble(rdbPregunta02.getText().toString());
			// Toast.makeText(getApplicationContext(),puntaje+"",
			// Toast.LENGTH_SHORT).show();

			SharedPreferences prefe = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			String usuario = prefe.getString("id", "0");

			puntaje = (double) Math.round(puntaje / 2);
			String fecha = getFechaActual();
			new ReadJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/puntaje_cualidad/puntaje/fecha/"
							+ fecha
							+ "/usuario/"
							+ usuario
							+ "/puntaje/"
							+ puntaje + "/format/json");

			/*
			 * Intent i = new Intent(this, Evaluacion_diaria2.class);
			 * i.putExtra("puntaje", puntaje); startActivity(i);
			 */
			break;
		}
	}

	public String mensaje(int numero) {
		String var = "";
		switch (numero) {
		case 1:
			var = "Vamos: Es una oportunidad de ver los primeros resultados del inicio de tu cambio personal. Necesitas un mentor a quien rendir cuentas.";
			break;
		case 2:
			var = "Sigue adelante: Si le aumentas un poco de esfuerzo más podrías ver una diferencia substancial en tu conducta actual que formará tu carácter en un futuro cercano. Habla con tu mentor.";
			break;
		case 3:
			var = "Vas bien: ¿Cómo podrías hacerlo mejor? y como podrías transmitir tu ejemplo a otra persona, habla sobre esto con tu mentor.";
			break;
		case 4:
			var = "Muy bien: Se está formando un hábito fuerte en ti, celébralo con tu mentor.";
			break;
		case 5:
			var = "Excelente: Piensa en ser un mentor para alguien más! qué nombres de candidatos pasan por tu cabeza? Recuerda otras personas también necesitan ayuda.";
			break;
		}
		return var;
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

	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
	}

	public void mostrarAlerta(int puntaje, int estado) {
		alert = new AlertDialog.Builder(Evaluacion_diaria1.this).create();
		alert.setTitle("Resultado");
		alert.setIcon(R.drawable.ic_action_accept);		
		alert.setMessage("Su puntaje del día de hoy es " + puntaje + "\n"
				+ "Su puntaje mensual es " + estado+".\n\n"+mensaje(puntaje));
		
		mensajeCompartir = "Hoy optube "+puntaje+" en mi evaluación diaria.";
		
		alert.setButton2("Compartir", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				compartir();
				alert.dismiss();
			}
		});		
		alert.setButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
			}
		});
		alert.setIconAttribute(R.drawable.ic_action_accept);
		alert.show();
	}
	
	private void compartir(){
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Evaluación diaria");
		intent.putExtra(Intent.EXTRA_TEXT, mensajeCompartir);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		this.startActivity(Intent.createChooser(intent, "Compartir en"));
	}
	
	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				Log.e("Result",result);
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				datos = jsonArray.getJSONObject(0);
				mostrarAlerta(Integer.parseInt((int) puntaje + ""),
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
