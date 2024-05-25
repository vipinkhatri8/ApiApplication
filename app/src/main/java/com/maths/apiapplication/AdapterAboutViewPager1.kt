package com.maths.apiapplication

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class AdapterAboutViewPager1( val context: Context) : RecyclerView.Adapter<AdapterAboutViewPager1.SubViewHolder>() {
    var imagelist: List<GetAboutImage> = emptyList()
    private var paused = false
    class SubViewHolder(val binding: LayoutforviewpagerinaboutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val binding = LayoutforviewpagerinaboutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubViewHolder(binding)
    }

    override fun getItemCount(): Int = imagelist.size

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val currentItem = imagelist[position]
        if (currentItem.aboutMediaUrl.endsWith(".mp4")) {
            holder.binding.image12.visibility = View.GONE
            holder.binding.videoView.visibility = View.VISIBLE
            holder.binding.videoView.setVideoURI(Uri.parse(BaseUrl.about + currentItem.aboutMediaUrl))
            holder.binding.videoView.start()
        } else {
            holder.binding.image12.visibility = View.VISIBLE
            holder.binding.videoView.visibility = View.GONE
            Glide.with(context).load(BaseUrl.about + currentItem.aboutMediaUrl).into(holder.binding.image12)
        }




        holder.binding.videoView.setOnClickListener {
            if (paused) {


                holder.binding.videoView.start()
                holder.binding.imagePlayVideo.visibility = View.GONE
                paused = false
                //  Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show()

            } else {
                holder.binding.videoView.pause()
                holder.binding.imagePlayVideo.visibility = View.VISIBLE
                paused = true                //   Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show()
            }
        }


    }


    fun updateData(newAbout: List<GetAboutImage>) {
        imagelist = newAbout
        notifyDataSetChanged()
    }


}
