package DataTransferObjects;

import components.Movie;
import components.ProductionCompany;

import java.io.Serializable;

public class MovieAddedDTO implements Serializable {
    private boolean status = false;
    private String oldCompany;
    private Movie movie;
    private ProductionCompany productionCompany;

    public MovieAddedDTO(String oldCompany, Movie movie, ProductionCompany productionCompany) {
        this.status = true;
        this.oldCompany = oldCompany;
        this.movie = movie;
        this.productionCompany = productionCompany;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOldCompany() {
        return oldCompany;
    }

    public void setOldCompany(String oldCompany) {
        this.oldCompany = oldCompany;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ProductionCompany getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(ProductionCompany productionCompany) {
        this.productionCompany = productionCompany;
    }
}
