package com.starters.hsge.presentation.common.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.starters.hsge.R

object LoadingLottieDialog {

    private lateinit var dogLoadingDialog: Dialog
    private lateinit var locationLoadingDialog: Dialog

    fun showDogLoadingDialog(context: Context): Dialog{
        dogLoadingDialog = Dialog(context)
        dogLoadingDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.dialog_loading_dog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

    fun dismissDogLoadingDialog(context: Context){
        if(dogLoadingDialog.isShowing){
            dogLoadingDialog.dismiss()
        }
    }

    fun showLocationLoadingDialog(context: Context): Dialog{
        locationLoadingDialog = Dialog(context)
        locationLoadingDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.dialog_loading_location)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

    fun dismissLocationLoadingDialog(context: Context){
        if(locationLoadingDialog.isShowing){
            locationLoadingDialog.dismiss()
        }
    }
}