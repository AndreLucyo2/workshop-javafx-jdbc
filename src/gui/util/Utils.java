package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {

	// Função para pegar o stage a partir de um control, ou da ação , evento , ou seja a partir de onde
	// esta - Pauco atual
	public static Stage currentStage(ActionEvent event)
	{
		// Pegar o Stage a partir de uma ação disparada por um btn ou menu...
		// a partir do node, pega a scene e da sece napra a window w retorna o stage atual
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	// faz a conversao de texto para inteiro, caos nao for numero, retorna ull
	// para facilitar ler um numero de um campo de texto da tela
	public static Integer tryParseToInt(String str)
	{
		try
		{
			// Convrter String para Integer
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			// se nao for um numero valido, retorna null
			return null;
		}
	}

	// Tenta converte String para double, se der erro, retorna null
	public static Double tryParseToDouble(String str)
	{
		try
		{
			// Converte String para Double
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e)
		{
			// se nao for um numero valido, retorna null
			return null;
		}
	}

	// Formata a data na coluna da tableview
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format)
	{
		tableColumn.setCellFactory(column ->
		{
			TableCell<T, Date> cell = new TableCell<T, Date>()
			{
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty)
				{
					super.updateItem(item, empty);
					if (empty)
					{
						setText(null);
					}
					else
					{
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	// Formata a numero com virgula na coluna da tableview
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces)
	{
		tableColumn.setCellFactory(column ->
		{
			TableCell<T, Double> cell = new TableCell<T, Double>()
			{
				@Override
				protected void updateItem(Double item, boolean empty)
				{
					super.updateItem(item, empty);
					if (empty)
					{
						setText(null);
					}
					else
					{
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}

	// Seção 23 - 290. TextField & DatePicker
	// ref.:
	// https://stackoverflow.com/questions/26831978/javafx-datepicker-getvalue-in-a-specific-format
	public static void formatDatePicker(DatePicker datePicker, String format)
	{
		datePicker.setConverter(new StringConverter<LocalDate>()
		{
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date)
			{
				if (date != null)
				{
					return dateFormatter.format(date);
				}
				else
				{
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string)
			{
				if (string != null && !string.isEmpty())
				{
					return LocalDate.parse(string, dateFormatter);
				}
				else
				{
					return null;
				}
			}
		});
	}

}
