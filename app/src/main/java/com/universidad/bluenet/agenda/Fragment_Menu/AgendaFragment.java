package com.universidad.bluenet.agenda.Fragment_Menu;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
//import android.support.v4.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.universidad.bluenet.agenda.Agenda.AgendaAdapter;
import com.universidad.bluenet.agenda.Agenda.AgendaAtributos;
import com.universidad.bluenet.agenda.Amigos.AmigosAdapter;
import com.universidad.bluenet.agenda.Amigos.AmigosAtributos;
import com.universidad.bluenet.agenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pc on 1/5/2018.
 */

public class AgendaFragment extends android.support.v4.app.Fragment {
    private static final String TAB = "Agenda";
    private Button btnTest;

    private List<AgendaAtributos> atributosList;
    private AgendaAdapter adapter;
    private RecyclerView rv;
    private static final String url = "http://192.168.43.15:8000/api/getAgendas/";
    //----------------------------
    //---Peticion al servidor ---
    private RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_agenda,container,false);
        requestQueue = Volley.newRequestQueue(getContext());
        //Codigo de acitivity_amigos
        atributosList = new ArrayList<>();
        rv = (RecyclerView) v.findViewById(R.id.recyclerViewAgenda);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new AgendaAdapter(atributosList, getActivity());
        rv.setAdapter(adapter);
        SolicitarAgenda(url);
        return v;
    }

    public void AgregarAgenda(String titulo, String mensaje, String fecha, String curso){
        AgendaAtributos agendaAtributos = new AgendaAtributos();
        agendaAtributos.setCurso(curso);
        agendaAtributos.setFecha(fecha);
        agendaAtributos.setTitulo(titulo);
        agendaAtributos.setMensaje(mensaje);
        atributosList.add(agendaAtributos);
        adapter.notifyDataSetChanged();// se hizo un cambio en la lista
    }
    public void SolicitarAgenda(String url){
        SharedPreferences prf = getContext().getSharedPreferences("ChatAgenda",MODE_PRIVATE);
        String email = prf.getString("UsuarioLogueado", null);
        // Log.i("preferencia-prueba:",email);
        //AgregarAmigos(R.drawable.ic_account_circle_black_24dp,email,"aaa","0");
        if(email != null){
            url = url + email;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                Log.i("Tamaño ",String.valueOf(response.length()));
                                for(int i = 1 ; i <= response.length(); i++) {
                                    String indice = response.getString(String.valueOf(i));
                                    JSONArray jsonArray = new JSONArray(indice);
                                    for(int in = 0 ; in <jsonArray.length(); in++){
                                        JSONObject usr = jsonArray.getJSONObject(in);
                                        AgregarAgenda(usr.getString("titulo"),usr.getString("mensaje"),usr.getString("fecha"),usr.getString("nombre"));
                                    }
                                }
                            }catch (JSONException e){
                                Toast.makeText(getContext(),"error de parseo json",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),"Ocurrio un error",Toast.LENGTH_SHORT).show();

                        }
                    });
            requestQueue.add(jsonObjectRequest);
        }

    }
}
