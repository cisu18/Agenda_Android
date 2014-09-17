package com.example.agendacaracter;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.reutilizables.Util;
import com.example.reutilizables.Val;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Pensamiento extends Activity implements OnClickListener {
	TextView txvPensamiento;
	TextView txvAutorPensamiento;
	TextView txvLabelPlanlectura;
	TextView txvPlanLectura;
	TextView txvNombreCualidad;
	TextView txvIdCualidad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pensamiento);

		Typeface tfHelveticaCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface tfGeosansLightOblique1 = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface tfGeosansLight2 = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface tfHelveticaLightCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		TextView txvCabecera = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabecera.setTypeface(tfHelveticaCond);

		txvPensamiento = (TextView) findViewById(R.id.txv_pensamiento);
		txvPensamiento.setTypeface(tfGeosansLight2);

		txvAutorPensamiento = (TextView) findViewById(R.id.txv_autor_pensamiento);
		txvAutorPensamiento.setTypeface(tfGeosansLightOblique1);

		txvLabelPlanlectura = (TextView) findViewById(R.id.txv_redes_sociales);
		txvLabelPlanlectura.setTypeface(tfHelveticaLightCond);

		txvPlanLectura = (TextView) findViewById(R.id.txv_plan_lectura);
		txvPlanLectura.setTypeface(tfGeosansLight2);

		txvNombreCualidad = (TextView) findViewById(R.id.txv_mensaje_reserva);

		TextView txvCompartirPensamiento = (TextView) findViewById(R.id.txv_compartir_pensamiento);
		txvCompartirPensamiento.setOnClickListener(this);

		TextView txvIrEvaluacion = (TextView) findViewById(R.id.txv_ir_evaluacion);
		txvIrEvaluacion.setOnClickListener(this);

		TextView txtIrLibroReferencia = (TextView) findViewById(R.id.txv_ir_libro_referencia);
		txtIrLibroReferencia.setOnClickListener(this);

		String fecha = Util.getFechaActual().substring(0, 5);

		// fecha="09-10"; //mensaje corto
		// fecha="24-11"; //mensaje largo
		// fecha="24-08"; //autor corto
		// fecha="02-12"; //autor largo

		new ReadJSONFeedTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad_dia/pensamiento/format/json/fecha/"
						+ fecha);

		Bundle bundle = getIntent().getExtras();
		txvNombreCualidad.setText(bundle.getString("Nombre Cualidad"));
		txvPlanLectura.setText(bundle.getString("Plan lectura"));
		txvIdCualidad = new TextView(this);
		txvIdCualidad.setText(bundle.getString("idCualidad"));
		registerForContextMenu(txtIrLibroReferencia);
		txtIrLibroReferencia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});
		mostrarNotBarra();
	}
	
	private void mostrarNotBarra() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Agregando el icono, texto y momento para lanzar la notificación
		int icon = R.drawable.key;
		CharSequence tickerText = "Notification Bar";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		CharSequence contentTitle = "Notificación en barra";
		CharSequence contentText = "Mensaje corto de la notificación";

		// Agregando sonido
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Agregando vibración
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		Intent notificationIntent = new Intent(this,
				MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(1, notification);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txv_ir_evaluacion:
			SharedPreferences preferencias = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			if (!Val.isEvaluated(getApplicationContext(),preferencias.getString("id", "0"))) {
				Intent i = new Intent(this, EvaluacionDiaria.class);
				startActivity(i);
			}else{
				Toast.makeText(this, "Usted ya realizo su evaluaci�n", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.txv_compartir_pensamiento:
			Util.compartir(this, "Pensamiento", txvPensamiento.getText()
					.toString() + " " + txvAutorPensamiento.getText().toString());
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_cualidades,
				menu.setHeaderTitle("Opciones disponibles"));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.opcVerLibros:
			Intent i = new Intent(this, Referencia.class);
			i.putExtra("id cualidad", txvIdCualidad.getText());
			startActivity(i);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(Pensamiento.this);
		}

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				datos = jsonArray.getJSONObject(0);

				txvPensamiento.setText("\"" + datos.getString("pensamiento")
						+ "\"");
				txvAutorPensamiento.setText(datos.getString("autorpensamiento"));

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Pensamiento: Error Interno -> onPostExecute. "+e.getMessage(), Toast.LENGTH_SHORT).show();				
			}
			Util.cerrarDialogLoad();
		}
	}

}
