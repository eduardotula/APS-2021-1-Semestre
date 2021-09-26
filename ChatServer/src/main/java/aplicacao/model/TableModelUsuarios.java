package aplicacao.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import aplicacao.dao.ServidorDAO;
import aplicacao.servidor.bean.Cargo;
import aplicacao.servidor.bean.UsuarioBD;

public class TableModelUsuarios extends AbstractTableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UsuarioBD> data;
	private Class<?>[] cl = new Class<?>[] {Integer.class, String.class, Cargo.class};
	private String[] columnNames = {"ID","Usuario","Cargo"};
	
	public TableModelUsuarios() {
		refreshModel();
	}
	public int getRowCount() {
		 if (data == null) {
	         return 0;
	      }
	      else {
	         return data.size();
	      }
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return data.get(rowIndex).getId();
		case 1:
			return data.get(rowIndex).getUsuario();
		case 2:
			return data.get(rowIndex).getCargo().getCargo();
		}
		return null;
	}
	public Integer getId(int modelRow) {
		return data.get(modelRow).getId();
	}
	public String getUsuarioNomeAt(int modelRow) {
		return data.get(modelRow).getUsuario();
	}
	public String getSenhaAt(int modelRow) {
		return data.get(modelRow).getSenha();
	}
	public UsuarioBD getUsuarioAt(int modelRow) {
		return data.get(modelRow);
	}
	public void setUsuarioAt(int modelRow, UsuarioBD usuario) {
		data.set(modelRow, usuario);
		fireTableRowsUpdated(modelRow, modelRow);
	}
	public void setUsuaiorNomeAt(int modelRow, String usuario) {
		data.get(modelRow).setUsuario(usuario);
		fireTableRowsUpdated(modelRow, modelRow);
	}
	public void setSenhaAt(int modelRow, String senha) {
		data.get(modelRow).setSenha(senha);
		fireTableRowsUpdated(modelRow, modelRow);
	}
	public void addRow(UsuarioBD usuario) {
		int row = getRowCount();
		data.add(usuario);
		fireTableRowsInserted(row,row);
	}

    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public UsuarioBD getRow(int row) {
    	return data.get(row);
    }
	
    public void removeRow(int row) {
    	data.remove(row);
    	fireTableRowsDeleted(row, row);
    }
    public String getColumnName(int col) {
    	return columnNames[col];
    }
    public Class<?> getColumnClass(int col) {
    	return cl[col];
    }
    public void refreshModel() {
		data = ServidorDAO.getEm().createQuery("SELECT u FROM UsuarioBD u",UsuarioBD.class).getResultList();
		fireTableDataChanged();
    }
}
