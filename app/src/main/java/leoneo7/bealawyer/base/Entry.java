package leoneo7.bealawyer.base;

/**
 * Created by ryouken on 2016/11/03.
 */

public class Entry {
    String title;
    long date;
    String repeat;

    public Entry(String title, long date, String repeat) {
        this.title = title;
        this.date = date;
        this.repeat = repeat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
