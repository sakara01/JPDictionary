package com.example.easydictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.widget.*
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class MainActivity : AppCompatActivity() {
    private lateinit var addBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        setContentView(R.layout.activity_main)

        addBtn = findViewById(R.id.btnAdd)

        val words1 = arrayOf("寝る", "走る","食べる")
        val hiragana1 = arrayOf("ねる", "はしる","たべる")
        val romaji1 = arrayOf("neru","hashiru", "taberu")
        val definitions1 = arrayOf("To sleep", "To run", "To eat")

        val words2 = arrayOf("家族", "世界","お店")
        val hiragana2 = arrayOf("かぞく", "せかい","おみせ")
        val romaji2 = arrayOf("kazoku","sekai", "omise")
        val definitions2 = arrayOf("Family", "World", "Shop")

        val words3 = arrayOf("狭い", "少ない","高い")
        val hiragana3 = arrayOf("せまい", "すくない","たかい")
        val romaji3 = arrayOf("semai","sukunai", "takai")
        val definitions3 = arrayOf("Narrow", "Few", "Tall")

        data class Value(val words: Array<String>,val hiraganas: Array<String>,val romajis: Array<String>, val defs: Array<String>) {}

        val key1 = "Verbs"
        val wordndef1 = Value(words1,hiragana1,romaji1, definitions1)
        val key2 = "Nouns"
        val wordndef2 = Value(words2,hiragana2,romaji2, definitions2)
        val key3 = "Adjectives"
        val wordndef3 = Value(words3,hiragana3,romaji3, definitions3)

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
            if (nameOfSelected.text == "new list"){
                val rando = arrayOf<String>()
                intent.putExtra("wordList", rando)
                intent.putExtra("hiraganaList", rando)
                intent.putExtra("romajiList", rando)
                intent.putExtra("defList", rando)
            }
            else {
                intent.putExtra("wordList", mapOfAllLists[nameOfSelected.text]!!.words)
                intent.putExtra("hiraganaList", mapOfAllLists[nameOfSelected.text]!!.hiraganas)
                intent.putExtra("romajiList", mapOfAllLists[nameOfSelected.text]!!.romajis)
                intent.putExtra("defList", mapOfAllLists[nameOfSelected.text]!!.defs)
            }
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        addBtn.setOnClickListener{
            listOfLists.add("new list")
            adapter.notifyDataSetChanged()
        }
    }
}

//icon credits to pngegg, icons8