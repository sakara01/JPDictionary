package com.example.easydictionary

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Dark)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val words = intent.extras?.getStringArray("wordList")
        val definitions = intent.extras?.getStringArray("defList")

        backBtn = findViewById(R.id.btnBack)
        btnHideWord = findViewById(R.id.btnHideWord)
        btnHideDef = findViewById(R.id.btnHideDef)
        btnAdd= findViewById(R.id.btnAdd)
        listName = findViewById(R.id.etListName)

        wordList = ArrayList()

        for(i in words!!.indices){
            val word = Word(words!![i], definitions!![i])
            wordList.add(word)
        }
        binding.lvWordList.adapter = MyAdapter(this,wordList)

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
            }
            else {
                val rule = findViewById<ImageView>(R.id.JPrule)
                rule.apply { translationZ = 0F }
                hideWordClicked = false
            }
        }

        btnHideDef.setOnClickListener{
            if (!hideDefClicked) {
                val rule = findViewById<ImageView>(R.id.ENrule)
                rule.apply { translationZ = 30F }
                hideDefClicked = true
            }
            else {
                val rule = findViewById<ImageView>(R.id.ENrule)
                rule.apply { translationZ = 0F }
                hideDefClicked = false
            }
        }

    }
}