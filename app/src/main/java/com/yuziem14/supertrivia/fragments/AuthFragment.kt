package com.yuziem14.supertrivia.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.MainActivity
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.dao.UserDAO
import com.yuziem14.supertrivia.models.User
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_auth.view.*

class AuthFragment : Fragment() {
    private lateinit var inflatedView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isSignedIn()) {
            goToMain()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        this.inflatedView = view
        inflatedView.registerLink.setOnClickListener { goToRegister() }
        inflatedView.signInButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            handleAuth(email, password)
        }
        return view
    }

    fun handleAuth(email: String, password: String) {
        inflatedView.authErrorText.visibility = View.GONE

        val user = User("", email, password)
        UserDAO().auth(user,
            {
                response ->
                    val token = response.data.user.token.toString()
                    context?.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        ?.edit()
                        ?.apply {
                            putString("token", token)
                            apply()
                        }
                    fetchLoggedUserData(token) {
                        goToMain()
                    }
            },
            {
                error ->
                inflatedView.authErrorText.apply {
                    text = resources.getString(R.string.invalid_credentials)
                    visibility = View.VISIBLE
                }
            }
        )
    }

    fun fetchLoggedUserData(token: String, finished: () -> Unit) {
        UserDAO().fetch(token,
            {
                user ->
                context?.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.apply {
                        putLong("id", user.id!!)
                        putString("name", user.name)
                        putString("email", user.email)

                        apply()
                    }
                    finished()
            },
            {
                error -> // TODO
            }
        )
    }

    fun isSignedIn(): Boolean {
        val token = context?.getSharedPreferences("auth", Context.MODE_PRIVATE)?.getString("token", "")
        return token != null && token.isNotBlank()
    }

    fun goToMain() {
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    fun goToRegister() {
        findNavController().navigate(R.id.authToRegister)
    }
}