package com.example.mistreckless.wikilocationarticle.presentation.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.mistreckless.wikilocationarticle.domain.entity.Image
import com.example.mistreckless.wikilocationarticle.presentation.view.ImageViewHolder

/**
 * Created by mistreckless on 27.10.17.
 */
class ImageAdapter : RecyclerView.Adapter<ImageViewHolder>(){

    val items : MutableList<Image> by lazy { mutableListOf<Image>() }

    override fun onBindViewHolder(holder: ImageViewHolder?, position: Int) {
        holder?.txtTitle?.text=items[position].title
    }

    override fun getItemCount()=items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)=ImageViewHolder(parent)
}