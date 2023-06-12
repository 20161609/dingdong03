package com.example.dingdong3

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import java.util.*

var dayButtons : Array<AppCompatButton> = arrayOf()
var monthTextViews : Array<TextView> = arrayOf()

fun button0Func (mainActivity: MainActivity, context: Context){
    if(buttonBlock) return

    /* variables */
    val box1 = mainActivity.findViewById<RelativeLayout>(R.id.box)
    box1.removeView(mainActivity.menuBox)
    mainActivity.dayBox = createDayBox(mainActivity, context)
    box1.addView(mainActivity.dayBox)

    val dayList : Array<Calendar> = mainActivity.dayList
    val box : RelativeLayout = mainActivity.findViewById(R.id.box)

    /* functions */
    fun modifyUI(){
        Log.e("modifyUI","modifyUI")
        val box = mainActivity.findViewById<RelativeLayout>(R.id.box)
    }
    fun modifyMonth(){
        Log.e("modifyMonth","modifyMonth")
        var month : Int = -1
        for(i in 6 downTo 0){
            val new_month : Int = dayList[6-i].get(Calendar.MONTH).plus(1)
            if(month != new_month) monthTextViews[i].text="${new_month}월"
            else monthTextViews[i].text=""
            month=new_month
        }
    }
    fun modifyDay(){
        Log.e("modifyDay","modifyDay")
        for(i in 0..6){
            val dayInt : Int = dayList[6-i].get(Calendar.DAY_OF_MONTH).plus(100)
            dayButtons[i].text="$dayInt".substring(1,3)
        }
    }
    fun modifyButtonUI(){
        Log.e("modifyButtonUI","modifyButtonUI")
        for(i in 0..6){
            val button : AppCompatButton = dayButtons[i]
            if(dayIndex==(6-i)) {
                button.setBackgroundResource(R.drawable.shape_for_circle_button_clicked)
                button.setTextColor(Color.WHITE)
                button.typeface = ResourcesCompat.getFont(context, R.font.arita4_0_b)
            }
            else {
                button.setBackgroundResource(R.drawable.shape_for_circle_button)
                button.setTextColor(Color.BLACK)
                button.typeface = ResourcesCompat.getFont(context, R.font.arita4_0_m)
            }
        }
    }
    fun buttonAction(){
        // define action of each day's button
        for(i in 0..6){
            val buttonIndex : Int = 6-i
            val button : AppCompatButton = dayButtons[buttonIndex]
            button.setOnClickListener(){
                if(dayIndex!=i) {
                    if (isNetworkConnected(context)) {
                        dayIndex = i
                        modifyButtonUI()
                        val dayCard: ImageView = mainActivity.findViewById(R.id.day_card)
                        cacheImageFromFirebase(mainActivity, context, dayList[dayIndex], dayCard)
                    } else {
                        //float error message
                    }
                }
            }
        }

        backButton?.setOnClickListener(){
            box.removeView(mainActivity.dayBox)
            box.addView(mainActivity.menuBox)
            dayButtons = arrayOf()
            monthTextViews = arrayOf()
            backButton = null
        }

/*
        for(i in 0..6){
            val button : AppCompatButton = mainActivity.findViewById(dayText[i])
            button.setOnClickListener(){
                if(isNetworkConnected(context)){
                    dayIndex=i
                    modifyButtonUI()
                    val dayCard : ImageView = mainActivity.findViewById(R.id.day_card)
                    cacheImageFromFirebase(context, dayList[dayIndex], dayCard)
                }
                else{
                    //float error message
                }
            }
        }

        val backButton : AppCompatButton = mainActivity.findViewById(R.id.button_back)
        backButton.setOnClickListener(){
            box.removeView(mainActivity.dayBox)
            box.addView(mainActivity.menuBox)
        }
*/
    }

    /* body */
    //1. modify UI
    modifyUI()

    //2. modify month
    modifyMonth()

    //3. modify day
    modifyDay()

    //4. modify Button UI
    modifyButtonUI()

    //5. set each button's action
    buttonAction()
    return
}

fun createDayBox(mainActivity: MainActivity, context: Context) : LinearLayout{
    fun dpToPx(dp: Float): Int {
        val px= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics())
        return px.toInt()
    }

    fun createDaysLayout(): LinearLayout {
        val daysLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                // Set the top margin
                topMargin = dpToPx(15f)
            }
            gravity = Gravity.CENTER_HORIZONTAL
            orientation = LinearLayout.HORIZONTAL
        }
        for (i in 0..6) {
            val dayLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (i != 0)
                        setMargins(dpToPx(21f), 0, 0, 0)
                }
                gravity = Gravity.CENTER_HORIZONTAL
                orientation = LinearLayout.VERTICAL
            }

            val monthTextView = TextView(context).apply {
                id = resources.getIdentifier("month$i", "id", context.packageName)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
                typeface = ResourcesCompat.getFont(context, R.font.arita4_0_m)
                text = "00월"
                textSize =13f
            }
            monthTextViews=monthTextViews.plus(monthTextView)
            dayLayout.addView(monthTextView)

            val dayButton = AppCompatButton(context).apply {
                id = resources.getIdentifier("day$i", "id", context.packageName)
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(30f),
                    dpToPx(30f)
                ).apply { topMargin = dpToPx(5f) }
                setPadding(0,0,0,0)
                stateListAnimator=null
                background =
                    ResourcesCompat.getDrawable(resources, R.drawable.shape_for_circle_button, null)
                typeface = ResourcesCompat.getFont(context, R.font.arita4_0_m)
                setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
                textSize = 14f
                text = "00"
            }
            dayButtons=dayButtons.plus(dayButton)
            dayLayout.addView(dayButton)

            daysLayout.addView(dayLayout)
        }

        return daysLayout
    }

    val mainLinearLayout = LinearLayout(context).apply {
        layoutParams = RelativeLayout.LayoutParams(
            dpToPx(370.8f), dpToPx(105.0524737f)
        ).apply {
            // Set the rules for the LinearLayout
            addRule(RelativeLayout.CENTER_HORIZONTAL)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

            bottomMargin = dpToPx(54.872563f).toInt()
        }

        background = ResourcesCompat.getDrawable(resources, R.drawable.custom_card, null)
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }
    val relativeLayout : RelativeLayout= RelativeLayout(context).apply {
        layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER_HORIZONTAL
    }
    val textView = TextView(context).apply {
        setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        typeface = ResourcesCompat.getFont(context, R.font.arita4_0_b)
        setPadding(dpToPx(10f), dpToPx(10f), 0, 0)
        text = "최근 일주일 말씀입니다."
    }
    relativeLayout.addView(textView)

    backButton = AppCompatButton(context).apply {
        layoutParams = RelativeLayout.LayoutParams(dpToPx(23f).toInt(), dpToPx(23f).toInt()).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END)
            setMargins(0, dpToPx(8f).toInt(), dpToPx(20f).toInt(), 0)
        }
        setPadding(dpToPx(3f), 0, 0, 0)
        background =
            ResourcesCompat.getDrawable(resources, android.R.drawable.presence_offline, null)
        isClickable = true
    }
    relativeLayout.addView(backButton)

    mainLinearLayout.addView(relativeLayout)

    val daysLinearLayout = createDaysLayout()
    mainLinearLayout.addView(daysLinearLayout)

    return mainLinearLayout
}