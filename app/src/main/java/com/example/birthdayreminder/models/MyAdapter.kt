package com.example.birthdayreminder.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birthdayreminder.R

class MyAdapter(
    private val items: List<Person>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_person_card, viewGroup, false)
        return MyViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewName.text = items[position].name
        viewHolder.textViewCountdown.text = "Days left: ${items[position].countdown}"
        viewHolder.textViewBirthday.text = "${items[position].birthYear}/${items[position].birthMonth}/${items[position].birthDayOfMonth}"
    }

    class MyViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewBirthday: TextView = itemView.findViewById(R.id.birth_day)
        val textViewName: TextView = itemView.findViewById(R.id.name_of_person)
        val textViewCountdown: TextView = itemView.findViewById(R.id.days_until)
        //Her skal du lave binding til elementerne fra card.xml filen og så i onbin
        //onbindviewholder skal du tage elementerne og fylde dem ud med elementerne fra PersonLiveData

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            // gradle     implementation "androidx.recyclerview:recyclerview:1.2.1"
            onItemClicked(position)
        }
    }
}