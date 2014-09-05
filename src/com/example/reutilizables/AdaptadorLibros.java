package com.example.reutilizables;

import java.util.ArrayList;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.example.agendacaracter.R;
import com.example.entidad.Libro;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorLibros extends BaseAdapter{

	Typeface tf;
	private static ArrayList<Libro> searchArrayList;
	private LayoutInflater mInflater;
	

	public AdaptadorLibros(Context context, ArrayList<Libro> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/Myriad_Pro.ttf");
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
			convertView = mInflater.inflate(R.layout.custom_row_view2, null);
			holder = new ViewHolder();
			holder.imagen = (ImageView) convertView
					.findViewById(R.id.img_Libro);
			holder.tituloLibro = (TextView) convertView.findViewById(R.id.txt_TituloLibro);
			holder.idLibro = (TextView) convertView.findViewById(R.id.txt_idLibro);

			TextView titulo_libro = (TextView) convertView
					.findViewById(R.id.txt_TituloLibro);
			titulo_libro.setTypeface(tf);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imagen.setImageBitmap(searchArrayList.get(position).getBimagen());
		//holder.imagen.setContentDescription(searchArrayList.get(position).getUrlImagen().toString());
		holder.tituloLibro.setText(searchArrayList.get(position).getTituloLibro());
		// holder.id.setText(searchArrayList.get(position).getId());

		return convertView;
	}
	static class ViewHolder {
		ImageView imagen;
		TextView tituloLibro;
		TextView idLibro;
		public ImageView getImagen() {
			return imagen;
		}
		public void setImagen(ImageView imagen) {
			this.imagen = imagen;
		}
		
		
		
	}

}
