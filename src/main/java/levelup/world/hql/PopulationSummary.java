package levelup.world.hql;

public class PopulationSummary {

    private String continentName;
    private long countryCount;

    public PopulationSummary(String continentName, long countryCount) {

        setContinentName(continentName);
        setCountryCount(countryCount);

    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setCountryCount(long countryCount) {
        this.countryCount = countryCount;
    }

    public long getCountryCount() {
        return countryCount;
    }

}
