package com.example.mysynccontactapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mysynccontactapp.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public static final String AUTHORITY = ContactsContract.AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.mysynccontactapp";
    // The account name
    public static final String ACCOUNT = "MySyncContactApp";
    // Instance fields
    Account mAccount;

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabFont();

        mAccount = getOrCreateAccount(getContext());

//        Bundle bundle = new Bundle();
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // Performing a sync no matter if it's off
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // Performing a sync no matter if it's off
//        ContentResolver.requestSync(mAccount, ContactsContract.AUTHORITY, bundle);

    }


    private static Account getOrCreateAccount(Context context) {

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        Account account;
        if (accounts.length == 0) {
            account = new Account(context.getString(R.string.app_name), ACCOUNT_TYPE);

            if (accountManager.addAccountExplicitly(account, null, null)){
                Log.d(TAG, "Created new account...");
                ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
            }

            else {
                Log.d(TAG, "Failed to create account!");
            }
        }

        else{
            Log.d(TAG, "Account (" + accounts[0] + ") Already Exist! ");
            account = accounts[0];
        }

        if (account != null && !ContentResolver.getSyncAutomatically(account, ContactsContract.AUTHORITY)) {
            Log.d(TAG, "ContentResolver:setSyncAutomatically");
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        }

        return account;
    }

    private void setTabFont() {
        Typeface fontTypeFace = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/IRANSansMobile.ttf");

        ViewGroup vg = (ViewGroup) binding.tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(fontTypeFace, Typeface.NORMAL);
                }
            }
        }
    }
}
