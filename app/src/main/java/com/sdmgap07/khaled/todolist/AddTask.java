package com.sdmgap07.khaled.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TaskDBHelper myDB;
    DatePickerDialog dpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    String dateFinal, nameFinal, id;
    Intent intent;
    Boolean isUpdate;
    ImageView deleteTask;





    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_task);

        myDB = new TaskDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        deleteTask = (ImageView) findViewById(R.id.deleteTask);
        deleteTask.setVisibility(View.INVISIBLE);

        dateFinal = todayDateString();
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        if(isUpdate){
            init_update();
            deleteTask.setVisibility(View.VISIBLE);
        }

    }


    public void init_update()
    {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        toolbar_task_add_title.setText("Update");
        Cursor task = myDB.getDataSpecific(id);
        if(task != null){
            task.moveToFirst();

            task_name.setText(task.getString(1).toString());
            Calendar cal = Function.Epoch2Calendar(task.getString(2).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);
            task_date.setText(Function.Epoch2DateString(task.getString(2).toString(), "dd/MM/yyyy"));
        }
    }


    public String todayDateString()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());

        return dateFormat.toString();

    }


    public void closeAddTask(View view)
    {
        finish();
    }


    public void doneAddTask(View v)
    {
        int errorStep = 0;
        EditText task_name = (EditText) findViewById(R.id.task_name);
        EditText task_date = (EditText) findViewById(R.id.task_date);
        nameFinal = task_name.getText().toString();
        dateFinal = task_date.getText().toString();


        /* Checking */
        if (nameFinal.trim().length() < 1) {
            errorStep++;
            task_name.setError("Provide a task name.");
        }

        if (dateFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Provide a specific date");
        }



        if (errorStep == 0) {
            if (isUpdate) {
                myDB.updateContact(id, nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                myDB.insertContact(nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }

    }


    public void deleteTask(View v)
    {
        myDB.deleteContact(id);
        Toast.makeText(getApplicationContext(), "Task Deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        int monthAddOne = startMonth + 1;
        String date = (startDay < 10 ? "0" + startDay : "" + startDay) + "/" +
                (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                startYear;
        EditText task_date = (EditText) findViewById(R.id.task_date);
        task_date.setText(date);
    }


    public void showStartDatePicker(View v)
    {
        dpd = DatePickerDialog.newInstance(AddTask.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }
}
