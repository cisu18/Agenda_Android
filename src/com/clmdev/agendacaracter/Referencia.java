package com.clmdev.agendacaracter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Multimedia;
import com.clmdev.reutilizables.AdaptadorLibro;
import com.clmdev.reutilizables.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Referencia extends Activity {

	ArrayList<Multimedia> listadoMultimedia;

	AdaptadorLibro adaptadorMultimedia;
	public String tipo = "";
	public String nombreCualidad = "";
	public String idCualidad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_referencia);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		listadoMultimedia = new ArrayList<Multimedia>();

		Bundle bundle = getIntent().getExtras();
		idCualidad = bundle.getString("id cualidad");
		tipo = bundle.getString("tipo multimedia");
		nombreCualidad=bundle.getString("Nombre Cualidad");
		Log.e("ids", idCualidad + " " + tipo);
		usuarioConectado();
		
		
		final GridView grv_Libros_Referencia = (GridView) findViewById(R.id.grv_libros_referencia);
		adaptadorMultimedia = new AdaptadorLibro(getApplicationContext(),
				R.layout.custom_row_lista_libros_referencia, listadoMultimedia);

		grv_Libros_Referencia.setAdapter(adaptadorMultimedia);

		grv_Libros_Referencia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				String idMultimedia = listadoMultimedia.get(position)
						.getIdMultimedia();
				String urlImagenMultimedia = listadoMultimedia.get(position)
						.getUrlImagenMultimedia();
				Intent i = new Intent(getApplicationContext(),
						DescripcionMultimedia.class);
				i.putExtra("id multimedia", idMultimedia);
				i.putExtra("url multimedia", urlImagenMultimedia);
				i.putExtra("tipo", tipo);
				startActivity(i);
			}
		});
	}
	public void usuarioConectado() {
		if (estaConectado()) {

			final String url = getResources().getString(R.string.url_web_service);
			new JSONAsyncTask().execute(url
					+ "multimedia/lista_multimedia_bycualidadtipo/cualidad/"
					+ idCualidad + "/tipo/" + tipo + "/format/json");
			
		} else {
			showAlertDialog(Referencia.this, "Conexión a Internet",
					"Tu Dispositivo necesita una conexión a internet.", false);
		}
	}


	class JSONAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(Referencia.this);
		}

		@Override
		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {

			adaptadorMultimedia.notifyDataSetChanged();
			try {
				JSONObject object = new JSONObject();
				JSONArray jarray = new JSONArray(result);

				for (int i = 0; i < jarray.length(); i++) {
					object = jarray.getJSONObject(i);
					Multimedia multimedia = new Multimedia();
					multimedia.setIdMultimedia(object.getString("id"));
					multimedia.setTituloMultimedia(object.getString("titulo"));
					multimedia.setUrlImagenMultimedia(object.getString("img"));
					listadoMultimedia.add(multimedia);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(
						getApplicationContext(),
						"No se encontraron libros relacionados con "+ nombreCualidad,
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
