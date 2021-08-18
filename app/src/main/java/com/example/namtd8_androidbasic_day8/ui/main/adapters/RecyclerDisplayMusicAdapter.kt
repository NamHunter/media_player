package com.example.namtd8_androidbasic_day8.ui.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.namtd8_androidbasic_day8.R
import com.example.namtd8_androidbasic_day8.callback.ItemClickListener
import com.example.namtd8_androidbasic_day8.data.models.Music
import com.example.namtd8_androidbasic_day8.databinding.ItemMusicBinding


class RecyclerDisplayMusicAdapter(private val itemClickLister: ItemClickListener):
    RecyclerView.Adapter<RecyclerDisplayMusicAdapter.ViewHolder>() {

    private var mListStudent = mutableListOf<Music>()

    fun updateData(listStudent: MutableList<Music>){
        this.mListStudent = listStudent
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(music: Music){
            binding.txtGenre.text = music.genre
            binding.txtTittle.text = music.title

            Log.d("TAG", "bind: ${music.image}")
            Glide.with(binding.imgAvatar)
                .load(music.image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imgAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mListStudent[position])

        holder.itemView.setOnClickListener {
            itemClickLister.onCellClickListener(position)
        }
    }

    override fun getItemCount(): Int = mListStudent.size
}