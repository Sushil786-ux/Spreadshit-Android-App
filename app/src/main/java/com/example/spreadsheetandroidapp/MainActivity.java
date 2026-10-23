package com.example.spreadsheetandroidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SheetAdapter adapter;
    private List<SheetItem> sheetItemList;
    private static final String SHEET_CSV_URL = "https://docs.google.com/spreadsheets/d/1-wZJ9Xe_zevj-zF7vEaflnAxzUjN_HyIlutYoHi9sAk/export?format=csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sheetItemList = new ArrayList<>();

        fetchGoogleSheetData();
    }

    private void fetchGoogleSheetData() {
        progressBar.setVisibility(View.VISIBLE);

        // Background thread using ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<SheetItem> fetchedData = new ArrayList<>();
            try {
                URL url = new URL(SHEET_CSV_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                boolean isFirstRow = true; // Optional: skip header row if desired

                while ((line = reader.readLine()) != null) {
                    if (isFirstRow) {
                        isFirstRow = false;
                        continue; // Skip header column titles
                    }

                    String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Regex to handle commas inside quotes safely

                    if (columns.length >= 3) {
                        String col1 = columns[0].replace("\"", "").trim();
                        String col2 = columns[1].replace("\"", "").trim();
                        String col3 = columns[2].replace("\"", "").trim();
                        fetchedData.add(new SheetItem(col1, col2, col3));
                    }
                }
                reader.close();

                // Update UI on Main Thread
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    sheetItemList.clear();
                    sheetItemList.addAll(fetchedData);
                    adapter = new SheetAdapter(sheetItemList);
                    recyclerView.setAdapter(adapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Failed to load Google Sheet data", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}