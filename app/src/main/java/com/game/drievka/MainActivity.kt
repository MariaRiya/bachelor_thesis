package com.game.drievka

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var TAG = "MAIN"
    var width = 0
    var task1Level = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var sPref = getSharedPreferences("game", Context.MODE_PRIVATE)
        imageButtonSecondTask.isActivated = sPref.getBoolean("activated2", false)
        imageButtonThirdTask.isActivated = sPref.getBoolean("activated3", false)
        imageButtonFourthTask.isActivated = sPref.getBoolean("activated4", false)
        imageButtonFifthTask.isActivated = sPref.getBoolean("activated5", false)

        imageButtonFirstTask.setOnClickListener {
            var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
            sPref.edit().putInt("current_game", 1).apply()

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        imageButtonSecondTask.setOnClickListener {
            if(imageButtonSecondTask.isActivated) {
                var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
                sPref.edit().putInt("current_game", 2).apply()
                //           sPref.getInt("current_game", -1)

                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        imageButtonThirdTask.setOnClickListener {
            if(imageButtonThirdTask.isActivated) {
                var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
                sPref.edit().putInt("current_game", 3).apply()

                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        imageButtonFourthTask.setOnClickListener {
            if(imageButtonFourthTask.isActivated) {
                var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
                sPref.edit().putInt("current_game", 4).apply()

                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        imageButtonFifthTask.setOnClickListener {
            if(imageButtonFifthTask.isActivated) {
                var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
                sPref.edit().putInt("current_game", 5).apply()

                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        imageButtonTitle.setOnClickListener {
            var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
            sPref.edit().putInt("current_game", 0).apply()

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

//        imageButtonSettings.setOnClickListener{
//            task1Level = 1
//        }

        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        width = metricsB.widthPixels
        var height = metricsB.heightPixels

 //       Log.d(TAG, "width = " + width + " height = " + height)

        var imageWidth = width / 7
        var smallImageWidth = width / 15

//        imageButton2.width = imageWidth

//        imageButton2.layout(imageWidth, imageHeight*5, imageWidth*6, imageHeight*3)
//        imageButton2.layoutParams.height = imageHeight
        imageButtonFirstTask.layoutParams.width = imageWidth
        imageButtonSecondTask.layoutParams.width = imageWidth
        imageButtonThirdTask.layoutParams.width = imageWidth
        imageButtonFourthTask.layoutParams.width = imageWidth
        imageButtonFifthTask.layoutParams.width = imageWidth

//        buttonRepeatFirst.layoutParams.width = imageWidth
////        buttonRepeatSecond.layoutParams.width = imageWidth
////        buttonRepeatThird.layoutParams.width = imageWidth
////        buttonRepeatFourth.layoutParams.width = imageWidth
////        buttonRepeatFirst.layoutParams.width = imageWidth

 //       imageButtonSettings.layoutParams.width = smallImageWidth
        buttonSettings.layoutParams.width = smallImageWidth
        imageButtonTitle.layoutParams.height = imageWidth


        buttonSettings.setOnClickListener {
//            var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
//            sPref.edit().putInt("current_level1", 1).apply()
//            sPref.edit().putInt("current_level2", 1).apply()
////            sPref.edit().putInt("current_level3", 1).apply()
//            sPref.edit().putInt("current_level4", 1).apply()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

        }
    }


    override fun onResume() {
        super.onResume()
        var sPref = getSharedPreferences("game", Context.MODE_PRIVATE)
        imageButtonSecondTask.isActivated = sPref.getBoolean("activated2", false)
        imageButtonThirdTask.isActivated = sPref.getBoolean("activated3", false)
        imageButtonFourthTask.isActivated = sPref.getBoolean("activated4", false)
        imageButtonFifthTask.isActivated = sPref.getBoolean("activated5", false)
    }
}