package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {

	private UsuarioDao usuarioDao;
	private Session session;

	// OBS: ANTES DE RODAR OS TESTES, EXECUTE A CLASSE CRIA TABELAS

	@Before
	public void antes() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);

		session.beginTransaction();
	}

	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void deveEncontrarPeloNomeEemailMockado() {

		Usuario novoUsuario = new Usuario("João da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);

		Usuario usuario = usuarioDao.porNomeEEmail("João da Silva", "joao@dasilva.com.br");

		assertEquals(novoUsuario.getNome(), usuario.getNome());
		assertEquals(novoUsuario.getEmail(), usuario.getEmail());

	}

	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {

		Usuario usuario = usuarioDao.porNomeEEmail("João Joaquim", "joao@dasilva.com.br");

		Assert.assertNull(usuario);

	}

	@Test
	public void deveDeletarUmUsuario() {

		Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");
		usuarioDao.salvar(mauricio);
		usuarioDao.deletar(mauricio);

		session.flush();
		session.clear();

		Usuario deletado = usuarioDao.porNomeEEmail("Mauricio", "mauricio@mauricio.com.br");

		Assert.assertNull(deletado);

	}

}
