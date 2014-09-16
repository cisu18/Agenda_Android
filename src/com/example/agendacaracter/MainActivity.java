package com.example.agendacaracter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.reutilizables.Util;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

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
		}

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miVersiculoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface miNumeroTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface miPlanTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		fechaMovil = (TextView) findViewById(R.id.txv_fecha_movil);
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
		establecerFecha();

		tvIdCualidad = new TextView(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_Pensamiento_Diario:
			Intent i = new Intent(this, Pensamiento.class);
			i.putExtra("idCualidad", tvIdCualidad.getText());
			i.putExtra("Nombre Cualidad", cualidad.getText());
			i.putExtra("Plan lectura", textosPlanLectura.getText());
			startActivity(i);
			break;
		case R.id.txtEvaluacionDiaria:
			/*
			 * SharedPreferences preferencias = getSharedPreferences("user",
			 * Context.MODE_PRIVATE); String fechaevaluacion =
			 * preferencias.getString("eval", "0"); if
			 * (!Val.isEvaluated(fechaevaluacion)) {
			 */
			Intent in = new Intent(this, EvaluacionDiaria.class);
			startActivity(in);
			/* }else{
				Toast.makeText(this, "Usted ya realizo su evaluación", Toast.LENGTH_SHORT).show();
			} */
			break;
		case R.id.txt_Compartir_Versiculo:
			Util.compartir(this, cualidad.getText().toString(), versiculo
					.getText().toString()
					+ " "
					+ textobiblico.getText().toString());

			break;
		default:

			break;

		}
	}

	public void establecerFecha() {

		String formato = "MMMM'\t\t'yyyy'\t\t'EEEE'\t\t'dd";

		SimpleDateFormat dateFormat = new SimpleDateFormat(formato,
				Locale.getDefault());
		Date cal1 = new Date();
		String fecha = dateFormat.format(cal1);
		fechaMovil.setText(fecha);

		String fechaWebService = Util.getFechaActual().substring(0, 5);

		// fecha="21-09"; //autor corto
		// fecha="26-10"; //autor largo
		// fecha="19-11"; //largo
		// fecha="25-01"; //corto
		new ReadDiarioJSONFeedTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad_dia/cualidades_dia/format/json/fecha/"
						+ fechaWebService);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_listar:
			Intent mostrarAct = new Intent(this, ListaCualidades.class);
			startActivity(mostrarAct);
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
			return true;
		} else {
			if (conectadoRedMovil()) {
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
		alertDialog.setCanceledOnTouchOutside(false);
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
			Util.MostrarDialog(MainActivity.this);
		}

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
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
			Util.CerrarDialog();
		}
	}

}
