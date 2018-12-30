package com.sdmgap07.khaled.todolist;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.ArrayList;
import java.util.HashMap;

public class ListTaskAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public ListTaskAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data=d;
    }


    @Override
    public int getCount()
    {
        return data.size();
    }


    @Override
    public Object getItem(int position)
    {
        return position;
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ListTaskViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.list_row, parent, false);
            holder.task_image = (TextView) convertView.findViewById(R.id.task_image);
            holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }
        holder.task_image.setId(position);
        holder.task_name.setId(position);
        holder.task_date.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.task_name.setText(song.get(MainActivity.KEY_TASK));
            holder.task_date.setText(song.get(MainActivity.KEY_DATE));

            /* Image */
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(Html.fromHtml("&#11044;"));
            /* Image */

        }catch(Exception e) {}



        return convertView;
    }
}

class ListTaskViewHolder {
    TextView task_image;
    TextView task_name, task_date;
}
