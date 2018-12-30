package com.sdmgap07.khaled.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListToday, taskListTomorrow, taskListUpcoming;
    NestedScrollView scrollView;
    ProgressBar loader;
    TextView todayText,tomorrowText,upcomingText;
    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();

    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        mydb = new TaskDBHelper(activity);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListToday = (NoScrollListView) findViewById(R.id.taskListToday);
        taskListTomorrow = (NoScrollListView) findViewById(R.id.taskListTomorrow);
        taskListUpcoming = (NoScrollListView) findViewById(R.id.taskListUpcoming);

        todayText = (TextView) findViewById(R.id.todayText);
        tomorrowText = (TextView) findViewById(R.id.tomorrowText);
        upcomingText = (TextView) findViewById(R.id.upcomingText);

    }

    public void openAddTask(View v)
    {
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);
    }

    public void populateData()
    {
        mydb = new TaskDBHelper(activity);
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        populateData();

    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========*/
            Cursor today = mydb.getDataToday();
            loadDataList(today, todayList);
            /* ===== TODAY ========*/

            /* ===== TOMORROW ========*/
            Cursor tomorrow = mydb.getDataTomorrow();
            loadDataList(tomorrow, tomorrowList);
            /* ===== TOMORROW ========*/

            /* ===== UPCOMING ========*/
            Cursor upcoming = mydb.getDataUpcoming();
            loadDataList(upcoming, upcomingList);
            /* ===== UPCOMING ========*/

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loadListView(taskListToday,todayList);
            loadListView(taskListTomorrow,tomorrowList);
            loadListView(taskListUpcoming,upcomingList);


            if(todayList.size()>0)
            {
                todayText.setVisibility(View.VISIBLE);
            }else{
                todayText.setVisibility(View.GONE);
            }

            if(tomorrowList.size()>0)
            {
                tomorrowText.setVisibility(View.VISIBLE);
            }else{
                tomorrowText.setVisibility(View.GONE);
            }

            if(upcomingList.size()>0)
            {
                upcomingText.setVisibility(View.VISIBLE);
            }else{
                upcomingText.setVisibility(View.GONE);
            }


            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }



    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList)
    {
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0).toString());
                mapToday.put(KEY_TASK, cursor.getString(1).toString());
                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(2).toString(), "dd-MM-yyyy"));
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }


    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList)
    {
        ListTaskAdapter adapter = new ListTaskAdapter(activity, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity, AddTask.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("id", dataList.get(+position).get(KEY_ID));
                startActivity(intent);
            }
        });
    }



}
