package com.yuziem14.supertrivia.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.models.RankingPosition
import kotlinx.android.synthetic.main.player_item.view.*

class RankingAdapter(private var rankingList: MutableList<RankingPosition> = mutableListOf()) :
    RecyclerView.Adapter<RankingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.player_item, parent, false)
        )

    override fun getItemCount(): Int = this.rankingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rankingPosition = this.rankingList[position]
        holder.fillView(rankingPosition)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun fillView(rankingPosition: RankingPosition) {
            itemView.apply {
                val scoreColor = if(rankingPosition.score < 0L) "#D11818" else "#1abc9c"
                userText.text = rankingPosition.user
                userScoreText.text = rankingPosition.score.toString()
                userScoreText.setTextColor(Color.parseColor(scoreColor))
            }
        }
    }
}
