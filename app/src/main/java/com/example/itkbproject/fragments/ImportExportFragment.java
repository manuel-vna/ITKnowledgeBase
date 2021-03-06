package com.example.itkbproject.fragments;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.itkbproject.Entry;
import com.example.itkbproject.models.EntryDatabase;
import com.example.itkbproject.R;
import com.example.itkbproject.databinding.ImportExportFragmentBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImportExportFragment extends Fragment {

    private ImportExportFragmentBinding binding;
    EntryDatabase appDb;
    public static String packageName;
    public static String dbName;
    private Integer DbColumnIndex = 0;
    private Integer LastDbId = 0;
    private Scanner inputStream;
    private long inputFileSize;
    private Integer androidVersion;

    private Integer TitleMax = 50;
    private Integer CategoryMax = 20;
    private Integer SubcategoryMax = 20;
    private Integer DescriptionMax = 500;
    private Integer SourceMax = 400;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ImportExportFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();;
        binding=null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDb = EntryDatabase.getInstance(getContext());
        packageName = getContext().getPackageName();
        dbName = EntryDatabase.DB_NAME;
        androidVersion = Build.VERSION.SDK_INT;
        Log.d("Debug_A", "Android Version: "+String.valueOf(androidVersion));

        setHasOptionsMenu(true);
        backToHome();
        showExportFrame();
        exportDb();
        importDb();
        infoBoxes();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuContextSearch:
                Navigation.findNavController(getView()).navigate(R.id.action_importExportFragment_to_contextSearchFragment);
                return true;
            case R.id.menuAdd:
                Navigation.findNavController(getView()).navigate(R.id.action_importExportFragment_to_addEntryFragment);
                return true;
            case R.id.menuImportExport:
                Toast.makeText(getContext(),"Import/Export is already open",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void infoBoxes(){

        binding.ImportExportInfoBoxPrimaryKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),getResources().getString(R.string.ImportExportSwitchPrimaryKeyDescription),
                        Toast.LENGTH_LONG).show();
            }
        });

        binding.ImportExportInfoBoxFilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),getResources().getString(R.string.ImportExportTextFilenameDescription),
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    private void backToHome(){
        binding.ImportExportButtonToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_importExportFragment_to_mainFragment);

            }
        });
    }



    private void showExportFrame() {
        binding.ImportExportButtonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.PopupExportSwitch.setVisibility(View.VISIBLE);
                binding.ImportExportInfoBoxPrimaryKey.setVisibility(View.VISIBLE);
                binding.PopupExportEditTextFilename.setVisibility(View.VISIBLE);
                binding.ImportExportInfoBoxFilename.setVisibility(View.VISIBLE);
                binding.PopupExportButtonStartExport.setVisibility(View.VISIBLE);

            }
        });
    }

    private boolean checkImportPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestImportPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    private boolean checkExportPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return true;
        } else {
            int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestExportPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, 101);
    }


    private void importDb() {

        ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();

                            binding.ImportExportProgressBarImport.setVisibility(View.VISIBLE);

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.submit(new Runnable() {
                                public void run() {

                                    try {
                                        Cursor cursor = appDb.entryDao().getAllEntriesasCursor();
                                        cursor.moveToLast();
                                        DbColumnIndex = cursor.getColumnIndex("id");
                                        LastDbId = cursor.getInt(DbColumnIndex);
                                    }
                                 catch (Exception e) {
                                     Log.i("Debug_A", "Database Status: No entries yet");
                                }

                                    try {
                                            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                                            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

                                            String mLine;
                                            while ((mLine = r.readLine()) != null) {

                                                String[] values = mLine.split(";");

                                                Log.i("Debug_A","Column Number: "+Arrays.asList(values).size());
                                                //checking if there are less than the five necessary columns
                                                if (Arrays.asList(values).size() < 5){
                                                    Log.d("Debug_A", "Not enough columns in line: "+values[0]);
                                                    continue;
                                                }

                                                LastDbId += 1;

                                                //check via ternary operator the length of input and cut it if too long
                                                String title = values[0].length() <= TitleMax ? values[0] : values[0].substring(0, TitleMax);
                                                String category = values[1].length() <= CategoryMax ? values[1] : values[1].substring(0, CategoryMax);
                                                String subcategory = values[2].length() <= SubcategoryMax ? values[2] : values[2].substring(0, SubcategoryMax);
                                                String description = values[3].length() <= DescriptionMax ? values[3] : values[3].substring(0, SubcategoryMax);
                                                String source = values[4].length() <= SourceMax ? values[4] : values[4].substring(0, SourceMax);
                                                Log.i("Debug_A","Value via String[]: "+source);


                                                // creating entry object
                                                Entry entry = new Entry(LastDbId,
                                                        title,
                                                        category,
                                                        String.valueOf(java.time.LocalDate.now()),
                                                        subcategory,
                                                        description,
                                                        source
                                                        );

                                                // inserting entry object into database
                                                appDb.entryDao().insertEntry(entry);

                                            }

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            Toast.makeText(getContext(), "Entries added to database", Toast.LENGTH_SHORT).show();

                            binding.ImportExportProgressBarImport.setVisibility(View.INVISIBLE);

                        }
                    }
                });


        binding.ImportExportButtonImport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //if(checkImportPermission() == false) {
                   //requestImportPermission();
                //}

                Intent data = new Intent(Intent.ACTION_GET_CONTENT);
                data.addCategory(Intent.CATEGORY_OPENABLE);
                String [] mimeTypes = {"text/csv", "text/comma-separated-values"};
                data.setType("*/*");
                data.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                data = Intent.createChooser(data, "Choose a file");
                sActivityResultLauncher.launch(data);
            }
        });

    }



    private void exportDb(){
            binding.PopupExportButtonStartExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkExportPermission() == false) {
                    requestExportPermission();
                }

                binding.PopupExportSwitch.setVisibility(View.GONE);
                binding.ImportExportInfoBoxPrimaryKey.setVisibility(View.GONE);
                binding.PopupExportEditTextFilename.setVisibility(View.GONE);
                binding.ImportExportInfoBoxFilename.setVisibility(View.GONE);
                binding.PopupExportButtonStartExport.setVisibility(View.GONE);

                Toast.makeText(getContext(), "Storing data at: 'storage/emulated/0/Download'", Toast.LENGTH_SHORT).show();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(new Runnable() {
                    public void run() {

                        Cursor cursor = appDb.entryDao().getAllEntriesasCursor();
                        cursor.moveToFirst();

                        String pathname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                        try {
                            File root = new File(pathname);
                            if (!root.exists()) {
                                root.mkdirs();
                            }
                            File gpxfile = new File(root, binding.PopupExportEditTextFilename.getText().toString()+".csv");
                            FileWriter writer = new FileWriter(gpxfile);

                            if (binding.PopupExportSwitch.isChecked()) {
                                while (cursor.isAfterLast() == false) {

                                    writer.append(cursor.getString(cursor.getColumnIndexOrThrow("id")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("title")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("category")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("subcategory")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("description")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("source"))+"\n");
                                    cursor.moveToNext();
                                }
                            }
                            else {
                                while (cursor.isAfterLast() == false) {

                                    //Log.d("Debug_A", "Switch: " + String.valueOf(binding.PopupExportSwitch.isChecked()));

                                    writer.append(cursor.getString(cursor.getColumnIndexOrThrow("title")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("category")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("subcategory")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("description")) + ";"
                                            + cursor.getString(cursor.getColumnIndexOrThrow("source"))+"\n");
                                    cursor.moveToNext();
                                }
                            }

                            writer.flush();
                            writer.close();
                            cursor.close();
                            appDb.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Debug_A", String.valueOf(e));
                        }

                    }

                });
                executor.shutdown();

            }
        });
    }

}
