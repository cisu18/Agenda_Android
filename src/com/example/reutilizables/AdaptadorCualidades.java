package com.example.reutilizables;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agendacaracter.R;
import com.example.entidad.Cualidad;

public class AdaptadorCualidades extends BaseAdapter {

	Typeface tf;
	private static ArrayList<Cualidad> searchArrayList;

	private LayoutInflater mInflater;

	public AdaptadorCualidades(Context context, ArrayList<Cualidad> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
	}

	public int getCount() {
		return searchArrayList.size();
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_lista_cualidad, null);
			holder = new ViewHolder();
			holder.mes = (TextView) convertView.findViewById(R.id.txv_item_mes_lista_cualidad);
			holder.cualidad = (TextView) convertView.findViewById(R.id.txv_item_cualidad_lista_cualidad);			
			holder.id = (TextView) convertView.findViewById(R.id.txv_item_id_lista_cualidad);
			
			TextView mes_cualidad = (TextView) convertView
					.findViewById(R.id.txv_item_mes_lista_cualidad);
			mes_cualidad.setTypeface(tf);

			TextView lista_cualidades = (TextView) convertView
					.findViewById(R.id.txv_item_cualidad_lista_cualidad);
			lista_cualidades.setTypeface(tf);
			
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		holder.mes.setText(searchArrayList.get(position).getMes());
		holder.cualidad.setText(searchArrayList.get(position).getCualidad());
		return convertView;
	}

	static class ViewHolder {
		TextView mes;
		TextView cualidad;		
		TextView id;
	}
}