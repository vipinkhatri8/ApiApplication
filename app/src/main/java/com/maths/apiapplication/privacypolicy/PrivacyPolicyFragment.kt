package com.maths.apiapplication.privacypolicy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class PrivacyPolicyFragment : Fragment() {

    var binding: FragmentPrivacyPolicyBinding? = null;
    private val privacyPolicyViewModel: PrivacyPolicyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PrivacyPolicyViewModel::class.java)
    }
    private lateinit var sessionManagement : SessionManagement

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        sessionManagement = SessionManagement(requireContext())
        intialize();


        binding!!.pullToRefresh.setOnRefreshListener {
            if(internetCheck()){
                privacyPolicyApi()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }


        }

        if (internetCheck()){
            privacyPolicyApi()
        } else{
            Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

        }


        return binding!!.root
    }

    private fun internetCheck(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }

    private class PicassoImageGetter(private val textView: TextView, private val context: Context) : Html.ImageGetter {
        var target: com.squareup.picasso.Target // Strong reference to Target

        init {
            target = object : com.squareup.picasso.Target, Html.ImageGetter {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    val drawable = BitmapDrawable(context.resources, bitmap)
                    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    // Set the loaded drawable into the TextView

                    // Trigger a redraw of the TextView


                    textView.post {
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                        updateTextViewHtml(textView, textView.text.toString(), this@PicassoImageGetter)
                    }
                }

                override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {

                    textView.post {
                        Toast.makeText(context, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, errorDrawable, null, null)
                    }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {


                    textView.post {
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, placeHolderDrawable, null, null)
                    }
                }


                private fun updateTextViewHtml(textView: TextView, html: String, imageGetter: Html.ImageGetter) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
                    } else {
                        @Suppress("DEPRECATION")
                        textView.text = Html.fromHtml(html, imageGetter, null)
                    }
                }
                override fun getDrawable(p0: String?): Drawable {
                    TODO("Not yet implemented")
                }
            }
        }

        override fun getDrawable(source: String?): Drawable {
            val placeholder = ContextCompat.getDrawable(context, R.drawable.user_img)!!
            placeholder.setBounds(0, 0, placeholder.intrinsicWidth, placeholder.intrinsicHeight)
            val decodedBytes: ByteArray = Base64.decode(source, Base64.DEFAULT)
//            if (source != null) {
//                Picasso.get().load(decodedBytes).error(R.drawable.plumbing).into(target)
//            }


            if (source != null) {
                try {
                    val base64String = source.substring(source.indexOf(",") + 1)
                    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    if (decodedBitmap != null) {
                        val drawable = BitmapDrawable(context.resources, decodedBitmap)
                        //  drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                        drawable.setBounds(0, 0, 1000, 800)
                        return drawable
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }



            return placeholder
        }
    }




    fun privacyPolicyApi() {
        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        privacyPolicyViewModel.privacyPolicy().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    sessionManagement.closeDialog()
                    result.data?.let { privacyPolicyResponse ->
                        binding?.pullToRefresh?.isRefreshing = false
                        try {
                            val imageGetter = PicassoImageGetter(binding!!.textDescription, requireContext())
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding?.textDescription?.text = Html.fromHtml(privacyPolicyResponse.data.description, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()

                        }


                    }


                }

                is NetworkResult.Error -> {
                    sessionManagement.closeDialog()
                    binding?.pullToRefresh?.isRefreshing = false

                }

                is NetworkResult.Loading -> TODO()

            }


        }

    }


    private fun intialize() {
        binding!!.backImg.setOnClickListener { activity?.onBackPressed() }
    }

}