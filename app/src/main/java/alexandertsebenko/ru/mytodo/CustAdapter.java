package alexandertsebenko.ru.mytodo;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sas on 12.05.2016.
 */
public class CustAdapter extends BaseAdapter {

    ArrayList<TodoInstance> arrayList;
    Context context;
    LayoutInflater layoutInflater;

    public CustAdapter(ArrayList<TodoInstance> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public TodoInstance getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.todo_item, parent, false);
            holder = new Holder();
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.imageView);
            holder.tvTODO = (TextView) convertView.findViewById(R.id.textOfTodo);
            holder.tvExpDate = (TextView) convertView.findViewById(R.id.expireDate);
            holder.tvDonDate = (TextView) convertView.findViewById(R.id.doneDate);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();

        TodoInstance p = arrayList.get(position);
        holder.tvTODO.setText(p.getTodoText());
        holder.tvExpDate.setText(p.getTodoText());
        holder.tvDonDate.setText(p.getTodoText());
        //holder.tvExpDate.setText(p.getExpireDate());
        //holder.tvDonDate.setText(p.getDoneDate());
        //tvDate.setText(DateFormat.format("dd.MM.yyyy", p.date, null));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.ivAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.clipboard_text, context.getTheme()));
            else
                holder.ivAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.clipboard_text));

        return convertView;
    }
    private class Holder {
        ImageView ivAvatar;
        TextView tvTODO;
        TextView tvExpDate;
        TextView tvDonDate;
    }

}
