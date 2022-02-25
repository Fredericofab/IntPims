package gui.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	public static Long tentarConverterParaLong(String x) {
		try {
			return Long.parseLong(x);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	public static String tentarConverterParaMaiusculo(String x) {
		try {
			if (x != null) {
				x = x.toUpperCase();
			}
			return x;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static <T> void formatarTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		// Codigo copiado do curso -291 SellerList TableView

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
	public static <T> void formatarTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		// Codigo copiado do curso -291 SellerList TableView

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

	public static void formatarDatePicker(DatePicker datePicker, String format) {
		// Codigo copiado do curso -293 TextField e DatePicker
		// O DatePicker é um componente do FX para receber data (abre um calendario)  ao inves de usar o TextField
		
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

	public static NumberFormat formatarNumeroDecimalComMilhar(char decimal, char milhar ) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator(decimal);
        symbols.setGroupingSeparator(milhar);
        return new DecimalFormat("#,##0.00", symbols);
    }
	public static NumberFormat formatarNumeroDecimalSemMilhar(char decimal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator(decimal);
        return new DecimalFormat("#0.00", symbols);
    }
	public static NumberFormat formatarNumeroInteiroComMilhar(char milhar) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setGroupingSeparator(milhar);
        return new DecimalFormat("#,##0", symbols);
    }
	
// 2 Rotinas Exclusivas para o Sistema IntPims (Inicio)
	
	public static String excluiCaracterNaoEditavel(String campo, Integer tamanho) {
		if ( campo.length() != tamanho ) {
			int inicio = campo.length() - tamanho;
			int termino = campo.length();
			campo = campo.substring(inicio, termino);
		}
		return campo;
	}
	public static List<String> camposErp() {
		List<String> listaCamposOracle = new ArrayList<String>();
		listaCamposOracle.add("Arquivo de Origem			ORIGEM					C02");
		listaCamposOracle.add("Tipo Movimento			TIPO_MOVIMENTO			C06");
		listaCamposOracle.add("Ano e Mes de Referencia	ANO_MES				N06");
		listaCamposOracle.add("Codigo Centro de Custos	COD_CENTRO_CUSTOS		N20");
		listaCamposOracle.add("Descricao Centro de Custos	DESC_CENTRO_CUSTOS		C50");
		listaCamposOracle.add("Codigo Conta Contabil		COD_CONTA_CONTABIL		C20");
		listaCamposOracle.add("Descricao Conta Contabil	DESC_CONTA_CONTABIL	C50");
		listaCamposOracle.add("Codigo Material			COD_MATERIAL			C10");
		listaCamposOracle.add("Desc.  Material / Movimento	DESC_MOVIMENTO			C255");	
		listaCamposOracle.add("Unidade de Medida			UNIDADE_MEDIDA			C05");
		listaCamposOracle.add("Quantidade				QUANTIDADE				N14.2");
		listaCamposOracle.add("Preco Unitario				PRECO_UNITARIO			N14.2");
		listaCamposOracle.add("Valor do Movimento		VALOR_MOVIMENTO		N14.2");
		listaCamposOracle.add("Numero da O.S.			NUMERO_OS				C10");
		listaCamposOracle.add("Frota ou CC da O.S.			FROTA_OU_CC				C20");
		listaCamposOracle.add("Documento no ERP			DOCUMENTO_ERP			C10");
		listaCamposOracle.add("Data Movimento			DATA_MOVIMENTO		DATA");
		listaCamposOracle.add("Flag Importar				IMPORTAR				C01");
		listaCamposOracle.add("Observacao				OBSERVACAO				C255");
		listaCamposOracle.add("Politicas					POLITICAS				C255");
		listaCamposOracle.add("Flag Salvar Material na OS	SALVAR_OS_MATERIAL		C01");
		listaCamposOracle.add("Flag Salvar Valor do Material	SALVAR_CSTG_INTVM		C01");
		listaCamposOracle.add("Flag Salvar Consumo Material	SALVAR_CSTG_INTCM		C01");
		listaCamposOracle.add("Flag Salvar Despesas Gerais	SALVAR_CSTG_INTDG		C01");
		listaCamposOracle.add("Numero do Registro		SEQUENCIAL				N10");
		return listaCamposOracle;
	}

// 2 Rotinas Exclusivas para o Sistema IntPims (Termino)
	
}
