package com.example.reutilizables;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.agendacaracter.R;
import com.example.entidad.cualidad;

public class AdaptadorAvance extends BaseAdapter {

	Typeface tf;
	private static ArrayList<cualidad> searchArrayList;
	
	private LayoutInflater mInflater;

	public AdaptadorAvance(Context context, ArrayList<cualidad> results) {
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
			convertView = mInflater.inflate(R.layout.custom_lista_avance, null);
			holder = new ViewHolder();
			holder.cualidad = (TextView) convertView
					.findViewById(R.id.txv_nombre_cualidad);
			holder.puntaje = (ProgressBar) convertView
					.findViewById(R.id.pb_avance_cualidad);
			holder.id = (TextView) convertView.findViewById(R.id.txv_id_avance);
			
			
			TextView mes_cualidad = (TextView) convertView
					.findViewById(R.id.txv_nombre_cualidad);
			mes_cualidad.setTypeface(tf);

			TextView lista_cualidades = (TextView) convertView
					.findViewById(R.id.txv_id_avance);
			lista_cualidades.setTypeface(tf);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cualidad.setText(searchArrayList.get(position).getMes());
		holder.puntaje.setProgress(Integer.parseInt(searchArrayList.get(
				position).getId()) * 10);
		
		GradientDrawable.Orientation fgGradDirection = GradientDrawable.Orientation.RIGHT_LEFT;
		GradientDrawable.Orientation bgGradDirection = GradientDrawable.Orientation.RIGHT_LEFT;

		// Background
		GradientDrawable bgGradDrawable = new GradientDrawable(bgGradDirection,
				new int[] { 0xff4d6371, 0xfffefffe });
		bgGradDrawable.setShape(GradientDrawable.RECTANGLE);
		bgGradDrawable.setCornerRadius(8);
		ClipDrawable bgclip = new ClipDrawable(bgGradDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
		bgclip.setLevel(10000);

		// SecondaryProgress
		GradientDrawable fg2GradDrawable = new GradientDrawable(				
				fgGradDirection, new int[] { 0xff464747, 0xffFFFFFF });
		fg2GradDrawable.setShape(GradientDrawable.RECTANGLE);
		fg2GradDrawable.setCornerRadius(8);
		ClipDrawable fg2clip = new ClipDrawable(fg2GradDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);

		// Progress
		GradientDrawable fg1GradDrawable = new GradientDrawable(
				fgGradDirection, new int[] { 0xff00456e,0xff00cec5  });
		fg1GradDrawable.setShape(GradientDrawable.RECTANGLE);
		fg1GradDrawable.setCornerRadius(8);
		ClipDrawable fg1clip = new ClipDrawable(fg1GradDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);

		// Setup LayerDrawable and assign to progressBar
		Drawable[] progressDrawables = { bgclip, fg2clip, fg1clip };
		LayerDrawable progressLayerDrawable = new LayerDrawable(
				progressDrawables);
		progressLayerDrawable.setId(0, android.R.id.background);
		progressLayerDrawable.setId(1, android.R.id.secondaryProgress);
		progressLayerDrawable.setId(2, android.R.id.progress);

		// Copy the existing ProgressDrawable bounds to the new one.
		Rect bounds = holder.puntaje.getProgressDrawable().getBounds();
		holder.puntaje.setProgressDrawable(progressLayerDrawable);
		holder.puntaje.getProgressDrawable().setBounds(bounds);		

		holder.id.setText(Integer.parseInt(searchArrayList.get(position)
				.getId()) * 10 + "%");
		return convertView;
	}

	static class ViewHolder {
		TextView cualidad;
		ProgressBar puntaje;
		TextView id;
	}

}