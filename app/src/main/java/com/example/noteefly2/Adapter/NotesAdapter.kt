package com.example.noteefly2.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteefly2.Models.Note
import com.example.noteefly2.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        NotesList.clear()

        for(item in fullList){
            if(item.category?.lowercase()?.contains(search.lowercase()) == true)
                {
                NotesList.add(item)
            }
        }


        notifyDataSetChanged()
    }

    fun RandomColor(): Int {
        var list = ArrayList<Int>()
        list.add(R.color.magenta)
        list.add(R.color.cyan)
        list.add(R.color.green)
        list.add(R.color.gray)
        list.add(R.color.red)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.category.text = currentNote.category
        holder.importanceLevel.text = currentNote.importanceLevel

        holder.notesLayout.setBackgroundColor(holder.itemView.resources.getColor(RandomColor()))

        holder.notesLayout.setOnClickListener {
            listener.onitemClicked(NotesList[holder.adapterPosition])
        }

        holder.notesLayout.setOnLongClickListener {
            listener.onLongitemClicked(NotesList[holder.adapterPosition], holder.notesLayout)
            true
        }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesLayout: CardView = itemView.findViewById(R.id.card_layout)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val note: TextView = itemView.findViewById(R.id.tv_note)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val category: TextView = itemView.findViewById(R.id.tv_category)
        val importanceLevel: TextView = itemView.findViewById(R.id.tv_importance_level)
    }

    interface NotesClickListener {
        fun onitemClicked(note: Note)
        fun onLongitemClicked(note: Note, cardView: CardView)
    }
}
