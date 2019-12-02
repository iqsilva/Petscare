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

public class ProcedimentoList extends ArrayAdapter<Procedimento> {
    private Activity context;
    List<Procedimento> prodecimentos;

    public ProcedimentoList(Activity context, List<Procedimento> procedimentos) {
        super(context, R.layout.list_layout_procedimento, procedimentos);
        this.context = context;
        this.prodecimentos = procedimentos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout_procedimento, null, true);

        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewObservation = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Procedimento procedimento = prodecimentos.get(position);
        textViewDescription.setText(procedimento.getDescricao());
        textViewObservation.setText(procedimento.getObservacao());

        return listViewItem;
    }
}