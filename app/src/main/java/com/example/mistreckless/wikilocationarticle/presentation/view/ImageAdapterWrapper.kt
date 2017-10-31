package com.example.mistreckless.wikilocationarticle.presentation.view

import com.example.mistreckless.wikilocationarticle.domain.entity.Image

/**
 * Created by mistreckless on 30.10.17.
 */
class ImageAdapterWrapper(private val imageAdapter: ImageAdapter){

    fun getLastImage() : Image?=if (imageAdapter.items.isNotEmpty()) imageAdapter.items.last() else null

}