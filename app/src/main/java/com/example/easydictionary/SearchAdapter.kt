package com.example.easydictionary

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
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
    private lateinit var addBtn: FrameLayout
    var counter: Int = 0
    private lateinit var mContext: Context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        inflater = LayoutInflater.from(context)
        view= inflater.inflate(R.layout.search_result, null)
        this.mContext = context

        num = view.findViewById(R.id.tvNumber)
        kanji= view.findViewById(R.id.tvKanji)
        hiragana= view.findViewById(R.id.tvHiragana)
        romaji= view.findViewById(R.id.tvRomaji)
        definition= view.findViewById(R.id.tvDef)
        addBtn = view.findViewById(R.id.btnAdd)

        num.text =  (position + 1).toString() +"."
        kanji.text = arrayList[position].name
        hiragana.text = arrayList[position].hiragana
        romaji.text = arrayList[position].romaji
        definition.text = arrayList[position].definition

        if (position == 0){
            counter = 0
            println("counter set to 0")
        }

        addBtn.setTag(counter)
        counter += 1

        addBtn.setOnClickListener { v ->
            val pos = v.tag as Int
            if (mContext is SearchActivity) {
                (mContext as SearchActivity).testfun(pos)
            }
        }
        return view
    }




}