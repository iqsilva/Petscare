package br.com.android.queiros.igor.petscare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
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

import br.com.android.queiros.igor.petscare.Bean.ImageUploadInfo;

public class TesteVeterinarioActivity extends AppCompatActivity {
    public static final String ANIMAL_ID = "animalid";
    public static final String USUARIO_ID = "usuarioid";
    public static final String ANIMAL_NAME = "animalname";
    public static final String ANIMAL_BREED = "animalbreed";
    public static final String ANIMAL_COLOR = "animalcolor";
    public static final String ANIMAL_COAT = "animalcoat";
    public static final String ANIMAL_GENRE = "animalgenre";
    public static final String ANIMAL_SPECIE = "animalspecie";
    public static final String ANIMAL_IMAGEURL = "animalimageurl";

    // Creating DatabaseReference.
    DatabaseReference databaseReference;
    // Creating RecyclerView.
    RecyclerView recyclerView;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;
    // Creating Progress dialog
    ProgressDialog progressDialog;
    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_veterinario);
        getSupportActionBar().setTitle("Consultas por Animal");

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        String id = intent.getStringExtra(Usuarios2Activity.USUARIO_ID);
        String name = intent.getStringExtra(Usuarios2Activity.USUARIO_NAME);

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(TesteVeterinarioActivity.this));
        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(TesteVeterinarioActivity.this);
        // Setting up message in Progress dialog.
        progressDialog.setMessage("Carregando Imagens...");
        // Showing progress dialog.
        progressDialog.show();
        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("animais").child(id);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);


                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerAdapter4(getApplicationContext(), list);
                if(adapter.getItemCount()!=0){
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(TesteVeterinarioActivity.this, "Não existem animais cadastrados",Toast.LENGTH_SHORT).show();
                }

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
    }
}