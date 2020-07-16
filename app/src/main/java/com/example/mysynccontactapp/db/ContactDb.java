package com.example.mysynccontactapp.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.mysynccontactapp.model.SystemContactInfo;
import com.example.mysynccontactapp.util.PhoneNumberFormatter;

public class ContactDb {
    private Context context;

    public ContactDb(Context context) {
        this.context = context;
    }

    public SystemContactInfo getSystemContactInfo(String e164) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(e164));
        String[] projection = {ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor numberCursor = null;
        Cursor idCursor = null;

        try {
            numberCursor = context.getContentResolver().query(uri, projection, null, null, null);

            while (numberCursor != null && numberCursor.moveToNext()) {
                String systemNumber = numberCursor.getString(0);
                String systemE164 = PhoneNumberFormatter.getInstance().formatE164(systemNumber);

                if (systemE164.equals(e164)) {
                    idCursor = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
                            new String[]{ContactsContract.RawContacts._ID},
                            ContactsContract.RawContacts.CONTACT_ID + " = ? ",
                            new String[]{String.valueOf(numberCursor.getLong(1))},
                            null);

                    if (idCursor != null && idCursor.moveToNext()) {
                        return new SystemContactInfo(numberCursor.getString(2),
                                numberCursor.getString(0),
                                idCursor.getLong(0));
                    }
                }
            }
        } finally {
            if (numberCursor != null) numberCursor.close();
            if (idCursor != null) idCursor.close();
        }

        return null;
    }
}
