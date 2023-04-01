package com.example.easydictionary

import android.animation.ValueAnimator.REVERSE
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityListBinding


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

        words = intent.extras?.getStringArray("wordList")
        hiraganas = intent.extras?.getStringArray("hiraganaList")
        romajis = intent.extras?.getStringArray("romajiList")
        definitions = intent.extras?.getStringArray("defList")

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

        wordList = ArrayList()

        for(i in words!!.indices){
            val unit = Word(words!![i], hiraganas!![i], romajis!![i], definitions!![i])
            wordList.add(unit)
        }
        var adapterMiddle = MyAdapter(this,wordList)
        binding.lvWordList.adapter = adapterMiddle

        backBtn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("listName",listName.text ?: "new list")
            startActivity(intent)
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

        btnAdd.setOnClickListener {
            btnAdd.startAnimation(buttonClick)
            btnAdd.startAnimation(buttonClick)
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
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

    fun hideAllWords(){
        for(i in words!!.indices){
            wordList[i].name = ""
            wordList[i].hiragana = ""
        }
    }
    fun showAllWords(){
        for(i in words!!.indices){
            wordList[i].name = words!![i]
            wordList[i].hiragana = hiraganas!![i]
        }
    }
    fun hideAllDefs(){
        for(i in definitions!!.indices){
            wordList[i].definition = ""
        }
    }

    fun showAllDefs(){
        for(i in definitions!!.indices){
            wordList[i].definition = definitions!![i]
        }
    }
}