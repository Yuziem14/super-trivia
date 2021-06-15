package com.yuziem14.supertrivia.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.SessionActivity
import com.yuziem14.supertrivia.dao.CategoryDAO
import com.yuziem14.supertrivia.dao.GameDAO
import com.yuziem14.supertrivia.models.Category
import com.yuziem14.supertrivia.models.Difficulty
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
        this.setup()
        return view
    }

    private fun setup() {
        this.setupProfile()
        this.loadDifficulties()
        this.loadCategories()
        this.inflatedView.logoutButton.setOnClickListener { logout() }
    }

    private fun setupProfile() {
        activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)?.apply {
            inflatedView.usernameText.text = getString("name", "")
            inflatedView.userEmailText.text = "<${getString("email", "")}>"
        }
    }

    private fun loadDifficulties() {
        val difficulties: List<Difficulty> = listOf(
            Difficulty(resources.getString(R.string.easy), "easy"),
            Difficulty(resources.getString(R.string.medium), "medium"),
            Difficulty(resources.getString(R.string.hard), "hard"),
        )
        val difficultyPref = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getString("difficulty", "easy")
        val selectedDifficulty = difficulties.filter { it.slug.equals(difficultyPref) }.firstOrNull()
        ArrayAdapter<Difficulty>(requireContext(), android.R.layout.simple_spinner_item, difficulties).also {
            adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.inflatedView.difficultySpinner.adapter = adapter
                this.inflatedView.difficultySpinner.setSelection(adapter.getPosition(selectedDifficulty))
                this.inflatedView.difficultySpinner.onItemSelectedListener = difficultyItemSelectListener()
        }
    }

    private fun loadCategories() {
        val categories: MutableList<Category> = mutableListOf(Category(Category.RANDOM_CATEGORY_ID, resources.getString(R.string.random)))
        CategoryDAO().fetch({
            response ->
            categories.addAll(response.data.categories)
            val categoryIdPref = context?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getLong("category_id", Category.RANDOM_CATEGORY_ID)
            val selectedCategory = categories.filter { it.id == categoryIdPref }.firstOrNull()
            ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item, categories).also {
                adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    this.inflatedView.categorySpinner.adapter = adapter
                    this.inflatedView.categorySpinner.setSelection(adapter.getPosition(selectedCategory))
                    this.inflatedView.categorySpinner.onItemSelectedListener = categoryItemSelectListener()
            }
        }, {
            error -> // TODO
        })
    }

    private fun difficultyItemSelectListener(): AdapterView.OnItemSelectedListener {
        return object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                context?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()?.apply {
                    val difficulty = adapter?.selectedItem as Difficulty
                    putString("difficulty", difficulty.slug)
                    apply()
                }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}

        }
    }

    private fun categoryItemSelectListener(): AdapterView.OnItemSelectedListener {
        return object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                context?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()?.apply {
                    val category = adapter?.selectedItem as Category
                    putLong("category_id", category.id)
                    putString("category_name", category.name)
                    apply()
                }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}

        }
    }

    fun getAuthToken(): String {
        return context
            ?.getSharedPreferences("auth", Context.MODE_PRIVATE)
            ?.getString("token", "")
            .toString()
    }

    private fun logout() {
        GameDAO().destroy(
            getAuthToken(),
            {
                response -> clearSession()

            },
            {
                error -> clearSession()
            }
        )


    }

    private fun clearSession() {
        listOf("auth", "settings", "game").forEach {
                pref ->
            activity?.getSharedPreferences(pref, Context.MODE_PRIVATE)?.edit()?.apply {
                clear()
                apply()
            }
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