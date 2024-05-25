package com.maths.apiapplication.myprofile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.maths.apiapplication.myprofile.getProfile.viewmodel.MyProfileViewModel
import com.maths.apiapplication.myprofile.updateprofile.viewmodel.UpdateProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class MyProfileFragment : Fragment() {

    var binding:FragmentMyProfileBinding?=null;
    val GALLERY_PERMISSION_CODE = 102
    val CAMERA_PERMISSION_CODE=101


     val REQUEST_IMAGE_CAPTURE = 1
     val PICK_IMAGE = 2
    val REQUEST_CODE = 200


    private var tempUri: Uri? = null
    private val myProfileViewModel : MyProfileViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyProfileViewModel::class.java)
    }

    private  val  updateProfileViewModel : UpdateProfileViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UpdateProfileViewModel::class.java)
    }
    private  val  deleteViewModel : DeleteViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DeleteViewModel::class.java)
    }
    private  val  logoutViewModel : LogoutViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LogoutViewModel::class.java)
    }
    private var uriPath: String? = null
    private lateinit var sessionManagement : SessionManagement
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentMyProfileBinding.inflate(inflater,container,false)
        sessionManagement = SessionManagement(requireContext())
        initialize();

        return binding!!.root
    }

    private fun initialize() {
        try {
            binding!!.pullToRefresh.setOnRefreshListener {
                if(internetCheck()){
                    getProfileApi()
                }
                else{
                    Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

                }


            }
            if(internetCheck()){
                getProfileApi()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }


        }
        catch (e: Exception){
            e.printStackTrace()
        }



        binding!!.logoutTv.setOnClickListener { logoutDialog() }

        binding!!.deleteAccountTv.setOnClickListener { deleteDialog() }

        binding!!.backImg.setOnClickListener {
            activity?.onBackPressed()
        }

        binding!!.circularView.setOnClickListener {
            context?.let { it1 -> chooseImage(it1) };

        }

        binding!!.rlSaveButton.setOnClickListener {
            binding!!.pullToRefresh.setOnRefreshListener {
                if(internetCheck()){
                    updateProfileApi()
                }
                else{
                    Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

                }


            }
            if(internetCheck()){
                updateProfileApi()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }

        }


    }



    private fun internetCheck(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }




    fun logoutApi(){
        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        val id = sessionManagement.getUserId()
        logoutViewModel.logoutAccount(id.toString()).observe(viewLifecycleOwner){result->
            when(result){

                is NetworkResult.Success ->{
                    sessionManagement.closeDialog()
                    result.data?.let { logoutResponse ->

                }
                    binding?.pullToRefresh?.isRefreshing = false}
                is NetworkResult.Error ->  {
                    sessionManagement.closeDialog()
                    binding?.pullToRefresh?.isRefreshing = false
                }
                is NetworkResult.Loading -> TODO()

            }

        }

    }

    fun deleteApi(){
        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        val id = sessionManagement.getUserId()
        deleteViewModel.deleteAccount(id.toString()).observe(viewLifecycleOwner){result->
            when(result){
                is NetworkResult.Success -> {
                    sessionManagement.closeDialog()
                    result.data?.let { deleteResponse ->


                    }
                    binding?.pullToRefresh?.isRefreshing = false
                }
                is NetworkResult.Error ->  {
                    sessionManagement.closeDialog()
                    binding?.pullToRefresh?.isRefreshing = false
                }
                is NetworkResult.Loading -> TODO()

            }

        }

    }


    fun getProfileApi(){
        val id = sessionManagement.getUserId()


        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        myProfileViewModel.getProfile(id.toString()).observe(viewLifecycleOwner){result->
            when(result){
                is NetworkResult.Success -> {
                    sessionManagement.closeDialog()
                    result.data?.let { getProfileResponse ->

                        try {
                            binding?.apply {
                                etFullName.setText(getProfileResponse.data.name ?: "N/A")
                                etEmail.setText(getProfileResponse.data.email ?: "N/A")
                                etPhone.setText(getProfileResponse.data.phone?: "N/A")
                                if (getProfileResponse.data.phone != null){
                                    sessionManagement.setUserPhoneNumber(getProfileResponse.data.phone)
                                }

                            }

                            Glide.with(requireContext()).load( BaseUrl.baseUrlImages+getProfileResponse.data.profile_img ?: R.drawable.user_img).into(binding!!.profileImg)

                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                        binding?.pullToRefresh?.isRefreshing = false


                      }

                }
                is NetworkResult.Error -> {
                    sessionManagement.closeDialog()
                    binding?.pullToRefresh?.isRefreshing = false
                }

                is NetworkResult.Loading -> {
                    // this?.let { sessionMangment?.openDialog(it) }!!
                }

            }

        }

    }


    fun updateProfileApi() {
        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        val idReq = sessionManagement.getUserId().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val nameReq = binding?.etFullName?.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val emailReq = binding?.etEmail?.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneReq = binding?.etPhone?.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordReq = binding?.etPassword?.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val profilePic: MultipartBody.Part? = tempUri?.let { uri ->
            val file = File(getPath(requireContext(), uri))
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("profile_img", file.path, requestFile)
            } else {
                null
            }
        }



        updateProfileViewModel.updateProfile(
            idReq,
            nameReq,
            emailReq,
            phoneReq,
            passwordReq,
            profilePic
        ).observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    sessionManagement.closeDialog()

                    binding?.pullToRefresh?.isRefreshing = false


                }
                is NetworkResult.Error -> {
                    sessionManagement.closeDialog()
                    binding?.pullToRefresh?.isRefreshing = false
                }
                is NetworkResult.Loading -> {
                    // Optionally handle loading state
                }
            }
        }
    }





    private fun logoutDialog() {

        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_logout_account)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imgCross = dialog.findViewById<ImageView>(R.id.img_cross)
        dialog.show()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        imgCross.setOnClickListener { v14: View? ->
            dialog.dismiss()
        }

        var cancel:RelativeLayout = dialog.findViewById(R.id.rl_cancel)
        var yes:RelativeLayout=dialog.findViewById(R.id.rl_yes)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        yes.setOnClickListener {
            binding!!.pullToRefresh.setOnRefreshListener {
                if(internetCheck()){
                    logoutApi()
                }
                else{
                    Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

                }


            }

            if(internetCheck()){
                logoutApi()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }


            sessionManagement.logOut()
            var intent:Intent=Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()


        }

    }


    private fun deleteDialog() {

        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_delete_account)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imgCross = dialog.findViewById<ImageView>(R.id.img_cross)

        dialog.show()
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        imgCross.setOnClickListener { v14: View? ->
            dialog.dismiss()
        }
        var cancel:RelativeLayout = dialog.findViewById(R.id.rl_cancel)
        var yes:RelativeLayout=dialog.findViewById(R.id.rl_yes)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        yes.setOnClickListener {
            binding!!.pullToRefresh.setOnRefreshListener {
                if(internetCheck()){
                    deleteApi()
                }
                else{
                    Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

                }


            }
            if(internetCheck()){
                deleteApi()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }
            var intent:Intent=Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(optionsMenu,
            DialogInterface.OnClickListener { dialogInterface, i ->
                if (optionsMenu[i] == "Take Photo") {
                    // Open the camera and get the photo
                    //dispatchTakePictureIntent()
                    checkCameraPermission()
                } else if (optionsMenu[i] == "Choose from Gallery") {
                    // choose from  external storage
                    checkGalleryPermission()
                } else if (optionsMenu[i] == "Exit") {
                    dialogInterface.dismiss()
                }
            })
        builder.show()
    }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_PERMISSION_CODE)
        } else {
            openGalleryForImages()
        }
    }



    private fun checkPermission():Boolean{
        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            val write = activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            val read= activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            write== PackageManager.PERMISSION_GRANTED &&read== PackageManager.PERMISSION_GRANTED
        }
    }


