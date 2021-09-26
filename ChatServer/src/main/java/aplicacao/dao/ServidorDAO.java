package aplicacao.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import aplicacao.servidor.bean.Cargo;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.MensagemBD;
import aplicacao.servidor.bean.Usuario;
import aplicacao.servidor.bean.UsuarioBD;

public class ServidorDAO {

	private static EntityManager em;
	
	/**
	 * Salva um Usuario 
	 * @param usuario
	 * @param nomeUs
	 * @param senha
	 * @throws Exception
	 */
	public static void criarUsuario(UsuarioBD usuario) throws Exception{
		if(!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		if(usuario.getId() == null) {
			em.persist(usuario);
		}
		em.getTransaction().commit();
	}
	
	/**Remove um Usuario 
	 * 
	 * @param Usuario usuario
	 * @throws Exception
	 */
	public static void apagarUsuario(UsuarioBD usuario) throws Exception{
		em.getTransaction().begin();
		if(em.contains(usuario)) {
			em.remove(usuario);
		}else {
			em.remove(em.find(UsuarioBD.class, usuario.getId()));
		}
		em.getTransaction().commit();
	}
	
	/**
	 * Remove um Usuario de acordo com seu ID
	 * @param Integer id
	 * @throws Exception
	 */
	public static void apagarUsuarioById(Integer id)throws Exception {
		em.getTransaction().begin();
		Usuario usuario = em.find(Usuario.class, id);
		em.remove(usuario);
		em.getTransaction().commit();
	}
	/**Persiste um objeto Mensagem no banco ou salva mudanças se o objeto já estiver gerenciado
	 * @param mensagem
	 * @throws Exception
	 */
	public static void criarMensagem(MensagemBD mensagem) throws Exception{
		if(!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		if(mensagem.getId() == null) {
			em.persist(mensagem);
		}
		em.getTransaction().commit();
	}
	
	/**
	 * Retorna um Usuario não gerenciado do banco de acordo com o id,
	 * retorna null se não for encontrado
	 * @param Integer id
	 * @return Usuario usuario
	 * @throws IllegalArgumentException
	 */
	public static Mensagem getMensagemGById(Integer id)throws IllegalArgumentException {
		return em.find(Mensagem.class, id);
	}
	
	public static void apagarMensagem(MensagemBD mensagem) throws Exception{
		begin();
		em.remove(mensagem);
		commit();
	}
	
	/**
	 * Retorna um Usuario gerenciado do banco de acordo com o id,
	 * retorna null se não for encontrado
	 * @param Integer id
	 * @return Usuario usuario
	 * @throws IllegalArgumentException
	 */
	public static UsuarioBD getUsuarioById(Integer id) throws IllegalArgumentException{
		UsuarioBD usuario = em.find(UsuarioBD.class, id);
		em.detach(usuario);
		return usuario;
	}
	
	/**Retorna um Usuario não gerenciado do banco de dados utilizando o nome como chave de busca
	 * @param nomeUsuario
	 * @return Usuario 
	 */
	public static UsuarioBD findUsuarioByName(String nomeUsuario){
		try {
			Query query = em.createQuery("SELECT u FROM UsuarioBD u WHERE u.usuario = :usuario",UsuarioBD.class);
			UsuarioBD usuario = (UsuarioBD) query.setParameter("usuario", nomeUsuario).getSingleResult();
			em.detach(usuario);
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**Retorna um Usuario gerenciado do banco de dados utilizando o nome como chave de busca
	 * @param nomeUsuario
	 * @return
	 */
	public static UsuarioBD findUsuarioGByName(String nomeUsuario){
		try {
			Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.usuarioBD = :usuario",UsuarioBD.class);
			UsuarioBD usuario = (UsuarioBD) query.setParameter("usuario", nomeUsuario).getSingleResult();
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**Obtem uma lista contendo todos os cargos*/
	public static Cargo[] getAllCargos() {
		return em.createQuery("SELECT c FROM Cargo c",Cargo.class).getResultList().toArray(new Cargo[0]);
	}
	
	/**Obtem uma lista contendo todos os Usuarios*/
	public static UsuarioBD[] getAllUsuarios() {
		return em.createQuery("SELECT c FROM UsuarioBD c",UsuarioBD.class).getResultList().toArray(new UsuarioBD[0]);
	}
	/**Cadastra um novo Cargo*/
	public static void criarCargo(Cargo cargo) {
		if(!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		if(cargo.getId() == null) {
			em.persist(cargo);
		}
		em.getTransaction().commit();
	}
	public static void begin() throws Exception{
		if(!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}else {
			throw new Exception();
		}
	}
	public static void commit() throws Exception{
		if(em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}else {
			throw new Exception();
		}
	}
	public static EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		ServidorDAO.em = em;
	}
	
}
