package com.stavro_xhardha.pockettreasure.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import com.stavro_xhardha.pockettreasure.R
import kotlinx.android.synthetic.main.fragment_full_image.*
import java.lang.Exception
import java.net.URL
import android.graphics.BitmapFactory
import java.io.IOException
import android.media.MediaScannerConnection
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

const val REQUEST_STORAGE_PERMISSION = 355


class FullImageFragment : Fragment() {

    val args: FullImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val urlToExpect = args.imageUrl

        if (urlToExpect.isNotEmpty()) {
            Picasso.get().load(urlToExpect)
                .into(ivFullImage, object : Callback {
                    override fun onSuccess() {
                        pbFullImage.visibility = View.GONE
                        setHasOptionsMenu(true)
                    }

                    override fun onError(e: Exception?) {
                        pbFullImage.visibility = View.GONE
                        Picasso.get().load(R.drawable.ic_error_in_connection)
                            .into(ivFullImage)
                    }

                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.image_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_download) {
            saveImageToUrl()
        } else if (item.itemId == R.id.action_share) {
            shareImageUrl()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveImageToUrl() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        activity!!.applicationContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
                } else {
                    writeImageToFile()
                }
            } else {
                writeImageToFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeImageToFile()
                } else {
                    Toast.makeText(activity, "Can't save image, need permission", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun writeImageToFile() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL(args.imageUrl)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            MediaStore.Images.Media.insertImage(
                activity!!.contentResolver,
                image,
                System.currentTimeMillis().toString(),
                ""
            )
            withContext(Dispatchers.Main) {
                Snackbar.make(rlFullImageHolder, "Image Saved", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun shareImageUrl() {
        val image = args.imageUrl
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, image)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
}
