package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import br.com.android.queiros.igor.petscare.Bean.Consulta;
import br.com.android.queiros.igor.petscare.Bean.ConsultaList2;

public class AnimaisConsultasVeterinarioActivity extends AppCompatActivity {
    public static final String CONSULTA_ID = "consultaid";
    public static final String CONSULTA_NAME = "consultaname";

    EditText editTextConsultaName;
    EditText editTextConsultaState;
    EditText editTextConsultaStatus;
    Button buttonSalvar;
    Button buttonCancelar;
    Button buttonAddConsultas;
    Button buttonAlterar;
    Button buttonDeletar;
    ListView listViewConsultas;
    List<Consulta> consultas;
    DatabaseReference databaseConsultas;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animais_consultas_veterinario);

        getSupportActionBar().setTitle("Consultas");

        buttonAddConsultas = (Button) findViewById(R.id.buttonAdicionarConsulta);
        listViewConsultas = (ListView) findViewById(R.id.listViewConsultas);

        Intent intent = getIntent();

        String id2 = intent.getStringExtra(TesteVeterinarioActivity.ANIMAL_ID);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseConsultas = FirebaseDatabase.getInstance().getReference("consultas").child(id2);

        consultas = new ArrayList<>();

        buttonAddConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConsultaDialog();
            }
        });

        listViewConsultas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Consulta consulta = consultas.get(i);
                showUpdateDeleteDialog(consulta.getConsultaId(),consulta.getConsultaName());
                return true;
            }
        });

        listViewConsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Consulta consulta = consultas.get(i);

                Intent intent = new Intent(getApplicationContext(), ProcedimentosVeterinarioActivity.class);

                intent.putExtra(CONSULTA_ID, consulta.getConsultaId());
                intent.putExtra(CONSULTA_NAME, consulta.getConsultaName());

                startActivity(intent);
            }
        });
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

                ConsultaList2 trackListAdapter = new ConsultaList2(AnimaisConsultasVeterinarioActivity.this, consultas);
                if(trackListAdapter.getCount()!=0){
                    listViewConsultas.setAdapter(trackListAdapter);
                }else{
                    Toast.makeText(AnimaisConsultasVeterinarioActivity.this, "Não existem consultas para este animal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String consultaId, String name) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_consulta_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextConsultaName = (EditText) dialogView.findViewById(R.id.editConsultaName);
        editTextConsultaStatus  = (EditText) dialogView.findViewById(R.id.editConsultaStatus);
        editTextConsultaState = (EditText) dialogView.findViewById(R.id.editConsultaState);
        buttonAlterar = (Button) dialogView.findViewById(R.id.buttonAlterar);
        buttonDeletar = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle(name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextConsultaName.getText().toString();
                String status = editTextConsultaStatus.getText().toString();
                String state = editTextConsultaState.getText().toString();

                if (!TextUtils.isEmpty(name)) {
                    updateConsulta(consultaId, name, status, state);
                    b.dismiss();
                }
            }
        });

        buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteConsulta(consultaId);
                b.dismiss();
                finish();
            }
        });
    }

    private void showConsultaDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.consulta_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextConsultaName = (EditText) dialogView.findViewById(R.id.editConsultaName);
        editTextConsultaStatus  = (EditText) dialogView.findViewById(R.id.editConsultaStatus);
        editTextConsultaState = (EditText) dialogView.findViewById(R.id.editConsultaState);
        buttonSalvar = (Button) dialogView.findViewById(R.id.buttonAlterar);
        buttonCancelar = (Button) dialogView.findViewById(R.id.buttonDeletar);

        dialogBuilder.setTitle("Nova Consulta");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String consultaName = editTextConsultaName.getText().toString().trim();
                String consultaStatus = editTextConsultaStatus.getText().toString();
                String consultaState = editTextConsultaState.getText().toString();

                if(!TextUtils.isEmpty(consultaName)){
                    String id = databaseConsultas.push().getKey();

                    Consulta consulta = new Consulta(id, consultaName, consultaStatus, consultaState);

                    databaseConsultas.child(id).setValue(consulta);

                    Toast.makeText(AnimaisConsultasVeterinarioActivity.this, "Consulta salva com sucesso", Toast.LENGTH_SHORT).show();

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

    private boolean updateConsulta(String consultaId, String name, String state, String status) {
        Consulta consulta = new Consulta(consultaId, name, state, status);
        databaseConsultas.child(consultaId).setValue(consulta);

        Toast.makeText(getApplicationContext(), "Consulta Atualizada", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteConsulta(String consultaId) {

        databaseConsultas.child(consultaId).removeValue();

        Toast.makeText(getApplicationContext(), "Consulta Excluída", Toast.LENGTH_LONG).show();

        return true;
    }

}
