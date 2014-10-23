package com.clmdev.agendacaracter;

import com.clmdev.agendacaracter.R;
import com.clmdev.reutilizables.Util;
import com.clmdev.reutilizables.Val;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Pensamiento extends Activity implements OnClickListener {
	TextView txvPensamiento;
	TextView txvAutorPensamiento;
	TextView txvLabelPlanlectura;
	TextView txvPlanLectura;
	TextView txvNombreCualidad;
	TextView txvIdCualidad;
	SharedPreferences archivoUsuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pensamiento);
		Typeface tfHelveticaCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface tfGeosansLightOblique1 = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight-Oblique_1.ttf");

		Typeface tfGeosansLight2 = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		Typeface tfHelveticaLightCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-LightCond.otf");

		TextView txvCabecera = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabecera.setTypeface(tfHelveticaCond);

		txvPensamiento = (TextView) findViewById(R.id.txv_pensamiento);
		txvPensamiento.setTypeface(tfGeosansLight2);

		txvAutorPensamiento = (TextView) findViewById(R.id.txv_autor_pensamiento);
		txvAutorPensamiento.setTypeface(tfGeosansLightOblique1);

		txvLabelPlanlectura = (TextView) findViewById(R.id.txv_label_plan_lectura);
		txvLabelPlanlectura.setTypeface(tfHelveticaLightCond);

		txvPlanLectura = (TextView) findViewById(R.id.txv_plan_lectura);
		txvPlanLectura.setTypeface(tfGeosansLight2);
		
		txvNombreCualidad = (TextView) findViewById(R.id.txv_nombre_cualidad);
		txvNombreCualidad.setTypeface(tfHelveticaCond);

		TextView txvCompartirPensamiento = (TextView) findViewById(R.id.txv_compartir_pensamiento);
		txvCompartirPensamiento.setOnClickListener(this);

		TextView txvIrEvaluacion = (TextView) findViewById(R.id.txv_ir_evaluacion);
		txvIrEvaluacion.setOnClickListener(this);

		TextView txtIrLibroReferencia = (TextView) findViewById(R.id.txv_ir_libro_referencia);
		txtIrLibroReferencia.setOnClickListener(this);

		registerForContextMenu(txtIrLibroReferencia);
		txvIdCualidad = new TextView(this);	
		archivoUsuario = getApplicationContext().getSharedPreferences("user",
				Context.MODE_PRIVATE);
		llenarData();
	}
	
	public void llenarData(){		
		txvIdCualidad.setText(archivoUsuario.getString("idCualidad", "0"));
		txvNombreCualidad.setText(archivoUsuario.getString("cualidad", ""));		
		txvPlanLectura.setText(archivoUsuario.getString("planLectura", ""));
		txvPensamiento.setText("\""+archivoUsuario.getString("pensamiento", "")+"\"");
		txvAutorPensamiento.setText(archivoUsuario.getString("autorPensamiento", ""));
	} 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txv_ir_evaluacion:
			SharedPreferences preferencias = getSharedPreferences("user",
					Context.MODE_PRIVATE);
			if (!Val.isEvaluated(getApplicationContext(),
					preferencias.getString("id", "0"))) {
				Intent i = new Intent(this, EvaluacionDiaria.class);
				startActivity(i);
			} else {
				Toast.makeText(this, "Usted ya realizó su evaluación",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.txv_compartir_pensamiento:
			Util.compartir(this, "Pensamiento", txvPensamiento.getText()
					.toString()
					+ " "
					+ txvAutorPensamiento.getText().toString());
			break;
		case R.id.txv_ir_libro_referencia:
			openContextMenu(v);
			break;
		}
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contextual_cualidades,
				menu.setHeaderTitle("Actividades para Fortalecer tu Carácter"));
		menu.setHeaderIcon(getResources().getDrawable(R.drawable.icono_mas));

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.opcVerLibros:
			Intent i = new Intent(this, Referencia.class);
			i.putExtra("id cualidad", txvIdCualidad.getText());
			i.putExtra("tipo multimedia", "1");
			i.putExtra("Nombre Cualidad", txvNombreCualidad.getText());
			startActivity(i);
			return true;

		case R.id.opcVerPeliculas:
			Intent peliculas = new Intent(this, ListadoMultimedia.class);
			peliculas.putExtra("id cualidad", txvIdCualidad.getText());
			peliculas.putExtra("tipo multimedia", "2");
			peliculas.putExtra("Nombre Cualidad", txvNombreCualidad.getText());
			startActivity(peliculas);
			return true;
		case R.id.opcVerAudios:
			Intent audios = new Intent(this, ListadoMultimedia.class);
			audios.putExtra("id cualidad", txvIdCualidad.getText());
			audios.putExtra("tipo multimedia", "3");
			audios.putExtra("Nombre Cualidad", txvNombreCualidad.getText());
			startActivity(audios);
			return true;
		case R.id.opcVerCirculos:
			Intent circulos = new Intent(this, CirculoCrecimiento.class);
			startActivity(circulos);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
