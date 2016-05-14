package alexandertsebenko.ru.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    String textOfSelectTodoInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        fillArrayList();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CustomAdapter(values));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "SHORT CLICK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //TODO нужно переделать чтобы holder хранил ID записи заметки чтобы удалять,редактировать только одну заметку
                //Запоминаем текст заметки из выбранного item
                CustomAdapter.TodoHolder holder = (CustomAdapter.TodoHolder) recyclerView.findViewHolderForAdapterPosition(position);
                textOfSelectTodoInstance = (String) holder.tvTODO.getText();
                //Toast.makeText(MainActivity.this, textOfSelectTodoInstance, Toast.LENGTH_SHORT).show();
                registerForContextMenu(view);
            }
        }));

    }
    public void refreshRecycleView() {
        values = datasource.getAllTodoInstances();
        recyclerView.setAdapter(new CustomAdapter(values));
    }
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
                        refreshRecycleView();
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.edit_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit_todo:
                Toast.makeText(MainActivity.this, "EDIT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_set_done:
                Toast.makeText(MainActivity.this, "SET DONE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_delete_todo:
                datasource.deleteTodoInstanceByText("'" + textOfSelectTodoInstance + "'");
                refreshRecycleView();
                Toast.makeText(MainActivity.this, "DELETE", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onContextItemSelected(item);
    }

}
