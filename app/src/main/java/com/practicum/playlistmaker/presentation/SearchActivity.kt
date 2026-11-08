package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.TrackAdapter
import com.practicum.playlistmaker.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.SearchTracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.creator.Creator

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    //ВСЕ View
    private lateinit var searchEditText: EditText
    private lateinit var btnClearSearch: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyPlaceholder: View
    private lateinit var errorPlaceholder: View
    private lateinit var retryBtn: Button

    private lateinit var historyContainer: View
    private lateinit var youSearch: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var btnClearHistory: Button

    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var searchTracksInteractor: SearchTracksInteractor
    private lateinit var progressBar: ProgressBar

    private var searchQuery: String  = ""

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val query = searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            searchTracks(query)
        }
    }

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ИНИЦИАЛИЗАЦИЯ ВСЕХ View
        searchEditText = findViewById(R.id.searchEditText)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.rvTracks)
        emptyPlaceholder = findViewById(R.id.emptyPlaceholder)
        errorPlaceholder = findViewById(R.id.errorPlaceholder)
        retryBtn = findViewById(R.id.retryBtn)
        progressBar = findViewById(R.id.progressBar)

        //ИСТОРИЯ
        historyContainer = findViewById(R.id.historyContainer)
        youSearch = findViewById(R.id.youSearch)
        rvHistory = findViewById(R.id.rvHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)


        // Инициализация интеракторов
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)
        searchTracksInteractor = Creator.provideSearchTracksInteractor()

        // АДАПТЕР для результатов поиска
        adapter = TrackAdapter(
            tracks = emptyList(),
            onItemClick = { track ->
                if (clickDebounce()) {
                    searchHistoryInteractor.addTrack(track)
                    updateHistoryVisibility()

                    //переход на экран плеера
                    val intent = Intent(this, PlayerActivity::class.java)
                    intent.putExtra(PlayerActivity.EXTRA_TRACK, track)
                    startActivity(intent)
                }
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //АДАПТЕР для истории
        historyAdapter = TrackAdapter(
            tracks = emptyList(),
            onItemClick = { track ->
                if (clickDebounce()) {
                    searchHistoryInteractor.addTrack(track)
                    updateHistoryVisibility()

                    //переход на экран плеера
                    val intent = Intent(this, PlayerActivity::class.java)
                    intent.putExtra(PlayerActivity.EXTRA_TRACK, track)
                    startActivity(intent)
                }
            }
        )
        rvHistory.adapter = historyAdapter
        rvHistory.layoutManager = LinearLayoutManager(this)


        //КНОПКА ОЧИСТКИ ИСТОРИИ
        btnClearHistory.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            historyAdapter.updateTracks(emptyList())
            historyContainer.visibility = View.GONE
        }

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
                    recyclerView.visibility = View.GONE
                    emptyPlaceholder.visibility = View.GONE
                    errorPlaceholder.visibility = View.GONE
                    updateHistoryVisibility()
                }else{
                    historyContainer.visibility = View.GONE
                    searchDebounce()
                }
            }
        })

        //ФОКУС ПОЛЯ ПОИСКА
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchEditText.text.isNullOrEmpty()) {
                updateHistoryVisibility()
            }else{
                historyContainer.visibility = View.GONE
            }
        }

        // ОТРАБОТКА НАЖАТИЯ DONE
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchEditText.text.toString().trim()

                if (query.isNotEmpty()) {
                    searchTracks(query)
                    hideKeyboard()
                }
                true
            } else {
                false
            }
        }

        //КНОПКА ОЧИСТКИ ПОИСКА
        btnClearSearch.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            hideKeyboard()
            updateHistoryVisibility()
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

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable,SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun updateHistoryVisibility() {
        val history = searchHistoryInteractor.getHistory()
        val isSearchEmpty = searchEditText.text.isNullOrEmpty()
        val hasFocus = searchEditText.hasFocus()
        val hasHistory = history.isNotEmpty()

        val showHistory = isSearchEmpty && hasFocus && hasHistory

        if (showHistory) {
            historyContainer.visibility = View.VISIBLE
            historyAdapter.updateTracks(history)

            recyclerView.visibility = View.GONE
            emptyPlaceholder.visibility = View.GONE
            errorPlaceholder.visibility = View.GONE

        }else{
            historyContainer.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        handler.removeCallbacksAndMessages(null)
    }

    private fun searchTracks(query: String) {
        searchQuery = query
        hideAll()
        progressBar.visibility = View.VISIBLE

        searchTracksInteractor.searchTracks(query, object : SearchTracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                progressBar.visibility = View.GONE
                if (errorMessage != null) {
                    showError()
                } else if (foundTracks.isNullOrEmpty()) {
                    showEmpty()
                } else {
                    showTracks(foundTracks)
                }
            }
        })
    }


private fun showTracks(tracks: List<Track>) {
    adapter.updateTracks(tracks)
    recyclerView.visibility = View.VISIBLE
    emptyPlaceholder.visibility = View.GONE
    errorPlaceholder.visibility = View.GONE
    historyContainer.visibility = View.GONE
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
        progressBar.visibility = View.GONE
    }

    private fun updateClearButtonVisibility() {
        val text = searchEditText.text.toString()
        btnClearSearch.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY", searchEditText.text.toString())
    }
}