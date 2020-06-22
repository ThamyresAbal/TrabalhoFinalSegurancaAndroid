package com.app.at_seguranca.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.at_seguranca.NotasActivity
import com.app.at_seguranca.R
import com.app.at_seguranca.viewModel.ListaViewModel
import com.app.at_seguranca.viewModel.UsuarioViewModel
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.EnumSet.of


class HomeFragment : Fragment() {
    lateinit var listaViewModel: ListaViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listaViewModel = ViewModelProviders.of(this).get(ListaViewModel::class.java)
        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogout.setOnClickListener {
            usuarioViewModel.logout(requireContext())
        }
        usuarioViewModel.preferenciaUsuario(requireContext())
        usuarioViewModel.identificarUsuario(textEmailUsuario)
        listaViewModel.recyclerConfig(recycler,requireContext())

        buttonCriarLista.setOnClickListener {
            startActivity(Intent(context, NotasActivity::class.java))
        }


    }
}