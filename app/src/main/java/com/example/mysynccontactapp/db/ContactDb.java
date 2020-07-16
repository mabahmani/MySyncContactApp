package com.example.mysynccontactapp.db;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.mysynccontactapp.model.SystemContactInfo;
import com.example.mysynccontactapp.util.PhoneNumberFormatter;

import java.util.ArrayList;
import java.util.List;



public class ContactDb {
    private static final String TAG = "ContactDb";
    private static final String SYNC = "__MS";
    private Context context;

    public ContactDb(Context context) {
        this.context = context;
    }

    public void setRegisteredUsers(Account account, List<SystemContactInfo> registeredAddressList) {
        ArrayList<ContentProviderOperation> operations           = new ArrayList<>();
        for (SystemContactInfo systemContactInfo: registeredAddressList) {
            int index   = operations.size();
            Uri dataUri = ContactsContract.Data.CONTENT_URI.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();

            Log.d(TAG, "setRegisteredUsers: " + account);
            operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.name)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type)
                    .withValue(ContactsContract.RawContacts.SYNC1, systemContactInfo.getNumber())
                    .withValue(ContactsContract.RawContacts.SYNC4, String.valueOf(true))
                    .build());

            operations.add(ContentProviderOperation.newInsert(dataUri)
                    .withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, index)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, systemContactInfo.getName())
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .build());

            operations.add(ContentProviderOperation.newInsert(dataUri)
                    .withValueBackReference(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, index)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, systemContactInfo.getNumber())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
                    .withValue(ContactsContract.Data.SYNC2, SYNC)
                    .build());

            operations.add(ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI)
                    .withValue(ContactsContract.AggregationExceptions.RAW_CONTACT_ID1, systemContactInfo.getId())
                    .withValueBackReference(ContactsContract.AggregationExceptions.RAW_CONTACT_ID2, index)
                    .withValue(ContactsContract.AggregationExceptions.TYPE, ContactsContract.AggregationExceptions.TYPE_KEEP_TOGETHER)
                    .build());
        }

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (OperationApplicationException e) {
            Log.d(TAG, "setRegisteredUsers: " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            Log.d(TAG, "setRegisteredUsers: " + e.getMessage());
            e.printStackTrace();
        }
        Log.d(TAG, "setRegisteredUsers: ");
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

    public List<String> getAppRawContacts(Account account){
        Uri currentContactsUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(ContactsContract.RawContacts.ACCOUNT_NAME, account.name)
                .appendQueryParameter(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type).build();

        List<String> result = new ArrayList<>();
        Cursor                     cursor         = null;

        try {
            String[] projection = new String[] {BaseColumns._ID, ContactsContract.RawContacts.SYNC1, ContactsContract.RawContacts.SYNC4, ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, ContactsContract.RawContacts.DISPLAY_NAME_SOURCE};

            cursor = context.getContentResolver().query(currentContactsUri, projection, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                String  currentAddress              = PhoneNumberFormatter.getInstance().formatE164(cursor.getString(1));
                result.add(currentAddress);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }
}
