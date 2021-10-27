package com.example.registration

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditFriend : Activity() {

    lateinit var friends_list: Array<String>
    lateinit var birthdays_list: Array<String>
    lateinit var selected_friend: String
    lateinit var selected_birthday: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit)

        friends_list = intent.getStringArrayExtra("friends_list") as Array<String>
        birthdays_list = intent.getStringArrayExtra("birthdays_list") as Array<String>
        selected_friend = intent.getStringExtra("selected_friend").toString()
        selected_birthday = intent.getStringExtra("selected_birthday").toString()

        initDialog()
        initDiscardButton()

    }

    fun initDiscardButton() {
        val discard_button = findViewById<Button>(R.id.discard_button)
        discard_button.setOnClickListener { finish() }
    }

    fun initDialog() {

        val confirm_changes = findViewById<Button>(R.id.confirm_button)
        lateinit var new_name: String
        lateinit var new_birthday: String

        confirm_changes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val index_old_name = findIndex(friends_list, selected_friend)
                    val index_old_birthday = findIndex(birthdays_list, selected_birthday)

                    new_name = findViewById<EditText>(R.id.edit_name).text.toString()
                    new_birthday = find_date()

                    val updatedList = friends_list.mapIndexed { index, e -> if (index == index_old_name) new_name else e }
                    val updatedBirthday = birthdays_list.mapIndexed { index, e -> if (index == index_old_birthday) new_birthday else e}

                    friends_list = updatedList.toTypedArray()
                    birthdays_list = updatedBirthday.toTypedArray()

                    intent.putExtra("updated_friends_list", friends_list)
                    intent.putExtra("updated_birthdays_list", birthdays_list)
                    setResult(2, intent)
                    finish()
                }catch (e: IOException) {
                    Log.d("Error", "One of the input-fields were empty. No information found")
                    e.printStackTrace()
                }
            }
        })
    }

    fun find_date(): String {
        val date_picker = findViewById<DatePicker>(R.id.edit_birthday)
        val day = date_picker.dayOfMonth
        val month = date_picker.month
        val year = date_picker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val date_converted = SimpleDateFormat("dd-MM-yyyy")
        val formated_date = date_converted.format(calendar.time)

        return formated_date
    }

    fun findIndex(arr: Array<String>, name: String): Int {
        return arr.indexOf(name)
    }
}