package com.yuziem14.supertrivia.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuziem14.supertrivia.MainActivity
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.SessionActivity
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {
    private lateinit var inflatedView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        this.inflatedView = view

        this.setupProfile()
        this.inflatedView.logoutButton.setOnClickListener { logout() }
        return view
    }

    private fun setupProfile() {
        activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)?.apply {
            inflatedView.usernameText.text = getString("name", "")
            inflatedView.userEmailText.text = "<${getString("email", "")}>"

        }
    }

    private fun logout() {
        activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)?.edit()?.apply {
            clear()
            apply()
        }

        goToLogin()
    }

    fun goToLogin() {
        val intent = Intent(activity, SessionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}