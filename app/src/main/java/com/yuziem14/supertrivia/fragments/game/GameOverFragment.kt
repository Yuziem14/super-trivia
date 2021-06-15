package com.yuziem14.supertrivia.fragments.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.dao.GameDAO
import kotlinx.android.synthetic.main.fragment_game_over.view.*

class GameOverFragment : Fragment() {
    private lateinit var inflatedView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_game_over, container, false)
        finishGame()
        this.inflatedView.concludeButton.setOnClickListener { findNavController().navigate(R.id.gameover_to_settings) }
        return this.inflatedView
    }

    fun getAuthToken(): String {
        return context
            ?.getSharedPreferences("auth", Context.MODE_PRIVATE)
            ?.getString("token", "")
            .toString()
    }

    fun getUserName(): String {
        return context
            ?.getSharedPreferences("auth", Context.MODE_PRIVATE)
            ?.getString("name", "")
            .toString()
    }

    fun finishGame() {
        GameDAO().destroy(
            getAuthToken(),
            {
                response ->
                context
                    ?.getSharedPreferences("game", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.apply {
                        putBoolean("is_running", false)
                        putString("finished_at", response.data.game.finished_at)
                        apply()
                    }
                this.inflatedView.nameText.text = getUserName()
                this.inflatedView.finalScoreText.text = response.data.game.score.toString()
                this.inflatedView.startText.text = response.data.game.started_at
                this.inflatedView.finishText.text = response.data.game.finished_at
            },
            {
                error ->
            }
        )
    }
}