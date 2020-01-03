package com.hoangpro.pikacover.morefunct;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {
    public static String currentJson;

    public static long toMilies(String time) {
        if (time != null) {
            String[] arr = time.split(":");
            long result = 0;
            if (arr.length == 3) {
                for (int i = 2; i >= 0; i--) {
                    switch (i) {
                        case 2:
                            result += (long) (Double.parseDouble(arr[i].replace(",", ".")) * 1000);
                            break;
                        case 1:
                            result += Integer.parseInt(arr[i]) * 60000;
                            break;
                        case 0:
                            result += Integer.parseInt(arr[i]) * 3600000;
                            break;
                    }
                }
            } else if (arr.length == 2) {
                for (int i = 1; i >= 0; i--) {
                    switch (i) {
                        case 1:
                            result += (long) (Double.parseDouble(arr[i].replace(",", ".")) * 1000);
                            break;
                        case 0:
                            result += Integer.parseInt(arr[i]) * 60000;
                            break;
                    }
                }
            }
            return result;
        }
        return 0;
    }

    private static String sharedName = "ListSongCache";

    public static void saveListSong(Context context, String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json", json);
        editor.apply();
    }

    public static String getListSongFromCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("json", "");
    }

}
