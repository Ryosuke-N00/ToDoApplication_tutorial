package com.example.ryousuke.todoapplicationtutorial.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ryousuke.todoapplicationtutorial.R
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.InformationViewModel
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.ToDoViewModel
import com.example.ryousuke.todoapplicationtutorial.databinding.FragmentListBinding
import com.example.ryousuke.todoapplicationtutorial.fragments.list.adapter.ToDoListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment() , SearchView.OnQueryTextListener{

    //ViewModelの生成
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mInformationViewModel: InformationViewModel by viewModels()

    //Bindingオブジェクトを生成しないと実行しても画面遷移は起こらない
    //FragmentListBindingは、データバインディングをレイアウトに取り込むと自動で生成される
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    //adapterのインスタンの生成
    private val adapter: ToDoListAdapter by lazy { ToDoListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //メニューボタンの生成→データバインディングを用いるとビューの生成の仕方が変わることに注意
        setHasOptionsMenu(true)

        _binding = FragmentListBinding.inflate(inflater,container,false)
        //lifecycleOwnerとViewModelが直接レイアウトにデータのやり取りができるようにしている
        binding.lifecycleOwner = this
        binding.mInformationViewModel = mInformationViewModel

        //RecyclerViewをメソッドを使って生成させる

        setupRecylerView()

        //fragmentがライフサイクルのオーナーとなり、変化を伝える
        mToDoViewModel.getToDoAllData.observe(viewLifecycleOwner, Observer {data ->
            //dataがあるかないかをここでも判断する
            mInformationViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        return binding.root
    }

    //onCreateの所でこのメソッドの呼び出しによりRecyclerViewを生成
    private fun setupRecylerView() {
        //どのRecyclerViewを選択するのかが大事→id通りに選択しよう
        val recyclerView =binding.todoRecyclerView
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())で作ってた
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        //RecyclerViewのアニメーションの追加
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        //swipe delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(deletedItem)
                //スワイプ削除を復活させるときに必要
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //削除したものをためることができるので必要なくなった
                //Toast.makeText(requireContext(),"Successfully Removed: '${deletedItem.title}'",Toast.LENGTH_SHORT).show()

                //何が消されたかを保存している
                restoreDeleteData(viewHolder.itemView,deletedItem,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //削除したときに復活させることができるメソッド
    private fun restoreDeleteData(view: View, deletedItem: ToDoDataEntity,position: Int){
        val snackBar = Snackbar.make(
            view,"Deleted '${deletedItem.todotitle}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        //これでToastの分が省略できる
        snackBar.show()
    }

    //メニューボタンの生成
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)

        //探すボタンのインスタンス化と検索機能
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriorityToDo.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriorityToDo.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    //探し出すのに必要な処理
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let{
                adapter.setData(it)
            }
        })
    }

    //すべてのデータを削除する
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())

        //Yes or Noの選択で候補を処理するコード
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully Removed everything", Toast.LENGTH_SHORT).show()
            //ナビゲーションの指示は特に必要がない
        }
        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle("Delete Everything??")
        builder.setMessage("Are you sure you want to remove Everything??")
        builder.create().show()
    }

    //メモリリークを防ぐ
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}