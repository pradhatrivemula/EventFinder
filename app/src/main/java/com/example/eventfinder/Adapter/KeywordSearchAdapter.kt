package com.example.eventfinder.Adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.eventfinder.Model.KeywordApi

class KeywordSearchAdapter(context: Context, private val resource: Int) : ArrayAdapter<String>(context, resource), Filterable {
    private var results: ArrayList<String> = ArrayList()
    private val keywordApi = KeywordApi()

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(pos: Int): String {
        return results[pos]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    results = keywordApi.autoComplete(constraint.toString())
                    filterResults.values = results
                    filterResults.count = results.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}