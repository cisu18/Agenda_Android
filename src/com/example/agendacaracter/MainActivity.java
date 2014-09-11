package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private TextView fechaMovil;
	private TextView cualidad;
	private TextView versiculo;
	private TextView textobiblico;
	private TextView planlectura;
	private TextView textosPlanLectura;

	private TextView compartirVersiculo;
	private TextView IrPensamiento;
	private TextView IrEvaluacion;

	private ProgressDialog pDialog;
	public TextView tvIdCualidad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		SharedPreferences prefe = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
		if (idUsuario == 0) {
			Intent i = new Intent(this, Login.class);
			startActivity(i);
		} else {
		}

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miVersiculoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface miNumeroTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface miPlanTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		fechaMovil = (TextView) findViewById(R.id.txt_Fecha_Movil);
		fechaMovil.setTypeface(miPropiaTypeFace);

		cualidad = (TextView) findViewById(R.id.txt_Nombre_Cualidad);
		cualidad.setTypeface(miPropiaTypeFace);

		versiculo = (TextView) findViewById(R.id.txt_Descripcion_Libro);
		versiculo.setTypeface(miVersiculoTypeFace);

		textobiblico = (TextView) findViewById(R.id.txt_Numero_Versiculo);
		textobiblico.setTypeface(miNumeroTypeFace);

		planlectura = (TextView) findViewById(R.id.lbl_Plan_Lectura);
		planlectura.setTypeface(miPlanTypeFace);

		textosPlanLectura = (TextView) findViewById(R.id.txt_Plan_Lectura);
		textosPlanLectura.setTypeface(miVersiculoTypeFace);

		compartirVersiculo = (TextView) findViewById(R.id.txt_Compartir_Versiculo);
		IrPensamiento = (TextView) findViewById(R.id.txt_Pensamiento_Diario);
		IrEvaluacion = (TextView) findViewById(R.id.txtEvaluacionDiaria);

		IrPensamiento.setOnClickListener(this);
		IrEvaluacion.setOnClickListener(this);
		compartirVersiculo.setOnClickListener(this);

		estaConectado();
		EstablecerFecha();

		tvIdCualidad = new TextView(this);
		// tvIdCualidad.setText("1");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.txt_Pensamiento_Diario:
			Intent i = new Intent(this, Pensamiento.class);
			i.putExtra("idCualidad", tvIdCualidad.getText());
			i.putExtra("Nombre Cualidad", cualidad.getText());
			i.putExtra("Plan lectura", textosPlanLectura.getText());

			Log.e("idCualidad", tvIdCualidad.getText().toString());
			startActivity(i);
			break;
		case R.id.txtEvaluacionDiaria:
			Intent in = new Intent(this, Evaluacion_diaria1.class);
			startActivity(in);
			break;
		case R.id.txt_Compartir_Versiculo:
			Util.compartir(this, cualidad.getText().toString(), versiculo
					.getText().toString()
					+ "\n\n"
					+ textobiblico.getText().toString());

			break;
		default:

			break;

		}
	}

	// public static void share(Context ctx, String subject,String text) {
	// final Intent intent = new Intent(Intent.ACTION_SEND);
	//
	// intent.setType("text/plain");
	// intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	// intent.putExtra(Intent.EXTRA_TEXT, text);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//
	// ctx.startActivity(Intent.createChooser(intent,
	// ctx.getString(R.string.msg_MostrarCualidad)));
	// }

	public void EstablecerFecha() {

		String formato = "MMMM'		'yyyy'	'EEEE' 'dd";

		SimpleDateFormat dateFormat = new SimpleDateFormat(formato,
				Locale.getDefault());
		Date cal1 = new Date();
		String fecha = dateFormat.format(cal1);
		System.out.println(fecha);
		fechaMovil.setText(fecha);

		String formatoWebservice = "dd-MM";
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(formatoWebservice,
				Locale.getDefault());
		fecha = dateFormat2.format(cal1);
		System.out.println(fecha);

		// fecha="21-09"; //autor corto
		// fecha="26-10"; //autor largo
		// fecha="19-11"; //largo
		// fecha="25-01"; //corto
		new ReadDiarioJSONFeedTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad_dia/cualidades_dia/format/json/fecha/"
						+ fecha);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		// return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.action_listar:
			Intent mostrarAct = new Intent(this, ListaCualidades.class);
			startActivity(mostrarAct);
			finish();
			return true;
		case R.id.action_cerrar_sesion:
			SharedPreferences archivoUsuario = getApplicationContext()
					.getSharedPreferences("user", Context.MODE_PRIVATE);
			archivoUsuario.edit().remove("id").commit();
			archivoUsuario.edit().remove("usuario").commit();
			archivoUsuario.edit().remove("email").commit();
			Intent i = new Intent(this, Login.class);
			startActivity(i);

			return true;
		default:
			return false;
		}
	}

	protected Boolean estaConectado() {
		if (conectadoWifi()) {
			// showAlertDialog(MainActivity.this, "Conexion a Internet",
			// "Tu Dispositivo tiene Conexion a Wifi.", true);
			return true;
		} else {
			if (conectadoRedMovil()) {
				// showAlertDialog(MainActivity.this, "Conexion a Internet",
				// "Tu Dispositivo tiene Conexion Movil.", true);
				return true;
			} else {
				showAlertDialog(MainActivity.this, "Conexion a Internet",
						"Tu Dispositivo necesita una conexion a internet.",
						false);

				return false;

			}
		}
	}

	protected Boolean conectadoWifi() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected Boolean conectadoRedMovil() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon((status) ? R.drawable.ic_action_accept
				: R.drawable.ic_action_cancel);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});

		alertDialog.show();
	}

	private class ReadDiarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Por favor espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();

				datos = jsonArray.getJSONObject(0);

				tvIdCualidad.setText(datos.getString("cualidad_id"));
				cualidad.setText(datos.getString("cualidad"));
				versiculo.setText(datos.getString("versiculo"));
				textobiblico.setText(datos.getString("numeroversiculo"));
				textosPlanLectura.setText(datos.getString("planlectura"));

			} catch (Exception e) {
				Log.e("onPostExecute", e.getLocalizedMessage());
			}
			// dismiss the dialog once product deleted
			pDialog.dismiss();
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
				Log.d("JSON", "No se ha podido descargar archivo");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}
		Log.e("Datos Diarios", stringBuilder.toString());
		return stringBuilder.toString();
	}

}
