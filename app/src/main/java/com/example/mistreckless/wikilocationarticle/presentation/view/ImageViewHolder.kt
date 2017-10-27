package com.example.mistreckless.wikilocationarticle.presentation.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.mistreckless.wikilocationarticle.R

/**
 * Created by mistreckless on 27.10.17.
 */
class ImageViewHolder(parent : ViewGroup?) : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.image_item,parent,false)) {

    var txtTitle : TextView = itemView.findViewById(R.id.txtTitle)

}