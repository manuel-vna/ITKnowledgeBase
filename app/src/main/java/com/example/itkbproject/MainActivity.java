package com.example.itkbproject;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AutoCompleteTextView;



public class MainActivity extends AppCompatActivity { //implements SubcategorySearchFragment.OnFragmentInteractionListener {

    EntryDatabase appDb;
    AutoCompleteTextView autoCompleteTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        /* //spinner:
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getEntryLiveData().observe(this, new Observer<List<Entry>>() {

            @Override
            public void onChanged(List<Entry> entryList) {


                AutoCompleteEntryAdapter adapter = new AutoCompleteEntryAdapter(MainActivity.this, entryList);
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) MainActivity.this.findViewById(
                        R.id.autoCompleteTextViewSearch
                );
                adapter.getFilter().filter(autoCompleteTextView.getText().toString());
                autoCompleteTextView.setAdapter(adapter);

            }
        });**/
    }


    //@Override
    //public void onFragmentInteraction(Uri uri) {
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}