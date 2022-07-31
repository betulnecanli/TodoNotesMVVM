package com.betulnecanli.todomvvm.ui.todo.tasks


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.todomvvm.data.Task
import com.betulnecanli.todomvvm.databinding.ItemTaskBinding

class TaskAdapter(
    private val listener : OnItemClickListener
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallBack()) {



   inner class TaskViewHolder(private val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        init{
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task  = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                taskCheckBox.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onChecBoxClick(task,taskCheckBox.isChecked)
                    }
                }
            }
        }

        fun bind(task : Task){
            binding.apply {
                taskCheckBox.isChecked = task.completed
                taskName.text = task.name
                taskName.paint.isStrikeThruText = task.completed
                taskPriority.isVisible = task.important
            }
        }
    }


    interface OnItemClickListener{
        fun onItemClick(task : Task)
        fun onChecBoxClick(task : Task, isChecked :  Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       val currentIem = getItem(position)
        holder.bind(currentIem)
    }

    class DiffCallBack: DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                                                                                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                                                                                oldItem == newItem

    }

}