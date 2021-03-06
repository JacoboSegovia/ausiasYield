package net.daw.operation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.daw.bean.StreamBean;
import net.daw.bean.UsuarioBean;
import net.daw.dao.StreamDao;
import net.daw.dao.UsuarioDao;
import net.daw.helper.Contexto;
import net.daw.parameter.StreamParam;

public class StreamUpdate1 implements Operation {

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Contexto oContexto = (Contexto) request.getAttribute("contexto");

        //Parte para saber el tipo de usuario
        UsuarioBean oUsuarioBean;
        oUsuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");
        java.lang.Enum tipoUsuario = oUsuarioBean.getTipoUsuario();

        //Validación
        if (tipoUsuario.equals(net.daw.helper.Enum.TipoUsuario.Profesor)) {

            oContexto.setVista("jsp/stream/form.jsp");
            StreamBean oStreamBean;
            StreamDao oStreamDao;
            oStreamBean = new StreamBean();
            StreamParam oStreamParam = new StreamParam(request);
            oStreamBean = oStreamParam.loadId(oStreamBean);
            oStreamDao = new StreamDao(oContexto.getEnumTipoConexion());
            try {
                oStreamBean = oStreamDao.get(oStreamBean);
            } catch (Exception e) {
                throw new ServletException("StreamController: Update Error: Phase 1: " + e.getMessage());
            }

            try {
                oStreamBean = oStreamParam.load(oStreamBean);
            } catch (NumberFormatException e) {
                oContexto.setVista("jsp/mensaje.jsp");
                return "Tipo de dato incorrecto en uno de los campos del formulario";
            }

            UsuarioDao oUsuarioDao = new UsuarioDao(oContexto.getEnumTipoConexion());
            oStreamBean.setUsuario(oUsuarioDao.get(oStreamBean.getUsuario()));
            return oStreamBean;
        } else {
            //Mostramos el MENSAJE
            oContexto.setVista("jsp/mensaje.jsp");
            return "<span class=\"label label-important\">¡¡¡ No estás autorizado a entrar aquí !!!<span>";
        }
    }
}