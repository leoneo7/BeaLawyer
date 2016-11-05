package leoneo7.bealawyer.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import leoneo7.bealawyer.R;
import leoneo7.bealawyer.base.Entry;
import leoneo7.bealawyer.edit.ViewActivity;

/**
 * Created by ryouken on 2016/11/03.
 */

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{

    private List<Entry> list;
    private Context context;
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
    private static final String NUMBERING = "numbering";

    public CardRecyclerAdapter(Context context, List<Entry> entryList) {
        super();
        this.list = entryList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        final Entry entry = list.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(entry.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        String dateString = String.format("%s/%s/%s", year, month+1, date);

        vh.title.setText(entry.getTitle());
        vh.date.setText(dateString);
        vh.repeat.setText(entry.getRepeat() + "回");
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewActivity.class);
                intent.putExtra(ID, entry.getId());
                intent.putExtra(TITLE, entry.getTitle());
                intent.putExtra(IMAGE, entry.getImage());
                intent.putExtra(NUMBERING, entry.getNumbering());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.layout_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView repeat;
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView)v.findViewById(R.id.title);
            date = (TextView)v.findViewById(R.id.date);
            repeat = (TextView)v.findViewById(R.id.repeat);
            layout = (LinearLayout)v.findViewById(R.id.layout);
        }
    }
}