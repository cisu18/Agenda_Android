package com.example.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entidad.Cualidad;
import com.example.reutilizables.AdaptadorAvance;
import com.example.reutilizables.Util;

public class AvanceMensual extends Activity {

	private ListView lsvListaAvance;
	ArrayList<Cualidad> listadoAvance;
	Cualidad cualidad = new Cualidad();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avance_mensual);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		lsvListaAvance = (ListView) findViewById(R.id.lsv_avance_mensual);
		SharedPreferences prefe = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
		
		final String url = getResources().getString(R.string.url_web_service);
		new ReadCualidadesJSONFeedTask()
				.execute(url+"puntaje_cualidad/puntaje_all/format/json/usuario/"
						+ String.valueOf(idUsuario));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.avance_mensual, menu);
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

	private class ReadCualidadesJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(AvanceMensual.this);
		}

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				listadoAvance = new ArrayList<Cualidad>();

				for (int i = 0; i < jsonArray.length(); i++) {
					Cualidad c = new Cualidad();
					datos = jsonArray.getJSONObject(i);
					c.setId(datos.getString("int_id"));
					c.setCualidad(datos.getString("var_nombre"));
					c.setMes(datos.getString("var_mes"));
					c.setPuntaje(datos.getString("promedio"));
					listadoAvance.add(c);

				}

				lsvListaAvance.setAdapter(new AdaptadorAvance(
						getApplicationContext(), listadoAvance));

			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Lista de Cualidades",
						Toast.LENGTH_LONG).show();

			}
			Util.cerrarDialogLoad();
		}
	}

}
