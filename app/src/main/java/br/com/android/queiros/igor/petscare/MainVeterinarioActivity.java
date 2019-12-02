package br.com.android.queiros.igor.petscare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainVeterinarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MenuVeterinarioFragment.OnFragmentInteractionListener{

    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_veterinario);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FrameLayout container = (FrameLayout) findViewById(R.id.fragment_container);
        setSupportActionBar(toolbar);

        MenuVeterinarioFragment menuVeterinarioFragment = new MenuVeterinarioFragment();
        menuVeterinarioFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,menuVeterinarioFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, this.getText(R.string.voltar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_menu_sobre) {
            Intent tela = new Intent(this, SobreVeterinarioActivity.class);
            startActivityForResult( tela, 1 );

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FrameLayout container = (FrameLayout) findViewById(R.id.fragment_container);
        this.doubleBackToExitPressedOnce = false;
        if (id == R.id.nav_animais) {

            Intent tela = new Intent(this, UsuariosActivity.class);
            startActivityForResult( tela, 1 );

        }else if (id == R.id.nav_consultas) {
            Intent tela = new Intent(this, Usuarios2Activity.class);
            startActivityForResult( tela, 1 );

        }else if (id == R.id.nav_contato) {
            Intent tela = new Intent(this, ContatoVeterinarioActivity.class);
            startActivityForResult( tela, 1 );

        }else if (id == R.id.nav_avaliar) {
            String url = "https://goo.gl/forms/hH1g4QEshmddBDEv1";
            Intent tela = new Intent(Intent.ACTION_VIEW);
            tela.setData(Uri.parse(url));
            startActivityForResult( tela, 1 );

        }else if (id == R.id.nav_sair) {
            LogOut();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void LogOut() {

        //logging out the user
        firebaseAuth.signOut();
        //closing activity
        finish();
        //starting login activity
        startActivity(new Intent(this, LoginActivity.class));


    }
}
