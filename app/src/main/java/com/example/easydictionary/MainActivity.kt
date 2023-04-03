package com.example.easydictionary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var addBtn: ImageButton
    private lateinit var myListView: ListView
    private lateinit var clickedList: String
    private var mapOfAllLists= mutableMapOf<String, ArrayList<Word>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBtn = findViewById(R.id.btnAdd)
        myListView= findViewById(R.id.lvListOfLists)

        val listOfLists = mutableListOf("Verbs","Nouns","Adjectives")

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

        var wordlist1 = createData(words1,hiragana1,romaji1,definitions1)
        var wordlist2 = createData(words2,hiragana2,romaji2,definitions2)
        var wordlist3 = createData(words3,hiragana3,romaji3,definitions3)

        mapOfAllLists[listOfLists[0]] = wordlist1
        mapOfAllLists[listOfLists[1]] = wordlist2
        mapOfAllLists[listOfLists[2]] = wordlist3

        val numWords1 = words1.size
        val numWords2 = words2.size
        val numWords3 = words3.size
        val numWords = arrayOf(numWords1,numWords2, numWords3)

        val arrayOfListInfo = ArrayList<ListData>()

        for(i in listOfLists.indices){
            val unit = ListData(listOfLists[i], numWords[i])
            arrayOfListInfo.add(unit)
        }

        val adapterMiddle = ListAdapter(this,arrayOfListInfo)
        binding.lvListOfLists.adapter = adapterMiddle

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var myResult = result.data!!.extras?.getString("result")
                updateList(myResult)
            }
        }

        myListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
            val nameOfSelected = view.findViewById<TextView>(R.id.tvListName)
            clickedList = nameOfSelected.text.toString()

            var gson = Gson()
            val intent = Intent(this,ListActivity::class.java)
            if (nameOfSelected.text == "new list"){
                val rando = ArrayList<Word>()
                val json: String = gson.toJson(rando)
                intent.putExtra("mainList", json)
            }
            else {
                val json: String = gson.toJson(mapOfAllLists[nameOfSelected.text])
                intent.putExtra("mainList", json)
            }
            startForResult.launch(intent)
            Animatoo.animateSlideLeft(this)
        }

        addBtn.setOnClickListener{
            val unit = ListData("new list", 0)
            arrayOfListInfo.add(unit)
            adapterMiddle.notifyDataSetChanged()
        }
    }

    private fun updateList(myResult: String?){
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        var wordArray: ArrayList<Word> = gson.fromJson(myResult, newArray)

        mapOfAllLists[clickedList] = wordArray
    }

    private fun createData(words: Array<String>, hiraganas: Array<String>,romajis: Array<String>,definitions: Array<String>,) : ArrayList<Word>{
        var wordList= ArrayList<Word>()
        for(i in 0 until words.size){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }
        return wordList
    }

}

//icon credits to pngegg, icons8
//cats profile pics credit to  @cadmium_red on Freepik
//books icon credit to iconsDB