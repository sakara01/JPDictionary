package com.example.easydictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.widget.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class MainActivity : AppCompatActivity() {
    private lateinit var addBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Dark)
        setContentView(R.layout.activity_main)

        addBtn = findViewById(R.id.btnAdd)

        val words1 = arrayOf("寝る", "走る","食べる")
        val definitions1 = arrayOf("To sleep from main", "To run from main", "To eat from main")

        val words2 = arrayOf("寝る", "走る","食べる")
        val definitions2 = arrayOf("Random Noun", "A noun ig", "These are nouns")

        val words3 = arrayOf("寝る", "走る","食べる")
        val definitions3 = arrayOf("An adjective", "Pretend :)", "New adjective")

        data class Value(val word: Array<String>, val def: Array<String>) {}

        val key1 = "Verbs"
        val wordndef1 = Value(words1,definitions1)
        val key2 = "Nouns"
        val wordndef2 = Value(words2,definitions2)
        val key3 = "Adjectives"
        val wordndef3 = Value(words3,definitions3)

        val mapOfAllLists = mutableMapOf<String, Value>()
        //key is name of list, and wordndef holds arrays of both words and defs
        mapOfAllLists.put(key1,wordndef1)
        mapOfAllLists.put(key2,wordndef2)
        mapOfAllLists.put(key3,wordndef3)

        println(mapOfAllLists)

        val listOfLists = mutableListOf<String>("Verbs","Nouns","Adjectives")
        val myListView= findViewById<ListView>(R.id.lvListOfLists)
        val adapter = ArrayAdapter<String>(this, R.layout.list_item,R.id.tvListName, listOfLists)
        myListView.adapter= adapter
        myListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
            val nameOfSelected = view.findViewById<TextView>(R.id.tvListName)
            val intent = Intent(this,ListActivity::class.java)
            intent.putExtra("wordList",mapOfAllLists[nameOfSelected.text]!!.word)
            intent.putExtra("defList",mapOfAllLists[nameOfSelected.text]!!.def)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        addBtn.setOnClickListener{
            listOfLists.add("new list")
            adapter.notifyDataSetChanged()
        }
    }
}