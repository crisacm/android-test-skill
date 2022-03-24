package com.github.crisacm.testskills.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.crisacm.testskills.R
import com.github.crisacm.testskills.databinding.CompCustomRecyclerViewBinding
import com.github.crisacm.testskills.databinding.CompCustomRecyclerViewEmptyBinding

class CustomRecyclerView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private val binding: CompCustomRecyclerViewBinding = CompCustomRecyclerViewBinding.inflate(LayoutInflater.from(context), this)
    private val emptyBinding: CompCustomRecyclerViewEmptyBinding = binding.layoutEmpty

    val recyclerView: RecyclerView = binding.recyclerView

    var emptyText: String = resources.getString(R.string.txt_list_is_empty)
        set(value) {
            field = value
            emptyBinding.textView.text = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomRecyclerView,
            0,
            0
        ).apply {
            try {
                emptyText = getString(R.styleable.CustomRecyclerView_setEmptyText) ?: resources.getString(R.string.txt_list_is_empty)

                recyclerView.setBackgroundResource(getResourceId(R.styleable.CustomRecyclerView_setRecyclerViewBackground, android.R.color.white))
                emptyBinding.constraintLayout
                    .setBackgroundResource(getResourceId(R.styleable.CustomRecyclerView_setEmptyBackground, android.R.color.white))

                val height = getDimensionPixelSize(R.styleable.CustomRecyclerView_setLayoutsHeight, LayoutParams.MATCH_PARENT)
                recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
                emptyBinding.constraintLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
            } finally {
                recycle()
            }
        }
    }

    fun init(
        hasFixedSize: Boolean = true,
        adapter: RecyclerView.Adapter<*>,
        layoutManager: RecyclerView.LayoutManager
    ) {
        recyclerView.apply {
            setHasFixedSize(hasFixedSize)
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    fun showRecyclerView(
        size: Int,
        emptyText: String = resources.getString(R.string.txt_list_is_empty)
    ) {
        if (size > 0) {
            recyclerView.visibility = View.VISIBLE
            emptyBinding.root.visibility = View.GONE
        } else {
            showEmptyView(emptyText)
        }
    }

    fun showEmptyView(
        title: String = resources.getString(R.string.txt_list_is_empty)
    ) {
        emptyText = title

        recyclerView.visibility = View.GONE
        emptyBinding.root.visibility = View.VISIBLE
    }
}