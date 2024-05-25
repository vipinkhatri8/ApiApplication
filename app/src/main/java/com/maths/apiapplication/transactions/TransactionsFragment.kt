package com.maths.apiapplication.transactions

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
import com.maths.apiapplication.transactions.viewmodel.TransactionsViewModel


class TransactionsFragment : Fragment() {
    var binding:FragmentTransactionsBinding?=null;

    var tranactionAdapter: TransactionAdapter?=null;
   // var transactonList: ArrayList<TransactionData> = arrayListOf()
    lateinit var sessionMangment: SessionManagement

    val transactionsViewModel : TransactionsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TransactionsViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding=FragmentTransactionsBinding.inflate(inflater,container,false);
sessionMangment = SessionManagement(requireActivity())
        initialize()


        binding!!.pullToRefresh.setOnRefreshListener {
            // on below line we are setting is refreshing to false.


            if(internetCheck()){
                fetchTransactions()
            }
            else{
                Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

            }

        }


        if(internetCheck()){
            fetchTransactions()
        }
        else{
            Toast.makeText(requireContext(),"please check your network",Toast.LENGTH_SHORT).show()

        }

        return binding!!.root;
    }

    private fun initialize() {




        tranactionAdapter= context?.let { TransactionAdapter(it, arrayListOf())  };
        var linearLayoutManager1  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerTransaction.layoutManager=linearLayoutManager1
        binding!!.recyclerTransaction.adapter=tranactionAdapter





        binding!!.backImg.setOnClickListener { activity?.onBackPressed() }

    }


    private fun internetCheck(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }
    fun fetchTransactions() {
    //    var user_id = sessionMangment.getUserId().toString()
       var user_id = "2"
        transactionsViewModel.fetchTransactions(user_id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Error -> {
                    binding!!.pullToRefresh.isRefreshing = false

                }

                is NetworkResult.Loading -> TODO()
                is NetworkResult.Success -> {
                    result.data?.let { transactionsResponse ->


                            // on below line we are setting is refreshing to false.
                        binding!!.pullToRefresh.isRefreshing = false
                            tranactionAdapter?.updateData(transactionsResponse.data)






                    }
                }

            }


        }


    }}