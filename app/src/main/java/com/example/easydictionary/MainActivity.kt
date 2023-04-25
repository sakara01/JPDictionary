package com.example.easydictionary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.easydictionary.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var addBtn: ImageButton
    private lateinit var myListView: ListView
    private lateinit var clickedList: String
    private var mapOfAllLists= LinkedHashMap<String, ArrayList<Word>>()
    var arrayOfListInfo= ArrayList<ListData>()
    var arrayOfListInfoCopy= ArrayList<ListData>()
    private lateinit var theme: String
    private lateinit var profile: FrameLayout
    private val fileName = "data.txt"
    private lateinit var searchBar: SearchView
    private lateinit var mainparent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        theme = intent.extras?.getString("theme").toString()
        if (theme == "null"){theme ="Light" }
        setTheme(if (theme == "Dark") R.style.Dark else R.style.Light)
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMap()

        addBtn = findViewById(R.id.btnAdd)
        myListView= findViewById(R.id.lvListOfLists)
        profile = findViewById(R.id.profile)
        searchBar = findViewById(R.id.searchBar)
        mainparent = findViewById(R.id.mainparent)

        var adapterMiddle = MainAdapter(this,arrayOfListInfo)
        binding.lvListOfLists.adapter = adapterMiddle

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var myResult = result.data!!.extras?.getString("result")
                var listname = result.data!!.extras?.getString("name")
                if (!listname.isNullOrEmpty()){
                    updateList(myResult, listname)
                    stringifyMap()
                    println("array in startforResutl:")
                    println(arrayOfListInfo)
                    adapterMiddle.clear()
                    adapterMiddle.addAll(ArrayList(arrayOfListInfo))
                    adapterMiddle.notifyDataSetChanged()
                    adapterMiddle.filter.filter(searchBar.query)
                }
            }
        }

        myListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show()
            val nameOfSelected = view.findViewById<TextView>(R.id.tvListName)
            clickedList = nameOfSelected.text.toString()

            var gson = Gson()
            val intent = Intent(this,ListActivity::class.java)

            val json: String = gson.toJson(mapOfAllLists[clickedList])
            intent.putExtra("name",clickedList)
            intent.putExtra("mainList", json)
            intent.putExtra("theme",theme)

            arrayOfListInfo = ArrayList(arrayOfListInfoCopy) //reset array to original

            startForResult.launch(intent)
            Animatoo.animateSlideLeft(this)
        }

        addBtn.setOnClickListener{
            val unit = ListData("new list", 0)
            arrayOfListInfo.add(unit)
            arrayOfListInfoCopy.add(unit)
            println("array in addbtn")
            println(arrayOfListInfo)
            adapterMiddle.clear()
            adapterMiddle.addAll(ArrayList(arrayOfListInfoCopy))
            println("array in addbtn")
            println(arrayOfListInfo)
            adapterMiddle.notifyDataSetChanged()
            searchBar.setQuery("",false)
        }

        profile.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("theme", if (theme == "Light") "Dark" else "Light")
            startActivity(intent)
            finish()
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapterMiddle.filter.filter(newText)
                return true
            }
        })

        clearSearchFocus()  //clear search bar focus if user clicks outside it

    }

    private fun updateList(myResult: String?, listname: String?){
        var gson = Gson()
        val newArray = object : TypeToken<ArrayList<Word>>() {}.type
        var wordArray: ArrayList<Word> = gson.fromJson(myResult, newArray)

        var save = clickedList
        clickedList = listname!!
        var interMap= LinkedHashMap<String, ArrayList<Word>>()

        if (save == "new list"){
            mapOfAllLists[clickedList] = wordArray   //updates mapofalllists with new arraylist
        }
        else if (save != clickedList) {  //name of list changed
            mapOfAllLists.forEach { (key, value) ->
                if (key != save) {
                    interMap[key] = value
                } else {
                    interMap[clickedList] = wordArray
                }
            }
            mapOfAllLists.clear()
            interMap.forEach{(key, value)->
                mapOfAllLists[key]= value
            }
        }else{  //name of list not changed
            mapOfAllLists[clickedList] = wordArray   //updates mapofalllists with new arraylist
        }

        //remove arrayoflistinfo item if it is named "new list", and replace with actual name/data
        val removed = arrayOfListInfo.removeIf { listelm ->
            listelm.listName == "new list"
        }
        if (removed){
            val unit = ListData(clickedList, wordArray.size)
            arrayOfListInfo.add(unit)
        }else {
            arrayOfListInfo.forEach { listelm ->
                if (listelm.listName == save) {
                    listelm.numWords = wordArray.size
                    if (save != clickedList) {
                        listelm.listName = clickedList
                    }
                }
            }
        }
        arrayOfListInfoCopy = ArrayList(arrayOfListInfo)
        println("array in updateList")
        println(arrayOfListInfo)
    }

    //helper function for defaultMap()
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

    private fun getMap(){
        val file = File(this@MainActivity.filesDir, fileName)  //just to check if file exists
        //if file exists, read. if not, call defaultMap to populate mapOfAllLists with default data
        if (file.exists()) {
            readFile(fileName)
        }
        else {
            defaultMap()
            stringifyMap()
        }
    }

    private fun readFile(fileName: String){
        val mapStr: String
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
        mapStr = stringBuilder.toString()
        parseMap(mapStr)
    }

    private fun writeFile(fileName: String, mapStr: String){
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

    private fun parseMap(mapStr: String){
        var gson = Gson()
        val map = object : TypeToken< LinkedHashMap<String, ArrayList<Word>>>() {}.type
        try {
            var parsedMap: LinkedHashMap<String, ArrayList<Word>> = gson.fromJson(mapStr, map)
            mapOfAllLists = parsedMap
        } catch (e: IllegalStateException) {
            println("error: ${e.message} ")
        } catch (e: JsonSyntaxException) {
            println("error: ${e.message} ")
        }

        //update arrayoflistinfo
        mapOfAllLists.forEach { t, u ->
            val unit = ListData(t, u.size)
            arrayOfListInfo.add(unit)
        }

        arrayOfListInfoCopy = ArrayList(arrayOfListInfo)
    }

    private fun stringifyMap(){
        //to write new data to file
        val gson = Gson()
        val json = gson.toJson(mapOfAllLists)
        writeFile(fileName,json)
    }

    private fun defaultMap(){
        //create default map if app being opened for the first time, and data file doesnt exist yet
        val listOfLists = mutableListOf("Verbs","Nouns","Adjectives")

        val words1 = arrayOf("寝る", "走る","食べる")
        val hiragana1 = arrayOf("ねる", "はしる","たべる")
        val romaji1 = arrayOf("neru","hashiru", "taberu")
        val definitions1 = arrayOf("to sleep", "to run", "to eat")

        val words2 = arrayOf("家族", "世界","お店")
        val hiragana2 = arrayOf("かぞく", "せかい","おみせ")
        val romaji2 = arrayOf("kazoku","sekai", "omise")
        val definitions2 = arrayOf("family", "world", "shop")

        val words3 = arrayOf("狭い", "少ない","高い")
        val hiragana3 = arrayOf("せまい", "すくない","たかい")
        val romaji3 = arrayOf("semai","sukunai", "takai")
        val definitions3 = arrayOf("narrow", "few", "tall")

        mapOfAllLists[listOfLists[0]] = createData(words1,hiragana1,romaji1,definitions1)
        mapOfAllLists[listOfLists[1]] = createData(words2,hiragana2,romaji2,definitions2)
        mapOfAllLists[listOfLists[2]] = createData(words3,hiragana3,romaji3,definitions3)

        listOfLists.forEach { item ->
            val unit = ListData(item, mapOfAllLists[item]!!.size)
            arrayOfListInfo.add(unit)
        }

        arrayOfListInfoCopy = ArrayList(arrayOfListInfo)

    }

    private fun clearSearchFocus(){
        //clear focus on search bar if user clicks outside it
        mainparent.setOnTouchListener{ _, _ ->
            if (searchBar.hasFocus()){
                searchBar.clearFocus()
                println("should clear focus main parent")
            }
            false
        }

        myListView.setOnTouchListener{ _, _ ->
            if (searchBar.hasFocus()){
                searchBar.clearFocus()
                println("should clear focus mylistview")
            }
            false
        }
    }


}

//icon credits to pngegg, icons8
//cats profile pics credit to  @cadmium_red on Freepik
//books icon credit to iconsDB