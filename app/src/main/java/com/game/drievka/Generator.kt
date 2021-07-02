package com.game.drievka

import android.util.Log

class Generator {
    val creator = Creator()
    val solver = Solver()
    val TAG = "STICK"

    fun generateThird(level : Int, pwidth : Float, pheight : Float): Pair<Pair<Int, Int>, MutableList<Stick>> {
        Log.d("STICK", "GENERATOR LEVEL: "+level)
        var square = level % 2 != 0
        if(level == 11){
            square = false
        }
        var sticksCount = 0
        var shapesCount = (2..5).random()
        if(level <= 2){
            sticksCount = 0
        }else if(level <= 4){
            shapesCount = 1
            sticksCount = (1..3).random()
        }else if(level <= 6) {
            sticksCount = shapesCount
        }else if(level >= 7){
            shapesCount = (3..5).random()
            var count = 0
            for(i in 0..shapesCount-1){
//                Log.d("STICK", " sticks count =" + sticksCount)
//                Log.d("STICK", "i = " + i)
                if(i == 0){
                    count = (2..3).random()
                    sticksCount += count
                }else{
                    if(count != 3 || i <= 3) {
                        sticksCount++
                    }
                }
            }
        }
        if(square){
   //         Log.d("STICK", "GENERATOR sticksCOUNT = : "+sticksCount)

            sticksCount *= 4
        }else{
            sticksCount *= 3
        }
        if(level >= 9){
            var edges = (1..2).random()
            //                   Log.d("STICK", "edges = " + edges)
            sticksCount -= edges


        }
        var gridOfSticks = creator.createGrid(pwidth,pheight,0,0,square,1)
        return Pair(Pair(sticksCount, shapesCount), gridOfSticks)
    }


