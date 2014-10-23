package com.clmdev.servicios;

import java.util.Timer;
import java.util.TimerTask;

import com.clmdev.agendacaracter.EvaluacionDiaria;
import com.clmdev.agendacaracter.MainActivity;
import com.clmdev.agendacaracter.R;
import com.clmdev.reutilizables.Util;
import com.clmdev.reutilizables.Val;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class ServicioAlerta extends Service {

	private static ServicioAlerta instance = null;
	private boolean alert6 = false;
	private boolean alert8 = false;
	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 1000;
	MainActivity main = new MainActivity();

	public static boolean isRunning() {
		return instance != null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		instance = this;
	}

	@Override
	public void onDestroy() {
		instance = null;
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		hilo();
	}

	protected Boolean conectadoWifi() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected Boolean conectadoRedMovil() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected Boolean estaConectado() {
		if (conectadoWifi()) {
			return true;
		} else {
			if (conectadoRedMovil()) {
				return true;
			} else {
				return false;

			}
		}
	}

	private void hilo() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				if (Util.getHoraAlerta().equals("06:00") && estaConectado()
						&& !alert6) {
					mostrarNotBarra("¡Tienen un nuevo mensaje!",
							MainActivity.class);
					alert6 = true;
				}
				if (Util.getHoraAlerta().equals("20:00") && estaConectado()
						&& !alert8) {
					SharedPreferences preferencias = getSharedPreferences(
							"user", Context.MODE_PRIVATE);
					if (!Val.isEvaluated(getApplicationContext(),
							preferencias.getString("id", "0"))) {
						mostrarNotBarra("¡Aplica ya!", EvaluacionDiaria.class);
					}
					alert8 = true;
				}

			}
		}, 0, UPDATE_INTERVAL);
	}

	private void mostrarNotBarra(String text, Class<?> activity) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Agenda Carácter";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this, activity);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(getApplicationContext(),
				"Agenda Carácter", text, contentIntent);
		mNotificationManager.notify(1, notification);
	}
}
