package DataTransferObjects;

import components.ProductionCompany;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ProductionCompanyDTO implements Serializable {
    private ConcurrentHashMap<String,String> trailerList;
    private ProductionCompany productionCompany;

    public ConcurrentHashMap<String, String> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ConcurrentHashMap<String, String> trailerList) {
        this.trailerList = trailerList;
    }

    public ProductionCompany getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(ProductionCompany productionCompany) {
        this.productionCompany = productionCompany;
    }
}
