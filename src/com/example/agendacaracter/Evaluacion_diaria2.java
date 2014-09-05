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
import android.content.SharedPreferences.Editor;
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

public class Evaluacion_diaria2 extends Activity implements OnClickListener {

	public double puntaje;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria2);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);

		title.setTypeface(miPropiaTypeFace);

		TextView indicaciones = (TextView) findViewById(R.id.txt_especificaciones);
		indicaciones.setTypeface(miPropiaTypeFace);

		TextView tercerapregunta = (TextView) findViewById(R.id.txv_pregunta_03);
		tercerapregunta.setTypeface(miPropiaTypeFace);

		Button btnVerResultado = (Button) findViewById(R.id.btn_ver_resultado);
		btnVerResultado.setTypeface(miPropiaTypeFace);
		btnVerResultado.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ver_resultado:

			RadioGroup rdbgPregunta03 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_03);
			RadioButton rdbPregunta03 = (RadioButton) findViewById(rdbgPregunta03
					.getCheckedRadioButtonId());

			Bundle b = getIntent().getExtras();
			puntaje = b.getDouble("puntaje");
			puntaje = puntaje
					+ Double.parseDouble(rdbPregunta03.getText().toString());
			puntaje = Math.round(puntaje / 3);
			
			SharedPreferences prefe = getSharedPreferences("user",Context.MODE_PRIVATE);
			String usuario = prefe.getString("id", "0");
			
			String fecha = getFechaActual();
			new ReadJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/puntaje_cualidad/puntaje/fecha/"
							+ fecha
							+ "/usuario/"
							+ usuario
							+ "/puntaje/"
							+ puntaje + "/format/json");

			break;
		}
	}

	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
	}

	public void mostrarAlerta(int puntaje, int mensual) {
		alert = new AlertDialog.Builder(Evaluacion_diaria2.this).create();
		alert.setTitle("Resultado");
		// alert.setIcon(R.drawable.ic_action_accept);
		alert.setMessage("Su puntaje del día de hoy es " + puntaje + "\n"
				+ "Su puntaje mensual es " + mensual);
		alert.setButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.hide();
			}
		});
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluacion_diaria2, menu);
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

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
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
