package levelup.world.hql;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "CONTINENT")
public class Continent {

    @Id
    @GeneratedValue
    @Column(name = "CONTINENT_ID")
    private Integer id;

    @Column(name = "CONTINENT_NAME")
    private String name;

    @OneToMany(mappedBy = "continent")
    /*@JoinColumn(name = "continent_id")*/
    private Set<Country> countries = new HashSet<Country>();

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void addCountry(Country country) {
        country.setContinent(this);
        getCountries().add(country);
    }

    public String toString() {
        return getName();
    }

}
