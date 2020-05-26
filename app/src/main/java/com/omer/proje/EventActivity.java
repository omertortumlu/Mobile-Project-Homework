package com.omer.proje;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.MapFragment;
import android.widget.EditText;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.FragmentTransaction;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class EventActivity extends FragmentActivity implements
        RemindDialog.ReminderDialogListener,
        AdapterView.OnItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        TimePickerDialog.OnTimeSetListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener {

    List<reminder> reminders = new ArrayList<reminder>();
    String[] times = { "Hiçbir Zaman","Her Gün", "Her Hafta", "Her Ay", "Her Yıl"};
    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    private double longitude;
    private double latitude;
    int baslangic=0,bitis=23*60+59;
    AlertReceiver alarm = new AlertReceiver();
    int flag=0;
    int date;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent i=getIntent();
        date=i.getIntExtra("date",0);
        month=i.getIntExtra("month",0)+1;
        year=i.getIntExtra("year",0);
        EditText e=findViewById(R.id.header);
        e.setText("Date:"+date+" Month:"+month+" Year:"+year);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.ekle);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.takvim:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ekle:
                        return true;
                    case R.id.ayarlar:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button addEvent=findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                flag=1;
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        Button finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                flag=2;
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        Button remindButton = (Button) findViewById(R.id.remindButton);
        remindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,times);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setSelection(0);
    }
    public void setAlarm(Context context)
    {

        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlertReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    public void openDialog(){
        RemindDialog exampleDialog=new RemindDialog();
        exampleDialog.show(getSupportFragmentManager(),"Reminder Dialog");
    }
    @Override
    public void applyRemind(reminder r) {
        reminders.add(r);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(flag==1){
            Button start = (Button) findViewById(R.id.start);
            if(hourOfDay*60+minute>bitis){
                Toast.makeText(EventActivity.this, "Bitiş saatinden sonra bir saat giremessiniz", Toast.LENGTH_LONG).show();
                hourOfDay=bitis/60;
                minute=bitis%60;
                start.setText(hourOfDay + ":" + minute);
            }
            else
                baslangic=hourOfDay*60+minute;
                start.setText(hourOfDay + ":" + minute);
        }
        else if(flag==2){
            Button finish = (Button) findViewById(R.id.finish);
            if(hourOfDay*60+minute<baslangic){
                Toast.makeText(EventActivity.this, "Başlangıç saatinden önce bir saat giremessiniz!!!", Toast.LENGTH_LONG).show();
                hourOfDay=baslangic/60;
                minute=baslangic%60;
                finish.setText(hourOfDay + ":" + minute);
            }
            else
                bitis=hourOfDay*60+minute;
                finish.setText(hourOfDay + ":" + minute);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        LatLng turkey=new LatLng(41.015137,28.979530);
        mMap.addMarker(new MarkerOptions()
        .position(turkey)
        .title("Marker in Turkey"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(turkey));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude

            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }
    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        mMap.clear();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Konum"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //move to current position
        moveMap();
    }

}
