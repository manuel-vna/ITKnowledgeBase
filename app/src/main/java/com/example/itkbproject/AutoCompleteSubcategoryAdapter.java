package com.example.itkbproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class AutoCompleteSubcategoryAdapter extends ArrayAdapter<Entry> {
    private List<Entry> allEntrysList;
    private List<Entry> filteredEntrysList;
    private List<String> filteredListOfSubcategories; //used to avoid duplicates in the suggestions array

    public AutoCompleteSubcategoryAdapter(@NonNull Context context, @NonNull List<Entry> entryList) {
        super(context, 0, entryList);

        allEntrysList = new ArrayList<>(entryList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return entryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_suggestions, parent, false
            );
        }


        TextView entryLabel = convertView.findViewById(R.id.textViewArrayItem);

        Entry entry = getItem(position);
        if (entry != null) {
            entryLabel.setText(entry.getSubcategory());
        }

        return convertView;
    }

    private Filter entryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            filteredEntrysList = new ArrayList<>();
            filteredListOfSubcategories = new ArrayList<String>();

            if (constraint == null || constraint.length() == 0) {
                filteredEntrysList.addAll(allEntrysList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Entry entry: allEntrysList) {
                    if (entry.getSubcategory().toLowerCase().contains(filterPattern)) {

                        if (!filteredListOfSubcategories.contains(entry.getSubcategory().toLowerCase())) {
                            filteredEntrysList.add(entry);
                            filteredListOfSubcategories.add(entry.getSubcategory().toLowerCase());
                        }
                    }
                }
            }
            results.values = filteredEntrysList;
            results.count = filteredEntrysList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Entry) resultValue).getSubcategory();
        }
    };
}

