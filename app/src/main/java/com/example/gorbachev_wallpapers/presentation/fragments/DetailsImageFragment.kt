package com.example.gorbachev_wallpapers.presentation.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentDetailsImageBinding
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_details_image.*
import retrofit2.http.Url
import java.io.ByteArrayOutputStream


class DetailsImageFragment : BaseFragment(R.layout.fragment_details_image) {
	
	private var _binding: FragmentDetailsImageBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var botNav: BottomNavigationView
	
	private val args by navArgs<DetailsImageFragmentArgs>()
	
	var WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 101
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		
		_binding = FragmentDetailsImageBinding.bind(view)
		
		botNav = requireActivity().findViewById(R.id.bot_nav)
		botNav.visibility = View.GONE
		
		binding.imageDetailsMenu.clearFocus()
		
		(activity as AppCompatActivity).supportActionBar
		(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
		(activity as AppCompatActivity).supportActionBar?.apply {
			setDisplayShowHomeEnabled(true)
			setDisplayHomeAsUpEnabled(true)
			title = null
		}
		binding.toolbar.setNavigationOnClickListener {
			Navigation.findNavController(requireView())
				.navigate(R.id.action_detailsImageFragment_to_search_fr)
			botNav.visibility = View.VISIBLE
		}
		
		binding.apply {
			val photo = args.photo
			Glide.with(this@DetailsImageFragment)
				.load(photo.urls.full)
				.error(R.drawable.ic_baseline_error_24)
				.listener(object : RequestListener<Drawable> {
					override fun onLoadFailed(
						e: GlideException?,
						model: Any?,
						target: Target<Drawable>?,
						isFirstResource: Boolean
					): Boolean {
						imageDetailsProgressbar.isVisible = false
						return false
					}
					
					override fun onResourceReady(
						resource: Drawable?,
						model: Any?,
						target: Target<Drawable>?,
						dataSource: DataSource?,
						isFirstResource: Boolean
					): Boolean {
						imageDetailsProgressbar.isVisible = false
						return false
					}
				})
				.into(imageDetailsIV)
			
			val query = args.query
			imageDetailsQuery.text = query
		}
		
		
		binding.apply {
			var checkFullscreen = false
			imageFullscreenBtn.setOnClickListener {
				if (!checkFullscreen) {
					toolbar.isVisible = false
					imageDetailsMenu.isVisible = false
					imageFullscreenBtn.setImageResource(R.drawable.ic_fullscreen_exit)
					checkFullscreen = true
				} else {
					toolbar.isVisible = true
					imageDetailsMenu.isVisible = true
					imageFullscreenBtn.setImageResource(R.drawable.ic_fullscreen)
					checkFullscreen = false
				}
			}
		}
		
		binding.apply {
			
			val onClickBottomNavItem =
				BottomNavigationView.OnNavigationItemSelectedListener { item ->
					when (item.itemId) {
						R.id.details_menu_share -> {
							startRequest()
							return@OnNavigationItemSelectedListener true
						}
						R.id.details_menu_settings -> {
							// put your code here
							return@OnNavigationItemSelectedListener true
						}
						R.id.details_menu_info -> {
							// put your code here
							return@OnNavigationItemSelectedListener true
						}
					}
					false
				}
			imageDetailsMenu.setOnNavigationItemSelectedListener(onClickBottomNavItem)
		}
	}
	
	private fun getBitmapFromView(photo: ImageView): Bitmap? {
		val bitmap = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)
		val convas = Canvas(bitmap)
		photo.draw(convas)
		return bitmap
	}
	
	private fun getImageUrl(context: Context, photo: Bitmap): Uri? {
		
		val bytes = ByteArrayOutputStream()
		photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
		val path =
			MediaStore.Images.Media.insertImage(context.contentResolver, photo, "Title", null)
		return Uri.parse(path)
	}
	
	private fun sharePhoto(){
		val photo = getBitmapFromView(imageDetailsIV)
		val share = Intent(Intent.ACTION_SEND)
		share.type = "image/*"
		share.putExtra(Intent.EXTRA_STREAM, getImageUrl(requireContext(),photo!!))
		startActivity(Intent.createChooser(share, "Share"))
	}
	
	// request permission //////////////////////////////////
	
	private fun startRequest() {
		checkForPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "camera", WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
	}
	
	private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			when {
				ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED -> {
					sharePhoto()
				}
				shouldShowRequestPermissionRationale(permission) -> showDialog(permission,name, requestCode)
				
				else -> ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode)
			}
		}
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		fun innerCheck(name: String) {
			if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(requireActivity(),"$name permission refused", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(requireActivity(),"$name permission granted", Toast.LENGTH_SHORT).show()
				sharePhoto()
			}
		}
		innerCheck("camera")
	}
	
	
	private fun showDialog(permission: String, name:String, requestCode: Int){
		
		val bulder = AlertDialog.Builder(requireContext())
		
		bulder.apply {
			setMessage("Permission for access your $name")
			setTitle("Permission")
			setPositiveButton("Ok"){ _, _ ->
				ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission),requestCode)
			}
		}
		val dialog = bulder.create()
		dialog.show()
	}
	
	
	
}