package levelup.world.hql;

import javax.persistence.*;
import java.util.Date;


@NamedQueries({
        @NamedQuery(
                name = "mostPopulous",
                query = "from Country where population > 10000000 order by population desc")
})

@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "COUNTRY_ID")
    private Integer id;

    @Column(name = "COUNTRY_NAME")
    private String name;

    private int area;

    @Column(name = "POP")
    private long population;

    @Column(name = "POP_UPD_ON")
    private Date populationUpdatedOn;

    @Transient
    private int rank;

    private String currency;

    // Lazy loading to make SQL more readable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent_id")
    private Continent continent;

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getArea() {
        return area;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulationUpdatedOn(Date populationUpdatedOn) {
        this.populationUpdatedOn = populationUpdatedOn;
    }

    public Date getPopulationUpdatedOn() {
        return populationUpdatedOn;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public Continent getContinent() {
        return continent;
    }

    public String toString() {
        return getName();
    }


}

