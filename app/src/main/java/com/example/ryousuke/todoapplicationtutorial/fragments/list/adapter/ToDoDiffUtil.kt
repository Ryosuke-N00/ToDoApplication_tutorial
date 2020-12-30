package com.example.ryousuke.todoapplicationtutorial.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity

class ToDoDiffUtil(
    private val oldList: List<ToDoDataEntity>,
    private val newList: List<ToDoDataEntity>
): DiffUtil.Callback() {

    //削除前の個数を教えてくれる
    override fun getOldListSize(): Int {
        return oldList.size
    }

    //新しい個数を教えてくれる
    override fun getNewListSize(): Int {
        return newList.size
    }

    //ポジションの変化がないかを確認
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    //中身に変化がないかを確認
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].todoid == newList[newItemPosition].todoid
                && oldList[oldItemPosition].todotitle == newList[newItemPosition].todotitle
                && oldList[oldItemPosition].tododescription == newList[newItemPosition].tododescription
                && oldList[oldItemPosition].todopriority == newList[newItemPosition].todopriority

    }
}