package com.systemteam.smstool.statistics;

import android.text.TextUtils;

import com.google.common.base.Objects;

import java.util.List;

/**
 * the immutable contact list including furthur statistics.
 */
public class ContactsData {

    /**
     * newline
     */
    private static final CharSequence NL = System.getProperty("line.separator");

    /**
     * The contact list.
     */
    private final List<Contact> contacts;

    /**
     * The total number of incoming messages.
     */
    private final int totalIncoming;

    /**
     * The total number of outgoing messages.
     */
    private final int totalOutgoing;

    /**
     * @param contacts the contact list
     */
    public ContactsData(final List<Contact> contacts) {
        this.contacts = contacts;
        int total = 0, totalIncoming = 0, totalOutgoing = 0;

        for (final Contact contact : contacts) {
            total += contact.getTotal();
            totalIncoming += contact.getIncoming();
            totalOutgoing += contact.getOutgoing();
        }

        this.totalIncoming = totalIncoming;
        this.totalOutgoing = totalOutgoing;

        for (final Contact contact : contacts) {
            contact.setPercentage(total);
        }

        if (contacts.size() > 0) {
            final int greatest = contacts.get(0).getTotal();

            for (final Contact contact : contacts) {
                contact.setBarScale(greatest);
            }
        }
    }

    /**
     * @return the contact list
     */
    public List<Contact> getList() {
        return this.contacts;
    }

    /**
     * @return the totalIncoming
     */
    public int getTotalIncoming() {
        return this.totalIncoming;
    }

    /**
     * @return the totalOutgoing
     */
    public int getTotalOutgoing() {
        return this.totalOutgoing;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("total_incoming", this.totalIncoming)
                .add("total_outgoing", this.totalOutgoing)
                .add("list", TextUtils.join(NL, this.contacts))
                .toString();
    }
}