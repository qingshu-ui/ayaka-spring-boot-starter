package io.github.qingshu.ayaka.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

object NetUtils {

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient()

    fun get(url: String): Response {
        val req = Request.Builder().url(url).get().build()
        return client.newCall(req).execute()
    }

    fun post(url: String, json: String): Response {
        val req = Request.Builder().url(url).post(json.toRequestBody(mediaType)).build()
        return client.newCall(req).execute()
    }

    fun post(url: String, json: String, headers: Map<String, String>): Response {
        val req = Request.Builder().url(url).post(json.toRequestBody(mediaType))
        headers.forEach {
            req.header(it.key, it.value)
        }
        return client.newCall(req.build()).execute()
    }

    fun post(url: String, headers: Map<String, String>, json: String, readTimeout: Long): Response {
        val req = Request.Builder().url(url).post(json.toRequestBody(mediaType))
        headers.forEach {
            req.header(it.key, it.value)
        }
        val client = client.newBuilder()
        return client.readTimeout(readTimeout, TimeUnit.SECONDS).build().newCall(req.build()).execute()
    }
}