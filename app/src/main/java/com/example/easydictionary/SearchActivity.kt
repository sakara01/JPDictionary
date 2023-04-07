package com.example.easydictionary

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivitySearchBinding
import com.google.gson.Gson
import dev.esnault.wanakana.core.Wanakana
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var wordList: ArrayList<Word>
    private lateinit var btnBack: ImageButton
    private lateinit var btnClose: FrameLayout
    private lateinit var etInput: EditText
    private lateinit var words: MutableList<String>
    private lateinit var hiraganas: MutableList<String>
    private lateinit var romajis: MutableList<String>
    private lateinit var definitions: MutableList<String>
    private lateinit var adapterMiddle: SearchAdapter
    private var timer: Timer = Timer()
    private val DELAY: Long = 700 // Milliseconds
    private lateinit var lvResultsList: ListView
    private lateinit var addedWord: Word
    private lateinit var addedList: ArrayList<Word>
    private lateinit var theme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        theme = intent.extras?.getString("theme").toString()
        setTheme(if (theme == "Dark") R.style.Dark else R.style.Light)

        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonClick = AlphaAnimation(1f, 0.3f)
        buttonClick.duration = 100
        buttonClick.repeatMode = 2
        buttonClick.repeatCount = 1

        //used to display list on search
        wordList = ArrayList()
        //used to send data of selected words to list activity
        addedList = ArrayList()

        btnBack = findViewById(R.id.btnBack)
        btnClose = findViewById(R.id.btnClose)
        etInput = findViewById(R.id.etInput)
        lvResultsList = findViewById(R.id.lvResultsList)

        //send request to jisho and use loop to add to these arrays
        words = mutableListOf()
        hiraganas = mutableListOf()
        romajis = mutableListOf()
        definitions = mutableListOf()

        for(i in words.indices){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }
        adapterMiddle = SearchAdapter(this,wordList)
        binding.lvResultsList.adapter = adapterMiddle

        btnBack.setOnClickListener{
            val gson = Gson()
            val json: String = gson.toJson(addedList)
            val intent = Intent().apply{ putExtra("result", json) }
            setResult(RESULT_OK, intent)
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnClose.setOnClickListener{
            btnClose.startAnimation(buttonClick)
            etInput.text.clear()
            etInput.hint = "せんせい, sensei, teacher"
        }

        etInput.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            var mythread = Thread {
                                sendReq(etInput.text.toString())
                            }
                            if (mythread.isAlive()){
                                mythread.run()
                            }
                            else {
                                mythread.start()
                            }
                        }
                    },
                    DELAY
                )
            }
        })

        lvResultsList.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "search result clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val gson = Gson()
        val json: String = gson.toJson(addedList)
        val intent = Intent().apply{putExtra("result", json) }
        setResult(RESULT_OK, intent)
        finish()
        Animatoo.animateSlideRight(this)
    }


    private fun parseJson(info: String){
        clearAll()

        var jsonArray: JSONArray = JSONObject(info).get("data") as JSONArray
        for (i in 0 until jsonArray.length()){
            var jsonObject = jsonArray.getJSONObject(i)
            parseEach(jsonObject)
        }
        for(i in words.indices){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }

    }

    private fun parseEach(jsonObject: JSONObject?) {
        //get kanji and hiragana
        var jpobject: JSONObject = if (jsonObject!!.has("japanese")) (jsonObject.get("japanese") as JSONArray).getJSONObject(0) else ("none" as JSONObject)
        var kanji = if (jpobject.has("word")) jpobject.get("word") else "none"
        var hiragana = if (jpobject.has("reading")) jpobject.get("reading") else "none"
        words.add(kanji.toString())
        hiraganas.add(hiragana.toString())
        romajis.add(Wanakana.toRomaji(hiragana.toString()))
        //get english definitions
        var enobject: JSONArray= ((jsonObject.get("senses") as JSONArray).getJSONObject(0).get("english_definitions")) as JSONArray
        var separator = ", "
        var endefs = enobject.join(separator)
        endefs = endefs.replace("\"","" )
        definitions.add(endefs)
    }

    private fun clearAll(){
        words.clear()
        hiraganas.clear()
        romajis.clear()
        definitions.clear()
        wordList.clear()
    }

    private fun sendReq(raw:String){
        if (raw.length >1) {
            val jishoReq = resources.getString(
                R.string.jisho_request, raw
            )
            println(jishoReq)
            var info = URL(jishoReq).readText()
            parseJson(info)
            runOnUiThread {
                adapterMiddle.notifyDataSetChanged()
            }
        }

    }

    //add each word user clicks to the addedList to be sent to List activity
    fun add(pos: Int){
        addedWord = wordList[pos]
        addedList.add(addedWord)
    }

}