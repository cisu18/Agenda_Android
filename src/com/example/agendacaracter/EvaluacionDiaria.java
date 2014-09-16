package com.example.agendacaracter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class EvaluacionDiaria extends Activity {
	public double puntaje;
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

		Typeface tfGeosansLight = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		TextView title = (TextView) findViewById(R.id.txv_cabecera);
		title.setTypeface(tfHelvetica);

		TextView indicaciones = (TextView) findViewById(R.id.txt_introduccion);
		indicaciones.setTypeface(tfHelvetica);

		txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(tfGeosansLight);

		txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(tfGeosansLight);

		Button btnSiguiente = (Button) findViewById(R.id.btn_ver_resultado_evaluacion);
		btnSiguiente.setTypeface(tfHelvetica);
		btnSiguiente.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mostrarAlerta();
			}
		});
		llenarSpinner();
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
				v.setTextColor(Color.WHITE);
				//v.setBackgroundColor(Color.BLUE);
				v.setTextSize(18);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					android.view.ViewGroup parent) {
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(tfJokerman);
				//v.setTextColor(Color.GREEN);
				//v.setBackgroundColor(Color.YELLOW);
				v.setTextSize(18);
				return v;
			}
		};

		adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_list_item_checked);
		spiCritetioEvaluacion01.setAdapter(adaptadorSpinner);
		spiCritetioEvaluacion02.setAdapter(adaptadorSpinner);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void mostrarAlerta() {
		alert = new AlertDialog.Builder(EvaluacionDiaria.this).create();
		alert.setTitle("Mensaje");
		alert.setIcon(R.drawable.ic_action_accept);
		alert.setCanceledOnTouchOutside(false);
		alert.setMessage("¿Esta seguro que quiere ver los resultados de su evaluación?");
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
				.valueOf(spiCritetioEvaluacion01.getSelectedItemPosition())) + 1;
		double puntaje02 = Double.parseDouble(String
				.valueOf(spiCritetioEvaluacion02.getSelectedItemPosition())) + 1;

		int promedio = (int) Math.round((puntaje01 + puntaje02) / 2);
		return promedio;

	}

}
