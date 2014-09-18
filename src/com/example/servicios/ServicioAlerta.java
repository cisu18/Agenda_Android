package com.example.servicios;

import java.util.Timer;
import java.util.TimerTask;

import com.example.agendacaracter.EvaluacionDiaria;
import com.example.agendacaracter.MainActivity;
import com.example.agendacaracter.R;
import com.example.reutilizables.Util;
import com.example.reutilizables.Val;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
public class ServicioAlerta extends Service {
	private boolean alert6 = false;
	private boolean alert12 = false;
	private boolean alert8 = false;
	private Timer timer = new Timer();
	private static final long UPDATE_INTERVAL = 60000;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void hilo() {

		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				
				if (Util.getHoraAlerta().equals("06:00")&&!alert6) {
					mostrarNotBarra("Nuevo vers�culo... ;-)", MainActivity.class);
					alert6=true;
				}else if(Util.getHoraAlerta().equals("12:00")&&!alert12){
					mostrarNotBarra("!Quiero evaluarme! :-)", EvaluacionDiaria.class);
					alert12=true;
				}else if(Util.getHoraAlerta().equals("20:00")&&!alert8){
					SharedPreferences preferencias = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					if (!Val.isEvaluated(getApplicationContext(),preferencias.getString("id", "0"))) {
						mostrarNotBarra("�Hoy no me he evaluado! :-(", EvaluacionDiaria.class);
					}					
					alert8=true;
				}
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
		CharSequence tickerText = "Notificaci�n Agenda Car�cter";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		notification.defaults |= Notification.DEFAULT_SOUND;

		notification.defaults |= Notification.DEFAULT_VIBRATE;

		Intent notificationIntent = new Intent(this,activity);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		
		notification.setLatestEventInfo(context, "Agenda Car�cter", text, contentIntent);
		mNotificationManager.notify(1, notification);
	}
}