package com.maths.apiapplication.base

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import com.maths.apiapplication.R


class SessionManagement(var context: Context){

     var dialog: Dialog?= null
     var editor:SharedPreferences.Editor?=null
     var pref: SharedPreferences? = null




     init{
         pref=context.getSharedPreferences(AppConstant.LOGIN_SESSION, Context.MODE_PRIVATE)
         editor=pref?.edit()
     }



    companion object {

        const val KEY_USER_ID = "userId"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_ONBOARDING_COMPLETED = "onboardingCompleted"
    }

    // login details
    fun setUserEmail(email:String){
        editor!!.putString(AppConstant.EMAIL,email)
        editor!!.commit()
    }

    fun getUserEmail():String?{
        return pref?.getString(AppConstant.EMAIL,"")
    }


    fun setUserPhoneNumber(phone:String){
        editor!!.putString(AppConstant.PHONE,phone)
        editor!!.commit()
    }

    fun getUserPhoneNumber():String?{
        return pref?.getString(AppConstant.PHONE,"")
    }



    fun setUserId(id:Int){
        editor!!.putInt(AppConstant.Id,id)
        editor!!.commit()
    }

    fun getUserId():Int?{
        return pref?.getInt(AppConstant.Id,-1)
    }


    fun setUserName(name:String){
        editor!!.putString(AppConstant.NAME,name)
        editor!!.commit()
    }

    fun getUserName():String?{
        return pref?.getString(AppConstant.NAME,"")
    }



    fun logOut(){
        editor?.clear()
        editor?.apply()
    }


     fun isOnboardingCompleted(): Boolean {
         return pref!!.getBoolean(KEY_ONBOARDING_COMPLETED, false)
     }

     fun setOnboardingCompleted(completed: Boolean) {
         editor?.putBoolean(KEY_ONBOARDING_COMPLETED, completed)?.apply()
     }



    fun openDialog(context:Context){
        dialog =Dialog(context)
        dialog!!.setContentView(R.layout.dialog_loading)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun closeDialog(){
        dialog?.dismiss()
    }

}