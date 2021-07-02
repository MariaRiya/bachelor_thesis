package com.game.drievka

import java.util.*

class Solver {
//    var arrayOfShape = Collections.synchronizedList(Collections.synchronizedList(emptyList<List<Stick>>().toMutableList()))
    var shape = Collections.synchronizedList(emptyList<Stick>().toMutableList())
    var forRemove = emptyList<Stick>().toMutableList()
    var arrayOfShape = Collections.synchronizedList(Collections.synchronizedList(emptyList<List<Stick>>().toMutableList()))
    var allShapes = Collections.synchronizedList(Collections.synchronizedList(emptyList<List<Stick>>().toMutableList()))

    fun solveFirst(task: Pair<Int, Int>, row: Int, col: Int): Boolean {

        return true
    }

    fun solveSecond(listOfSticks: MutableList<Stick>,
                        allShapes: MutableList<List<Stick>>,
                        square: Boolean){

    }

    fun countShapes( listOfSticks: MutableList<Stick>, allShapes: MutableList<List<Stick>>, square: Boolean): MutableList<List<Stick>>? {
        this.arrayOfShape.clear()
        this.allShapes = allShapes
        listOfSticks.forEach {
            if (it.angle == 270f) {
                var baseLength = countBaseLength(it, listOfSticks)
                if (!square) {
                    findTriangles(it, baseLength, true, listOfSticks)
                    shape.clear()
                    findTriangles(it, baseLength, false, listOfSticks)
                    shape.clear()
                } else {
                    findTetragons(it, baseLength, listOfSticks)
                    shape.clear()
                }
            }
        }
        return this.arrayOfShape
    }

    private fun countBaseLength(it: Stick, listOfSticks : MutableList<Stick>): Int {
        var stick = it
        var count = 1
        while(stick.right != null && listOfSticks.contains(stick.right)){
            count++
            stick = stick.right!!
        }
        return count
    }

    private fun findTriangles(it : Stick, count: Int, down : Boolean, listOfSticks : MutableList<Stick>){
        var baseLength = count
        var firstSideLength = 0
        //               if(it.leftDown != null && selectedSticks.contains(it.leftDown)){
        var left = it.lower
        if(!down) {
            left = it.upper
        }
        while ( left != null && listOfSticks.contains(left) && firstSideLength < baseLength){
            shape.add(left)
            firstSideLength++
            var secondSideLength = 0
            var secondRight = left.rightSecond
            while(secondRight != null && listOfSticks.contains(secondRight) && secondSideLength < firstSideLength){
                secondSideLength++
                shape.add(secondRight)
                forRemove.add(secondRight)
                if(secondSideLength == firstSideLength){
                    addShape(it, secondSideLength)
                }
                secondRight = secondRight.right
            }
            var countTo = forRemove.size
            for(i in 0.. countTo-1){
                shape.remove(forRemove[i])
            }
            forRemove.clear()
            left = left.right
        }
    }

    fun findTetragons(stick: Stick, baseLength: Int, listOfSticks : MutableList<Stick>) {
        var firstSideLength = 0
        var lower = stick.lower
        var removeSide = emptyList<Stick>().toMutableList()
        while ( lower != null && listOfSticks.contains(lower) && firstSideLength < baseLength){
            shape.add(lower)
            firstSideLength++
            var secondSideLength = 0
            var right = lower.right
            while(right != null && listOfSticks.contains(right) && secondSideLength < firstSideLength ){
                secondSideLength++
                shape.add(right)
                forRemove.add(right)
                var thirdSideLength = 0
                var upper = right.upper
                while(upper != null && listOfSticks.contains(upper) && thirdSideLength < secondSideLength) {
                    thirdSideLength++
                    shape.add(upper)
                    removeSide.add(upper)
                    if (thirdSideLength == secondSideLength && secondSideLength == firstSideLength) {
                        addShape(stick, secondSideLength)
                    }
                    upper = upper.upper
                }
                var countTo = removeSide.size
                for(i in 0.. countTo-1){
                    shape.remove(removeSide[i])
                }
                removeSide.clear()
                right = right.right
            }
            var countTo = forRemove.size
            for(i in 0.. countTo-1){
                shape.remove(forRemove[i])
            }
            forRemove.clear()
            lower = lower.lower
        }

    }

    private fun addShape(stick: Stick, count: Int) {
        var c = count
        var it = stick
        var list = emptyList<Stick>().toMutableList()
        for(i in 0..shape.size-1){
            list.add(shape[i])
        }
        var list2 = emptyList<Stick>().toMutableList()

        for(i in 0..c-1){
            list2.add(it)
            if( it.right != null) {
                it = it.right!!
            }
        }
        c = list2.size
        for (i in 0..list2.size-1){
            c--
            list.add(list2[c])
        }
        if(checkUnique(list)) {
            arrayOfShape.add(list)
 //           listToShow.add(list)
 //           shapesCounter++
 //           countSticks()
        }
    }

    private fun checkUnique(list: MutableList<Stick>): Boolean{
        //      Log.d(TAG, "Array of Shape size = " + arrayOfShape.size)
        allShapes.forEach {
            if(it.size == list.size){
                var different = false
                it.forEach{ elem ->
                    if(!list.contains(elem)){
                        different = true
                    }
                }
                if(!different){
                    return false
                }
            }
            //        Log.d(TAG, "SHAPE is = " + it.toString())
        }
        return true
    }

}