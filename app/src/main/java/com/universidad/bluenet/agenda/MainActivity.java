package com.universidad.bluenet.agenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.universidad.bluenet.agenda.Amigos.Activity_Amigos;
import com.universidad.bluenet.agenda.Mensajes.MensajeriaActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    RequestQueue requestQueue;
    RadioButton mantenerSesion;
    boolean ActivadoRadioButton;
    public static final String STRING_PREFERENCES = "ChatAgenda";
    private static final String USER_PREFERENCES   = "UsuarioLogueado";
    private static final String PASS_PREFERENCES   = "PassLogueado";
    private static final String PREFERENCES_ESTADO_BUTTON_SESSION ="EstadoSession";
    private static final String url = "http://192.168.43.15:8000/api/loginAndroid";// "http:127.0.0.1:8000";
    private static final String token = "http://192.168.43.15:8000/api/setToken"; //mandamos el token a la BD



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(obtenerEstadoButton()){
            //SubirToken();
            CambiarVista();
        }
        login = (Button) findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        requestQueue = Volley.newRequestQueue(this);
        mantenerSesion = (RadioButton) findViewById(R.id.MantenerSession);
        ActivadoRadioButton = mantenerSesion.isChecked();
        mantenerSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivadoRadioButton){
                    mantenerSesion.setChecked(false);
                }
                ActivadoRadioButton = mantenerSesion.isChecked();
            }
        });
    }

    public void guardarEstadoButton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_BUTTON_SESSION,mantenerSesion.isChecked()).apply();
        preferences.edit().putString(USER_PREFERENCES,username.getText().toString()).apply();
        preferences.edit().putString(PASS_PREFERENCES,username.getText().toString()).apply();
    }

    public boolean obtenerEstadoButton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        //String a = preferences.getString(USER_PREFERENCES,"no hay");
        //Toast.makeText(this, a , Toast.LENGTH_SHORT).show();
        //boolean  b = preferences.getBoolean(PREFERENCES_ESTADO_BUTTON_SESSION,false);

        //Toast.makeText(this, String.valueOf(b) , Toast.LENGTH_SHORT).show();
        return preferences.getBoolean(PREFERENCES_ESTADO_BUTTON_SESSION,false);

    }
    public void userLogin(View view){
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")){
                                SubirToken();
                                guardarEstadoButton();
                                CambiarVista();
                            }
                            Log.i("Response", response);
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
                    map.put("email",username.getText().toString().trim());
                    map.put("password", password.getText().toString().trim());
                    return map;
                }
            };
            requestQueue.add(postRequest);
    }
    private void CambiarVista(){
        //Intent intent = new Intent(this, Activity_Amigos.class);
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
        finish();//terminamos con la actividad loguin;
    }

    private void SubirToken(){
        StringRequest solicitud = new StringRequest(Request.Method.POST, token,
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
                map.put("email",username.getText().toString().trim());
                map.put("token_firebase", FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };
        requestQueue.add(solicitud);
    }
}
