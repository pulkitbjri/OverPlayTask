package com.eezy.util

import android.content.Context
import androidx.core.content.ContextCompat
import timber.log.Timber
import javax.inject.Inject

interface ResourceProvider {
    fun getString(id: Int): String
    fun getString(id: Int, vararg formatArg: Any?): String
    fun getColor(id: Int): Int
}

class ResourceProviderImpl @Inject constructor(
    private val context: Context
) : ResourceProvider {

    override fun getString(id: Int): String {
        return try {
            context.getString(id)
        } catch (exc: Exception) {
            Timber.e(exc)
            ""
        }
    }

    override fun getColor(id: Int): Int {
        return try {
            ContextCompat.getColor(context, id)
        } catch (exc: Exception) {
            Timber.e(exc)
            0
        }
    }

    override fun getString(id: Int, vararg formatArg: Any?): String {
        return getString(id).format(*formatArg)
//        return try {
//            context.getString(id, formatArg)
//        } catch (exc: Exception) {
//            Timber.e(exc)
//            ""
//        }
    }
}
