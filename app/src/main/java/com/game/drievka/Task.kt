package com.game.drievka

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Task(internal var context: Context, attrs: AttributeSet)
    : View(context, attrs) {


        lateinit var gameState : GameState

        var usedSticks = 0
            get() {
                return field
            }
            set(value) { field = value}
        var allSticks = 16
            get() {
                return field
            }
            set(value) { field = value}
        var createdShapes = 0
            get() {
                return field
            }
            set(value) { field = value}
        var allShapes = 5
            get() {
                return field
            }
            set(value) { field = value}
        var square = false
        get() {
            return field
        }
        set(value) { field = value}
        var pwidth = 0f
        var pheight = 0f
        val TAG = "TASK"
        var radX = 0f
        var radY = 0f
        var rectf = RectF()
        val round = 20F
        var paintFill = Paint()
        var paintStroke = Paint()
        var paintYellowStroke = Paint()
        var path = Path()
        var pathCenterX = 0f
        var pathCenterY = 0f



    init{
        paintFill = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            textSize = 120F
        }
        paintStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = 5f
            textSize = 120F
        }

        paintYellowStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.YELLOW
            strokeWidth = 20f
            textSize = 120F
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(gameState.currentGame > 2) {
            if (gameState.sticksTask > 0 && gameState.currentGame == 3) {
                canvas.drawRoundRect(rectf, round, round, paintFill)
                canvas.drawRoundRect(rectf, round, round, paintStroke)

                canvas.drawText(
                    "" + gameState.sticksCounter + "/" + gameState.sticksTask,
                    rectf.right + radX,
                    rectf.centerY(),
                    paintFill
                )
                canvas.drawText(
                    "" + gameState.sticksCounter + "/" + gameState.sticksTask,
                    rectf.right + radX,
                    rectf.centerY(),
                    paintStroke
                )
            }
            if (gameState.square) {
                canvas.drawRect(
                    rectf.right + radY * 2,
                    radX / 2,
                    rectf.right + radY * 3,
                    radX / 2 + radY,
                    paintYellowStroke
                )
            } else {
                canvas.drawPath(path, paintYellowStroke)
            }
            canvas.drawText(
                "" + gameState.shapesCounter + "/" + gameState.shapesTask,
                rectf.right + radY * 3 + radX,
                rectf.centerY(),
                paintFill
            )
            canvas.drawText(
                "" + gameState.shapesCounter + "/" + gameState.shapesTask,
                rectf.right + radY * 3 + radX,
                rectf.centerY(),
                paintStroke
            )
        }else if(gameState.currentGame == 2){
            val mat = Matrix()
            mat.setRotate(-90f, pathCenterX, pathCenterY)
            path.transform(mat)
            canvas.drawPath(path, paintYellowStroke)

            var r = RectF()
            path.computeBounds(r, true)

            canvas.drawLine(r.right + gameState.rectF.rX, r.top -gameState.rectF.rX*2 , r.right + gameState.rectF.rX, r.bottom + gameState.rectF.rX*2, paintStroke)

            mat.reset()
            mat.setRotate(180f, pathCenterX, pathCenterY)
            path.transform(mat)

            mat.reset()
            mat.setTranslate(r.right/2.75f,0f)
            path.transform(mat)

            canvas.drawPath(path, paintYellowStroke)
        }
        else if(gameState.currentGame ==1 ){
            canvas.drawPath(path, paintYellowStroke)
            canvas.drawLine(rectf.right + radY * 3 + radX, radX /2 , rectf.right + radY * 3 + radX, radX /2 + radY + gameState.rectF.rX*2, paintStroke)

            val mat = Matrix()
            mat.reset()
            mat.setTranslate(radX*7,0f)
            path.transform(mat)

            canvas.drawPath(path, paintYellowStroke)

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pwidth = w.toFloat()
        pheight = h.toFloat()

        radY = pheight * 0.8f
        radX = pwidth * 0.02f

        rectf = RectF(radX, radX / 2, radX + radX, radX + radY)

        val p1 = PointF(rectf.right + radY * 2, radX / 2 + radY)
        val p2 = PointF(rectf.right + radY * 2.5f, radX/2)
        val p3 = PointF(rectf.right + radY * 3, radX / 2 + radY)

//            path.setFillType(Path.FillType.EVEN_ODD)
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.close()

        pathCenterY = radX / 2 + radY /2
        pathCenterX = rectf.right + radY * 2.5f

        paintFill.textSize = pheight /2
        paintStroke.textSize = pheight /2
        paintFill.strokeWidth = pheight / 50
        paintStroke.strokeWidth = pheight / 50
        paintYellowStroke.strokeWidth = pheight / 25



    }




}