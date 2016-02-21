package br.com.alura.loja.testes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTeste {
	
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
	public void testaQueBuscarUmProjetoTrazOprojetoEsperado() {
		Projeto conteudo = target.path("/projetos/1").request().get(Projeto.class);
		
		Assert.assertEquals("Minha loja", conteudo.getNome());
	}
}
