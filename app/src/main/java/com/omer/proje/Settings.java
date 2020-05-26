package com.omer.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Settings extends FragmentActivity implements AdapterView.OnItemSelectedListener{
    String[] times = { "5 dk","10 dk", "30 dk", "1 saat", "2 saat","3 saat"};
    String[] times2 = { "5 dk aralıkla","15 dk aralıkla", "25 dk aralıkla", "1 saat aralıkla", "2 saat aralıkla"};
    Bundle bundle;
    Intent intent2;
    Boolean flag;
    private Switch drk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
            flag=true;
        } else {
            setTheme(R.style.LightTheme);
            flag=false;
        }

        setContentView(R.layout.activity_settings);
        intent2=getIntent();
        bundle = intent2.getExtras();
        drk=findViewById(R.id.switch1);
        drk.setChecked(flag);

        Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
        spin2.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,times);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(aa);
        spin2.setSelection(0);

        Spinner spin3 = (Spinner) findViewById(R.id.spinner3);
        spin3.setOnItemSelectedListener(this);
        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,times2);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(aa1);
        spin3.setSelection(0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.ayarlar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.takvim:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ekle:
                        startActivity(new Intent(getApplicationContext(),EventActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ayarlar:
                        return true;
                }
                return false;
            }
        });

        drk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                chng();
            }
        });
    }
    public void chng(){
        Intent intent=new Intent(Settings.this, Settings.this.getClass());
        bundle = new Bundle();
        bundle.putBoolean("swc",drk.isChecked());
        intent.putExtras(bundle);
        finish();
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
