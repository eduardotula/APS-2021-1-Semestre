package aplicacao.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;
import aplicacao.dao.ServidorDAO;
import aplicacao.servidor.bean.MensagemBD;

public class TableModelMensagens extends AbstractTableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MensagemBD> data;
	private Class<?>[] cl = new Class<?>[] {Integer.class,String.class, LocalDateTime.class,String.class, String.class};
	private String[] columnNames = {"ID","Conteudo","Data Hora","Remetente","Destinatario"};
	private boolean[] ColumnEditables = {false,false,false,false,false};
	
	public TableModelMensagens(Integer idUsuario) {
		refreshModel(idUsuario);
	}
	public TableModelMensagens() {
		
	}
	public int getRowCount() {
		 if (data == null) {
	         return 0;
	      }
	      else {
	         return data.size();
	      }
	}
    public void setValueAt(Object value, int row, int column) {
    	MensagemBD ms;
		switch (column) {
		case 0:
			ms = data.get(row);
			ms.setId((Integer) value);
			data.set(row, ms);
			break;
		case 1:
			ms = data.get(row);
			ms.setMensagem((String) value);
			data.set(row, ms);
			break;
		case 2:
			ms = data.get(row);
			ms.setDateTime((LocalDateTime) value);
			data.set(row, ms);
			break;
		}
		fireTableCellUpdated(row, column);
    }
	public int getColumnCount() {
		return columnNames.length;
	}

	public void setMensagemAt(int modelRow, MensagemBD mensagem) {
		data.set(modelRow, mensagem);
		fireTableRowsUpdated(modelRow, modelRow);
	}
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			switch (columnIndex) {
			case 0:
				return data.get(rowIndex).getId();
			case 1:
				return data.get(rowIndex).getMensagem();
			case 2:
				return data.get(rowIndex).getDateTime();
			case 3:
				return data.get(rowIndex).getUsuario().getUsuario();
			case 4:
				if(data.get(rowIndex).getDestinatario() == null) {
					return "Todos";
				}else {
					return data.get(rowIndex).getDestinatario().getUsuario();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Integer getId(int modelRow) {
		return data.get(modelRow).getId();
	}
	public MensagemBD getMensagem(int modelRow) {
		return data.get(modelRow);
	}
	public void addRow(MensagemBD mensagem) {
		int row = getRowCount();
		data.add(mensagem);
		fireTableRowsInserted(row,row);
	}

    public boolean isCellEditable(int row, int column) {
    	return ColumnEditables[column];
    }
    
    public MensagemBD getRow(int row) {
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
    
    @SuppressWarnings("unchecked")
	public void refreshModel(Integer idUsuario) {
		Query query = ServidorDAO.getEm().createQuery("SELECT u FROM MensagemBD u WHERE u.usuario.id = :id OR u.destinatario.id = :id",MensagemBD.class);
		data = query.setParameter("id", idUsuario).getResultList();
		fireTableStructureChanged();
    }
}
