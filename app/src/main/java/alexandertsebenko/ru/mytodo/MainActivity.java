package alexandertsebenko.ru.mytodo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView listView;
    private TodosDataSource datasource;
    private RecyclerView recyclerView;
    ArrayList<TodoInstance> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView = (ListView)findViewById(R.id.lvMain);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        fillArrayList();

//        CustAdapter customAdapter = new CustAdapter(values, MainActivity.this);
//        listView.setAdapter(customAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CustomAdapter(values));

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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

    public void addTodoDialog(final View view) {
        final EditText input = new EditText(MainActivity.this);//TODO добавить возможность удаления заданий по длительномы нажатию
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        new AlertDialog.Builder(view.getContext())
                .setTitle(getString(R.string.add_todo_dialog_title))
                .setView(input)
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String todoText = input.getText().toString();
                        long curTime = System.currentTimeMillis();//TODO делать возможность выбирать будующую дату, и по умолчанию ставить текущую дату
                        datasource.createTodoInstance(todoText,curTime,0,false);//TODO если дата исполнения 0 не отрисовывать
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
}
