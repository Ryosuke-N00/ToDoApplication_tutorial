package com.example.ryousuke.todoapplicationtutorial.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ryousuke.todoapplicationtutorial.R
import com.example.ryousuke.todoapplicationtutorial.data.models.ToDoDataEntity
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.InformationViewModel
import com.example.ryousuke.todoapplicationtutorial.data.viewmodel.ToDoViewModel
import com.example.ryousuke.todoapplicationtutorial.databinding.FragmentUpdateBinding
import kotlinx.android.synthetic.main.fragment_update.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    //ToDoViewModelを追加
    private val mInformationViewModel: InformationViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    //DataBindingを追加させる
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       //変更を伝えるためにデータバインディングを利用するので、ビューの作り方が異なる
        //これでデータバインディングされたビューを生成
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        binding.args = args

        //メニューボタンの生成
        setHasOptionsMenu(true)

        binding.currentPrioritiesSpinner.onItemSelectedListener = mInformationViewModel.listener

        return binding.root
    }

    //これを忘れない→メニュー場に現れない
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    //saveボタンを押したときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //押したものがsaveボタンと等しかったら保存する処理
        //処理が二つあるところはこういう風にwhenで条件分岐を書くとめちゃくちゃシンプルになる
        when(item.itemId){
            R.id.menu_save->updateItem()
            R.id.menu_delete->confirmItemRemove()
        }
        return super.onOptionsItemSelected(item)
    }


    //保存をするときの処理→Daoに@Updateを追加 →Repositoryにsuspendを追加、ViewModelにcoroutinesで処理を明確化
    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_descriptin_et.text.toString()
        val getpriority = current_priorities_spinner.selectedItem.toString()

        //veryfyDataFromUserメソッドで、nullチェックをしているのでこのメソッドはまじで天才や
        val validation = mInformationViewModel.verifyDataFromUser(title,description)
        if(validation){
            //Entityが持つべきものを引数に与える
            val updatedItem = ToDoDataEntity(
                args.currentItem.todoid,
                title,
                mInformationViewModel.parsePriority(getpriority),
                description
            )
            //ここで変更を保存している処理
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(),"Successfully updated", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmItemRemove() {
        val builder = AlertDialog.Builder(requireContext())

        //Yes or Noの選択で候補を処理するコード
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Succesfully Removed: ${args.currentItem.todotitle}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle("Delete '${args.currentItem.todotitle}'??")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.todotitle}'??")
        builder.create().show()
    }

    //メモリリーク防止
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}