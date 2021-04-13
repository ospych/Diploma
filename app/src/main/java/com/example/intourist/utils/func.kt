package com.example.intourist.utils

import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.intourist.R
import com.example.intourist.retrofit.model.Category
import com.example.intourist.ui.filters.data.CategoryName
import com.example.intourist.ui.filters.data.Period
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

private val calendar = Calendar.getInstance()

@RequiresApi(Build.VERSION_CODES.M)
fun isNetworkAvailable(context: Context): Boolean {
    val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetwork
    return activeNetwork != null
}

fun isInternetAvailable(): Boolean {
    return try {
        val ipAddr: InetAddress = InetAddress.getByName("google.com")
        !ipAddr.equals("")
    } catch (e: java.lang.Exception) {
        false
    }
}

fun date(text: TextView) : DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text.text = sdf.format(calendar.time)
    }
}

fun currentData(): String {
    val myFormat = "dd.MM.yyyy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    return sdf.format(Date())
}

fun getThisWeek(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, +7)
    val date = calendar.time
    return sdf.format(date)
}

fun getNextWeek(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, +7)
    val date = calendar.time

    return sdf.format(date)
}

fun getNextWeek2(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, +14)
    val date = calendar.time

    return sdf.format(date)
}

fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun log(msg: String) {
    Log.v("LogMSG", msg)
}

fun spinnerCategory(context: Context, arrayList: ArrayList<CategoryName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
            context,
            R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}