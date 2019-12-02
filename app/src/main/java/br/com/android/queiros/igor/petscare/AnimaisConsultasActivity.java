package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.android.queiros.igor.petscare.Bean.Animal;
import br.com.android.queiros.igor.petscare.Bean.AnimalList;

public class AnimaisConsultasActivity extends AppCompatActivity {

    public static final String ANIMAL_ID = "animalid";
    public static final String ANIMAL_NAME = "animalname";
    public static final String ANIMAL_BREED = "animalbreed";
    public static final String ANIMAL_COLOR = "animalcolor";
    public static final String ANIMAL_COAT = "animalcoat";
    public static final String ANIMAL_GENRE = "animalgenre";
    public static final String ANIMAL_SPECIE = "animalspecie";

    DatabaseReference databaseAnimais;

    public ListView listViewAnimais;

    public List<Animal> animalList;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animais_consultas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Consultas por animal");

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        databaseAnimais = FirebaseDatabase.getInstance().getReference("animais").child(id);

        listViewAnimais = (ListView) findViewById(R.id.listViewAnimais);

        animalList = new ArrayList<>();

        listViewAnimais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Animal animal = animalList.get(i);

                Intent intent = new Intent(getApplicationContext(),ConsultasActivity.class);

                intent.putExtra(ANIMAL_ID, animal.getAnimalId());
                intent.putExtra(ANIMAL_NAME, animal.getName());
                intent.putExtra(ANIMAL_BREED, animal.getBreed());
                intent.putExtra(ANIMAL_COLOR, animal.getColor());
                intent.putExtra(ANIMAL_COAT, animal.getCoat());
                intent.putExtra(ANIMAL_GENRE, animal.getGenre());
                intent.putExtra(ANIMAL_SPECIE, animal.getSpecie());

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseAnimais.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                animalList = new ArrayList<Animal>();

                animalList.clear();

                for(DataSnapshot animalSnapshot : dataSnapshot.getChildren()){
                    Animal animal = animalSnapshot.getValue(Animal.class);

                    animalList.add(animal);
                }

                AnimalList adapter = new AnimalList(AnimaisConsultasActivity.this, animalList);
                listViewAnimais.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
