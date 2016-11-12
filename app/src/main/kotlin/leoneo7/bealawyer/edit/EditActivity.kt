package leoneo7.bealawyer.edit

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import butterknife.bindView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.base.Category
import leoneo7.bealawyer.base.Entry
import leoneo7.bealawyer.helper.CategoryDialogFragment
import leoneo7.bealawyer.helper.Const.Companion.ENTRY
import leoneo7.bealawyer.helper.Const.Companion.REQUEST_CHOOSER
import leoneo7.bealawyer.helper.DBAdapter
import leoneo7.bealawyer.helper.MenuHelper
import leoneo7.bealawyer.main.EntryActivity
import java.util.*

/**
 * Created by ryouken on 2016/11/01.
 */
class EditActivity : AppCompatActivity() {

    val mNavigationView: NavigationView by bindView(R.id.navigation_view)
    val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val mToolBar: Toolbar by bindView(R.id.tool_bar)
    val titleText: EditText by bindView(R.id.title)
    val imageView: ImageView by bindView(R.id.imageView)
    val numberingText: EditText by bindView(R.id.numbering)
    val categoryButton: Button by bindView(R.id.categoryButton)
    val categoryText: TextView by bindView(R.id.category_text)
    val deleteButton: Button by bindView(R.id.deleteButton)
    val cameraButton: Button by bindView(R.id.cameraButton)
    val galleryButton: Button by bindView(R.id.galleryButton)
    val saveButton: FloatingActionButton by bindView(R.id.saveButton)
    val layoutBox: LinearLayout by bindView(R.id.layoutBox)
    private var entry: Entry? = null
    private var entryId: Int? = null
    private var image: String? = null
    private var mUri: Uri? = null
    private var mCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit)
        ButterKnife.bind(this)

        setupToolBar()
        MenuHelper.onClickMenu(this, mNavigationView)

        setupData()
        setupListener()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle(R.string.no_save_yet)
                .setPositiveButton("OK") { dialog, whichButton ->  super.onBackPressed()}
                .setNegativeButton("キャンセル") { dialog, whichButton -> }
                .show()
    }

    private fun setupListener() {
        categoryButton.setOnClickListener(categoryButtonListener)
        categoryText.setOnClickListener(categoryButtonListener)
        deleteButton.setOnClickListener(deleteButtonListener)
        cameraButton.setOnClickListener(cameraButtonListener)
        galleryButton.setOnClickListener(galleryButtonListener)
        saveButton.setOnClickListener(saveButtonListener)
    }

    private fun setupData() {
        entry = intent.getParcelableExtra(ENTRY) ?: return

        entry.let {
            entryId = entry!!.entryId
            titleText.setText(entry!!.title)
            numberingText.setText(entry!!.numbering)
            mCategory = Category(entry!!.categoryId, entry!!.categoryName, 0)
            categoryText.text = entry!!.categoryName
            image = entry!!.image
        }

        val uri: Uri?
        if (image != null) uri = Uri.parse(image)
        else uri = null
        if (uri != null) {
            imageView.setImageURI(uri)
            imageView.visibility = View.VISIBLE
            layoutBox.visibility = View.GONE
        }
    }

    fun setCategory(category: Category) {
        mCategory = category
        categoryText.text = category.name
    }

    val categoryButtonListener = View.OnClickListener {
        val dialogFragment = CategoryDialogFragment()
        dialogFragment.show(fragmentManager, "category")
    }

    val deleteButtonListener = View.OnClickListener {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm)
                .setPositiveButton("OK") { dialog, whichButton ->
                    if (entry == null) {
                        super.onBackPressed()
                    } else {
                        deleteEntry(entry!!)
                        val intent = Intent(this, EntryActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("キャンセル") { dialog, whichButton -> }
                .show()
    }

    val cameraButtonListener = View.OnClickListener {
        useCamera()
    }

    val galleryButtonListener = View.OnClickListener {
        useGallery()
    }

    val saveButtonListener = View.OnClickListener {
        val title = titleText.text.toString()
        if (title.isEmpty()) {
            showAlertDialog(getString(R.string.no_title_error))
            return@OnClickListener
        } else if (categoryText.text == null) {
            showAlertDialog(getString(R.string.no_category_error))
            return@OnClickListener
        }

        val numbering = numberingText.text.toString()
        val calendar = Calendar.getInstance()
        val date = calendar.timeInMillis
        val categoryId = mCategory!!.id

        if (entry != null) {
            entry = Entry(entryId!!, title, image, numbering, 0, date, categoryId, mCategory!!.name)
            updateEntry(entry!!)
        } else {
            entry = Entry(-1, title, image, numbering, 0, date, categoryId, mCategory!!.name)
            saveEntry(entry!!)
        }

        moveToView()
    }

    private fun moveToView() {
        val intent = Intent(this, ViewActivity::class.java)
        intent.putExtra(ENTRY, entry)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        this.startActivity(intent)
    }

    private fun showAlertDialog(text: String) {
        AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("OK") { dialog, whichButton -> }
                .show()
    }

    private fun saveEntry(entry: Entry) {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        dbAdapter.saveEntry(entry)
        dbAdapter.close()
    }

    private fun updateEntry(entry: Entry) {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        dbAdapter.updateEntry(entry)
        dbAdapter.close()
    }

    private fun deleteEntry(entry: Entry) {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        dbAdapter.deleteEntry(entry.entryId)
        dbAdapter.close()
    }

    private fun useCamera() {
        val photoName: String
        val title: String? = titleText.text.toString()
        if (title != null)
            photoName = "BeaLawyer/$title.jpg"
        else
            photoName = "BeaLawyer/" + System.currentTimeMillis() + ".jpg"

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        mUri = contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intentCamera, REQUEST_CHOOSER)
    }

    private fun useGallery() {
        // ギャラリー用のIntent作成
        val intentGallery: Intent
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = Intent(Intent.ACTION_GET_CONTENT)
            intentGallery.type = "image/*"
        } else {
            intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
            intentGallery.type = "image/*"
        }
        startActivityForResult(intentGallery, REQUEST_CHOOSER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }

            val resultUri = if (data.data != null) data.data else mUri
            if (resultUri == null)
                return
            else
                image = resultUri.toString()

            MediaScannerConnection.scanFile(this, arrayOf(resultUri.path),
                    arrayOf("image/jpeg"), null)

            imageView.setImageURI(resultUri)

            imageView.visibility = View.VISIBLE
            layoutBox.visibility = View.GONE
        }
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar, "")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
