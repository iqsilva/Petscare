package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import br.com.android.queiros.igor.petscare.Bean.Consulta;
import br.com.android.queiros.igor.petscare.Bean.ConsultaList;

public class ConsultasActivity extends AppCompatActivity {
    public static final String CONSULTA_ID = "consultaid";
    public static final String CONSULTA_NAME = "consultaname";

    ListView listViewConsultas;
    List<Consulta> consultas;
    DatabaseReference databaseConsultas;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Consultas");

        listViewConsultas = (ListView) findViewById(R.id.listViewConsultas);

        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseConsultas = FirebaseDatabase.getInstance().getReference("consultas").child(intent.getStringExtra(AnimaisActivity.ANIMAL_ID));

        consultas = new ArrayList<>();

        listViewConsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Consulta consulta = consultas.get(i);

                Intent intent = new Intent(getApplicationContext(), ProcedimentosActivity.class);

                intent.putExtra(CONSULTA_ID, consulta.getConsultaId());
                intent.putExtra(CONSULTA_NAME, consulta.getConsultaName());

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, TesteActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseConsultas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consultas.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Consulta consulta = postSnapshot.getValue(Consulta.class);
                    consultas.add(consulta);
                }

                ConsultaList trackListAdapter = new ConsultaList(ConsultasActivity.this, consultas);
                if(trackListAdapter.getCount()!=0){
                    listViewConsultas.setAdapter(trackListAdapter);
                }else{
                    Toast.makeText(ConsultasActivity.this, "Não existem consultas para este animal",Toast.LENGTH_SHORT).show();
                }
                //listViewConsultas.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
