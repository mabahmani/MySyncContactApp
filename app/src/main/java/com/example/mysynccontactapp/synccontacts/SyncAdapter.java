package com.example.mysynccontactapp.synccontacts;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.example.mysynccontactapp.retrofit.RetrofitConfig;
import com.example.mysynccontactapp.retrofit.req.SyncContactReqBody;
import com.example.mysynccontactapp.retrofit.res.SyncContactResBody;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private static int count = 0;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.i(TAG, "SyncAdapter: ");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: ");

        List<String> results = new ArrayList<>();

        try (Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER}, null ,null, null)) {
            while (cursor != null && cursor.moveToNext()) {
                if (!TextUtils.isEmpty(cursor.getString(0))) {
                    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                    try {
                        Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(cursor.getString(0),"IR");
                        Log.d(TAG, "PhoneNumberUtil: " + phoneNumber);
                        Log.d(TAG, "PhoneNumberUtil:E164: " + phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164));
                        results.add(phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164));
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        SyncContactReqBody syncContactReqBody = new SyncContactReqBody("09109703008",results);
        Call<SyncContactResBody> call = RetrofitConfig.getService().syncContacts(syncContactReqBody);

        call.enqueue(new Callback<SyncContactResBody>() {
            @Override
            public void onResponse(Call<SyncContactResBody> call, Response<SyncContactResBody> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<SyncContactResBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                syncResult.stats.numIoExceptions++;
            }
        });

    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        Log.d(TAG, "SyncAdapter: ");
    }

    @Override
    public Context getContext() {
        Log.d(TAG, "getContext: ");
        return super.getContext();
    }

    @Override
    public boolean onUnsyncableAccount() {
        Log.d(TAG, "onUnsyncableAccount: ");
        return super.onUnsyncableAccount();
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
        super.onSecurityException(account, extras, authority, syncResult);
        Log.d(TAG, "onSecurityException: " + extras + " * "  + authority + " * " + syncResult);
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        Log.d(TAG, "onSyncCanceled: ");
    }

    @Override
    public void onSyncCanceled(Thread thread) {
        super.onSyncCanceled(thread);
        Log.d(TAG, "onSyncCanceled: " + thread);
    }
}