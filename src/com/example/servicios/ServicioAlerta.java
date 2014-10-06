package com.example.servicios;

import java.util.Timer;
import java.util.TimerTask;

import com.example.agendacaracter.MainActivity;
import com.example.agendacaracter.R;
import com.example.reutilizables.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ServicioAlerta extends Service {
	private boolean alert6 = false;
	//private boolean alert8 = false;
	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 1000;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void hilo() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (Util.getHoraAlerta().equals("06:02") && !alert6) {
					mostrarNotBarra("¡Tienen un nuevo mensaje!", MainActivity.class);
					alert6 = true;				
				}
				/*else if (Util.getHoraAlerta().equals("19:00") && !alert8) {
					SharedPreferences preferencias = getSharedPreferences(
							"user", Context.MODE_PRIVATE);
					if (!Val.isEvaluated(getApplicationContext(),
							preferencias.getString("id", "0"))) {
						mostrarNotBarra("¡Aplica ya!",
								EvaluacionDiaria.class);
					}
					alert8 = true;
				}*/
			}
		}, 0, UPDATE_INTERVAL);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		hilo();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}

	private void mostrarNotBarra(String text, Class<?> activity) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Agenda Carácter";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);		
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this,activity);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);		
		notification.setLatestEventInfo(getApplicationContext(), "Agenda Carácter", text, contentIntent);
		mNotificationManager.notify(1, notification);
	}
}
