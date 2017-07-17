package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This name value pair view adapter class to diplay the list of data on an activity.
 */
public class KeyValueViewAdapter extends ArrayAdapter<InfoList> {
    private static final String TAG = KeyValueViewAdapter.class.getSimpleName();
    Context context;
    int layoutResourceId;
    InfoList data[] = null;

    public KeyValueViewAdapter(Context context, int layoutResourceId, InfoList[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        Log.d(TAG,"KeyValueViewAdapter: data=" + this.data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        KeyValueHolder holder;
        Log.d(TAG,"convertView: " + convertView);
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new KeyValueHolder();
            holder.txtKey = (TextView)row.findViewById(R.id.txtKey);
            holder.txtValue = (TextView)row.findViewById(R.id.txtValue);

            Log.d(TAG,"holder.txtKey: " + holder.txtKey);
            Log.d(TAG,"holder.txtValue: " + holder.txtValue);
            row.setTag(holder);
        }
        else
        {
            Log.d(TAG,"row: " + row);
            holder = (KeyValueHolder)row.getTag();
        }

        InfoList sample = data[position];
        Log.d(TAG,"sample: " + sample);
        holder.txtKey.setText(sample.key);
        holder.txtValue.setText(sample.value);

        return row;
    }

    static class KeyValueHolder
    {
        TextView txtKey;
        TextView txtValue;
    }
}
