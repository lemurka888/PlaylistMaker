package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.network.RetrofitClient
import com.practicum.playlistmaker.network.TrackResponse
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class SearchActivity : AppCompatActivity() {

    //ВСЕ View
    private lateinit var searchEditText: EditText
    private lateinit var btnClearSearch: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyPlaceholder: View
    private lateinit var errorPlaceholder: View
    private lateinit var retryBtn: Button

    private lateinit var adapter: TrackAdapter

    private var searchQuery: String  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // ИНИЦИАЛИЗАЦИЯ ВСЕХ View
        searchEditText = findViewById(R.id.searchEditText)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.rvTracks)
        emptyPlaceholder = findViewById(R.id.emptyPlaceholder)
        errorPlaceholder = findViewById(R.id.errorPlaceholder)
        retryBtn = findViewById(R.id.retryBtn)

        // АДАПТЕР
        adapter = TrackAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // КНОПКА НАЗАД
        btnBack.setOnClickListener {
            finish()
        }


        // ОТСЛЕЖИВАНИЕ ТЕКСТА
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateClearButtonVisibility()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    hideAll()
                }
            }
        })

        // ОТРАБОТКА НАЖАТИЯ DONE
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchEditText.text.toString()
                if (query.isNotEmpty()) {
                    searchTracks(query)
                    hideKeyboard()
                }
                true
            } else {
                false
            }
        }

        btnClearSearch.setOnClickListener {
            searchEditText.text.clear()
            hideAll()
            searchEditText.clearFocus()
            hideKeyboard()
        }

        // КНОПКА ОБНОВИТЬ
        retryBtn.setOnClickListener {
            searchEditText.clearFocus()
            hideKeyboard()
            if (searchQuery.isNotEmpty()) {
                searchTracks(searchQuery)
            }
        }

        // ВОССТАНОВЛЕНИЕ ПРИ ПОВОРОТЕ
        if(savedInstanceState != null) {
            val savedText = savedInstanceState.getString("SEARCH_QUERY", "")
            searchEditText.setText(savedText)
            updateClearButtonVisibility()
            searchEditText.clearFocus()
            hideKeyboard()
        }
    }
    private fun searchTracks(query: String) {
        searchQuery = query
        hideAll()

        RetrofitClient.api.searchTracks(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    val tracks = response.body()?.results ?: emptyList()
                    if (tracks.isEmpty()) {
                        showEmpty()
                    } else {
                        showTracks(tracks)
                    }
                } else {
                    showError()
                }
            }
            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
            showError()
            }
        })
}


private fun showTracks(tracks: List<Track>) {
    adapter.updateTracks(tracks)
    recyclerView.visibility = View.VISIBLE
    emptyPlaceholder.visibility = View.GONE
    errorPlaceholder.visibility = View.GONE
}

    private fun showEmpty() {
        emptyPlaceholder.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE
    }

    private fun showError() {
        errorPlaceholder.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        emptyPlaceholder.visibility = View.GONE
}

    private fun hideAll() {
        recyclerView.visibility = View.GONE
        emptyPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE
    }

    private fun updateClearButtonVisibility() {
        val text = searchEditText.text.toString()
        btnClearSearch.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY", searchEditText.text.toString())
    }
}
