package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        //add click to go back to main activity
        findViewById<View>(R.id.exit_button).setOnClickListener{
            //dismiss add question activity
            finish()
        }
        //click listener for save button
        findViewById<View>(R.id.save_button).setOnClickListener{

            //Creating a new intent
            val data = Intent()
            //puts new question into the Intent
            data.putExtra("add_question", findViewById<EditText>(R.id.add_question).text.toString())
            //puts answer into Intent
            data.putExtra("answer", findViewById<EditText>(R.id.add_answer).text.toString())

            setResult(RESULT_OK, data)
            //dismiss add card activity
            finish()
        }

    }
}