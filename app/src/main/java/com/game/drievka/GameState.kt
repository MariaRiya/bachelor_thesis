package com.game.drievka

import android.content.Context
import android.graphics.*
import android.util.Log
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.sin
import kotlin.math.cos

class GameState(
    internal var context: Context,
    pwidth: Float,
    pheight: Float,
    v : Playground
) {

    var win = false
    val rnd = Random()
    var partOfWidthX = 0.9F
    var partOfHeightY = 0.3F
    var partOfWidthRad = 100
    var partOfHeightRad = 7.5f
    val STROKE_WIDTH = 5f
    var shapesCounter = 0
    var sticksCounter = 0
    var allSticksCounter = 0
    var sticksTask = 2
    var shapesTask = 0
    val round = 20F
    var rectF: Stick = Stick(0f,0f,0f,0f,1f,1f,"0")
    var sticks = emptyList<Stick>().toMutableList()
    val TAG = "STICK"
    var paintFill = Paint()
    var paintStroke = Paint()
    var selectedPaintStroke = Paint()
    var index = -1
    var firstPointer = -1
    var secondPointer = -1
    var bitmapBin: Bitmap
    var bitmapBinX = 0F
    var bitmapBinY = 0F
    var canvRectF = RectF()
    var debagRectF = RectF()
    var debagMode = false
    var anglesList = arrayListOf(0f, 30f, 60f, 270f, 300f, 330f)
    var rotateAngle = 0f
    var rotatedSticks = emptyList<Stick>().toMutableList()
    var transparentPaintFill = Paint()
    var transparentPaintStroke = Paint()
    var paintColor = Paint()
    var paintForRectF = Paint()
    var bitmapRotation: Bitmap
    var bitmapRotationX = 0f
    var bitmapRotationY = 0f
    var bitmapColor: Bitmap
    var bitmapMagnetActive: Bitmap
    var bitmapMagnetInactive: Bitmap
    var rectRotation = RectF()
    var mat = Matrix()
    var clearSelected = false
    var idStick = 1
    var gridOfSticks = emptyList<Stick>().toMutableList()
    var selectedSticks = emptyList<Stick>().toMutableList()
    var arrayOfShape = Collections.synchronizedList(emptyList<List<Stick>>().toMutableList())
    var shape = emptyList<Stick>().toMutableList()
    var square = false
    var forRemove = emptyList<Stick>().toMutableList()
    var currentGame = 1
    var currentLevel = 1
    val generator = Generator()
    var pwidth = pwidth
    var pheight = pheight
    var vi = v
    var listToShow =
        Collections.synchronizedList(Collections.synchronizedList(emptyList<List<Stick>>().toMutableList()))
    var show = GlobalScope.launch { }
    var canRemove = true
    var solver = Solver()
    val creator = Creator()
    var countOfExtraSticksTask = 0
    var listOfExtraSticks = mutableListOf<Stick>()
    var extraSticksCounter = 0
//    val colors = listOf(
//        Color.YELLOW,
//        Color.parseColor("#F36223"),
//        Color.RED,
//        Color.parseColor("#F976D8"),
//        Color.parseColor("#1280F3"),
//        Color.GREEN,
//        Color.parseColor("#1E5945"),
//        Color.parseColor("#651B0B"),
//        Color.GRAY,
//        Color.BLACK
//    )
    var numberOfColors = 8
    var colors = mutableListOf(
        Color.YELLOW
    )
    var colorsStack = listOf(
        Color.parseColor("#F36223"),
        Color.RED,
        Color.parseColor("#F976D8"),
        Color.parseColor("#1280F3"),
        Color.GREEN,
        Color.parseColor("#1E5945"),
        Color.parseColor("#C10087"),
        Color.parseColor("#7D0057"),
        Color.parseColor("#AD66D5"),
        Color.parseColor("#7109AA"),
        Color.parseColor("#1049A9"),
        Color.parseColor("#6A92D4"),
        Color.parseColor("#009999"),
        Color.parseColor("#33CCCC"),
        Color.parseColor("#2D9B27"),
        Color.parseColor("#CCF600"),
        Color.parseColor("#FFAA73"),
        Color.parseColor("#A2000C"),
        Color.parseColor("#FC717B"),
        Color.parseColor("#5E687F"),
        Color.parseColor("#1E2E53"),
        Color.parseColor("#A3ACBF"),
        Color.parseColor("#FFE583")
    )
    var colorsIndex = 0
    var openColor = false
    var rectColor = RectF()
    var circleList = mutableListOf<RectF>()
    var middleW = 0f
    var middleH = 0f
    var ind = 0
    var intersectR = RectF()
    val pictures = listOf(
        // 0 elka 4d 2c
        listOf(
            Parameters(43.579563f, 76.14677f, 270.0f, -16711936),
            Parameters(43.459717f, 60.587143f, 0.0f, -15564557),
            Parameters(29.162718f, 52.332767f, 60.0f, -16711936),
            Parameters(57.756714f, 52.33276f, 300.0f, -16711936)
        ),
        // 1 capla 4d 2c
        listOf(
            Parameters(44.718605f, 71.0875f, 0.0f, -65536),
            Parameters(44.08916f, 55.527878f, 270.0f, -15564557),
            Parameters(55.58917f, 40.36121f, 0.0f, -15564557),
            Parameters(62.497288f, 32.066948f, 330.0f, -65536)
        ),
        // 2 stol 4d 2c
        listOf(
            Parameters(30.48481f, 34.59658f, 270.0f, -65536),
            Parameters(57.151485f, 34.596584f, 270.0f, -65536),
            Parameters(18.98481f, 49.763245f, 0.0f, -256),
            Parameters(68.65149f, 49.763245f, 0.0f, -256)
        ),
        // 3 strelka 4d 2c
        listOf(
            Parameters(42.31808f, 72.82744f, 0.0f, -15564557),
            Parameters(42.31808f, 46.160774f, 0.0f, -15564557),
            Parameters(34.124866f, 38.582783f, 30.0f, -826845),
            Parameters(50.63363f, 38.582783f, 330.0f, -826845)
        ),
        // 4 romb 4d 2c
        listOf(
            Parameters(28.532042f, 34.04553f, 30.0f, -428328),
            Parameters(28.532042f, 58.972874f, 330.0f, -15564557),
            Parameters(45.0408f, 34.045532f, 330.0f, -15564557),
            Parameters(45.040817f, 58.97288f, 30.0f, -428328)
        ),
        // 5 morozenka 4d 2c
        listOf(
            Parameters(36.786415f, 48.342533f, 270.0f, -428328),
            Parameters(28.53205f, 34.045532f, 30.0f, -15564557),
            Parameters(45.0408f, 34.045532f, 330.0f, -15564557),
            Parameters(37.024673f, 64.426994f, 0.0f, -428328)
        ),
        // 6 taburet 5d 2c
        listOf(
            Parameters(47.12592f, 31.6721f, 270.0f, -65536),
            Parameters(47.125923f, 35.33877f, 270.0f, -65536),
            Parameters(47.125923f, 28.00544f, 270.0f, -256),
            Parameters(35.625923f, 50.505436f, 0.0f, -256),
            Parameters(58.625935f, 50.505436f, 0.0f, -256)
        ),
        // 7 stul so spinkoj 5d 2c
        listOf(
            Parameters(29.00721f, 63.980232f, 0.0f, -256),
            Parameters(59.340538f, 63.980232f, 0.0f, -256),
            Parameters(44.173878f, 48.813576f, 270.0f, -65536),
            Parameters(44.173885f, 45.146896f, 270.0f, -65536),
            Parameters(29.00721f, 29.980227f, 0.0f, -65536)
        ),
        // 8 derevo mal 5d 2c
        listOf(
            Parameters(46.103996f, 67.92872f, 0.0f, -10151157),
            Parameters(46.104f, 41.26205f, 0.0f, -10151157),
            Parameters(36.016273f, 43.965042f, 330.0f, -16711936),
            Parameters(56.064003f, 30.29013f, 30.0f, -16711936),
            Parameters(55.982574f, 53.97475f, 30.0f, -16711936)
        ),
        // 9 bokal 5d 2c
        listOf(
            Parameters(44.724773f, 75.99056f, 270.0f, -15564557),
            Parameters(44.724773f, 60.348495f, 0.0f, -15564557),
            Parameters(35.007618f, 36.38483f, 330.0f, -256),
            Parameters(54.18304f, 36.38483f, 30.0f, -256),
            Parameters(44.724773f, 22.087828f, 270.0f, -256),
        ),
        // 10 skvorecnik 5d 2c
        listOf(
            Parameters(47.839226f, 80.286385f, 270.0f, -16711936),
            Parameters(47.24872f, 64.93047f, 0.0f, -16711936),
            Parameters(47.08827f, 49.370846f, 270.0f, -16711936),
            Parameters(34.38216f, 44.19226f, 60.0f, -826845),
            Parameters(59.3095f, 44.19226f, 300.0f, -826845)
        ),
        //11 romb na podstavke 5d 2c
        listOf(
            Parameters(46.853775f, 80.01715f, 270.0f, -7829368),
            Parameters(34.574017f, 69.82486f, 300.0f, -10151157),
            Parameters(59.501377f, 69.82486f, 60.0f, -10151157),
            Parameters(34.574024f, 53.3161f, 60.0f, -10151157),
            Parameters(59.50136f, 53.3161f, 300.0f, -10151157)
        ),
        // 12 capla 6d 3c
        listOf(
            Parameters(32.086544f, 62.95623f, 30.0f, -65536),
            Parameters(48.595306f, 62.95623f, 330.0f, -65536),
            Parameters(39.713924f, 47.991047f, 270.0f, -15564557),
            Parameters(39.713917f, 44.324398f, 270.0f, -15564557),
            Parameters(28.213917f, 29.157726f, 0.0f, -15564557),
            Parameters(22.521477f, 18.963192f, 30.0f, -826845)
        ),
        //13 flag 6d 3c
        listOf(
            Parameters(31.055984f, 71.799095f, 0.0f, -10151157),
            Parameters(31.055984f, 41.465763f, 0.0f, -14788283),
            Parameters(46.222652f, 56.63243f, 270.0f, -14788283),
            Parameters(46.222652f, 26.299091f, 270.0f, -14788283),
            Parameters(55.683937f, 33.514576f, 60.0f, -16711936),
            Parameters(55.683937f, 50.023327f, 300.0f, -16711936)
        ),
        // 14 dom 6d 3c
        listOf(
            Parameters(45.353485f, 67.29956f, 270.0f, -10151157),
            Parameters(30.186817f, 52.132885f, 0.0f, -256),
            Parameters(60.520157f, 52.132885f, 0.0f, -256),
            Parameters(45.35349f, 36.966217f, 270.0f, -826845),
            Parameters(37.099106f, 22.669207f, 30.0f, -826845),
            Parameters(53.607872f, 22.669209f, 330.0f, -826845)
        ),
        //  15 meduza 6d 3c
        listOf(
            Parameters(33.586098f, 28.122805f, 30.0f, -15564557),
            Parameters(50.094864f, 28.122803f, 330.0f, -15564557),
            Parameters(41.84049f, 42.41982f, 270.0f, -15564557),
            Parameters(31.296654f, 57.19405f, 30.0f, -428328),
            Parameters(41.38436f, 59.89705f, 0.0f, -65536),
            Parameters(51.472076f, 57.19405f, 330.0f, -428328)
        ),
        //16 derevo 6d 3c
        listOf(
            Parameters(45.696384f, 73.527855f, 270.0f, -10151157),
            Parameters(45.19377f, 58.05317f, 0.0f, -15564557),
            Parameters(41.40477f, 48.737354f, 0.0f, -16711936),
            Parameters(48.988945f, 48.737354f, 0.0f, -16711936),
            Parameters(37.85027f, 42.571648f, 0.0f, -16711936),
            Parameters(52.777943f, 41.781948f, 0.0f, -16711936)
        ),
        // 17 TV 6d 3c
        listOf(
            Parameters(28.76654f, 33.811222f, 0.0f, -7829368),
            Parameters(59.099876f, 33.811222f, 0.0f, -7829368),
            Parameters(43.93321f, 18.64456f, 270.0f, -10151157),
            Parameters(43.933197f, 48.977898f, 270.0f, -10151157),
            Parameters(35.47443f, 64.37074f, 30.0f, -16777216),
            Parameters(51.98319f, 64.37074f, 330.0f, -16777216)
        ),
        // 18 venik 7d 3c
        // mozet peredelat ugol
        listOf(
            Parameters(25.767427f, 43.682426f, 270.0f, -826845),
            Parameters(41.244324f, 44.31158f, 0.0f, -65536),
            Parameters(55.541332f, 25.899302f, 60.0f, -256),
            Parameters(55.54256f, 33.967426f, 60.0f, -256),
            Parameters(58.245552f, 44.05515f, 270.0f, -256),
            Parameters(55.542553f, 54.142853f, 300.0f, -256),
            Parameters(55.541336f, 62.723873f, 300.0f, -256)
        ),
        //19 pesocnye casy 7d 3c
        listOf(
            Parameters(42.79325f, 18.164818f, 270.0f, -256),
            Parameters(34.538868f, 32.461823f, 330.0f, -428328),
            Parameters(51.047634f, 32.461823f, 30.0f, -428328),
            Parameters(35.094265f, 61.760006f, 30.0f, -428328),
            Parameters(42.79325f, 47.396606f, 270.0f, -826845),
            Parameters(42.79325f, 76.05702f, 270.0f, -256),
            Parameters(51.603027f, 61.76001f, 330.0f, -428328),
        ),
        // 20 capla 7d 3c
        listOf(
            Parameters(39.275772f, 73.38283f, 0.0f, -65536),
            Parameters(49.223442f, 73.45659f, 0.0f, -65536),
            Parameters(44.965443f, 57.896965f, 270.0f, -15564557),
            Parameters(48.632114f, 54.230305f, 270.0f, -256),
            Parameters(33.465443f, 42.730297f, 0.0f, -15564557),
            Parameters(26.44039f, 27.255007f, 270.0f, -256),
            Parameters(30.895538f, 23.186092f, 270.0f, -65536)
        ),
        // 21 roza 7d 4c
        listOf(
            Parameters(39.75094f, 79.06691f, 0.0f, -16711936),
            Parameters(39.75094f, 52.400246f, 0.0f, -16711936),
            Parameters(39.57522f, 36.96567f, 270.0f, -428328),
            Parameters(28.075218f, 21.798994f, 0.0f, -65536),
            Parameters(50.95289f, 21.560354f, 0.0f, -65536),
            Parameters(39.01917f, 21.402277f, 330.0f, -826845),
            Parameters(39.452873f, 6.3936977f, 270.0f, -826845)
        ),
        // 22  kust 7d 3c
        listOf(
            Parameters(35.8909f, 83.01539f, 330.0f, -826845),
            Parameters(46.819836f, 73.304726f, 30.0f, -65536),
            Parameters(36.27049f, 63.06295f, 330.0f, -256),
            Parameters(46.458828f, 51.657497f, 30.0f, -826845),
            Parameters(36.504986f, 40.128796f, 330.0f, -65536),
            Parameters(46.06973f, 28.359583f, 30.0f, -256),
            Parameters(35.442818f, 17.74916f, 330.0f, -826845)
        ),
        // 23 elka 7d 3c
        listOf(
            Parameters(45.59489f, 83.09783f, 0.0f, -10151157),
            Parameters(46.934f, 67.13902f, 270.0f, -14788283),
            Parameters(38.67962f, 52.842003f, 30.0f, -16711936),
            Parameters(55.188374f, 52.842003f, 330.0f, -16711936),
            Parameters(46.853775f, 38.072113f, 270.0f, -14788283),
            Parameters(38.599396f, 23.775105f, 30.0f, -16711936),
            Parameters(55.108162f, 23.775108f, 330.0f, -16711936)
        ),
        // 24 morkov 8d 3c
        listOf(
            Parameters(33.740376f, 79.37932f, 330.0f, -826845),
            Parameters(50.249146f, 79.379326f, 30.0f, -826845),
            Parameters(26.828083f, 53.58231f, 0.0f, -826845),
            Parameters(57.161438f, 53.58232f, 0.0f, -826845),
            Parameters(41.99475f, 38.41565f, 270.0f, -826845),
            Parameters(41.315407f, 21.063461f, 0.0f, -14788283),
            Parameters(31.227688f, 23.766457f, 330.0f, -16711936),
            Parameters(51.40313f, 23.766457f, 30.0f, -16711936)
        ),
        // 25 grib 8d 3c
        listOf(
            Parameters(32.59365f, 72.74934f, 0.0f, -256),
            Parameters(55.593655f, 72.74934f, 0.0f, -256),
            Parameters(44.093655f, 87.91601f, 270.0f, -256),
            Parameters(30.34632f, 57.18537f, 270.0f, -826845),
            Parameters(57.01299f, 57.185364f, 270.0f, -826845),
            Parameters(22.09194f, 42.888363f, 30.0f, -826845),
            Parameters(65.26738f, 42.888363f, 330.0f, -826845),
            Parameters(43.67966f, 30.424696f, 270.0f, -65536)
        ),
        // 26 lestnica 8d 3c
        listOf(
            Parameters(29.402153f, 80.173355f, 0.0f, -10151157),
            Parameters(59.73549f, 80.173355f, 0.0f, -10151157),
            Parameters(44.568817f, 65.00669f, 270.0f, -826845),
            Parameters(29.402153f, 49.840015f, 0.0f, -10151157),
            Parameters(59.735485f, 49.840015f, 0.0f, -10151157),
            Parameters(44.568817f, 34.67335f, 270.0f, -256),
            Parameters(29.40215f, 19.506685f, 0.0f, -10151157),
            Parameters(59.735485f, 19.506685f, 0.0f, -10151157)
        ),
        // 27 lodka 8d 3c
        listOf(
            Parameters(42.515553f, 78.43775f, 270.0f, -10151157),
            Parameters(64.10328f, 65.97407f, 30.0f, -10151157),
            Parameters(20.927837f, 65.974075f, 330.0f, -10151157),
            Parameters(29.182215f, 51.67708f, 270.0f, -10151157),
            Parameters(55.84889f, 51.67708f, 270.0f, -10151157),
            Parameters(40.221138f, 33.37136f, 0.0f, -7829368),
            Parameters(54.518166f, 41.625744f, 60.0f, -826845),
            Parameters(54.51815f, 25.11697f, 300.0f, -826845)
        ),
        // 28 krovat 8d 3c
        listOf(
            Parameters(20.42951f, 80.06032f, 0.0f, -16777216),
            Parameters(77.42951f, 80.06032f, 0.0f, -16777216),
            Parameters(35.71851f, 68.360504f, 270.0f, -7829368),
            Parameters(62.26284f, 68.560326f, 270.0f, -7829368),
            Parameters(35.718513f, 64.69384f, 270.0f, -256),
            Parameters(62.140514f, 64.4988f, 270.0f, -256),
            Parameters(20.42951f, 53.39365f, 0.0f, -16777216),
            Parameters(77.42951f, 53.39365f, 0.0f, -16777216)
        ),
        // 29 sobaka 8d 3c
        listOf(
            Parameters(35.992794f, 80.5682f, 0.0f, -10151157),
            Parameters(47.49279f, 65.40154f, 270.0f, -10151157),
            Parameters(58.9928f, 80.5682f, 0.0f, -10151157),
            Parameters(47.4928f, 61.734863f, 270.0f, -826845),
            Parameters(26.529123f, 51.64715f, 300.0f, -826845),
            Parameters(65.90508f, 47.437862f, 30.0f, -256),
            Parameters(76.21696f, 35.46872f, 300.0f, -256),
            Parameters(72.08523f, 28.88524f, 300.0f, -826845)
        ),
        // 30 kust 9d 3c
        listOf(
            Parameters(45.193768f, 85.54069f, 270.0f, -65536),
            Parameters(44.564323f, 69.82486f, 0.0f, -826845),
            Parameters(58.868732f, 71.72099f, 60.0f, -16711936),
            Parameters(30.185873f, 71.71666f, 300.0f, -16711936),
            Parameters(30.472595f, 61.501446f, 300.0f, -16711936),
            Parameters(58.708286f, 61.611145f, 60.0f, -16711936),
            Parameters(30.267315f, 51.412575f, 300.0f, -16711936),
            Parameters(58.861336f, 51.412575f, 60.0f, -16711936),
            Parameters(44.79883f, 42.654087f, 0.0f, -16711936)
        ),
        // 31 kubik 9d 3c
        listOf(
            Parameters(28.013676f, 55.29357f, 0.0f, -826845),
            Parameters(43.180344f, 70.46024f, 270.0f, -826845),
            Parameters(58.347008f, 55.29357f, 0.0f, -826845),
            Parameters(43.180355f, 40.1269f, 270.0f, -826845),
            Parameters(40.47735f, 30.039198f, 60.0f, -65536),
            Parameters(66.27435f, 23.126917f, 270.0f, -65536),
            Parameters(68.97736f, 33.214622f, 60.0f, -65536),
            Parameters(83.27436f, 41.469f, 0.0f, -428328),
            Parameters(72.64401f, 63.547955f, 60.0f, -428328)
        ),
        // 32 chashka 9d 3c
        listOf(
            Parameters(42.553f, 93.35893f, 270.0f, -15564557),
            Parameters(24.140717f, 79.06191f, 330.0f, -15564557),
            Parameters(61.044014f, 78.74579f, 30.0f, -15564557),
            Parameters(17.307142f, 52.94878f, 0.0f, -16711936),
            Parameters(67.95629f, 52.94878f, 0.0f, -16711936),
            Parameters(28.80713f, 37.78212f, 270.0f, -16711936),
            Parameters(55.473812f, 37.78212f, 270.0f, -16711936),
            Parameters(75.17682f, 38.60039f, 300.0f, -10151157),
            Parameters(77.55279f, 57.485107f, 30.0f, -10151157)
        ),
        // 33 povoreshka 9d 3c
        listOf(
            Parameters(42.984535f, 90.33069f, 270.0f, -15564557),
            Parameters(21.396826f, 81.533714f, 330.0f, -15564557),
            Parameters(64.57227f, 81.533714f, 30.0f, -15564557),
            Parameters(29.651213f, 67.236694f, 270.0f, -16711936),
            Parameters(56.317898f, 67.2367f, 270.0f, -16711936),
            Parameters(71.484566f, 55.7367f, 0.0f, -7829368),
            Parameters(71.484566f, 29.07003f, 0.0f, -7829368),
            Parameters(77.86619f, 15.72371f, 300.0f, -7829368),
            Parameters(42.98455f, 93.9974f, 270.0f, -15564557)
        ),
        // 34 ezik 9d 3c
        listOf(
            Parameters(24.014318f, 67.51723f, 60.0f, -10151157),
            Parameters(26.717306f, 77.60494f, 270.0f, -10151157),
            Parameters(53.38398f, 77.60494f, 270.0f, -10151157),
            Parameters(42.899044f, 48.632504f, 30.0f, -14788283),
            Parameters(61.848785f, 50.773056f, 30.0f, -14788283),
            Parameters(71.9365f, 68.245476f, 270.0f, -14788283),
            Parameters(53.026123f, 48.464905f, 30.0f, -16711936),
            Parameters(69.233505f, 58.15778f, 60.0f, -16711936),
            Parameters(76.20068f, 73.80832f, 270.0f, -16711936)
        ),
        // 35 ryba 9d 3c
        listOf(
            Parameters(27.992638f, 40.135063f, 60.0f, -428328),
            Parameters(27.992647f, 56.643818f, 300.0f, -428328),
            Parameters(52.919994f, 40.13506f, 300.0f, -428328),
            Parameters(52.92f, 56.643818f, 60.0f, -428328),
            Parameters(47.36861f, 19.417004f, 30.0f, -826845),
            Parameters(47.368614f, 77.36186f, 330.0f, -826845),
            Parameters(73.63805f, 34.58367f, 30.0f, -16711936),
            Parameters(73.63804f, 62.1952f, 330.0f, -16711936),
            Parameters(77.81758f, 48.103863f, 0.0f, -16711936)
        ),
        // 36  kachely 10d 4c
        listOf(
            Parameters(46.934f, 83.09783f, 270.0f, -10151157),
            Parameters(38.67962f, 68.80082f, 30.0f, -14788283),
            Parameters(55.188385f, 68.80082f, 330.0f, -14788283),
            Parameters(49.007057f, 53.002583f, 300.0f, -7829368),
            Parameters(72.101074f, 66.33592f, 300.0f, -7829368),
            Parameters(25.913044f, 39.66926f, 300.0f, -7829368),
            Parameters(25.913042f, 23.160501f, 60.0f, -826845),
            Parameters(40.210056f, 31.414875f, 0.0f, -826845),
            Parameters(86.39809f, 58.08154f, 0.0f, -826845),
            Parameters(72.1011f, 49.827156f, 60.0f, -826845)
        ),
        // 37 ziraf 10d 4c
        listOf(
            Parameters(34.74978f, 82.288506f, 30.0f, -10151157),
            Parameters(44.837498f, 84.9915f, 0.0f, -10151157),
            Parameters(61.34238f, 84.227135f, 30.0f, -10151157),
            Parameters(71.50414f, 84.99149f, 0.0f, -10151157),
            Parameters(56.337486f, 69.82483f, 270.0f, -256),
            Parameters(60.004166f, 66.15815f, 270.0f, -256),
            Parameters(44.83749f, 54.65816f, 0.0f, -826845),
            Parameters(44.83749f, 27.991488f, 0.0f, -826845),
            Parameters(37.84587f, 15.710525f, 60.0f, -65536),
            Parameters(39.801537f, 9.812378f, 60.0f, -65536)
        ),
        // 38 pelikan 10d 4c
        listOf(
            Parameters(37.492348f, 60.10985f, 270.0f, -10151157),
            Parameters(41.20839f, 56.07893f, 270.0f, -15564557),
            Parameters(56.37505f, 44.578922f, 0.0f, -15564557),
            Parameters(52.708385f, 40.91225f, 0.0f, -15564557),
            Parameters(37.541714f, 52.41226f, 270.0f, -256),
            Parameters(37.541714f, 48.745598f, 270.0f, -256),
            Parameters(67.87505f, 29.412262f, 270.0f, -826845),
            Parameters(64.20839f, 25.74559f, 270.0f, -826845),
            Parameters(70.30978f, 40.120117f, 300.0f, -826845),
            Parameters(70.2419f, 44.658684f, 300.0f, -826845)
        ),
        // 39 melnica 10d 4c
        listOf(
            Parameters(45.11972f, 93.520096f, 270.0f, -16711936),
            Parameters(45.11972f, 89.853424f, 270.0f, -256),
            Parameters(45.119724f, 86.18675f, 270.0f, -16711936),
            Parameters(45.11972f, 82.52007f, 270.0f, -256),
            Parameters(36.865345f, 68.22306f, 30.0f, -65536),
            Parameters(53.374107f, 68.22306f, 330.0f, -65536),
            Parameters(32.591698f, 57.04607f, 60.0f, -15564557),
            Parameters(57.51904f, 57.046062f, 300.0f, -15564557),
            Parameters(36.800983f, 34.985924f, 330.0f, -15564557),
            Parameters(53.309734f, 34.98592f, 30.0f, -15564557)
        ),
        // 40 kot 10d 4c
        listOf(
            Parameters(38.501255f, 62.37952f, 330.0f, -826845),
            Parameters(55.01003f, 62.37952f, 30.0f, -826845),
            Parameters(38.50126f, 37.45218f, 30.0f, -826845),
            Parameters(55.01002f, 37.45218f, 330.0f, -826845),
            Parameters(25.254316f, 32.334454f, 330.0f, -256),
            Parameters(28.615534f, 22.663084f, 300.0f, -256),
            Parameters(65.87323f, 29.268513f, 30.0f, -15564557),
            Parameters(62.39476f, 19.909555f, 60.0f, -15564557),
            Parameters(24.582594f, 69.347565f, 60.0f, -10151157),
            Parameters(69.11184f, 68.961395f, 300.0f, -10151157)
        ),
        // 41 ptica 10d 4c
        listOf(
            Parameters(57.71784f, 78.19477f, 270.0f, -10151157),
            Parameters(60.207836f, 62.478943f, 0.0f, -7829368),
            Parameters(50.120132f, 59.77595f, 30.0f, -7829368),
            Parameters(44.568752f, 39.057896f, 300.0f, -15564557),
            Parameters(69.49609f, 39.057896f, 60.0f, -15564557),
            Parameters(26.87821f, 29.307348f, 330.0f, -15564557),
            Parameters(36.095818f, 12.702219f, 300.0f, -15564557),
            Parameters(15.000898f, 17.45957f, 60.0f, -256),
            Parameters(61.892822f, 19.614506f, 270.0f, -15564557),
            Parameters(77.659454f, 22.369396f, 30.0f, -15564557)
        ),
        // 42 devocka 11d 4c
        listOf(
            Parameters(39.670715f, 82.937294f, 0.0f, -10151157),
            Parameters(50.648945f, 82.85485f, 0.0f, -10151157),
            Parameters(45.193775f, 66.82227f, 270.0f, -65536),
            Parameters(36.939392f, 52.525265f, 30.0f, -65536),
            Parameters(53.448162f, 52.525265f, 330.0f, -65536),
            Parameters(46.063885f, 36.96567f, 270.0f, -256),
            Parameters(33.54382f, 32.14937f, 60.0f, -428328),
            Parameters(58.47117f, 32.149357f, 300.0f, -428328),
            Parameters(24.784597f, 52.74181f, 60.0f, -256),
            Parameters(65.34212f, 52.20855f, 300.0f, -256),
            Parameters(79.64035f, 60.660904f, 0.0f, -428328)
        ),
        // 43 byk 11d 4c
        listOf(
            Parameters(33.586098f, 81.27979f, 30.0f, -16777216),
            Parameters(43.67381f, 83.982796f, 0.0f, -16777216),
            Parameters(58.70211f, 81.674644f, 30.0f, -16777216),
            Parameters(68.789825f, 84.37764f, 0.0f, -16777216),
            Parameters(51.998383f, 66.982796f, 270.0f, -7829368),
            Parameters(26.201378f, 60.070503f, 300.0f, -256),
            Parameters(26.201384f, 43.561756f, 60.0f, -256),
            Parameters(33.586067f, 26.01913f, 330.0f, -10151157),
            Parameters(52.470814f, 44.903854f, 300.0f, -10151157),
            Parameters(75.96206f, 56.89507f, 60.0f, -7829368),
            Parameters(72.86366f, 49.77117f, 60.0f, -7829368),
        ),
        // 44 traktor 11d 4c
        listOf(
            Parameters(71.73529f, 80.802505f, 270.0f, -7829368),
            Parameters(40.409508f, 80.243835f, 270.0f, -7829368),
            Parameters(25.242842f, 65.077156f, 0.0f, -7829368),
            Parameters(40.409504f, 49.910503f, 270.0f, -7829368),
            Parameters(86.901955f, 65.635826f, 0.0f, -7829368),
            Parameters(71.73529f, 50.469162f, 270.0f, -7829368),
            Parameters(86.901955f, 35.30249f, 0.0f, -10151157),
            Parameters(75.40196f, 20.135828f, 270.0f, -10151157),
            Parameters(58.8218f, 35.613487f, 30.0f, -15564557),
            Parameters(35.46312f, 34.430355f, 0.0f, -256),
            Parameters(75.40196f, 16.469158f, 270.0f, -256)
        ),
        // 45 piramida 11d 4c
        listOf(
            Parameters(21.138035f, 92.64796f, 270.0f, -14788283),
            Parameters(74.47136f, 92.64797f, 270.0f, -14788283),
            Parameters(18.807829f, 78.35094f, 30.0f, -14788283),
            Parameters(39.550327f, 78.350945f, 30.0f, -826845),
            Parameters(56.059074f, 78.350945f, 330.0f, -826845),
            Parameters(77.20268f, 78.58959f, 330.0f, -14788283),
            Parameters(32.141163f, 55.25693f, 30.0f, -14788283),
            Parameters(63.869347f, 55.495575f, 330.0f, -14788283),
            Parameters(47.93921f, 92.56671f, 270.0f, -826845),
            Parameters(47.978783f, 38.021454f, 270.0f, -65536),
            Parameters(47.978783f, 41.68812f, 270.0f, -256)
        ),
        // 46 tank 11d 4c
        listOf(
            Parameters(31.4571f, 90.43941f, 270.0f, -14788283),
            Parameters(58.12377f, 90.43941f, 270.0f, -14788283),
            Parameters(16.290436f, 75.27273f, 0.0f, -14788283),
            Parameters(73.29045f, 75.27273f, 0.0f, -14788283),
            Parameters(31.4571f, 60.10607f, 270.0f, -16711936),
            Parameters(58.12378f, 60.106064f, 270.0f, -16711936),
            Parameters(30.074791f, 44.31158f, 0.0f, -15564557),
            Parameters(59.097054f, 44.78453f, 0.0f, -15564557),
            Parameters(45.241463f, 29.144922f, 270.0f, -15564557),
            Parameters(74.1925f, 39.285908f, 270.0f, -7829368),
            Parameters(45.24147f, 25.47825f, 270.0f, -7829368)
        ),
        // 47 pavlin 11d 4c
        listOf(
            Parameters(36.739487f, 79.50515f, 30.0f, -14788283),
            Parameters(52.61881f, 82.10207f, 330.0f, -14788283),
            Parameters(45.22518f, 66.03208f, 300.0f, -14788283),
            Parameters(50.773468f, 64.55104f, 300.0f, -15564557),
            Parameters(43.388756f, 47.0084f, 30.0f, -256),
            Parameters(52.61133f, 60.97765f, 300.0f, -256),
            Parameters(55.135296f, 57.74076f, 300.0f, -65536),
            Parameters(46.063885f, 32.857742f, 270.0f, -65536),
            Parameters(65.76365f, 47.4297f, 0.0f, -14788283),
            Parameters(73.32935f, 55.426266f, 30.0f, -15564557),
            Parameters(75.70083f, 64.55103f, 60.0f, -256)
        ),
        // 48 cvetok 12d 4c
        listOf(
            Parameters(45.668938f, 93.83684f, 270.0f, -7829368),
            Parameters(45.668938f, 90.170166f, 270.0f, -7829368),
            Parameters(45.668938f, 86.5035f, 270.0f, -7829368),
            Parameters(45.668938f, 82.83684f, 270.0f, -7829368),
            Parameters(44.79882f, 67.06092f, 0.0f, -16711936),
            Parameters(34.690712f, 67.776855f, 330.0f, -16711936),
            Parameters(58.39973f, 60.926575f, 60.0f, -16711936),
            Parameters(44.79882f, 40.39425f, 0.0f, -65536),
            Parameters(34.711105f, 43.097244f, 330.0f, -65536),
            Parameters(54.88654f, 43.097244f, 30.0f, -65536),
            Parameters(54.67244f, 31.954134f, 30.0f, -256),
            Parameters(34.85116f, 31.71332f, 330.0f, -256)
        ),
        // 49 olen 12d 4c
        listOf(
            Parameters(25.131815f, 83.33214f, 0.0f, -10151157),
            Parameters(35.219536f, 80.62914f, 330.0f, -10151157),
            Parameters(46.84408f, 83.46436f, 0.0f, -10151157),
            Parameters(56.9318f, 80.76137f, 330.0f, -10151157),
            Parameters(38.519493f, 66.46436f, 270.0f, -14788283),
            Parameters(38.519516f, 62.797703f, 270.0f, -14788283),
            Parameters(34.85283f, 59.131035f, 270.0f, -14788283),
            Parameters(50.0195f, 47.631035f, 0.0f, -826845),
            Parameters(53.285053f, 32.298252f, 270.0f, -14788283),
            Parameters(53.28506f, 28.631584f, 270.0f, -14788283),
            Parameters(34.872776f, 14.334577f, 330.0f, -16777216),
            Parameters(51.381535f, 14.334579f, 30.0f, -16777216)
        ),
        // 50 raketa 12d 4c
        listOf(
            Parameters(45.07652f, 68.08926f, 270.0f, -14788283),
            Parameters(26.66423f, 82.38626f, 30.0f, -65536),
            Parameters(63.488815f, 82.38626f, 330.0f, -65536),
            Parameters(41.17039f, 84.065254f, 0.0f, -256),
            Parameters(49.143215f, 84.12183f, 0.0f, -256),
            Parameters(45.07652f, 64.4226f, 270.0f, -15564557),
            Parameters(33.57652f, 49.25593f, 0.0f, -15564557),
            Parameters(56.576523f, 49.25593f, 0.0f, -15564557),
            Parameters(45.07652f, 34.08926f, 270.0f, -15564557),
            Parameters(36.82214f, 16.125587f, 30.0f, -15564557),
            Parameters(45.07651f, 30.42259f, 270.0f, -256),
            Parameters(53.330902f, 16.125587f, 330.0f, -15564557)
        ),
        // 51 dom 12d 4c
        listOf(
            Parameters(25.23193f, 92.03125f, 270.0f, -256),
            Parameters(25.231934f, 88.36458f, 270.0f, -256),
            Parameters(25.23193f, 84.69792f, 270.0f, -256),
            Parameters(25.231934f, 81.03124f, 270.0f, -256),
            Parameters(51.898605f, 92.03125f, 270.0f, -15564557),
            Parameters(51.898605f, 88.364586f, 270.0f, -15564557),
            Parameters(51.898598f, 84.69792f, 270.0f, -15564557),
            Parameters(51.898598f, 81.031235f, 270.0f, -15564557),
            Parameters(16.977552f, 66.73424f, 30.0f, -826845),
            Parameters(33.486317f, 66.73423f, 330.0f, -826845),
            Parameters(60.15299f, 66.73424f, 330.0f, -65536),
            Parameters(41.74069f, 52.437233f, 270.0f, -65536)
        ),
        // 52 piramida 12d 4c
        listOf(
            Parameters(18.690628f, 6.3619204f, 270.0f, -256 ),
            Parameters(45.357292f, 6.3619175f, 270.0f, -256 ),
            Parameters(72.02396f, 6.3619175f, 270.0f, -256 ),
            Parameters(10.436252f, 20.658924f, 330.0f, -826845 ),
            Parameters(80.27834f, 20.658924f, 30.0f, -826845 ),
            Parameters(32.023964f, 33.122585f, 270.0f, -256 ),
            Parameters(58.69063f, 33.122593f, 270.0f, -256 ),
            Parameters(23.769587f, 47.419598f, 330.0f, -428328 ),
            Parameters(66.945015f, 47.419598f, 30.0f, -428328 ),
            Parameters(45.3573f, 59.883274f, 270.0f, -256 ),
            Parameters(37.102924f, 74.180275f, 330.0f, -65536 ),
            Parameters(53.611687f, 74.18026f, 30.0f, -65536 )

        ),
        // 53 lico 12d 4c
        listOf(
            Parameters(37.59814f, 82.78542f, 300.0f, -65536),
            Parameters(62.525494f, 82.78542f, 60.0f, -65536),
            Parameters(47.99541f, 68.391685f, 270.0f, -256),
            Parameters(47.64366f, 53.15445f, 0.0f, -256),
            Parameters(29.156986f, 48.340626f, 270.0f, -15564557),
            Parameters(66.17709f, 48.414387f, 270.0f, -15564557),
            Parameters(20.902609f, 34.04361f, 30.0f, -15564557),
            Parameters(37.411373f, 34.04361f, 330.0f, -15564557),
            Parameters(57.92272f, 34.11738f, 30.0f, -15564557),
            Parameters(74.43149f, 34.117374f, 330.0f, -15564557),
            Parameters(27.748325f, 11.280143f, 270.0f, -10151157),
            Parameters(67.352196f, 10.590205f, 270.0f, -10151157)
        ),
        // 54 drakon 13d 5c  problema s shirinoj nexus 9
        listOf(Parameters(74.66186f, 48.469814f, 30.0f, -65536 ),
            Parameters(73.71759f, 42.633236f, 30.0f, -14788283 ),
            Parameters(73.61594f, 34.678818f, 30.0f, -16711936 ),
            Parameters(59.878258f, 46.480034f, 330.0f, -65536 ),
            Parameters(59.662228f, 38.706596f, 330.0f, -14788283 ),
            Parameters(59.936962f, 30.971975f, 330.0f, -16711936 ),
            Parameters(46.04484f, 42.313366f, 30.0f, -65536 ),
            Parameters(45.75805f, 35.543262f, 30.0f, -14788283 ),
            Parameters(45.731586f, 27.464453f, 30.0f, -16711936 ),
            Parameters(35.939816f, 35.19061f, 0.0f, -256 ),
            Parameters(31.471483f, 21.761356f, 60.0f, -256 ),
            Parameters(36.930305f, 8.659249f, 300.0f, -14788283 ),
            Parameters(25.75463f, 13.483743f, 270.0f, -826845 )

        ),
        // 55 petushok 13d 5c
        listOf(
            Parameters(30.472786f, 68.702255f, 300.0f, -256 ),
            Parameters(55.400143f, 68.70227f, 60.0f, -256 ),
            Parameters(35.221043f, 26.206596f, 330.0f, -65536 ),
            Parameters(42.58796f, 62.797737f, 270.0f, -65536 ),
            Parameters(57.85935f, 51.484375f, 0.0f, -65536 ),
            Parameters(42.58797f, 39.79775f, 270.0f, -65536 ),
            Parameters(27.077778f, 33.320568f, 0.0f, -65536 ),
            Parameters(31.087963f, 51.29774f, 0.0f, -65536 ),
            Parameters(24.63135f, 14.166666f, 60.0f, -256 ),
            Parameters(54.087963f, 24.631077f, 0.0f, -16711936 ),
            Parameters(64.77164f, 25.687366f, 30.0f, -15564557 ),
            Parameters(71.15636f, 33.07209f, 60.0f, -826845 ),
            Parameters(67.78041f, 51.95681f, 330.0f, -65536 )
        ),
        // 56 begemot 13d 5c
        listOf(
            Parameters(51.08091f, 36.09271f, 0.0f, -14788283),
            Parameters(66.24758f, 24.59271f, 270.0f, -15564557),
            Parameters(66.24758f, 28.259377f, 270.0f, -15564557),
            Parameters(66.24758f, 31.926037f, 270.0f, -15564557),
            Parameters(66.24757f, 35.592697f, 270.0f, -15564557),
            Parameters(66.247574f, 39.25936f, 270.0f, -15564557),
            Parameters(81.414246f, 36.09271f, 0.0f, -14788283),
            Parameters(66.24758f, 42.926037f, 270.0f, -10151157),
            Parameters(47.04454f, 30.903385f, 0.0f, -15564557),
            Parameters(32.747528f, 12.491098f, 300.0f, -15564557),
            Parameters(30.914194f, 15.666522f, 300.0f, -826845),
            Parameters(31.64763f, 31.15332f, 60.0f, -65536),
            Parameters(32.9808f, 34.855103f, 60.0f, -15564557)
        ),
        // 57 zebra 13d 5c
        listOf(
            Parameters(32.086544f, 76.142426f, 0.0f, -15564557),
            Parameters(36.110043f, 65.008575f, 0.0f, -65536),
            Parameters(39.905216f, 76.30297f, 0.0f, -15564557),
            Parameters(28.130926f, 65.16478f, 0.0f, -65536),
            Parameters(18.043213f, 62.46179f, 30.0f, -428328),
            Parameters(43.928715f, 64.60939f, 0.0f, -65536),
            Parameters(47.643658f, 76.54162f, 0.0f, -15564557),
            Parameters(51.27839f, 64.76993f, 0.0f, -65536),
            Parameters(55.067383f, 76.85836f, 0.0f, -15564557),
            Parameters(58.936607f, 65.008575f, 0.0f, -65536),
            Parameters(62.491116f, 54.66008f, 0.0f, -15564557),
            Parameters(66.52078f, 40.632896f, 300.0f, -826845),
            Parameters(61.807693f, 33.74464f, 300.0f, -256)
        ),
        // 58 cvetok bolshoj 13d 5c
        listOf(
            Parameters(36.54476f, 80.40771f, 330.0f, -15564557),
            Parameters(53.053543f, 80.40768f, 30.0f, -15564557),
            Parameters(44.799156f, 66.11068f, 270.0f, -15564557),
            Parameters(44.169384f, 50.629154f, 0.0f, -16711936),
            Parameters(53.770687f, 52.35213f, 30.0f, -16711936),
            Parameters(29.851107f, 49.887802f, 300.0f, -16711936),
            Parameters(34.32233f, 30.53153f, 330.0f, -65536),
            Parameters(27.15525f, 25.836489f, 330.0f, -65536),
            Parameters(54.774376f, 21.61056f, 30.0f, -256),
            Parameters(34.476624f, 22.873198f, 330.0f, -256),
            Parameters(44.564327f, 20.170206f, 0.0f, -428328),
            Parameters(54.022602f, 30.852613f, 30.0f, -65536),
            Parameters(61.33889f, 25.94045f, 30.0f, -65536)
        ),
        // 59 horse 13 d 5c
        listOf(
            Parameters(54.83289f, 52.920143f, 270.0f, -14788283),
            Parameters(54.83289f, 49.25347f, 270.0f, -256),
            Parameters(54.832886f, 45.586803f, 270.0f, -428328),
            Parameters(54.832886f, 41.92013f, 270.0f, -256),
            Parameters(32.753937f, 67.21715f, 30.0f, -14788283),
            Parameters(39.682423f, 67.23677f, 30.0f, -14788283),
            Parameters(73.24518f, 67.21715f, 330.0f, -14788283),
            Parameters(66.286285f, 67.13468f, 330.0f, -14788283),
            Parameters(39.66623f, 41.420135f, 0.0f, -428328),
            Parameters(35.085655f, 27.859789f, 60.0f, -256),
            Parameters(33.252327f, 24.684362f, 60.0f, -16711936),
            Parameters(31.418999f, 21.508934f, 60.0f, -256),
            Parameters(46.7136f, 17.378008f, 330.0f, -826845)
        )
    )
    val symmetry = listOf(

        //1ul
        // 0 z 3d 2c 2u
        listOf(
            Parameters(77.300575f, 42.30199f, 270.0f, -256 ),
            Parameters(78.405174f, 32.586975f, 60.0f, -16777216 ),
            Parameters(77.300575f, 22.499268f, 270.0f, -256 )
        ),
        // 1 malenkij priamougolnik 3d 2c 2u
        listOf(
            Parameters(77.301544f, 25.914257f, 270.0f, -428328 ),
            Parameters(62.13488f, 41.08092f, 0.0f, -15564557 ),
            Parameters(77.301544f, 56.2476f, 270.0f, -428328 )
        ),
        // 2 treugolnik 3d 2c 2u
        listOf(
            Parameters(77.104065f, 24.642933f, 270.0f, -14788283 ),
            Parameters(68.84969f, 38.93993f, 330.0f, -16711936 ),
            Parameters(82.18302f, 62.033943f, 330.0f, -16711936 )
        ),
        // 3 derevo mal 3d 2c 2u
        listOf(
            Parameters(88.08229f, 65.64207f, 0.0f, -7829368 ),
            Parameters(78.22909f, 55.81484f, 330.0f, -10151157 ),
            Parameters(73.35003f, 39.433243f, 330.0f, -10151157 )
        ),

        //2ul
        // 4 zuk2 3d 2c 3u
        listOf(
            Parameters(77.62419f, 78.70019f, 300.0f, -10151157 ),
            Parameters(77.62419f, 62.19143f, 60.0f, -10151157 ),
            Parameters(83.17557f, 41.473385f, 330.0f, -826845 )
        ),
        // 5 snezinka 3d 2c 3u
        listOf(
            Parameters(82.71353f, 32.461796f, 330.0f, -256 ),
            Parameters(82.71353f, 61.05581f, 30.0f, -256 ),
            Parameters(74.45915f, 46.758812f, 270.0f, -826845 )
        ),
        // 6 zuk 3d 2c 3u
        listOf(
            Parameters(82.79374f, 31.20349f, 30.0f, -428328 ),
            Parameters(82.79374f, 56.130833f, 330.0f, -15564557 ),
            Parameters(79.618324f, 79.224846f, 30.0f, -826845 )
        ),
        // 7 statuja romba 3d 2c 3u
        listOf(
            Parameters(78.87823f, 51.024002f, 300.0f, -15564557 ),
            Parameters(78.87825f, 34.51524f, 60.0f, -15564557 ),
            Parameters(64.54606f, 43.28758f, 0.0f, -16711936 )
        ),

        //3ul
        // 8 kvadrat 4d 2c 2u
        listOf(
            Parameters(77.11024f, 21.327942f, 270.0f, -65536 ),
            Parameters(61.943577f, 36.494606f, 0.0f, -826845 ),
            Parameters(61.943573f, 63.161274f, 0.0f, -826845 ),
            Parameters(77.11024f, 78.32795f, 270.0f, -65536 )
        ),
        // 9 elka 4d 2c 2u
        listOf(
            Parameters(88.75494f, 40.679844f, 0.0f, -10151157 ),
            Parameters(88.75494f, 67.34652f, 0.0f, -10151157 ),
            Parameters(82.968414f, 30.038668f, 30.0f, -14788283 ),
            Parameters(78.90172f, 55.55767f, 30.0f, -14788283 )
        ),
        // 10 derevo 4d 3c 1u
        listOf(
            Parameters(88.872185f, 77.80426f, 0.0f, -10151157 ),
            Parameters(84.92274f, 67.29523f, 0.0f, -14788283 ),
            Parameters(81.28801f, 59.08585f, 0.0f, -16711936 ),
            Parameters(77.51135f, 51.89614f, 0.0f, -14788283 )
        ),
        // 11 z bol 4d 2c 2u
        listOf(
            Parameters(77.02385f, 49.52705f, 270.0f, -65536 ),
            Parameters(77.22132f, 20.143398f, 270.0f, -65536 ),
            Parameters(81.211525f, 35.110775f, 30.0f, -826845 ),
            Parameters(68.76948f, 35.230045f, 30.0f, -826845 )
        ),

        //4ul
        // 12 romb 4d 2c 2u
        listOf(
            Parameters(82.55308f, 28.517653f, 330.0f, -14788283 ),
            Parameters(66.04431f, 28.517653f, 30.0f, -14788283 ),
            Parameters(66.04432f, 53.444996f, 330.0f, -15564557 ),
            Parameters(82.55308f, 53.444996f, 30.0f, -15564557 )
        ),
        // 13 babochka 4d 3c 4u
        listOf(
            Parameters(88.872185f, 47.54847f, 0.0f, -16711936 ),
            Parameters(79.004745f, 32.066948f, 330.0f, -256 ),
            Parameters(79.004745f, 61.211956f, 30.0f, -256 ),
            Parameters(73.23485f, 46.64945f, 270.0f, -826845 )
        ),
        // 14 tv 5d 3c 3u
        listOf(
            Parameters(70.9558f, 21.666828f, 300.0f, -7829368 ),
            Parameters(77.315506f, 31.57736f, 270.0f, -15564557 ),
            Parameters(62.148838f, 43.077362f, 0.0f, -15564557 ),
            Parameters(62.14883f, 69.74403f, 0.0f, -15564557 ),
            Parameters(77.31549f, 81.24404f, 270.0f, -10151157 )
        ),
        // 15 dva treugolnika 5d 3c 3u
        listOf(
            Parameters(82.55308f, 28.517653f, 330.0f, -14788283 ),
            Parameters(66.04431f, 28.517653f, 30.0f, -14788283 ),
            Parameters(66.04432f, 57.111668f, 330.0f, -15564557 ),
            Parameters(82.55308f, 57.111668f, 30.0f, -15564557 ),
            Parameters(74.29869f, 42.814667f, 270.0f, -7829368 )
        ),

        //5ul
        // 16 babochka2 5d 3c 3u
        listOf(
            Parameters(87.37881f, 50.238644f, 0.0f, -10151157 ),
            Parameters(78.58179f, 28.650932f, 300.0f, -65536 ),
            Parameters(78.581795f, 71.82636f, 60.0f, -65536 ),
            Parameters(64.28478f, 36.905315f, 0.0f, -256 ),
            Parameters(64.28478f, 63.571983f, 0.0f, -256 )
        ),
        // 17 zabor 5d 3c 4u
        listOf(
            Parameters(88.76718f, 70.09027f, 0.0f, -826845 ),
            Parameters(58.433834f, 70.09027f, 0.0f, -826845 ),
            Parameters(65.34616f, 44.29325f, 30.0f, -256 ),
            Parameters(81.85491f, 44.29326f, 330.0f, -256 ),
            Parameters(73.6005f, 81.59027f, 270.0f, -10151157 )
        ),
        // 18 bruky 5d 3c 4u
        listOf(
            Parameters(77.046974f, 25.866257f, 270.0f, -826845 ),
            Parameters(55.459263f, 38.329926f, 30.0f, -256 ),
            Parameters(48.546974f, 64.12694f, 0.0f, -826845 ),
            Parameters(60.046974f, 79.293594f, 270.0f, -16711936 ),
            Parameters(81.634674f, 66.829926f, 30.0f, -826845 )
        ),
        // 19 zvezda 5d 3c 4u
        listOf(
            Parameters(82.91407f, 29.578537f, 30.0f, -256 ),
            Parameters(64.50178f, 43.875553f, 270.0f, -826845 ),
            Parameters(61.79879f, 53.963257f, 300.0f, -826845 ),
            Parameters(69.1835f, 71.505875f, 30.0f, -65536 ),
            Parameters(78.7034f, 74.864006f, 60.0f, -65536 )
            ),

        //6ul
        // 20 cvetok 5d 4c 5u
        listOf(
            Parameters(88.93364f, 73.617134f, 0.0f, -14788283 ),
            Parameters(72.7364f, 51.499413f, 270.0f, -256 ),
            Parameters(75.43942f, 41.41169f, 300.0f, -826845 ),
            Parameters(82.82415f, 34.026974f, 330.0f, -65536 ),
            Parameters(80.9908f, 65.79641f, 30.0f, -65536 )
        ),
        // 21 gusenica 6d 4c 4u
        listOf(
            Parameters(77.5854f, 25.198324f, 270.0f, -15564557 ),
            Parameters(60.05917f, 23.258953f, 330.0f, -256 ),
            Parameters(58.62806f, 41.07469f, 0.0f, -16711936 ),
            Parameters(60.52873f, 57.65832f, 30.0f, -256 ),
            Parameters(77.27069f, 57.58022f, 270.0f, -15564557 ),
            Parameters(88.77069f, 41.38954f, 0.0f, -14788283 )
        ),
        // 22 muravej 6d 3c 3u
        listOf(
            Parameters(88.63769f, 44.554565f, 0.0f, -826845 ),
            Parameters(88.63769f, 71.22124f, 0.0f, -826845 ),
            Parameters(74.90602f, 43.921074f, 300.0f, -256 ),
            Parameters(74.979805f, 72.51069f, 60.0f, -256 ),
            Parameters(74.90603f, 27.41231f, 60.0f, -428328 ),
            Parameters(74.9798f, 89.019455f, 300.0f, -428328 )
        ),
        // 23 babochka4 6d 3c 3u
        listOf(
            Parameters(78.33518f, 39.84921f, 60.0f, -7829368 ),
            Parameters(78.33518f, 56.357964f, 300.0f, -7829368 ),
            Parameters(64.03817f, 31.594828f, 0.0f, -10151157 ),
            Parameters(64.03817f, 64.61236f, 0.0f, -10151157 ),
            Parameters(78.33518f, 23.340458f, 300.0f, -826845 ),
            Parameters(78.335175f, 72.86674f, 60.0f, -826845 )
        ),

        //7ul
        // 24 skvoresnik 6d 3c 4u
        listOf(
            Parameters(65.64384f, 36.415173f, 0.0f, -65536 ),
            Parameters(77.143845f, 51.581844f, 270.0f, -256 ),
            Parameters(76.59013f, 67.1027f, 0.0f, -10151157 ),
            Parameters(77.14383f, 21.24851f, 270.0f, -256 ),
            Parameters(81.60304f, 37.59786f, 30.0f, -65536 ),
            Parameters(78.236015f, 11.160787f, 60.0f, -65536 )
        ),
        // 25 babochka3 6d 4c 6u
        listOf(
            Parameters(88.756035f, 48.49871f, 0.0f, -14788283 ),
            Parameters(74.934166f, 26.298485f, 300.0f, -15564557 ),
            Parameters(76.29234f, 70.08643f, 60.0f, -15564557 ),
            Parameters(71.19707f, 60.963005f, 30.0f, -428328 ),
            Parameters(71.19707f, 36.035652f, 330.0f, -428328 ),
            Parameters(82.318886f, 18.913761f, 330.0f, -16711936 )
        ),
        // 26 bol snezinka 7d 4c 6u
        listOf(
            Parameters(78.603645f, 66.271225f, 30.0f, -15564557 ),
            Parameters(71.21893f, 58.88651f, 60.0f, -428328 ),
            Parameters(68.51591f, 48.798794f, 270.0f, -7829368 ),
            Parameters(71.21893f, 38.711075f, 300.0f, -428328 ),
            Parameters(78.60364f, 31.32635f, 330.0f, -15564557 ),
            Parameters(88.69134f, 28.62337f, 0.0f, -7829368 ),
            Parameters(88.69135f, 68.97422f, 0.0f, -7829368 )
        ),
        // 27 korabl 6d 4c 6u
        listOf(
            Parameters(88.47724f, 46.60257f, 0.0f, -256 ),
            Parameters(77.97542f, 25.990744f, 60.0f, -826845 ),
            Parameters(72.42403f, 46.708797f, 330.0f, -826845 ),
            Parameters(77.295616f, 71.0621f, 270.0f, -10151157 ),
            Parameters(76.977234f, 61.769245f, 270.0f, -10151157 ),
            Parameters(55.7079f, 58.598446f, 330.0f, -7829368 )
        ),

        //8ul
        // 28 dva treugolnika i dve palky 7d 4c 4u
        listOf(
            Parameters(82.55308f, 28.517653f, 330.0f, -14788283 ),
            Parameters(66.04431f, 28.517653f, 30.0f, -14788283 ),
            Parameters(66.04432f, 57.111668f, 330.0f, -15564557 ),
            Parameters(82.55308f, 57.111668f, 30.0f, -15564557 ),
            Parameters(74.29869f, 42.814667f, 270.0f, -7829368 ),
            Parameters(55.956596f, 25.814653f, 0.0f, -16711936 ),
            Parameters(55.956608f, 59.814655f, 0.0f, -16711936 )
        ),
        // 29 hz1 7d 4c 5u
        listOf(
            Parameters(78.2889f, 27.55614f, 60.0f, -14788283 ),
            Parameters(77.196724f, 39.30136f, 270.0f, -15564557 ),
            Parameters(62.972454f, 34.242954f, 30.0f, -16711936 ),
            Parameters(62.97245f, 59.170307f, 330.0f, -16711936 ),
            Parameters(77.26451f, 54.503876f, 270.0f, -15564557 ),
            Parameters(78.37529f, 66.826614f, 300.0f, -14788283 ),
            Parameters(53.012436f, 46.997417f, 0.0f, -10151157 )
        ),
        // 30 zakat 7d 4c 6u
        listOf(
            Parameters(89.112854f, 31.042946f, 0.0f, -256 ),
            Parameters(79.02512f, 33.74594f, 330.0f, -826845 ),
            Parameters(77.2179f, 50.63539f, 270.0f, -15564557 ),
            Parameters(89.112854f, 69.59434f, 0.0f, -256 ),
            Parameters(79.02513f, 66.89134f, 30.0f, -826845 ),
            Parameters(70.71091f, 40.12446f, 300.0f, -65536 ),
            Parameters(70.54429f, 60.660904f, 60.0f, -65536 )
        ),
        // 31 derevo bol 7d 4c 5u
        listOf(
            Parameters(89.186905f, 72.27205f, 0.0f, -16711936 ),
            Parameters(73.47563f, 70.62466f, 300.0f, -826845 ),
            Parameters(71.09967f, 51.739937f, 30.0f, -256 ),
            Parameters(81.17076f, 37.599163f, 0.0f, -65536 ),
            Parameters(77.49914f, 21.878994f, 270.0f, -826845 ),
            Parameters(65.55458f, 32.676853f, 330.0f, -256 ),
            Parameters(82.639465f, 55.60598f, 30.0f, -65536 )
        ),

        //9ul
        // 32 hz3 7d 4c 5u
        listOf(
            Parameters(83.18868f, 27.012022f, 330.0f, -256 ),
            Parameters(78.29507f, 11.890635f, 60.0f, -826845 ),
            Parameters(70.91036f, 29.433258f, 330.0f, -256 ),
            Parameters(72.978165f, 46.22879f, 60.0f, -428328 ),
            Parameters(60.773876f, 56.581703f, 300.0f, -826845 ),
            Parameters(77.18429f, 66.66607f, 270.0f, -15564557 ),
            Parameters(58.68115f, 37.974403f, 0.0f, -15564557 )
        ),
        // 33 kot vverh golovoj 8d 4c 5 4u
        listOf(
            Parameters(78.17567f, 28.27467f, 60.0f, -10151157 ),
            Parameters(76.89476f, 47.926918f, 300.0f, -15564557 ),
            Parameters(78.538994f, 85.26755f, 300.0f, -65536 ),
            Parameters(52.378666f, 72.95638f, 0.0f, -10151157 ),
            Parameters(62.466385f, 75.65937f, 30.0f, -10151157 ),
            Parameters(59.290955f, 47.159378f, 30.0f, -10151157 ),
            Parameters(45.588722f, 62.878128f, 30.0f, -826845 ),
            Parameters(58.90106f, 34.830887f, 60.0f, -826845 )
        ),
        // 34 korona 8d 5c 5u
        listOf(
            Parameters(82.79374f, 34.99143f, 330.0f, -428328 ),
            Parameters(72.70603f, 37.694427f, 0.0f, -15564557 ),
            Parameters(72.70603f, 64.3611f, 0.0f, -826845 ),
            Parameters(62.37765f, 48.84978f, 330.0f, -256 ),
            Parameters(76.79552f, 21.011196f, 270.0f, -14788283 ),
            Parameters(55.2078f, 33.47487f, 30.0f, -14788283 ),
            Parameters(82.793755f, 67.0641f, 30.0f, -826845 ),
            Parameters(58.393154f, 63.395645f, 300.0f, -15564557 )
        ),
        // 35 grustnij robot 8d 4c 4u
        listOf(
            Parameters(76.99299f, 56.951065f, 270.0f, -16711936 ),
            Parameters(76.99299f, 33.951057f, 270.0f, -16711936 ),
            Parameters(75.50587f, 46.47045f, 60.0f, -14788283 ),
            Parameters(74.29001f, 23.863356f, 60.0f, -7829368 ),
            Parameters(74.29001f, 67.03877f, 300.0f, -7829368 ),
            Parameters(61.82633f, 45.451057f, 0.0f, -16711936 ),
            Parameters(76.94979f, 13.11423f, 270.0f, -428328 ),
            Parameters(76.974846f, 77.74333f, 270.0f, -428328 )
        ),

        // 10ul
        // 36 sfera s treugolnikami 9d 5c 6u
        listOf(
            Parameters(79.12719f, 17.427208f, 60.0f, -10151157 ),
            Parameters(76.75123f, 36.311928f, 330.0f, -16711936 ),
            Parameters(77.11024f, 50.47295f, 270.0f, -14788283 ),
            Parameters(76.41476f, 64.45535f, 30.0f, -16711936 ),
            Parameters(78.79071f, 83.34008f, 300.0f, -10151157 ),
            Parameters(88.610245f, 35.306274f, 0.0f, -16777216 ),
            Parameters(88.610245f, 65.63962f, 0.0f, -16777216 ),
            Parameters(60.242477f, 36.311928f, 30.0f, -15564557 ),
            Parameters(59.905987f, 64.45536f, 330.0f, -15564557 )
        ),
        // 37 chasha vesov 11d 4c 6u
        listOf(
            Parameters(82.66781f, 26.539072f, 330.0f, -7829368 ),
            Parameters(76.43476f, 44.755856f, 30.0f, -256 ),
            Parameters(78.81072f, 63.640575f, 300.0f, -256 ),
            Parameters(73.259315f, 21.661848f, 330.0f, -7829368 ),
            Parameters(76.10776f, 73.72827f, 270.0f, -7829368 ),
            Parameters(59.92601f, 44.755856f, 330.0f, -15564557 ),
            Parameters(58.82245f, 63.463894f, 270.0f, -15564557 ),
            Parameters(68.5373f, 38.022705f, 0.0f, -14788283 ),
            Parameters(78.5234f, 83.49268f, 300.0f, -14788283 ),
            Parameters(49.83829f, 47.458855f, 0.0f, -7829368 ),
            Parameters(58.269966f, 25.741743f, 60.0f, -256 )
        ),
        // 38 zuk bolshoj 10d 4c 6u
        listOf(
            Parameters(78.52957f, 31.897573f, 60.0f, -65536 ),
            Parameters(71.144844f, 24.51285f, 30.0f, -826845 ),
            Parameters(88.63769f, 43.521885f, 0.0f, -256 ),
            Parameters(71.144844f, 49.440193f, 330.0f, -65536 ),
            Parameters(67.67367f, 58.970097f, 300.0f, -256 ),
            Parameters(78.3075f, 69.13864f, 60.0f, -826845 ),
            Parameters(88.87836f, 80.173355f, 0.0f, -10151157 ),
            Parameters(71.753876f, 81.616455f, 300.0f, -10151157 ),
            Parameters(58.87667f, 37.382393f, 0.0f, -256 ),
            Parameters(61.26926f, 66.90905f, 270.0f, -65536 )
        ),
        // 39 sova 9d 4c 5u
        listOf(
            Parameters(78.76407f, 67.92872f, 300.0f, -428328 ),
            Parameters(72.52827f, 53.39743f, 330.0f, -15564557 ),
            Parameters(75.99945f, 39.256657f, 0.0f, -428328 ),
            Parameters(62.105427f, 34.709557f, 60.0f, -15564557 ),
            Parameters(58.277008f, 48.737354f, 30.0f, -14788283 ),
            Parameters(88.94427f, 57.208736f, 0.0f, -7829368 ),
            Parameters(78.487274f, 23.69312f, 300.0f, -7829368 ),
            Parameters(63.828392f, 69.455414f, 300.0f, -14788283 ),
            Parameters(78.83327f, 82.85825f, 300.0f, -14788283 )
        )
    )
    var picturesSticksFirstSide = mutableListOf<Stick>()
    var picturesSticksSecondSide = mutableListOf<Stick>()
    var rotatedLineFirstPoint = PointF(0f,0f)
    var rotatedLineSecondPoint = PointF(0f,0f)
    var magnetic = false
    var magneticRectF = RectF()


    init {

        var opt = BitmapFactory.Options()
        opt.inMutable = true
        bitmapColor = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.colors,
            opt
        )

        paintFill = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            textSize = 100F
        }

        paintForRectF = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            textSize = 100F
        }

        paintColor = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            textSize = 100F
        }

        paintStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = STROKE_WIDTH
            textSize = 100F
        }
        selectedPaintStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = STROKE_WIDTH + 1
            textSize = 100F
        }
        transparentPaintFill = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            textSize = 100F
        }
        transparentPaintStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = STROKE_WIDTH
            textSize = 100F
        }
        transparentPaintFill.alpha = 20
        transparentPaintStroke.alpha = 20

        canvRectF = RectF(0F, 0F, pwidth, pheight)


        var sPref = context.getSharedPreferences("game", Context.MODE_PRIVATE)
        currentGame = sPref.getInt("current_game", 1)
        Log.d(TAG, " Current game = " + currentGame)
        currentLevel = sPref.getInt("current_level" + currentGame, 1)
        Log.d(TAG, " Current level = " + currentLevel)

        if (currentLevel % 2 == 0 || currentLevel == 11) {
            square = false
        } else {
            square = true
        }

        if (currentGame == 3) {
            var result = generator.generateThird(currentLevel, pwidth, pheight)
            sticksTask = result.first.first
            shapesTask = result.first.second
            Log.d(
                TAG,
                result.toString() + " sticksTask = " + sticksTask + " shapesTask = " + shapesTask
            )
            gridOfSticks = result.second
        } else if (currentGame == 4) {
            var result = generator.generateFourth(currentLevel, pwidth, pheight)
            shapesTask = result.first.first
            countOfExtraSticksTask = result.first.second
            Log.d(
                TAG,
                result.toString() + " sticksTask = " + sticksTask + " shapesTask = " + shapesTask
            )
            gridOfSticks = result.second
            addToSelected()
            countShapes()
            sticksTask = selectedSticks.size + countOfExtraSticksTask


            listOfExtraSticks = creator.createListOfRectf(pwidth, pheight, countOfExtraSticksTask)
            extraSticksCounter = countOfExtraSticksTask

            selectAllExtraSticks(true)
            lockSelection(true)
            allSticksCounter = selectedSticks.size

        } else if (currentGame == 5) {
            var result = generator.generateFifth(currentLevel, pwidth, pheight)
            shapesTask = result.first.first
            countOfExtraSticksTask = result.first.second
            Log.d(
                TAG,
                result.toString() + " sticksTask = " + sticksTask + " shapesTask = " + shapesTask
            )
            gridOfSticks = result.second
            addToSelected()
            countShapes()
            sticksTask = selectedSticks.size - countOfExtraSticksTask


            listOfExtraSticks = creator.createListOfRectf(pwidth, pheight, countOfExtraSticksTask)
            extraSticksCounter = 0

            selectAllExtraSticks(false)

            lockSelection(false)
            allSticksCounter = selectedSticks.size

        } else if (currentGame < 3) {
            if(currentLevel == 11){
                currentLevel = 10
            }
            listOfExtraSticks = creator.createListOfRectf(pwidth, pheight, 1)
            rectF = listOfExtraSticks[0]
            rectF.selected = true
            rectF.canSelected = false

            var resizedColor = Bitmap.createScaledBitmap(
                bitmapColor, (rectF.rX * 3).toInt(), (rectF.rX * 3).toInt(), false
            )

            bitmapColor = resizedColor

            var startX = rectF.getRectF().left
            var startY = rectF.getRectF().top * 0.8f
            var width = bitmapColor.width.toFloat()
            var space = width * 0.5f

            rectColor = RectF(
                rectF.getRectF().left - rectF.rX*1.5f,
                rectF.getRectF().top * 0.8f - rectF.rX,
                rectF.getRectF().right + rectF.rX,
                rectF.getRectF().top * 0.8f + bitmapColor.height.toFloat()
            )

            if(currentGame == 0){
                numberOfColors *= 2
            }
            colorsStack = colorsStack.shuffled()
            startX += width
            startX += space
            var newRect = RectF(startX, startY, startX + width, startY + width)
            startX += width
            circleList.add(newRect)
            for (i in 0..numberOfColors-1){
                colors.add(colorsStack[i])
                startX += space
                var newRect = RectF(startX, startY, startX + width, startY + width)
                startX += width
                circleList.add(newRect)
            }
            colors.shuffle()
            paintForRectF.color = colors[0]

            middleH = pheight / 2

            if (currentGame == 1) {
                middleW = pwidth / 2 + rectF.rX * 2


                var picN = sPref.getInt("picture_number" + currentLevel, 1)

                var picIndex = 6 * currentLevel - picN

                if (picN == 6) {
                    picN = 0
                }
                sPref.edit().putInt("picture_number" + currentLevel, picN + 1).apply()

//            Log.d(TAG, "radX = " + newrX + " radY = " + newrY)

                var result = createFirstSide(pictures[picIndex])

                sticksTask = picturesSticksFirstSide.size

                createSecondSide(result)

                var rndIndex = (0..picturesSticksSecondSide.size-1).random()
                picturesSticksSecondSide[rndIndex].selected = true
                picturesSticksSecondSide[rndIndex].inShape = true
                sticksTask--

                canvRectF = RectF(0F, 0F, middleW, pheight)
            } else if (currentGame == 2) {

                middleW = pwidth / 2

                var picN = sPref.getInt("picture_number_sym" + currentLevel, 1)

                var ind = 4 * currentLevel - picN

                if (picN >= 4) {
                    picN = 0
                }

                sPref.edit().putInt("picture_number_sym" + currentLevel, picN + 1).apply()

                var result = createFirstSide(symmetry[ind])

                sticksTask = symmetry[ind].size

                var rndAngle = rnd.nextInt(4) * 90f

                createSecondSide(result)

                var result1 = rotateShape(middleW, 0f, 0f, rndAngle)

                rotatedLineFirstPoint = PointF(result1.first.first, result1.first.second)

                var result2 = rotateShape(middleW, pheight, 0f, rndAngle)

                rotatedLineSecondPoint = PointF(result2.first.first, result2.first.second)

                for (i in 0..picturesSticksFirstSide.size - 1) {

                    picturesSticksFirstSide[i] =
                        createRotatedNewStick(picturesSticksFirstSide[i], rndAngle)

                    picturesSticksSecondSide[i] =
                        createRotatedNewStick(picturesSticksSecondSide[i], rndAngle)

                }

            }
        }


        bitmapBin = BitmapFactory.decodeResource(
            context.resources,
            android.R.drawable.ic_menu_delete,
            opt
        )

        bitmapMagnetActive = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.magnet,
            opt
        )

        bitmapMagnetActive = Bitmap.createScaledBitmap(
            bitmapMagnetActive, rectF.rY.toInt(), rectF.rY.toInt(), false
        )

        bitmapMagnetInactive = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.magnet_inactive,
            opt
        )

        bitmapMagnetInactive = Bitmap.createScaledBitmap(
            bitmapMagnetInactive, rectF.rY.toInt(), rectF.rY.toInt(), false
        )

        magneticRectF = RectF(pwidth - rectF.rY - rectF.rX*2, rectF.rX*2, pwidth + rectF.rY, rectF.rX*2 + rectF.rY)

        bitmapBin = Bitmap.createScaledBitmap(
            bitmapBin, rectF.rY.toInt(), rectF.rY.toInt(), false
        )

        bitmapRotation = BitmapFactory.decodeResource(
            context.resources,
            android.R.drawable.ic_menu_rotate,
            opt
        )

        bitmapRotation = Bitmap.createScaledBitmap(
            bitmapRotation, (rectF.rX * 3.5).toInt(), (rectF.rX * 3.5).toInt(), false
        )

        bitmapRotationX = rectF.rX
        bitmapRotationY = rectF.rX * -3

        if(currentGame == 1) {
            bitmapBinX = middleW - rectF.rY - rectF.rX/2
            bitmapBinY = pheight - rectF.rY - rectF.rX/2
        }else{
            bitmapBinX = pwidth - rectF.rY - rectF.rX/2
            bitmapBinY = pheight - rectF.rY - rectF.rX/2
        }


    }

    private fun createRotatedNewStick(stick: Stick, rndAngle: Float): Stick {
        var result = rotateShape(stick.centerX, stick.centerY, stick.angle, rndAngle)

        var xLeft = result.first.first - rectF.rX
        var yTop = result.first.second - rectF.rY

        var xRight = xLeft + rectF.rX * 2
        var yBotton = yTop + rectF.rY * 2

        var newStick = Stick(
            xLeft, yTop, xRight, yBotton,
            rectF.rX, rectF.rY, "0"
        )

        val path = Path()
        path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
        newStick.setPath(path)

        newStick.rotate(result.second)
        newStick.paint = createPaint()
        newStick.paint.color = stick.paint.color

        return newStick
    }


    private fun createFirstSide(listOfParameters : List<Parameters>): Pair<Float, Float> {
        var percentX = pheight / 100f
        var percentY = pheight / 100f

        var minX = pwidth
        var minY = pheight
        var maxX = 0f
        var maxY = 0f

        var mapOfColor = HashMap<Int, Int>()

        listOfParameters.forEach {

            var x = it.x * percentX
            var y = it.y * percentY


            var xLeft = 0f
            if(currentGame == 1) {
                xLeft = middleW + x - rectF.rX
            }else{
                xLeft = x - rectF.rX
            }
            var yTop = y - rectF.rY


            var xRight = xLeft + rectF.rX * 2
            var yBotton = yTop + rectF.rY * 2
            var newStick = Stick(
                xLeft, yTop, xRight, yBotton,
                rectF.rX, rectF.rY, "0"
            )
            val path = Path()
            path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
            newStick.setPath(path)

            newStick.rotate(it.angle)
            newStick.paint = createPaint()

            if(mapOfColor.contains(it.color)) {
                newStick.paint.color = mapOfColor.get(it.color)!!
            }else{
                numberOfColors--
                newStick.paint.color = colors[numberOfColors]
                mapOfColor.set(it.color, colors[numberOfColors])
            }

            picturesSticksFirstSide.add(newStick)

            var currentMaxX = maxOf(newStick.xlb, newStick.xrt, newStick.xlt, newStick.xrb)
            var currentMaxY = maxOf(newStick.ylb, newStick.yrt, newStick.ylt, newStick.yrb)
            var currentMinX = minOf(newStick.xlb, newStick.xrt, newStick.xlt, newStick.xrb)
            var currentMinY = minOf(newStick.ylb, newStick.yrt, newStick.ylt, newStick.yrb)

            if (currentMaxX > maxX) {
                maxX = currentMaxX
            }
            if (currentMinX < minX) {
                minX = currentMinX
            }
            if (currentMaxY > maxY) {
                maxY = currentMaxY
            }
            if (currentMinY < minY) {
                minY = currentMinY
            }
        }

        colors.shuffle()

        paintForRectF.color = colors[0]

        var leftPad = minX - middleW
        var rightPad = pwidth - maxX
        var topPad = minY
        var bottonPad = pheight - maxY

        var diffX = 0f
        if(currentGame ==1) {
            diffX = (rightPad - leftPad) / 2f
        }else{
            diffX = middleW - maxX
        }
        var diffY = (bottonPad - topPad) / 2f

        Log.d(
            TAG,
            " left = " + leftPad + " right = " + rightPad + " top = " + topPad + " botton = " + bottonPad + " diff X = " + diffX + " diffY = " + diffY
        )

        return Pair(diffX, diffY)
    }

    private fun createSecondSide(dxdy: Pair<Float, Float>) {
        var diffX = dxdy.first
        var diffY = dxdy.second

        picturesSticksFirstSide.forEach {
            it.moveStarted(it.centerX, it.centerY)
            it.move(it.centerX + diffX, it.centerY + diffY)
            it.moveEnded()

            var newAngle = it.angle
            var xLeft = 0f

            if(currentGame == 1) {
                xLeft = it.centerX - (middleW - rectF.xlt) - rectF.rX
            }else{
                if(it.angle == 30f){
                    newAngle = 330f
                }else if(it.angle == 330f){
                    newAngle = 30f
                }else if(it.angle == 60f){
                    newAngle = 300f
                }else if(it.angle == 300f){
                    newAngle = 60f
                }
                xLeft = middleW + (middleW - it.centerX) - rectF.rX
            }
            var yTop = it.centerY - rectF.rY


            var xRight = xLeft + rectF.rX * 2
            var yBotton = yTop + rectF.rY * 2

            var newStick = Stick(
                xLeft, yTop, xRight, yBotton,
                rectF.rX, rectF.rY, "0"
            )
            val path = Path()
            path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
            newStick.setPath(path)

            newStick.rotate(newAngle)
            newStick.paint = createPaint()
            newStick.paint.color = it.paint.color

            picturesSticksSecondSide.add(newStick)

        }

    }


    fun rotateShape(x : Float, y : Float, old_angle : Float, rotation_angle : Float) : Pair<Pair<Float, Float>, Float>{
        var x0 = x - middleW
        var y0 = y - middleH

        var x01 = (cos((rotation_angle * (Math.PI / 180)) )*x0 - sin((rotation_angle *(Math.PI / 180)))*y0).toFloat()
        var y01 = (cos((rotation_angle *(Math.PI / 180)))*y0 + sin((rotation_angle *(Math.PI / 180)))*x0).toFloat()

        var x1 = middleW + x01
        var y1 = middleH + y01

        return Pair(Pair(x1,y1), computeNewAngle(old_angle, rotation_angle))
    }

    fun computeNewAngle(old_angle : Float, rotation_angle : Float) : Float{
        var rotated_old_angle = old_angle + rotation_angle
        var new_angle = 0f
        anglesList.forEach { ang ->
//            Log.d(TAG, " angle = " + ang + " mod 180 = " + (ang % 180))
            if( ang % 180 == rotated_old_angle % 180){
                new_angle = ang
            }
        }
        return new_angle
    }

    fun addToSelected() {
        gridOfSticks.forEach {
            if(it.selected){
                selectedSticks.add(it)
            }
        }
    }

    fun selectAllExtraSticks(select : Boolean) {
        listOfExtraSticks.forEach {
            it.selected = select
        }
    }

    fun onDraw(canvas: Canvas) {
 //       super.onDraw(canvas)


        if(currentGame < 3) {

            picturesSticksFirstSide.forEach {
                canvas.drawPath(it.getPath(), it.paint)
                canvas.drawPath(it.getPath(), paintStroke)
            }

            if(currentGame == 1) {
                picturesSticksSecondSide.forEach {
                    if (it.selected) {
                        canvas.drawPath(it.getPath(), it.paint)
                        canvas.drawPath(it.getPath(), paintStroke)
                    }
                }
            }


            canvas.drawBitmap(
                bitmapBin,
                bitmapBinX,
                bitmapBinY,
                paintStroke
            )

            var paint = Paint()
            paint.strokeWidth = 30f
            Log.d(TAG, "sticks task = " + sticksTask)
            var rX = rectF.rX
            var rY = rectF.rY
            for (i in 0..sticks.size - 1) {
//                Log.d(TAG, sticks[i].toString())
                if (index == i) {
                    if (sticks[index].rotateMode) {
                        rotatedSticks.forEach() {
                            transparentPaintFill.color = sticks[index].paint.color
                            transparentPaintFill.alpha = 20
                            canvas.drawPath(it.getPath(), transparentPaintFill)
                            canvas.drawPath(it.getPath(), transparentPaintStroke)
                            if (debagMode) {
                                paint.setColor(Color.RED)
                                canvas.drawPoint(it.xlt, it.ylt, paint)
                                paint.setColor(Color.MAGENTA)
                                canvas.drawPoint(it.xrt, it.yrt, paint)
                                paint.setColor(Color.LTGRAY)
                                canvas.drawPoint(it.xlb, it.ylb, paint)
                                paint.setColor(Color.BLUE)
                                canvas.drawPoint(it.xrb, it.yrb, paint)
                            }
                        }
                    }
                    paintFill.color = sticks[i].paint.color
                    canvas.drawPath(sticks[i].getPath(), paintFill)
                    canvas.drawPath(sticks[i].getPath(), selectedPaintStroke)
                    canvas.drawBitmap(
                        bitmapRotation,
                        sticks[i].xrt + bitmapRotationX ,
                        sticks[i].yrt + bitmapRotationY,
                        paintStroke
                    )
                } else {
                    Log.d(TAG, "rotate mode false index != i " + sticks[i].angle )

                    canvas.drawPath(sticks[i].getPath(), sticks[i].paint)
                    canvas.drawPath(sticks[i].getPath(), paintStroke)
                }
                if (debagMode) {
                    paint.setColor(Color.RED)
                    canvas.drawPoint(sticks[i].xlt, sticks[i].ylt, paint)
                    paint.setColor(Color.MAGENTA)
                    canvas.drawPoint(sticks[i].xrt, sticks[i].yrt, paint)
                    paint.setColor(Color.LTGRAY)
                    canvas.drawPoint(sticks[i].xlb, sticks[i].ylb, paint)
                    paint.setColor(Color.BLUE)
                    canvas.drawPoint(sticks[i].xrb, sticks[i].yrb, paint)
                }
//                var percentX = sticks[i].centerX  / (pheight / 100f)
//                var percentY = sticks[i].centerY  / (pheight / 100f)

//                Log.d(TAG,  " x = " + sticks[i].centerX + " percentX = " + percentX)
//                Log.d(TAG,  " y = " + sticks[i].centerY + " percentY = " + percentY)
//                Log.d(TAG,  " middle" + middle + " height = " + pheight)

//                Log.d(TAG, "Parameters(" + percentX + "f, " + percentY + "f, " + sticks[i].angle + "f, " + sticks[i].paint.color + " ),")

            }

            canvas.drawRoundRect(rectF.getRectF(), round, round, paintForRectF)
            canvas.drawRoundRect(rectF.getRectF(), round, round, paintStroke)

            canvas.drawBitmap(bitmapColor, rectF.getRectF().left - rectF.rX/2, rectF.getRectF().top * 0.8f, paintStroke)

            if(openColor){

                for(i in 0..colors.size-1){
                    paintColor.color = colors[i]
                    canvas.drawOval(circleList[i], paintColor)
                    canvas.drawOval(circleList[i], paintStroke)

                }

            }

            if(currentGame == 1) {
                canvas.drawLine(middleW, 0f, middleW, pheight, paintStroke)
            }else{
                canvas.drawLine(rotatedLineFirstPoint.x, rotatedLineFirstPoint.y, rotatedLineSecondPoint.x, rotatedLineSecondPoint.y, transparentPaintStroke)
            }

            if(currentGame == 0){
                if(magnetic) {
                    canvas.drawBitmap(bitmapMagnetActive, pwidth - bitmapMagnetActive.width - rectF.rX*2, rectF.rX*2, paintStroke)
                }else{
                    canvas.drawBitmap(bitmapMagnetInactive, pwidth - bitmapMagnetActive.width - rectF.rX*2, rectF.rX*2, paintStroke)

                }
            }
        }
        var i = 0
        gridOfSticks.forEach {
            if(!it.selected) {
                if(!it.canSelected){
                    canvas.drawPath(it.getPath(), it.paint)
                }else {
                    canvas.drawPath(it.getPath(), transparentPaintFill)
                }
                canvas.drawPath(it.getPath(), transparentPaintStroke)

            }else{
//                Log.d(TAG, "IN SHAPE = " + it.id + " " + it.inShape)
                if(!it.canSelected){
                    canvas.drawPath(it.getPath(), it.paint)
                }else {
                    canvas.drawPath(it.getPath(), paintFill)
                }
                if(it.inShape){
                    canvas.drawPath(it.getPath(), selectedPaintStroke)
                }else {
//                    Log.d(TAG, "SELECTED TRUE")
                    canvas.drawPath(it.getPath(), paintStroke)
                }
            }
            i++
        }

        if(sticksTask != 0  && currentGame == 3){
            canvas.drawText(allSticksCounter.toString(), pwidth * 0.93f, pheight * 0.1f, paintFill)
            canvas.drawText(allSticksCounter.toString(), pwidth * 0.93f, pheight * 0.1f, paintStroke)

        }

        if(currentGame > 3){
            listOfExtraSticks.forEach {
                if(!it.selected) {
                    canvas.drawPath(it.getPath(), transparentPaintFill)
                    canvas.drawPath(it.getPath(), transparentPaintStroke)
                }else {
                    canvas.drawPath(it.getPath(), paintFill)
                    canvas.drawPath(it.getPath(), paintStroke)
                }
            }
        }

    }


   fun showNew(){
       show = GlobalScope.async(Dispatchers.Main) {
           canRemove = false
           if (listToShow.size != 0) {
               var iterator = listToShow.iterator()
               while(iterator.hasNext()) {
                   var it = iterator.next()
 //                  Log.d(TAG, "size listToShow 1 = " + listToShow.size)
 //                  Log.d(TAG, "listToShow it size = " + it.size)

                   var d: Long = 300
                   selectedPaintStroke.color =
                       Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    //               Log.d(TAG, "color= " + selectedPaintStroke.color)
                   for (j in 0..(it?.size?.minus(1) ?: -1)) {
                       it?.get(j)?.inShape = true
                       vi.invalidate()
                       delay(d)
                   }
                   delay((d).toLong())
                   for (j in 0..(it?.size?.minus(1) ?: -1)) {
                       it?.get(j)?.inShape = false
                   }
                   vi.invalidate()

               }
               listToShow.clear()
           }
           canRemove = true

       }

    }

    fun removeInArray(stick: Stick) {
            var j = 0
            for (i in 0..arrayOfShape.size - 1) {
                if (arrayOfShape[j].contains(stick)) {
                    arrayOfShape.removeAt(j)
                    j--
                    shapesCounter--
                }
            j++
        }
    }


    fun countSticks(){
        var sticksInShape = mutableListOf<Stick>()
        arrayOfShape.forEach { list ->
            list.forEach {
                if(!sticksInShape.contains(it)){
                    sticksInShape.add(it)
                }
            }
        }
        sticksCounter = sticksInShape.size
    }


       private fun checkDel(): Boolean {
           if(index >= 0 ){
               var delRectF = RectF(bitmapBinX,bitmapBinY,bitmapBinX+bitmapBin.width,bitmapBinY+bitmapBin.height)
               var st = sticks[index]
               if(delRectF.contains(st.xlt, st.ylt) || delRectF.contains(st.xrt, st.yrt) ||
                   delRectF.contains(st.xlb, st.ylb) || delRectF.contains(st.xrb, st.yrb)){
                   return true
               }
           }
           return false
       }



    fun gravity(list : MutableList<Stick>, stick : Stick) {
        var dx = 0f
        var dy = 0f
        var dist = 99999f

        list.forEach { it ->
            if(it != stick){
                var res = stick.isNear(it, list)
                Log.d(TAG, "" + res[0])
                if (res[0] < dist){
                    dist = res[0]
                    dx = res[1]
                    dy = res[2]
                }
            }
        }
        if (dx != 0f || dy != 0f) {
            stick.moveStarted(0f, 0f)
            stick.move(dx,dy)
            stick.moveEnded()
        }


    }

    fun cleanSticks(){
        if(index >= 0){
            sticks[index].moveEnded()
            sticks[index].rotateMode = false
            rotatedSticks.clear()
            index = -1
        }
        sticks.clear()
        picturesSticksSecondSide.forEach {
            it.inShape = false
        }
        if(currentGame == 1) {
            sticksTask = picturesSticksSecondSide.size - 1
        }else{
            sticksTask = picturesSticksSecondSide.size
        }

    }

    fun countShapes() {
        var newShapes = solver.countShapes(selectedSticks, arrayOfShape, square)
        if (newShapes != null) {
            arrayOfShape.addAll(newShapes)
            listToShow.addAll(newShapes)
            shapesCounter += newShapes.size
            countSticks()
        }
        showNew()
//        checkWin()
    }



    fun checkWin() : Boolean {
        var sPref = context.getSharedPreferences("game", Context.MODE_PRIVATE)
        if((sticksCounter == sticksTask || sticksTask == 0) && shapesCounter == shapesTask && currentGame == 3){
            if(currentLevel <= 10) {
                sPref.edit().putInt("current_level" + currentGame, currentLevel + 1).apply()
                Log.d(
                    TAG,
                    "!!!!!!!!!!!!!YOU WIN!!!!!!!!!!!!!" + " " + sPref.getInt(
                        "current_level" + currentGame,
                        0
                    )
                )
                }
            win = true
            if(currentLevel ==8){
                sPref.edit().putBoolean("activated4", true).apply()
            }
            return true
        } else if(currentGame == 4){
//            Log.d(TAG, "extra sticks counter IN WIN = " + extraSticksCounter)
            if(shapesTask == shapesCounter && extraSticksCounter == 0 && sticksTask == sticksCounter){
                if(currentLevel <= 10) {
                    sPref.edit().putInt("current_level" + currentGame, currentLevel + 1).apply()
                 Log.d(
                     TAG,
                     "!!!!!!!!!!!!!YOU WIN!!!!!!!!!!!!!" + " " + sPref.getInt(
                         "current_level" + currentGame,
                         0
                     )
                 )

             }
                if (currentLevel == 8){
                    sPref.edit().putBoolean("activated5", true).apply()
                }
             win = true
                return true
            }
        }else if(currentGame == 5){
                if(shapesTask == shapesCounter && extraSticksCounter == countOfExtraSticksTask && sticksTask == sticksCounter && sticksTask == allSticksCounter){
                 if(currentLevel <= 10) {
                     sPref.edit().putInt("current_level" + currentGame, currentLevel + 1).apply()
                     Log.d(
                         TAG,
                         "!!!!!!!!!!!!!YOU WIN!!!!!!!!!!!!!" + " " + sPref.getInt(
                             "current_level" + currentGame,
                             0
                         )
                     )

                 }
                 win = true
                    return true
             }
         }else if(currentGame == 1 || currentGame == 2){
//             Toast.makeText(context, "" + sticksCounter + " / " + sticksTask, Toast.LENGTH_SHORT).show()
             if(sticksTask == sticksCounter){
                 if(currentLevel <= 10) {
                     sPref.edit().putInt("current_level" + currentGame, currentLevel + 1).apply()
                 }
                 if(currentGame == 1 && currentLevel == 8){
                     sPref.edit().putBoolean("activated2", true).apply()
                 }else if(currentGame == 2 && currentLevel == 8){
                     sPref.edit().putBoolean("activated3", true).apply()

                 }
                 win = true
                 return true
             }
        }

        return false
    }


    fun lockSelection(selected : Boolean){
        gridOfSticks.forEach {
            if(it.selected == selected){
                it.canSelected = false
                it.paint.color = Color.parseColor("#fe9600")
                if(!selected){
                    it.paint.alpha = 20
                }
            }
        }
    }

    fun addNew(x : Float,y :Float) {
        var newStick = Stick(
            rectF.xLeft, rectF.yTop, rectF.xRight, rectF.yBottom,
            rectF.rX, rectF.rY, idStick.toString()
        )
        idStick++
        newStick.moveStarted(x, y)
        val path = Path()
        path.addRoundRect(newStick.getRectF(), round, round, Path.Direction.CW)
        newStick.setPath(path)
        newStick.paint = createPaint()
        sticks.add(newStick)
        index = sticks.size - 1
        if(openColor){
            openColor = false
        }
    }

    fun createPaint() : Paint{
        var newPaint = Paint().apply {
            style = Paint.Style.FILL
            color = colors[colorsIndex]
            textSize = 100F
        }
        return newPaint
    }

    fun checkStickInSticksList(x: Float, y: Float): Boolean {
        var colorChanged = false
        if(openColor) {
            circleList.forEach {
                if (it.contains(x, y)) {
                    colorChanged = true
                }
            }
        }
        var founded = false
        var indexNear = -1
        if(!colorChanged) {
            for (i in 0..sticks.size - 1) {
                if (!founded) {
                    if (sticks[i].isIn(x, y)) {
                        if (i != index) {
                            if (index != -1) {
                                sticks[index].rotateMode = false
                                rotatedSticks.clear()
                            }
                            index = i
                        } else {
                            clearSelected = true
                            sticks[index].rotateMode = false
                            rotatedSticks.clear()
                        }
                        sticks[index].moveStarted(x, y)
                        if (openColor) {
                            openColor = false
                        }
                        founded = true
                    }
                }
            }
        }
        Log.d(TAG, "sticks size = " + sticks.size)
        return founded
    }

    fun moveStick(x: Float, y: Float) {
        if (index >= 0) {
//                        Log.d(TAG, "YES" + i )
            if(sticks[index].rotateMode){
                var angle = findAngle(x,y)

                rotatedSticks.forEach {
                    if(angle == it.angle % 180){
                        sticks[index].rotate(it.angle)
                    }
                }
            } else if (sticks[index].isMove) {
                if (canvRectF.contains(
                        sticks[index].xlt + (x - sticks[index].previousEventX),
                        sticks[index].ylt + (y - sticks[index].previousEventY)
                    ) &&
                    canvRectF.contains(
                        sticks[index].xrt + (x - sticks[index].previousEventX),
                        sticks[index].yrt + (y - sticks[index].previousEventY)
                    )
                    &&
                    canvRectF.contains(
                        sticks[index].xlb + (x - sticks[index].previousEventX),
                        sticks[index].ylb + (y - sticks[index].previousEventY)
                    )
                    &&
                    canvRectF.contains(
                        sticks[index].xrb + (x - sticks[index].previousEventX),
                        sticks[index].yrb + (y - sticks[index].previousEventY)
                    )
                ) {
                    sticks[index].move(x, y)
                }
            }
        }
    }

    fun findAngle(x: Float, y: Float): Float {
        var v1x = 0
        var v1y = sticks[index].centerY - (sticks[index].centerY - rectF.rY)
        var v2x = sticks[index].centerX - x
        var v2y = sticks[index].centerY - y

        var angle = Math.acos((v1x * v2x + v1y * v2y) / (Math.sqrt( (v1x*v1x + v1y*v1y).toDouble() ) * Math.sqrt((v2x*v2x + v2y*v2y).toDouble() ))) *
                (180 / Math.PI)

        Log.d(TAG, "angle = " + angle)

        if(x < sticks[index].centerX){
            angle = 180 - angle + 180
        }

        var newAngle = Math.round(angle / 30) * 30

        Log.d(TAG, "new angle = " + newAngle)

        return newAngle % 180f

    }


    fun cancelMove(x: Float, y: Float) {
        if(index > -1) {
            Log.d(TAG, "CANCEL MOVE angle = " + sticks[index].angle)
        }
        canRemove = false
        if(rectColor.contains(x,y)){
//            Log.d(TAG, "BITMAP COLOR CONTAINS X, Y")
            openColor = !openColor
        }else if(openColor){
            for(i in 0..circleList.size-1) {
                if(circleList[i].contains(x,y)){
//                    Log.d(TAG, "CONTAINS")
                    paintForRectF.color = colors[i]
                    colorsIndex = i
                }
            }
            openColor = false
        }
        if( index >= 0 && rectF.contains(sticks[index].centerX, sticks[index].centerY)){
            sticks.removeAt(index)
            rotatedSticks.clear()
            index = -1
        }
        if (index >= 0) {
            var clearIndex = true
            if (sticks[index].isIn(x, y) && sticks[index].moved) {
                if (checkDel()) {
                    sticks.removeAt(index)
                    rotatedSticks.clear()
                    index = -1
//                            Log.d(TAG, "check true")
                } else {
//                            Log.d(TAG, "before gravity " + index)
                    sticks[index].rotateMode = false
                    rotatedSticks.clear()
                    sticks[index].moveEnded()
                    if(sticks[index].indexInShape > -1) {
                        if(sticks[index].paint.color == picturesSticksSecondSide[sticks[index].indexInShape].paint.color){
                            sticksCounter--
                        }
                        picturesSticksSecondSide[sticks[index].indexInShape].inShape = false
                        sticks[index].indexInShape = -1
                    }
                    checkIntersect()
                    if (currentGame == 0 && magnetic) {
                        gravity(sticks, sticks[index])
                    }
                }
                clearIndex = false
            } else if(!sticks[index].moved){
                rectRotation.left = sticks[index].xrt + bitmapRotationX
                rectRotation.top = sticks[index].yrt + bitmapRotationY
                rectRotation.right = sticks[index].xrt + bitmapRotationX + bitmapRotation.width
                rectRotation.bottom = sticks[index].yrt + bitmapRotationY + bitmapRotation.height
                sticks[index].moveEnded()
                if (rectRotation.contains(x, y)) {
                    if (sticks[index].rotateMode) {
                        sticks[index].rotateMode = false
                        rotatedSticks.clear()
                    } else {
                        sticks[index].rotateMode = true
                        var i = 0
                        anglesList.forEach { x ->
                            var newStick = Stick(
                                sticks[index].getRectF().left,
                                sticks[index].getRectF().top,
                                sticks[index].getRectF().right,
                                sticks[index].getRectF().bottom,
                                rectF.rX,
                                rectF.rY,
                                "-1"
                            )
                            val path = Path()
                            path.addRoundRect(
                                newStick.getRectF(),
                                round,
                                round,
                                Path.Direction.CW
                            )
                            newStick.setPath(path)
                            newStick.angle = 0f
                            newStick.rotate(anglesList[i])
                            rotatedSticks.add(newStick)
                            i++
                        }
                        rotateAngle = sticks[index].angle
                        Log.d(TAG, "CLEAR INDEX FALSE")
                        clearIndex = false
                    }
                }else if (clearSelected && sticks[index].isIn(x,y)){

                    clearIndex = true
                } else if(index != -1 && sticks[index].rotateMode && !sticks[index].isIn(x,y)){
                    var finded = false
//                    if(sticks[index].angle != rotateAngle) {
////                            Log.d(TAG, "i= " + i + " anglesLits= " + anglesList[i])
////                                    Log.d(TAG, "angle old= " + sticks[index].angle + " angle new= "+angleNew)
//                            if (sticks[index].indexInShape > -1 ) {
//                                if (sticks[index].paint.color == picturesSticksSecondSide[sticks[index].indexInShape].paint.color) {
//                                    sticksCounter--
//                                }
//                                picturesSticksSecondSide[sticks[index].indexInShape].inShape = false
//                                sticks[index].indexInShape = -1
//                            }
//                            sticks[index].rotate(rotateAngle)
//                            finded = true
////                            break
//                    }
//                    if(!finded){
                        var angle = findAngle(x,y)
                        rotatedSticks.forEach {
                            if (angle == it.angle % 180) {
//                                if(sticks[index].angle != it.angle) {
                                    if (sticks[index].indexInShape > -1) {
                                        if (sticks[index].paint.color == picturesSticksSecondSide[sticks[index].indexInShape].paint.color) {
                                            sticksCounter--
                                        }
                                        picturesSticksSecondSide[sticks[index].indexInShape].inShape = false
                                        sticks[index].indexInShape = -1
                                    }
                                    sticks[index].rotate(it.angle)
//                                }
                            }
                        }
//                    }
                    sticks[index].rotateMode = false
                    rotatedSticks.clear()
                    sticks[index].moveEnded()
                    index = -1
                }
            }
            if(clearIndex){
                if(index > -1 ) {
                    if( !sticks[index].isIn(x,y)) {
                        sticks[index].rotateMode = false
                        sticks[index].moveEnded()
                        index = -1
                    }
                    rotatedSticks.clear()
                }
            }
        }

        clearSelected = false
        canRemove = true


        if(currentGame == 0 && magneticRectF.contains(x,y)){
            magnetic = !magnetic
        }

//        Toast.makeText(context, "sticks size = " + sticks.size,Toast.LENGTH_SHORT).show()

    }

    private fun checkIntersect() {

        var rX = rectF.rX
        var rY = rectF.rY

        var maxS = 0f
        var ind = 0
        var i = 0

        var angleInRadians = -sticks[index].angle * (Math.PI / 180)

        var thisCenterX = (cos(angleInRadians )*sticks[index].centerX - sin(angleInRadians)*sticks[index].centerY).toFloat()
        var thisCenterY = (cos(angleInRadians)*sticks[index].centerY + sin(angleInRadians)*sticks[index].centerX).toFloat()

        picturesSticksSecondSide.forEach {
            if(it.angle == sticks[index].angle && !it.inShape) {
                var otherCenterX = (cos(angleInRadians )*it.centerX - sin(angleInRadians)*it.centerY).toFloat()
                var otherCenterY = (cos(angleInRadians)*it.centerY + sin(angleInRadians)*it.centerX).toFloat()

                if (Math.abs(thisCenterX - otherCenterX) <= rectF.rX * 2 && Math.abs(thisCenterY - otherCenterY) <= rectF.rY * 2) {
//                    Log.d(TAG, "INTERSECT ")
                    var firstX = otherCenterX - rX
                    var secondX = thisCenterX + rX
                    if (thisCenterX > otherCenterX) {
                        firstX = otherCenterX + rX
                        secondX = thisCenterX - rX
                    }
                    var firstY = otherCenterY - rY
                    var secondY = thisCenterY + rY
                    if (thisCenterY > otherCenterY) {
                        firstY = otherCenterY + rY
                        secondY = thisCenterY - rY
                    }
                    intersectR = RectF(firstX, firstY, secondX, secondY)

                    var currentS = Math.abs(firstX - secondX) * Math.abs(firstY - secondY)

                    if(currentS > maxS){
                        maxS = currentS
                        ind = i
                    }
                }
            }
                i++
            }
            if(maxS > 0){
                sticks[index].moveStarted(sticks[index].centerX, sticks[index].centerY)
                sticks[index].move(
                    picturesSticksSecondSide[ind].centerX,
                    picturesSticksSecondSide[ind].centerY
                )
                sticks[index].moveEnded()
                picturesSticksSecondSide[ind].inShape = true
                sticks[index].indexInShape = ind
                if(sticks[index].paint.color == picturesSticksSecondSide[ind].paint.color){
                    sticksCounter++
//                    checkWin()
                }
            }

    }


}