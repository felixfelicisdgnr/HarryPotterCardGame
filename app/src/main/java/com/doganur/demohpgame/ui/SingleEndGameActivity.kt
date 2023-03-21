package com.doganur.demohpgame.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.doganur.demohpgame.R
import kotlinx.android.synthetic.main.activity_multi_end_game.*
import kotlinx.android.synthetic.main.activity_single_end_game.*

class SingleEndGameActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_end_game)
    }

    public fun showScore(score: Int){
        singleScoreTextView.text = score.toString()
    }
}