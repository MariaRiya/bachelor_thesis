package com.game.drievka

import android.graphics.Path
import android.graphics.RectF
import android.util.Log

class Creator {

    var gridOfSticks = emptyList<Stick>().toMutableList()
    val TAG = "STICK"
    val round = 20f


    fun createGrid(pwidth : Float, pheight : Float, row1 : Int, col1 : Int, square : Boolean, currentGame : Int): MutableList<Stick> {
        gridOfSticks.clear()

        var cellLength = pheight / 3f

        Log.d(TAG, "cellL= " + cellLength)
        var radY = cellLength * 0.8f / 2
        var radX = cellLength * 0.11f /2

        var col = col1
        var row = row1
        Log.d(TAG, "rX= " + radX + " rY= " + radY)
        if(currentGame == 1) {
            col = (pwidth / cellLength).toInt()
            row = (pheight / cellLength).toInt()
        }
        if(square) {
            var startX = (pwidth - (cellLength * (col - 0.35f))) / 2f
            if (startX < 10) {
                col -= 1
                startX = (pwidth - (cellLength * (col -0.35f))) / 2f
            }

            var startY = (pheight - (cellLength * (row - 0.2f))) / 2f
            createRectGrid(row,col,radX, radY, startX, startY)

        }else{
            var startX = (pwidth - (cellLength * (col + 0.5f))) / 2f
            if (startX < 30) {
                col -= 1
                startX = (pwidth - (cellLength * (col + 0.5f))) / 2f
            }


            var startY = (pheight - (cellLength * (row - 0.25f))) / 2f

            createTriangleGrid(row, col, radX, radY, startX, startY)

        }

//        var startY = ((pheight - cell*row) - radX*2)/ 2
//        var startX = ((pwidth - cell*col)  - radX*2)/ 2
        addNeighbours(square)
        return gridOfSticks
    }

    fun createListOfRectf(pwidth : Float, pheight : Float, count : Int) : MutableList<Stick> {
        var cellLength = pheight / 3f

//        Log.d(TAG, "cellL= " + cellLength)
        var radY = cellLength * 0.8f / 2
        var radX = cellLength * 0.11f /2

        var startY = 0f
        if(count < 3) {
            startY = (pheight - (radY * 2 * count)) / 2f
        }else{
            startY = (pheight - (radY * 2 * 3)) / 2f

        }
        var startX = pwidth * 0.05f
        var stY = startY

        var result = mutableListOf<Stick>()
        for(i in 0..count-1) {
            var newStick = Stick(startX-radX, stY, startX + radX, stY + radY*2, radX, radY, "0")
            val path = Path()
            path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
            newStick.setPath(path)
            result.add(newStick)
            stY += radY*2 + radX
            if(i == 2){
                startX += radX*3
                stY = startY
            }
            if(i == 5){
                startX += radX*3
                stY = startY
            }
        }
        return result
    }

