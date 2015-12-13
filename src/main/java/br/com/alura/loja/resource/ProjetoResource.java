package br.com.alura.loja.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.alura.loja.dao.ProjetoDAO;

@Path("projetos")
public class ProjetoResource {
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") long id) {
		ProjetoDAO dao = new ProjetoDAO();
		return dao.busca(id).toJson();
	}
 }
