package com.starters.hsge.presentation.main.find

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.starters.hsge.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
    val mCalloutBalloon: View = inflater.inflate(R.layout.item_map_balloon, null)
    val name: TextView = mCalloutBalloon.findViewById(R.id.tv_other_person_nickname)

    override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
        // 마커 클릭 시 나오는 말풍선
        name.text = poiItem?.itemName
        return mCalloutBalloon
    }

    override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
        return mCalloutBalloon
    }
}