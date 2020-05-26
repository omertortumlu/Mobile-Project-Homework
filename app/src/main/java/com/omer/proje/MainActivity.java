package com.omer.proje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.time.Month;
import android.widget.Toast;
import java.io.Serializable;
import android.widget.CalendarView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
public class MainActivity extends AppCompatActivity {
    private DatePicker datePicker1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.takvim);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ekle:
                        Intent intent = new Intent(getApplicationContext(),EventActivity.class);
                        datePicker1 = findViewById(R.id.datePicker1);
                        int date=datePicker1.getDayOfMonth();
                        int  month=datePicker1.getMonth();
                        int  year=datePicker1.getYear();
                        Bundle extras = new Bundle();
                        extras.putInt("date", date);
                        extras.putInt("month", month);
                        extras.putInt("year", year);
                        intent.putExtras(extras);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.takvim:
                        return true;
                    case R.id.ayarlar:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}
