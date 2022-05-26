package model.dao;

import java.util.List;
import model.entities.PlcRat;

public interface PlcRatDao {
	List<PlcRat> listarTodos(String anoMes, String usuarioPimsCS);
}
