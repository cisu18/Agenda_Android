package com.clmdev.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Multimedia;
import com.clmdev.reutilizables.AdaptadorMultimedia;
import com.clmdev.reutilizables.Util;

public class ListadoMultimedia extends Activity {

	ArrayList<Multimedia> listadoMultimedias;

	AdaptadorMultimedia adaptadorMultimedia;
	public String tipo = "";
	public String nombreCualidad = "";
	public String idCualidad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_listado_multimedia);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		listadoMultimedias = new ArrayList<Multimedia>();
		
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		Bundle bundle = getIntent().getExtras();
		idCualidad = bundle.getString("id cualidad");
		tipo = bundle.getString("tipo multimedia");
		nombreCualidad = bundle.getString("Nombre Cualidad");
		
		usuarioConectado();
		if (tipo.equals("1")) {
			txvCabeceraDescripcion.setText("LIBROS DE REFERENCIA");
		}
		if (tipo.equals("2")) {
			txvCabeceraDescripcion.setText("PELÍCULAS Y SERIES");
		}
		if (tipo.equals("3")) {
			txvCabeceraDescripcion.setText("AUDIOS Y CONFERENCIAS");
		}
		
		
		Log.e("ids", idCualidad + " " + tipo);
		
		
		
		final ListView lsvListaMultimedia = (ListView) findViewById(R.id.lsv_Lista_peliculas);
		 //el número de columnas se calculará en función del tamaño de pantalla y la posición
        //boolean bigScreen = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
         
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            if (bigScreen)
//            {
//                lsvListaMultimedia.setNumColumns(4);
//            }
//            else
//            {
//            	lsvListaMultimedia.setNumColumns(3);
//            }
//        }
//        else
//        {
//            if (bigScreen)
//            {
//            	lsvListaMultimedia.setNumColumns(3);
//            }
//            else
//            {
//            	lsvListaMultimedia.setNumColumns(2);
//            }
//        }
		
		adaptadorMultimedia = new AdaptadorMultimedia(getApplicationContext(),
				R.layout.custom_row_multimedia, listadoMultimedias);

		lsvListaMultimedia.setAdapter(adaptadorMultimedia);

		lsvListaMultimedia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String idMultimedia = listadoMultimedias.get(position)
						.getIdMultimedia();
				String urlImagenMultimedia = listadoMultimedias.get(position)
						.getUrlImagenMultimedia();
				Intent intent = new Intent(getApplicationContext(),
						DescripcionMultimedia.class);
				intent.putExtra("id multimedia", idMultimedia);
				intent.putExtra("url multimedia", urlImagenMultimedia);
				intent.putExtra("tipo", tipo);
				startActivity(intent);
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
			showAlertDialog(ListadoMultimedia.this, "Conexión a Internet",
					"Tu Dispositivo necesita una conexión a internet.", false);
		}
	}


	class JSONAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(ListadoMultimedia.this);
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
					multimedia.setGeneroMultimedia(object.getString("genero"));
					listadoMultimedias.add(multimedia);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(
						getApplicationContext(),
						"No se encontraron recursos relacionados con "+nombreCualidad,
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
