package br.com.android.queiros.igor.petscare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.android.queiros.igor.petscare.Bean.Usuario;

public class CadastroActivity extends AppCompatActivity {

    EditText edtEmailUsuario, edtSenhaUsuario, edtNomeUsuario;
    Button btnConfirmarCadastro;

    Usuario usuario;

    private FirebaseAuth mAuth;

    // Write a message to the database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        edtEmailUsuario = (EditText) findViewById(R.id.edtEmailCadastro);
        edtSenhaUsuario = (EditText) findViewById(R.id.edtSenhaCadastro);
        edtNomeUsuario = (EditText) findViewById(R.id.edtNomeCadastro);
        btnConfirmarCadastro = (Button) findViewById(R.id.btnAdd);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
    }

    public Toast validaCampos(String msg) {
        Context context = getApplicationContext();
        CharSequence alert = msg;

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, alert, duration);
        //toast.show();
        return toast;
    }

    public boolean validarSenha(String senha) {
        int compSenhaMinima = 8;
        int compSenhaUsuario = senha.length();

        if (compSenhaMinima < compSenhaUsuario) {
            return true;
        }
        return false;
    }

    public final static boolean validarEmail(CharSequence target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean validarcSenha(String confirmarSenha, String senha) {
        return confirmarSenha.equals(senha);
    }

    public void compararCampos(View v) {

        EditText edtCampoEmail = (EditText) findViewById(R.id.edtEmailCadastro);
        CharSequence cadastroEmail = edtCampoEmail.getText();

        EditText edtCampoSenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        String cadastroSenha = edtCampoSenha.getText().toString();

        EditText edtCampoCsenha = (EditText) findViewById(R.id.edtConfSenhaCadastro);
        String cadastroCsenha = edtCampoCsenha.getText().toString();


        validarEmail(cadastroEmail);

        if (!validarSenha(cadastroSenha)) {

            Toast toastCompsenha = validaCampos("Tamanho do campo senha menor que 8 caracteres!");

            toastCompsenha.show();

        } else if (!validarcSenha(cadastroSenha, cadastroCsenha)) {

            Toast toastCompsenha = validaCampos("Tamanho do campo diferente do campo senha!");

            toastCompsenha.show();

        } else {

            mAuth.createUserWithEmailAndPassword(edtCampoEmail.getText().toString(), edtCampoSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                Toast.makeText(CadastroActivity.this, "Falha ao conectar com o banco de dados!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                btnConfirmarCadastro.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {

                                        usuario = new Usuario();
                                        usuario.setName(edtNomeUsuario.getText().toString());

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        final FirebaseUser user = mAuth.getCurrentUser();

                                        databaseReference.child("usuarios").child(user.getUid()).setValue(firebaseDatabase, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                //Problem with saving the data
                                                if (databaseError != null) {
                                                    Toast.makeText(CadastroActivity.this, "Erro ao criar usuário", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //Data uploaded successfully on the server
                                                    databaseReference.child("id").setValue(user.getUid().toString());
                                                    databaseReference.child("name").setValue(edtNomeUsuario.getText().toString());
                                                    databaseReference.child("userType").setValue(0);
                                                    Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }

                                            }
                                        });

                                    }
                                });



                            }

                            // ...
                        }
                    });

        }

    }

    /**public void confirmarCadastro(View v) {

        EditText edtCadastroEmail = (EditText) findViewById(R.id.edtEmailCadastro);
        String textoCadastroEmail = edtCadastroEmail.getText().toString();

        EditText edtCadastroSenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        String textoCadastroSenha = edtCadastroSenha.getText().toString();

        EditText edtCadastroCsenha = (EditText) findViewById(R.id.edtConfSenhaCadastro);
        String textoCadastroCsenha = edtCadastroCsenha.getText().toString();

        if (textoCadastroEmail.isEmpty()) {

            Context contextEmail = getApplicationContext();
            CharSequence alertEmail = "Campo e-mail vazio!";

            int duration = Toast.LENGTH_SHORT;

            Toast toastEmail = Toast.makeText(contextEmail, alertEmail, duration);
            toastEmail.show();

        } else if (textoCadastroSenha.isEmpty()) {

            Context contextSenha = getApplicationContext();
            CharSequence alertSenha = "Campo senha vazio!";

            int duration = Toast.LENGTH_SHORT;

            Toast toastSenha = Toast.makeText(contextSenha, alertSenha, duration);
            toastSenha.show();

        } else if (textoCadastroCsenha.isEmpty()) {

            Toast toastCsenha = validaCampos("Campo confirmar senha vazio!");
            toastCsenha.show();

        }
    }**/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, LoginActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

}

