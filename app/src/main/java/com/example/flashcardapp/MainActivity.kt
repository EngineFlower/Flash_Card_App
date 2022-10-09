package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        findViewById<View>(R.id.next_question_button).setOnClickListener {
            var currentCardDisplayedIndex = 0
            // advance our pointer index so we can show the next card
            currentCardDisplayedIndex++
            // don't try to go to next card if you have no cards to begin with
            if (allFlashcards.size == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }


            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.flash_question), // This should be the TextView for displaying your flashcard question
                    "You've reached the end of the cards, going back to start.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<TextView>(R.id.flash_answer).text = answer
            findViewById<TextView>(R.id.flash_question).text = question
        }
        if (allFlashcards.size > 0) {
            findViewById<TextView>(R.id.flash_question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.flash_answer).text = allFlashcards[0].answer
        }
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

                if (string1 != null && string2 != null){
                   flashcardDatabase.insertCard(Flashcard(string1, string2))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
                else{
                    Log.e("TAG", "Missing question or answer to input into database. Question is $string1 and answer is $string2")
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