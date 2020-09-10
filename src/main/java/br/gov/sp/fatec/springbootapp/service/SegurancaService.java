package br.gov.sp.fatec.springbootapp.service;

import br.gov.sp.fatec.springbootapp.entity.Usuario;

public interface SegurancaService {

    public Usuario criarUsuario(String nome, String senha, String autorizacao);
    
}