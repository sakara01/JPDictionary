package com.example.easydictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var wordList: ArrayList<Word>
    private lateinit var backBtn : ImageButton
    private lateinit var listName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val words = intent.extras?.getStringArray("wordList")
        val definitions = intent.extras?.getStringArray("defList")

        backBtn = findViewById(R.id.btnBack)
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

    }
}