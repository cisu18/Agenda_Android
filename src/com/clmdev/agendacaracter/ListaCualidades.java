package com.clmdev.agendacaracter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Cualidad;
import com.clmdev.reutilizables.AdaptadorCualidades;
import com.clmdev.reutilizables.Util;

public class ListaCualidades extends Activity {

	private ListView lsvListaCualidades;
	ArrayList<Cualidad> listaCualidades;
	Cualidad cualidad = new Cualidad();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_cualidades);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		lsvListaCualidades = (ListView) findViewById(R.id.lsv_cualidades);
		registerForContextMenu(lsvListaCualidades);

		lsvListaCualidades.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				openContextMenu(v);
			}
		});
		usuarioConectado();
		



	}
	public void usuarioConectado() {
		if (estaConectado()) {
			final String url = getResources().getString(R.string.url_web_service);
			new ReadCualidadesJSONFeedTask().execute(url
					+ "cualidad/cualidades/format/json/anio/"+Util.getFechaActual().substring(6, 10));

			
		} else {
			showAlertDialog(ListaCualidades.this, "Conexión a Internet",
					"Tu Dispositivo necesita una conexión a internet.", false);
		}
	}

	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		cualidad = (Cualidad) lsvListaCualidades.getAdapter().getItem(
				info.position);

		menu.setHeaderTitle("Actividades para Fortalecer tu Carácter");
		menu.setHeaderIcon(getResources().getDrawable(R.drawable.icono_mas));
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_cualidades, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String id = cualidad.getId();
		String nombreCualidad=cualidad.getCualidad();
		switch (item.getItemId()) {

		case R.id.opcVerLibros:
			Intent libros = new Intent(this, ListadoMultimedia.class);
			libros.putExtra("id cualidad", id);
			libros.putExtra("tipo multimedia", "1");
			libros.putExtra("Nombre Cualidad", nombreCualidad);
			startActivity(libros);
			return true;
	
		/*case R.id.opcVerPeliculas:
			Intent peliculas = new Intent(this, ListadoMultimedia.class);
			peliculas.putExtra("id cualidad", id);
			peliculas.putExtra("tipo multimedia", "2");
			peliculas.putExtra("Nombre Cualidad", nombreCualidad);
			startActivity(peliculas);
			return true;
		case R.id.opcVerAudios:
			Intent audios = new Intent(this, ListadoMultimedia.class);
			audios.putExtra("id cualidad", id);
			audios.putExtra("tipo multimedia", "3");
			audios.putExtra("Nombre Cualidad", nombreCualidad);
			startActivity(audios);
			return true;
		case R.id.opcVerCirculos:
			Intent circulos = new Intent(this, CirculoCrecimiento.class);
			startActivity(circulos);
			return true;*/
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
				listaCualidades = new ArrayList<Cualidad>();

				for (int i = 0; i < jsonArray.length(); i++) {
					Cualidad c = new Cualidad();
					datos = jsonArray.getJSONObject(i);
					c.setId(datos.getString("id"));
					c.setCualidad(datos.getString("cualidad"));
					c.setMes(datos.getString("mes"));
					listaCualidades.add(c);

				}

				lsvListaCualidades.setAdapter(new AdaptadorCualidades(
						getApplicationContext(), listaCualidades));

			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Lista de Cualidades",
						Toast.LENGTH_LONG).show();

			}
			Util.cerrarDialogLoad();
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

}
