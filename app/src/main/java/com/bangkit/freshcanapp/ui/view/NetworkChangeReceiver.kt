package com.bangkit.freshcanapp.ui.view

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog

class NetworkChangeReceiver : BroadcastReceiver() {
    private var alertDialog: AlertDialog? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (!haveNetworkConnection(context)) {
            if(alertDialog==null){
                alertDialog = AlertDialog.Builder(context).apply {
                    setTitle("Warning")
                    setMessage("Please connect your phone to the internet")
                    setPositiveButton("Close") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                }.create()
                alertDialog!!.show()
            }else{
                alertDialog!!.show()
            }
        }
    }

    private fun haveNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}