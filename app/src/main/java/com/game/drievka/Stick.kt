package com.game.drievka

import android.graphics.*
import android.util.Log
import androidx.core.graphics.transform

data class Stick (var xLeft : Float, var yTop : Float,
             var xRight : Float, var yBottom : Float, val rX : Float, val rY : Float, var id : String)  {

    var rotateMode = false
    var selected = false
//    val distance = 3F
    var previousEventX = 0F
    var previousEventY = 0F
    var isMove = false
    var angle = 0F
    var rotated = false;
    var mat = Matrix()
    private var path = Path()
    var fpx = 0f
    var fpy = 0f
    var spx = 0f
    var spy = 0f
    var newfpx = 0f
    var newfpy = 0f
    var newspx = 0f
    var newspy = 0f
    var xlt = xLeft
    var xrt = xRight
    var ylt = yTop
    var yrt = yTop
    var xlb = xLeft
    var xrb = xRight
    var ylb = yBottom
    var yrb = yBottom
    var arrayOfPoints = floatArrayOf(xlt, ylt, xrt, yrt, xlb, ylb, xrb, yrb)
    var moved = false
    var rectf = RectF(xLeft,yTop,xRight,yBottom)
    var centerX = rectf.centerX()
    var centerY = rectf.centerY()
    var neighborhoods  = emptyArray<Stick>().toMutableList()
    var upperNeighbor  : Stick? = null
    var inShape = false
    var right : Stick ? = null
    var upper : Stick ? = null
    var lower : Stick ? = null
    var rightSecond : Stick ? = null
    var canSelected = true
    var paint = Paint()
    var indexInShape = -1

//    var arrayOfPoints = floatArrayOf(xLeft, yTop, xRight, yTop, xLeft, yBottom, xRight, yBottom)

    val TAG = "STICK"

    fun getRectF() : RectF{
        return rectf
//        return RectF(xlt, ylt, xrb, yrb)
    }

    fun moveStarted(eventX : Float, eventY : Float){
        moved = false
        isMove = true
        previousEventX = eventX
        previousEventY = eventY
        fpx = eventX
        fpy = eventY
//        Log.d()
    }

    fun move(dX : Float, dY : Float){
        if(isMove){
//            var r = RectF()
            mat.reset()
            mat.setTranslate(dX - previousEventX, dY - previousEventY)
            rectf.transform(mat)
            path.transform(mat)
//            path.computeBounds(r, true)
            mat.mapPoints(arrayOfPoints)
            previousEventX = dX
            previousEventY = dY
            centerX = rectf.centerX()
            centerY = rectf.centerY()
            translateXY()
            moved = true
        }
    }

    fun moveEnded(){
        isMove = false
        previousEventX = 0F
        previousEventY = 0F
        fpx = 0f
        fpy = 0f
        spx = 0f
        spy = 0f
        newfpx = 0f
        newfpy = 0f
        newspx = 0f
        newspy = 0f
        moved = false

    }


    fun translateXY(){
        xlt = arrayOfPoints[0]
        ylt = arrayOfPoints[1]
        xrt = arrayOfPoints[2]
        yrt = arrayOfPoints[3]
        xlb = arrayOfPoints[4]
        ylb = arrayOfPoints[5]
        xrb = arrayOfPoints[6]
        yrb = arrayOfPoints[7]
    }

    fun rotate(selectedAngle : Float) {
            var angleNew = selectedAngle - angle
            if(angleNew < 0){
                angleNew = 360 + angleNew
            }
            mat.reset()
            mat.setRotate(angleNew, centerX, centerY)
            path.transform(mat)
            mat.mapPoints(arrayOfPoints)
 //           Log.d(TAG, "angle = " + angleNew)
            rotateXY(selectedAngle)

    }

    fun rotateXY(selectedAngle: Float){
//        Log.d(TAG, "xlt=" + xlt +" ylt=" + ylt + " xrt=" + xrt + " yrt=" + yrt + " xlb=" + xlb + " ylb=" + ylb + " xrb=" + xrb + " yrb=" + yrb)
        xlt = arrayOfPoints[0]
        ylt = arrayOfPoints[1]
        xrt = arrayOfPoints[2]
        yrt = arrayOfPoints[3]
        xlb = arrayOfPoints[4]
        ylb = arrayOfPoints[5]
        xrb = arrayOfPoints[6]
        yrb = arrayOfPoints[7]
//        Log.d(TAG, "xlt=" + xlt +" ylt=" + ylt + " xrt=" + xrt + " yrt=" + yrt + " xlb=" + xlb + " ylb=" + ylb + " xrb=" + xrb + " yrb=" + yrb)
        if(selectedAngle == 270f && angle != 270f){
            xlt = arrayOfPoints[0]
            ylt = arrayOfPoints[3]
            xrt = arrayOfPoints[4]
            yrt = arrayOfPoints[3]
            xlb = arrayOfPoints[0]
            ylb = arrayOfPoints[1]
            xrb = arrayOfPoints[4]
            yrb = arrayOfPoints[1]
        }else if (angle == 270f && selectedAngle != 270f){
            xlt = arrayOfPoints[4]
            ylt = arrayOfPoints[5]
            xrt = arrayOfPoints[0]
            yrt = arrayOfPoints[1]
            xlb = arrayOfPoints[6]
            ylb = arrayOfPoints[7]
            xrb = arrayOfPoints[2]
            yrb = arrayOfPoints[3]
        }
        angle = selectedAngle
        updateCoordinatesInArray()
    }

    fun updateCoordinatesInArray(){
        arrayOfPoints[0] = xlt
        arrayOfPoints[1] = ylt
        arrayOfPoints[2] = xrt
        arrayOfPoints[3] = yrt
        arrayOfPoints[4] = xlb
        arrayOfPoints[5] = ylb
        arrayOfPoints[6] = xrb
        arrayOfPoints[7] = yrb
//        Log.d(TAG, "xlt=" + xlt +" ylt=" + ylt + " xrt=" + xrt + " yrt=" + yrt + " xlb=" + xlb + " ylb=" + ylb + " xrb=" + xrb + " yrb=" + yrb)

    }



    fun isIn(eventX : Float, eventY : Float ) : Boolean =
        pointInTriangle(xlt, ylt, xrt, yrt, xrb, yrb, eventX, eventY) ||
                pointInTriangle(xlt, ylt, xlb, ylb, xrb, yrb, eventX, eventY)

    fun pointInTriangle(x1 : Float, y1 : Float, x2 : Float, y2: Float, x3 : Float, y3 : Float, x0 : Float, y0 : Float): Boolean {
        var z1 = (x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0)
        var z2 = (x2 - x0) * (y3 - y2) - (x3 - x2) * (y2 - y0)
        var z3 = (x3 - x0) * (y1 - y3) - (x1 - x3) * (y3 - y0)
        return z1 >= 0 && z2 >= 0 && z3 >= 0 || z1 <= 0 && z2 <= 0 && z3 <= 0
    }


    fun setPath(p: Path) {
        path = p
    }

    fun getPath() : Path{
        return path
    }


    fun isNear(stick : Stick, listOfSticks : MutableList<Stick>) : FloatArray {
        var r = RectF()

        var thisrt = RectF(xrt - rX, yrt - rX, xrt + rX, yrt + rX)

        var thisrb = RectF(xrb - rX, yrb - rX, xrb + rX, yrb + rX)

        var thislt = RectF(xlt - rX, ylt - rX, xlt + rX, ylt + rX)

        var thislb = RectF(xlb - rX, ylb - rX, xlb + rX, ylb + rX)

        var otherrt = RectF(stick.xrt - rX, stick.yrt - rX, stick.xrt + rX, stick.yrt + rX)

        var otherrb = RectF(stick.xrb - rX, stick.yrb - rX, stick.xrb + rX, stick.yrb + rX)

        var otherlt = RectF(stick.xlt - rX, stick.ylt - rX, stick.xlt + rX, stick.ylt + rX)

        var otherlb = RectF(stick.xlb - rX, stick.ylb - rX, stick.xlb + rX, stick.ylb + rX)

        var dx = 0f
        var dy = 0f
        var gravityTrue = false
        var distan = 99999.0
        var newDistan = 0.0
        if (RectF.intersects(thisrt, otherlt)){
            Log.d(TAG, "CONTAINS RT + LT")

//            gravityTrue = true
            newDistan = distance(xrt.toDouble(), yrt.toDouble(), stick.xlt.toDouble(), stick.ylt.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thisrt, otherlt)){
                    distan = newDistan
                    dx = otherlt.centerX() - thisrt.centerX()
                    dy = otherlt.centerY() - thisrt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thisrt, otherlb)){
            Log.d(TAG, "CONTAINS RT + LB")
//            gravityTrue = true
            newDistan = distance(xrt.toDouble(), yrt.toDouble(), stick.xlb.toDouble(), stick.ylb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thisrt, otherlb)){
                    distan = newDistan
                    dx = otherlb.centerX() - thisrt.centerX()
                    dy = otherlb.centerY() - thisrt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thisrt, otherrb)){
            Log.d(TAG, "CONTAINS RT + RB")

//            gravityTrue = true
            newDistan = distance(xrt.toDouble(), yrt.toDouble(), stick.xrb.toDouble(), stick.yrb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thisrt, otherrb)){
                    distan = newDistan
                    dx = otherrb.centerX() - thisrt.centerX()
                    dy = otherrb.centerY() - thisrt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thislt, otherrb)) {
            Log.d(TAG, "CONTAINS LT + RB")

//            gravityTrue = true
            newDistan = distance(xlt.toDouble(), ylt.toDouble(), stick.xrb.toDouble(), stick.yrb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thislt, otherrb)){
                    distan = newDistan
                    dx = otherrb.centerX() - thislt.centerX()
                    dy = otherrb.centerY() - thislt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thislt, otherrt)) {
            Log.d(TAG, "CONTAINS LT + RT")

//            gravityTrue = true
            newDistan = distance(xlt.toDouble(), ylt.toDouble(), stick.xrt.toDouble(), stick.yrt.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thislt, otherrt)){
                    distan = newDistan
                    dx = otherrt.centerX() - thislt.centerX()
                    dy = otherrt.centerY() - thislt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thislt, otherlb)) {
            Log.d(TAG, "CONTAINS LT + LB")

            newDistan = distance(xlt.toDouble(), ylt.toDouble(), stick.xlb.toDouble(), stick.ylb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thislt, otherlb)){
                    //                gravityTrue = true
                    distan = newDistan
                    dx = otherlb.centerX() - thislt.centerX()
                    dy = otherlb.centerY() - thislt.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thislb, otherrb)) {
            Log.d(TAG, "CONTAINS LB + RB")

            newDistan = distance(xlb.toDouble(), ylb.toDouble(), stick.xrb.toDouble(), stick.yrb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thislb, otherrb)){
//                gravityTrue = true
                    distan = newDistan
                    dx = otherrb.centerX() - thislb.centerX()
                    dy = otherrb.centerY() - thislb.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thislb, otherrt)) {
            Log.d(TAG, "CONTAINS LB + RT")

            newDistan = distance(xlb.toDouble(), ylb.toDouble(), stick.xrt.toDouble(), stick.yrt.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thislb, otherrt)){
//                gravityTrue = true
                    distan = newDistan
                    dx = otherrt.centerX() - thislb.centerX()
                    dy = otherrt.centerY() - thislb.centerY()
                }
            }
        }
         if (!gravityTrue && RectF.intersects(thislb, otherlt)) {
            Log.d(TAG, "CONTAINS LB + LT")

             newDistan = distance(xlb.toDouble(), ylb.toDouble(), stick.xlt.toDouble(), stick.ylt.toDouble())
             if (newDistan < distan){
                 if(checkIntersectsWithAll(listOfSticks, thislb, otherlt)){
//                gravityTrue = true
                     distan = newDistan
                     dx = otherlt.centerX() - thislb.centerX()
                     dy = otherlt.centerY() - thislb.centerY()
                 }
             }
        }
       if (!gravityTrue && RectF.intersects(thisrb, otherrt)) {
            Log.d(TAG, "CONTAINS RB + RT")
           newDistan = distance(xrb.toDouble(), yrb.toDouble(), stick.xrt.toDouble(), stick.yrt.toDouble())
           if (newDistan < distan){
               if(checkIntersectsWithAll(listOfSticks, thisrb, otherrt)){
//                gravityTrue = true
                   distan = newDistan
                   dx = otherrt.centerX() - thisrb.centerX()
                   dy = otherrt.centerY() - thisrb.centerY()
               }
           }
        }
        if (!gravityTrue && RectF.intersects(thisrb, otherlt)) {
            Log.d(TAG, "CONTAINS RB + LT")

            newDistan = distance(xrb.toDouble(), yrb.toDouble(), stick.xlt.toDouble(), stick.ylt.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thisrb, otherlt)){
//                gravityTrue = true
                    distan = newDistan
                    dx = otherlt.centerX() - thisrb.centerX()
                    dy = otherlt.centerY() - thisrb.centerY()
                }
            }
        }
        if (!gravityTrue && RectF.intersects(thisrb, otherlb)) {
            Log.d(TAG, "CONTAINS RB + LB")

            newDistan = distance(xrb.toDouble(), yrb.toDouble(), stick.xlb.toDouble(), stick.ylb.toDouble())
            if (newDistan < distan){
                if(checkIntersectsWithAll(listOfSticks, thisrb, otherlb)){
//                gravityTrue = true
                    distan = newDistan
                    dx = otherlb.centerX() - thisrb.centerX()
                    dy = otherlb.centerY() - thisrb.centerY()
                }
            }
        }
        var result = floatArrayOf(distan.toFloat(), dx, dy)
        return result
    }

    fun checkIntersectsWithAll(listOfSticks: MutableList<Stick>, thisR : RectF, otherR : RectF) : Boolean{
        var dx = otherR.centerX() - thisR.centerX()
        var dy = otherR.centerY() - thisR.centerY()
        listOfSticks.forEach {
            if (it != this) {
                if (!checkIntersect(dx, dy, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun checkIntersect(dx: Float, dy: Float, stick: Stick): Boolean {
        var newArrayOfPoints = FloatArray(8)
        mat.reset()
        mat.setTranslate(dx, dy)
        mat.mapPoints(newArrayOfPoints, arrayOfPoints )
        if (stick.contains(newArrayOfPoints[0], newArrayOfPoints[1]) || stick.contains(newArrayOfPoints[2], newArrayOfPoints[3]) ||
            stick.contains(newArrayOfPoints[4], newArrayOfPoints[5]) || stick.contains(newArrayOfPoints[6], newArrayOfPoints[7]) ){
            return false
        }
        return true
    }

    fun contains(x : Float, y : Float) : Boolean =
        triangleContains(xlt, ylt, xrt, yrt, xrb, yrb, x, y) ||
                triangleContains(xlt, ylt, xlb, ylb, xrb, yrb, x, y)



    fun triangleContains(x1 : Float, y1 : Float, x2 : Float, y2: Float, x3 : Float, y3 : Float, x0 : Float, y0 : Float): Boolean {
        var z1 = (x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0)
        var z2 = (x2 - x0) * (y3 - y2) - (x3 - x2) * (y2 - y0)
        var z3 = (x3 - x0) * (y1 - y3) - (x1 - x3) * (y3 - y0)
//        Log.d(TAG, "Point in Triangle " + z1 + " " + z2 + " " + z3)
        return z1 > 0 && z2 > 0 && z3 > 0 || z1 < 0 && z2 < 0 && z3 < 0
    }

    fun distance(x1 : Double ,y1 : Double, x2 : Double, y2 : Double) : Double{
        return Math.sqrt(Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1), 2.0))
    }

    fun isNeighbor(other : Stick) : Boolean{
        if (xlt == other.xlt && ylt == other.ylt || xlt == other.xlb && ylt == other.ylb ||
            xlt == other.xrt && ylt == other.yrt || xlt == other.xrb && ylt == other.yrb){
            return true
        }
        if (xlb == other.xlt && ylb == other.ylt || xlb == other.xlb && ylb == other.ylb ||
            xlb == other.xrt && ylb == other.yrt || xlb == other.xrb && ylb == other.yrb){
            return true
        }
        if (xrb == other.xlt && yrb == other.ylt || xrb == other.xlb && yrb == other.ylb ||
            xrb == other.xrt && yrb == other.yrt || xrb == other.xrb && yrb == other.yrb){
            return true
        }
        if (xrt == other.xlt && yrt == other.ylt || xrt == other.xlb && yrt == other.ylb ||
            xrt == other.xrt && yrt == other.yrt || xrt == other.xrb && yrt == other.yrb){
            return true
        }
        return false
    }

    override
    fun toString(): String {
//       return "Stick id: " +id + " lt= " + xlt + " " + ylt + " rt= " + xrt + " " + yrt + " lb= " + xlb + " " + ylb + " rb= " + xrb + " " + yrb
        return "Stick id: " +id
    }

    fun copy() : Stick{
        var newStick = Stick(xLeft, yTop, xRight, yBottom, rX, rY, id)

        newStick = this


        return newStick
    }
}