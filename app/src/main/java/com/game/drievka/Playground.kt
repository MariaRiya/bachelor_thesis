package com.game.drievka

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.*


class Playground(internal var context: Context, attrs: AttributeSet )
    : View(context, attrs) {

    lateinit var gameState: GameState
    var TAG = "STICK"
    var drawSun = false
    var drawCloud = false
    lateinit var bitmapCloud : Bitmap
    lateinit var bitmapSun : Bitmap
    var bitmapX = 0f
    var bitmapY = 0f
    var inProcess = false

    init {
        var opt = BitmapFactory.Options()
        opt.inMutable = true
        bitmapCloud = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.cloud,
            opt
        )
        bitmapSun = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.sun,
            opt
        )

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        gameState.onDraw(canvas)

        if(drawCloud){
            canvas.drawBitmap(bitmapCloud, bitmapX, bitmapY, Paint() )

        }else if(drawSun){
            canvas.drawBitmap(bitmapSun, bitmapX, bitmapY, Paint() )

        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        pwidth = widthMeasureSpec.toFloat()
//        pheight = heightMeasureSpec.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        Log.d("STICK", "Width w= " + w + " height h=" + h)
        super.onSizeChanged(w, h, oldw, oldh)

        var resizedBitmap = Bitmap.createScaledBitmap(
            bitmapCloud, w / 3, h / 3, false
        )
        var resizedBitmap2 = Bitmap.createScaledBitmap(
            bitmapSun, w / 3, h / 3, false
        )

        bitmapCloud = resizedBitmap

        bitmapSun = resizedBitmap2

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.d(
//            TAG, "event.actionMasked " + event.actionMasked +
//                    "event.getPointerId(pointerIndex) " + event.getPointerId(0)
//        )

//        val pointerIndex = event.actionMasked -1
//        val pointerID = event.getPointerId(pointerIndex)
        val x = event.x
        val y = event.y
        if(gameState.currentGame > 2) {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return true
                }
                MotionEvent.ACTION_UP -> {
//                Log.d(TAG, "EVENT 1")
                    gameState.gridOfSticks.forEach {
                        if (it.isIn(x, y)) {
                            //                       Log.d(TAG, "CAN REMOVE " + gameState.canRemove)
                            if (it.selected && gameState.canRemove && it.canSelected) {
                                var ok = false
                                if ((gameState.currentGame == 4 && gameState.extraSticksCounter < gameState.countOfExtraSticksTask) || gameState.currentGame == 3 ||
                                    (gameState.currentGame == 5 && gameState.extraSticksCounter < gameState.countOfExtraSticksTask)
                                ) {
                                    ok = true
                                }
                                if (ok) {
                                    gameState.selectedSticks.remove(it)
                                    gameState.removeInArray(it)
                                    gameState.countSticks()
                                    gameState.allSticksCounter--
                                    it.selected = !it.selected
                                    if (gameState.currentGame == 4 || gameState.currentGame == 5) {
                                        gameState.extraSticksCounter++
                                        gameState.listOfExtraSticks.forEach {
                                            if (!it.selected && ok) {
                                                it.selected = true
                                                ok = false
                                            }
                                        }
                                        gameState.checkWin()
                                    }
                                }
                            } else if (!it.selected && gameState.canRemove && it.canSelected) {
                                var ok = true
                                if ((gameState.currentGame == 4 || gameState.currentGame == 5) && gameState.extraSticksCounter < 1) {
                                    ok = false
                                }
                                if ((gameState.sticksTask == 0 || (gameState.sticksTask > gameState.allSticksCounter && (gameState.currentGame == 3 || gameState.currentGame == 4) ) || (gameState.currentGame == 5)) && ok) {
                                    gameState.selectedSticks.add(it)
                                    gameState.allSticksCounter++
                                    it.selected = !it.selected
                                    if (gameState.currentGame == 4 || gameState.currentGame == 5) {
                                        gameState.extraSticksCounter--
                                        gameState.listOfExtraSticks.forEach {
                                            if (it.selected && ok) {
                                                it.selected = false
                                                ok = false
                                            }
                                        }
                                    }
                                    gameState.countShapes()
//                                gameState.countExtraSticksInShape()
                                }
                            }
//                        Log.d(TAG, "selected = " + it.selected)
                        }
                    }
                    /*               if(gameState.currentGame == 2){
                    gameState.listOfExtraSticks.forEach {
                        if(it.selected){

                        }
                    }
                }*/
//                Log.d(TAG, "extra sticks counter = " + gameState.extraSticksCounter)

                    invalidate()
                    //               if(triangle){
//                }else{
//                    countSquare()
//                }
                    return true
                }
            }
        }else{
            when (event.actionMasked) {
                0 -> {
//                Toast.makeText(context, " " + paths.size + " " + rects.size, Toast.LENGTH_SHORT).show()

//                Toast.makeText(context, "DOWN: " + sticks.size, Toast.LENGTH_SHORT).show()
                    if(!gameState.checkStickInSticksList(x,y)) {
                        var biggerRectF = Stick(
                            gameState.rectF.xLeft - gameState.rectF.rX,
                            gameState.rectF.yTop,
                            gameState.rectF.xRight + gameState.rectF.rX,
                            gameState.rectF.yBottom,
                            gameState.rectF.rX,
                            gameState.rectF.rY,
                            "0"
                        )
                        if (biggerRectF.isIn(x, y)) {
//                        Toast.makeText(context, "NEW ADDED", Toast.LENGTH_SHORT).show()
                            gameState.addNew(x, y)
                        }
                    }
                    invalidate()
                    return true
                    }
                2 -> {
                    gameState.moveStick(x,y)

                    invalidate()
                    return true
                }
                1 -> {
//                Toast.makeText(context, "UP: " + 1, Toast.LENGTH_SHORT).show()
                    gameState.cancelMove(x,y)

                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun clearPlayground() {
        if(gameState.canRemove) {
            if (gameState.currentGame == 3) {
                gameState.selectedSticks.forEach {
                    it.selected = false
                    it.inShape = false
                }
            }
            gameState.shape.clear()
            gameState.arrayOfShape.clear()
            gameState.forRemove.clear()
            gameState.selectedSticks.clear()
            gameState.shapesCounter = 0
            gameState.sticksCounter = 0
            gameState.allSticksCounter = 0
            if (gameState.currentGame == 4) {
                gameState.gridOfSticks.forEach {
                    if (!it.canSelected && it.selected) {
                        gameState.selectedSticks.add(it)
                    } else if (it.selected) {
                        it.selected = false
                    }
                }
                gameState.extraSticksCounter = gameState.countOfExtraSticksTask
                gameState.selectAllExtraSticks(true)
                gameState.countShapes()
                gameState.allSticksCounter = gameState.selectedSticks.size
            } else if (gameState.currentGame == 5) {
                gameState.gridOfSticks.forEach {
                    if (it.canSelected) {
                        if (!it.selected) {
                            it.selected = true
                        }
                        gameState.selectedSticks.add(it)
                    }
                }
                gameState.extraSticksCounter = 0
                gameState.selectAllExtraSticks(false)
                gameState.countShapes()
                gameState.allSticksCounter = gameState.selectedSticks.size


            }
            invalidate()
        }
        gameState.cleanSticks()
    }

    fun getCloudOrSun(): Boolean {
        if(!inProcess) {
            inProcess = true
            var sun = gameState.checkWin()
            GlobalScope.async(Dispatchers.Main) {
                bitmapX = width / 2f - bitmapCloud.width / 2
                bitmapY = 0f - bitmapCloud.height
                if(sun){
                    drawSun = true
                }else{
                    drawCloud = true
                }
                while (bitmapY < bitmapCloud.height / 4) {
                    bitmapY += 20
                    invalidate()
                    delay(50)
                }
                delay(100)
                while (bitmapY > 0 - bitmapCloud.height) {
                    bitmapY -= 20
                    invalidate()
                    delay(50)
                }
                drawCloud = false
                drawSun = false
                invalidate()
                inProcess = false
            this.cancel()
            }
            return sun
        }
        return false
    }

}