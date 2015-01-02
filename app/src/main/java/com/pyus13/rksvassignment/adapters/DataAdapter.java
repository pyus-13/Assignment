package com.pyus13.rksvassignment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pyus13.rksvassignment.R;
import com.pyus13.rksvassignment.databeans.Data;
import com.pyus13.rksvassignment.utils.Utilities;

import java.util.ArrayList;

/**
 * Adapter to show data in Recycler view
 *
 * Created by pyus_13 on 1/2/2015.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyDataViewHolder> {

    private LayoutInflater inflater;

    private ArrayList<Data> datas;

    public DataAdapter(Context context, ArrayList<Data> datas) {
        inflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public MyDataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row, viewGroup, false);
        MyDataViewHolder viewHolder = new MyDataViewHolder(view);
        return viewHolder;

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(MyDataViewHolder myDataViewHolder, int i) {
        myDataViewHolder.textView.setText(Utilities.getDateCurrentTimeZone(datas.get(i).getTimeStamp()));

    }

    class MyDataViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public MyDataViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.date_text_view);

        }
    }
}