//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode==STORAGE_PERMISSION_CODE || requestCode == CAMERA_PERMISSION_CODE){
//            val write=grantResults[0] == PackageManager.PERMISSION_GRANTED
//            val read=grantResults[0]== PackageManager.PERMISSION_GRANTED
//
//            if(write && read){
////Do our task
//                context?.let { chooseImage(it) }
//            }else{
//
//                Toast.makeText(activity,"OnRequestPermissionResult:External Storage Permmission denied",Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    Toast.makeText(context, "Camera permission is required to use the camera.", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryForImages()
                } else {
                    Toast.makeText(context, "Storage permission is required to access the gallery.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun requestPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            try{
                val intent= Intent()
                intent.action= Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri= Uri.fromParts("package",activity?.packageName,null)
                intent.data=uri
                storageActivityResultLauncher.launch(intent)
            }catch(e:Exception){
                val intent=Intent()
                intent.action= Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }else{
            activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),GALLERY_PERMISSION_CODE )
            }
        }
    }

    private val storageActivityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){
                context?.let { chooseImage(it) }
            }else{
                Toast.makeText(context,"Manage External Storage permission is denied",Toast.LENGTH_LONG).show()
            }
        }else{

        }
    }


    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , REQUEST_CODE
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }

    }



    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user`

            Toast.makeText(context, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }



    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path =
//            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//        return Uri.parse(path)

        var tempFile: File? = null
        try {
            tempFile = File.createTempFile("plethora", ".png")
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
            val bitmapData = bytes.toByteArray()
            var fileOutPut: FileOutputStream? = null
            fileOutPut = FileOutputStream(tempFile)
            fileOutPut.write(bitmapData)
            fileOutPut.flush()
            fileOutPut.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(tempFile)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
             tempUri = context?.let { getImageUri(it, bitmap) }
          //  binding?.profileImg?.setImageURI(tempUri)


            Glide.with(requireContext()).load(tempUri ?: R.drawable.user_img).into(binding!!.profileImg)

        }
        else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            // if multiple images are selected
            if (data?.getClipData() != null) {

                Toast.makeText(context,"You can not select multiple images",Toast.LENGTH_LONG).show()
            } else if (data?.getData() != null) {
                // if single image is selected

                 tempUri = data.data!!
                Glide.with(requireContext()).load(tempUri ?: R.drawable.user_img).into(binding!!.profileImg)

                //   iv_image.setImageURI(tempUri) Here you can assign the picked image uri to your imageview
             // binding!!.profileImg.setImageURI(tempUri)
                Log.d("tempUri" , tempUri.toString())
            }
        }
    }




    fun getPath(context: Context?, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)

                )
                return context?.let { getDataColumn(it, contentUri, null, null) }

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return context?.let { getDataColumn(it, contentUri, selection, selectionArgs) }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else context?.let {
                getDataColumn(
                    it, uri, null, null)
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


}