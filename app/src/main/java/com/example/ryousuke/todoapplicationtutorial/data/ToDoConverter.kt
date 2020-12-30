package com.example.ryousuke.todoapplicationtutorial.data

import androidx.room.TypeConverter
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoPriority

//型を変えたことをToDoDatabaseにアノテーションして知らせることを忘れない
//この時、TypeConvertersでアノテーションをすることに注意
class ToDoConverter {

    //databaseで扱うことのできる型に帰る .nameである理由はStringに変えたいから
    @TypeConverter
    fun fromPriority(pritority: ToDoPriority):String{
        return pritority.name
    }

    //Stringから、Priority型に帰るメソッド
    @TypeConverter
    fun toPirority(pritority: String): ToDoPriority {
        return  ToDoPriority.valueOf(pritority)
    }

}