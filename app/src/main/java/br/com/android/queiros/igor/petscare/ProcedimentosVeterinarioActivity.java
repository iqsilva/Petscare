package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import br.com.android.queiros.igor.petscare.Bean.Procedimento;
import br.com.android.queiros.igor.petscare.Bean.ProcedimentoList2;

public class ProcedimentosVeterinarioActivity extends AppCompatActivity {

    EditText editTextProcedimentoDescription;
    EditText editTextProcedimentoObservation;
    Button buttonSalvar;
    Button buttonCancelar;
    Button buttonAddProcedimentos;
    Button buttonAlterar;
    Button buttonDeletar;
    ListView listViewProcedimentos;
    List<Procedimento> procedimentos;
    DatabaseReference databaseProcedimentos;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimentos_veterinario);

        getSupportActionBar().setTitle("Procedimentos da Consulta");

        buttonAddProcedimentos = (Button) findViewById(R.id.buttonAdicionarProcedimento);
        listViewProcedimentos = (ListView) findViewById(R.id.listViewProcedimentos);

        final Intent intent = getIntent();
        final String animalId = intent.getStringExtra(AnimaisActivity.ANIMAL_ID);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseProcedimentos = FirebaseDatabase.getInstance().getReference("procedimentos").child(intent.getStringExtra(ConsultasActivity.CONSULTA_ID));
        //databaseProcedimentos = FirebaseDatabase.getInstance().getReference("procedimentos").child(user.getUid()).child(intent.getStringExtra(ConsultasActivity.CONSULTA_ID));

        procedimentos = new ArrayList<>();

        buttonAddProcedimentos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProcedimentoDialog();
        }
        });


        listViewProcedimentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Procedimento procedimento = procedimentos.get(i);
                showUpdateDeleteDialog(procedimento.getId(),procedimento.getDescricao());
                return true;
            }
        });
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
                ProcedimentoList2 trackListAdapter = new ProcedimentoList2(ProcedimentosVeterinarioActivity.this, procedimentos);
                if(trackListAdapter.getCount()!=0){
                    listViewProcedimentos.setAdapter(trackListAdapter);
                }else{
                    Toast.makeText(ProcedimentosVeterinarioActivity.this, "Não existem procedimentos nesta consulta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String procedimentoId, String descricao) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_procedimento_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextProcedimentoDescription = (EditText) dialogView.findViewById(R.id.editConsultaName);
        editTextProcedimentoObservation = (EditText) dialogView.findViewById(R.id.editConsultaStatus);
        buttonAlterar = (Button) dialogView.findViewById(R.id.buttonAlterar);
        buttonDeletar = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle(descricao);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descricao = editTextProcedimentoDescription.getText().toString();
                String observacao = editTextProcedimentoObservation.getText().toString();

                if (!TextUtils.isEmpty(descricao)) {
                    updateProcedimento(procedimentoId, descricao, observacao);
                    b.dismiss();
                }
            }
        });

        buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProcedimento(procedimentoId);
                b.dismiss();
                finish();
            }
        });
    }

    private void showProcedimentoDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.procedimento_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextProcedimentoDescription = (EditText) dialogView.findViewById(R.id.editConsultaName);
        editTextProcedimentoObservation = (EditText) dialogView.findViewById(R.id.editConsultaStatus);
        buttonSalvar = (Button) dialogView.findViewById(R.id.buttonAlterar);
        buttonCancelar = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle("Novo Procedimento");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String procedimentoDescription = editTextProcedimentoDescription.getText().toString().trim();
                String procedimentoObservation = editTextProcedimentoObservation.getText().toString();

                if(!TextUtils.isEmpty(procedimentoDescription)){
                    String id = databaseProcedimentos.push().getKey();

                    Procedimento procedimento = new Procedimento(id, procedimentoDescription, procedimentoObservation);

                    databaseProcedimentos.child(id).setValue(procedimento);

                    Toast.makeText(ProcedimentosVeterinarioActivity.this, "Procedimento salvo com sucesso", Toast.LENGTH_SHORT).show();

                    b.dismiss();
                }
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.dismiss();
            }
        });
    }

    private boolean updateProcedimento(String procedimentoId, String descricao, String observacao) {
        Procedimento procedimento = new Procedimento(procedimentoId, descricao, observacao);
        databaseProcedimentos.child(procedimentoId).setValue(procedimento);

        Toast.makeText(getApplicationContext(), "Procedimento Atualizado", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProcedimento(String procedimentoId) {

        databaseProcedimentos.child(procedimentoId).removeValue();

        Toast.makeText(getApplicationContext(), "Procedimento Excluído", Toast.LENGTH_LONG).show();

        return true;
    }

}
