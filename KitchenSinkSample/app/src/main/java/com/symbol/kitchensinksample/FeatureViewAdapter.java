package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This feature view adapter class to diplay the list of features supported by the sample.
 */
public class FeatureViewAdapter extends ArrayAdapter<FeatureInfo> {

    Context context;
    int layoutResourceId;
    FeatureInfo data[] = null;

    public FeatureViewAdapter(Context context, int layoutResourceId, FeatureInfo[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FeatureHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FeatureHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (FeatureHolder)row.getTag();
        }

        FeatureInfo sample = data[position];
        holder.txtTitle.setText(sample.title);
        holder.imgIcon.setImageResource(sample.icon);

        return row;
    }

    static class FeatureHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
