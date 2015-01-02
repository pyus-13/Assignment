package com.pyus13.rksvassignment.utils;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pyus_13 on 1/1/2015.
 */
public class Utilities {

    private static DownloadManager downloadManager;
    public static final String DIRECTORY_NAME = "/RKSV";
    private static File downloadDirectory;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static long downloadFile(Context context, String url, String fileName) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        downloadDirectory = new File(Environment.getExternalStorageDirectory() + DIRECTORY_NAME);
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdir();
        }


        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse(url));

        request.setDescription("File from RKSV");
        request.setTitle("Downloading File");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir(DIRECTORY_NAME, fileName);
        return downloadManager.enqueue(request);
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        InputStreamReader reader;
        FileInputStream in = new FileInputStream(path);
        if (encoding == null) {
            // This constructor sets the character converter to the encoding
            // specified in the "file.encoding" property and falls back
            // to ISO 8859_1 (ISO-Latin-1) if the property doesn't exist.
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, encoding);
        }

        StringBuilder sb = new StringBuilder();

        final char[] buf = new char[1024];
        int len;
        while ((len = reader.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }

        return sb.toString();
    }

    public static String getDateCurrentTimeZone(String timestamp) {


        long timeStamp = Long.parseLong(timestamp);

        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timeStamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy ");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

}
