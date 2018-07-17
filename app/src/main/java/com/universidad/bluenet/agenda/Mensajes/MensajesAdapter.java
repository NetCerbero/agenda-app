package com.universidad.bluenet.agenda.Mensajes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universidad.bluenet.agenda.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Pc on 25/4/2018.
 */

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajesViewHolder> {
    private List<MensajeDeTexto> mensajeDeTexto;

    public MensajesAdapter(List<MensajeDeTexto> mensajeDeTexto) {
        this.mensajeDeTexto = mensajeDeTexto;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*Retorna una vista*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes,parent,false);
        return new MensajesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int position) {
        /*Donde se modifica cada tarjeta que vamos a tener*/
        FrameLayout.LayoutParams f = (FrameLayout.LayoutParams) holder.contenedorMensaje.getLayoutParams();
        LinearLayout.LayoutParams hr = (LinearLayout.LayoutParams) holder.tvHora.getLayoutParams();
        LinearLayout.LayoutParams txt = (LinearLayout.LayoutParams) holder.tvMensaje.getLayoutParams();
        if(mensajeDeTexto.get(position).getTipoMensaje() == 1){
            holder.contenedorMensaje.setBackgroundResource(R.drawable.in_message_bg);
            f.gravity = Gravity.RIGHT;
            hr.gravity = Gravity.RIGHT;
            txt.gravity = Gravity.RIGHT;
        }else{
            holder.contenedorMensaje.setBackgroundResource(R.drawable.out_message_bg);
            f.gravity = Gravity.LEFT;
            hr.gravity = Gravity.LEFT;
            txt.gravity = Gravity.LEFT;
        }
        holder.contenedorMensaje.setLayoutParams(f);
        holder.tvHora.setLayoutParams(hr);
        holder.tvMensaje.setLayoutParams(txt);
        holder.tvMensaje.setText(mensajeDeTexto.get(position).getMensaje());
        holder.tvHora.setText(mensajeDeTexto.get(position).getHoradelMensaje());
    }

    @Override
    public int getItemCount() {
        /*Elementos que tiene RecyclerView*/
        return mensajeDeTexto.size();
    }

    static class MensajesViewHolder extends RecyclerView.ViewHolder{
        /*creamos la variables que estan en nuestra tarjeta*/
        CardView cardView;
        TextView tvMensaje, tvHora;
        LinearLayout contenedorMensaje;
        MensajesViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardViewMensaje);
            tvMensaje = (TextView) itemView.findViewById(R.id.msTexto);
            tvHora = (TextView) itemView.findViewById(R.id.msHora);
            contenedorMensaje = (LinearLayout)itemView.findViewById(R.id.contenedor);
        }
    }
}
