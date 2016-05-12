package alexandertsebenko.ru.mytodo;

    import android.content.Context;
    import android.os.Build;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.TodoHolder> {

    ArrayList<TodoInstance> arrayList;

    public CustomAdapter(ArrayList<TodoInstance>arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        TodoHolder todoHolder = new TodoHolder(view);
        return todoHolder;
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
        TodoInstance todoItem = arrayList.get(position);
        holder.tvTODO.setText(todoItem.getTodoText());
        holder.tvExpDate.setText(sdf.format(todoItem.getExpireDate()));
        holder.tvDonDate.setText(sdf.format(todoItem.getDoneDate()));
        Context context = holder.ivAvatar.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            holder.ivAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.clipboard_text, context.getTheme()));
        else
            holder.ivAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.clipboard_text));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class TodoHolder extends RecyclerView.ViewHolder{
        ImageView ivAvatar;
        TextView tvTODO;
        TextView tvExpDate;
        TextView tvDonDate;
        public TodoHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.imageView);
            tvTODO = (TextView) itemView.findViewById(R.id.textOfTodo);
            tvExpDate = (TextView) itemView.findViewById(R.id.expireDate);
            tvDonDate = (TextView) itemView.findViewById(R.id.doneDate);
        }
    }
}
