package com.example.agendacaracter;

import java.util.ArrayList;
import java.util.List;

import com.example.reutilizables.AdaptadorSpinner;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Evaluacion_diaria1 extends Activity {	
	public double puntaje;
	String mensajeCompartir;
	private TextView txvPregunta01;
	private TextView txvPregunta02;
	private Spinner sp_critetioEvaluacion01;
	private Spinner sp_critetioEvaluacion02;

	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluacion_diaria1);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface miContenidoTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);
		title.setTypeface(miPropiaTypeFace);

		TextView indicaciones = (TextView) findViewById(R.id.txt_introduccion);
		indicaciones.setTypeface(miPropiaTypeFace);

		txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(miContenidoTypeFace);

		txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(miContenidoTypeFace);

		Button btnSiguiente = (Button) findViewById(R.id.btn_siguiente);
		btnSiguiente.setTypeface(miPropiaTypeFace);
		btnSiguiente.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mostrarAlerta();
			}
		});
		llenarSpinner();

	}

	public void llenarSpinner() {
		sp_critetioEvaluacion01 = (Spinner) findViewById(R.id.sp_critetio_evaluacion01);
		sp_critetioEvaluacion02 = (Spinner) findViewById(R.id.sp_critetio_evaluacion02);

		List<String> list = new ArrayList<String>();
		list.add("Nada");
		list.add("Muy poco");
		list.add("Algo");
		list.add("Casi Siempre");
		list.add("Siempre");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp_critetioEvaluacion01.setAdapter(dataAdapter);
		sp_critetioEvaluacion01.setOnItemSelectedListener(new AdaptadorSpinner());
		
		sp_critetioEvaluacion02.setAdapter(dataAdapter);		
		sp_critetioEvaluacion01.setOnItemSelectedListener(new AdaptadorSpinner());
		
	}

	public void mostrarAlerta() {
		alert = new AlertDialog.Builder(Evaluacion_diaria1.this).create();
		alert.setTitle("Mensaje");
		alert.setIcon(R.drawable.ic_action_accept);
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
		double puntaje01 = Double.parseDouble(String.valueOf(sp_critetioEvaluacion01.getSelectedItemPosition()))+1;
		double puntaje02 = Double.parseDouble(String.valueOf(sp_critetioEvaluacion02.getSelectedItemPosition()))+1;
		
		int promedio=(int)Math.round((puntaje01+puntaje02) / 2);
		Log.e("PROMEDIDO",puntaje+"-"+puntaje01+"@"+puntaje02);
		return promedio;
		
	}

}
