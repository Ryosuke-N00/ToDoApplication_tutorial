package com.example.ryousuke.todoapplicationtutorial.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoPriority
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoDataEntity(

    //自動で番号を割り振りできる
    @PrimaryKey(autoGenerate = true)
    var todoid :Int,
    var todotitle :String,
    //クラス型の定義に注意
    var todopriority : ToDoPriority,
    var tododescription :String

): Parcelable
//safe argsを利用するには追加の変更を忘れないようにしよう