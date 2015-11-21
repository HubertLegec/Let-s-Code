package sii.letscode.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sii.letscode.activities.R;
import sii.letscode.model.BookListViewModel;

/**
 * Created by dominik on 21.11.15.
 */
public class BookListAdapter extends ArrayAdapter<BookListViewModel> {
    List<BookListViewModel> data;
    Context context;
    int resource;

    public BookListAdapter(Context context, int resource, List<BookListViewModel> objects) {
        super(context, resource, objects);
        this.data = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(resource, parent, false);

            holder = new BookHolder();
            holder.lvTitle = (TextView)row.findViewById(R.id.lvTitle);
            holder.lvAuthor = (TextView)row.findViewById(R.id.lvAuthor);
            holder.lvNick = (TextView)row.findViewById(R.id.lvNick);
            holder.lvStreet = (TextView)row.findViewById(R.id.lvStreet);
            holder.bLend = (Button)row.findViewById(R.id.bLend);

            row.setTag(holder);
        }
        else
        {
            holder = (BookHolder)row.getTag();
        }

        BookListViewModel bookListViewModel = data.get(position);
        holder.lvTitle.setText(bookListViewModel.getTitle());
        holder.lvAuthor.setText(bookListViewModel.getAuthor());
        holder.lvNick.setText(bookListViewModel.getNick());
        holder.lvStreet.setText(bookListViewModel.getStreet());

        return row;
    }

    private static class BookHolder {
        TextView lvTitle;
        TextView lvAuthor;
        TextView lvNick;
        TextView lvStreet;
        Button bLend;
    }
}
