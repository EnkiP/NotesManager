package fr.utt.if26.notesmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            holder.icon = (ImageView)row.findViewById(R.id.itemIcon);
            holder.name = (TextView)row.findViewById(R.id.itemName);
            holder.preview = (TextView)row.findViewById(R.id.itemPreview);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder)row.getTag();
        }

        Item item = items[position];
        holder.name.setText(item.getName());
        switch (item.getType()){
            case Folder:
                holder.icon.setImageResource(R.drawable.folder_icon);
                holder.preview.setHeight(0);
                break;
            case Note:
                holder.icon.setImageResource(R.drawable.note_icon);
                String noteContent = ((Note)item).getContent();
                holder.preview.setText(noteContent);
                break;
        }

        return row;
    }

    private static class ItemViewHolder
    {
        TextView name;
        TextView preview;
        ImageView icon;
    }
}
