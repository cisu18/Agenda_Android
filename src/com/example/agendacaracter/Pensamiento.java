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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Pensamiento extends Activity implements OnClickListener {
	TextView txtPensamiento;
	TextView txtAutor;
	TextView planlectura;
	TextView textosPlanLectura;
	TextView nombreCualidad;
	TextView tvIdCualidad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pensamiento);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miNumeroTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface miVersiculoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface miPlanTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);
		title.setTypeface(miPropiaTypeFace);

		txtPensamiento = (TextView) findViewById(R.id.txt_pensamiento);
		txtPensamiento.setTypeface(miVersiculoTypeFace);

		txtAutor = (TextView) findViewById(R.id.txt_autorPensamiento);
		txtAutor.setTypeface(miNumeroTypeFace);

		planlectura = (TextView) findViewById(R.id.lbl_Plan_Lectura);
		planlectura.setTypeface(miPlanTypeFace);

		textosPlanLectura = (TextView) findViewById(R.id.txt_Plan_Lectura);
		textosPlanLectura.setTypeface(miVersiculoTypeFace);

		nombreCualidad = (TextView) findViewById(R.id.txt_Nombre_Cualidad);

		TextView txvCompartir = (TextView) findViewById(R.id.txt_compartir);
		txvCompartir.setOnClickListener(this);

		TextView txtEvaluacion = (TextView) findViewById(R.id.txt_Evaluacion);
		txtEvaluacion.setOnClickListener(this);

		TextView txtLibroReferencia = (TextView) findViewById(R.id.txt_LibroReferencia);
		txtLibroReferencia.setOnClickListener(this);

		String fecha = Util.getFechaActual().substring(0, 5);

		// fecha="09-10"; //mensaje corto
		// fecha="24-11"; //mensaje largo
		// fecha="24-08"; //autor corto
		// fecha="02-12"; //autor largo

		new ReadJSONFeedTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad_dia/pensamiento/format/json/fecha/"
						+ fecha);

		Bundle bundle = getIntent().getExtras();
		nombreCualidad.setText(bundle.getString("Nombre Cualidad"));
		textosPlanLectura.setText(bundle.getString("Plan lectura"));
		tvIdCualidad = new TextView(this);
		tvIdCualidad.setText(bundle.getString("idCualidad"));
		registerForContextMenu(txtLibroReferencia);
		txtLibroReferencia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_Evaluacion:
			SharedPreferences preferencias = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			String fechaevaluacion = preferencias.getString("eval", "0");
			if (!Val.isEvaluated(fechaevaluacion)) {
				Intent i = new Intent(this, EvaluacionDiaria.class);
				startActivity(i);
			}
			break;
		case R.id.txt_compartir:
			Util.compartir(this, "Pensamiento", txtPensamiento.getText()
					.toString() + " " + txtAutor.getText().toString());
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
			i.putExtra("id cualidad", tvIdCualidad.getText());
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

				txtPensamiento.setText("\"" + datos.getString("pensamiento")
						+ "\"");
				txtAutor.setText(datos.getString("autorpensamiento"));

			} catch (Exception e) {
				Log.e("onPostExecute", e.getLocalizedMessage());
			}
			Util.CerrarDialog();
		}
	}

}
