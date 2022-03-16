package com.example.itkbproject;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.itkbproject.databinding.AddEntryBinding;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class AddEntryFragment extends Fragment {

    private AddEntryBinding binding;
    EntryDatabase appDb;
    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = AddEntryBinding.inflate(inflater,container,false);
        return binding.getRoot();


    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();;
        binding=null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        appDb = EntryDatabase.getInstance(getContext());

        binding.buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_addEntryFragment_to_mainFragment);

            }
        });

        binding.buttonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntry();
            }
        });

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getEntryLiveData().observe(this, new Observer<List<Entry>>() {

            @Override
            public void onChanged(List<Entry> entryList) {

                AutoCompleteCategoryAdapter adapter = new AutoCompleteCategoryAdapter(getContext(), entryList);
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.autoCompleteTextViewCategoryAdd;
                adapter.getFilter().filter(autoCompleteTextView.getText().toString());
                autoCompleteTextView.setAdapter(adapter);
            }
        });


        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {

                    Log.d("Debug_A", "AddEntryFragment-Filter: "+String.valueOf(source.charAt(i)));
                    if(String.valueOf(source.charAt(i)).equals(";")){
                        Toast toast = Toast.makeText(getContext(), "Invalid Input Character", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return "";
                    }
                }
                return null;
            }
        };

        binding.autoCompleteTextViewTitleAdd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50),filter });
        binding.autoCompleteTextViewCategoryAdd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20),filter });
        binding.editTextDescriptionAdd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500),filter });
        binding.autoCompleteTextViewSubcategoryAdd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20),filter });
        binding.editTextSourceAdd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(400),filter });

    }


    Handler handler = new Handler(Looper.getMainLooper()){
        @Override public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            String output = bundle.getString("Key1");
            Toast.makeText(getContext(),output,
                    Toast.LENGTH_LONG).show();
        }
    };

    private void saveEntry() {

        //decided against escape option: DatabaseUtils.sqlEscapeString()
        final String title = binding.autoCompleteTextViewTitleAdd.getText().toString().trim();
        final String category = binding.autoCompleteTextViewCategoryAdd.getText().toString().trim();
        final String description = binding.editTextDescriptionAdd.getText().toString().trim();
        final String subcategory = binding.autoCompleteTextViewSubcategoryAdd.getText().toString().trim();
        final String source = binding.editTextSourceAdd.getText().toString().trim();
        final String date = String.valueOf(java.time.LocalDate.now());


        if (title.isEmpty()) {
            binding.autoCompleteTextViewTitleAdd.setError("Title required");
            binding.autoCompleteTextViewTitleAdd.requestFocus();
            return;
        }

        if (category.isEmpty()) {
            binding.autoCompleteTextViewCategoryAdd.setError("Category required");
            binding.autoCompleteTextViewCategoryAdd.requestFocus();
            return;
        }


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable(){
            public void run() {

                Entry entry = new Entry(0,title,category,date,subcategory,description,source);
                appDb.entryDao().insertEntry(entry);

                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("Key1","Title saved: "+title);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        executor.shutdown();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuContextSearch:
                Navigation.findNavController(getView()).navigate(R.id.action_addEntryFragment_to_contextSearchFragment);
                return true;
            case R.id.menuAdd:
                Toast.makeText(getContext(),"You are already at page 'Add Entry'",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

