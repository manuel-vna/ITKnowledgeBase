package com.example.itkbproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itkbproject.Entry;
import com.example.itkbproject.models.EntryDatabase;
import com.example.itkbproject.R;
import com.example.itkbproject.databinding.UpdateDeleteEntryBinding;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UpdateDeleteEntryActivity extends AppCompatActivity {

    EntryDatabase appDb;
    private UpdateDeleteEntryBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UpdateDeleteEntryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final Entry entry = (Entry) getIntent().getSerializableExtra("entry");
        appDb = EntryDatabase.getInstance(this);
        loadEntry(entry);


        findViewById(R.id.UpdateDeleteButtonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Entry Updated", Toast.LENGTH_LONG).show();
                updateEntry(entry);
            }
        });

        findViewById(R.id.UpdateDeleteButtonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Entry Deleted", Toast.LENGTH_LONG).show();
                deleteEntry(entry);
            }
        });

    }


    private void loadEntry(Entry entry) {
        binding.UpdateDeleteEditTextTitle.setText(entry.getTitle());
        binding.UpdateDeleteEditTextCategory.setText(entry.getCategory());
        binding.UpdateDeleteEditTextDescription.setText(entry.getDescription());
        binding.UpdateDeleteEditTextSubcategory.setText(entry.getSubcategory());
        binding.UpdateDeleteEditTextSource.setText(entry.getSource());

    }

    private void updateEntry(final Entry entry) {

        final String title = binding.UpdateDeleteEditTextTitle.getText().toString().trim();
        final String category = binding.UpdateDeleteEditTextCategory.getText().toString().trim();
        final String description = binding.UpdateDeleteEditTextDescription.getText().toString().trim();
        final String subcategory = binding.UpdateDeleteEditTextSubcategory.getText().toString().trim();
        final String source = binding.UpdateDeleteEditTextSource.getText().toString().trim();

        if (title.isEmpty()) {
            binding.UpdateDeleteEditTextTitle.setError("Entry required");
            binding.UpdateDeleteEditTextTitle.requestFocus();
            return;
        }

        if (category.isEmpty()) {
            binding.UpdateDeleteEditTextCategory.setError("Category required");
            binding.UpdateDeleteEditTextCategory.requestFocus();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
                public void run () {
                    entry.setTitle(title);
                    entry.setCategory(category);
                    entry.setSubcategory(subcategory);
                    entry.setDescription(description);
                    entry.setSource(source);
                    appDb.entryDao().updateEntry(entry);
                    startActivity(new Intent(UpdateDeleteEntryActivity.this, MainActivity.class));
                }
            });
        executor.shutdown();

    }


    private void deleteEntry(final Entry entry) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            public void run () {
                appDb.entryDao().deleteEntry(entry);
                startActivity(new Intent(UpdateDeleteEntryActivity.this, MainActivity.class));
            }
        });
        executor.shutdown();
    }

}

