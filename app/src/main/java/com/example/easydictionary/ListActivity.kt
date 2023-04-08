package com.example.easydictionary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private var wordList= ArrayList<Word>()
    private var copyList= ArrayList<Word>()
    private lateinit var backBtn : ImageButton
    private lateinit var btnHideWord : ImageView
    private lateinit var btnHideDef : ImageView
    private lateinit var btnAdd : ImageButton
    private lateinit var listName: EditText
    private var hideWordClicked: Boolean = false
    private var hideDefClicked: Boolean = false
    private lateinit var constraint1: ConstraintLayout
    private lateinit var constraint2: ConstraintLayout
    private lateinit var constraint3: ConstraintLayout
    private var mainListStr: String? = null
    private lateinit var theme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        theme = intent.extras?.getString("theme").toString()
        setTheme(if (theme == "Dark") R.style.Dark else R.style.Light)

        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listName = findViewById(R.id.etListName)
        var name = intent.extras?.getString("name")
        if (name != "new list")listName.setText(name)

        mainListStr = intent.extras?.getString("mainList")
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        if (mainListStr != "null"){
            wordList = gson.fromJson(mainListStr, newArray)
            copyList = gson.fromJson(mainListStr, newArray)
        }


        backBtn = findViewById(R.id.btnBack)
        btnHideWord = findViewById(R.id.btnHideWord)
        btnHideDef = findViewById(R.id.btnHideDef)
        btnAdd= findViewById(R.id.btnAdd)
        constraint1 = findViewById(R.id.constraint1)
        constraint2 = findViewById(R.id.constraint2)
        constraint3 = findViewById(R.id.constraint3)

        val buttonClick = AlphaAnimation(1f, 0.7f)
        buttonClick.duration = 200
        buttonClick.repeatMode = 2
        buttonClick.repeatCount = 1

        val adapterMiddle = MyAdapter(this,wordList)
        binding.lvWordList.adapter = adapterMiddle

        backBtn.setOnClickListener{
            var entered = listName.text.toString()
            if (entered.isNullOrEmpty() && wordList.isNotEmpty() ){
                Toast.makeText(this, "You must name the list to save changes",Toast.LENGTH_SHORT).show()
            }else {
                val gson = Gson()
                showAllWords()
                showAllDefs()
                val json: String = gson.toJson(wordList)
                val intent = Intent().apply { putExtra("result", json); putExtra("name",entered)}
                setResult(RESULT_OK, intent)
                finish()
                Animatoo.animateSlideRight(this)
            }
        }

        btnHideWord.setOnClickListener{
                val rule = findViewById<ImageView>(R.id.JPrule)
                hideWordClicked = !hideWordClicked
                rule.apply { translationZ = if (hideWordClicked)30F else 0F }
                if (hideWordClicked) hideAllWords() else showAllWords()
                adapterMiddle.notifyDataSetChanged()
        }

        btnHideDef.setOnClickListener{
                val rule = findViewById<ImageView>(R.id.ENrule)
                hideDefClicked = !hideDefClicked
                rule.apply { translationZ = if (hideDefClicked)30F else 0F }
                if (hideDefClicked) hideAllDefs() else showAllDefs()
                adapterMiddle.notifyDataSetChanged()
        }

        //gets result when Search activity finishes
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
            val intent = Intent(this,SearchActivity::class.java)
            intent.putExtra("theme", if (theme == "Dark") "Dark" else "Light")
            startForResult.launch(intent)
            Animatoo.animateSlideLeft(this)
        }

        setOnClickListenersForConstraints(R.drawable.border, constraint1, constraint2, constraint3)
    }

    override fun onBackPressed() {
        val gson = Gson()
        showAllWords()
        showAllDefs()
        val json: String = gson.toJson(wordList)
        val intent = Intent().apply { putExtra("result", json)}
        setResult(RESULT_OK, intent)
        finish()
        Animatoo.animateSlideRight(this)
    }


    private fun hideAllWords(){
        wordList.forEach{
            it.name = ""
            it.hiragana = ""
        }


    }
    private fun showAllWords(){
        wordList.forEachIndexed { i, word ->
            word.name= copyList[i].name
            word.hiragana= copyList[i].hiragana
        }
    }
    private fun hideAllDefs(){
        wordList.forEach{
            it.definition = ""
        }
    }

    private fun showAllDefs(){
        wordList.forEachIndexed { i, word ->
            word.definition = copyList[i].definition
        }
    }

    private fun updateList(myResult:String?){
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        var wordArray: ArrayList<Word> = gson.fromJson(myResult, newArray)
        var newWord: Word
        for (i in 0 until wordArray.size) {
            newWord = wordArray[i]
            wordList.add(newWord)
            copyList.add(newWord.copy())
        }
    }

    fun setOnClickListenersForConstraints(
        borderDrawable: Int,
        vararg constraints: ConstraintLayout
    ) {
        constraints.forEach { constraint ->
            constraint.setOnClickListener {
                constraints.forEach { c ->
                    c.setBackgroundResource(if (c == constraint) borderDrawable else 0)
                }
            }
        }
    }

}