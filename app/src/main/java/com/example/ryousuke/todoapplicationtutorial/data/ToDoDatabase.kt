package com.example.ryousuke.todoapplicationtutorial.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity

//このクラスではRoom(データベース)が複数存在しないように管理することが大事

@Database(entities = [ToDoDataEntity::class] ,version = 1,exportSchema = false)
@TypeConverters(ToDoConverter::class)
abstract class ToDoDatabase :RoomDatabase(){

    //メソッドは不明瞭なままで→ViewModel作成時に、Daoとの関連を持たせるために必要な定義
    abstract fun toDoDao(): ToDoDao

    //Javaでいうstatic
    companion object{
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        //既に存在しているときは存在しているものを返す
        fun getToDoDatabase(context: Context): ToDoDatabase {
            val todoInstance = INSTANCE
            if (todoInstance != null){
                return todoInstance
            }

            //synchronizedは、この処理が終わるまでほかの処理を実行できないので、データベースがない時はこのブロックから処理が始まる
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


    }
}