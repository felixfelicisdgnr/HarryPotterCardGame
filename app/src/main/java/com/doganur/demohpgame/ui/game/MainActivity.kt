package com.doganur.demohpgame.ui.game

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.doganur.demohpgame.FirebaseUtils
import com.doganur.demohpgame.R
import com.doganur.demohpgame.SingleCard
import com.doganur.demohpgame.decodeFromBase64
import com.doganur.demohpgame.ui.SelectionScreenActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hard_level_multiply.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private var cards = mutableListOf<SingleCard>()
    private var indexOfSingleSelectedCard: Int? = null
    private var timerStarted = false
    private lateinit var db: FirebaseFirestore
    var mdplayer: MediaPlayer? = null
    var musicPlaying = false
    var score1 = 0
    var isMatched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBackgroundSound()
        db = FirebaseFirestore.getInstance()

        val timer = object : CountDownTimer(45000, 1000) { // 30 saniye süre, 1 saniye aralıklarla

            override fun onTick(millisUntilFinished: Long) {
                // her döngüde çalışacak kod buraya yazılır
                // millisUntilFinished değişkeni, kalan süreyi milisaniye cinsinden verir
                easySingleCountTimerTV.text = "Left : ${millisUntilFinished/1000}"
            }

            override fun onFinish() {
                 // buton etkin değil hale getirilir
                easySingleCountTimerTV.text = "Left : 0"
            }
        }
        stopButton4.setOnClickListener {
            if (musicPlaying) {
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                musicPlaying = false
            } else {
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 50)
                musicPlaying = true
            }
        }

        backButtonOne.setOnClickListener {

            val backSelectionIntent = Intent(this@MainActivity, SelectionScreenActivity::class.java)

            startActivity(backSelectionIntent)

        }

        buttons = listOf(easySingleImageBttn4, easySingleImageBttn, easySingleImageBttn2, easySingleImageBttn3)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i("DOGA", "Button Clicked!!")
                //Update Models
                updateModels(index)
                //Update the UI for the game
                updateViews()
                if(timerStarted == false){
                    timer.start()
                    timerStarted = true
                }
            }
        }

        getImagesFromFirebase()
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if(card.faceUp){
                card.image64?.let { button.decodeFromBase64(it) }
            } else{
                button.setImageResource(R.drawable.backcard)
            }
        }
    }
    fun playBackgroundSound(){
        mdplayer = MediaPlayer.create(this,R.raw.background)
        mdplayer!!.isLooping = true
        mdplayer!!.start()
    }
    fun playFailSound(){
        mdplayer = MediaPlayer.create(this,R.raw.fail)
        mdplayer!!.isLooping = false
        mdplayer!!.start()
    }
    fun playVictorySound(){
        mdplayer = MediaPlayer.create(this,R.raw.victory)
        mdplayer!!.isLooping = false
        mdplayer!!.start()
    }
    fun playMatchSound(){
        mdplayer = MediaPlayer.create(this,R.raw.match)
        mdplayer!!.isLooping = false
        mdplayer!!.start()
    }
    private fun updateModels(position: Int) {
        val card = cards[position]
        //error checking:
        if (card.faceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        if (indexOfSingleSelectedCard == null) {
            indexOfSingleSelectedCard = position
            Log.d("DOGA",position.toString())
            restoreCards()
        } else {
            //exactly 1 cards was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.faceUp = !card.faceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.faceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].name == cards[position2].name) {
            playMatchSound()
            Toast.makeText(this, "Match Found!!", Toast.LENGTH_SHORT).show()

            easySingleScoreTV.text=score1.toString()
            cards[position1].isMatched = true
            cards[position2].isMatched = true

            score1 +=((cards[position1].score?:0)*2*(cards[position1].houseScore?:0))//*(gecensüre/10)
            Log.i("DOGA", "Score1 is: " + score1.toString())
            easySingleScoreTV.text = "Player1: " + score1.toString()
            isMatched = true
        }else {
            score1 -=((cards[position1].score?:0)*2*(cards[position1].houseScore?:0))//*(kalansüre/10)
            Log.i("DOGA", "Score1 is: " + score1.toString())
            easySingleScoreTV.text = "Player1: " + score1.toString()
            isMatched = false
        }
    }

    private fun getImagesFromFirebase(){
        FirebaseUtils.getCardsFromCollectionRandom("Gryffindor", 2, true) {
            val newList = mutableListOf<SingleCard>()
            newList.addAll(it)
            newList.shuffle()
            cards = newList
            updateViews()
        }
    }
}


