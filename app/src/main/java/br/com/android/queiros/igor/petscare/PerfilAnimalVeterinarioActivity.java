package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class PerfilAnimalVeterinarioActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtName;
    TextView txtBreed;
    TextView txtColor;
    TextView txtCoat;
    TextView txtGenres;
    TextView txtSpecies;


    DatabaseReference databaseAnimais;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_animal_veterinario);
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        databaseAnimais = FirebaseDatabase.getInstance().getReference("animais").child(id);

        getSupportActionBar().setTitle("Informações do animal");

        txtName = (TextView) findViewById(R.id.editConsultaName);
        txtBreed = (TextView) findViewById(R.id.editConsultaStatus);
        txtColor = (TextView) findViewById(R.id.edtColor);
        txtCoat = (TextView) findViewById(R.id.edtCoat);
        txtGenres = (TextView) findViewById(R.id.edtGenre);
        txtSpecies = (TextView) findViewById(R.id.edtSpecie);
        imageView = (ImageView) findViewById(R.id.ShowImageView);

        final Intent intent = getIntent();

        final String animalId = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_ID);
        final String name = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_NAME);
        String breed = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_BREED);
        String color = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_COLOR);
        String coat = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_COAT);
        String genre = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_GENRE);
        String specie = intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_SPECIE);
        String imageurl= intent.getStringExtra(AnimaisVeterinarioActivity.ANIMAL_IMAGEURL);

        txtName.setText(name);
        txtBreed.setText(breed);
        txtColor.setText(color);
        txtCoat.setText(coat);
        txtGenres.setText(genre);
        txtSpecies.setText(specie);
        Glide.with(this).load(intent.getStringExtra(DisplayImagesActivity.ANIMAL_IMAGEURL)).bitmapTransform(new GlideCircleTransformation(getApplicationContext())).into(imageView);
    }

}

