package com.example.easydictionary

data class Word(var name: String, var hiragana: String,var romaji: String, var definition: String){
    private var clicked: Boolean = false
    public fun isClicked(): Boolean{
        return clicked
    }
    public fun toggleClick(){
        clicked=true
    }
}