    private fun addNeighbours(square: Boolean) {
        gridOfSticks.forEach { it ->
            if(it.angle == 270f){
//                Log.d(TAG, "ANGLE = 270"  )
                if(!square){
                    gridOfSticks.forEach { neighbor ->
                        if(it != neighbor) {
//                            if (neighbor.xrb == it.xlt && neighbor.yrb == it.ylt){
                            if( neighbor.angle == 30f) {
                                var x1 = it.xlt
                                var x2 = neighbor.xrb
                                var y1 = it.ylt
                                var y2 = neighbor.yrb
//                                Log.d(TAG, "DISTANCE = " + Math.sqrt(
//                                    Math.pow(
//                                        (x1.toDouble() - x2),
//                                        2.0
//                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
//                                ))
                                if (Math.sqrt(
                                        Math.pow(
                                            (x1.toDouble() - x2),
                                            2.0
                                        ) + Math.pow((y1.toDouble() - y2), 2.0)
                                    ) < 7
                                ) {
                                    it.upper = neighbor
//                            }else if(neighbor.xrt == it.xlb && neighbor.yrt == it.ylb){
                                }
                            }
                            if( neighbor.angle == 330f) {
                                var x1 = it.xlb
                                var x2 = neighbor.xrt
                                var y1 = it.ylb
                                var y2 = neighbor.yrt
                                if (Math.sqrt(
                                        Math.pow(
                                            (x2.toDouble() - x1),
                                            2.0
                                        ) + Math.pow((y2.toDouble() - y1), 2.0)
                                    ) < 7
                                ) {
                                    it.lower = neighbor
                                }
                            }
                            if( neighbor.angle == 270f ){
                                var rightRect = RectF(it.xrt, it.yrt - it.rX, it.xrt + 5*it.rX, it.yrt + it.rX*5)
                                if( rightRect.contains(neighbor.xlt, neighbor.ylt) ){
//                                    Log.d(TAG, "APPEND RIGHT it id = " + it.id + " id n = " + neighbor.id)
                                    it.right = neighbor
                                }
                            }
                        }
                    }
                }else{
                    gridOfSticks.forEach { neighbor ->
                        if(it != neighbor) {
                            if (neighbor.angle == 0f) {
                                var x1 = it.xrt
                                var x2 = neighbor.xlb
                                var y1 = it.yrt
                                var y2 = neighbor.ylb
//                                Log.d(TAG, "DISTANCE = " + Math.sqrt(
//                                    Math.pow(
//                                        (x1.toDouble() - x2),
//                                        2.0
//                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
//                                ))
                                if (Math.sqrt(
                                        Math.pow(
                                            (x1.toDouble() - x2),
                                            2.0
                                        ) + Math.pow((y1.toDouble() - y2), 2.0)
                                    ) < 7
                                ) {
                                    it.upper = neighbor
                                } else {
                                    x1 = it.xlb
                                    x2 = neighbor.xrt
                                    y1 = it.ylb
                                    y2 = neighbor.ylt
                                    if (Math.sqrt(
                                            Math.pow(
                                                (x1.toDouble() - x2),
                                                2.0
                                            ) + Math.pow((y1.toDouble() - y2), 2.0)
                                        ) < 7
                                    ) {
                                        it.lower = neighbor
                                    }
                                }
                            }else if ( neighbor.angle == it.angle){
                                var rightRect = RectF(it.xrb, it.yrb - it.rX*3, it.xrb + 3*it.rX, it.yrb + it.rX*3)
                                if( rightRect.contains(neighbor.xlb, neighbor.ylb) ){
                                    it.right = neighbor
                                }
                            }
                        }
                    }
                }
            } else if(it.angle == 30f){
                gridOfSticks.forEach { neighbor ->
                    if(it != neighbor) {
                        if (neighbor.angle == 330f){
                            var x1 = it.xrt
                            var x2 = neighbor.xlt
                            var y1 = it.yrt
                            var y2 = neighbor.ylt
//                                Log.d(TAG, "DISTANCE = " + Math.sqrt(
//                                    Math.pow(
//                                        (x1.toDouble() - x2),
//                                        2.0
//                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
//                                ))
                            if (Math.sqrt(
                                    Math.pow(
                                        (x1.toDouble() - x2),
                                        2.0
                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
                                ) < 7
                            ) {
                                it.rightSecond = neighbor
                            }
                        }else if ( neighbor.angle == it.angle){
                            var rightRect = RectF(it.xrt, it.yrt - it.rX*3, it.xrt + 3*it.rX, it.yrt + it.rX*3)
                            if( rightRect.contains(neighbor.xrb, neighbor.yrb) ){
                                it.right = neighbor
                            }
                        }
                    }
                }
            } else if(it.angle == 330f){
                gridOfSticks.forEach { neighbor ->
                    if(it != neighbor) {
                        if (neighbor.angle == 30f){
                            var x1 = it.xrb
                            var x2 = neighbor.xlb
                            var y1 = it.yrb
                            var y2 = neighbor.ylb
//                                Log.d(TAG, "DISTANCE = " + Math.sqrt(
//                                    Math.pow(
//                                        (x1.toDouble() - x2),
//                                        2.0
//                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
//                                ))
                            if (Math.sqrt(
                                    Math.pow(
                                        (x1.toDouble() - x2),
                                        2.0
                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
                                ) < 7
                            ) {
                                it.rightSecond = neighbor
                            }
                        }else if ( neighbor.angle == it.angle){
                            var rightRect = RectF(it.xrb, it.yrb - it.rX*3, it.xrb + 3*it.rX, it.yrb + it.rX*3)
                            if( rightRect.contains(neighbor.xrt, neighbor.yrt) ){
                                it.right = neighbor
                            }
                        }
                    }
                }
            } else{
                gridOfSticks.forEach { neighbor ->
                    if(it != neighbor) {
                        if (neighbor.angle == 270f){
                            var x1 = it.xrb
                            var x2 = neighbor.xlt
                            var y1 = it.yrb
                            var y2 = neighbor.ylt
//                                Log.d(TAG, "DISTANCE = " + Math.sqrt(
//                                    Math.pow(
//                                        (x1.toDouble() - x2),
//                                        2.0
//                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
//                                ))
                            if (Math.sqrt(
                                    Math.pow(
                                        (x1.toDouble() - x2),
                                        2.0
                                    ) + Math.pow((y1.toDouble() - y2), 2.0)
                                ) < 7
                            ) {
                                it.right = neighbor
                            }
                        }else if ( neighbor.angle == it.angle){
                            var lowerRect = RectF(it.xrb, it.yrb - it.rX*3, it.xrb + 3*it.rX, it.yrb + it.rX*3)
                            if( lowerRect.contains(neighbor.xrt, neighbor.yrt) ){
                                it.lower = neighbor
                            }else{
                                var upperRect = RectF(it.xrt, it.yrt - it.rX*3, it.xrt + 3*it.rX, it.yrt + it.rX*3)
                                if( upperRect.contains(neighbor.xrb, neighbor.yrb) ){
                                    it.upper = neighbor
                                }
                            }
                        }
                    }
                }
            }

/*            var topRect = RectF(it.xlt - it.rX*2, it.ylt - it.rX*2, it.xrt+it.rX*3, it.yrt + it.rX*3)
            var botRect = RectF(it.xlb - it.rX*2, it.ylb - it.rX*2, it.xrb+it.rX*3, it.yrb + it.rX*3)
            gridOfSticks.forEach { neighbor ->
                if(it != neighbor) {
                    var otherTopRect = RectF(
                        neighbor.xlt - it.rX * 2,
                        neighbor.ylt - it.rX * 2,
                        neighbor.xrt + it.rX * 2,
                        neighbor.yrt + it.rX * 2
                    )
                    var otherBotRect = RectF(
                        neighbor.xlb - it.rX * 2,
                        neighbor.ylb - it.rX * 2,
                        neighbor.xrb + it.rX * 2,
                        neighbor.yrb + it.rX * 2
                    )
                    if (RectF.intersects(topRect, otherBotRect) || RectF.intersects(
                            topRect,
                            otherTopRect
                        ) || RectF.intersects(botRect, otherBotRect) || RectF.intersects(
                            botRect,
                            otherTopRect
                        )
                    ) {
                        if (!it.neighborhoods.contains(neighbor)) {
                            it.neighborhoods.add(neighbor)
                            neighbor.neighborhoods.add(it)
                        }
                    }
                }
            }

 */
            //           Log.d(TAG, "it id = " + it.id + " RIGHT = " + it.right.toString() + " RIGHT SECOND = " + it.rightSecond.toString() + " LEFT Up = " + it.upper + " LEFT DOWN = " + it.lower)
        }


    }


    fun createRectGrid(row: Int, col : Int, radX : Float, radY : Float, startX : Float, startY : Float){
        var stY = startY

//        var horizontalRect = Stick(context, xx - radX, yy - radY, xx + radX, yy + radY, radX, radY, 0)
//        horizontalRect.rotateXY(270f)
        Log.d(TAG, "r = " + row + " c = " + col)
        for(i in 0..row*2){
            var stX = startX
            if(i % 2 == 0){
                stX += radX*2
            }
            for(j in 0..col){
                if (i % 2 == 0 ) {
                    if( j != col){
                        //                       Log.d(TAG, "i%2")
                        var idS = "" + i + 0 + j
                        var newStick = Stick(stX, stY, stX+radX*2, stY+radY*2, radX, radY, idS)
                        val path = Path()
                        path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
                        newStick.setPath(path)
                        newStick.rotate(270f)
                        newStick.moveStarted(newStick.xlt, newStick.ylt)
                        newStick.move(stX, stY)
                        newStick.moveEnded()
                        gridOfSticks.add(newStick)
                    }
//                    startX += radX*2 + radY*2
                }else{
                    var idS = "" + i + 1 + j
                    var newStick = Stick(stX, stY, stX+radX*2, stY+radY*2, radX, radY, idS)
                    val path = Path()
                    path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
                    newStick.setPath(path)
                    gridOfSticks.add(newStick)
//                    startX += radX*2 + radY*2
                }
                stX += radX*2 + radY*2
            }
            if(i %2 ==0){
                stY += radX*2
            }
            else{
                stY += radY*2
            }
        }
    }

    fun createTriangleGrid(row: Int, col : Int, radX : Float, radY : Float, startX : Float, startY : Float){
        var stX = startX
        var stY = startY
        var pov = 2
        var rot30 = false
//        var horizontalRect = Stick(context, xx - radX, yy - radY, xx + radX, yy + radY, radX, radY, 0)
//        horizontalRect.rotateXY(270f)
        Log.d(TAG, "r = " + row + " c = " + col)
        for(i in 0..row){
            var stX1 = stX
            if(i % 2 != 0){
                stX1 += radX*2 + radY
            }
            for(j in 0..col-1){
                var idS = "" + i + 0 + j
                var newStick1 = Stick(stX1, stY, stX1+radX*2, stY+radY*2, radX, radY, idS)
                var path1 = Path()
                path1.addRoundRect(newStick1.getRectF(), round, round, Path.Direction.CW)
                newStick1.setPath(path1)
                newStick1.rotate(270f)
                newStick1.moveStarted(newStick1.xlt, newStick1.ylt)
                newStick1.move(stX1, stY)
                newStick1.moveEnded()
                gridOfSticks.add(newStick1)
                if( i != row) {
                    idS = "" + i + 1 + j
                    var newStick2 = Stick(
                        newStick1.xlb - radX * 2,
                        newStick1.ylb,
                        newStick1.xlb,
                        newStick1.ylb + radY * 2,
                        radX,
                        radY,
                        idS
                    )
                    var path2 = Path()
                    path2.addRoundRect(newStick2.getRectF(), round, round, Path.Direction.CW)
                    newStick2.setPath(path2)
                    newStick2.rotate(330f)
                    newStick2.moveStarted(newStick2.xlt, newStick2.ylt)
                    newStick2.move(newStick1.xlb - radX * 2, newStick1.ylb + radX)
                    newStick2.moveEnded()
                    gridOfSticks.add(newStick2)
                    idS = "" + i + 2 + j
                    var newStick3 = Stick(
                        newStick1.xrb,
                        newStick1.yrb,
                        newStick1.xrb + radX * 2,
                        newStick1.yrb + radY * 2,
                        radX,
                        radY,
                        idS
                    )
                    var path3 = Path()
                    path3.addRoundRect(newStick3.getRectF(), round, round, Path.Direction.CW)
                    newStick3.setPath(path3)
                    newStick3.rotate(30f)
                    newStick3.moveStarted(newStick3.xlt, newStick3.ylt)
                    newStick3.move(newStick1.xrb, newStick1.yrb)
                    newStick3.moveEnded()
                    gridOfSticks.add(newStick3)
                    if(i % 2 != 0 && j == 0){
                        idS = "" + i + 3 + j
                        var newStick4 = Stick(
                            newStick2.xlt - radX*2,
                            newStick2.ylt,
                            newStick2.xlt,
                            newStick2.ylt + radY * 2,
                            radX,
                            radY,
                            idS
                        )
                        var path4 = Path()
                        path4.addRoundRect(newStick4.getRectF(), round, round, Path.Direction.CW)
                        newStick4.setPath(path4)
                        newStick4.rotate(30f)
                        newStick4.moveStarted(newStick4.xlt, newStick4.ylt)
                        newStick4.move(newStick2.xlt - radX*2, newStick2.ylt - radX)
                        newStick4.moveEnded()
                        gridOfSticks.add(newStick4)
                    }
                    if(i % 2 == 0 && j == col-1){
                        idS = "" + i + 3 + j
                        var newStick4 = Stick(
                            newStick3.xlt,
                            newStick3.ylt,
                            newStick3.xlt + radX*2,
                            newStick3.ylt + radY * 2,
                            radX,
                            radY,
                            idS
                        )

                        var path4 = Path()
                        path4.addRoundRect(newStick4.getRectF(), round, round, Path.Direction.CW)
                        newStick4.setPath(path4)
                        newStick4.rotate(330f)
                        newStick4.moveStarted(newStick4.xlt, newStick4.ylt)
                        newStick4.move(newStick3.xrt, newStick3.yrt)
                        newStick4.moveEnded()
                        gridOfSticks.add(newStick4)
                    }
                }
                stX1 += radX*4 + radY*2
            }
            stY += radX + 2*radY
        }
    }

}