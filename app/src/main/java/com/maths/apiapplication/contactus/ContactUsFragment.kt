package com.maths.apiapplication.contactus

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maths.apiapplication.contactus.viewmodel.ContactUsViewModel

class ContactUsFragment : Fragment(){

    var binding:FragmentContactUsBinding?=null;
    val contactUsViewModel : ContactUsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ContactUsViewModel::class.java)
    }
    lateinit var sessionManagement: SessionManagement
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
sessionManagement = SessionManagement(requireActivity())
        binding=FragmentContactUsBinding.inflate(inflater,container,false)
        initialize()
        binding!!.backImg.setOnClickListener {
            activity?.onBackPressed()
        }



        return binding!!.root
    }


    private fun initialize() {
        val subjectList= arrayOf("Feedback","Complaint","Others")
        val subjectAdapter = ArrayAdapter(requireContext(),R.layout.item_drop_down, subjectList)
        binding!!.subjectCategory.setAdapter(subjectAdapter)

        if(getArguments()!=null){

            if(arguments?.getString(AppConstant.FEEDBACK)!=null){
                binding!!.subjectCategory.setText(binding!!.subjectCategory.getAdapter().getItem(0).toString(), false);
            }

            else if(arguments?.getString(AppConstant.COMPLAINT)!=null){
                binding!!.subjectCategory.setText(binding!!.subjectCategory.getAdapter().getItem(1).toString(), false);
            }

            else if(arguments?.getString(AppConstant.OTHERS)!=null){
                binding!!.subjectCategory.setText(binding!!.subjectCategory.getAdapter().getItem(2).toString(), false);
            }

        }

        binding!!.rlSubmitButton.setOnClickListener{

            if(internetCheck()){
                contactUsApi()
            }
            else{
                Toast.makeText(requireContext(),"Please check your network", Toast.LENGTH_SHORT).show()

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


    fun contactUsApi(){


        val user_name  = sessionManagement.getUserName().toString()
        val phone  = sessionManagement.getUserPhoneNumber().toString()
        val email  = sessionManagement.getUserEmail().toString()
        val user_type  = "User"
        // Retrieve the currently selected text value from the AutoCompleteTextView
        val subject = binding?.subjectCategory?.text.toString()
        Toast.makeText(requireContext(), subject.toString(), Toast.LENGTH_SHORT).show()

        val messages = binding?.etMessage?.text
        requireContext()?.let { sessionManagement?.openDialog(it) }!!
        contactUsViewModel.contactUs(user_name, phone, email, subject, messages.toString(), user_type).observe(requireActivity()){result->
            when(result){
                is NetworkResult.Success -> {
                    result.data?.let { contactUsResponse ->
                     Toast.makeText(requireContext(),contactUsResponse.message, Toast.LENGTH_SHORT).show()
                        sessionManagement.closeDialog()
                    }

                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),result.message, Toast.LENGTH_SHORT).show()
                    sessionManagement.closeDialog()
                }
                is NetworkResult.Loading -> TODO()
            }
        }
    }
}