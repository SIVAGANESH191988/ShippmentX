package com.example.todolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText taskInput;
    private Button addButton;
    private RecyclerView taskList;
    private List<String> tasks;
    private TaskAdapter taskAdapter;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private static final int LOCATION_PERMISSION_CODE = 102;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createNotificationChannel(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        taskInput = findViewById(R.id.taskInput);
        addButton = findViewById(R.id.addButton);
        taskList = findViewById(R.id.taskList);

        tasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasks, this);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(taskAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    taskAdapter.notifyItemInserted(tasks.size() - 1);
                    taskInput.setText("");
                    fetchLocationAndNotify("Task Added", "You added: " + task);
                }
            }
        });
    }

    private void fetchLocationAndNotify(String title, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        String locationMessage = message + "\nLocation: " + location.getLatitude() + ", " + location.getLongitude();
                        NotificationHelper.showNotification(MainActivity.this, title, locationMessage);
                    } else {
                        NotificationHelper.showNotification(MainActivity.this, title, message + "\nLocation not available");
                    }
                }
            });
        } else {
            NotificationHelper.showNotification(MainActivity.this, title, message + "\nLocation permission denied");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
