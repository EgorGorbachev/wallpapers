package com.example.gorbachev_wallpapers.presentation.fragments

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentDetailsImageBinding
import com.example.gorbachev_wallpapers.databinding.InfoMenuBinding
import com.example.gorbachev_wallpapers.databinding.TuneMenuBinding
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.viewmodels.ImagesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details_image.*
import kotlinx.android.synthetic.main.info_menu.*
import java.util.*

@AndroidEntryPoint
class DetailsImageFragment : BaseFragment(R.layout.fragment_details_image) {
	
	private val imagesViewModel by viewModels<ImagesViewModel>()
	
	private var _binding: FragmentDetailsImageBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var botNav: BottomNavigationViewEx
	
	private val args by navArgs<DetailsImageFragmentArgs>()
	
	val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 101
	
	private var popupWindow: PopupWindow? = null
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		
		_binding = FragmentDetailsImageBinding.bind(view)
		
		botNav = requireActivity().findViewById(R.id.bot_nav)
		botNav.visibility = View.GONE
		botNav.enableAnimation(false)
		
		val photo = args.photo
		val image = args.image
		
		val imageDetailsMenu = view.findViewById(R.id.imageDetailsMenu) as BottomNavigationViewEx
		imageDetailsMenu.enableAnimation(false);
		imageDetailsMenu.enableShiftingMode(false);
		imageDetailsMenu.enableItemShiftingMode(false);
		
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
			if (photo != null) {
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
							onImageReady()
							return false
						}
						
						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							onImageReady()
							return false
						}
					})
					.into(imageDetailsIV)
				Glide.with(this@DetailsImageFragment)
					.load(photo.user.profile_image.medium)
					.error(R.drawable.ic_baseline_error_24)
					.into(goneImage)
				val query = args.query
				imageDetailsQuery.text = query
			} else {
				Glide.with(this@DetailsImageFragment)
					.load(image?.img)
					.error(R.drawable.ic_baseline_error_24)
					.listener(object : RequestListener<Drawable> {
						override fun onLoadFailed(
							e: GlideException?,
							model: Any?,
							target: Target<Drawable>?,
							isFirstResource: Boolean
						): Boolean {
							onImageReady()
							return false
						}
						
						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							onImageReady()
							return false
						}
					})
					.into(imageDetailsIV)
				imageDetailsQuery.text = image?.query
			}
		}
		
		
		binding.apply {
			var checkFullscreen = false
			imageFullscreenBtn.setOnClickListener {
				val checkFullscreenRes = fullScreen(checkFullscreen)
				checkFullscreen = checkFullscreenRes
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
							showTune()
							return@OnNavigationItemSelectedListener true
						}
						R.id.details_menu_info -> {
							showInfo()
							onInfoShow()
							return@OnNavigationItemSelectedListener true
						}
					}
					false
				}
			imageDetailsMenu.onNavigationItemSelectedListener = onClickBottomNavItem
		}
	}
	
	private fun onImageReady() {
		binding.imageDetailsProgressbar.isVisible = false
		imageDetailsMenu.isVisible = true
		binding.imageFullscreenBtn.isVisible = true
	}
	
	private fun onInfoShow() {
		binding.imageFullscreenBtn.isVisible = false
		binding.toolbar.isVisible = false
		binding.backBtnOnImageInfo.isVisible = true
	}
	
	
	private fun fullScreen(checkFullscreen: Boolean): Boolean {
		return if (!checkFullscreen) {
			toolbar.isVisible = false
			imageDetailsMenu.isVisible = false
			imageFullscreenBtn.setImageResource(R.drawable.ic_fullscreen_exit)
			true
		} else {
			toolbar.isVisible = true
			imageDetailsMenu.isVisible = true
			imageFullscreenBtn.setImageResource(R.drawable.ic_fullscreen)
			false
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (popupWindow != null) {
					hideOptionsWindow()
				} else {
					Navigation.findNavController(requireView())
						.navigate(R.id.action_detailsImageFragment_to_search_fr)
				}
			}
		})
	}
	
	
	private fun showTune() {
		val inflater: LayoutInflater =
			binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val popupView = inflater.inflate(R.layout.tune_menu, null)
		val tuneMenu = TuneMenuBinding.bind(popupView)
		val popupWidth = LinearLayout.LayoutParams.MATCH_PARENT
		val popupHeight = LinearLayout.LayoutParams.WRAP_CONTENT
		
		popupWindow = PopupWindow(popupView, popupWidth, popupHeight)
		popupWindow!!.update(
			0,
			0,
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		)
		popupWindow!!.animationStyle = R.style.popup_anim
		popupWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
		
		tuneMenu.wallpaperToWorkSpaceBtn.setOnClickListener {
			setWallpapersToWorkSpace()
		}
		
		tuneMenu.wallpaperToBlockScreenBtn.setOnClickListener {
			setWallpapersToBlockScreen()
		}
		
		tuneMenu.addToFavouritesBtn.setOnClickListener {
			imagesViewModel.insertDataBase(args.photo!!, args.query!!, imageDetailsIV, goneImage)
		}
		
		if (args.photo == null) {
			tuneMenu.addToFavouritesBtn.isVisible = false
		}
		
		binding.imageDetailsFullScreen.setOnClickListener {
			if (popupWindow != null) {
				popupWindow?.dismiss()
				popupWindow = null
			}
		}
		
		binding.backBtnOnImageInfo.setOnClickListener {
			popupWindow?.dismiss()
			popupWindow = null
		}
	}
	
	private fun setWallpapersToWorkSpace() {
		val wallpaperManager = WallpaperManager.getInstance(requireContext())
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			wallpaperManager.setBitmap(
				imagesViewModel.getBitmapFromView(imageDetailsIV),
				null,
				true,
				WallpaperManager.FLAG_SYSTEM
			)
			toast(getString(R.string.successful_mes_work_space_wallpaper))
		} else {
			toast(getString(R.string.device_error_mes))
		}
	}
	
	private fun setWallpapersToBlockScreen() {
		val wallpaperManager = WallpaperManager.getInstance(requireContext())
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			wallpaperManager.setBitmap(
				imagesViewModel.getBitmapFromView(imageDetailsIV),
				null,
				true,
				WallpaperManager.FLAG_LOCK
			)
			toast(getString(R.string.successful_mes_block_screen_wallpaper))
		} else {
			toast(getString(R.string.device_error_mes))
		}
	}
	
	
	private fun showInfo() {
		val photo = args.photo
		val image = args.image
		val inflater: LayoutInflater =
			binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val popupView = inflater.inflate(R.layout.info_menu, null)
		
		val infoMenu = InfoMenuBinding.bind(popupView)
		
		val popupWidth = LinearLayout.LayoutParams.MATCH_PARENT
		val popupHeight = LinearLayout.LayoutParams.WRAP_CONTENT
		popupWindow = PopupWindow(popupView, popupWidth, popupHeight)
		popupWindow!!.update(
			0,
			0,
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		)
		popupWindow!!.animationStyle = R.style.popup_anim
		popupWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
		if (photo != null) {
			if (photo.user.profile_image.large != null) {
				Glide.with(this@DetailsImageFragment)
					.load(photo.user.profile_image.large)
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
					.into(infoMenu.imageInfoAuthorPhoto)
			} else infoMenu.imageInfoAuthorPhoto.setImageResource(R.drawable.ic_baseline_person_24)
			
			infoMenu.imageInfoAuthorName.text = photo.user.name
			infoMenu.imageInfoAuthorTag.text = photo.user.username
			
			if (photo.user.instagram_username != null) {
				infoMenu.imageInfoAuthorInst.text = photo.user.instagram_username
			} else infoMenu.instaInfoContainer.isVisible = false
			
			if (photo.user.twitter_username != null) {
				infoMenu.imageInfoAuthorTwitter.text = photo.user.twitter_username
			} else infoMenu.twitterInfoContainer.isVisible = false
			
			if (photo.description != null) {
				infoMenu.imageInfoPhotoTitle.text = photo.description
			} else infoMenu.imageInfoPhotoTitle.isVisible = false
			
			val formattedDate = imagesViewModel.dateConvert(photo.updated_at)
			infoMenu.imageInfoDate.text = formattedDate
			
			infoMenu.imageInfoColor.text = photo.color
			
			"px: ${photo.width} x ${photo.height}".also { infoMenu.imageInfoSize.text = it }
			
			infoMenu.portfolioBtn.setOnClickListener {
				val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(photo.user.portfolio_url))
				startActivity(browserIntent)
			}
			
		} else {
			infoMenu.imageInfoAuthorPhoto.setImageBitmap(image?.imageProfile)
			
			infoMenu.imageInfoAuthorName.text = image?.name
			infoMenu.imageInfoAuthorTag.text = image?.username
			
			if (image?.instagram_username != null) {
				infoMenu.imageInfoAuthorInst.text = image.instagram_username
			} else infoMenu.instaInfoContainer.isVisible = false
			
			if (image?.twitter_username != null) {
				infoMenu.imageInfoAuthorTwitter.text = image.twitter_username
			} else infoMenu.twitterInfoContainer.isVisible = false
			if (image?.description != null) {
				infoMenu.imageInfoPhotoTitle.text = image.description
			} else infoMenu.imageInfoPhotoTitle.isVisible = false
			
			infoMenu.imageInfoDate.text = image?.date
			
			infoMenu.imageInfoColor.text = image?.color
			
			"px: ${image?.width} x ${image?.height}".also { infoMenu.imageInfoSize.text = it }
			
			infoMenu.portfolioBtn.setOnClickListener {
				val browserIntent =
					Intent(Intent.ACTION_VIEW, Uri.parse(photo?.user?.portfolio_url))
				startActivity(browserIntent)
			}
		}
		
		
		binding.imageDetailsFullScreen.setOnClickListener {
			if (popupWindow != null) hideOptionsWindow()
		}
		
		binding.backBtnOnImageInfo.setOnClickListener {
			hideOptionsWindow()
		}
	}
	
	private fun hideOptionsWindow() {
		binding.imageFullscreenBtn.isVisible = true
		binding.toolbar.isVisible = true
		popupWindow?.dismiss()
		binding.backBtnOnImageInfo.isVisible = false
		popupWindow = null
	}
	
	private fun sharePhoto() {
		val photo = imagesViewModel.getBitmapFromView(imageDetailsIV)
		val share = Intent(Intent.ACTION_SEND)
		share.type = "image/*"
		share.putExtra(Intent.EXTRA_STREAM, imagesViewModel.getImageUri(requireContext(), photo!!))
		startActivity(Intent.createChooser(share, "Share"))
	}
	
	
	// request permission //////////////////////////////////
	
	private fun startRequest() {
		checkForPermissions(
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
		)
	}
	
	private fun checkForPermissions(permission: String, requestCode: Int) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			when {
				ContextCompat.checkSelfPermission(
					requireActivity(),
					permission
				) == PackageManager.PERMISSION_GRANTED -> {
					sharePhoto()
				}
				shouldShowRequestPermissionRationale(permission) -> {
					requestPermissions(
						arrayOf(
							permission
						), requestCode
					)
				}
				else -> requestPermissions(
					arrayOf(
						permission
					), requestCode
				)
			}
		}
	}
	
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		fun innerCheck(name: String) {
			if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				toast(name + getString(R.string.permission_refused_mes))
			} else {
				toast(name + getString(R.string.permission_granted_mes))
				sharePhoto()
			}
		}
		innerCheck("storage")
	}
	
}