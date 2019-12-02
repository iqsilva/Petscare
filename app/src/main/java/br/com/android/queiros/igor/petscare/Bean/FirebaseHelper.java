package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 06/10/2017.
 */
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by lap on 01/10/2017.
 */

public class FirebaseHelper {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Boolean saved=null;
    ArrayList<String> animal =new ArrayList<>();
    public FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
    //WRITE
    public Boolean save(Animal animal)
    {
        if(animal==null)
        {
            saved=false;
        }else
            try {
                databaseReference.child("animais").push().setValue(animal);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        return saved;
    }
    //READ
    public ArrayList<String> retrieve()
    {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return animal;
    }
    private void fetchData(DataSnapshot dataSnapshot)
    {
        animal.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name=ds.getValue(Animal.class).getName();
            String breed=ds.getValue(Animal.class).getBreed();
            String color=ds.getValue(Animal.class).getColor();
            String coat=ds.getValue(Animal.class).getCoat();
            String genre=ds.getValue(Animal.class).getGenre();
            String specie=ds.getValue(Animal.class).getSpecie();
            animal.add(name);
            animal.add(breed);
            animal.add(color);
            animal.add(coat);
            animal.add(genre);
            animal.add(specie);
        }
    }
}