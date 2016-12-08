package com.systemteam.smstool.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.systemteam.smstool.R;

import java.util.Locale;
import java.util.Set;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

    /**
     * @see Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main2);

        new AsyncGetContacts(this) {
            @Override
            protected void onPostExecute(final Set<Contact> results) {
                super.onPostExecute(results);

                final ContactsData data = new ContactsData(Lists.newArrayList(results));

                // set-up total infobar
                final TextView total = (TextView) this.aty.findViewById(R.id.total);
                final String format = this.aty.getResources().getString(R.string.total);
                total.setText(String.format(Locale.CANADA, format, data.getTotalIncoming(), data.getTotalOutgoing()));

                // set-up list view
                final ListView contactsView = (ListView) this.aty.findViewById(R.id.contacts);
                contactsView.setAdapter(new ContactList(this.aty, data.getList()));
            }
        }.execute();
    }
}