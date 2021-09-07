package br.gov.sp.fatec.springbootapp.repository;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {

    public Conversa findByIdConversa(Long idConversa);
    
}
