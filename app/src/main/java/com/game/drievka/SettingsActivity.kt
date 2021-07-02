package com.game.drievka

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity() {

    val TAG = "STICK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var sPref = getSharedPreferences("game", Context.MODE_PRIVATE)
        buttonRepeatSecond.isActivated = sPref.getBoolean("activated2", false)
        buttonRepeatThird.isActivated = sPref.getBoolean("activated3", false)
        buttonRepeatFourth.isActivated = sPref.getBoolean("activated4", false)
        buttonRepeatFifth.isActivated = sPref.getBoolean("activated5", false)

        buttonRepeatFirst.setOnClickListener {
            setNewPref(1)
        }

        buttonRepeatSecond.setOnClickListener {
            if(it.isActivated) {
                setNewPref(2)
            }

        }

        buttonRepeatThird.setOnClickListener {
            if(it.isActivated) {
                setNewPref(3)
            }

        }

        buttonRepeatFourth.setOnClickListener {
            if(it.isActivated) {
                setNewPref(4)
            }
        }

        buttonRepeatFifth.setOnClickListener {
            if(it.isActivated) {
                setNewPref(5)
            }
        }

        imageButtonBackToMain.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

    }

    private fun setNewPref(game : Int) {
        var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
        sPref.edit().putInt("current_level" + game, 1).apply()
    }

    override fun onResume() {
        super.onResume()

        var sPref = getSharedPreferences("game", Context.MODE_PRIVATE)
        buttonRepeatSecond.isActivated = sPref.getBoolean("activated2", false)
        buttonRepeatThird.isActivated = sPref.getBoolean("activated3", false)
        buttonRepeatFourth.isActivated = sPref.getBoolean("activated4", false)
        buttonRepeatFifth.isActivated = sPref.getBoolean("activated5", false)
    }
}