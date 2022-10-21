package DataTransferObjects;

import components.Movie;
import components.ProductionCompany;

import java.io.Serializable;

public class AddMovieDTO implements Serializable {
    private boolean status;
    private Movie movie;
    private ProductionCompany productionCompany;

    public AddMovieDTO(Movie movie, ProductionCompany productionCompany) {
        this.movie = movie;
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

    public ProductionCompany getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(ProductionCompany productionCompany) {
        this.productionCompany = productionCompany;
    }
}
