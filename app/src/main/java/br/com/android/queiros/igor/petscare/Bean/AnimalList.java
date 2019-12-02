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

import br.com.android.queiros.igor.petscare.Bean.Animal;
import br.com.android.queiros.igor.petscare.R;

/**
 * Created by igorf on 01/10/2017.
 */

public class AnimalList extends ArrayAdapter<Animal>{
    private Activity context;
    private List<Animal> animalList;

    public AnimalList(Activity context, List<Animal> animalList){
        super(context, R.layout.list_layout, animalList);
        this.context = context;
        this.animalList = animalList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);


        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewBreed = (TextView) listViewItem.findViewById(R.id.textViewBreed);
        TextView textViewColor = (TextView) listViewItem.findViewById(R.id.textViewColor);
        TextView textViewCoat = (TextView) listViewItem.findViewById(R.id.textViewCoat);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewSpecie = (TextView) listViewItem.findViewById(R.id.textViewSpecie);

        Animal animal = animalList.get(position);

        textViewName.setText(animal.getName());
        textViewBreed.setText(animal.getBreed());
        textViewColor.setText(animal.getColor());
        textViewCoat.setText(animal.getCoat());
        textViewGenre.setText(animal.getGenre());
        textViewSpecie.setText(animal.getSpecie());

        return listViewItem;
    }
}
