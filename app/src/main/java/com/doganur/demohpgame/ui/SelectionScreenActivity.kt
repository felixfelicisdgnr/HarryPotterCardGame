package com.doganur.demohpgame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.doganur.demohpgame.R
import com.doganur.demohpgame.ui.game.*
import kotlinx.android.synthetic.main.activity_selection_screen.*

class SelectionScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_screen)


        startGameButton.setOnClickListener {

            val easySingleIntent = Intent(this@SelectionScreenActivity, MainActivity::class.java)
            val midSingleIntent  = Intent(this@SelectionScreenActivity, MidLevelGameActivity::class.java)
            val hardSingleIntent = Intent(this@SelectionScreenActivity, HardLevelGameActivity::class.java)

            val easyMultiplayerIntent = Intent(this@SelectionScreenActivity,
                EasyLevelMultiplyActivity::class.java)
            val midMultiplayerIntent = Intent(this@SelectionScreenActivity,
                MidLevelMultiplyActivity::class.java)
            val hardMultiplayerIntent = Intent(this@SelectionScreenActivity,
                HardLevelMultiplyActivity::class.java)

            if (easyRadioButton.isChecked && singlePlayerRadioButton.isChecked) {

                startActivity(easySingleIntent)

            } else if (midRadioButton.isChecked && singlePlayerRadioButton.isChecked) {

                startActivity(midSingleIntent)

            } else if (hardRadioButton.isChecked && singlePlayerRadioButton.isChecked) {

                startActivity(hardSingleIntent)

            } else if (easyRadioButton.isChecked && multiplayerRadioButton.isChecked)  {

                startActivity(easyMultiplayerIntent)
            }
            else if (midRadioButton.isChecked && multiplayerRadioButton.isChecked) {

                startActivity(midMultiplayerIntent)
            }
            else if (hardRadioButton.isChecked && multiplayerRadioButton.isChecked) {

                startActivity(hardMultiplayerIntent)
            }
            else {

                Toast.makeText(this, "Please choose a level !!!", Toast.LENGTH_LONG).show()

            }


           }
        }
}