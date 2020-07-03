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

import static android.content.Context.ACCOUNT_SERVICE;

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

        mAccount = createSyncAccount(getContext());

        mResolver = getActivity().getContentResolver();
        mResolver.setSyncAutomatically(mAccount, ContactsContract.AUTHORITY, true);
    }


    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d(TAG, "Created new account...");
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {

            Log.d(TAG, "Failed to create account!");

            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
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
