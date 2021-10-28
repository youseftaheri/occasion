package com.iranwebyar.occasions.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.pm.PackageInfoCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.iranwebyar.occasions.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object CommonUtils {

    @JvmStatic
    fun showMessageDialog(
        mContext: Context,
        message: String?,
        listener: DialogInterface.OnClickListener?
    ) {
        showAlertDialog(
            R.layout.simple_allert,
            mContext, "",
            message!!,
            mContext.getString(R.string.ok)
        )
    }

    fun showAlertDialog(
        layout: Int, context: Context,
        titleString: String, textString: String, buttonString: String
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val alertDialog: AlertDialog
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        val layoutView: View = inflater.inflate(layout, null)
        val title: TextView = layoutView.findViewById(R.id.title)
        val text: TextView = layoutView.findViewById(R.id.text)
        val dialogButton: Button = layoutView.findViewById(R.id.btnDialog)
        title.text = textString
        text.text = titleString
        text.visibility = if(titleString != "") View.VISIBLE else View.GONE
        dialogButton.text = buttonString
        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        dialogButton.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss() })
    }

    /**
     * Show toast message
     *
     * @param mContext Context of activity or fragment
     * @param message  Message that show into the Toast
     */
    @JvmStatic
    fun showToast(mContext: Context?, message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    @JvmStatic
    fun showLoadingDialog(context: Context?): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val wlmp = Objects.requireNonNull(progressDialog.window)?.attributes
            if (wlmp != null) {
                wlmp.gravity = Gravity.CENTER_HORIZONTAL
            }
            progressDialog.window!!.attributes = wlmp
        }
        progressDialog.setContentView(R.layout.progress_spinner)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    /**
     * Get Real path while getting image from SD card or internal memory
     *
     * @param mContext Avtivity or Fragment Context
     * @param uri      Uri of file
     * @return String path of file
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    fun getRealPathFromURI(
        mContext: Context?,
        uri: Uri
    ): String? {
        val isKitKat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory()
                        .toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return mContext?.let {
                    getDataColumn(
                        it,
                        contentUri,
                        null,
                        null
                    )
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return mContext?.let {
                    getDataColumn(
                        it,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else mContext?.let {
                getDataColumn(
                    it,
                    uri,
                    null,
                    null
                )
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
        //  return cursor.getString(column_index);
    }


    /**
     * Show Log
     *
     * @param message Message that want to show into Log
     */
    fun showLog(message: String) {
        Log.e("Log Message", "" + message)
    }

    /**
     * Goto previous Activity with animation
     *
     * @param mContext Context of the Activity.
     */
    fun gotoPreviousActivityAnimation(mContext: Context) {
        (mContext as Activity).overridePendingTransition(
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
    }

    /**
     * Goto next Activity with animation
     *
     * @param mContext Context of the Activity.
     */
    @JvmStatic
    fun gotoNextActivityAnimation(mContext: Context) {
        (mContext as Activity).overridePendingTransition(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left
        )
//        (mContext as Activity).overridePendingTransition( 0, R.anim.fade_activity );

    }

    /**
     * To get current app version
     *
     * @param mContext context of activity or fragment
     */
    @JvmStatic
    fun getAndroidVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * To get current app version
     *
     * @param mContext context of activity or fragment
     */
    @JvmStatic
    fun getAppVersion(mContext: Context): Int {
//        String version = "";
//        try {
//            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
//            version = pInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return version;
        var versionCode = 0
        try {
            val pInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            val longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
            versionCode = longVersionCode.toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     * To get current app version name
     *
     * @param mContext context of activity or fragment
     */
    fun getAppVersionName(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }

    /**
     * To add textWatcher on Edittext(Prevent First letter as space)
     *
     * @param editText all EditTexts that you want to add
     */
    @JvmStatic
    fun setTextWatcherWithSpace(vararg editText: EditText) {
        for (et in editText) {
            et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = s.toString()
                    if (str.length > 0 && str.startsWith(" ")) {
                        et.setText(str.trim { it <= ' ' })
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    /**
     * Hide Soft Keyboard
     *
     * @param mContext Context of the Activity or Fragment.
     * @param view     Current focus of View
     */
    @JvmStatic
    fun hideKeyboard(mContext: Context, view: View?) {
        if (view != null) {
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * To get time from long time duration
     *
     * @param duration int duration
     * @return return time in hh:mm:ss
     */
    @JvmStatic
    fun getTimeInMinutes(duration: Int): String {
        var hour = ""
        var min = ""
        var sec = ""
        sec = if (TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration.toLong()
                )
            ) < 10) {
            "0" + (TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration.toLong()
                )
            ))
        } else {
            "" + (TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration.toLong()
                )
            ))
        }
        min = if (TimeUnit.MILLISECONDS.toMinutes(duration.toLong()) < 10) {
            "0" + TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        } else {
            "" + TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        }
        hour = if (TimeUnit.MILLISECONDS.toHours(duration.toLong()) < 10) {
            "0" + TimeUnit.MILLISECONDS.toHours(duration.toLong())
        } else {
            "" + TimeUnit.MILLISECONDS.toHours(duration.toLong())
        }
        return if (hour.equals("00", ignoreCase = true)) {
            "$min:$sec"
        } else {
            "$hour:$min:$sec"
        }
    }

    /**
     * Load image from url into imageview
     *
     * @param mContext    Context of Activity or Fragment
     * @param url         Url that want to load into Imageview
     * @param imageView   ImageView in which url loads
     * @param placeholder Drawable image while loading image from Url
     */
    @JvmStatic
    fun loadImage(mContext: Context?, url: String?, imageView: ImageView?, placeholder: Int) {
        Glide.with(mContext!!).load(url).apply(RequestOptions().placeholder(placeholder)).diskCacheStrategy(
            DiskCacheStrategy.RESOURCE
        ).into(imageView!!)
    }

    fun loadImageWithoutCash(
        mContext: Context?,
        url: String?,
        imageView: ImageView?,
        placeholder: Int
    ) {
        Glide.with(mContext!!).load(url).apply(RequestOptions().placeholder(placeholder)).diskCacheStrategy(
            DiskCacheStrategy.NONE
        ).skipMemoryCache(true).into(imageView!!)
    }

    @JvmStatic
    fun isExist(data: String?): Boolean {
        return data != null && !data.isEmpty()
    }

    @JvmStatic
    fun getIfExists(data: String?): String? {
        return if (isExist(data)) data else "0"
    }

    fun convertLessThanOneThousand(number: Int, numNames: Array<String>): String {
        var soFar = ""
        if (number % 100 <= 20) {
            soFar = numNames[number % 100]
        } else {
            if (number % 10 == 0) {
                soFar = numNames[number % 100 / 10 + 18]
            } else {
                soFar = numNames[number % 10] + soFar
                soFar = numNames[number % 100 / 10 + 18] + " و " + soFar
            }
        }
        if (number / 100 != 0) soFar = if (number % 100 != 0) numNames[number / 100 + 27] + " و " + soFar else numNames[number / 100 + 27] + soFar
        return soFar
    }

    @JvmStatic
    fun faToEn(num: String): String {
        return num
            .replace("۰", "0")
            .replace("۱", "1")
            .replace("۲", "2")
            .replace("۳", "3")
            .replace("۴", "4")
            .replace("۵", "5")
            .replace("۶", "6")
            .replace("۷", "7")
            .replace("۸", "8")
            .replace("۹", "9")
    }

    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     * @param url      Url that you want to open in Browser
     */
    @JvmStatic
    fun intentToBrowser(mContext: Context, url: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            mContext.startActivity(Intent.createChooser(intent, "Go To : "))
        } catch (e: Exception) {
        }
    }
    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     */
    //    public static void intentToPostBrowser(Context mContext, String amount, String course_id,
    //                                           String test_id, String random) {
    //        try {
    //            String uri = Uri.parse(Const.PAYMENT_URL + "online_payment")
    //                    .buildUpon()
    //                    .appendQueryParameter("amount", amount)
    //                    .appendQueryParameter("course_id", course_id)
    //                    .appendQueryParameter("test_id", test_id)
    //                    .appendQueryParameter("academy_id", Const.getSelectedSchoolId(mContext))
    //                    .appendQueryParameter("national_code", Const.getMelliCode(mContext))
    //                    .build().toString();
    //            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    //            mContext.startActivity(Intent.createChooser(intent, "Go To : "));
    //        } catch (Exception e) {
    //
    //        }
    //    }
    /**
     * To convert string to Base64Encode
     *
     * @param text string
     * @return Base64 encoded string
     */
    fun convertBase64Encode(text: String): String {
        return Base64.encodeToString(text.toByteArray(), Base64.NO_WRAP)
    }

    val randomNumber: String
        get() {
            val rand = Random()
            var n = rand.nextInt(500000).toLong()
            n = +System.currentTimeMillis()
            return n.toString()
        }

    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     */
    @JvmStatic
    fun intentToPostBrowser(
        mContext: Context, amount: String?, selectedItem: String?
    ) {
        try {
            val uri = Uri.parse(Const.PAYMENT_URL + "online_payment")
                .buildUpon()
                .appendQueryParameter("amount", amount)
                .appendQueryParameter("course_id", selectedItem)
                .build().toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mContext.startActivity(Intent.createChooser(intent, "Go To : "))
        } catch (e: Exception) {
        }
    }

    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     */
    @JvmStatic
    fun intentToPostBrowserToSellPerfectMoney(
        mContext: Context, amount: String?, username: String?, password: String?, deviceId: String?
    ) {
        try {
            val uri = Uri.parse(
                Const.PERFECT_MONEY_URL +
                        convertBase64Encode(
                            convertBase64Encode(
                                convertBase64Encode(
                                    convertBase64Encode(
                                        username!!
                                    )
                                )
                            )
                        ) + "/" +
                        convertBase64Encode(
                            convertBase64Encode(
                                convertBase64Encode(
                                    convertBase64Encode(
                                        password!!
                                    )
                                )
                            )
                        ) + "/" +
                        convertBase64Encode(
                            convertBase64Encode(
                                convertBase64Encode(
                                    convertBase64Encode(
                                        amount!!
                                    )
                                )
                            )
                        ) + "/" +
                        convertBase64Encode(
                            convertBase64Encode(
                                convertBase64Encode(
                                    convertBase64Encode(
                                        deviceId!!
                                    )
                                )
                            )
                        ) + "/"
            )
                .buildUpon()
                .build().toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mContext.startActivity(Intent.createChooser(intent, "Go To : "))
        } catch (e: Exception) {
        }
    }

    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     */
    @JvmStatic
    fun intentToPostBrowserToIncreaseWallet(
        mContext: Context, gatewayUrl: String?
    ) {
        try {
            val uri = Uri.parse(gatewayUrl)
                .buildUpon()
                .build().toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mContext.startActivity(Intent.createChooser(intent, "Go To : "))
        } catch (e: Exception) {
        }
    }

    /**
     * Simple Browser Intent
     *
     * @param mContext Context of the Activity or Fragment.
     */
    @JvmStatic
    fun intentToPostBrowserToPurchaseCurrency(
        mContext: Context, gatewayUrl: String?
    ) {
        try {
            val uri = Uri.parse(gatewayUrl)
                .buildUpon()
                .build().toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mContext.startActivity(Intent.createChooser(intent, "Go To : "))
        } catch (e: Exception) {
        }
    }


    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr: String = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        } // for now eat exceptions
        return ""
    }

    //<-------------------------------------------------------------------------------->\\
    //Get basic multipart api content
    //<-------------------------------------------------------------------------------->\\
    fun getImageRequest(filePath: String?, fileName: String): MultipartBody.Part {
        val zipped_folderFile =  File(filePath!!).toString().toRequestBody("image/jpg".toMediaTypeOrNull())

//            RequestBody.create(
//            "image/jpg".toMediaTypeOrNull(), File(
//                filePath!!
//            )
//        )
        return MultipartBody.Part.createFormData(fileName, File(filePath).name, zipped_folderFile)
    }

    fun getFileRequest(filePath: String?): RequestBody {
        return File(filePath!!).toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), File(filePath!!))
    }

    fun getFileRequest1(filePath: String?, fileName: String): MultipartBody.Part {
        val zipped_folderFile = File(filePath!!).toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            RequestBody.create(
//            "multipart/form-data".toMediaTypeOrNull(), File(
//                filePath!!
//            )
//        )
        return MultipartBody.Part.createFormData(fileName, File(filePath).name, zipped_folderFile)
    }

    fun getPlaneRequest(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
//        RequestBody.create(
//            "text/plain".toMediaTypeOrNull(), value)
    }

    fun getFileKey(key: String, filePath: String): String {
        return "$key\";filename=\"$filePath"
    }

    @JvmName("getRandomNumber1")
    fun getRandomNumber(): String {
        val rand = Random()
        var n = rand.nextInt(500000).toLong()
        n = +System.currentTimeMillis()
        return n.toString()
    }


    //<-------------------------------------------------------------------------------->\\
    //Return name without Parentheses
    //<-------------------------------------------------------------------------------->\\
    @JvmStatic
    fun removeParentheses(string: String?): String {
        val split = string!!.split('(').toTypedArray()
        return split[0]
    }

    //<-------------------------------------------------------------------------------->\\
    //Return formatted price
    //<-------------------------------------------------------------------------------->\\
    @JvmStatic
    fun formatPrice(price: String?): String? {
        var formatter //to use for price format
                : DecimalFormat? = null
        formatter = DecimalFormat("#,###,###,###")
        return formatter.format(java.lang.Long.valueOf(price!!))
    }

    @SuppressLint("SimpleDateFormat")
    fun generateDeviceId():String{
        val randomString = UUID.randomUUID().toString()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        val datetime: String = ft.format(Date())
        return convertBase64Encode(randomString + datetime)
    }
}