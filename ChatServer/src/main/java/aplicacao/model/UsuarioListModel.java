package aplicacao.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

import aplicacao.servidor.bean.Usuario;

public class UsuarioListModel extends AbstractListModel<String>{
	private static final long serialVersionUID = 1L;
	
	
	private List<Usuario> delegate = new ArrayList<Usuario>();
	
	public int getSize() {
		return delegate.size();
	}

	public String getElementAt(int index) {
		return delegate.get(index).getUsuario();
	}
	public Usuario getUsuarioAt(int index) {
		return delegate.get(index);
	}
    public void clear() {
        int index1 = delegate.size()-1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }
    public void insertElementAt(Usuario element, int index) {
        delegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }
    
    public void addElement(Usuario element) {
        int index = delegate.size();
        delegate.add(element);
        fireIntervalAdded(this, index, index);
    }

    public boolean removeElement(Object obj) {
        int index = delegate.indexOf(obj);
        System.out.println("indice " + index);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }
}
