package com.example.dingdong3

import DateChangeReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

var dayIndex = 0
val uriSet : Array<Uri?> = arrayOf(null,null,null,null,null,null,null)
var justSet : Array<Int> = arrayOf(0,0,0,0,0,0,0)
var urlSet : Array<Task<Uri>?> = arrayOf(null,null,null,null,null,null,null)
var backButton : AppCompatButton? = null
var buttonBlock : Boolean = false

class MainActivity : AppCompatActivity() {
    lateinit var toastMessageSaved : CardView
    lateinit var toastMessageError : CardView
    lateinit var dayList : Array<Calendar>
    lateinit var dayBox : LinearLayout
    lateinit var menuBox : LinearLayout
    lateinit var dialog : BottomSheetDialog
    lateinit var loadingAnimation : LottieAnimationView
    lateinit var vAnimation : LottieAnimationView
    lateinit var dayCardBox : RelativeLayout
    var toastX : Int = 0
    var toastY : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Dingdong3)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*2. set each frame's value */
        dialog=BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        val dayCard : ImageView = findViewById(R.id.day_card)
        dayCardBox = findViewById(R.id.day_card_box)
        loadingAnimation = findViewById(R.id.loading_animation)
        dayCardBox.removeView(loadingAnimation)

        /*1. init today*/
        initToday()

        /*3. arrange viewers*/
        val box : RelativeLayout = findViewById(R.id.box)
        menuBox=findViewById(R.id.menu_box)

        // init variable for each message
        val viewersActivity = LayoutInflater.from(this).inflate(R.layout.viewers,null)
        toastMessageSaved = viewersActivity.findViewById(R.id.toastMessageSaved)
        toastMessageError = viewersActivity.findViewById(R.id.toastMessageError)

        vAnimation =viewersActivity.findViewById(R.id.v_animation)

        val location = IntArray(2)
        menuBox.getLocationOnScreen(location)
        toastY = menuBox.marginBottom
        toastX = 0
        val viewerBox : RelativeLayout = viewersActivity.findViewById(R.id.viewerBox)
        viewerBox.removeView(toastMessageSaved)
        viewerBox.removeView(toastMessageError)

        /*4. set each button*/
        val button0 : Button = findViewById(R.id.button0)
        button0.setOnClickListener(){ button0Func(this,this) }

        val button1 : Button = findViewById(R.id.button1)
        button1.setOnClickListener(){ button1Func(this,this)}

        val button2 : Button = findViewById(R.id.button2)
        button2.setOnClickListener(){ button2Func(this, this) }

        val button3 : Button = findViewById(R.id.button3)
        button3.setOnClickListener(){ button3Func(this) }

        /*4. catch the moment when it's midnight */
        val dateChangeReceiver = DateChangeReceiver(this)
        val intentFilter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        this.registerReceiver(dateChangeReceiver, intentFilter)
    }

    override fun onBackPressed() {
        backButton?.callOnClick()
    }

    public fun initToday(){
        // Clear Glide's cache
        Thread(Runnable { Glide.get(this).clearDiskCache()}).start()
        Glide.get(this).clearMemory()

        /*1. init date*/
        dayIndex = 0
        dayList = emptyArray()
        for(ago in 0..6){
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_YEAR, -ago)
            dayList = dayList.plus(date)
        }

        /*2. print today's date on screen*/
        val year = dayList[0].get(Calendar.YEAR).toString()
        val month = (dayList[0].get(Calendar.MONTH) + 101).toString().substring(1,3)
        val day = (dayList[0].get(Calendar.DAY_OF_MONTH) + 100).toString().substring(1,3)
        val weekDay : String = when (dayList[0].get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            Calendar.SUNDAY -> "일"
            else -> "?"
        }
        val dateText : TextView = findViewById(R.id.date_text)
        dateText.text = year
            .plus("년 ")
            .plus(month)
            .plus("월 ")
            .plus(day).plus("일 ")
            .plus(weekDay).plus("요일")

        val dayCard : ImageView = findViewById(R.id.day_card)
        cacheImageFromFirebase(this, this, dayList[dayIndex], dayCard)
    }
}

public val mCountDown : CountDownTimer = object : CountDownTimer(2000, 5000){
    override fun onTick(millisUntilFinishedq: Long) {
        buttonBlock = true
    }
    override fun onFinish() {
        buttonBlock = false
    }
}