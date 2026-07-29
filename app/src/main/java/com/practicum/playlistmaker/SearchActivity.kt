package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String  = ""
    private lateinit var searchEditText: EditText
    private lateinit var btnClearSearch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        searchEditText = findViewById(R.id.searchEditText)
        btnClearSearch = findViewById(R.id.btnClearSearch)

        btnBack.setOnClickListener {
            val btnBackIntent = Intent(this, MainActivity::class.java)
            startActivity(btnBackIntent)
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: ""
                updateVisibility()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateVisibility()
            } else {
                btnClearSearch.visibility = View.GONE
            }
        }

        btnClearSearch.setOnClickListener {
            searchEditText.text.clear()
            btnClearSearch.visibility = View.GONE
            searchEditText.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
    }

    fun updateVisibility() {
        val text = searchEditText.text.toString()
        btnClearSearch.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY", searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString("SEARCH_QUERY", "")
        searchEditText.setText(savedText)
        searchQuery = savedText
        updateVisibility()
    }
}