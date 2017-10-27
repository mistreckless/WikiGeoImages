package com.example.mistreckless.wikilocationarticle.data.network

import com.example.mistreckless.wikilocationarticle.data.dto.ImageQuery
import com.example.mistreckless.wikilocationarticle.data.dto.ImageResponse
import com.example.mistreckless.wikilocationarticle.data.dto.Page
import com.example.mistreckless.wikilocationarticle.data.dto.Pages
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by mistreckless on 27.10.17.
 */
class WikiImagesConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return Converter<ResponseBody, ImageResponse> { body ->
            val gson = JSONObject(body.string())
            val keys = gson.getJSONObject("query").getJSONObject("pages").keys()
            if (keys.hasNext()) {
                val page = Gson().fromJson<Page>(gson.getJSONObject("query").getJSONObject("pages").getString(keys.next()), Page::class.java)
                return@Converter ImageResponse(query = ImageQuery(Pages(page)))
            } else
                ImageResponse()
        }
    }
}