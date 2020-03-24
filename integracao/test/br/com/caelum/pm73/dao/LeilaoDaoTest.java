package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {

	private LeilaoDao leilaoDao;
	private Session session;
	private UsuarioDao usuarioDao;

	@Before
	public void antes() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);

		session.beginTransaction();
	}

	@After
	public void depois() {
		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void deveContaerLeiloesNaoEncerrados() {
		Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");

		Leilao ativo = new Leilao("Geladeira", 1500.0, mauricio, false);
		Leilao encerrado = new Leilao("xBox", 700.0, mauricio, false);
		encerrado.encerra();

		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);

		long total = leilaoDao.total();

		assertEquals(1L, total);
	}

	@Test
	public void deveTrazerLeiloesNaoEncerradosNoPerodo() {

		Calendar comecoDoIntervalo = Calendar.getInstance();
		comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);

		Calendar fimDoIntervalo = Calendar.getInstance();

		Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");
		Leilao leilao1 = new Leilao("xBox", 700.0, mauricio, false);
		Calendar dataLeilao1 = Calendar.getInstance();
		dataLeilao1.add(Calendar.DAY_OF_MONTH, -2);
		leilao1.setDataAbertura(dataLeilao1);

		Leilao leilao2 = new Leilao("Geladeira", 1700.0, mauricio, false);
		Calendar dataLeilao2 = Calendar.getInstance();
		dataLeilao2.add(Calendar.DAY_OF_MONTH, -20);
		leilao2.setDataAbertura(dataLeilao2);

		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);

		List<Leilao> leiloes = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);

		assertEquals(1, leiloes.size());
		assertEquals(leilao1.getNome(), leiloes.get(0).getNome());

	}

	@Test
	public void naoDeveTrazerLeiloesEncerradosNoPeriodo() {

		Calendar comecoDoIntervalo = Calendar.getInstance();
		comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);

		Calendar fimDoIntervalo = Calendar.getInstance();

		Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");

		Calendar dataLeilao1 = Calendar.getInstance();
		dataLeilao1.add(Calendar.DAY_OF_MONTH, -2);

		Leilao leilao1 = new Leilao("xBox", 700.0, mauricio, false);
		leilao1.setDataAbertura(dataLeilao1);
		leilao1.encerra();

		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(leilao1);

		List<Leilao> leiloes = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);

		assertEquals(0, leiloes.size());

	}

}
