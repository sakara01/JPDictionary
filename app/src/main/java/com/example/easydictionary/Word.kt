package com.example.easydictionary

data class Word(var name: String, var hiragana: String,var romaji: String, var definition: String){
    private var clicked: Boolean = false
    fun isClicked(): Boolean{
        return clicked
    }
    fun toggleClick(){
        clicked=true
    }
}
