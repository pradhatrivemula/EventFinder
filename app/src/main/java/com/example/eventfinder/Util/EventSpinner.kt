package com.example.eventfinder.Util

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class EventSpinner : DialogFragment() {
    private var progressDialog: ProgressDialog? = null

    //private EntwineTracker entwineTracker;
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //entwineTracker = EntwineTracker.getInstance(context);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        try {
            progressDialog = ProgressDialog(activity)
            setStyle(STYLE_NO_TITLE, theme)
            progressDialog!!.setMessage("Please wait...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        } catch (e: Exception) {
            //entwineTracker.sendException(e);
        }
        return progressDialog!!
    }
}