    fun generateFourth(level : Int, pwidth : Float, pheight : Float) :  Pair<Pair<Int,Int>, MutableList<Stick>>
    {
        // add
        var square = level % 2 != 0
        if (level == 11) {
            square = false
        }
        var rowCol = getGridSize(level, square)
        var gridOfSticks = creator.createGrid(pwidth,pheight,rowCol.first,rowCol.second,square,2)

        var allShapes = solver.countShapes(gridOfSticks, emptyList<List<Stick>>().toMutableList(), square)
        var selectedBinaryShapes = mutableListOf<String>()
        var task = 0
        var sticks = 0
        if (allShapes != null) {
            var binaryShapes = generateBin(gridOfSticks, allShapes)
            var splitShapes = getSplitShapes(binaryShapes)
            var smallShapes = splitShapes.first
            var bigShapes = splitShapes.second
            var repeat = true
            while(repeat) {
                if (level <= 4) {
                    while(repeat) {
                        selectedBinaryShapes = selectShapes(smallShapes)
                        if(!checkBigShapesInSelected(selectedBinaryShapes, bigShapes)){
                            repeat = false
                        }
                    }
                    repeat = true
                } else {
                    if(level < 7) {
                        selectedBinaryShapes = selectShapes(smallShapes)
                        bigShapes = bigShapes.shuffled()
                        selectedBinaryShapes = selectedBinaryShapes.drop(1).toMutableList()
                        selectedBinaryShapes.add(bigShapes[0])
                    }else{
                        selectedBinaryShapes = selectShapes(binaryShapes)
                    }
                }

                Log.d(TAG, "random shapes selected : " + selectedBinaryShapes.toString())
//            var result = solve(gridOfSticks, selectedShapes, action, square)
                var union = unionBin(selectedBinaryShapes)
                var indexListOfZero = getIndexList(union, '0')
                if (level > 6 && indexListOfZero.size < 5) {
                    selectedBinaryShapes = selectedBinaryShapes.drop(1).toMutableList()
                    union = unionBin(selectedBinaryShapes)
                }
                var commonEdges = mutableListOf<Int>()
                selectedBinaryShapes = checkAllShapes(selectedBinaryShapes, binaryShapes, union)
                commonEdges.addAll(listOfCommonEdges(selectedBinaryShapes))
                var indexListOfOne = getIndexList(union, '1')
                var unCommonEdges = mutableListOf<Int>()
                unCommonEdges.addAll(indexListOfOne)
                unCommonEdges.removeAll(commonEdges)

                if(level <= 2){
                    smallShapes = smallShapes.shuffled()
                    smallShapes.forEach {
                        var countOfDifEdges = countOfDifferentSelectedEdges(union, it)
                        if((countOfDifEdges <= 3 && square || countOfDifEdges <= 2 && !square)  && countOfDifEdges != 0){
                            if(!checkBigShapesInSelected(listOf(union,it), bigShapes)) {
                                repeat = false
                                sticks = countOfDifEdges
                            }
                        }
                    }
                    task = selectedBinaryShapes.size + 1
                }else if(level > 2 && level <= 4 && commonEdges.size >= 2){
                    repeat = false
                    commonEdges.shuffle()
                    union = changeBin(union, commonEdges[0], '0')
                    union = changeBin(union, commonEdges[1], '0')
                    task = selectedBinaryShapes.size
                    sticks = 2
                }else if (level > 4 && level < 7 && commonEdges.size >= 1){
                    task = countShapeInBin(union, binaryShapes)
                    var indexOfMaxCommonEdges = getMaxOfCommonEdges(commonEdges, binaryShapes)
                    union = changeBin(union, indexOfMaxCommonEdges, '0')
                    commonEdges.remove(indexOfMaxCommonEdges)
                    indexListOfZero.add(indexOfMaxCommonEdges)
                    var listOfEdges = getListOfEdges(indexOfMaxCommonEdges, selectedBinaryShapes)
                    listOfEdges.shuffle()
                    if(listOfEdges.size >= 2) {
                        if (indexListOfZero.size <= 3 || (!square && indexListOfZero.size <= 4)) {
                            sticks = 3
                            union = changeBin(union, listOfEdges[0], '0')
                            union = changeBin(union, listOfEdges[1], '0')
                        } else if (indexListOfZero.size < 5 || (!square && indexListOfZero.size <= 5)) {
                            sticks = 2
                            union = changeBin(union, listOfEdges[0], '0')
                        } else {
                            sticks = 1
                        }
                        repeat = false
                    }

                }else if(level >= 7){
                    task = countShapeInBin(union, binaryShapes)
                    if(indexListOfZero.size <= 5 || (!square && indexListOfZero.size < 7)){
                        sticks = 3
                    } else{
                        sticks = 2
                    }
                    if(level < 9){
                        //беру необщие грани

                            selectedBinaryShapes.shuffle()
                        var ok = false
                        selectedBinaryShapes.forEach {
                            if(!ok) {
                                var listOfEdges = getIndexList(it, '1')
//                                Log.d(TAG, "it string = " + it)
//                                Log.d(TAG, "it indexes = " + listOfEdges)
                                listOfEdges.removeAll(commonEdges)
//                                Log.d(TAG, "it indexes after remove = " + listOfEdges)
                                if (listOfEdges.size == sticks) {
                                    for (i in 0..listOfEdges.size - 1) {
                                        union = changeBin(union, listOfEdges[i], '0')
                                    }
                                    ok = true
                                }
                            }
                        }
                    }else {
                        //использую общие грани, подобно как в уровнях 5-6
                        var indexOfMaxCommonEdges = getMaxOfCommonEdges(commonEdges, binaryShapes)
                        union = changeBin(union, indexOfMaxCommonEdges, '0')
                        commonEdges.remove(indexOfMaxCommonEdges)
                        indexListOfZero.add(indexOfMaxCommonEdges)
                        var listOfEdges =
                            getListOfEdges(indexOfMaxCommonEdges, selectedBinaryShapes)
                        listOfEdges.shuffle()
                        Log.d(TAG, "list of edges = " + listOfEdges)
                        if (listOfEdges.size >= 2) {
                            if (sticks == 3) {
                                union = changeBin(union, listOfEdges[0], '0')
                                union = changeBin(union, listOfEdges[1], '0')
                            } else if (sticks == 2) {
                                union = changeBin(union, listOfEdges[0], '0')
                            }
                        }else{
                            Log.d(TAG, "list of edge < 2, level >= 9")
                            task = countShapeInBin(union, binaryShapes)
                        }
                    }

                    if(!(task == countShapeInBin(union, binaryShapes))) {
                        repeat = false
                    }else{
                        Log.d(TAG, "repeat true")

                    }

                }
                Log.d(TAG, "common edges : " + commonEdges)
                Log.d(TAG, "uncommon edges : " + unCommonEdges)
                Log.d(TAG, "index list of one: " + indexListOfOne)


                gridOfSticks = binToGrid(gridOfSticks, union) as MutableList<Stick>

            }
            Log.d(TAG, "grid of sticks size : " + gridOfSticks.size)

        }
        return Pair(Pair(task, sticks), gridOfSticks)

    }

