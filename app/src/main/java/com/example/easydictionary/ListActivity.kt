package com.example.easydictionary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var wordList: ArrayList<Word>
    private lateinit var backBtn : ImageButton
    private lateinit var btnHideWord : Button
    private lateinit var btnHideDef : Button
    private lateinit var btnAdd : ImageButton
    private lateinit var listName: EditText
    private var hideWordClicked: Boolean = false
    private var hideDefClicked: Boolean = false
    private lateinit var constraint1: View
    private lateinit var constraint2: View
    private lateinit var constraint3: View
    private var mainListStr: String? = null
    private var words: Array<String>? = null
    private var hiraganas: Array<String>? = null
    private var romajis: Array<String>? = null
    private var definitions: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainListStr = intent.extras?.getString("mainList")
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        wordList = gson.fromJson(mainListStr, newArray)


        backBtn = findViewById(R.id.btnBack)
        btnHideWord = findViewById(R.id.btnHideWord)
        btnHideDef = findViewById(R.id.btnHideDef)
        btnAdd= findViewById(R.id.btnAdd)
        listName = findViewById(R.id.etListName)
        constraint1 = findViewById(R.id.constraint1)
        constraint2 = findViewById(R.id.constraint2)
        constraint3 = findViewById(R.id.constraint3)

        val buttonClick = AlphaAnimation(1f, 0.7f)
        buttonClick.duration = 200
        buttonClick.repeatMode = 2
        buttonClick.repeatCount = 1

        //wordList = ArrayList()

        /*
        for(i in words!!.indices){
            val unit = Word(words!![i], hiraganas!![i], romajis!![i], definitions!![i])
            wordList.add(unit)
        }
        */

        val adapterMiddle = MyAdapter(this,wordList)
        binding.lvWordList.adapter = adapterMiddle

        backBtn.setOnClickListener{
            val intent = Intent()
            val gson = Gson()
            val json: String = gson.toJson(wordList)
            intent.putExtra("result", json) //pass intent extra here
            setResult(RESULT_OK, intent)
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnHideWord.setOnClickListener{
            if (!hideWordClicked) {
                val rule = findViewById<ImageView>(R.id.JPrule)
                rule.apply { translationZ = 30F }
                hideWordClicked = true
                hideAllWords()
                adapterMiddle.notifyDataSetChanged()
            }
            else {
                val rule = findViewById<ImageView>(R.id.JPrule)
                rule.apply { translationZ = 0F }
                hideWordClicked = false
                showAllWords()
                adapterMiddle.notifyDataSetChanged()
            }
        }

        btnHideDef.setOnClickListener{
            if (!hideDefClicked) {
                val rule = findViewById<ImageView>(R.id.ENrule)
                rule.apply { translationZ = 30F }
                hideDefClicked = true
                hideAllDefs()
                adapterMiddle.notifyDataSetChanged()
            }
            else {
                val rule = findViewById<ImageView>(R.id.ENrule)
                rule.apply { translationZ = 0F }
                hideDefClicked = false
                showAllDefs()
                adapterMiddle.notifyDataSetChanged()
            }
        }

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var myResult = result.data!!.extras?.getString("result")
                updateList(myResult)
                adapterMiddle.notifyDataSetChanged()
            }
        }

        btnAdd.setOnClickListener {
            btnAdd.startAnimation(buttonClick)
            btnAdd.startAnimation(buttonClick)
            startForResult.launch(Intent(this, SearchActivity::class.java))
            Animatoo.animateSlideLeft(this)
        }

        constraint1.setOnClickListener{
            constraint1.setBackgroundResource(R.drawable.border)
            constraint2.setBackgroundResource(0)
            constraint3.setBackgroundResource(0)
        }
        constraint2.setOnClickListener{
            constraint1.setBackgroundResource(0)
            constraint2.setBackgroundResource(R.drawable.border)
            constraint3.setBackgroundResource(0)
        }
        constraint3.setOnClickListener{
            constraint1.setBackgroundResource(0)
            constraint2.setBackgroundResource(0)
            constraint3.setBackgroundResource(R.drawable.border)
        }
    }



    private fun hideAllWords(){
        for(i in words!!.indices){
            wordList[i].name = ""
            wordList[i].hiragana = ""
        }
    }
    private fun showAllWords(){
        for(i in words!!.indices){
            wordList[i].name = words!![i]
            wordList[i].hiragana = hiraganas!![i]
        }
    }
    private fun hideAllDefs(){
        for(i in definitions!!.indices){
            wordList[i].definition = ""
        }
    }

    private fun showAllDefs(){
        for(i in definitions!!.indices){
            wordList[i].definition = definitions!![i]
        }
    }

    private fun updateList(myResult:String?){
        var gson = Gson()
        val newArray = object : TypeToken<Array<Word>>() {}.type
        var wordArray: Array<Word> = gson.fromJson(myResult, newArray)

        for (i in 0 until wordArray.size)
            wordList.add(wordArray[i])
    }



}