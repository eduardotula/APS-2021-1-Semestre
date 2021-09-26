package aplicacao.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.Usuario;


public class MensagemTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	
	private List<Mensagem> delegate = new ArrayList<Mensagem>();
	private String[] columnName = {"Mensagem", "Confirmacao"};
	
	@Override
	public int getRowCount() {
		return delegate.size();
	}

	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	@Override
	public Mensagem getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return delegate.get(rowIndex);
		case 1:
			if(delegate.get(rowIndex).getStatus() != null) {
				return delegate.get(rowIndex);
			}else {
				return null;
			}
		}
		return null;
	}
	public Mensagem getValueAt(int rowIndex) {
		return delegate.get(rowIndex);
	}
	public Usuario getUsuario(int row) {
		return delegate.get(row).getUsuario();
	}
	
	public Usuario getUltiUsua() {
		return delegate.get(getRowCount()-1).getUsuario();
	}
    public void clear() {
        int index1 = delegate.size()-1;
        delegate.clear();
        if (index1 >= 0) {
        	fireTableRowsDeleted(0, index1);
        }
    }
    public void insertElementAt(Mensagem element, int index) {
        delegate.add(index, element);
        fireTableRowsInserted(index, index);
    }
    
    public void replaceAt(Mensagem element, int index) {
    	delegate.set(index, element);
    	fireTableCellUpdated(index, index);
    }
	public void addRow(Mensagem mensagem) {
		int row = getRowCount();
		delegate.add(mensagem);
		fireTableRowsInserted(row,row);
	}

    public boolean removeElement(Object obj) {
        int index = delegate.indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireTableRowsDeleted(index, index);
        }
        return rv;
    }

}
