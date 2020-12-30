package com.example.ryousuke.todoapplicationtutorial.fragments.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDelete: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }


    //これを定義してメソッドを作らないとエラーが出てしまうのでこのクラスが必要になる
    /*override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }*/
}