package br.com.android.queiros.igor.petscare.Bean;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.android.queiros.igor.petscare.R;

/**
 * Created by igorf on 12/10/2017.
 */

public class ConsultaList extends ArrayAdapter<Consulta> {
    private Activity context;
    List<Consulta> consultas;

    public ConsultaList(Activity context, List<Consulta> consultas) {
        super(context, R.layout.list_layout_consulta, consultas);
        this.context = context;
        this.consultas = consultas;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout_consulta, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textViewStatus);
        TextView textViewState = (TextView) listViewItem.findViewById(R.id.textViewState);

        Consulta consulta = consultas.get(position);
        textViewName.setText(consulta.getConsultaName());
        textViewStatus.setText(consulta.getConsultaStatus());
        textViewState.setText(consulta.getConsultaState());

        return listViewItem;
    }
}