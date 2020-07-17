package com.example.mysynccontactapp.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysynccontactapp.R;
import com.example.mysynccontactapp.util.BaseCursorAdapter;
import com.example.mysynccontactapp.util.PhoneNumberFormatter;

public class ContactsCursorAdapter extends BaseCursorAdapter<ContactsCursorAdapter.ViewHolder> {
    public ContactsCursorAdapter() {
        super(null);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.contactName.setText(cursor.getString(1));
        holder.contactPhone.setText(PhoneNumberFormatter.getInstance().formatE164(cursor.getString(0)));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
        }
    }
}
