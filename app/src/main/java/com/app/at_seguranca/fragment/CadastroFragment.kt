package com.app.at_seguranca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.at_seguranca.R
import com.app.at_seguranca.viewModel.UsuarioViewModel
import kotlinx.android.synthetic.main.fragment_cadastro.*
import kotlinx.android.synthetic.main.fragment_cadastro.txtEmail
import kotlinx.android.synthetic.main.fragment_cadastro.txtSenha
import kotlinx.android.synthetic.main.fragment_login.*


class CadastroFragment : Fragment() {
    private lateinit var usuarioViewModel: UsuarioViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            usuarioViewModel = ViewModelProviders.of(it).get(UsuarioViewModel::class.java)
        }
        btnCadastrar.setOnClickListener {

             val cadastrou = usuarioViewModel.verificarNulo(view, requireContext().applicationContext)
            if(!cadastrou){
                Toast.makeText(context, "Preencha todas as informações", Toast.LENGTH_SHORT).show()
            }else{
                usuarioViewModel.criarAuth(txtEmail.text.toString(), txtSenha.text.toString(), requireContext())
                findNavController().navigate(R.id.loginFragment)
            }
        }
        btnVoltar.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }    }


}
