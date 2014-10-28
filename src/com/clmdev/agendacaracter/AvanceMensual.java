package com.clmdev.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Cualidad;
import com.clmdev.reutilizables.AdaptadorAvance;
import com.clmdev.reutilizables.Util;

public class AvanceMensual extends Activity {

	private ListView lsvListaAvance;
	private ArrayList<Cualidad> lstAvance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avance_mensual);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		lsvListaAvance = (ListView) findViewById(R.id.lsv_avance_mensual);
				
		usuarioConectado();
		
	}
	public void usuarioConectado() {
		if (estaConectado()) {
			SharedPreferences prefe = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			
			int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
			final String url = getResources().getString(R.string.url_web_service);
			
			new ReadCualidadesJSONFeedTask().execute(url
					+ "puntaje_cualidad/puntaje_all/format/json/usuario/"
					+ String.valueOf(idUsuario)+"/anio/"+Util.getFechaActual().substring(6, 10));

		} else {
			showAlertDialog(AvanceMensual.this, "Conexi�n a Internet",
					"Tu Dispositivo necesita una conexi�n a internet.", false);
		}
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
				lstAvance = new ArrayList<Cualidad>();

				for (int i = 0; i < jsonArray.length(); i++) {
					Cualidad c = new Cualidad();
					datos = jsonArray.getJSONObject(i);
					c.setId(datos.getString("int_id"));
					c.setCualidad(datos.getString("var_nombre"));
					c.setMes(datos.getString("var_mes"));
					c.setPuntaje(datos.getString("promedio"));
					lstAvance.add(c);

				}

				lsvListaAvance.setAdapter(new AdaptadorAvance(
						getApplicationContext(), lstAvance));

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
