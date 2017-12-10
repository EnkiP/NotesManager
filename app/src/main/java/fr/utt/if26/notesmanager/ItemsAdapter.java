package fr.utt.if26.notesmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private Item[] items = null;

    public ItemsAdapter(Context context, int resource, Item[] items) {
        super(context, resource, items);
        this.layoutResourceId = resource;
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ItemViewHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemViewHolder();
            holder.id = (TextView)row.findViewById(R.id.itemId);
            holder.name = (TextView)row.findViewById(R.id.itemName);
            holder.type = (TextView)row.findViewById(R.id.itemType);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder)row.getTag();
        }

        Item item = items[position];
        holder.id.setText( Integer.toString(item.getId()));
        holder.name.setText(item.getName());
        holder.type.setText(item.getType().toString());

        return row;
    }

    private static class ItemViewHolder
    {
        TextView id;
        TextView name;
        TextView type;
    }
}
