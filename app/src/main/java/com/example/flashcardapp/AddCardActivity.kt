package com.example.flashcardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        //add click to go back to main activity
        findViewById<View>(R.id.exit_button).setOnClickListener{
            //dismiss add question activity
            finish()
        }
    }
}