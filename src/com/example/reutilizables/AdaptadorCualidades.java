package com.example.reutilizables;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agendacaracter.R;
import com.example.entidad.Cualidades;

public class AdaptadorCualidades extends BaseAdapter{

	
	private static ArrayList<Cualidades> searchArrayList;

	private LayoutInflater mInflater;

	public AdaptadorCualidades(Context context, ArrayList<Cualidades> results) {
	searchArrayList = results;
	mInflater = LayoutInflater.from(context);
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
	convertView = mInflater.inflate(R.layout.customs_row_view, null);
	holder = new ViewHolder();
	holder.cualidad = (TextView) convertView.findViewById(R.id.txtcualidad);
	holder.mes = (TextView) convertView.findViewById(R.id.txtmes);
	holder.id = (TextView) convertView.findViewById(R.id.txtid);

	convertView.setTag(holder);
	} else {
	holder = (ViewHolder) convertView.getTag();
	}

	holder.cualidad.setText(searchArrayList.get(position).getCualidad());
	holder.mes.setText(searchArrayList.get(position).getMes());
	//holder.id.setText(searchArrayList.get(position).getId());

	return convertView;
	}

	static class ViewHolder {
	TextView cualidad;
	TextView mes;
	TextView id;
	}
}