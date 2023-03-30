package com.example.easydictionary

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyAdapter(private val context: Activity, private val arrayList: ArrayList<Word>): ArrayAdapter<Word>(context,
                R.layout.word_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.word_item, null)

        val num: TextView = view.findViewById(R.id.tvNumber)
        val word: TextView = view.findViewById(R.id.tvWord)
        val definition: TextView = view.findViewById(R.id.tvDef)

        num.text = (position + 1).toString() +"."
        word.text = arrayList[position].name
        definition.text = arrayList[position].definition


        return view
    }
}