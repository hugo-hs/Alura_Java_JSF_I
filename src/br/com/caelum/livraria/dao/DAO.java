package br.com.caelum.livraria.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@SuppressWarnings("serial")
public class DAO<T> implements Serializable{

	private final Class<T> classe;
	private EntityManager em;

	public DAO(EntityManager em, Class<T> classe) {
		this.classe = classe;
		this.em = em;
	}

	public void adiciona(T t) {
		em.persist(t);
	}

	public void remove(T t) {
		em.remove(em.merge(t));
	}

	public void atualiza(T t) {
		em.merge(t);
	}

	public List<T> listaTodos() {
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();

		return lista;
	}

	public T buscaPorId(Integer id) {
		T instancia = em.find(classe, id);
		
		return instancia;
	}

	public int contaTodos() {
		long result = (Long) em.createQuery("select count(n) from livro n").getSingleResult();

		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults, String coluna, String valor) {
		
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		Root<T> root = query.from(classe);

		if(valor != null)
	        query = query.where(em.getCriteriaBuilder().like(root.<String>get(coluna), valor + "%"));
		
		List<T> lista = em.createQuery(query).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();

		return lista;
	}

	// classe DAO
	public int quantidadeDeElementos() {
	        long result = (Long) em.createQuery("select count(n) from " + classe.getSimpleName() + " n").getSingleResult();

	        return (int) result;
	}
}
