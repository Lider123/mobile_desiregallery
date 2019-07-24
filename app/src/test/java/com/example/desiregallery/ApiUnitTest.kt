package com.example.desiregallery

import com.example.desiregallery.helpers.ModelGenerator
import com.example.desiregallery.network.DGNetwork
import org.junit.Assert
import org.junit.Test


class ApiUnitTest {
    @Test
    fun gettingPosts_responseOk() {
        val api = DGNetwork.getService()
        val response = api.getPosts().execute()
        Assert.assertEquals(200, response.code())
    }

    @Test
    fun gettingUsers_responseOk() {
        val api = DGNetwork.getService()
        val response = api.getUsers().execute()
        Assert.assertEquals(200, response.code())
    }

    @Test
    fun gettingPost_isCorrect() {
        val postGen = ModelGenerator.getPost("1")
        val api = DGNetwork.getService()
        val response = api.getPost("1").execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body())
        Assert.assertEquals(postGen, response.body())
    }

    @Test
    fun gettingUser_isCorrect() {
        val userGen = ModelGenerator.getUser("login2")
        val api = DGNetwork.getService()
        val response = api.getUser("login2").execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body())
        Assert.assertEquals(userGen, response.body())
    }
}