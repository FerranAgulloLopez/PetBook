package com.example.PETBook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.PETBook.Fragments.ForumFragment;
import com.example.PETBook.Fragments.HomeFragment;
import com.example.PETBook.Fragments.MyCalendarFragment;
import com.example.PETBook.Fragments.MyEventsFragment;
import com.example.PETBook.Fragments.MyFriendsFragment;
import com.example.PETBook.Fragments.MyMapFragment;
import com.example.PETBook.Fragments.MyPetsFragment;
import com.example.PETBook.Fragments.MyPostsFragment;
import com.example.PETBook.Fragments.MyProfileFragment;
import com.example.pantallafirstview.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, MyCalendarFragment.OnFragmentInteractionListener
                            , MyEventsFragment.OnFragmentInteractionListener, MyPetsFragment.OnFragmentInteractionListener, MyPostsFragment.OnFragmentInteractionListener
                            , MyProfileFragment.OnFragmentInteractionListener, MyFriendsFragment.OnFragmentInteractionListener, ForumFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Mi perfil");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        // First fragment to display (de momento HomeFragment -perfil-)
        Fragment fragment = null;
        Bundle recibir = this.getIntent().getExtras();
        if (recibir != null){
            String fragType = recibir.getString("fragment");
            if(fragType.equals("events")) {
                fragment = new MyEventsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if(fragType.equals("pets")) {
                fragment = new MyPetsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if(fragType.equals("profile")){
                fragment = new MyProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if(fragType.equals("friends")){
                fragment = new MyFriendsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if(fragType.equals("posts")){
                fragment = new MyPostsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if (fragType.equals("calendar")){
                fragment = new MyCalendarFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if (fragType.equals("forum")){
                fragment = new ForumFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else if (fragType.equals("mapa")){
                fragment = new MyMapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
            else {
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
        }
        else{
            fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_main, fragment).commit();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment myFragment=null;
        boolean fragmentSeleccionado=false;

        if (id == R.id.nav_home) {
            myFragment = new HomeFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_pets) {
            myFragment = new MyPetsFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_profile) {
            myFragment = new MyProfileFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_friends) {
            myFragment = new MyFriendsFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_events) {
            myFragment = new MyEventsFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_calendar) {
            myFragment = new MyCalendarFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_forum) {
            myFragment = new ForumFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_map){
            myFragment = new MyMapFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_logout) {
            //salir a la ventana de log in
            AlertDialog.Builder exit = new AlertDialog.Builder(MainActivity.this);
            exit.setMessage("¿Esta seguro de querer salir de la aplicación?")
                    .setCancelable(false)
                    .setPositiveButton("Si",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){

                            /*
                            remove info of user logged in
                             */
                            SharedPreferences sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("login");
                            editor.commit();

                            finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });

            AlertDialog cerrar = exit.create();
            cerrar.setTitle("Salida");
            cerrar.show();
        }


        if (fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
