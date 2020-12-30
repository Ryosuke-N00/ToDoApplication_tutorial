package com.example.ryousuke.todoapplicationtutorial.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.databinding.RowLayoutBinding
import com.example.ryousuke.todoapplicationtutorial.fragments.list.ListFragmentDirections
import kotlinx.android.synthetic.main.row_layout.view.*

class ToDoListAdapter: RecyclerView.Adapter<ToDoListAdapter.MyViewHolder>() {

    //ここで何も持っていないリストを作ることが大事
    var dataList = emptyList<ToDoDataEntity>()

    //class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    class MyViewHolder(private val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(toDoDataEntity: ToDoDataEntity){

            //ここの引数の名称を一致させる
            binding.toDoDataEntity = toDoDataEntity
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)


        //safe argsを使うにはこの処理からRebuildが必要→与える引数を変えて実行すると,Argsのクラスが生成される

        /*holder.itemView.title_txt.text = dataList[position].todotitle
        holder.itemView.description_txt.text = dataList[position].tododescription
        holder.itemView.row_background.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }*/
    }

    //これでデータベースに情報を渡すorRecyclerViewに情報を渡すと思う
    fun setData(toDoData: List<ToDoDataEntity>){
        //Diffのオブジェクトを生成し、処理を行う
        val toDoDiffUtil = ToDoDiffUtil(dataList,toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        //notifyDataSetChanged()は、オーバーキルの処理になるのでDiffUtilを使った処理に変更

        toDoDiffResult.dispatchUpdatesTo(this)

    }

    //RecyclerViewのもつデータの数を教えてくれる、リストの中身の大きさを伝える
    override fun getItemCount(): Int {
        return dataList.size
    }


}