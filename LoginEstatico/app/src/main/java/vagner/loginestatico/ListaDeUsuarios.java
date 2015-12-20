package vagner.loginestatico;

import java.util.ArrayList;
import java.util.List;

public class ListaDeUsuarios {

    private static ListaDeUsuarios instance;
    private List<Usuario> usuarios;
    private ListaDeUsuarios() {
        usuarios = new ArrayList<Usuario>();
    }

    public static ListaDeUsuarios getInstance() {
        if (instance == null) {
            instance = new ListaDeUsuarios();
        }
        Usuario usuario = new Usuario();
        usuario.setNome("vagner");
        usuario.setSenha("123456");
        instance.getUsuarios().add(usuario);
        return instance;
    }

    public static void setInstance(ListaDeUsuarios instance) {
        ListaDeUsuarios.instance = instance;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
