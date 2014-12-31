package com.clmdev.agendacaracter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

import com.clmdev.agendacaracter.R;
import com.clmdev.reutilizables.Util;
import com.clmdev.reutilizables.Val;
import com.clmdev.servicios.ServicioAlerta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private TextView txvDia_numero;
	private TextView txvDia;
	private TextView txvMes;
	private TextView cualidad;
	private TextView versiculo;
	private TextView textobiblico;
	private TextView planlectura;
	private TextView textosPlanLectura;
	private TextView compartirVersiculo;
	private TextView IrPensamiento;
	private TextView IrEvaluacion;
	public TextView tvIdCualidad;
	public static Activity principal;
	SharedPreferences archivoUsuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miVersiculoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface miNumeroTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface miPlanTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		txvDia_numero = (TextView) findViewById(R.id.txv_dia_numero);
		txvDia_numero.setTypeface(miPlanTypeFace);

		txvDia = (TextView) findViewById(R.id.txv_dia);
		txvDia.setTypeface(miPropiaTypeFace);

		txvMes = (TextView) findViewById(R.id.txv_mes);
		txvMes.setTypeface(miVersiculoTypeFace);

		cualidad = (TextView) findViewById(R.id.txv_nombre_cualidad);
		cualidad.setTypeface(miPropiaTypeFace);

		versiculo = (TextView) findViewById(R.id.txt_label_versiculo);
		versiculo.setTypeface(miVersiculoTypeFace);

		textobiblico = (TextView) findViewById(R.id.txv_numero_versiculo);
		textobiblico.setTypeface(miNumeroTypeFace);

		planlectura = (TextView) findViewById(R.id.txv_mensaje_plan_lectura);
		planlectura.setTypeface(miPlanTypeFace);

		textosPlanLectura = (TextView) findViewById(R.id.txv_plan_lectura);
		textosPlanLectura.setTypeface(miVersiculoTypeFace);		

		compartirVersiculo = (TextView) findViewById(R.id.txv_compartir_versiculo);
		IrPensamiento = (TextView) findViewById(R.id.txv_ir_pensamiento_diario);
		IrEvaluacion = (TextView) findViewById(R.id.txv_ir_evaluacion_diaria);		

		IrPensamiento.setOnClickListener(this);
		IrEvaluacion.setOnClickListener(this);
		compartirVersiculo.setOnClickListener(this);

		tvIdCualidad = new TextView(this);
		iniciarServicios();

		archivoUsuario = getApplicationContext().getSharedPreferences("user",
				Context.MODE_PRIVATE);
		usuarioConectado();

	}

	public void usuarioConectado() {
		if (estaConectado()) {
			int idUsuario = Integer.parseInt(archivoUsuario
					.getString("id", "0"));
			if (idUsuario == 0) {
				Intent i = new Intent(this, Login.class);
				startActivity(i);
			} else {
				principal = this;
				establecerFecha();

			}

		} else {
			showAlertDialog(MainActivity.this, "Conexión a Internet",
					"Tu Dispositivo necesita una conexión a internet.", false);
		}
	}

	public void iniciarServicios() {
		if(!ServicioAlerta.isRunning()){
			startService(new Intent(getBaseContext(), ServicioAlerta.class));
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			 this.moveTaskToBack(true);
			
		}

		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txv_ir_pensamiento_diario:
			Intent i = new Intent(this, Pensamiento.class);			
			startActivity(i);
			break;
		case R.id.txv_ir_evaluacion_diaria:
//			Toast.makeText(
//					getApplicationContext(),
//					"Evaluación diaria: Opción diponible desde diciembre 2014", Toast.LENGTH_SHORT).show();
			SharedPreferences preferencias = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			if (!Val.isEvaluated(getApplicationContext(),
					preferencias.getString("id", "0"))) {

				Intent in = new Intent(this, EvaluacionDiaria.class);
				startActivity(in);

			} else {
				Toast.makeText(this, "Usted ya realizó su evaluación",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.txv_compartir_versiculo:
			String title = cualidad.getText().toString();
			String post = versiculo
					.getText().toString()
					+ " "
					+ textobiblico.getText().toString();
			Util.mostrarMenuCompartir(this,title,post);
			break;
		default:

			break;

		}
	}

	public void establecerFecha() {

		String formato = "MMMM' 'yyyy";
		String formatonumdia = "dd";
		String formatodia = "EEEE";

		SimpleDateFormat dateFormat = new SimpleDateFormat(formato,
				Locale.getDefault());
		SimpleDateFormat dateFormatnumdia = new SimpleDateFormat(formatonumdia,
				Locale.getDefault());
		SimpleDateFormat dateFormatdia = new SimpleDateFormat(formatodia,
				Locale.getDefault());

		Date cal1 = new Date();
		String fecha = dateFormat.format(cal1);
		String dia = dateFormatnumdia.format(cal1);
		String nombredia = dateFormatdia.format(cal1);

		txvMes.setText(fecha);
		txvDia_numero.setText(dia);
		txvDia.setText(nombredia);

		String fechaWebService = Util.getFechaActual();
		if(!fechaWebService.equals(archivoUsuario.getString("fechaData", "01-01-2000"))){
			final String url = getResources().getString(R.string.url_web_service);		
			new ReadDiarioJSONFeedTask().execute(url
					+ "cualidad_dia/cualidades_dia2/format/json/fecha/"
					+ fechaWebService);
		}else{
			String cualidadId = archivoUsuario.getString("idCualidad", "0");
			String cualidad = archivoUsuario.getString("cualidad", "");
			String versiculo = archivoUsuario.getString("versiculo", "");
			String numeroVersiculo = archivoUsuario.getString("numeroVersiculo", "");
			String planLectura = archivoUsuario.getString("planLectura", "");	
			llenarData(cualidadId, cualidad, versiculo, numeroVersiculo, planLectura);
		}

		

	}
	public void llenarData(String cuaId, String cua, String ver, String numVer, String plaLec){
		tvIdCualidad.setText(cuaId);
		cualidad.setText(cua);
		versiculo.setText(ver);
		textobiblico.setText(numVer);
		textosPlanLectura.setText(plaLec);
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
			archivoUsuario.edit().remove("id").commit();
			archivoUsuario.edit().remove("usuario").commit();
			archivoUsuario.edit().remove("email").commit();
			Intent i = new Intent(this, Login.class);
			startActivity(i);

			return true;
			
		case R.id.action_listar_avance:
			Intent intAvance = new Intent(this, AvanceMensual.class);
			startActivity(intAvance);
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
		alertDialog.setCancelable(false);
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
				String cualidadId = datos.getString("cualidad_id");
				String cualidad = datos.getString("cualidad");
				String versiculo = datos.getString("versiculo");
				String numeroVersiculo = datos.getString("numeroversiculo");
				String planLectura = datos.getString("planlectura");
				String pensamiento = datos.getString("pensamiento");
				String autorPensamiento = datos.getString("autorpensamiento");
				
				Editor editor = archivoUsuario.edit();
				editor.putString("fechaData", Util.getFechaActual());
				editor.putString("idCualidad", cualidadId);				
				editor.putString("cualidad", cualidad);
				editor.putString("versiculo", versiculo);
				editor.putString("numeroVersiculo", numeroVersiculo);
				editor.putString("planLectura", planLectura);
				editor.putString("pensamiento", pensamiento);
				editor.putString("autorPensamiento", autorPensamiento);
				editor.commit();
				llenarData(cualidadId, cualidad, versiculo, numeroVersiculo, planLectura);

			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"Error al momento cargar datos de la aplicación", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			Util.cerrarDialogLoad();
		}
	}

}

