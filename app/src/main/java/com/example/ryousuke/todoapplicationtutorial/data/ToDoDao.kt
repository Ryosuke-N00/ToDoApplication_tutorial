package com.example.ryousuke.todoapplicationtutorial.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity

//@Daoはデータベースにアクセスする
@Dao
interface ToDoDao {

    //todoData(リスト型)をLiveDataが管理し変化を教えてくれるので、FragmentなどのObserverが変化に気づく
    @Query("SELECT * FROM todo_table ORDER BY todoid ASC")
    fun getAllToDoData(): LiveData<List<ToDoDataEntity>>

    //Coroutinesの関数で処理、backgroundで処理を行ってくれる
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToDoData(toDoData: ToDoDataEntity)

    //保存の処理
    @Update
    suspend fun updateToDoData(toDoData: ToDoDataEntity)

    //削除の処理
    @Delete
    suspend fun deleteItem(toDoData: ToDoDataEntity)

    //すべて削除
    @Query("DELETE FROM todo_table")
    suspend fun deleteAllToDo()

    //探し出す
    @Query("SELECT * FROM todo_table WHERE todotitle LIKE :searchQuery")
    fun searchQueryToDo(searchQuery: String): LiveData<List<ToDoDataEntity>>

    //優先度の高い順に並び替える
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN todopriority LIKE 'H%' THEN 1 WHEN todopriority LIKE 'M%' THEN 2 WHEN todopriority LIKE 'L%' THEN 3 END")
    fun sortByHighPriorityToDo() : LiveData<List<ToDoDataEntity>>

    //優先度の低い順に並び替える
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN todopriority LIKE 'L%' THEN 1 WHEN todopriority LIKE 'M%' THEN 2 WHEN todopriority LIKE 'H%' THEN 3 END")
    fun sortByLowPriorityToDo() : LiveData<List<ToDoDataEntity>>
}