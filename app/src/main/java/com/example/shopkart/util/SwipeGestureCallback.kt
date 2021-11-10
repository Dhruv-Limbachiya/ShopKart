package com.example.shopkart.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By Dhruv Limbachiya on 10-11-2021 11:41 AM.
 */
abstract class SwipeGestureCallback(private val icon: Drawable, private val direction: Int) : ItemTouchHelper.SimpleCallback(0, direction) {

    private val intrinsicWidth = icon.intrinsicWidth
    private val intrinsicHeight = icon.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = when(direction) {
        ItemTouchHelper.LEFT -> Color.RED
        ItemTouchHelper.RIGHT -> Color.GREEN
        else -> Color.RED
    }

    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        val itemViewDirection: Int = when (direction) {
            ItemTouchHelper.LEFT -> itemView.right
            ItemTouchHelper.RIGHT -> itemView.left
            else -> 0
        }

        if (isCanceled) {
            clearCanvas(c, itemViewDirection + dX, itemView.top.toFloat(), itemViewDirection.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemViewDirection + dX.toInt(), itemView.top, itemViewDirection, itemView.bottom)
        background.draw(c)

        // Calculate position of icon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft =  when (direction) {
            ItemTouchHelper.LEFT ->   itemViewDirection - iconMargin - intrinsicWidth
            ItemTouchHelper.RIGHT ->  itemViewDirection + iconMargin - intrinsicWidth
            else -> 0
        }

        val iconRight = when (direction) {
                ItemTouchHelper.LEFT ->   itemViewDirection - iconMargin
                ItemTouchHelper.RIGHT ->  itemViewDirection  + iconMargin
                else -> 0
            }

        val iconBottom = iconTop + intrinsicHeight

        // Draw the delete icon
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }


    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}