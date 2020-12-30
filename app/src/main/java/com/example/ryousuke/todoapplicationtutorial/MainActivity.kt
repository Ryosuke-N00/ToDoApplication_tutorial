package com.example.ryousuke.todoapplicationtutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //バーの表示を対応させる
        setupActionBarWithNavController(findNavController(R.id.navHostFragment))
    }

    //バーの戻るボタンでも戻る処理
    override fun onSupportNavigateUp(): Boolean {
        val navContoroller = findNavController(R.id.navHostFragment)
        return navContoroller.navigateUp() || super.onSupportNavigateUp()
    }
}