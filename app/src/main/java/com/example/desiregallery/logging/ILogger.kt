package com.example.desiregallery.logging

/**
 * @author babaetskv on 18.09.19
 */
interface ILogger {
    fun logInfo(tag: String, message: String)
    fun logDebug(tag: String, message: String)
    fun logWarning(tag: String, message: String)
    fun logError(tag: String, message: String)
}