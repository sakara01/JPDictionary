package com.example.easydictionary

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView

class MyAdapter(private val context: Activity, private val arrayList: ArrayList<Word>): ArrayAdapter<Word>(context,
                R.layout.word_item, arrayList) {

    private lateinit var num: TextView
    private lateinit var word: TextView
    private lateinit var hiragana: TextView
    private lateinit var definition: TextView
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var tvOpen: TextView
    private lateinit var tvClose: TextView
    private lateinit var speakBtn: FrameLayout
    private lateinit var mContext: Context


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        inflater = LayoutInflater.from(context)
        view= inflater.inflate(R.layout.word_item, null)
        this.mContext = context

        num = view.findViewById(R.id.tvNumber)
        word= view.findViewById(R.id.tvWord)
        hiragana= view.findViewById(R.id.tvHiragana)
        definition= view.findViewById(R.id.tvDef)
        tvOpen = view.findViewById(R.id.tvOpen)
        tvClose = view.findViewById(R.id.tvClose)
        speakBtn = view.findViewById(R.id.btnSpeak)

        num.text =  (position + 1).toString() +"."
        word.text = arrayList[position].name
        hiragana.text = arrayList[position].hiragana
        definition.text = arrayList[position].definition

        if (word.text == ""){
            tvOpen.text = ""
            tvClose.text = ""
        }

        speakBtn.setOnClickListener { _ ->
            if (mContext is ListActivity) {
                (mContext as ListActivity).speak(position)
            }
        }

        return view
    }

}