package com.example.dharmendra.buddy1;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener,
SecondFragment.OnFragmentInteractionListener,ThirdFragment.OnFragmentInteractionListener,FourthFragment.OnFragmentInteractionListener,
FifthFragment.OnFragmentInteractionListener,SixthFragment.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener
,TimeLine.OnFragmentInteractionListener{
    FirebaseAuth firebaseAuth;
    private TextView mTextMessage;
    String user_login;
    DatabaseReference mDatabase1;
    int count;
    Boolean check=false;
    GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    int PLACE_PICKER_REQUEST=1;
    Fragment fragment=null;
    Boolean flag;
    DatabaseReference mDatabase;
    String address;
    public int fset=0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,MainActivity.this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = place.getLatLng().latitude;
                address=place.getAddress().toString();
                longitude =place.getLatLng().longitude;
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference("activity");
                flag=true;
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Activity1 post1 = postSnapshot.getValue(Activity1.class);
                            int status = post1.getStatus();
                            Double lat = post1.getLatitude();
                            if (lat.equals(latitude) && status == 1) {
                                flag = false;
                                Log.d("flag", "fs");
                                break;
                            }
                        }
                        if (flag.equals(true)) {
                            fragment = FifthFragment.newInstance();
                            Bundle bundle = new Bundle();
                            bundle.putString("latitude", String.valueOf(latitude));
                            bundle.putString("longitude", String.valueOf(longitude));
                            bundle.putString("address",address);
                            fragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, fragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Select Another Location", Toast.LENGTH_SHORT).show();
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            try {
                                startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);

                            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Snackbar.make(add_location, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            fset=1;

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            fset=1;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient
                .Builder(MainActivity.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                //.enableAutoManage(MainActivity.this,0)
                .build();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            Toast.makeText(this, "Allow Permission", Toast.LENGTH_SHORT).show();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, TimeLine/**FirstFragment**/.newInstance());
        fragmentTransaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


      /**  AHBottomNavigation navigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.title_maps, R.drawable.maps,R.color.icons);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.title_connection, R.drawable.connection, R.color.icons);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.title_request, R.drawable.request,R.color.icons);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.title_delete, R.drawable.delete, R.color.icons);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.title_add, R.drawable.add, R.color.icons);

        navigation.addItem(item1);
       navigation.addItem(item2);
       navigation.addItem(item3);
        navigation.addItem(item4);
        navigation.addItem(item5);
        navigation.setNotification("1", 2);

        navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = FirstFragment.newInstance();
                        break;
                    case 1:
                        fragment = SecondFragment.newInstance();
                        break;

                    case 2:
                        fragment = ThirdFragment.newInstance();
                        break;

                    case 3:
                        fragment = FourthFragment.newInstance();
                        break;
                    case 4:
                        fragment = FifthFragment.newInstance();
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });**/

        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_maps:
                        fragment = TimeLine/**FirstFragment**/.newInstance();
                        break;
                    case R.id.navigation_connection:
                        fragment = SecondFragment.newInstance();
                        break;

                   /** case R.id.navigation_request:
                        fragment = ThirdFragment.newInstance();
                        break;**/

                    case R.id.navigation_delete:
                        fragment = FourthFragment.newInstance();
                        break;
                    case R.id.navigation_add:

                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);

                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                        /**if(check.equals(true)) {
                            fragment = FifthFragment.newInstance();
                        }**/
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }

        });



    }




}
class BottomNavigationViewHelper {

    static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}

