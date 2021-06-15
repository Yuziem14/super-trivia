package com.yuziem14.supertrivia.fragments.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuziem14.supertrivia.R
import kotlinx.android.synthetic.main.fragment_resume_game.view.*

class ResumeGameFragment : Fragment() {
    private lateinit var inflatedView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_resume_game, container, false)
        setup(arguments?.getLong("score")!!, arguments?.getBoolean("isCorrect")!!)
        return this.inflatedView
    }

    fun setup(score: Long, isCorrect: Boolean) {
        this.inflatedView.answerStatus.text =
            if (isCorrect) getString(R.string.correct_answer) else getString(R.string.incorrect_answer)
        this.inflatedView.scoreText.text = "${getString(R.string.score)}: ${score.toString()}"
        this.inflatedView.nextButton.setOnClickListener {
            findNavController().navigate(R.id.resume_to_game)
        }
        this.inflatedView.finishButton.setOnClickListener { findNavController().navigate(R.id.resume_to_gameover) }
    }
}