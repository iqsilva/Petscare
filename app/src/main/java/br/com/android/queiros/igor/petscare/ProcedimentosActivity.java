package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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
import br.com.android.queiros.igor.petscare.Bean.Procedimento;
import br.com.android.queiros.igor.petscare.Bean.ProcedimentoList;

public class ProcedimentosActivity extends AppCompatActivity {

    /**EditText editTextProcedimentoDescription;
    EditText editTextProcedimentoObservation;
    Button buttonAddProcedimentos;**/
    ListView listViewProcedimentos;
    List<Procedimento> procedimentos;
    DatabaseReference databaseProcedimentos;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimentos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Procedimentos da Consulta");

        /**editTextProcedimentoDescription = (EditText) findViewById(R.id.editConsultaName);
        editTextProcedimentoObservation = (EditText) findViewById(R.id.editProcedimentoObservation);

        buttonAddProcedimentos = (Button) findViewById(R.id.buttonAddProcedimentos);**/
        listViewProcedimentos = (ListView) findViewById(R.id.listViewProcedimentos);
        Intent intent = getIntent();


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseProcedimentos = FirebaseDatabase.getInstance().getReference("procedimentos").child(intent.getStringExtra(ConsultasActivity.CONSULTA_ID));
        //databaseProcedimentos = FirebaseDatabase.getInstance().getReference("procedimentos").child(user.getUid()).child(intent.getStringExtra(ConsultasActivity.CONSULTA_ID));

        procedimentos = new ArrayList<>();

        /**buttonAddProcedimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProcedimentos();
            }
        });**/
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseProcedimentos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                procedimentos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Procedimento procedimento = postSnapshot.getValue(Procedimento.class);
                    procedimentos.add(procedimento);
                }
                ProcedimentoList trackListAdapter = new ProcedimentoList(ProcedimentosActivity.this, procedimentos);
                if(trackListAdapter.getCount()!=0){
                    listViewProcedimentos.setAdapter(trackListAdapter);
                }else{
                    Toast.makeText(ProcedimentosActivity.this, "Não existem procedimentos nesta consulta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**private void saveProcedimentos(){
        String procedimentoDescription = editTextProcedimentoDescription.getText().toString().trim();
        String procedimentoObservation = editTextProcedimentoObservation.getText().toString();
        if(!TextUtils.isEmpty(procedimentoDescription)){
            String id = databaseProcedimentos.push().getKey();

            Procedimento procedimento = new Procedimento(id, procedimentoDescription, procedimentoObservation);

            databaseProcedimentos.child(id).setValue(procedimento);

            Toast.makeText(this, "Procedimento salvo com sucesso", Toast.LENGTH_SHORT).show();

        } else{
            Toast.makeText(this, "Nome do procedimento não deve ser vazio", Toast.LENGTH_SHORT).show();
        }
    }**/
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

}
