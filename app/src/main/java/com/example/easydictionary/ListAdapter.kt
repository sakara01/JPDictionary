package com.example.easydictionary

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter (private val context: Activity, private val arrayList: ArrayList<ListData>): ArrayAdapter<ListData>(context,
    R.layout.list_item, arrayList) {

    private lateinit var name: TextView
    private lateinit var num: TextView
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        inflater = LayoutInflater.from(context)
        view= inflater.inflate(R.layout.list_item, null)

        name = view.findViewById(R.id.tvListName)
        num= view.findViewById(R.id.tvNumWords)

        name.text = arrayList[position].listName
        num.text = arrayList[position].numWords.toString() + " words"

        return view
    }
    }