    fun generateFifth(level : Int, pwidth : Float, pheight : Float) :  Pair<Pair<Int,Int>, MutableList<Stick>> {
        // remove
        var square = level % 2 != 0
        if (level == 11) {
            square = false
        }
        var l = level
        if(l == 8 || l == 7){
            l -= 2
        }
        var rowCol = getGridSize(l, square)
        var gridOfSticks = creator.createGrid(pwidth,pheight,rowCol.first,rowCol.second,square,2)

        var allShapes = solver.countShapes(gridOfSticks, emptyList<List<Stick>>().toMutableList(), square)
        var selectedBinaryShapes = mutableListOf<String>()
        var task = 0
        var sticks = 0
        if (allShapes != null) {
            var binaryShapes = generateBin(gridOfSticks, allShapes)
            var splitShapes = getSplitShapes(binaryShapes)
            var smallShapes = splitShapes.first
            var bigShapes = splitShapes.second
            var repeat = true
            while (repeat) {
                if (level <= 2) {
                    while (repeat) {
                        selectedBinaryShapes = selectShapes(smallShapes)
                        if (!checkBigShapesInSelected(selectedBinaryShapes, bigShapes)) {
                            repeat = false
                        }
                    }
                    repeat = true
                } else {
                    if (level < 7) {
                        selectedBinaryShapes = selectShapes(smallShapes)
                        bigShapes = bigShapes.shuffled()
//                        selectedBinaryShapes = selectedBinaryShapes.drop(1).toMutableList()
                        selectedBinaryShapes.add(bigShapes[0])
                    } else if(level == 7 || level == 8){
                        selectedBinaryShapes = binaryShapes.toMutableList()
                    }else {
                        selectedBinaryShapes = selectShapes(binaryShapes)
                    }
                }

                Log.d(TAG, "random shapes selected : " + selectedBinaryShapes.toString())
                var union = unionBin(selectedBinaryShapes)
                var commonEdges = mutableListOf<Int>()
                selectedBinaryShapes = checkAllShapes(selectedBinaryShapes, binaryShapes, union)
                commonEdges.addAll(listOfCommonEdges(selectedBinaryShapes))
                var indexListOfOne = getIndexList(union, '1')
                var unCommonEdges = mutableListOf<Int>()
                unCommonEdges.addAll(indexListOfOne)
                unCommonEdges.removeAll(commonEdges)

                Log.d(TAG, "common edges : " + commonEdges)
                Log.d(TAG, "uncommon edges : " + unCommonEdges)
                Log.d(TAG, "index list of one: " + indexListOfOne)

                if (level <= 4) {
                    if(unCommonEdges.size >= 2){
                        repeat = false
                        var two = false
                        var three = false
                        task = selectedBinaryShapes.size -1
                        selectedBinaryShapes.forEach {
                            if(!two || !three) {
                                var edgesList = getIndexList(it, '1')
                                edgesList.removeAll(commonEdges)
                                if (edgesList.size == 2) {
                                    two = true
                                } else if (edgesList.size == 3) {
                                    three = true
                                }
                            }
                        }
                        if(two && three){
                            sticks = (2..3).random()
                        }else if(two){
                            sticks = 2
                        }else if(three){
                            sticks = 3
                        }else{
                            repeat = true
                        }
                    }
                }else if(level == 5 || level == 6 || level > 8){
                    var ok = false
                    if(commonEdges.size >= 1){
                        commonEdges.shuffle()
                        commonEdges.forEach {
                            if(!ok) {
                                var newUnion = changeBin(union, it, '0')
                                //                               commonEdges.remove(it)
                                //                               indexListOfZero.add(it)
                                var listOfEdges =
                                    getListOfEdges(it, selectedBinaryShapes)
                                Log.d(TAG, " index of common edge : " + it)
                                Log.d(TAG, "list of index : " + listOfEdges)
                                //нужно найти индкексы тех стиков, которые не принадлежат другим фигурам
                                //listOfEdges = removeIndex(listOfEdges, selectedBinaryShapes)
                                listOfEdges.removeAll(commonEdges)
                                Log.d(TAG, "list of index  after removing: " + listOfEdges)
                                if (level == 5 || level == 6) {
                                    if (listOfEdges.size == 2 || listOfEdges.size == 1 || listOfEdges.size == 3) {
                                        task = countShapeInBin(newUnion, binaryShapes)
                                        sticks = listOfEdges.size + 1
                                        ok = true
                                        repeat = false
                                    }
                                }else{
                                    if (listOfEdges.size == 2 || listOfEdges.size == 4 || listOfEdges.size == 3 || listOfEdges.size == 5 || listOfEdges.size == 6) {
                                        task = countShapeInBin(newUnion, binaryShapes)
                                        sticks = listOfEdges.size + 1
                                        ok = true
                                        repeat = false
                                    }
                                }
                            }
                        }
                    }else{
                        repeat = true
                    }
//                    repeat = false
                }else if(level == 7 || level == 8){
                    var newSelectedBinaryShapes = selectShapes(binaryShapes)
                    var newUnion = unionBin(newSelectedBinaryShapes)
                    task = countShapeInBin(newUnion, binaryShapes)
                    sticks = getIndexList(newUnion, '0').size
                    repeat = false
                }else if(level > 8){

                }
//                repeat = false
                gridOfSticks = binToGrid(gridOfSticks, union) as MutableList<Stick>

            }
        }
        return Pair(Pair(task, sticks), gridOfSticks)

    }

