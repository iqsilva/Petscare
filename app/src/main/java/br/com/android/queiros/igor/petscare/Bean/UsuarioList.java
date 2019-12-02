package br.com.android.queiros.igor.petscare.Bean;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.android.queiros.igor.petscare.R;


public class UsuarioList extends ArrayAdapter<UserInformation> {
    private Activity context;
    private List<UserInformation> usuarioList;

    public UsuarioList(Activity context, List<UserInformation> usuarioList){
        super(context, R.layout.list_layout_usuarios, usuarioList);
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout_usuarios, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        UserInformation usuario = usuarioList.get(position);

        textViewName.setText(usuario.getName());

        return listViewItem;
    }
}
