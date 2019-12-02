package br.com.android.queiros.igor.petscare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.android.queiros.igor.petscare.Bean.Usuario;


public class LoginActivity extends AppCompatActivity {

    TextView txtEsqueceuSenha;
    TextView txtCadastro;
    EditText edtEmail;
    EditText edtSenha;
    Button btnLogin;

    DatabaseReference databaseUsuarios;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEsqueceuSenha  = (TextView) findViewById(R.id.txtEsqueceuSenha);
        txtCadastro  = (TextView) findViewById(R.id.txtCadastro);
        edtEmail = (EditText) findViewById(R.id.edtEmailRec);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        /**if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }**/

    }

    public void Logar (View v) {
        String Email = edtEmail.getText().toString();
        String Senha = edtSenha.getText().toString();
        if (Email.isEmpty()) {

            Context contextEmail = getApplicationContext();
            CharSequence alertEmail = "Preencha todos os campos!";

            int duration = Toast.LENGTH_SHORT;

            Toast toastEmail = Toast.makeText(contextEmail, alertEmail, duration);
            toastEmail.show();
        }else if (Senha.isEmpty()) {

                Context contextSenha = getApplicationContext();
                CharSequence alertSenha = "Preencha todos os campos!";

                int duration = Toast.LENGTH_SHORT;

                Toast toastSenha = Toast.makeText(contextSenha, alertSenha, duration);
                toastSenha.show();

        }else{
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();


                                databaseUsuarios = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user.getUid());
                                databaseUsuarios.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                    if(usuario.getUserType() == 0){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else if(usuario.getUserType() == 1){
                                        Intent intent = new Intent(LoginActivity.this, MainVeterinarioActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                    Toast.makeText(LoginActivity.this, "Logado com sucesso",
                                            Toast.LENGTH_SHORT).show();
                                }

                                /**databaseUsuarios = FirebaseDatabase.getInstance().getReference().child(user.getUid());
                                databaseUsuarios.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);                                    startActivity(intent);
                                        //LoginActivity.this.finish();

                                        //Toast.makeText(LoginActivity.this, "Bem-vindo ao Petscare",
                                        //Toast.LENGTH_LONG).show();
                                    }**/

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });

                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Senha ou E-mail invalidos",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            }

        }

    public void Cadastro(View v){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void esqueceuSenha(View v){
        Intent activityEsqueceuSenha = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(activityEsqueceuSenha);

    }

}
