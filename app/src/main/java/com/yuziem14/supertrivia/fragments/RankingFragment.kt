package com.yuziem14.supertrivia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.adapters.AnswerListAdapter
import com.yuziem14.supertrivia.adapters.RankingAdapter
import com.yuziem14.supertrivia.dao.RankingDAO
import kotlinx.android.synthetic.main.fragment_game.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*

class RankingFragment : Fragment() {
    private lateinit var inflatedView: View
    private lateinit var adapter: RankingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_ranking, container, false)
        configureRecyclerView()
        loadRanking()
        return this.inflatedView
    }

    fun configureRecyclerView() {
        this.inflatedView.rankingList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    fun loadRanking() {
        RankingDAO().fetch(
            {
                response ->
                this.adapter = RankingAdapter(response.data.ranking.toMutableList())
                this.inflatedView.rankingList.adapter = this.adapter

            },
            {
                error ->
            }
        )
    }
}