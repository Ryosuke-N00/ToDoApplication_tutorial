package com.example.ryousuke.todoapplicationtutorial.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ryousuke.todoapplicationtutorial.data.ToDoDatabase
import com.example.ryousuke.todoapplicationtutorial.data.Repository.ToDoRepository
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    //DataBase(Repository)とのやりとりに必要なインスタンス化
    private val toDoDao = ToDoDatabase.getToDoDatabase(
        application
    ).toDoDao()

    //ViewModelは、LiveDataに情報を持たせる
    val getToDoAllData: LiveData<List<ToDoDataEntity>>

    //Repository型、UI操作からデータを与えることもできるし、データをもらうこともできる
    private val repository :ToDoRepository

    //優先順位の変更
    val sortByHighPriorityToDo: LiveData<List<ToDoDataEntity>>
    val sortByLowPriorityToDo: LiveData<List<ToDoDataEntity>>

    //ViewModelは、RepositoryとUIのやりとりをするので、Repositoryとやり取りをする上でのRepository型の初期化
    init {
        repository = ToDoRepository(toDoDao)
        getToDoAllData = repository.getAllData

        //初期化を忘れない
        sortByHighPriorityToDo = repository.sortByHighPriorityToDo
        sortByLowPriorityToDo = repository.sortByLowPriorityToDo
    }

    //Repositoryでsuspend関数にしていたのでCoroutinesスコープを定義して処理を実行させる
    fun insertData(toDoData: ToDoDataEntity){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insertToDoData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoDataEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToDoData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoDataEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllToDo()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoDataEntity>> {
        return repository.searchDatabase(searchQuery)
    }
}