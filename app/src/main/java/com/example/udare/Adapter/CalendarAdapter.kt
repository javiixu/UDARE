package com.example.udare.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udare.R
import com.example.udare.data.model.CalendarData
import java.text.DateFormat
import java.text.SimpleDateFormat


class CalendarAdapter (private val calendarItemList: List<CalendarData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val PORTRAIT = 0
    private val LANDSCAPE = 1

    inner class ViewHolderPortrait(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //PORTRAIT items
        var ivCalendarViewerPortrait = itemView.findViewById<ImageView>(R.id.ivCalendarViewerPortrait)
        var tvChallengeDateCalendarPortrait = itemView.findViewById<TextView>(R.id.tvChallengeDateCalendarPortrait)
        var ivCategoryIconCalendarPortrait = itemView.findViewById<ImageView>(R.id.ivCategoryIconCalendarPortrait)
    }

    inner class ViewHolderLandscape(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //LANDSCAPE items
        var ivCalendarViewerLandscape = itemView.findViewById<ImageView>(R.id.ivCalendarViewerLandscape)
        var tvChallengeDateCalendarLandscape = itemView.findViewById<TextView>(R.id.tvChallengeDateCalendarLandscape)
        var ivCategoryIconCalendarLandscape = itemView.findViewById<ImageView>(R.id.ivCategoryIconCalendarLandscape)
    }


    override fun getItemViewType(position : Int) : Int {
        if(calendarItemList[position].orientation.equals("portrait")){
            Log.d("tag-calendar-adapter","PORTRAIT")
            return PORTRAIT
        }
        else{
            Log.d("tag-calendar-adapter","LANDSCAPE")
            return LANDSCAPE
        }
    }




    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the layout (either landscape or portrait)
        if(viewType == LANDSCAPE){
            val calendarView =  inflater.inflate(R.layout.item_calendar_landscape, parent, false)
            return ViewHolderLandscape(calendarView)
        }
        else{
            val calendarView = inflater.inflate(R.layout.item_calendar_portrait, parent, false)
            return ViewHolderPortrait(calendarView)
        }




    }



    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // Get the data model based on position
        val calendarItem: CalendarData = calendarItemList.get(position)

        val standardSize = 1200 // Tamaño estándar en píxeles

        if(viewHolder.itemViewType == PORTRAIT){
            var ivCalendarViewer = (viewHolder as ViewHolderPortrait).ivCalendarViewerPortrait

            //TODO check if the override works here
            val options = RequestOptions()
                .override(standardSize, standardSize)
                .centerCrop()

            //Set the image in the Calendar
            Glide.with(ivCalendarViewer)
                .load(calendarItem.post.image)
                .apply(options)
                .into(ivCalendarViewer)

            //set the icon
            setIcon(calendarItem,viewHolder)

            //set the correct date
            val df: DateFormat = SimpleDateFormat("dd / MM / yy")
            viewHolder.tvChallengeDateCalendarPortrait.text= df.format(calendarItem.post.date)
        }
        else{
            var ivCalendarViewer = (viewHolder as ViewHolderLandscape).ivCalendarViewerLandscape

            //TODO check if the override works here
            val options = RequestOptions()
                .override(standardSize, standardSize)
                .centerCrop()

            //Set the image in the Calendar
            Glide.with(ivCalendarViewer)
                .load(calendarItem.post.image)
                .apply(options)
                .into(ivCalendarViewer)

            //set the icon
            setIcon(calendarItem,viewHolder)

            //set the correct date
            val df: DateFormat = SimpleDateFormat("dd / MM / yy")
            viewHolder.tvChallengeDateCalendarLandscape.text= df.format(calendarItem.post.date)
        }

    }



    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return calendarItemList.size
    }

    fun setIcon(calendarItem : CalendarData, viewHolder: ViewHolder) {
        when (calendarItem.challenge.category) {
            "deportes" -> {
                if(viewHolder.itemViewType == PORTRAIT){
                (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_deporte)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_deporte)
                }
            }
            "social" -> {
                if(viewHolder.itemViewType == PORTRAIT){
                    (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_social)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_social)
                }
            }
            "cultura" -> {
                if(viewHolder.itemViewType == PORTRAIT){
                    (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_cultural)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_cultural)}
            }
            "cocina" -> {
                if(viewHolder.itemViewType == PORTRAIT){
                    (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_cocina)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_cocina)}
            }
            "crecimientopersonal" -> {
                if(viewHolder.itemViewType == PORTRAIT){
                    (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_crec_pers)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_crec_pers)}
            }
            else -> {
                if(viewHolder.itemViewType == PORTRAIT){
                    (viewHolder as ViewHolderPortrait).ivCategoryIconCalendarPortrait.setImageResource(R.drawable.icono_social)}
                else{ (viewHolder as ViewHolderLandscape).ivCategoryIconCalendarLandscape.setImageResource(R.drawable.icono_social)}
            }
        }
    }
}