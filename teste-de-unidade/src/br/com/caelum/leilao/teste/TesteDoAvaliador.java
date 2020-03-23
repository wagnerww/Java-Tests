package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.service.Avaliador;

public class TesteDoAvaliador {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario mario;

	@Before // O JUnit executa esse cara, antes de cada método de teste
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();

		joao = new Usuario("João");
		jose = new Usuario("José");
		mario = new Usuario("Mario");
	}

	// Teste de excessão
	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {

		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 novo").constroi();

		leiloeiro.avalia(leilao);

	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {

		// parte 1: cenário

		Leilao leilao = new Leilao("Playstation 3");

		leilao.propoe(new Lance(joao, 250.0));
		leilao.propoe(new Lance(jose, 300.0));
		leilao.propoe(new Lance(mario, 400.0));

		// parte 2: Acao, trocado pelo vefore

		leiloeiro.avalia(leilao);

		// parte 3: validacao
		double maiorEperado = 400;
		double menorEsperado = 250;

		assertThat(maiorEperado, equalTo(leiloeiro.getMaiorLance()));
		assertThat(menorEsperado, equalTo(leiloeiro.getMenorDeTodos()));

	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {

		// parte 1: cenário

		Leilao leilao = new CriadorDeLeilao().para("Playstation 3").lance(joao, 1000.0).constroi();

		// parte 2: Acao

		leiloeiro.avalia(leilao);

		assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.0, leiloeiro.getMenorDeTodos(), 0.00001);

	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {
		// parte 1: cenário

		Leilao leilao = new CriadorDeLeilao().para("Playstation 3").lance(joao, 100.0).lance(jose, 200.0)
				.lance(joao, 300.0).lance(jose, 400.0).constroi();

		// parte 2: Acao

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());
		assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
		assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
		assertEquals(200.0, maiores.get(2).getValor(), 0.00001);

	}

}
