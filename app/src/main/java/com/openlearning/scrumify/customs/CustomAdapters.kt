package com.openlearning.scrumify.customs

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.openlearning.scrumify.R


@BindingAdapter("android:arrayList")
fun setArrayList(spinner: Spinner, arrayList: ArrayList<String>) {

    val adapter = ArrayAdapter(
        spinner.context,
        R.layout.support_simple_spinner_dropdown_item,
        arrayList
    )

    spinner.adapter = adapter
}

@BindingAdapter("android:itemIndexSelector")
fun itemIndexSelector(spinner: Spinner, liveData: MutableLiveData<Int>?) {

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            liveData?.value = position
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@BindingAdapter("android:imgSrcDownloadUri")
fun setImageUri(view: ImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .into(view)
}

@BindingAdapter("android:imgSrcURI")
fun setImageUri(view: ImageView, url: Uri?) {

    if (url == null) return
    Glide.with(view)
        .load(url)
        .into(view)
}

@BindingAdapter("android:imgSrcBitmap")
fun setImageUri(view: ImageView, bitmap: Bitmap?) {

    Glide.with(view)
        .load(bitmap)
        .into(view)
}


@BindingAdapter("android:latLong")
fun getLatLong(view: TextView, location: Location?) {

    if (location?.latitude != 0.0 && location?.longitude != 0.0) {
        val s = "lat: ${location?.latitude} long: ${location?.longitude}"
        view.text = s
    }

}
