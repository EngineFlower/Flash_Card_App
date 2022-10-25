package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import kotlin.math.hypot
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    private var allFlashcards = mutableListOf<Flashcard>()
    var countDownTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        countDownTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.timer).text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {}
        }
        fun startTimer() {
            countDownTimer?.cancel()
            countDownTimer?.start()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        var currentCardDisplayedIndex = 0
        if (allFlashcards.size > 0) {
            findViewById<TextView>(R.id.flash_question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.flash_answer).text = allFlashcards[0].answer

        }
        findViewById<View>(R.id.next_question_button).setOnClickListener {
            startTimer()
            val leftOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_in)
            leftOutAnim.setAnimationListener(object :Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    findViewById<View>(R.id.flash_question).startAnimation(leftOutAnim)
                }

                override fun onAnimationEnd(animation: Animation?) {
                }
                override fun onAnimationRepeat(animation: Animation?) {

                }
            })


            // advance our pointer index so we can show the next card
            currentCardDisplayedIndex ++
            // don't try to go to next card if you have no cards to begin with
            if (allFlashcards.size == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }


            if(currentCardDisplayedIndex > allFlashcards.size) {
            Snackbar.make(
                findViewById<TextView>(R.id.flash_question), // This should be the TextView for displaying your flashcard question
                "You've reached the end of the cards, going back to start.",
                Snackbar.LENGTH_SHORT).show()
                currentCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<TextView>(R.id.flash_answer).text = answer
            findViewById<TextView>(R.id.flash_question).text = question
            findViewById<View>(R.id.flash_question).startAnimation(leftOutAnim)
            findViewById<View>(R.id.flash_question).startAnimation(rightInAnim)


            }


        //Attaches click event to question

        findViewById<View>(R.id.flash_question).setOnClickListener{
            val answerSideView = findViewById<View>(R.id.flash_answer)


            val cx = answerSideView.width / 2
            val cy = answerSideView.width / 2

            val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)
            //Makes answer visible, after clicking question
            findViewById<View>(R.id.flash_answer).visibility = View.VISIBLE

            //makes question invisible after click
            findViewById<View>(R.id.flash_question).visibility = View.INVISIBLE

            anim.duration = 1000
            anim.start()
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
            val extras = data?.extras
            if (extras != null) { // Check that we have data returned
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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)




        }

    }

}