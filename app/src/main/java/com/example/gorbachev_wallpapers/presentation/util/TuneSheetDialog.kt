package com.example.gorbachev_wallpapers.presentation.util

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.gorbachev_wallpapers.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TuneSheetDialog(
	private val bitmap:Bitmap?
): BottomSheetDialogFragment() {
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.tune_menu, container,false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val wallpaperToWorkSpaceBtn = view.findViewById<LinearLayout>(R.id.wallpaperToWorkSpaceBtn)
		val wallpaperToBlockScreenBtn = view.findViewById<LinearLayout>(R.id.wallpaperToBlockScreenBtn)
		wallpaperToWorkSpaceBtn.setOnClickListener{
			val wallpaperManager = WallpaperManager.getInstance(requireContext())
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
				Toast.makeText(requireContext(), "Обои рабочего стола успешно установлены!", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(requireContext(), "Неподходящая версия устройства!", Toast.LENGTH_SHORT).show()
			}
		}
		wallpaperToBlockScreenBtn.setOnClickListener{
			val wallpaperManager = WallpaperManager.getInstance(requireContext())
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
				Toast.makeText(requireContext(), "Обои экрана блокировки успешно установлены!", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(requireContext(), "Неподходящая версия устройства!", Toast.LENGTH_SHORT).show()
			}
		}
	}
}