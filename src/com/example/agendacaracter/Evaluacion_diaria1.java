package com.example.agendacaracter;


import android.app.Activity;
import android.app.AlertDialog;
<<<<<<< Updated upstream
import android.app.ProgressDialog;
import android.content.Context;
=======
>>>>>>> Stashed changes
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

<<<<<<< Updated upstream
	private Button btnSiguiente;
	public double puntaje;
	AlertDialog alert;
	String mensajeCompartir;
	private TextView txtEspecificaciones;
	private TextView txtEspecificaciones2;
	private TextView txvPregunta01;
	private TextView txvPregunta02;
	private RadioButton rbtn1_1; 
	private RadioButton rbtn2_1; 
	private RadioButton rbtn3_1;
	private RadioButton rbtn4_1;
	private RadioButton rbtn5_1;
	private RadioButton rbtn1_2; 
	private RadioButton rbtn2_2; 
	private RadioButton rbtn3_2;
	private RadioButton rbtn4_2;
	private RadioButton rbtn5_2;

=======
public class Evaluacion_diaria1 extends Activity {
>>>>>>> Stashed changes

	
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
		
		txtEspecificaciones = (TextView) findViewById(R.id.txt_especificaciones);
		txtEspecificaciones.setTypeface(miPropiaTypeFace);
		
		txvPregunta01 = (TextView) findViewById(R.id.txv_pregunta_01);
		txvPregunta01.setTypeface(miContenidoTypeFace);
		
		txvPregunta02 = (TextView) findViewById(R.id.txv_pregunta_02);
		txvPregunta02.setTypeface(miContenidoTypeFace);
		
		rbtn1_1 = (RadioButton) findViewById(R.id.rdb_puntaje_01_1);
		rbtn1_1.setTypeface(miContenidoTypeFace);
		
		rbtn2_1 = (RadioButton) findViewById(R.id.rdb_puntaje_01_2);
		rbtn2_1.setTypeface(miContenidoTypeFace);
		
		rbtn3_1 = (RadioButton) findViewById(R.id.rdb_puntaje_01_3);
		rbtn3_1.setTypeface(miContenidoTypeFace);
		
		rbtn4_1 = (RadioButton) findViewById(R.id.rdb_puntaje_01_4);
		rbtn4_1.setTypeface(miContenidoTypeFace);
		
		rbtn5_1 = (RadioButton) findViewById(R.id.rdb_puntaje_01_5);
		rbtn5_1.setTypeface(miContenidoTypeFace);
		
		rbtn1_2 = (RadioButton) findViewById(R.id.rdb_puntaje_02_1);
		rbtn1_2.setTypeface(miContenidoTypeFace);
		
		rbtn2_2 = (RadioButton) findViewById(R.id.rdb_puntaje_02_2);
		rbtn2_2.setTypeface(miContenidoTypeFace);
		
		rbtn3_2 = (RadioButton) findViewById(R.id.rdb_puntaje_02_3);
		rbtn3_2.setTypeface(miContenidoTypeFace);
		
		rbtn4_2 = (RadioButton) findViewById(R.id.rdb_puntaje_02_4);
		rbtn4_2.setTypeface(miContenidoTypeFace);
		
		rbtn5_2 = (RadioButton) findViewById(R.id.rdb_puntaje_02_5);
		rbtn5_2.setTypeface(miContenidoTypeFace);
			
		
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
		RadioGroup rdbgPregunta01 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_01);
		RadioGroup rdbgPregunta02 = (RadioGroup) findViewById(R.id.rdbg_grupo_puntaje_02);

		RadioButton rdbPregunta01 = (RadioButton) findViewById(rdbgPregunta01
				.getCheckedRadioButtonId());
		RadioButton rdbPregunta02 = (RadioButton) findViewById(rdbgPregunta02
				.getCheckedRadioButtonId());

		Double puntaje = Double.parseDouble(rdbPregunta01.getText().toString())
				+ Double.parseDouble(rdbPregunta02.getText().toString());
		puntaje = (double) Math.round(puntaje / 2);
		int redondeado=puntaje.intValue();
		return redondeado;
	}
	
	
	
}
