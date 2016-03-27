package dev.tcc.caique.medreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.fragments.AboutFragment;
import dev.tcc.caique.medreport.fragments.AccompanimentsFragment;
import dev.tcc.caique.medreport.fragments.InviteFragment;
import dev.tcc.caique.medreport.fragments.ProfileMedicalFragment;
import dev.tcc.caique.medreport.fragments.ProfilePacientFragment;
import dev.tcc.caique.medreport.fragments.ReportFragment;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.DialogUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public FloatingActionButton fab;
    public NavigationView navigationView;
    public Toolbar toolbar;
    private TextView name;
    private TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AccompanimentsFragment(), "HOME").addToBackStack(null).commit();
        }
        View headerLayout = navigationView.getHeaderView(0);
        name = (TextView) headerLayout.findViewById(R.id.nameHeader);
        type = (TextView) headerLayout.findViewById(R.id.typeAccount);
        name.setText("Olá " + Singleton.getInstance().getName());
        type.setText(Singleton.getInstance().getType().equals("1") ? "Médico" : "Paciente");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isAdded("HOME")) {
            DialogUtils.createDialogCloseApp(this, "Deseja realmente fechar o aplicativo?");
        } else {
            super.onBackPressed();
        }
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        boolean b = true;
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            if (Singleton.getInstance().getType().equals("1"))
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileMedicalFragment()).addToBackStack(null).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfilePacientFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_report) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReportFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_invite) {
            toolbar.setTitle("Convites");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new InviteFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_chat) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AccompanimentsFragment(), "HOME").addToBackStack(null).commit();
        } else if (id == R.id.nav_share) {
            b = false;
            String shareBody = "Here is the share content body";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Compartilhar Usando"));
        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_exit) {
            b = false;
            DialogUtils.createDialogCloseApp(this, "Deseja realmente sair do aplicativo?");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (b)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isAdded(String TAG) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null) {
            if (fragment.isAdded()) {
                return true;
            }
        }
        return false;
    }

}
