package com.universidad.bluenet.agenda.Mensajes;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pc on 25/4/2018.
 */

public class MensajeDeTexto extends AppCompatActivity{
    private String id;
    private String mensaje;
    private int tipoMensaje;
    private String HoradelMensaje;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHoradelMensaje() {
        return HoradelMensaje;
    }

    public void setHoradelMensaje(String horadelMensaje) {
        this.HoradelMensaje = horadelMensaje;
    }
}
