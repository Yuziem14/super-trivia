package com.yuziem14.supertrivia.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.dao.UserDAO
import com.yuziem14.supertrivia.models.User
import kotlinx.android.synthetic.main.fragment_auth.view.*
import kotlinx.android.synthetic.main.fragment_register.*

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
        view.registerButton.setOnClickListener {
            view.let {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                val confirmPassword = confirmPasswordInput.text.toString()

                handleRegister(name, email, password, confirmPassword)
            }
        }
        return view
    }

    private fun validate(password: String, confirmPassword: String): Boolean {
        return password.equals(confirmPassword) && password.length > 5
    }

    private fun handleRegister(name: String, email: String, password: String, confirmPassword: String) {
        if(!validate(password, confirmPassword)) {
            Toast.makeText(context, "Confira sua senha e a confirmação", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(name, email, password)
        UserDAO().register(user,
            {
                response ->
                Log.d("response", response.toString())
                this.goToAuth()
            },
            {
                error ->
                Toast.makeText(context, "Houve um erro com seu registro! Tente novamente.", Toast.LENGTH_SHORT).show()
            })
    }

    private fun goToAuth() {
        findNavController().navigate(R.id.registerToAuth)
    }
}