package com.universidad.bluenet.agenda.Agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universidad.bluenet.agenda.Amigos.AmigosAdapter;
import com.universidad.bluenet.agenda.Amigos.AmigosAtributos;
import com.universidad.bluenet.agenda.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Pc on 2/5/2018.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaHolder> {
    private List<AgendaAtributos> atributosList;
    private Context context;

    public AgendaAdapter(List<AgendaAtributos> atributos, Context context){
        this.atributosList = atributos;
        this.context = context;
    }
    @Override
    public AgendaHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_agenda, parent,false);
        return new AgendaAdapter.AgendaHolder(v);
    }

    @Override
    public void onBindViewHolder(AgendaHolder holder, int position) {
        holder.titulo.setText(atributosList.get(position).getTitulo());
        holder.curso.setText(atributosList.get(position).getCurso());
        holder.fecha.setText(atributosList.get(position).getFecha());
        holder.mensaje.setText(atributosList.get(position).getMensaje());
    }

    @Override
    public int getItemCount() {

        return atributosList.size();
    }

    static class AgendaHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView fecha;
        TextView mensaje;
        TextView curso;

        public AgendaHolder(View itemView) {
            super(itemView);
            titulo = (TextView)itemView.findViewById(R.id.TituloAgenda);
            fecha = (TextView) itemView.findViewById(R.id.fechaAgenda);
            mensaje = (TextView) itemView.findViewById(R.id.mensajeAgenda);
            curso = (TextView) itemView.findViewById(R.id.cursoAgenda);
        }
    }
}
