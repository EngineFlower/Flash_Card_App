package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Attaches click event to question
        findViewById<View>(R.id.flash_question).setOnClickListener{

            //Makes answer visible, after clicking question
            findViewById<View>(R.id.flash_answer).visibility = View.VISIBLE

            //makes question invisible after click
            findViewById<View>(R.id.flash_question).visibility = View.INVISIBLE
        }
        //Attaches click to answer
        findViewById<View>(R.id.flash_answer).setOnClickListener{
            //Makes question visible again
            findViewById<View>(R.id.flash_question).visibility = View.VISIBLE

            //Makes answer invisible
            findViewById<View>(R.id.flash_answer).visibility = View.INVISIBLE
        }

        //Adds click listener that takes user to add_card_activity
        findViewById<View>(R.id.add_question_button).setOnClickListener{
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)



        }
    }

}