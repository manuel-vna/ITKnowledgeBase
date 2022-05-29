package com.example.itkbproject.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itkbproject.Entry;
import com.example.itkbproject.R;
import com.example.itkbproject.activities.UpdateDeleteEntryActivity;

import java.io.Serializable;
import java.util.List;


public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {


    private final int entryAdapterLayout;
    private List<Entry> entryList;
    private FragmentActivity c;

    Context mContext;

    public EntryAdapter(Context context, FragmentActivity c, int layoutId){
        mContext = context;
        this.c = c;
        entryAdapterLayout = layoutId;
    }

    public void setEntryList(List<Entry> entries) {
        entryList = entries;
        notifyDataSetChanged();
    }



    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        holder.entry = entry;

    }


    @Override
    public int getItemCount(){
        return entryList == null ? 0 : entryList.size();
    }


    class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle, textViewCategory, textViewDate, textViewSubcategory, textViewDescription, textViewSource;
        Entry entry;

        public EntryViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSubcategory = itemView.findViewById(R.id.textViewSubcategory);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewSource = itemView.findViewById(R.id.textViewSource);


            itemView.findViewById(R.id.EntryRevyclerviewButtonOpenSource).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("Debug_A", "Clicked Source URL:"+entry.getSource());
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.getSource()));
                        mContext.startActivity(browserIntent);
                    }
                    catch (Exception e){
                        Toast.makeText(mContext,"The URL could not be opened. Please check its format!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Entry entry = entryList.get(getAdapterPosition());
            Intent intent = new Intent(c, UpdateDeleteEntryActivity.class);
            intent.putExtra("entry", (Serializable) entry);
            c.startActivity(intent);
        }

    }
}
