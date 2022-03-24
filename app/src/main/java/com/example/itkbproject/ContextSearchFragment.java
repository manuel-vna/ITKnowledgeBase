package com.example.itkbproject;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.itkbproject.databinding.ContextsearchFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class ContextSearchFragment extends Fragment {

    private MainViewModel mViewModel;
    private ContextsearchFragmentBinding binding;
    private EntryAdapter adapter;
    private EntryAdapter adapterCategory;
    private EntryAdapter adapterSubcategory;
    private Calendar dateFromCalendar;
    public FragmentActivity c;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        listenerSetup();
        observerSetup();
        recyclerSetup();
        backToHome();

        mViewModel.getEntryLiveData().observe(this, new Observer<List<Entry>>() {

            @Override
            public void onChanged(List<Entry> entryList) {

                AutoCompleteCategoryAdapter adapterCategory = new AutoCompleteCategoryAdapter(getContext(), entryList);
                AutoCompleteTextView autoCompleteTextViewCategory = (AutoCompleteTextView) binding.ContextSearchCategoryEditText;
                adapterCategory.getFilter().filter(autoCompleteTextViewCategory.getText().toString());
                autoCompleteTextViewCategory.setThreshold(3);
                autoCompleteTextViewCategory.setAdapter(adapterCategory);

                AutoCompleteSubcategoryAdapter adapterSubcategory = new AutoCompleteSubcategoryAdapter(getContext(), entryList);
                AutoCompleteTextView autoCompleteTextViewSubcategory = (AutoCompleteTextView) binding.ContextSearchSubcategoryEditText;
                adapterSubcategory.getFilter().filter(autoCompleteTextViewSubcategory.getText().toString());
                autoCompleteTextViewSubcategory.setThreshold(3);
                autoCompleteTextViewSubcategory.setAdapter(adapterSubcategory);
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

                //TBD: Date
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
                    binding.ContextSearchPickerDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.GONE);

                }
                else if (spinner.getSelectedItem().toString().equals("Category")) {
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.GONE);

                }
                else if (spinner.getSelectedItem().toString().equals("Subcategory")){
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.GONE);

                }
                else if (spinner.getSelectedItem().toString().equals("Description")){
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.VISIBLE);
                    binding.ContextSearchPickerDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.GONE);
                }

                else if (spinner.getSelectedItem().toString().equals("Date")) {
                    binding.ContextSearchTitleEditText.setVisibility(View.GONE);
                    binding.ContextSearchCategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchSubcategoryEditText.setVisibility(View.GONE);
                    binding.ContextSearchDescriptionEditText.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateFrom.setVisibility(View.VISIBLE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.VISIBLE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.VISIBLE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.VISIBLE);
                    binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });


        // Click Listener when pressing enter in search field
        binding.ContextSearchTitleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findTitle(binding.ContextSearchTitleEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchCategoryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findCategory(binding.ContextSearchCategoryEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchSubcategoryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findSubcategory(binding.ContextSearchSubcategoryEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });

        binding.ContextSearchDescriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mViewModel.findDescription(binding.ContextSearchDescriptionEditText.getText().toString());
                closeKeyboard();
                return true;
            }
        });


        // Search Button Listener

        binding.ContextSearchButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinner.getSelectedItem().toString().equals("Title")) {
                    mViewModel.findTitle(binding.ContextSearchTitleEditText.getText().toString());
                    clearFields("Title");
                } else if (spinner.getSelectedItem().toString().equals("Category")) {
                    mViewModel.findCategory(binding.ContextSearchCategoryEditText.getText().toString());
                    clearFields("Category");
                } else if (spinner.getSelectedItem().toString().equals("Subcategory")) {
                    mViewModel.findSubcategory(binding.ContextSearchSubcategoryEditText.getText().toString());
                    clearFields("Subcategory");
                } else if (spinner.getSelectedItem().toString().equals("Description")) {
                    mViewModel.findDescription(binding.ContextSearchDescriptionEditText.getText().toString());
                    clearFields("Description");
                }
                else if(spinner.getSelectedItem().toString().equals("Date")) {

                    binding.ContextSearchPickerDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchPickerDateTo.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateFrom.setVisibility(View.GONE);
                    binding.ContextSearchTextViewDateTo.setVisibility(View.GONE);

                    //get dates from calender
                    Calendar dateFromCalendar = Calendar.getInstance();
                    Calendar dateToCalendar = Calendar.getInstance();

                    dateFromCalendar.set(binding.ContextSearchPickerDateFrom.getYear(),binding.ContextSearchPickerDateFrom.getMonth(),
                            binding.ContextSearchPickerDateFrom.getDayOfMonth());
                    SimpleDateFormat formatFromDate = new SimpleDateFormat("yyyy-MM-dd");
                    String FromDate = formatFromDate.format(dateFromCalendar.getTime());

                    dateToCalendar.set(binding.ContextSearchPickerDateTo.getYear(),binding.ContextSearchPickerDateTo.getMonth(),
                            binding.ContextSearchPickerDateTo.getDayOfMonth());
                    SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-MM-dd");
                    String ToDate = formatToDate.format(dateToCalendar.getTime());

                    binding.ContextSearchTextViewDateChosenTimespan.setText(FromDate+" until "+ToDate);

                    mViewModel.findDate(FromDate,ToDate);

                }
                else {
                    Toast.makeText(getContext(), "Selection didn't work",
                            Toast.LENGTH_LONG).show();
                }

                closeKeyboard();
            }
        });

        binding.ContextSearchButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.ContextSearchTextViewNoOfHits.setVisibility(View.GONE);
                binding.ContextSearchButtonClear.setVisibility(View.GONE);
                binding.ContextSearchTextViewDateChosenTimespan.setVisibility(View.GONE);
                adapter.setEntryList(Collections.<Entry>emptyList());
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
            case R.id.menuImportExport:
                Navigation.findNavController(getView()).navigate(R.id.action_contextSearchFragment_to_importExportFragment);
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
                            binding.ContextSearchButtonClear.setVisibility(View.VISIBLE);
                            binding.ContextSearchTextViewNoOfHits.setText(String.valueOf(entries.size()));
                        }
                        else {
                            binding.ContextSearchTextViewNoOfHits.setText("0");
                        }
                    }
                });
    }


    private void recyclerSetup(){
        c = getActivity();
        //adapter = new EntryAdapter(c,R.layout.entry_recyclerview_items);
        adapter = new EntryAdapter(getContext(),c,R.layout.entry_recyclerview_items);
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

    private void closeKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.d("Debug_A", String.valueOf("Keyboard closing Error: No keyboard was present on screen"));
        }
    }

}