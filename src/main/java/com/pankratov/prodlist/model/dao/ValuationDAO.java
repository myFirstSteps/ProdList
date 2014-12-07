package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.Valuation;
import java.util.*;

public interface ValuationDAO {
    Set<Valuation> readValuations(Valuation val)throws DAOException;
    boolean addValuation(Valuation val)throws DAOException;
}
