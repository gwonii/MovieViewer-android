package com.gmail.hc.gwnoii.movie.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class AppHelper {

    private static final String TAG = "AppHelper.class";

    public static RequestQueue requestQueue;

    public static String host = "boostcourse-appapi.connect.or.kr";
    public static int port = 10000;

    private static SQLiteDatabase database;

    private static String createTableMovieOutline =
            "(" +
                    "_id integer PRIMARY KEY autoincrement, " +
                    "id float UNIQUE, " +                  //0
                    "title text, " +                //1
                    "title_eng text, " +            //2
                    "date text, " +                 //3
                    "user_rating float, " +         //4
                    "audience_rating float, " +     //5
                    "reviewer_rating float, " +     //6
                    "reservation_rate float, " +    //7
                    "reservation_grade float, " +   //8
                    "grade float, " +               //9
                    "thumb text, " +                //10
                    "image text" +                  //11
                    ")";

    private static String createTableMovieDetailOutline =
            "(" +
                    "_id integer PRIMARY KEY autoincrement, " +
                    "id float UNIQUE, " +                  //0
                    "title text, " +                //1
                    "date text, " +                 //2
                    "user_rating float, " +         //3
                    "audience_rating float, " +     //4
                    "reviewer_rating float, " +     //5
                    "reservation_rate float, " +    //6
                    "reservation_grade float, " +   //7
                    "grade float, " +               //8
                    "thumb text, " +                //9
                    "image text, " +                //10
                    "photos text, " +               //11
                    "videos text, " +               //12
                    "outlinks text, " +             //13
                    "genre text, " +                //14
                    "duration float, " +            //15
                    "audience float, " +            //16
                    "synopsis text, " +             //17
                    "director text, " +             //18
                    "actor text, " +                //19
                    "like_num float, " +                //20
                    "dislike_num float" +               //21
                    ")";

    private static String createTableCommentOutline =
            "(" +
                    "_id integer PRIMARY KEY autoincrement, " +
                    "id float, " +                  //0
                    "writer text, " +               //1
                    "movieId float, " +             //2
                    "writer_image text, " +         //3
                    "time text, " +                 //4
                    "timestamp float, " +           //5
                    "rating float, " +              //6
                    "contents text, " +             //7
                    "recommend float, " +           //8
                    "total_count float" +            //9
                    ")";


    public static void openDatabase(Context context, String dbName) {

        database = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        Log.d(TAG, "데이터베이스가 생성되었습니다.");
    }

    public static void createMovieTable(String tableName, Context context) {
        if (database != null) {
            if (tableName.equals("Movie")) {

                Log.d("MyAppHelper", tableName);
                String sql = "create table if not exists " + tableName +
                        createTableMovieOutline;
                database.execSQL(sql);
            } else {
                Toast.makeText(context, "테이블 이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "데이터베이스 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public static void createMovieDetailTable(String tableName, Context context) {
        if (database != null) {
            if (tableName.equals("MovieDetail")) {
                String sql = "create table if not exists " + tableName +
                        createTableMovieDetailOutline;
                database.execSQL(sql);
            } else {
                Toast.makeText(context, "테이블 이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "데이터베이스 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public static void createCommentTable(String tableName, Context context, int num) {
        if (database != null) {
            if (tableName.equals("Comment")) {
                String tempName = tableName + num;
                String sql = "create table if not exists " + tempName +
                        createTableCommentOutline;
                database.execSQL(sql);
            } else {
                Toast.makeText(context, "테이블 이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "데이터베이스 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public static void testSql() {
        float id = 3;
        String title = "재밌는영화";

        String sql = "insert into " + "Movie" + "(id, title) values(?,?)";
        Object[] params = {id, title};

        database.execSQL(sql, params);
        Log.d("MyAppHelper", "데이터가 삽입되었어요");

        String sqll = "select id, title from " + "Movie";
        Cursor cursor = database.rawQuery(sqll, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Log.d("MyAppHelper", "여기는 왔나?");
            id = (int) cursor.getFloat(0);
            title = cursor.getString(1);
            Log.d("MyAppHelper", title);
        }
        cursor.close();
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }


}
