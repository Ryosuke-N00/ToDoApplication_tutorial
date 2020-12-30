package com.example.ryousuke.todoapplicationtutorial.data.Repository

import androidx.lifecycle.LiveData
import com.example.ryousuke.todoapplicationtutorial.data.ToDoDao
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity

//DataBase特に、Daoのメソッドを非同期処理に具体的に落とし込む
class ToDoRepository(private val toDoDao: ToDoDao) {

    //引数のtoDoDaoで、Daoと関連を持つ
    val getAllData: LiveData<List<ToDoDataEntity>> = toDoDao.getAllToDoData()

    //優先順位の入れ替え
    val sortByHighPriorityToDo: LiveData<List<ToDoDataEntity>> = toDoDao.sortByHighPriorityToDo()
    val sortByLowPriorityToDo: LiveData<List<ToDoDataEntity>> = toDoDao.sortByLowPriorityToDo()

    //データベースのパラメータ情報にアクセス
    suspend fun insertToDoData(toDoData: ToDoDataEntity){
        toDoDao.insertToDoData(toDoData)
    }

    //内容を変更する
    suspend fun updateToDoData(toDoData: ToDoDataEntity){
        toDoDao.updateToDoData(toDoData)
    }

    //1つを削除
    suspend fun deleteItem(toDoData: ToDoDataEntity){
        toDoDao.deleteItem(toDoData)
    }

    //すべてを削除
    suspend fun deleteAllToDo(){
        toDoDao.deleteAllToDo()
    }

    //探し出す
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoDataEntity>>{
        return toDoDao.searchQueryToDo(searchQuery)
    }

}