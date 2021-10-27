package com.example.registration

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var venner: Array<String>
    private lateinit var bursdager: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        venner = resources.getStringArray(R.array.venner)
        bursdager = resources.getStringArray(R.array.bursdager)

        initExitButton()
        initSpinner()
        initRegisterButton()
        initEditButton()

    }

    fun initExitButton() {
        val exit_button = findViewById<Button>(R.id.exit_button)
        exit_button.setOnClickListener { finish() }
    }

    fun initSpinner() {
        val spinner = findViewById<Spinner>(R.id.venner_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, venner)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, valgt: View, posisjon: Int, id: Long) {
                findViewById<TextView>(R.id.beskrivelse).text =
                    bursdager[posisjon]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun initRegisterButton() {
        val register_button = findViewById<Button>(R.id.register_button)
        register_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent("com.example.registration.RegisterFriend")
                intent.putExtra("friends_list", venner)
                intent.putExtra("birthdays_list", bursdager)
                startActivityForResult(intent, 1)
            }
        })
    }

    fun initEditButton() {
        val edit_button = findViewById<Button>(R.id.edit_button)
        val selected_friend = findViewById<Spinner>(R.id.venner_spinner).selectedItem.toString()
        val selected_birthday = findViewById<TextView>(R.id.beskrivelse).text.toString()
        edit_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent("com.example.registration.EditFriend")
                intent.putExtra("friends_list", venner)
                intent.putExtra("birthdays_list", bursdager)
                intent.putExtra("selected_friend", selected_friend)
                intent.putExtra("selected_birthday", selected_birthday)
                startActivityForResult(intent, 2)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1) {
            if(data != null) {
                venner = data.getStringArrayExtra("updated_names_list") as Array<String>
                bursdager = data.getStringArrayExtra("updated_birthdays_list") as Array<String>
                initSpinner()
            }
        }else if(resultCode == 2) {
            if(data != null) {
                venner = data.getStringArrayExtra("updated_friends_list") as Array<String>
                bursdager = data.getStringArrayExtra("updated_birthdays_list") as Array<String>
                initSpinner()
            }
        }
    }

    inner class CustomDialogClass(context: Context) : android.app.Dialog(context) {

        lateinit var selected_friend: String
        lateinit var old_birthday: String

        init {
            setCancelable(false)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_edit)

            selected_friend = findViewById<Spinner>(R.id.venner_spinner).selectedItem.toString()
            Log.d("SELECTED FRIEND: ", selected_friend)
            old_birthday = findViewById<TextView>(R.id.beskrivelse).text.toString()
            Log.d("OLD BIRTHDAY: ", old_birthday)

            initDialog()
        }

        fun initDialog() {

            val new_name = findViewById<EditText>(R.id.edit_name).text.toString()
            val new_birthday = find_date()

            val updatedList = venner.mapIndexed { index, e -> if (index.equals(selected_friend)) new_name else e }
            val updatedBirthday = bursdager.mapIndexed { index, e -> if (index.equals(old_birthday)) new_birthday else e}

            venner = updatedList.toTypedArray()
            bursdager = updatedBirthday.toTypedArray()

            val confirm_changes = findViewById<Button>(R.id.confirm_button)
            val discard_changes = findViewById<Button>(R.id.discard_button)

            confirm_changes.setOnClickListener {
                initSpinner()
                dismiss()
            }
            discard_changes.setOnClickListener { dismiss() }
            show()
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
    }
}
