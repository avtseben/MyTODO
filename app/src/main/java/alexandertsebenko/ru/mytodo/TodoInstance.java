package alexandertsebenko.ru.mytodo;

/**
 * Created by sas on 09.05.2016.
 */
public class TodoInstance {

    private long ID;
    private String todoText;
    private long expireDate;
    private long doneDate;
    private boolean done;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(long doneDate) {
        this.doneDate = doneDate;
    }

    public String getTodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
