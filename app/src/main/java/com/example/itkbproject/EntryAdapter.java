package com.example.itkbproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private final int entryAdapterLayout;
    private Context mCtx;
    private List<Entry> entryList;


    public EntryAdapter(int layoutId){
        entryAdapterLayout = layoutId;
    }

    public void setEntryList(List<Entry> entries) {
        entryList = entries;
        notifyDataSetChanged();
    }


        @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        //LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.entry_items, null);
        //return new EntryViewHolder(view);
            View view = LayoutInflater.from(
                    parent.getContext()).inflate(entryAdapterLayout, parent, false);
            return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        //getting the entry of the specified position
        Entry entry = entryList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(entry.getTitle());
        holder.textViewCategory.setText(entry.getCategory());
        holder.textViewDate.setText(String.valueOf(entry.getDate()));
        holder.textViewSubcategory.setText(String.valueOf(entry.getSubcategory()));
        holder.textViewDescription.setText(String.valueOf(entry.getDescription()));
        holder.textViewSource.setText(String.valueOf(entry.getSource()));

    }


    @Override
    public int getItemCount(){
        return entryList == null ? 0 : entryList.size();
    }


    class EntryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewCategory, textViewDate, textViewSubcategory, textViewDescription, textViewSource;

        public EntryViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSubcategory = itemView.findViewById(R.id.textViewSubcategory);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewSource = itemView.findViewById(R.id.textViewSource);
        }
    }
}
