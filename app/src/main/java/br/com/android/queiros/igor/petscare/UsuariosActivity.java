package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import br.com.android.queiros.igor.petscare.Bean.UserInformation;
import br.com.android.queiros.igor.petscare.Bean.Usuario;
import br.com.android.queiros.igor.petscare.Bean.UsuarioList;

public class UsuariosActivity extends AppCompatActivity {

    public static final String USUARIO_ID = "usuarioid";
    public static final String USUARIO_NAME = "usuarioname";

    EditText edtName;

    DatabaseReference databaseUsuarios;
    FirebaseDatabase firebaseDatabase;

    public ListView listViewUsuarios;

    public List<UserInformation> usuarioList;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        getSupportActionBar().setTitle("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        databaseUsuarios = FirebaseDatabase.getInstance().getReference();

        listViewUsuarios = (ListView) findViewById(R.id.listViewUsuarios);

        usuarioList = new ArrayList<>();

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInformation usuario = usuarioList.get(i);

                Intent intent = new Intent(getApplicationContext(), AnimaisVeterinarioActivity.class);

                intent.putExtra(USUARIO_ID, usuario.getId());
                intent.putExtra(USUARIO_NAME, usuario.getName());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseUsuarios.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioList = new ArrayList<UserInformation>();

                usuarioList.clear();

                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    UserInformation usuario = usuarioSnapshot.getValue(UserInformation.class);

                    usuarioList.add(usuario);
                }

                UsuarioList adapter = new UsuarioList(UsuariosActivity.this, usuarioList);
                if(adapter.getCount()!=0){
                    listViewUsuarios.setAdapter(adapter);
                }else{
                    Toast.makeText(UsuariosActivity.this, "Não existem usuarios cadastrados",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
