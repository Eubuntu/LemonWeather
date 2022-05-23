package com.lw.lemonweather.utils;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadExcelUtils {
    private static final String TAG  = "ReadExcel";

    //读取CSV文件
    private void ReadCSV(Activity activity){
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(activity.getAssets().open("world_country.csv"),"UTF-8");
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("line -->",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
