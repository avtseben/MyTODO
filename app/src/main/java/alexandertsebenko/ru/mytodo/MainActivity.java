package alexandertsebenko.ru.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    private TodosDataSource datasource;
    private RecyclerView recyclerView;
    ArrayList<TodoInstance> values;
    private int mYear, mMonth, mDay;
    TextView txtDate;
    long unixTime = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        fillArrayList();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CustomAdapter(values));

    }
    /*public void onClick(View view) {
        @SuppressWarnings("unchecked")
        Intent intentBack = new Intent(this,AddTodoInstance.class);
        switch (view.getId()) {
            case R.id.button_add_room:
                startActivity(intentBack);
                break;
        }
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TodoInstance item = (TodoInstance) getListAdapter().getItem(position);
        Intent intent = new Intent(this, UnsortedNote.class);
        Bundle b = new Bundle();
        b.putString("Note", item.getTodoInstance());
        b.putLong("ID", item.getId());
        intent.putExtras(b);
        startActivity(intent);
    }*/
    public void fillArrayList() {
        datasource = new TodosDataSource(this);
        datasource.open();
        values = datasource.getAllTodoInstances();
    }
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
//TODO добавить возможность удаления заданий по длительномы нажатию
//TODO сделать невозможным добавления пустой записи todo
    public void addTodoDialog(final View view) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final View inflate = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final EditText editTodoText = (EditText) inflate.findViewById(R.id.et_todo);
        txtDate = (TextView) inflate.findViewById(R.id.tv_date);
        txtDate.setText(mDay + "-" + mMonth + "-" + mYear);

        new AlertDialog.Builder(view.getContext())
                .setTitle(getString(R.string.add_todo_dialog_title))
                .setView(inflate)
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String todoText = editTodoText.getText().toString();
                        datasource.createTodoInstance(todoText,unixTime,-1,false);
                        //Повторно запрашиваем данные и обновляем recycleView
                        values = datasource.getAllTodoInstances();
                        recyclerView.setAdapter(new CustomAdapter(values));
                    }
                })
                .setNeutralButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    public void datePicker(View view) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        unixTime = calendar.getTimeInMillis();
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