    private fun checkAllShapes(selectedBinaryShapes: MutableList<String>, binaryShapes: List<String>, union: String): MutableList<String> {
        var indexesUnion = getIndexList(union, '1')
        binaryShapes.forEach {
            if(indexesUnion.containsAll(getIndexList(it, '1'))){
                if(!selectedBinaryShapes.contains(it)){
                    selectedBinaryShapes.add(it)
                }
            }
        }
        return selectedBinaryShapes
    }

    //дает список индексов граней, тех фигур, в которые входила грань индексОфЭдж, она скорее всего была ранее удалена и мы ищем разрушенные фигуры
    private fun getListOfEdges( indexOfEdges: Int, binaryShapes: MutableList<String>): MutableList<Int> {
        var result = hashSetOf<Int>()
        binaryShapes.forEach {
            if(it[indexOfEdges] == '1' ){
                result.addAll(getIndexList(it,'1'))
            }
        }
//        result.distinct()
        result.remove(indexOfEdges)
        return result.toMutableList()
    }

    //самая часто использованная в фигурах грань
    private fun getMaxOfCommonEdges(commonEdges: MutableList<Int>, binaryShapes: List<String>): Int {
        var edgeIndex = 0
        var result = 0
        commonEdges.forEach { index ->
            var max = 0
            binaryShapes.forEach {
                if(it[index] == '1'){
                    max++
                }
            }
            if(max > result){
                result = max
                edgeIndex = index
            }
        }

        return edgeIndex

    }

    //количество фигур в бинарном стринге
    private fun countShapeInBin(union: String, binaryShapes: List<String>): Int {
        var result = 0
        binaryShapes.forEach {
            if(countOfDifferentSelectedEdges(union, it) ==0){
                result++
            }
        }
        return result
    }

