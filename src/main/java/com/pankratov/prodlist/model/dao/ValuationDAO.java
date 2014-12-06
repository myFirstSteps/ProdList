package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.Valuation;
import java.util.List;

public interface ValuationDAO {
    List<Valuation> readValuations(Valuation val)throws DAOException;
    boolean addValuation(Valuation val)throws DAOException;
}
