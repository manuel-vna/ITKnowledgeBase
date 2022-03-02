package com.example.itkbproject;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.itkbproject.databinding.MainFragmentBinding;

import java.util.List;


public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private EntryAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ;
        binding = null;
    }

    public interface OnFragmentInteractionListener {
        //TBD Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        listenerSetup();
        observerSetup();
        recyclerSetup();

    }

    private void listenerSetup(){

        binding.MainSearchButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.findKeyword(binding.MainFragmentKeywordSearch.getText().toString());
                closeKeyboard();
                clearFields();
            }
        });

        binding.MainAddEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addEntryFragment);
            }
        });

    }

    private void observerSetup() {

        MainViewModel.getSearchResults().observe(getViewLifecycleOwner(),
                new Observer<List<Entry>>() {
                    @Override
                    public void onChanged(@Nullable final List<Entry> entries) {

                        //output entries in recyclerview
                        adapter.setEntryList(entries);

                        //output number of entry hits
                        if (entries.size() > 0) {
                            binding.MainTextViewNoOfHits.setVisibility(View.VISIBLE);
                            binding.MainTextViewNoOfHits.setText(String.valueOf(entries.size()));
                        }
                        else {
                            binding.MainTextViewNoOfHits.setText("0");
                        }
                    }
                });
    }

    private void recyclerSetup(){
        adapter = new EntryAdapter(R.layout.entry_recyclerview_items);
        binding.MainFragmentRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.MainFragmentRecyclerView.setAdapter(adapter);
    }

    private void closeKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void clearFields(){
        binding.MainFragmentKeywordSearch.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuContextSearch:
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_contextSearchFragment);
                return true;
            case R.id.menuAdd:
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_addEntryFragment2);
                return true;
            case R.id.menuImportExport:
                Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_importExportFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

