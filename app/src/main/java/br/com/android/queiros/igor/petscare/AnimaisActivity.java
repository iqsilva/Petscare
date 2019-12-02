package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import br.com.android.queiros.igor.petscare.Bean.Animal;
import br.com.android.queiros.igor.petscare.Bean.AnimalList;

public class AnimaisActivity extends AppCompatActivity {

    public static final String ANIMAL_ID = "animalid";
    public static final String ANIMAL_NAME = "animalname";
    public static final String ANIMAL_BREED = "animalbreed";
    public static final String ANIMAL_COLOR = "animalcolor";
    public static final String ANIMAL_COAT = "animalcoat";
    public static final String ANIMAL_GENRE = "animalgenre";
    public static final String ANIMAL_SPECIE = "animalspecie";

    EditText edtName;
    EditText edtBreed;
    EditText edtColor;
    Spinner spinnerCoat;
    Spinner spinnerGenres;
    Spinner spinnerSpecies;
    Button buttonUpdate;
    Button buttonDelete;

    DatabaseReference databaseAnimais;
    FirebaseDatabase firebaseDatabase;

    public ListView listViewAnimais;

    public List<Animal> animalList;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animais);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Meus Animais");

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
                showUpdateDeleteDialog(animal.getAnimalId(), animal.getName());
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

    private void showUpdateDeleteDialog(final String animalId, String name) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_animal_dialog, null);
        dialogBuilder.setView(dialogView);

        edtName = (EditText) dialogView.findViewById(R.id.editConsultaName);
        edtBreed = (EditText) dialogView.findViewById(R.id.editConsultaStatus);
        edtColor = (EditText) dialogView.findViewById(R.id.edtColor);
        spinnerCoat = (Spinner) dialogView.findViewById(R.id.spinnerCoat);
        spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        spinnerSpecies = (Spinner) dialogView.findViewById(R.id.spinnerSpecies);
        buttonUpdate = (Button) dialogView.findViewById(R.id.buttonAlterar);
        buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle(name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String breed = edtBreed.getText().toString();
                String color = edtColor.getText().toString();
                String coat = spinnerCoat.getSelectedItem().toString();
                String genre = spinnerGenres.getSelectedItem().toString();
                String specie = spinnerSpecies.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name)) {
                    Log.d("d","id_animal:"+name.toString());
                    updateAnimal(animalId, name, breed, color, coat, genre, specie);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAnimal(animalId);
                b.dismiss();
            }
        });
    }

    private boolean updateAnimal(String animalId, String name, String breed, String color, String coat, String genre, String specie) {

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        //updating artist
        Animal animal = new Animal(animalId, name, breed, color, coat, genre, specie);
        initializeFirebase();
        Log.d("d","id_animal:"+animal.getAnimalId().toString());
        databaseAnimais.child("animais").child(id).child(animalId).setValue(animal);

        Toast.makeText(getApplicationContext(), "Animal Atualizado", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteAnimal(String animalId) {

        databaseAnimais.child(animalId).removeValue();

        Toast.makeText(getApplicationContext(), "Animal Excluído", Toast.LENGTH_LONG).show();

        return true;
    }

    private void initializeFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseInstanceId.getInstance().getToken();
        FirebaseApp.initializeApp(this);
        databaseAnimais = firebaseDatabase.getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
            databaseAnimais.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    animalList = new ArrayList<Animal>();

                    animalList.clear();

                    for (DataSnapshot animalSnapshot : dataSnapshot.getChildren()) {
                        Animal animal = animalSnapshot.getValue(Animal.class);

                        animalList.add(animal);
                    }

                    AnimalList adapter = new AnimalList(AnimaisActivity.this, animalList);
                    if(adapter.getCount()!=0){
                        listViewAnimais.setAdapter(adapter);
                    }else{
                        Toast.makeText(AnimaisActivity.this, "Não existem animais cadastrados",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
