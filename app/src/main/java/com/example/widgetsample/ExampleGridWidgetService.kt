package com.example.widgetsample

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class ExampleGridWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GridRemoteViewFactory(applicationContext, intent)
    }
}

class GridRemoteViewFactory(context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory {
    companion object {
        const val WIDGET_SIZE = 10;
    }

    private val context: Context
    private val appWidgetId: Int
    private val widgetItems: MutableList<WidgetItem> = mutableListOf()

    init {
        this.context = context
        appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate() {
        for (i in 0 until WIDGET_SIZE) {
            widgetItems.add(WidgetItem("Widget $i"))
        }

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onDataSetChanged() {
        Log.d("KKS", "onDataSetChanged")
    }

    override fun onDestroy() {
        widgetItems.clear()
    }

    override fun getCount(): Int = WIDGET_SIZE

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.appwidget_grid_item).apply {
            setTextViewText(R.id.item_title, widgetItems[position].text)
            Log.d("KKS", "widgetItem[$position] = ${widgetItems[position].text}")
            setOnClickFillInIntent(R.id.widget_item, Intent().apply {
                Bundle().also { extras ->
                    extras.putInt(ExampleAppWidgetProvider.EXTRA_ITEM, position)
                    putExtras(extras)
                }
            })
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

}
