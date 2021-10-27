package com.example.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegisterFriend : Activity() {

    lateinit var friends_list: Array<String>
    lateinit var birthdays_list: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_friend)

        friends_list = intent.getStringArrayExtra("friends_list") as Array<String>
        birthdays_list = intent.getStringArrayExtra("birthdays_list") as Array<String>

        initAddButton()
        initExitButton()

    }

    fun initExitButton() {
        val exit_button = findViewById<Button>(R.id.exit_button)
        exit_button.setOnClickListener { finish() }
    }

    fun initAddButton() {
        val add_button = findViewById<Button>(R.id.add_button)
        lateinit var name_entered: String
        lateinit var birthday_entered: String

        add_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                try {
                    name_entered = findViewById<EditText>(R.id.name_entered).text.toString()
                    birthday_entered = find_date()

                    friends_list = append(friends_list, name_entered)
                    birthdays_list = append(birthdays_list, birthday_entered)

                    val intent = Intent("com.example.registration")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    intent.putExtra("updated_names_list", friends_list)
                    intent.putExtra("updated_birthdays_list", birthdays_list)
                    setResult(1, intent)
                    finish()

                }catch (e: IOException) {
                    Log.d("Error", "One of the input-fields were empty. No information found")
                    e.printStackTrace()
                }
            }
        })
    }

    fun find_date(): String {
        val date_picker = findViewById<DatePicker>(R.id.date_picker)
        val day = date_picker.dayOfMonth
        val month = date_picker.month
        val year = date_picker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val date_converted = SimpleDateFormat("dd-MM-yyyy")
        val formated_date = date_converted.format(calendar.time)

        return formated_date
    }

    fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
}