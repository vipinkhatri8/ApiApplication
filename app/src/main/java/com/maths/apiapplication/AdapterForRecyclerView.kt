package com.maths.apiapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


class AdapterForRecyclerView(private val listener: OnItemClickBrowsing, var context: Context, var datalist : ArrayList<AboutDataResponse>, var imagelist : ArrayList<GetAboutImage> ) : RecyclerView.Adapter<AdapterForRecyclerView.SubViewHolder>() {

    inner  class SubViewHolder(var binding : LayoutforaboutfirstpicturelistBinding) : RecyclerView.ViewHolder(binding.root){


        lateinit var adapter1: AdapterAboutViewPager1


        init {
            binding.leftNav1.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    listener.onLeftButtonClick(position, binding.viewpager)
                }
            }

            binding.rightNav1.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    listener.onRightButtonClick(position, binding.viewpager)
                }
            }
            binding?.viewpager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    toggleArrowVisibility1(position)
                }
            })



        }

        private fun toggleArrowVisibility1(position: Int) {

            val imageSize = datalist[adapterPosition].get_about_image.size
            if (imageSize <= 1) {
                binding.leftNav1.visibility = View.INVISIBLE
                binding.rightNav1.visibility = View.INVISIBLE
            } else {
                binding.leftNav1.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                binding.rightNav1.visibility =
                    if (position == imageSize - 1) View.INVISIBLE else View.VISIBLE
            }

            //  binding.leftNav1.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            //   binding.rightNav1.visibility = if (position == datalist[adapterPosition].get_about_image.size -1  ) View.INVISIBLE else View.VISIBLE


        }

        fun bind(aboutDataResponse: AboutDataResponse, context: Context, imagelist: ArrayList<GetAboutImage>) {
            adapter1 = AdapterAboutViewPager1(context)
            binding!!.viewpager.adapter = adapter1
            binding!!.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



            adapter1.updateData(aboutDataResponse.get_about_image)
            binding.textDescription1.setText(aboutDataResponse.description)
            binding.title.setText(aboutDataResponse.title)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val binding = LayoutforaboutfirstpicturelistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubViewHolder(binding)
    }

    override fun getItemCount(): Int = datalist.size

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(datalist[position] , context ,datalist[position].get_about_image)
    }


    fun updateData(newAbout: ArrayList<AboutDataResponse>) {
        datalist.clear()
        datalist.addAll(newAbout)
        notifyDataSetChanged()
    }



}