package aplicacao.model.editorrender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.table.DefaultTableCellRenderer;

public class TableRendererDate extends DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");



	public TableRendererDate()
	{}

	public void setValue(Object value)
	{

		try
		{
			if (value != null && value instanceof LocalDate) {
				value = ((LocalDate)value).format(formatters);
			}
		}
		catch(IllegalArgumentException e) {e.printStackTrace();}
		super.setValue(value);
	}


	public static TableRendererDate getDateTimeRenderer()
	{
		return new TableRendererDate();
	}



}
