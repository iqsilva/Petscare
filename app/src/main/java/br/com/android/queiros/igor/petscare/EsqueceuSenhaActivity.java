package br.com.android.queiros.igor.petscare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        mAuth = FirebaseAuth.getInstance();
    }

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

    public void enviarEmail(View v) {

        EditText edtEmail = (EditText) findViewById(R.id.edtEmailRec);
        String email = edtEmail.getText().toString();

        if (email.isEmpty()) {

            Context contextEmail = getApplicationContext();
            CharSequence alertEmail = "O campo e-mail está vazio!";

            int duration = Toast.LENGTH_SHORT;

            Toast toastEmail = Toast.makeText(contextEmail, alertEmail, duration);
            toastEmail.show();
        }else {

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener() {

                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EsqueceuSenhaActivity.this, R.string.sucesso_esqueceu_senha, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EsqueceuSenhaActivity.this, R.string.falha_esqueceu_senha, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}