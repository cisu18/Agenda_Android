package com.example.agendacaracter;

import java.util.ArrayList;

import com.example.entidad.Circulos;
import com.example.reutilizables.AdaptadorCirculos;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        listacirculos= new ArrayList<Circulos>();
        
        Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);
    
        // Introduzco los datos
        circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña","Las Gardenias de Villa Hermosa","044-223423");
        listacirculos.add(circulo);
        circulo = new Circulos("Nuevo Cajamarca", "Maria Durán","Martha Chávez","642372");
        listacirculos.add(circulo);
        circulo = new Circulos("Casitas", "Orlando Peña","Trigal, Averias, Cañaveral y la Choza","767787");
        listacirculos.add(circulo);
        circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo","Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio","658757");
        circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña","Las Gardenias de Villa Hermosa","044-223423");
        listacirculos.add(circulo);
        circulo = new Circulos("Nuevo Cajamarca", "Maria Durán","Martha Chávez","642372");
        listacirculos.add(circulo);
        circulo = new Circulos("Casitas", "Orlando Peña","Trigal, Averias, Cañaveral y la Choza","767787");
        listacirculos.add(circulo);
        circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo","Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio","658757");
        circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña","Las Gardenias de Villa Hermosa","044-223423");
        listacirculos.add(circulo);
        circulo = new Circulos("Nuevo Cajamarca", "Maria Durán","Martha Chávez","642372");
        listacirculos.add(circulo);
        circulo = new Circulos("Casitas", "Orlando Peña","Trigal, Averias, Cañaveral y la Choza","767787");
        listacirculos.add(circulo);
        circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo","Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio","658757");
        circulo = new Circulos("La Cruz - Tumbes", "Julián y Violeta Campaña","Las Gardenias de Villa Hermosa","044-223423");
        listacirculos.add(circulo);
        circulo = new Circulos("Nuevo Cajamarca", "Maria Durán","Martha Chávez","642372");
        listacirculos.add(circulo);
        circulo = new Circulos("Casitas", "Orlando Peña","Trigal, Averias, Cañaveral y la Choza","767787");
        listacirculos.add(circulo);
        circulo = new Circulos("Chimbote", "Gadaffy y Juanita Carrillo","Av. Enrique Meigg Mz. A2 Lt. 22 Urb El Trapecio","658757");
        
        
        // Creo el adapter personalizado
        AdaptadorCirculos adapterCirculos = new AdaptadorCirculos(this, listacirculos);
 
        // Lo aplico
        grvListaCirculos.setAdapter(adapterCirculos);
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.circulo_crecimiento, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
