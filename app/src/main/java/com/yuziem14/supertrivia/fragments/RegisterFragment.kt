package com.yuziem14.supertrivia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.R
import kotlinx.android.synthetic.main.fragment_auth.view.*

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.authLink.setOnClickListener { goToAuth() }
        return view
    }

    fun goToAuth() {
        findNavController().navigate(R.id.registerToAuth)
    }
}