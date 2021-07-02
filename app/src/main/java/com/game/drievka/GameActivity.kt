package com.game.drievka

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.roundToInt

class GameActivity : AppCompatActivity() {

    var started = true
    lateinit var gameState : GameState
    val TAG = "STICK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val points = listOf<TextView>(
            point1,
            point2,
            point3,
            point4,
            point5,
            point6,
            point7,
            point8,
            point9,
            point10
        )


        imageButtonBack.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

        imageButtonOK.setOnClickListener {
            if(it.isActivated) {
                it.isActivated = false
                if(gameState.currentLevel == 10 && gameState.currentGame != 5){
                    var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
                    sPref.edit().putInt("current_game", gameState.currentGame+1).apply()
                }
                this.recreate()
            }else {
 //               if (gameState.win) {
                    if(imageView2.getCloudOrSun()) {
                        it.isActivated = true
                    }
  //              }else{
  //                  imageView2.getCloudOrSun(false)
  //              }
            }
        }


        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        var width = metricsB.widthPixels
        var height = metricsB.heightPixels

        imageButtonOK.layoutParams.width = width / 15
        imageButtonOK.layoutParams.height = width / 15

        imageButtonRepeat.layoutParams.width = width / 15
        imageButtonRepeat.layoutParams.height = width / 15

        imageButtonBack.layoutParams.width = width / 15
        imageButtonBack.layoutParams.height = width / 15

        task.layoutParams.width = width / 15 *12
        task.layoutParams.height = (height * 0.15).roundToInt()


        imageView2.layoutParams.width = (width * 0.9).roundToInt()
        imageView2.layoutParams.height = (height * 0.75).roundToInt()

        imageViewCloud.layoutParams.width = (width * 0.3).roundToInt()
        imageViewCloud.layoutParams.height = (height * 0.3).roundToInt()


/*        Log.d(
            TAG,
            "SIZE = " + imageView2.layoutParams.width.toFloat() + " " + imageView2.layoutParams.height.toFloat()
        )*/
        gameState = GameState(
            this,
            imageView2.layoutParams.width.toFloat(),
            imageView2.layoutParams.height.toFloat(),
            imageView2
        )

        task.gameState = gameState

        imageView2.gameState = gameState

        imageButtonRepeat.setOnClickListener {
            imageView2.clearPlayground()

        }

        var sPref = this.getSharedPreferences("game", Context.MODE_PRIVATE)
        var currentGame = sPref.getInt("current_game", 1)
        Log.d(TAG, " Current game = " + currentGame)
        var currentLevel = sPref.getInt("current_level" + currentGame, 1)

        if(currentGame == 0){
            imageButtonOK.visibility = View.INVISIBLE
            view2.visibility = View.INVISIBLE
        }

        for(i in 0..points.size-1){
            if(i < currentLevel-1){
                points[i].setBackgroundResource(R.drawable.style_points_done)
            }else{
                points[i].setBackgroundResource(R.drawable.style_points)

            }
        }


//        val handler = Handler()
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                if (gameState.win) {
// //                   imageButtonOK.isActivated = true
//                    if(currentLevel == 8 ) {
//                        if(currentGame == 1) {
//                            sPref.edit().putBoolean("activated2", true).apply()
//                        }else if(currentGame == 2){
//                            sPref.edit().putBoolean("activated3", true).apply()
//                        }else if(currentGame == 3){
//                            sPref.edit().putBoolean("activated4", true).apply()
//                        }else if(currentGame == 4){
//                            sPref.edit().putBoolean("activated5", true).apply()
//                        }
//                    }
//                }
//
//                handler.postDelayed(this, 1000)
//            }
//        }, 1000)

        val handler2 = Handler()

        handler2.postDelayed(object : Runnable {
            override fun run() {
                if(currentGame > 2) {
                    var update = false
                    if (task.square != gameState.square) {
                        task.square = gameState.square
                    }
                    if (task.usedSticks != gameState.sticksCounter) {
                        task.usedSticks = gameState.sticksCounter
                        update = true
                    }
                    if (task.createdShapes != gameState.shapesCounter) {
                        task.createdShapes = gameState.shapesCounter
                        update = true
                    }
                    if (update) {
                        task.invalidate()
                    }

                    handler2.postDelayed(this, 1000)
                }
            }
        }, 1000)

    }



    override fun onPause() {
        super.onPause()
//        imageView2.activityPaused()
        //Log.d("STICK", " pause w=" + otec.width + " h=" + otec.height)
    }


}