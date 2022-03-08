package com.example.itkbproject;


import static java.lang.Math.toIntExact;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.itkbproject.databinding.ImportExportFragmentBinding;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ImportExportFragment extends Fragment {

    private ImportExportFragmentBinding binding;
    EntryDatabase appDb;
    private String firstColumn;
    public static String packageName;
    public static String dbName;
    private Integer LastDbId = 0;
    private Scanner inputStream;
    private Long inputFileSize;
    private ProgressBar progressBarImport;
    private int progressStatusImport = 0;
    private TextView ViewProgressImport;
    private Double ImportProgressThreshold;
    private Integer androidVersion;

    public static final int PICKFILE_RESULT_CODE = 1;
    private TextView tvItemPath;
    private Uri uri;
    private String filePath;



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

                binding.ImportExportRadioGroupExportType.setVisibility(View.VISIBLE);
                binding.PopupExportSwitch.setVisibility(View.VISIBLE);
                binding.ImportExportInfoBoxPrimaryKey.setVisibility(View.VISIBLE);
                binding.PopupExportEditTextFilename.setVisibility(View.VISIBLE);
                binding.ImportExportInfoBoxFilename.setVisibility(View.VISIBLE);
                binding.PopupExportButtonStartExport.setVisibility(View.VISIBLE);

            }
        });
    }


    private void permission(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 101);
        }
        else {
            Toast.makeText(getContext(), "Permission 'Read Storage' granted", Toast.LENGTH_SHORT).show();
        }
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
                            File file= new File(uri.getPath());


                            Log.d("Debug_A", String.valueOf(file.lastModified()));

                        }
                    }
                });

                            /*

                            List<List<String>> lines = new ArrayList<>();

                            inputFileSize = file.length();
                            Log.d("Debug_A", "inputFileSize: "+String.valueOf(inputFileSize));

                            progressBarImport.setMax(toIntExact(inputFileSize));


                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.submit(new Runnable() {
                                public void run() {


                                    Cursor cursor = appDb.entryDao().getAllEntriesasCursor();
                                    cursor.moveToLast();
                                    LastDbId = cursor.getCount();
                                    //Log.d("Debug_A", "Database Last Line: "+String.valueOf(cursor.getCount()));

                                    try{
                                        inputStream = new Scanner(file);

                                        while(inputStream.hasNext()){
                                            String line= inputStream.next();
                                            String[] values = line.split(";");
                                            lines.add(Arrays.asList(values));


                                            //progressBar
                                            progressStatusImport += line.getBytes().length; //toIntExact(line.getBytes().length);
                                            progressBarImport.setProgress(toIntExact(line.getBytes().length));
                                            ViewProgressImport.setText(progressStatusImport+"/"+progressBarImport.getMax()+" Bytes");
                                            ImportProgressThreshold = inputFileSize-((inputFileSize/100.0)*10); // Invisible-Threshold: 90% of max
                                            if (progressStatusImport >= ImportProgressThreshold){
                                                binding.ImportExportProgressBarImport.setVisibility(View.INVISIBLE);
                                                binding.ImportExportProgressTextViewImport.setVisibility(View.INVISIBLE);
                                            }

                                            LastDbId += 1;

                                            if (Arrays.asList(values).size() < 5){
                                                Log.d("Debug_A", "Not enough values in line "+Arrays.asList(values).get(0));
                                                continue;
                                            }


                                            Entry entry = new Entry(LastDbId,
                                                    Arrays.asList(values).get(0),
                                                    Arrays.asList(values).get(1),
                                                    null,
                                                    Arrays.asList(values).get(2),
                                                    Arrays.asList(values).get(3),
                                                    Arrays.asList(values).get(4));

                                            appDb.entryDao().insertEntry(entry);
                                            //Log.d("Debug_A", String.valueOf(entry));

                                            try {
                                                Thread.sleep(2100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        inputStream.close();
                                    }
                                    catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                        Log.d("Debug_A", String.valueOf(e));

                                    }
                                }
                            });

                        }
                    }
                });


      */


        binding.ImportExportButtonImport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                Intent data = new Intent(Intent.ACTION_GET_CONTENT);
                data.addCategory(Intent.CATEGORY_OPENABLE);
                data.setType("*/*");
                data = Intent.createChooser(data, "Choose a file");
                sActivityResultLauncher.launch(data);
            }
        });


    }



                 /*

                binding.ImportExportProgressBarImport.setVisibility(View.VISIBLE);
                binding.ImportExportProgressTextViewImport.setVisibility(View.VISIBLE);

                progressBarImport = binding.ImportExportProgressBarImport;
                ViewProgressImport = binding.ImportExportProgressTextViewImport;

                //String pathname = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                //pathname += "/db-export3.csv";

                //File file= new File(pathname);
                File file= new File(uri.getPath());


                List<List<String>> lines = new ArrayList<>();

                inputFileSize = file.length();
                Log.d("Debug_A", "inputFileSize: "+String.valueOf(inputFileSize));

                progressBarImport.setMax(toIntExact(inputFileSize));


                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(new Runnable() {
                    public void run() {


                        Cursor cursor = appDb.entryDao().getAllEntriesasCursor();
                        cursor.moveToLast();
                        LastDbId = cursor.getCount();
                        //Log.d("Debug_A", "Database Last Line: "+String.valueOf(cursor.getCount()));

                        try{
                            inputStream = new Scanner(file);

                            while(inputStream.hasNext()){
                                String line= inputStream.next();
                                String[] values = line.split(";");
                                lines.add(Arrays.asList(values));


                                //progressBar
                                progressStatusImport += line.getBytes().length; //toIntExact(line.getBytes().length);
                                progressBarImport.setProgress(toIntExact(line.getBytes().length));
                                ViewProgressImport.setText(progressStatusImport+"/"+progressBarImport.getMax()+" Bytes");
                                ImportProgressThreshold = inputFileSize-((inputFileSize/100.0)*10); // Invisible-Threshold: 90% of max
                                if (progressStatusImport >= ImportProgressThreshold){
                                    binding.ImportExportProgressBarImport.setVisibility(View.INVISIBLE);
                                    binding.ImportExportProgressTextViewImport.setVisibility(View.INVISIBLE);
                                }

                                LastDbId += 1;

                                if (Arrays.asList(values).size() < 5){
                                    Log.d("Debug_A", "Not enough values in line "+Arrays.asList(values).get(0));
                                    continue;
                                }


                                Entry entry = new Entry(LastDbId,
                                        Arrays.asList(values).get(0),
                                        Arrays.asList(values).get(1),
                                        null,
                                        Arrays.asList(values).get(2),
                                        Arrays.asList(values).get(3),
                                        Arrays.asList(values).get(4));

                                appDb.entryDao().insertEntry(entry);
                                //Log.d("Debug_A", String.valueOf(entry));

                                try {
                                    Thread.sleep(2100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }

                            inputStream.close();
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("Debug_A", String.valueOf(e));

                        }
                    }
                });
            }
        });
    }


 */




    private void exportDb(){
            binding.PopupExportButtonStartExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permission();

                binding.ImportExportRadioGroupExportType.setVisibility(View.GONE);
                binding.PopupExportSwitch.setVisibility(View.GONE);
                binding.ImportExportInfoBoxPrimaryKey.setVisibility(View.GONE);
                binding.PopupExportEditTextFilename.setVisibility(View.GONE);
                binding.ImportExportInfoBoxFilename.setVisibility(View.GONE);
                binding.PopupExportButtonStartExport.setVisibility(View.GONE);

                Toast.makeText(getContext(), "Storing data at: 'storage/emulated/0/Download'", Toast.LENGTH_SHORT).show();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(new Runnable() {
                    public void run() {


                        if (binding.ImportExportRadioButtonCSV.isChecked()) {

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

                                        //Log.d("Debug_A", StringEscapeUtils.escapeJson(cursor.getString(cursor.getColumnIndexOrThrow("description"))));
                                        //Log.d("Debug_A", cursor.getString(cursor.getColumnIndexOrThrow("description")));

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

                        if (binding.ImportExportRadioButtonDB.isChecked()) {

                            //TBD
                        }

                    }
                });
                executor.shutdown();

            }
        });
    }
    

}
