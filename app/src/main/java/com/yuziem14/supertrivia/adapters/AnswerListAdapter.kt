package com.yuziem14.supertrivia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuziem14.supertrivia.R
import com.yuziem14.supertrivia.models.Answer
import com.yuziem14.supertrivia.parsers.HTMLParser
import kotlinx.android.synthetic.main.answer_item.view.*

class AnswerListAdapter(private var answers: MutableList<Answer> = mutableListOf()) :
    RecyclerView.Adapter<AnswerListAdapter.ViewHolder>() {
    private var selectedAnswer: Answer? = null
    private var selectedOptionView: View? = null

    fun getSelectedAnswer() = this.selectedAnswer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.answer_item, parent, false)
        )

    override fun getItemCount(): Int = this.answers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = this.answers[position]
        holder.fillView(answer)
    }

    fun update(answers: MutableList<Answer>?) {
        if(answers == null) return

        this.answers.clear()
        this.answers.addAll(answers)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun fillView(answer: Answer) {
            itemView.rbAnswerOption.text = HTMLParser.parse(answer.description)
            itemView.rbAnswerOption.setOnClickListener {
                if(selectedOptionView != null) {
                    selectedOptionView!!.rbAnswerOption.isChecked = false
                }

                selectedOptionView = itemView
                selectedAnswer = answer
            }
        }
    }
}