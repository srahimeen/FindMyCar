package com.hallnguyenrahimeen.findmycar;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hallnguyenrahimeen.findmycar.Database.StoredLocation;

//import com.androidopentutorials.sqlite.R;
//import com.androidopentutorials.sqlite.to.Employee;

public class ListAdapter extends ArrayAdapter<StoredLocation> {

    private Context context;
    List<StoredLocation> locations;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    public ListAdapter(Context context, List<StoredLocation> employees) {
        super(context, R.layout.row_item, employees);
        this.context = context;
        this.locations = employees;
    }

    private class ViewHolder {
        TextView locLatText;
        TextView locLngText;
        TextView locTimeText;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public StoredLocation getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
            holder = new ViewHolder();

            holder.locLatText = (TextView) convertView
                    .findViewById(R.id.txt_loc_lat);
            holder.locLngText = (TextView) convertView
                    .findViewById(R.id.txt_loc_lng  );
            holder.locTimeText = (TextView) convertView
                    .findViewById(R.id.txt_loc_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoredLocation employee = (StoredLocation) getItem(position);
        holder.locLatText.setText(employee.getLat() + "");
        holder.locLngText.setText(employee.getLng() + "");
        holder.locTimeText.setText(employee.getTime() + "");

        return convertView;
    }

    @Override
    public void add(StoredLocation location) {
        locations.add(location);
        notifyDataSetChanged();
        super.add(location);
    }

    @Override
    public void remove(StoredLocation location) {
        locations.remove(location);
        notifyDataSetChanged();
        super.remove(location);
    }
}