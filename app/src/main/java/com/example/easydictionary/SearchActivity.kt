package com.example.easydictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityListBinding
import com.example.easydictionary.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var wordList: ArrayList<Word>
    private lateinit var btnBack: ImageButton
    private lateinit var btnClose: ImageButton
    private lateinit var etInput: EditText


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
        val words = arrayOf("先生", "教員","教師")
        val hiraganas = arrayOf("せんせい", "きょういん","きょうし")
        val romajis = arrayOf("sensei","kyouin", "kyoushi")
        val definitions = arrayOf("teacher, instructor, mentor", "teacher,instructor,teaching staff", "teacher (classroom)")

        for(i in words!!.indices){
            val unit = Word(words!![i], hiraganas!![i], romajis!![i], definitions!![i])
            wordList.add(unit)
        }
        var adapterMiddle = SearchAdapter(this,wordList)
        binding.lvResultsList.adapter = adapterMiddle

        btnBack.setOnClickListener{
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnClose.setOnClickListener{
            btnClose.startAnimation(buttonClick)
            etInput.text.clear()
            etInput.hint = "せんせい, sensei,teacher"
        }

    }
}