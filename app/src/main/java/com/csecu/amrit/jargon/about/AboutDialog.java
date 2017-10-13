package com.csecu.amrit.jargon.about;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.csecu.amrit.jargon.R;

/**
 * Created by Amrit on 26-10-2016.
 */

public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Developer's Info");
        builder.setIcon(R.drawable.info_icon);
        builder.setView(inflater.inflate(R.layout.dialog_about, null)).setCancelable(true);
        return builder.create();
    }
}
