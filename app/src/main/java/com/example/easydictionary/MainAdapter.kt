package com.example.easydictionary

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
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

    private var filteredItems: ArrayList<ListData> = arrayListOf()
    private lateinit var temp: ArrayList<ListData>

    private var myArray = (context as MainActivity).arrayOfListInfo

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

        try {
            val resourceid = resources.getIdentifier(picArray[counter], "mipmap", context.packageName)
            imgPic.setImageResource(resourceid)
        } catch(e:IndexOutOfBoundsException){
            println("error: ${e.message}")
            imgPic.setImageResource(R.mipmap.pic6)
        }

        counter += 1

        println("array in adapter:")
        println(myArray)

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                temp = (context as MainActivity).arrayOfListInfoCopy

                val filteredList = ArrayList<ListData>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(temp)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    println("filterpattern is: ")
                    println(filterPattern)
                    println("arrayList: ")
                    println(arrayList)
                    println("temp")
                    println(temp)
                    for (item in temp) {
                        if (item.listName.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                            //println(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayList.clear()
                arrayList.addAll(results?.values as ArrayList<ListData>)
                notifyDataSetChanged()
            }
        }
    }

    }