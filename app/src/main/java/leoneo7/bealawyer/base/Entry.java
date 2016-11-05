package leoneo7.bealawyer.base;

/**
 * Created by ryouken on 2016/11/03.
 */

public class Entry {
    int id;
    String title;
    String image;
    String numbering;
    long date;
    String repeat;

    public Entry(int id, String title, String image, String numbering, long date, String repeat) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.numbering = numbering;
        this.date = date;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
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
