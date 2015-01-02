package com.pyus13.rksvassignment.uielements;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pyus13.rksvassignment.R;
import com.pyus13.rksvassignment.adapters.DataAdapter;
import com.pyus13.rksvassignment.databeans.Data;
import com.pyus13.rksvassignment.utils.DataItemDecoration;
import com.pyus13.rksvassignment.utils.Utilities;
import com.pyus13.rksvassignment.widget.MyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 *
 * Main Fragment where the screen shows all data from server
 * Created by pyus_13 on 1/1/2015.
 */
public class MainFragment extends Fragment {

    private long enqueue;
    private static final String FILE_NAME = "data.txt";
    private ArrayList<Data> datas;
    private View rootView;

    private ProgressBar progressBar;

    private MyRecyclerView recyclerView;

    private DataAdapter dataAdapter;

    private TextView totalItemText;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().registerReceiver(broadcastReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        enqueue = Utilities.downloadFile(getActivity(), getString(R.string.url_download), FILE_NAME);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        totalItemText = (TextView) rootView.findViewById(R.id.total_item_text);

        recyclerView = (MyRecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DataItemDecoration(getActivity(), DataItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        parseData();

                        String totalItem = String.format(getResources().getString(R.string.total_item_count), datas.size());

                        totalItemText.setText(totalItem);

                        dataAdapter = new DataAdapter(getActivity(), datas);

                        if (recyclerView != null) {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(dataAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        }

                    }
                }
            }

        }
    };

    private void parseData() {
        datas = new ArrayList<>();
        try {
            String path = Environment.getExternalStorageDirectory() +
                    Utilities.DIRECTORY_NAME + File.separator + FILE_NAME;
            String fileData = Utilities.readFile(path, Charset.defaultCharset());

            JSONArray jsonArray = new JSONArray(fileData);

            for (int i = 0; i < jsonArray.length(); i++) {
                String[] rawData = jsonArray.get(i).toString().split(",");
                Data data = new Data();
                data.setTimeStamp(rawData[0]);
                data.setOpen(rawData[1]);
                data.setHigh(rawData[2]);
                data.setLow(rawData[3]);
                data.setClose(rawData[4]);
                data.setVol(rawData[5]);
                datas.add(data);

            }

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error in reading File" + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException jsonException) {
            Toast.makeText(getActivity(), "Error in reading File" + jsonException.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_data :
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                enqueue = Utilities.downloadFile(getActivity(), getString(R.string.url_download), FILE_NAME);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }


}
