package model.dao;

import java.util.List;
import model.entities.PlcPrim;

public interface PlcPrimDao {
	List<PlcPrim> listarTodos(String anoMes, String usuarioPimsCS);
}
