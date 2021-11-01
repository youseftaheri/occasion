package com.iranwebyar.occasions.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.remote.Events.ImagePickerResult
import com.iranwebyar.occasions.ui.imagePicker.ImagePickerActivity
import com.iranwebyar.occasions.utils.CommonUtils.getRealPathFromURI
import com.iranwebyar.occasions.utils.CommonUtils.showToast
import com.google.android.material.internal.ContextUtils.getActivity
//import id.zelory.compressor.Compressor
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException
import java.util.*


object MyImagePicker {
    private var cameraClickPath = ""
    private const val REQUEST_CODE_GALLARY = 1001
    private const val REQUEST_CODE_CAMERA = 1002
    const val REQUEST_IMAGE = 100

    @SuppressLint("NewApi")
    fun selectImage(mContext: Context) {
        val dialog = Dialog(mContext, R.style.themeDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_picker)
        dialog.setCancelable(true)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<View>(R.id.llCamera)
            .setOnClickListener { v: View? ->
                dialog.dismiss()
                getCameraPermission(mContext)
            }
        dialog.findViewById<View>(R.id.llGallery)
            .setOnClickListener { v: View? ->
                dialog.dismiss()
                getGalleryPermission(mContext)
            }
        dialog.show()
    }

    private fun getGalleryPermission(mContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                (mContext as Activity).requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_GALLARY
                )
            } else {
                launchGalleryIntent(mContext)
                //    gotoGallery(mContext);
            }
        } else {
            launchGalleryIntent(mContext)

            //     gotoGallery(mContext);
        }
    }

    private fun getCameraPermission(mContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                (mContext as Activity).requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE_CAMERA
                )
            } else {
                launchCameraIntent(mContext)
                //   gotoCamera(mContext);
            }
        } else {
            launchCameraIntent(mContext)
            //gotoCamera(mContext);
        }
    }

    fun onRequestPermissionsResult(
        mContext: Context,
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_GALLARY) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGalleryIntent(mContext)
                //  gotoGallery(mContext);
            } else {
                showToast(
                    mContext,
                    mContext.getString(R.string.gallery_permission_required)
                )
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    launchCameraIntent(mContext)
                    //   gotoCamera(mContext);
                } else {
                    showToast(
                        mContext,
                        mContext.getString(R.string.camera_permission_required)
                    )
                }
            } else {
                showToast(
                    mContext,
                    mContext.getString(R.string.camera_permission_required)
                )
            }
        }
    }

    @SuppressLint("IntentReset")
    fun gotoGallery(mContext: Context) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        (mContext as Activity).startActivityForResult(
            Intent.createChooser(intent, "Go To : "),
            REQUEST_CODE_GALLARY
        )
    }

    @SuppressLint("RestrictedApi")
    fun gotoCamera(mContext: Context) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {
            val photoFile: File?
            try {
                photoFile = createImageFile(mContext)
                if (photoFile != null) {
                    val photoURI =
                        FileProvider.getUriForFile(mContext, getActivity(mContext)!!.packageName + ".provider", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    (mContext as Activity).startActivityForResult(
                        takePictureIntent,
                        REQUEST_CODE_CAMERA
                    )
                } else showToast(
                    mContext,
                    mContext.getString(R.string.failed_to_capture_image)
                )
            } catch (ex: Exception) {
                showToast(
                    mContext,
                    mContext.getString(R.string.failed_to_capture_image)
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(mContext: Context): File? {
        val imageFileName = "Image_" + System.currentTimeMillis() + "_"
        val storageDir =
            mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".png", storageDir)
        return if (image != null) {
            cameraClickPath = image.absolutePath
            image
        } else null
    }

    fun onActivityResult(
        mContext: Context,
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val chosenImageUri =
                    data.getParcelableExtra<Uri>("path")

                //     Uri chosenImageUri = data.getData();
                try {
                    var realPath =
                        getRealPathFromURI(
                            mContext,
                            chosenImageUri!!
                        )
                    val imageFile = File(realPath)
                    val compImage = imageFile
//                    val compImage = Compressor(mContext)
//                        .setMaxWidth(800)
//                        .setMaxHeight(600)
//                        .setQuality(90)
//                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                        .setDestinationDirectoryPath(
//                            Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES
//                            ).absolutePath
//                        )
//                        .compressToFile(imageFile)
                    if (compImage != null) {
                        realPath = compImage.absolutePath
                        if (imageFile.exists()) {
                            EventBus.getDefault()
                                .post(ImagePickerResult(realPath))
                        } else {
                            showToast(
                                mContext,
                                mContext.getString(R.string.failed_to_fetch)
                            )
                        }
                    } else {
                        showToast(
                            mContext,
                            mContext.getString(R.string.failed_to_fetch)
                        )
                    }
                } catch (e: Exception) {
                    showToast(
                        mContext,
                        mContext.getString(R.string.failed_to_fetch)
                    )
                }
            } else {
                showToast(
                    mContext,
                    mContext.getString(R.string.failed_to_fetch)
                )
            }
        }

        if (requestCode == REQUEST_CODE_GALLARY) {
            if (resultCode == Activity.RESULT_OK) {
                val chosenImageUri = data.data
                try {
                    var realPath =
                        getRealPathFromURI(
                            mContext,
                            chosenImageUri!!
                        )
                    val imageFile = File(realPath)
                    val compImage = imageFile
//                    val compImage = Compressor(mContext)
//                        .setMaxWidth(800)
//                        .setMaxHeight(600)
//                        .setQuality(90)
//                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                        .setDestinationDirectoryPath(
//                            Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES
//                            ).absolutePath
//                        )
//                        .compressToFile(imageFile)
                    if (compImage != null) {
                        realPath = compImage.absolutePath
                        if (imageFile.exists()) {
                            EventBus.getDefault()
                                .post(ImagePickerResult(realPath))
                        } else {
                            showToast(
                                mContext,
                                mContext.getString(R.string.failed_to_fetch)
                            )
                        }
                    } else {
                        showToast(
                            mContext,
                            mContext.getString(R.string.failed_to_fetch)
                        )
                    }
                } catch (e: Exception) {
                    showToast(
                        mContext,
                        mContext.getString(R.string.failed_to_fetch)
                    )
                }
            } else {
                showToast(
                    mContext,
                    mContext.getString(R.string.failed_to_fetch)
                )
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                val imageFile = File(cameraClickPath)
                var compImage: File? = null
                compImage = imageFile
//                try {
//                    compImage = Compressor(mContext)
//                        .setMaxWidth(800)
//                        .setMaxHeight(600)
//                        .setQuality(90)
//                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                        .setDestinationDirectoryPath(
//                            Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES
//                            ).absolutePath
//                        )
//                        .compressToFile(imageFile)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
                if (compImage != null) {
                    cameraClickPath = compImage.absolutePath
                    EventBus.getDefault()
                        .post(ImagePickerResult(cameraClickPath))
                } else {
                    showToast(
                        mContext,
                        mContext.getString(R.string.failed_to_capture_image)
                    )
                }
            } else {
                showToast(
                    mContext,
                    mContext.getString(R.string.failed_to_capture_image)
                )
            }
        }
    }

    private fun launchCameraIntent(mContext: Context) {
        val intent = Intent(mContext, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        (mContext as Activity).startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent(mContext: Context) {
        val intent = Intent(mContext, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        (mContext as Activity).startActivityForResult(intent, REQUEST_IMAGE)
    }
}