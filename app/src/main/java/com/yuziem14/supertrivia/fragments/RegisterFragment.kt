package com.yuziem14.supertrivia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.dao.UserDAO
import com.yuziem14.supertrivia.models.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {
    private lateinit var inflatedView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.inflatedView = inflater.inflate(R.layout.fragment_register, container, false)

        inflatedView.authLink.setOnClickListener { goToAuth() }
        inflatedView.registerButton.setOnClickListener {
            inflatedView.let {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                val confirmPassword = confirmPasswordInput.text.toString()

                handleRegister(name, email, password, confirmPassword)
            }
        }
        return inflatedView
    }

    private fun validatePassword(password: String, confirmPassword: String): HashMap<String, String> {
        val errorsMap = HashMap<String, String>()
        val resources = context?.resources
        if(password.length < 6) {
            resources?.let {
                errorsMap.put("password",
                    it.getString(R.string.is_too_short, it.getString(R.string.password)),
                )
            }
        }
        if(!password.equals(confirmPassword)) {
            resources?.let {
                errorsMap.put("confirmPassword",
                    it.getString(R.string.fields_mismatch, it.getString(R.string.confirm_password), it.getString(R.string.password)),
                )
            }
        }

        return errorsMap
    }

    private fun handleRegister(name: String, email: String, password: String, confirmPassword: String) {
        val passwordErrors = validatePassword(password, confirmPassword)
        if(passwordErrors.isNotEmpty()) {
            if(passwordErrors["password"] != null) {
                inflatedView.passwordErrorsText.apply {
                    visibility = View.VISIBLE
                    inflatedView.passwordErrorsText.text = passwordErrors["password"]
                }
            }
            if(passwordErrors["confirmPassword"] != null) {
                inflatedView.confirmPasswordErrorsText.apply {
                    visibility = View.VISIBLE
                    text = passwordErrors["confirmPassword"]
                }

            }
            return
        }

        val user = User(name, email, password)
        UserDAO().register(user,
            {
                response ->
                this.goToAuth()
            },
            {
                error ->
                if(error.data.errors.name.isNotEmpty()) {
                    inflatedView.nameErrorsText.apply {
                        text = getErrorString(resources.getString(R.string.name), error.data.errors.name.first()).toString()
                        visibility = View.VISIBLE
                    }
                }
                if(error.data.errors.email.isNotEmpty()) {
                    inflatedView.emailErrorsText.apply {
                        text = getErrorString(resources.getString(R.string.email), error.data.errors.email.first()).toString()
                        visibility = View.VISIBLE
                    }
                }
                if(error.data.errors.password.isNotEmpty()) {
                    inflatedView.passwordErrorsText.apply {
                        text = getErrorString(resources.getString(R.string.password), error.data.errors.password.first()).toString()
                        visibility = View.VISIBLE
                    }
                }
            })
    }

    private fun getErrorString(fieldName: String, error: String): String? {
        return when {
            Regex("can\'t be blank").containsMatchIn(error) -> context?.resources?.getString(
                R.string.cant_be_blank,
                fieldName
            )
            Regex("has already been taken").containsMatchIn(error) -> context?.resources?.getString(
                R.string.has_already_been_taken,
                fieldName
            )
            Regex("is too short").containsMatchIn(error) -> context?.resources?.getString(
                R.string.is_too_short,
                fieldName
            )
            Regex("is invalid").containsMatchIn(error) -> context?.resources?.getString(
                R.string.is_invalid,
                fieldName
            )
            else -> null
        }
    }

    private fun goToAuth() {
        findNavController().navigate(R.id.registerToAuth)
    }
}