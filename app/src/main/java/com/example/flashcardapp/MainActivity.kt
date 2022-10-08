package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

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
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                val string1 = data.getStringExtra("add_question") // 'string1' needs to match the key we used when we put the string in the Intent
                val string2 = data.getStringExtra("answer")

                // Log the value of the strings for easier debugging
                val questionText = findViewById<TextView>(R.id.flash_question)
                questionText.text = string1

                val answerText = findViewById<TextView>(R.id.flash_answer)
                answerText.text = string2

                if (!string1.isNullOrEmpty() && !string2.isNullOrEmpty()){
                   //TODO: FlashcardDatabase.insertCard(Flashcard(string1, string2))
                }



            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }
        }

        //Adds click listener that takes user to add_card_activity
        findViewById<View>(R.id.add_question_button).setOnClickListener{ val intent = Intent(this, AddCardActivity::class.java)
                resultLauncher.launch(intent)


        }

    }

}