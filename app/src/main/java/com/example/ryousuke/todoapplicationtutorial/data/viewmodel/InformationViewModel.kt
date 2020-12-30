package com.example.ryousuke.todoapplicationtutorial.data.viewmodel

import android.app.Application
import android.renderscript.RenderScript
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ryousuke.todoapplicationtutorial.R
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoPriority

class InformationViewModel(application: Application): AndroidViewModel(application) {

    //falseにする理由→trueの時一瞬データが何もないと表示されるから
    //MutableLiveDataにすることで編集が可能になっていることに注意
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    //リストの中身が本当に何もないかを確認してくれる処理
    //LiveDataを使っているので、中身はValue
    fun checkIfDatabaseEmpty(toDoData: List<ToDoDataEntity>){
        emptyDatabase.value = toDoData.isEmpty()
    }


    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
        ) {

            //Priorityの色をここで定義
            when(position){
                0->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,R.color.design_default_color_primary_variant))}
                2->{(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,R.color.green))}
            }

        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

    fun verifyDataFromUser(title: String,description: String): Boolean{
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)){
            false
            //中身がnull出ないときはtrueを返してくれる
        } else !(title.isEmpty() || description.isEmpty())
    }

    //Priorityを直接与えると型のミスマッチによりエラーが起きるので型を変える
    fun parsePriority(priority: String): ToDoPriority{
        return when(priority){
            "High Priority"->{
                ToDoPriority.HIGH}
            "Medium Priority"->{
                ToDoPriority.MEDIUM}
            "Low Priority"->{
                ToDoPriority.LOW}
            else -> ToDoPriority.LOW
        }
    }

    fun parsePriorityInt(priority: ToDoPriority): Int {
        return when (priority) {
            ToDoPriority.HIGH -> 0
            ToDoPriority.MEDIUM -> 1
            ToDoPriority.LOW -> 2
        }
    }

}