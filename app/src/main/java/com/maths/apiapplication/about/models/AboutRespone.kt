package com.maths.apiapplication.about.models

data class AboutRespone(
    var   success: Boolean,
    var   code: Int,
    var   message: String,
    var   data:ArrayList<AboutDataResponse>



)
data class AboutDataResponse(
    var id: Int,
    var title: String,
    var description: String,
    var status: String,
    var created_at: String,
    var updated_at: String,
    var deleted_at: String,
    var get_about_image : ArrayList<GetAboutImage>


)


data class  GetAboutImage  (
      var id : Int,
       var about_id : Int,
    var about_imgs : String,
     var created_at : String,
       var updated_at : String,
       var deleted_at : String

)