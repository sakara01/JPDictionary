package com.example.easydictionary

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SearchAdapter(private val context: Activity, private val arrayList: ArrayList<Word>): ArrayAdapter<Word>(context,
    R.layout.search_result, arrayList) {

    private lateinit var num: TextView
    private lateinit var kanji: TextView
    private lateinit var hiragana: TextView
    private lateinit var romaji: TextView
    private lateinit var definition: TextView
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        inflater = LayoutInflater.from(context)
        view= inflater.inflate(R.layout.search_result, null)

        num = view.findViewById(R.id.tvNumber)
        kanji= view.findViewById(R.id.tvKanji)
        hiragana= view.findViewById(R.id.tvHiragana)
        romaji= view.findViewById(R.id.tvRomaji)
        definition= view.findViewById(R.id.tvDef)

        num.text =  (position + 1).toString() +"."
        kanji.text = arrayList[position].name
        hiragana.text = arrayList[position].hiragana
        romaji.text = arrayList[position].romaji
        definition.text = arrayList[position].definition

        return view
    }

}