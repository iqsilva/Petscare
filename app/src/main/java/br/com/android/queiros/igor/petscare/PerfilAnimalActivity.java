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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.android.queiros.igor.petscare.Bean.ImageUploadInfo;

public class PerfilAnimalActivity extends AppCompatActivity {

    ImageView imageView;
    EditText edtName;
    EditText edtBreed;
    EditText edtColor;
    TextView txtCoat;
    TextView txtGenres;
    TextView txtSpecies;
    TextView txtName;
    Button buttonUpdate;
    Button buttonDelete;
    Button buttonExcluir;
    Button buttonCancelar;

    DatabaseReference databaseAnimais;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_animal);
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        databaseAnimais = FirebaseDatabase.getInstance().getReference("animais").child(id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Informações do animal");

        edtName = (EditText) findViewById(R.id.editConsultaName);
        edtBreed = (EditText) findViewById(R.id.editConsultaStatus);
        edtColor = (EditText) findViewById(R.id.edtColor);
        txtCoat = (TextView) findViewById(R.id.edtCoat);
        txtGenres = (TextView) findViewById(R.id.edtGenre);
        txtSpecies = (TextView) findViewById(R.id.edtSpecie);
        buttonUpdate = (Button) findViewById(R.id.buttonAlterar);
        buttonDelete = (Button) findViewById(R.id.buttonDeletar);
        imageView = (ImageView) findViewById(R.id.ShowImageView);

        final Intent intent = getIntent();

        final String animalId = intent.getStringExtra(AnimaisActivity.ANIMAL_ID);
        final String name = intent.getStringExtra(AnimaisActivity.ANIMAL_NAME);
        String breed = intent.getStringExtra(AnimaisActivity.ANIMAL_BREED);
        String color = intent.getStringExtra(AnimaisActivity.ANIMAL_COLOR);
        String coat = intent.getStringExtra(AnimaisActivity.ANIMAL_COAT);
        String genre = intent.getStringExtra(AnimaisActivity.ANIMAL_GENRE);
        String specie = intent.getStringExtra(AnimaisActivity.ANIMAL_SPECIE);
        String imageurl= intent.getStringExtra(DisplayImagesActivity.ANIMAL_IMAGEURL);

        edtName.setText(name);
        edtBreed.setText(breed);
        edtColor.setText(color);
        txtCoat.setText(coat);
        txtGenres.setText(genre);
        txtSpecies.setText(specie);
        Glide.with(this).load(intent.getStringExtra(DisplayImagesActivity.ANIMAL_IMAGEURL)).bitmapTransform(new GlideCircleTransformation(getApplicationContext())).into(imageView);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtName.getText().toString();
                String breed = edtBreed.getText().toString();
                String color = edtColor.getText().toString();
                String coat = txtCoat.getText().toString();
                String genre = txtGenres.getText().toString();
                String specie = txtSpecies.getText().toString();
                String imageurl = intent.getStringExtra(DisplayImagesActivity.ANIMAL_IMAGEURL);

                if (!TextUtils.isEmpty(name)) {
                    Log.d("d","id_animal:"+name.toString());
                    updateAnimal(animalId, name, breed, color, coat, genre, specie,imageurl);
                    finish();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(animalId, name);
                //deleteAnimal(animalId);
                //finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, DisplayImagesActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    private void initializeFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseInstanceId.getInstance().getToken();
        FirebaseApp.initializeApp(this);
        databaseAnimais = firebaseDatabase.getReference();

    }

    private boolean updateAnimal(String animalId, String name, String breed, String color, String coat, String genre, String specie, String imageurl) {

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        //updating artist
        ImageUploadInfo animal = new ImageUploadInfo(animalId, name, breed, color, coat, genre, specie, imageurl);
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

    private void showDeleteDialog(final String animalId, String name) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        txtName = (TextView) dialogView.findViewById(R.id.editConsultaName);
        buttonExcluir = (Button) dialogView.findViewById(R.id.buttonExcluir);
        buttonCancelar = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle(name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAnimal(animalId);
                b.dismiss();
                finish();
            }
        });
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.dismiss();
            }
        });
    }

}

