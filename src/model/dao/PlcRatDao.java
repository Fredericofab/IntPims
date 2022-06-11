package model.dao;

import java.util.List;
import model.entities.PlcRat;

public interface PlcRatDao {
	List<PlcRat> listarTodos(String anoMes, String usuarioPimsCS);

	List<PlcRat> listarCCusto(String anoMes, String usuarioPimsCS, Double ccusto);
}
