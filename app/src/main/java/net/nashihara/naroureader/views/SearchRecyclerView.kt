package net.nashihara.naroureader.views

import net.nashihara.naroureader.entities.NovelItem

interface SearchRecyclerView {

    fun showRecyclerView(novelItems: List<NovelItem>?)

    fun showError()
}
