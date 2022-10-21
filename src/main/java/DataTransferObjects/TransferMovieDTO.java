package DataTransferObjects;

import components.Movie;
import components.ProductionCompany;

import java.io.Serializable;

public class TransferMovieDTO implements Serializable {
    private boolean status;
    private Movie movie;
    private String oldCompany;
    private String newCompany;
    private ProductionCompany productionCompany;

    public TransferMovieDTO(Movie movie, String oldCompany, String newCompany, ProductionCompany productionCompany) {
        this.movie = movie;
        this.oldCompany = oldCompany;
        this.newCompany = newCompany;
        this.productionCompany = productionCompany;
        status = true;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getOldCompany() {
        return oldCompany;
    }

    public void setOldCompany(String oldCompany) {
        this.oldCompany = oldCompany;
    }

    public String getNewCompany() {
        return newCompany;
    }

    public void setNewCompany(String newCompany) {
        this.newCompany = newCompany;
    }

    public ProductionCompany getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(ProductionCompany productionCompany) {
        this.productionCompany = productionCompany;
    }
}
