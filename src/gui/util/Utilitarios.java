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

public class Utilitarios {

	public static Stage atualStage(ActionEvent evento) {
		// Retorna o Stage (palco) onde o evento foi acionado. Para saber qual é a janela windows "pai".
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}

	public static Integer tentarConverterParaInt(String x) {
		try {
			return Integer.parseInt(x);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double tentarConverterParaDouble(String x) {
		try {
			return Double.parseDouble(x);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String tentarConverterParaMaiusculo(String x) {
		try {
			if (x != null) {
				x.toUpperCase();
			}
			return x;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// Codigo copiado do curso -291 SellerList TableView
	public static <T> void formatarTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}

			};
			return cell;
		});
	}

	// Codigo copiado do curso -291 SellerList TableView
	public static <T> void formatarTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}

	// Codigo copiado do curso -293 TextField e DatePicker
	// O DatePicker é um componente do FX para receber data (abre um calendario)  ao inves de usar o TextField
	public static void formatarDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}

}
