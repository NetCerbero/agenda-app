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
import com.universidad.bluenet.agenda.Amigos.Activity_Amigos;
import com.universidad.bluenet.agenda.Amigos.AmigosAdapter;
import com.universidad.bluenet.agenda.Amigos.AmigosAtributos;
import com.universidad.bluenet.agenda.MainActivity;
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

public class MensajeriaFragment extends android.support.v4.app.Fragment {
    //Atributos de Activity_Amigos
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;
    private RecyclerView rv;
    private static final String url = "http://192.168.43.15:8000/api/getHijos/";
    //----------------------------

    private static final String TAB = "Mensajeria";
    private Button btnTest;

    //---Peticion al servidor ---
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_amigos,container,false);
        requestQueue = Volley.newRequestQueue(getContext());
        //Codigo de acitivity_amigos
        atributosList = new ArrayList<>();
        rv = (RecyclerView) v.findViewById(R.id.recyclerViewAmigos);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new AmigosAdapter(atributosList, getActivity());
        rv.setAdapter(adapter);


        SolicitarJson(url);
        return v;
    }

    //Metodo de Activity_amigos
    public void AgregarAmigos(int FotoPerfil, String nombre, String ultimoMensaje, String hora, int id, int tipo ){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotodePerfil(FotoPerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setUltimoMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        amigosAtributos.setId(id);
        amigosAtributos.setTipo(tipo);
        atributosList.add(amigosAtributos);
        adapter.notifyDataSetChanged();// se hizo un cambio en la lista
    }


    public void SolicitarJson(String url){
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
                            // mTextView.setText("Response: " + response.toString());
                            try{
                                String indice = response.getString("resultado");
                                JSONArray jsonArray = new JSONArray(indice);
                                for(int i = 0 ; i < jsonArray.length(); i++) {
                                    JSONObject usr = jsonArray.getJSONObject(i);
                                    AgregarAmigos(R.drawable.ic_account_circle_black_24dp, usr.getString("nombre"), usr.getString("ayuda"), "00:",usr.getInt("id"),usr.getInt("dst"));
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
