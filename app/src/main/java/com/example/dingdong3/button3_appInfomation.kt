package com.example.dingdong3

import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun button3Func (mainActivity: MainActivity){
    if(buttonBlock)return

    val dialogView = mainActivity.layoutInflater.inflate(R.layout.app_info, null)
    mainActivity.dialog.setContentView(dialogView)
    val a : TextView? = mainActivity.dialog.findViewById(R.id.version_id)
    val version = "${BuildConfig.VERSION_NAME}"
    if (a != null) {
        a.text = version
    }

    mainActivity.dialog.behavior.state= BottomSheetBehavior.STATE_COLLAPSED
    mainActivity.dialog.show()
}