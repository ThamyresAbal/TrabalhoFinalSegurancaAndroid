package com.app.at_seguranca.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.at_seguranca.R
import com.app.at_seguranca.viewModel.ListaViewModel
import com.app.at_seguranca.viewModel.NovaAnotacaoViewModel
import kotlinx.android.synthetic.main.fragment_nova_anotacao.*
import java.util.Optional.of

class NovaAnotacaoFragment : Fragment() {
    private lateinit var novaAnotacaoViewModel: NovaAnotacaoViewModel
    private var TAKE_PICTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        novaAnotacaoViewModel = ViewModelProviders.of(this).get(NovaAnotacaoViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nova_anotacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getImageView.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    startActivityForResult(takePictureIntent, TAKE_PICTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}