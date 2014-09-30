package com.example.reutilizables;

import java.util.ArrayList;

import com.example.agendacaracter.R;
import com.example.entidad.Circulos;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorCirculos extends BaseAdapter {
	Typeface tf;
	private static ArrayList<Circulos> searchArrayList;

	private LayoutInflater mInflater;

	public AdaptadorCirculos(Context context, ArrayList<Circulos> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
	}

	@Override
	public int getCount() {
		return searchArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_circulos, null);
			holder = new ViewHolder();
			holder.ciuddad = (TextView) convertView
					.findViewById(R.id.txv_ciudad_circulo);
			holder.encargado = (TextView) convertView
					.findViewById(R.id.txv_encragado_circulo);
			holder.direccion = (TextView) convertView
					.findViewById(R.id.txv_direccion_circulo);
			holder.telefono = (TextView) convertView
					.findViewById(R.id.txv_telefono_circulo);

			TextView ciudad = (TextView) convertView
					.findViewById(R.id.txv_ciudad_circulo);
			ciudad.setTypeface(tf);

			TextView encargado = (TextView) convertView
					.findViewById(R.id.txv_encragado_circulo);
			encargado.setTypeface(tf);

			TextView direccion = (TextView) convertView
					.findViewById(R.id.txv_direccion_circulo);
			direccion.setTypeface(tf);

			TextView telefono = (TextView) convertView
					.findViewById(R.id.txv_telefono_circulo);
			telefono.setTypeface(tf);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ciuddad.setText(searchArrayList.get(position).getCiudad());
		holder.encargado.setText(searchArrayList.get(position).getEncargado());
		holder.direccion.setText(searchArrayList.get(position).getDireccion());
		holder.telefono.setText(searchArrayList.get(position).getTelefono());

		return convertView;
	}

	static class ViewHolder {
		TextView ciuddad;
		TextView encargado;
		TextView direccion;
		TextView telefono;

	}
}
