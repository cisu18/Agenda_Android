package com.example.agendacaracter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.entidad.Cualidades;
import com.example.reutilizables.AdaptadorCualidades;
import com.example.reutilizables.Util;

public class ListaCualidades extends Activity {

	private ListView lista_mensual;
	ArrayList<Cualidades> cualidades;
	Cualidades cu = new Cualidades();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_cualidades);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView lista_cualidades = (TextView) findViewById(R.id.txv_cabecera);

		lista_cualidades.setTypeface(miPropiaTypeFace);

		lista_mensual = (ListView) findViewById(R.id.lsv_cualidad);

		new ReadCualidadesJSONFeedTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad/cualidades/format/json");

		registerForContextMenu(lista_mensual);

		lista_mensual.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				openContextMenu(v);
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_inicio:
			Intent in = new Intent(this, MainActivity.class);
			startActivity(in);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		cu = (Cualidades) lista_mensual.getAdapter().getItem(info.position);
		menu.setHeaderTitle(cu.getCualidad());
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_cualidades, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String id = cu.getId();
		switch (item.getItemId()) {

		case R.id.opcVerLibros:
			Intent i = new Intent(this, Referencia.class);
			i.putExtra("id cualidad", id);
			startActivity(i);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private class ReadCualidadesJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(ListaCualidades.this);
		}

		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				cualidades = new ArrayList<Cualidades>();

				for (int i = 0; i < jsonArray.length(); i++) {
					Cualidades c = new Cualidades();
					datos = jsonArray.getJSONObject(i);
					c.setId(datos.getString("id"));
					c.setCualidad(datos.getString("cualidad"));
					c.setMes(datos.getString("mes"));
					cualidades.add(c);

				}

				lista_mensual.setAdapter(new AdaptadorCualidades(
						getApplicationContext(), cualidades));

			} catch (Exception e) {
				Log.e("ReadCualidadesJSONFeedTask", e.getLocalizedMessage());
			}
			Util.cerrarDialogLoad();
		}
	}

}
