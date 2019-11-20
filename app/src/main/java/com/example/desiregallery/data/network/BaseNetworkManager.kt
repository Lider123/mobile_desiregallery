package com.example.desiregallery.data.network

import com.example.desiregallery.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException

/**
 * @author babaetskv on 21.11.19
 */
abstract class BaseNetworkManager {

    protected suspend fun <T : Any> makeSafeCall(call: Call<T>, errorMessage: String): Result<T> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = call.execute()
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error(IOException(errorMessage + "\nResponse received with code ${response.code()}"))
        }
        catch (e: Exception) {
            Result.Error(IOException(errorMessage + "\n${e.message}"))
        }
    }
}