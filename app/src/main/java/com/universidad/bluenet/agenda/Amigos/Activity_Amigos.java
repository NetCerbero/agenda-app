package com.universidad.bluenet.agenda.Amigos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.universidad.bluenet.agenda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pc on 1/5/2018.
 */

public class Activity_Amigos extends AppCompatActivity {
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;
    private RecyclerView rv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        setTitle("Amigos");
        atributosList = new ArrayList<>();
        rv = (RecyclerView)findViewById(R.id.recyclerViewAmigos);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new AmigosAdapter(atributosList, this);
        rv.setAdapter(adapter);

        for(int i = 0 ; i < 10; i++){
            AgregarAmigos(R.drawable.ic_account_circle_black_24dp,"Usaurio "+i,"mensaje","00:"+i);


        }
    }

    public void AgregarAmigos(int FotoPerfil, String nombre, String ultimoMensaje, String hora ){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotodePerfil(FotoPerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setUltimoMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        atributosList.add(amigosAtributos);
        adapter.notifyDataSetChanged();// se hizo un cambio en la lista
    }
}
