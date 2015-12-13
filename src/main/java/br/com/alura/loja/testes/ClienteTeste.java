package br.com.alura.loja.testes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.Servidor;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClienteTeste {

	private HttpServer server;
	
	@Before
	public void startaServidor(){ 
		server = Servidor.inicializaServidor();
	}
	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		
		// System.out.println(conteudo);
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testaPostDeCarrinhos() {
		// cliente e webtarget
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		// Mock de um carrinho
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("São Paulo");
		
		// xml que armazena o objeto serializado
		String xml = carrinho.toXML();
		
		// objeto que é utilizado para representar o objeto carrinho e o tipo de requisição;
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		
		Response response = target.path("/carrinhos").request().post(entity);
		
		Assert.assertEquals("<status>Sucesso</status>", response.readEntity(String.class));
	}
//	
//	@Test
//    public void testaQueSuportaNovosCarrinhos(){
//		
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080");
//        
//        Carrinho carrinho = new Carrinho();
//        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
//        carrinho.setRua("Rua Vergueiro");
//        carrinho.setCidade("Sao Paulo");
//        String xml = carrinho.toXML();
//
//        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
//
//        Response response = target.path("/carrinhos").request().post(entity);
//        Assert.assertEquals("<status>Sucesso</status>", response.readEntity(String.class));
//        Assert.assertEquals("<status>Sucesso</status>", response);
//    }
}
