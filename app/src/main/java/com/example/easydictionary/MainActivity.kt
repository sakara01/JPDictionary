package com.example.easydictionary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var addBtn: ImageButton
    private lateinit var myListView: ListView
    private lateinit var clickedList: String
    private var mapOfAllLists= mutableMapOf<String, ArrayList<Word>>()
    private lateinit var arrayOfListInfo: ArrayList<ListData>
    private lateinit var theme: String
    private lateinit var profile: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        theme = intent.extras?.getString("theme").toString()
        if (theme == "null"){theme ="Light" }

        println("theme is : "+theme)
        if (theme == "Dark"){setTheme(R.style.Dark) }
        else {setTheme(R.style.Light)}

        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBtn = findViewById(R.id.btnAdd)
        myListView= findViewById(R.id.lvListOfLists)
        profile = findViewById(R.id.profile)

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

        mapOfAllLists[listOfLists[0]] = createData(words1,hiragana1,romaji1,definitions1)
        mapOfAllLists[listOfLists[1]] = createData(words2,hiragana2,romaji2,definitions2)
        mapOfAllLists[listOfLists[2]] = createData(words3,hiragana3,romaji3,definitions3)

        arrayOfListInfo = ArrayList()

        for(i in listOfLists.indices){
            val unit = ListData(listOfLists[i], mapOfAllLists[listOfLists[i]]!!.size)
            arrayOfListInfo.add(unit)
        }

        checkFile()

        println(mapOfAllLists)
        println(arrayOfListInfo)

        val adapterMiddle = ListAdapter(this,arrayOfListInfo)
        binding.lvListOfLists.adapter = adapterMiddle

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var myResult = result.data!!.extras?.getString("result")
                updateList(myResult)
                adapterMiddle.notifyDataSetChanged()
            }
        }

        myListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
            val nameOfSelected = view.findViewById<TextView>(R.id.tvListName)
            clickedList = nameOfSelected.text.toString()

            var gson = Gson()
            val intent = Intent(this,ListActivity::class.java)

            val json: String = gson.toJson(mapOfAllLists[nameOfSelected.text])
            intent.putExtra("mainList", json)
            intent.putExtra("theme",theme)

            startForResult.launch(intent)
            Animatoo.animateSlideLeft(this)
        }

        addBtn.setOnClickListener{
            val unit = ListData("new list", 0)
            arrayOfListInfo.add(unit)
            mapOfAllLists["new list"] = ArrayList<Word>()
            adapterMiddle.notifyDataSetChanged()
        }

        profile.setOnClickListener{
            println("profile clicked")
            val intent = Intent(this, MainActivity::class.java)
            if (theme == "Light"){
                (intent.putExtra("theme", "Dark"))
            } else {
                (intent.putExtra("theme", "Light"))
            }
            startActivity(intent)
            finish()
        }
    }

    private fun updateList(myResult: String?){
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        var wordArray: ArrayList<Word> = gson.fromJson(myResult, newArray)
        mapOfAllLists[clickedList] = wordArray
        for (i in 0 until arrayOfListInfo.size) {
            if (arrayOfListInfo[i].listName == clickedList){
                arrayOfListInfo[i].numWords = mapOfAllLists[clickedList]!!.size
            }
        }
    }

    private fun createData(
        words: Array<String>,
        hiraganas: Array<String>,
        romajis: Array<String>,
        definitions: Array<String>
    ) : ArrayList<Word>{
        var wordList= ArrayList<Word>()
        for(i in 0 until words.size){
            val unit = Word(words[i], hiraganas[i], romajis[i], definitions[i])
            wordList.add(unit)
        }
        return wordList
    }

    private fun checkFile(){
        val fileName = "data.txt"
        val file = File(this@MainActivity.filesDir, "data.txt")
        if (file.exists()) {
            println("file exists")
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(fileName)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            println("read data is: " + (stringBuilder.toString()))
        }
        else {
            var mapStr = "{Verbs=[Word(name=寝る, hiragana=ねる, romaji=neru, definition=To sleep), Word(name=走る, hiragana=はしる, romaji=hashiru, definition=To run), Word(name=食べる, hiragana=たべる, romaji=taberu, definition=To eat)], Nouns=[Word(name=家族, hiragana=かぞく, romaji=kazoku, definition=Family), Word(name=世界, hiragana=せかい, romaji=sekai, definition=World), Word(name=お店, hiragana=おみせ, romaji=omise, definition=Shop)], Adjectives=[Word(name=狭い, hiragana=せまい, romaji=semai, definition=Narrow), Word(name=少ない, hiragana=すくない, romaji=sukunai, definition=Few), Word(name=高い, hiragana=たかい, romaji=takai, definition=Tall)]}"
            val fileOutputStream: FileOutputStream
            try {
                fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                fileOutputStream.write(mapStr.toByteArray())
                println("file Written")
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

}

//icon credits to pngegg, icons8
//cats profile pics credit to  @cadmium_red on Freepik
//books icon credit to iconsDB