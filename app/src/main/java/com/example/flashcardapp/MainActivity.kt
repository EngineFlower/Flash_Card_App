package com.example.flashcardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Attaches click event to flash_answer
        findViewById<View>(R.id.flash_question).setOnClickListener{

            //Makes answer visible, after click
            findViewById<View>(R.id.flash_answer).visibility = View.VISIBLE

            //makes question invisible after click
            findViewById<View>(R.id.flash_question).visibility = View.INVISIBLE
        }
    }
}