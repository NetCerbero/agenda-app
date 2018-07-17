package com.universidad.bluenet.agenda.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.universidad.bluenet.agenda.Mensajes.MensajeriaActivity;
import com.universidad.bluenet.agenda.R;

import java.util.Random;

/**
 * Created by Pc on 27/4/2018.
 */

public class FirebaseServiceMenssage extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String data = remoteMessage.getData().get("mensaje");
        String hora = remoteMessage.getData().get("hora");
        int receptor = Integer.valueOf(remoteMessage.getData().get("id_Receptor"));
        int tipo = Integer.valueOf(remoteMessage.getData().get("tipo"));
        Mensaje(data,hora,receptor,tipo);
        showNotificacion();
    }

    private void Mensaje(String objeto, String hora,int receptor,int tipo){
        Intent i = new Intent(MensajeriaActivity.MENSAJE);
        i.putExtra("key_mensaje",objeto);
        i.putExtra("key_hora", hora);
        i.putExtra("key_receptor",receptor);
        i.putExtra("key_tipo",tipo);
        //getApplicationContext() => contexto actual de la APP
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void showNotificacion(){
        Intent i = new Intent(this, MensajeriaActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("BlueNet");
        builder.setContentText("Cuerpo de la notificacion");
        Uri soundNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundNotification);
        builder.setSmallIcon(R.drawable.icono_noti2);
        builder.setContentIntent(pendingIntent);
        //consumimos el servicio de notificacion del telefono
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random randomID = new Random();//para que no sobreescriba la notificacion
        notificationManager.notify(randomID.nextInt(), builder.build());
    }
}
