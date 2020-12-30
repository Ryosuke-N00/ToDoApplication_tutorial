package com.example.ryousuke.todoapplicationtutorial.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ryousuke.todoapplicationtutorial.R
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.InformationViewModel
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    //FragmentにViewModelのインスタンスを追加
    private val mToDoViewModel: ToDoViewModel by viewModels()

    //これを実行しないとデータは表示されない
    private val mInformationViewModel: InformationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //データバインディングを利用しないので、いつも通りビューを作成
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        //メニューボタンの生成
        setHasOptionsMenu(true)

        //SharedViewModelに追加した処理をここで実行にもっていく
        view.priorities_spinner.onItemSelectedListener = mInformationViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //フローティングボタンが押されたのかどうかの確認
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    //フローティングボタンが押されてから追加されるもの
    private fun insertDataToDb() {
        val mTitle = title_et.text.toString()
        val mPriority = priorities_spinner.selectedItem.toString()
        val mDescription = descriptin_et.text.toString()

        //下のメソッドを使って中身がnull出ないかの確認を行う、別のViewModelにメソッドを追加したので定義の確認をしっかりしよう
        val validtation = mInformationViewModel.verifyDataFromUser(mTitle,mDescription)
        if (validtation){
            val newData = ToDoDataEntity(
                0,
                mTitle,
                mInformationViewModel.parsePriority(mPriority),
                mDescription
            )
            //ここでデータを追加させてる
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added", Toast.LENGTH_SHORT).show()
            //追加されたらフラグメントを移動する
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            //すべて入力させる
            Toast.makeText(requireContext(),"Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

}