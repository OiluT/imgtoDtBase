package com.example.testawebservices;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Base64;

public class UsuarioDAO {

	private static final String URL = "http://192.168.0.105:8080/ServicetoImagem/services/UsuarioDAO?wsdl";
	private static final String NAMESPACE = "http://ImageToService.com.br";

	private static final String INSERIR = "inserirUsuario";
	private static final String BUSCAR_TODOS = "buscarTodosUsuarios";
	private static final String BUSCAR_POR_ID = "buscaUsuarioPorId";
	private static final String EXCLUIR_USUARIO_POR_ID = "excluirUsuario";
	private static final String SALVAR_PDF = "salvarPDF";
	private static final String SALVAR_DIRETORIO = "salvarDiretorio";

	public boolean salvarDiretorio(Usuario usr) {
		SoapObject buscarUsuarios = new SoapObject(NAMESPACE, SALVAR_DIRETORIO);
		//buscarUsuarios.addProperty("id", usr.getId());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(buscarUsuarios);

		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + SALVAR_DIRETORIO, envelope);

			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

			return Boolean.parseBoolean(resposta.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean salvarPDF(Usuario usr) {
		SoapObject buscarUsuarios = new SoapObject(NAMESPACE, SALVAR_PDF);
		//buscarUsuarios.addProperty("id", usr.getId());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(buscarUsuarios);

		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + SALVAR_PDF, envelope);

			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

			return Boolean.parseBoolean(resposta.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	public Usuario inserirUsuario(Usuario usuario) {

		SoapObject inserirUsuario = new SoapObject(NAMESPACE, INSERIR);

		SoapObject usr = new SoapObject(NAMESPACE, "usuario");
		usr.addProperty("id", usuario.getId());
		usr.addProperty("data", usuario.getData());
		usr.addProperty("nome", usuario.getNome());
		usr.addProperty("foto", usuario.getFoto());
		usr.addProperty("tipoNota", usuario.getTipoNota());
		usr.addProperty("valorNota", usuario.getValorNota());

		inserirUsuario.addSoapObject(usr);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(inserirUsuario);
		
		new MarshalBase64().register(envelope);
		
		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + INSERIR, envelope);

			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

			int id = Integer.parseInt(resposta.toString());

			if (id > 0) {
				usuario.setId(id);
				return usuario;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Usuario busarUsuarioPorID(int id) {
		Usuario usr = null;

		SoapObject buscarUsuarios = new SoapObject(NAMESPACE, BUSCAR_POR_ID);
		buscarUsuarios.addProperty("id", id);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(buscarUsuarios);

		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + BUSCAR_POR_ID, envelope);

			SoapObject resposta = (SoapObject) envelope.getResponse();

			usr = new Usuario();

			usr.setId(Integer.parseInt(resposta.getProperty("id").toString()));
			usr.setNome(resposta.getProperty("nome").toString());
			usr.setData(resposta.getProperty("idade").toString());

			String foto = resposta.getProperty("foto").toString();

			byte[] bt = Base64.decode(foto, Base64.DEFAULT);
			usr.setFoto(bt);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return usr;
	}

	public boolean exculirUsuario(Usuario usr) {
		SoapObject buscarUsuarios = new SoapObject(NAMESPACE, EXCLUIR_USUARIO_POR_ID);
		buscarUsuarios.addProperty("id", usr.getId());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(buscarUsuarios);

		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + EXCLUIR_USUARIO_POR_ID, envelope);

			SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

			return Boolean.parseBoolean(resposta.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Usuario> BuscarTodosUsuarios() {

		List<Usuario> listaUsr = new ArrayList<Usuario>();

		SoapObject buscarUsuarios = new SoapObject(NAMESPACE, BUSCAR_TODOS);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(buscarUsuarios);

		envelope.implicitTypes = true;

		HttpTransportSE http = new HttpTransportSE(URL);

		try {
			http.call("urn:" + BUSCAR_TODOS, envelope);

			if (envelope.getResponse() instanceof SoapObject) {
				SoapObject resposta = (SoapObject) envelope.getResponse();

				Usuario usr = new Usuario();

				usr.setId(Integer.parseInt(resposta.getProperty("id").toString()));
				usr.setNome(resposta.getProperty("nome").toString());
				usr.setData(resposta.getProperty("idade").toString());
				String foto = resposta.getProperty("foto").toString();

				byte[] bt = Base64.decode(foto, Base64.DEFAULT);
				usr.setFoto(bt);
				listaUsr.add(usr);
			} else {
				@SuppressWarnings("unchecked")
				Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

				for (SoapObject resposta : retorno) {

					Usuario usr = new Usuario();

					usr.setId(Integer.parseInt(resposta.getProperty("id").toString()));
					usr.setNome(resposta.getProperty("nome").toString());
					usr.setData(resposta.getProperty("idade").toString());

					String foto = resposta.getProperty("foto").toString();

					byte[] bt = Base64.decode(foto, Base64.DEFAULT);
					usr.setFoto(bt);
					listaUsr.add(usr);
				}
			}
		}

	  catch (NullPointerException e) {
				System.err.println("Null pointer exception");
			}catch(ClassCastException | SoapFault e) {//trata aqui caso tenha Exception de cast quer dizer que foi so um registro
				e.printStackTrace();

			} catch (HttpResponseException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		return listaUsr;
	}

}
