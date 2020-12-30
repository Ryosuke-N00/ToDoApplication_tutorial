package com.example.ryousuke.todoapplicationtutorial.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.ryousuke.todoapplicationtutorial.R
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoPriority
import com.example.ryousuke.todoapplicationtutorial.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {
    companion object{

        //データバインディングにより、フラグメントに記述している処理を減らしレイアウトに追加
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate : Boolean){
            view.setOnClickListener{
                if(navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        //データがない時は見えるように、見えないときは表示されないようにデータバインディングで決定できる
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: ToDoPriority){
            when(priority){
                ToDoPriority.HIGH -> {view.setSelection(0)}
                ToDoPriority.MEDIUM -> {view.setSelection(1)}
                ToDoPriority.LOW -> {view.setSelection(2)}
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: ToDoPriority){
            when(priority){
                ToDoPriority.HIGH -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                ToDoPriority.MEDIUM -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.design_default_color_primary_variant))}
                ToDoPriority.LOW -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
            }
        }

        //更新した内容を保持したまま画面遷移を行う
        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun senDataUpdateFragment(view: ConstraintLayout, currentItem: ToDoDataEntity){
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }
    }
}