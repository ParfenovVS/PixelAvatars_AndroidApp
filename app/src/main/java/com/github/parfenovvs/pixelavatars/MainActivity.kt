package com.github.parfenovvs.pixelavatars

import android.content.Intent
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.parfenovvs.pixelavatars.model.AvatarStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_avatar.view.*
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestBuilder
import com.github.parfenovvs.pixelavatars.common.glidesvg.SvgSoftwareLayerSetter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade


class MainActivity : AppCompatActivity() {

    private val avatarStorage by lazy { AvatarStorage(applicationContext) }
    private val adapter by lazy { AvatarsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter

        showAvatars()

        addButton.setOnClickListener { _ ->
            AvatarGenerateFragment().apply {
                onGenerateClickListener = { spriteType, seed ->
                    avatarStorage.saveAvatar(spriteType, seed)
                    dismissAllowingStateLoss()
                    showAvatars()
                }
                show(supportFragmentManager, null)
            }
        }
    }

    private fun showAvatars() {
        val avatars = avatarStorage.getAvatars()
        if (avatars.isEmpty()) {
            placeholderTextView.visibility = VISIBLE
            recyclerView.visibility = GONE
            return
        }
        placeholderTextView.visibility = GONE
        recyclerView.visibility = VISIBLE
        adapter.setItems(avatars.toList())
    }

    private fun share(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_with)))
    }

    inner class AvatarsAdapter : RecyclerView.Adapter<AvatarsAdapter.ViewHolder>() {
        private val avatarSize: Size = with(DisplayMetrics()) {
            windowManager.defaultDisplay.getMetrics(this)
            val width = widthPixels / (recyclerView.layoutManager as GridLayoutManager).spanCount
            Size(width, width)
        }
        private val imageLoader: RequestBuilder<PictureDrawable> = Glide.with(this@MainActivity)
            .`as`(PictureDrawable::class.java)
            .transition(withCrossFade())
            .listener(SvgSoftwareLayerSetter())

        private val items = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avatar, parent, false)
            view.layoutParams.apply {
                width = avatarSize.width
                height = avatarSize.height
            }
            view.requestLayout()
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        fun setItems(items: List<String>) {
            this.items.apply {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(url: String) {
                val uri = Uri.parse(url)
                imageLoader
                    .load(uri)
                    .into(itemView.avatarImageView)
                itemView.setOnClickListener { share(url) }
            }
        }
    }
}
