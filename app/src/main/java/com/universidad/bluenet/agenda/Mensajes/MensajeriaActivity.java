package com.universidad.bluenet.agenda.Mensajes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.universidad.bluenet.agenda.Mensajes.MensajeDeTexto;
import com.universidad.bluenet.agenda.R;
import com.universidad.bluenet.agenda.Services.FirebaseID;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class MensajeriaActivity extends AppCompatActivity {
    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver bR;
    private RecyclerView rv;
    private List<MensajeDeTexto> mensajeDeTextoList;
    private MensajesAdapter adapter;
    private Button btnEnviarMSG;
    private EditText etEscribirMSG;
    private int ID_Receptor;
    private int Tipo_Dst;
    private String EMISOR;
    private String MENSAJE_ENVIAR="";
    private RequestQueue requestQueue;
    private String urlMensajeAndroid="http://192.168.43.15:8000/api/setMensajeAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);
        mensajeDeTextoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences prf = getSharedPreferences("ChatAgenda",MODE_PRIVATE);
        EMISOR = prf.getString("UsuarioLogueado",null);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle != null){
            ID_Receptor = bundle.getInt("key_receptor");
            Tipo_Dst = bundle.getInt(("key_tipo"));

        }


        btnEnviarMSG = (Button) findViewById(R.id.EnviarMensaje);
        etEscribirMSG = (EditText) findViewById(R.id.mensajeEnviar);
        rv = (RecyclerView) findViewById(R.id.recyclerViewMensaje);
        LinearLayoutManager ln = new LinearLayoutManager(this);
        //rv.setHasFixedSize(true);
        rv.setLayoutManager(ln); //tener los datos en formatos

        /*for(int i=0; i<10; i++){
            MensajeDeTexto aux = new MensajeDeTexto();
            aux.setId(""+ i);
            aux.setMensaje("hola a todos"+i);
            aux.setHoradelMensaje("10:3"+i+" am");
            aux.setTipoMensaje(1);
            //añadiendo mensajes
            mensajeDeTextoList.add(aux);
        }*/

        adapter = new MensajesAdapter(mensajeDeTextoList);
        rv.setAdapter(adapter);


        btnEnviarMSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = etEscribirMSG.getText().toString().trim();
                String TOKEN = FirebaseInstanceId.getInstance().getToken();
                if(!ms.isEmpty()){
                    MENSAJE_ENVIAR = ms;
                    MandarMensaje();
                    Calendar calendar = Calendar.getInstance();
                    CrearMensaje(ms,String.valueOf(calendar.getTime().getHours())+":"+String.valueOf(calendar.getTime().getMinutes()),1);
                    etEscribirMSG.setText("");
                }
                if( TOKEN != null){
                    
                    Toast.makeText(MensajeriaActivity.this, TOKEN, Toast.LENGTH_SHORT).show();
                }

            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setScrollbarChat();
        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");
                int receptor = intent.getIntExtra("key_receptor",-1);
                int tipo = intent.getIntExtra("key_tipo",-1);
                if(receptor == ID_Receptor && tipo == Tipo_Dst){
                    CrearMensaje(mensaje,hora, 2);
                }

                //Toast.makeText(MensajeriaActivity.this,d,Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void MandarMensaje(){
        StringRequest solicitud = new StringRequest(Request.Method.POST, urlMensajeAndroid,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Log.i("Agenda ok", response);
                        }else{
                            Log.i("Agenda fallo", response);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.i("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  map = new HashMap<String, String>();
                map.put("email",EMISOR);
                map.put("id", String.valueOf(ID_Receptor));
                map.put("tipo_dst",String.valueOf(Tipo_Dst));
                map.put("mensaje",MENSAJE_ENVIAR);
                return map;
            }
        };
        requestQueue.add(solicitud);
    }

    public void CrearMensaje(String msg, String hora, int tipoMsg){
        MensajeDeTexto aux = new MensajeDeTexto();
        aux.setId("0");
        aux.setMensaje(msg);
        aux.setHoradelMensaje(hora);
        aux.setTipoMensaje(tipoMsg);
        mensajeDeTextoList.add(aux);
        adapter.notifyDataSetChanged();/*notifica que huvo un cambio*/
        //limpiamos el campo de entrada
        setScrollbarChat();
    }

    //Baja el scroll al ultimo mensaje
    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Se detiene junto con la app asi no dara error al intentar actualizar
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bR, new IntentFilter(MENSAJE));
    }
}
