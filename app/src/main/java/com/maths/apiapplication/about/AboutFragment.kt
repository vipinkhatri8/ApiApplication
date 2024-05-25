package com.maths.apiapplication.about

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.maths.apiapplication.about.models.GetAboutImage
import com.maths.apiapplication.about.viewmodel.AboutViewModel

class AboutFragment : Fragment(), OnItemClickBrowsing {

    var binding: FragmentAboutBinding? = null
    lateinit var  adapterForRecyclerView : AdapterForRecyclerView
    lateinit var adapterAboutViewPager1: AdapterAboutViewPager1

    //var datalist: ArrayList<GetAboutImage> = arrayListOf()
    var imagelist: ArrayList<GetAboutImage> = arrayListOf()
    var imagelist1: ArrayList<GetAboutImage> = arrayListOf()
    val aboutViewModel : AboutViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AboutViewModel::class.java)
    }
    lateinit var sessionMangment: SessionManagement
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        sessionMangment = SessionManagement(requireContext())

        binding!!.pullToRefresh.setOnRefreshListener {
            if(internetCheck()){
                aboutApi()
            }


        }

        if(internetCheck()){
            aboutApi()
        }


        initialize()
        binding?.backImg?.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding!!.root
    }

    private fun initialize() {


        adapterForRecyclerView = AdapterForRecyclerView(this,requireActivity(),arrayListOf(), arrayListOf())


        var linearLayoutManager1  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.layoutManager=linearLayoutManager1
        binding!!.recyclerView.adapter = adapterForRecyclerView
    }







    fun aboutApi(){
        requireContext()?.let { sessionMangment?.openDialog(it) }!!

        aboutViewModel.about().observe(requireActivity()){result->
            when(result){
                is NetworkResult.Success -> {
                    sessionMangment.closeDialog()
                    result.data?.let { aboutRespone ->


                        adapterForRecyclerView.updateData(aboutRespone.data)




                        binding?.pullToRefresh?.isRefreshing = false
                    }
                }
                is NetworkResult.Error -> {
                    sessionMangment.closeDialog()
                    Toast.makeText(requireActivity(), result.message,Toast.LENGTH_SHORT).show()
                    binding?.pullToRefresh?.isRefreshing = false
                }
                is NetworkResult.Loading -> TODO()



            }

        }
    }



    override fun onLeftButtonClick(position: Int, viewPager: ViewPager2) {
        var tab: Int = viewPager.currentItem
        if (tab > 0) {
            tab--
            viewPager.setCurrentItem(tab, true)
        } else if (tab == 0) {
            viewPager.setCurrentItem(tab, true)
        }
    }

    override fun onRightButtonClick(position: Int, viewPager: ViewPager2) {
        var tab: Int = viewPager.currentItem
        tab++
        viewPager.setCurrentItem(tab, true)
    }



}