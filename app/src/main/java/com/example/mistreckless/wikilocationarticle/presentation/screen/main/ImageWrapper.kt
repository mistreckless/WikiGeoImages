package com.example.mistreckless.wikilocationarticle.presentation.screen.main

import com.example.mistreckless.wikilocationarticle.domain.entity.Image

/**
 * Created by mistreckless on 30.10.17.
 */
class ImageWrapper(private val imageAdapter: ImageAdapter){

    fun getLastImage() : Image?=if (imageAdapter.items.isNotEmpty()) imageAdapter.items.last() else null

    fun getItemCount() : Int = imageAdapter.itemCount

}