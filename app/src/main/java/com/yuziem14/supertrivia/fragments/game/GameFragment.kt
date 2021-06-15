package com.yuziem14.supertrivia.fragments.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.adapters.AnswerListAdapter
import com.yuziem14.supertrivia.dao.GameDAO
import com.yuziem14.supertrivia.dao.ProblemDAO
import com.yuziem14.supertrivia.models.Category
import com.yuziem14.supertrivia.models.Problem
import com.yuziem14.supertrivia.parsers.HTMLParser
import kotlinx.android.synthetic.main.fragment_game.view.*

class GameFragment : Fragment() {
    private lateinit var inflatedView: View
    private lateinit var adapter: AnswerListAdapter

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
        this.inflatedView.playButton.setOnClickListener { startGame() }
        this.inflatedView.answerButton.setOnClickListener { answerProblem() }
        loadProblem()
        configureRecyclerView()
    }

    fun configureRecyclerView() {
        this.inflatedView.answersList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = AnswerListAdapter()
        }
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

    fun existPendingProblem(): Boolean {
        return context
            ?.getSharedPreferences("game", Context.MODE_PRIVATE)
            ?.getLong("problem_id", -1L) != -1L
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

    fun showProblem(problem: Problem) {
        this.inflatedView.infoText.text = HTMLParser.parse(problem.question)
        this.adapter = AnswerListAdapter(problem.answers.toMutableList())
        this.inflatedView.answersList.adapter = this.adapter
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
                nextProblem()
            },
            {
                error ->
            }
        )
    }

    fun loadProblem() {
        if(!isGameRunning()) return

        val existProblem = existPendingProblem()
        if(existProblem) {
            fetchProblem()
            return
        }

        nextProblem()
    }

    fun fetchProblem() {
        ProblemDAO().fetch(
            getAuthToken(),
            {
                    response ->
                showProblem(response.data.problem)
            },
            {
                    error ->
            }
        )
    }

    fun nextProblem() {
        ProblemDAO().next(
            getAuthToken(),
            {
                response ->
                context
                    ?.getSharedPreferences("game", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.apply {
                        putLong("problem_id", response.data.problem.id!!)
                        apply()
                    }
                    showProblem(response.data.problem)
            },
            {
                error ->
            }
        )
    }

    fun answerProblem() {
        val answer = this.adapter.getSelectedAnswer()
        if(answer == null) {
            Toast.makeText(context, getString(R.string.select_an_answer), Toast.LENGTH_SHORT).show()
            return
        }

        ProblemDAO().answer(
            getAuthToken(),
            this.adapter.getSelectedAnswer()!!.order!!,
            {
                    response ->
                    context
                        ?.getSharedPreferences("game", Context.MODE_PRIVATE)
                        ?.edit()
                        ?.apply {
                            remove("problem_id")
                            apply()
                        }

                    val isCorrect = response.data.answer.status == "correct"
                    val data = Bundle()
                    data.apply {
                        putLong("score", response.data.answer.score)
                        putBoolean("isCorrect", isCorrect)
                    }

                    findNavController().navigate(R.id.game_to_resume, data)
            },
            {
                    error ->
            }
        )
    }
}