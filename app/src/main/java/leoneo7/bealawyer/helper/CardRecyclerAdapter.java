package leoneo7.bealawyer.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import leoneo7.bealawyer.R;
import leoneo7.bealawyer.base.Entry;

/**
 * Created by ryouken on 2016/11/03.
 */

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{

    private List<Entry> list;
    private Context context;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getDate());
        Date date = calendar.getTime();
        vh.title.setText(list.get(position).getTitle());
        vh.date.setText(date.toString());
        vh.repeat.setText(list.get(position).getRepeat() + "回");
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 詳細表示
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