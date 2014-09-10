package com.example.agendacaracter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class Evaluacion_diaria1 extends Activity {
	private Button btnSiguiente;
	public double puntaje;	
	String mensajeCompartir;	
	private TextView txvPregunta01;
	private TextView txvPregunta02;
	/*private Spinner sp_critetioEvaluacion01; 
	private Spinner sp_critetioEvaluacion02;*/


	
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
		
		/*sp_critetioEvaluacion01 = (Spinner) findViewById(R.id.sp_critetio_evaluacion01);
		sp_critetioEvaluacion01.setTypeface(miContenidoTypeFace);	
		
		sp_critetioEvaluacion02 = (Spinner) findViewById(R.id.sp_critetio_evaluacion02);
		sp_critetioEvaluacion02.setTypeface(miContenidoTypeFace);	*/
			
		
		Button btnSiguiente = (Button) findViewById(R.id.btn_siguiente);
		btnSiguiente.setTypeface(miPropiaTypeFace);
		btnSiguiente.setOnClickListener(new OnClickListener() {
		    public void onClick(View v)
		    {
		        mostrarAlerta();
		    } 
		});


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
				
				Intent i = new Intent(getApplicationContext(), ResultadoEvaluacion.class);
				i.putExtra("puntaje", sumarPuntaje());
				startActivity(i);
				alert.dismiss();
				finish();
				
			}
		});		
		alert.show();
	}
	
	public int sumarPuntaje(){
		/*RadioGroup rdbgPregunta01 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_01);
		RadioGroup rdbgPregunta02 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_02);

		RadioButton rdbPregunta01 = (RadioButton) findViewById(rdbgPregunta01
				.getCheckedRadioButtonId());
		RadioButton rdbPregunta02 = (RadioButton) findViewById(rdbgPregunta02
				.getCheckedRadioButtonId());

		Double puntaje = Double.parseDouble(rdbPregunta01.getText().toString())
				+ Double.parseDouble(rdbPregunta02.getText().toString());
		puntaje = (double) Math.round(puntaje / 2);
		int redondeado=puntaje.intValue();*/
		return 1;
	}
	
	
	
}
