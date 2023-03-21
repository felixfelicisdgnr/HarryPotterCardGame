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
import kotlinx.android.synthetic.main.activity_easy_level_multiply.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mid_level_multiply.*

class MidLevelMultiplyActivity : AppCompatActivity() {
    private lateinit var buttons: List<ImageButton>
    private var cards = mutableListOf<SingleCard>()
    private var indexOfSingleSelectedCard: Int? = null
    var score1 = 0
    var score2 = 0
    var turn = 0
    var isMatched = false
    private var timerStarted = false
    var musicPlaying = false
    var mdplayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mid_level_multiply)
        playBackgroundSound()

        stopButton6.setOnClickListener {
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

            val timer = object : CountDownTimer(60000, 1000) { // 30 saniye süre, 1 saniye aralıklarla

            override fun onTick(millisUntilFinished: Long) {
                // her döngüde çalışacak kod buraya yazılır
                // millisUntilFinished değişkeni, kalan süreyi milisaniye cinsinden verir
                countTimerMLMTV.text = "Left : ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                // buton etkin değil hale getirilir
                countTimerMLMTV.text = "Left : 0"
            }

        }

            backButtonMidLevelMultiply.setOnClickListener {

            val backSelectionIntent =
                Intent(this@MidLevelMultiplyActivity, SelectionScreenActivity::class.java)

            startActivity(backSelectionIntent)

        }


        buttons = listOf(
            midMImageButton24,
            midMImageButton25,
            midMImageButton26,
            midMImageButton27,
            midMImageButton30,
            midMImageButton31,
            midMImageButton32,
            midMImageButton33,
            midMImageButton36,
            midMImageButton37,
            midMImageButton38,
            midMImageButton39,
            midMImageButton42,
            midMImageButton43,
            midMImageButton44,
            midMImageButton45,
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i("DOGA", "Button clicked!")
                updateModels(index)
                updateViews()

                if (timerStarted == false) {
                    timer.start()
                    timerStarted = true
                }
            }
        }
        getImagesFromFirebase()
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
    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.faceUp) {
                card.image64?.let { button.decodeFromBase64(it) }
            } else {
                button.setImageResource(R.drawable.backcard)
            }
        }
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
            restoreCards()
        } else {
            //exactly 1 cards was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.faceUp = !card.faceUp
    }

    private fun restoreCards() {
        Log.i("DOGA", "restoreCards isMatched: " + isMatched.toString())
        for (card in cards) {
            if (!card.isMatched) {
                card.faceUp = false
            }
        }
        if (turn == 2) {
            turn = 1
            midMUserTextView.text = "Kullanıcı: " + turn.toString()
        } else if (turn == 1) {
            turn = 2
            midMUserTextView.text = "Kullanıcı: " + turn.toString()
        }
        if (turn == 0) {
            turn = 1
            midMUserTextView.text = "Kullanıcı: " + turn.toString()
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].name == cards[position2].name) {
            playMatchSound()
            Toast.makeText(this, "Match found!", Toast.LENGTH_SHORT).show()

            midMPlayer1ScoreTV.text = score1.toString()
            cards[position1].isMatched = true
            cards[position2].isMatched = true

            if (turn == 1) {
                score1 += ((cards[position1].score ?: 0) * 2 * (cards[position1].houseScore
                    ?: 0))//*(gecensüre/10)
                Log.i("DOGA", "Score1 is: " + score1.toString())
                midMPlayer1ScoreTV.text = "Player1: " + score1.toString()
            } else if (turn == 2) {
                score2 += ((cards[position1].score ?: 0) * 2 * (cards[position1].houseScore
                    ?: 0))//*(gecensüre/10)
                Log.i("DOGA", "Score2 is: " + score2.toString())
                midMPlayer2ScoreTV.text = "Player2: " + score2.toString()
            }
            isMatched = true
        } else {
            if (turn == 1) {
                score1 -= ((cards[position1].score ?: 0) * 2 * (cards[position1].houseScore
                    ?: 0))//*(kalansüre/10)
                Log.i("DOGA", "Score1 is: " + score1.toString())
                midMPlayer1ScoreTV.text = "Player1: " + score1.toString()
            } else if (turn == 2) {
                score2 -= ((cards[position1].score ?: 0) * 2 * (cards[position1].houseScore
                    ?: 0))//*(gecensüre/10)
                Log.i("DOGA", "Score2 is: " + score2.toString())
                midMPlayer2ScoreTV.text = "Player2: " + score2.toString()
            }
            isMatched = false
        }
    }

    private fun startMusic() {
        // Arkaplandaki müziği çalmak için MediaPlayer nesnesi oluşturun
        val backgroundMediaPlayer = MediaPlayer.create(this, R.raw.background)

        // Arkaplandaki müziği döngü halinde çalmasını sağlayın
        backgroundMediaPlayer.isLooping = true

        // Arkaplandaki müziği çalmaya başlayın
        backgroundMediaPlayer.start()
    }


    private fun getImagesFromFirebase() {
        fun hufflepuff() {
            FirebaseUtils.getCardsFromCollectionRandom("Hufflepuff", 2, true) {
                cards.addAll(it)
                cards.shuffle()
                updateViews()
                Toast.makeText(this, "Loading Done!", Toast.LENGTH_SHORT).show()
            }
        }

        fun ravenclaw() {
            FirebaseUtils.getCardsFromCollectionRandom("Ravenclaw", 2, true) {
                cards.addAll(it)
                hufflepuff()
            }
        }

        fun slytherin() {
            FirebaseUtils.getCardsFromCollectionRandom("Slytherin", 2, true) {
                cards.addAll(it)
                ravenclaw()
            }
        }

        fun gryffindor() {
            FirebaseUtils.getCardsFromCollectionRandom("Gryffindor", 2, true) {
                cards.addAll(it)
                slytherin()
            }
        }
        gryffindor()
    }
}