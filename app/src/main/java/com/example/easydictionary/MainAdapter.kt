package com.example.easydictionary

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MainAdapter (private val context: Activity, private val arrayList: ArrayList<ListData>): ArrayAdapter<ListData>(context,
    R.layout.list_item, arrayList) {

    private lateinit var name: TextView
    private lateinit var num: TextView
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var picArray: Array<String>
    private lateinit var imgPic: ImageView
    private val resources = context.resources
    private var counter: Int = 0


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        inflater = LayoutInflater.from(context)
        view= inflater.inflate(R.layout.list_item, null)

        name = view.findViewById(R.id.tvListName)
        num= view.findViewById(R.id.tvNumWords)
        imgPic = view.findViewById(R.id.imgPic)

        name.text = arrayList[position].listName
        num.text = arrayList[position].numWords.toString() + " words"

        picArray = arrayOf("pic1","pic2", "pic3","pic4","pic5", "pic6","pic7", "pic8","pic9","pic1")
        counter = position % 9
        println(counter)

        try {
            val resourceid = resources.getIdentifier(picArray[counter], "mipmap", context.packageName)
            imgPic.setImageResource(resourceid)
        } catch(e:IndexOutOfBoundsException){
            println("error: ${e.message}")
            imgPic.setImageResource(R.mipmap.pic6)
        }

        counter += 1

        return view
    }
    }