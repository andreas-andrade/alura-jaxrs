package br.com.alura.loja.testes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClienteTeste {

	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void startaServidor() {
		server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		this.client = ClientBuilder.newClient(config);
		this.target = client.target("http://localhost:8080");
	}

	@After
	public void mataServidor() {
		server.stop();
	}

	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaQueSUportaNovosCarrinhosJson() {
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314, "Microfone", 37, 40));
		carrinho.setRua("Rua Tupaciguara 48");
		carrinho.setCidade("Belo Horizonte");
		String json = carrinho.toJson();
		Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);

		Response response = target.path("/carrinhos").request().post(entity);

		Assert.assertEquals(201, response.getStatus());
		String location = response.getHeaderString("Location");
		String conteudo = client.target(location).request().get(String.class);
		Assert.assertTrue(conteudo.contains("Microfone"));

	}

	@Test
	public void testaQueSuportaNovosCarrinhosXml() {

		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314, "Tablet", 37, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("Sao Paulo");

		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

		Response response = target.path("/carrinhos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());

		String location = response.getHeaderString("Location");
		Carrinho carrinhoCarregado = client.target(location).request().get(Carrinho.class);

		Assert.assertEquals("Tablet", carrinhoCarregado.getProdutos().get(0).getNome());
	}
}
