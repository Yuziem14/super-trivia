package com.yuziem14.supertrivia.fragments.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.dao.GameDAO
import com.yuziem14.supertrivia.models.Category
import kotlinx.android.synthetic.main.fragment_game.view.*

class GameFragment : Fragment() {
    private lateinit var inflatedView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_game, container, false)
        this.setup()
        return this.inflatedView
    }

    fun setup() {
        toggleElements(isGameRunning())
        inflatedView.playButton.setOnClickListener { startGame() }
    }

    fun toggleElements(isRunning: Boolean) {
        this.inflatedView.apply {
            alertView.visibility = if(isRunning) View.GONE else View.VISIBLE
            infoText.text = if(isRunning) "" else getString(R.string.start_game_instructions)
            playButton.visibility = if(isRunning) View.GONE else View.VISIBLE
            alertView.visibility = if(isRunning) View.GONE else View.VISIBLE
            answersList.visibility = if(isRunning) View.VISIBLE else View.GONE
        }
    }

    fun isGameRunning(): Boolean {
        return context
            ?.getSharedPreferences("game", Context.MODE_PRIVATE)
            ?.getBoolean("is_running", false) == true
    }

    fun getAuthToken(): String {
        return context
            ?.getSharedPreferences("auth", Context.MODE_PRIVATE)
            ?.getString("token", "")
            .toString()
    }

    fun getSettings(): HashMap<String, String?> {
        val hash = HashMap<String, String?>()
        context
            ?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            ?.apply {
                hash.put("category", getLong("category_id", Category.RANDOM_CATEGORY_ID).toString())
                hash.put("difficulty", getString("difficulty", null))
            }

        return hash
    }

    fun startGame() {
        val token = getAuthToken()
        val settings = getSettings()

        GameDAO().fetch(
            token, settings.get("difficulty"), settings.get("category")?.toLong(),
            {
                response ->
                context
                    ?.getSharedPreferences("game", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.apply {
                        putBoolean("is_running", true)
                        putString("started_at", response.data.game.started_at)
                        putLong("score", response.data.game.score)
                        apply()
                    }

                toggleElements(true)
            },
            {
                error ->
            }
        )
    }
}