package com.example.agendacaracter;

import java.util.ArrayList;

import com.example.entidad.Circulos;
import com.example.reutilizables.AdaptadorCirculos;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class CirculoCrecimiento extends Activity {
	GridView grvListaCirculos;
	ArrayList<Circulos> listacirculos;
	Circulos circulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circulo_crecimiento);
		grvListaCirculos = (GridView) findViewById(R.id.grv_Lista_Circulos);
		listacirculos = new ArrayList<Circulos>();

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		// Introduzco los datos
		circulo = new Circulos(
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio Trujillo",
				"Richard Oruna", "Lunes- 4:00 PM ", "044-223423");
		listacirculos.add(circulo);
		circulo = new Circulos("Nuevo Cajamarca", "Maria Durán",
				"Martha Chávez", "642372");
		listacirculos.add(circulo);
		circulo = new Circulos(
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio Trujillo",
				"Orlando Peña", "Trigal, Averias, Cañaveral y la Choza",
				"767787");
		listacirculos.add(circulo);
		circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo",
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio", "658757");
		circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña",
				"Las Gardenias de Villa Hermosa", "044-223423");
		listacirculos.add(circulo);
		circulo = new Circulos("Nuevo Cajamarca", "Maria Durán",
				"Martha Chávez", "642372");
		listacirculos.add(circulo);
		circulo = new Circulos("Casitas", "Orlando Peña",
				"Trigal, Averias, Cañaveral y la Choza", "767787");
		listacirculos.add(circulo);
		circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo",
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio", "658757");
		circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña",
				"Las Gardenias de Villa Hermosa", "044-223423");
		listacirculos.add(circulo);
		circulo = new Circulos("Nuevo Cajamarca", "Maria Durán",
				"Martha Chávez", "642372");
		listacirculos.add(circulo);
		circulo = new Circulos("Casitas", "Orlando Peña",
				"Trigal, Averias, Cañaveral y la Choza", "767787");
		listacirculos.add(circulo);
		circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo",
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio", "658757");
		circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña",
				"Las Gardenias de Villa Hermosa", "044-223423");
		listacirculos.add(circulo);
		circulo = new Circulos("Nuevo Cajamarca", "Maria Durán",
				"Martha Chávez", "642372");
		listacirculos.add(circulo);
		circulo = new Circulos("Casitas", "Orlando Peña",
				"Trigal, Averias, Cañaveral y la Choza", "767787");
		listacirculos.add(circulo);
		circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo",
				"Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio", "658757");

		AdaptadorCirculos adapterCirculos = new AdaptadorCirculos(this,
				listacirculos);

		grvListaCirculos.setAdapter(adapterCirculos);

	}

}