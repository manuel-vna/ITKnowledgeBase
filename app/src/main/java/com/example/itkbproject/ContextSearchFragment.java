package com.example.itkbproject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.itkbproject.databinding.ContextsearchFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ContextSearchFragment extends Fragment {

    private MainViewModel mViewModel;
    private ContextsearchFragmentBinding binding;
    private EntryAdapter adapter;
    final Calendar myCalendar= Calendar.getInstance();

    public static ContextSearchFragment newInstance() {
        return new ContextSearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ContextsearchFragmentBinding.inflate(inflater,container,false);
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
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        listenerSetup();
        observerSetup();
        recyclerSetup();
        backToHome();

        mViewModel.getEntryLiveData().observe(this, new Observer<List<Entry>>() {

            @Override
            public void onChanged(List<Entry> entryList) {

                AutoCompleteEntryAdapter adapter = new AutoCompleteEntryAdapter(getContext(), entryList);
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.ContextSearchCategoryEditText;
                adapter.getFilter().filter(autoCompleteTextView.getText().toString());
                autoCompleteTextView.setAdapter(adapter);
            }
        });
    }

    private void clearFields(String spinner_selection){
        switch(spinner_selection) {
            case "Title": binding.ContextSearchTitleEditText.setText("");
                break;
            case "Category": binding.ContextSearchCategoryEditText.setText("");
                break;
            case "Subcategory": binding.ContextSearchSubcategoryEditText.setText("");
                break;
            case "Description": binding.ContextSearchDescriptionEditText.setText("");
                break;
        }
    }


    private void listenerSetup(){

        Spinner spinner = binding.ContextSearchSpinnerSections;
        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.sections_array));
        adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterArray);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                //Log.d("Debug_A", String.valueOf(LocalDate.now()));

                if (spinner.getSelectedItem().toString().equals("Title")) {
                    binding.ContextSearchTitleEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);

                }
                else if (spinner.getSelectedItem().toString().equals("Category")) {
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);

                }
                else if (spinner.getSelectedItem().toString().equals("Subcategory")){
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);
                }
                else if (spinner.getSelectedItem().toString().equals("Description")){
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.VISIBLE);
                }
                else if (spinner.getSelectedItem().toString().equals("Date")){
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);
                    binding.ContextSearchEditTextDateFrom.setVisibility(View.VISIBLE);
                    binding.ContextSearchEditTextDateTo.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }



        });

        // Click Listener
        binding.ContextSearchTitleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findCategory(binding.ContextSearchTitleEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchCategoryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    mViewModel.findTitle(binding.ContextSearchCategoryEditText.getText().toString());
                    closeKeyboard();
                    return true;
            }
        });

        binding.ContextSearchSubcategoryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findTitle(binding.ContextSearchSubcategoryEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchDescriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findTitle(binding.ContextSearchDescriptionEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchEditTextDateFrom.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findTitle(binding.ContextSearchEditTextDateFrom.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchEditTextDateTo.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findTitle(binding.ContextSearchEditTextDateTo.getText().toString());
                closeKeyboard();
                return true;
            }
        });


        // Butto Listener

        binding.ContextSearchButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinner.getSelectedItem().toString().equals("Title")) {
                    mViewModel.findTitle(binding.ContextSearchTitleEditText.getText().toString());
                    clearFields("Title");
                }
                else if (spinner.getSelectedItem().toString().equals("Category")) {
                    mViewModel.findCategory(binding.ContextSearchCategoryEditText.getText().toString());
                    clearFields("Category");
                }
                else if(spinner.getSelectedItem().toString().equals("Subcategory")) {
                    mViewModel.findSubcategory(binding.ContextSearchSubcategoryEditText.getText().toString());
                    clearFields("Subcategory");
                }
                else if(spinner.getSelectedItem().toString().equals("Description")) {
                    mViewModel.findDescription(binding.ContextSearchDescriptionEditText.getText().toString());
                    clearFields("Description");
                }
                else if(spinner.getSelectedItem().toString().equals("Date")) {

                    /*
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, month);
                            myCalendar.set(Calendar.DAY_OF_MONTH, day);

                            String myFormat="MM/dd/yy";
                            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                            binding.ContextSearchEditTextDateFrom.setText(dateFormat.format(myCalendar.getTime()));
                        }
                    };
                    binding.ContextSearchEditTextDateFrom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                     */

                    mViewModel.findDate(binding.ContextSearchEditTextDateFrom.getText().toString(),
                            binding.ContextSearchEditTextDateTo.getText().toString());

                }
                closeKeyboard();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuContextSearch:
                Toast.makeText(getContext(),"ContextSearch is already open",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuAdd:
                Navigation.findNavController(getView()).navigate(R.id.action_contextSearchFragment_to_addEntryFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void observerSetup(){

        MainViewModel.getSearchResults().observe(getViewLifecycleOwner(),
                new Observer<List<Entry>>() {
                    @Override
                    public void onChanged(@Nullable final List<Entry> entries) {

                        //output entries in recyclerview
                        adapter.setEntryList(entries);

                        //output number of entry hits
                        if (entries.size() > 0) {
                            binding.ContextSearchTextViewNoOfHits.setVisibility(View.VISIBLE);
                            binding.ContextSearchTextViewNoOfHits.setText(String.valueOf(entries.size()));
                        }
                        else {
                            binding.ContextSearchTextViewNoOfHits.setText("0");
                        }
                    }
                });
    }


    private void recyclerSetup(){
        adapter = new EntryAdapter(R.layout.entry_recyclerview_items);
        binding.ContextSearchRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.ContextSearchRecyclerView.setAdapter(adapter);
    }

    private void backToHome(){
        binding.ContextSearchButtonToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_contextSearchFragment_to_mainFragment);
            }
        });
    }

    private void closeKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}