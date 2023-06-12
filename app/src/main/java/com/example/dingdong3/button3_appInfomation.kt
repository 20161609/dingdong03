package com.example.dingdong3

import com.google.android.material.bottomsheet.BottomSheetBehavior

fun button3Func (mainActivity: MainActivity){
    if(buttonBlock)return

    val dialogView = mainActivity.layoutInflater.inflate(R.layout.app_info, null)
    mainActivity.dialog.setContentView(dialogView)
    mainActivity.dialog.behavior.state= BottomSheetBehavior.STATE_COLLAPSED
    mainActivity.dialog.show()
}