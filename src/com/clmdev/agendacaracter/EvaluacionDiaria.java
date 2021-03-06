package com.clmdev.agendacaracter;

import com.clmdev.agendacaracter.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class EvaluacionDiaria extends Activity {
	double puntaje;
	String mensajeCompartir;
	private TextView txvPregunta01;
	private TextView txvPregunta02;
	private Spinner spiCritetioEvaluacion01;
	private Spinner spiCritetioEvaluacion02;
	Typeface tfJokerman;

	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria);

		Typeface tfHelvetica = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface tfHelveticaBold = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		Typeface tfGeosansLight = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		TextView title = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		title.setTypeface(tfHelvetica);

		txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(tfGeosansLight);

		txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(tfGeosansLight);

		Button btnSiguiente = (Button) findViewById(R.id.btn_ver_resultado_evaluacion);
		btnSiguiente.setTypeface(tfHelveticaBold);
		
		
		
		usuarioConectado();
		
		btnSiguiente.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mostrarAlerta();
			}
		});
		llenarSpinner();
		
		
	}
	
	public void usuarioConectado() {
		if (estaConectado()) {

		} else {
			showAlertDialog(EvaluacionDiaria.this, "Conexi�n a Internet",
					"Tu Dispositivo necesita una conexi�n a internet.", false);
		}
	}

	public <ViewGroup> void llenarSpinner() {
		spiCritetioEvaluacion01 = (Spinner) findViewById(R.id.spi_critetio_evaluacion_01);
		spiCritetioEvaluacion02 = (Spinner) findViewById(R.id.spi_critetio_evaluacion_02);
		String[] list = getResources().getStringArray(
				R.array.criterios_evaluacion);
		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,
				R.layout.custom_row_spinner, list) {
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				tfJokerman = Typeface.createFromAsset(getAssets(),
						"fonts/HelveticaLTStd-Cond.otf");
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tfJokerman);
				v.setTextColor(Color.rgb(255, 109, 104));
				v.setTextSize(18);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					android.view.ViewGroup parent) {
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tfJokerman);
				v.setTextColor(Color.rgb(255, 255, 255));
				v.setPadding(10, 5, 0, 10);
				v.setBackgroundColor(Color.rgb(77, 99, 113));
				v.setTextSize(18);
				return v;
			}
		};

		adaptadorSpinner
				.setDropDownViewResource(android.R.layout.simple_list_item_checked);
		spiCritetioEvaluacion01.setAdapter(adaptadorSpinner);
		spiCritetioEvaluacion02.setAdapter(adaptadorSpinner);
	}

	public void mostrarAlerta() {
		alert = new AlertDialog.Builder(EvaluacionDiaria.this).create();
		alert.setTitle("Mensaje");
		alert.setIcon(R.drawable.ic_action_accept);
		alert.setCanceledOnTouchOutside(false);
		alert.setMessage("�Esta seguro que quiere ver los resultados de su evaluaci�n?");
		alert.setButton2("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
			}
		});

		alert.setButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(getApplicationContext(),
						ResultadoEvaluacion.class);
				i.putExtra("puntaje", sumarPuntaje());
				startActivity(i);
				alert.dismiss();
				finish();
			}
		});
		alert.show();
	}

	public int sumarPuntaje() {
		double puntaje01 = Double.parseDouble(String
				.valueOf(spiCritetioEvaluacion01.getSelectedItemPosition()));
		double puntaje02 = Double.parseDouble(String
				.valueOf(spiCritetioEvaluacion02.getSelectedItemPosition()));

		int promedio = (int) Math.round((puntaje01 + puntaje02) / 2);
		return promedio;

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
