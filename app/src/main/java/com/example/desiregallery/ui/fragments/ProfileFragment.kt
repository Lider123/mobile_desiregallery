package com.example.desiregallery.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.ui.activities.MainActivity


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val toolbarProfile = root.findViewById<Toolbar>(R.id.profile_toolbar)
        toolbarProfile.title = (activity as MainActivity).getCurrUser()?.getLogin()
        return root
    }
}
