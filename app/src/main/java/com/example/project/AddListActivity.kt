package com.example.project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityAddListBinding

class AddListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSave.setOnClickListener {
            val title = binding.editTitle.text.toString()



            if (title.isNotEmpty()) {

                val resultIntent = Intent().apply {
                    putExtra("TITLE", title)

                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                binding.editTitle.error = "O título é obrigatório"
            }
        }
    }
}
