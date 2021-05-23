package com.openlearning.scrumify.customs

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.common.base.Enums
import com.openlearning.scrumify.R
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.models.TaskPriority


@BindingAdapter("android:arrayList")
fun setArrayList(spinner: Spinner, arrayList: ArrayList<String>) {

    val adapter = ArrayAdapter(
        spinner.context,
        R.layout.support_simple_spinner_dropdown_item,
        arrayList
    )

    spinner.adapter = adapter
}

@BindingAdapter("android:enumList")
fun setEnumList(spinner: Spinner, enumClass: Class<*>) {

    val adapter = ArrayAdapter(
        spinner.context,
        R.layout.support_simple_spinner_dropdown_item,
        enumClass.enumConstants
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

@BindingAdapter("app:userRoleImage")
fun imageForRole(view: ImageView, role: ROLES) {

    when (role) {

        ROLES.ADMINISTRATOR -> view.setImageResource(R.drawable.ic_admin)
        ROLES.SCRUM_MASTER -> view.setImageResource(R.drawable.ic_scrum_master)
        ROLES.TEAM_MEMBER -> view.setImageResource(R.drawable.ic_team_member)
    }
}