    //возмощает число граней, на которое фигура отличается от юнион бин, т.е. грани которые в фигуре есть, а в юнион нет
    private fun countOfDifferentSelectedEdges(unionBin: String, shape: String): Int {
        var result = 0
        for(i in 0..unionBin.length-1){
            if(shape[i] == '1' &&  unionBin[i] != '1'){
                result++
            }
        }
        return result
    }

    // входит ли большая фигура в юнион бин
    private fun checkBigShapesInSelected(selectedBinaryShapes: List<String>, bigShapes: List<String>): Boolean {
        var unionListOfOne = getIndexList(unionBin(selectedBinaryShapes), '1')
        bigShapes.forEach {
            var edgeOfBigShape = getIndexList(it, '1')
            if(unionListOfOne.containsAll(edgeOfBigShape)){
                return true
            }
        }
        return false
    }

    //деление фигур на большие и маленькие
    private fun getSplitShapes(binaryShapes: List<String>): Pair<List<String>,List<String>> {
        var bigger = emptyList<String>().toMutableList()
        var smaller = emptyList<String>().toMutableList()
        for(i in 0..binaryShapes.size-1){
            if(countCharInString(binaryShapes[i], '1') <= 4){
                smaller.add(binaryShapes[i])
            }else{
                bigger.add(binaryShapes[i])
            }
        }
        return Pair<List<String>,List<String>>(smaller,bigger)
    }

    //подсчет сколько раз встречается 0 или 1 в стринге
    private fun countCharInString(string : String, char: Char) : Int{
        var result = 0
        string.forEach {
            if(it == char){
                result++
            }
        }
        return result
    }

    //создает размер сетки в зависимости от уровня
    private fun getGridSize(level: Int, square: Boolean) : Pair<Int, Int> {
        var rowCol = Pair(3, 3)
        var row = (2..3).random()
        var col = 2
        if (row == 2){
            col = 3
        }
        if (level <= 6) {
            if (!square) {
                rowCol = Pair(2, 2)
            } else {
                rowCol = Pair(row, col)
            }
        }else if(!square){
            rowCol = Pair(row, col)
        }
        return rowCol
    }

    //случайно выбирает фигуры
    private fun selectShapes(binaryShapes: List<String>): MutableList<String> {
        var countOfSelectedShapes = binaryShapes.size / 3
        var rnd1 = (1..2).random()
        countOfSelectedShapes += rnd1

        return binaryShapes.shuffled().slice(0..countOfSelectedShapes-1).toMutableList()
    }

    private fun solve(grid: MutableList<Stick>, selectedShapes: List<String>, action: Int, square : Boolean): Any {
        var unionBinShape = unionBin(selectedShapes)
        var indexListOfOne = getIndexList(unionBinShape, '1').shuffled()
        var listOfCombination = emptyList<Pair<Int, List<Stick>>>().toMutableList()
        var removedUnionBin = ""
        var newGrid = emptyList<Stick>()
        Log.d(TAG, "UNION bin = " + unionBinShape)
        for (i in 0..indexListOfOne.size-2){
            for(j in i+1..indexListOfOne.size-1){
                //проверить правильно ли меняется removed юнионБин
                Log.d(TAG, " index i = " + i + " index j = " + j)
//                removedUnionBin = changeBin(unionBinShape, indexListOfOne[i], indexListOfOne[j], '0')
 //               Log.d(TAG, "removedUnionBin = " + removedUnionBin)
                //надо ли создавать новый грид или только добавить выбранные стики
                newGrid = binToGrid(grid, removedUnionBin)
//                Log.d(TAG, "new grid size " + newGrid.size)
                var newSelectedSticks = getSelecetedSticks(newGrid)
//                Log.d(TAG, "new selected sticks size  " + newSelectedSticks.size)
                var countedShapes = solver.countShapes(newSelectedSticks.toMutableList(), emptyList<List<Stick>>().toMutableList(), square)
                Log.d(TAG, "counted shapes size =  " + countedShapes?.size)

                listOfCombination.add(Pair(countedShapes?.size, newGrid) as Pair<Int, List<Stick>>)
            }
        }
        Log.d(TAG, "count of combinations = " + listOfCombination.size)
        if(action == 0) {



        }else if(action == 1){

        }else if(action == 2){

        }
        return 0
    }



