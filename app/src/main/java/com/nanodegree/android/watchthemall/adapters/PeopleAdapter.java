package com.nanodegree.android.watchthemall.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.ShowInfoFragment;
/**
 * Created by Sooheib on 8/27/16.
 */
/**
 * {@link PeopleAdapter} exposes a list of cast entries
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class PeopleAdapter extends CursorAdapter {

    public PeopleAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    // These views are reused as needed
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cast, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    // Fill-in the views with the contents of the cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String characterName = cursor.getString(ShowInfoFragment.COL_CHARACTER);
        if (characterName.indexOf('/')!=-1) {
            characterName = characterName.substring(0, characterName.indexOf('/')+1) + " ... / " +
                    context.getString(R.string.various_characters);
        }
        viewHolder.characterName.setText(characterName);

        String personName = cursor.getString(ShowInfoFragment.COL_PERSON_NAME);
        viewHolder.personName.setText("(" + personName + ")");
    }

    /**
     * Cache of the children views
     */
    private static class ViewHolder {
        public final TextView characterName;
        public final TextView personName;

        public ViewHolder(View view) {
            characterName = (TextView) view.findViewById(R.id.list_item_character_name);
            personName = (TextView) view.findViewById(R.id.list_item_person_name);
        }
    }
}