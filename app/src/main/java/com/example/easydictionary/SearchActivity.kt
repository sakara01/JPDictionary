package com.example.easydictionary

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivitySearchBinding
import com.jakewharton.rxbinding.widget.RxTextView
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.TimeUnit


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonClick = AlphaAnimation(1f, 0.3f)
        buttonClick.duration = 100
        buttonClick.repeatMode = 2
        buttonClick.repeatCount = 1

        wordList = ArrayList()

        btnBack = findViewById(R.id.btnBack)
        btnClose = findViewById(R.id.btnClose)
        etInput = findViewById(R.id.etInput)

        //send request to jisho and use loop to add to these arrays
        words = mutableListOf()
        hiraganas = mutableListOf()
        romajis = mutableListOf()
        definitions = mutableListOf()

        for(i in words.indices){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }
        val adapterMiddle = SearchAdapter(this,wordList)
        binding.lvResultsList.adapter = adapterMiddle

        btnBack.setOnClickListener{
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnClose.setOnClickListener{
            btnClose.startAnimation(buttonClick)
            etInput.text.clear()
            etInput.hint = "せんせい, sensei, teacher"
        }

        RxTextView.textChanges(etInput)
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { textChanged: CharSequence? ->
                //make request to API here
                var info = URL("https://jisho.org/api/v1/search/words?keyword=water").readText()
                parseJson(info)
                runOnUiThread {
                    // Stuff that updates the UI
                    adapterMiddle.notifyDataSetChanged()
                }
            }
    }

    private fun parseJson(info: String){
        var jsonArray: JSONArray = JSONObject(info).get("data") as JSONArray
        for (i in 0 until jsonArray.length()){
            var jsonObject = jsonArray.getJSONObject(i)
            parseEach(jsonObject)
        }
        wordList.clear()
        for(i in words.indices){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }
    }

    private fun parseEach(jsonObject: JSONObject?) {
        //get kanji and hiragana
        var jpobject: JSONObject = if (jsonObject!!.has("japanese")) (jsonObject?.get("japanese") as JSONArray).getJSONObject(0) else ("none" as JSONObject)
        var kanji = if (jpobject.has("word")) jpobject.get("word") else "none"
        var hiragana = if (jpobject.has("reading")) jpobject.get("reading") else "none"
        words.add(kanji.toString())
        hiraganas.add(hiragana.toString())
        romajis.add("lol")
        //get english definitions
        var enobject: JSONArray= ((jsonObject?.get("senses") as JSONArray).getJSONObject(0).get("english_definitions")) as JSONArray
        var separator: String = ", "
        var endefs = enobject.join(separator)
        endefs = endefs.replace("\"","" )
        definitions.add(endefs)
    }

}