    private fun getSelecetedSticks(grid: List<Stick>): List<Stick> {
        var result = emptyList<Stick>().toMutableList()
        grid.forEach {
            if(it.selected){
                result.add(it.copy())
            }
        }
        return result
    }

    //изменяется в бинарном стринге 0 на 1 или наоборот
    private fun changeBin(unionBinShape: String, i: Int, char : Char): String {
        var result = ""
        for (index in 0..unionBinShape.length-1){
            if(index == i){
                result += char
            }else{
                result += unionBinShape[index]
            }
        }
        return result

    }

    //дает список индексов, в котором индексы соотвествуют местам, где находится 0 либо 1
    private fun getIndexList(unionBinShape: String, char: Char): MutableList<Int> {
        var result = emptyList<Int>().toMutableList()
        for(i in 0..unionBinShape.length-1) {
            if(unionBinShape[i] == char){
                result.add(i)
            }
        }
        return result
    }

    //обьеденение стринга
    fun unionBin(binaryShapes: List<String>) : String {
        var result = ""
        var commonEdges = hashSetOf<Int>()
        if(binaryShapes.size > 1) {
            result = binaryShapes[0]
            binaryShapes.forEach {
                result = binOr(result, it)
            }
        }
        return result
    }

    //список индексов общих граней
    fun listOfCommonEdges(binaryShapes: List<String>) : HashSet<Int> {
        var result = ""
        var commonEdges = hashSetOf<Int>()
        if(binaryShapes.size > 1) {
            result = binaryShapes[0]
            binaryShapes.forEach {
                commonEdges = getIndexListOfCommonEdges(result, it, commonEdges)
                result = binOr(result, it)
            }
        }
        return commonEdges
    }

    fun binOr(string1 : String, string2 : String) : String{
        var result = ""
        for(i in 0.. string2.length-1){
            if(string1[i] == '1' || string2[i] == '1'){
                result += '1'
            }else{
                result += '0'
            }
        }
        return result
    }

    fun getIndexListOfCommonEdges(string1: String, string2: String, commonEdges: HashSet<Int>) : HashSet<Int>{
        if(string1.equals(string2)){return commonEdges}
        for(i in 0.. string2.length-1){
            if(string1[i] == '1' && string2[i] == '1'){
                commonEdges.add(i)
            }
        }
        return commonEdges
    }

    fun binToGrid(grid: List<Stick>, binString : String) : List<Stick>{
        //контроль правильности работы клонирования
        var result = ArrayList(grid.map { it.copy() })
/*            grid.map {
                Log.d(TAG, "stick angle = " + it.angle + " right = " + it.right)
                Log.d(TAG, "copy angle = " + it.copy().angle + " copy right = " + it.copy().right)
            }*/

        for(i in 0..grid.size-1){
            if(binString[i] == '1'){
                result.elementAt(i).selected = true
//                Log.d(TAG, "was selected : " + i)
            }else{
                result.elementAt(i).selected = false
            }
        }
        return result
    }

    fun listOfEdgesNotInShapes(union : String, shapes : List<String> ) : MutableList<Int> {

        shapes.forEach {
            for (i in 0..union.length-1){
                if(union[i] != '1' && it[i] == '1'){

                }
            }
        }

        return mutableListOf()
    }


    fun generateBin(grid : List<Stick>, shapes : MutableList<List<Stick>>) : List<String>{
        var result = emptyList<String>().toMutableList()
        shapes.forEach { shape ->
            var binKod = ""
             grid.forEach { stick ->
                 if(shape.contains(stick)){
                     binKod += "1"
                 }else{
                     binKod += "0"
                 }
             }
            result.add(binKod)
//            Log.d(TAG, "string = " + binKod)
        }
        return result
    }


}