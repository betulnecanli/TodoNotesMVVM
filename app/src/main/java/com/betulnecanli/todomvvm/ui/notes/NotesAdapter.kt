package com.betulnecanli.todomvvm.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.todomvvm.data.Notes
import com.betulnecanli.todomvvm.databinding.ItemNoteBinding
import java.time.format.DateTimeFormatter

class NotesAdapter(
    private val listener : OnItemClickListener
): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(){

    private var differCallback =
        object : DiffUtil.ItemCallback<Notes>(){
            override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this,differCallback)

    inner class NotesViewHolder(private val binding: ItemNoteBinding):RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val note = differ.currentList[position]
                        listener.onItemClick(note)

                    }
                }

                deleteNoteButton.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val note = differ.currentList[position]
                        listener.onDeleteNoteClick(note)
                    }
                }

            }
        }
        fun bind(note : Notes){
            binding.apply {
                titleTextView.text = note.title
                contentTextView.text = note.content
                dateTextView.text = note.createdDateFormatted

            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.bind(currentNote)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickListener{
        fun onItemClick(note : Notes)
        fun onDeleteNoteClick(note: Notes)


    }
}