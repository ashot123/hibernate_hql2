package levelup.world;

import levelup.world.hql.Continent;
import levelup.world.hql.Country;
import levelup.world.hql.PopulationSummary;
import levelup.world.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ashot Karakhanyan on 21-09-2014
 */
public class Main {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();


        // from clause
        // All properties for all continents
        List<Continent> allContinents = session.createQuery("from Continent").list();
        showContinents(allContinents);

        // where clause (selection)
        // Continents with 'America' anywhere in the name
        List<Continent> theAmericas = session.createQuery(
                "from Continent where name like '%America%'")
                .list();
        showContinents(theAmericas);

        // Complex selection - most common SQL expressions are supported
        // Countries matching a particular name or with no population updated date
        // within a specific area range
        List<Country> countries = session.createQuery(
                "from Country " +
                        "where (populationUpdatedOn is not null " +
                        "or lower(name) in ('gabon', 'gambia')) " +
                        "and area between 100000 AND 100000000")
                .list();
        showCountries(countries);

        // Implicit association joining
        // All countries in Africa
        List<Country> africanCountries = session.createQuery(
                "from Country where continent.name = 'Africa'")
                .list();
        showCountries(africanCountries);

        // Single instance returned with uniqueResult
        // Get the continent with an id of 7
        Continent antarctica = (Continent) session.createQuery(
                "from Continent where id = 7")
                .uniqueResult();
        System.out.println("The continent with an id of 7 is " + antarctica.getName());

        // Limit the number of objects returned setMaxResults
        // Get the country with the highest population
        Country highestPopulation = (Country) session.createQuery(
                "from Country order by population desc")
                .setMaxResults(1)
                .uniqueResult();
        System.out.println(highestPopulation.getName() +
                " has the highest population with " +
                highestPopulation.getPopulation());

        // Sorting
        // Sort the results into ascending (the default) country name within descending area
        List<Country> sortedCountries = session.createQuery(
                "from Country order by area desc, name")
                .list();
        showCountries(sortedCountries);

        // Paging
        // Get page 3 of a list of countries with 2 items per page
        List<Country> page3 = session.createQuery(
                "from Country order by name")
                .setFirstResult(4)
                .setMaxResults(2)
                .list();
        showCountries(page3);

        // SQL Injection
        // Get African countries which contain specified text (in this case 'Ga')
        String userInput = "Ga";
        String searchFor = "%" + userInput.toLowerCase() + "%";
        List<Country> onlyAfricanCountries = session.createQuery(
                "from Country " +
                        "where continent.name = 'Africa' " +
                        "and lower(name) like '" + searchFor + "'")
                .list();
        showCountries(onlyAfricanCountries);

        // Malicious user retrieves ALL countries
        userInput = "' or '%'='".toLowerCase();
        searchFor = "%" + userInput.toLowerCase() + "%";
        onlyAfricanCountries = session.createQuery(
                "from Country " +
                        "where continent.name = 'Africa' " +
                        "and lower(name) like '" + searchFor + "'")
                .list();
        showCountries(onlyAfricanCountries);

        // Parameter binding
        // Get African countries which contain specified text, safely
        userInput = "' or '%'='".toLowerCase();
        searchFor = "%" + userInput.toLowerCase() + "%";
        onlyAfricanCountries = session.createQuery(
                "from Country " +
                        "where continent.name = 'Africa' " +
                        "and lower(name) like :country_name")
                .setParameter("country_name", searchFor)
                .list();
        showCountries(onlyAfricanCountries);

        // Parameter binding and fixed literals
        // Countries within a size range (variable) with a populated date after 1-Jan-09 (fixed)
        countries = session.createQuery(
                "from Country " +
                        "where area between :smallest and :largest " +
                        "and populationUpdatedOn >= '2009-01-01'")
                .setParameter("smallest", 20000)
                .setParameter("largest", 500000)
                .list();
        showCountries(countries);

        // Parameter binding with persistent entities
        // Get the european countries using a previously retrieved continent entity
        Continent europe = (Continent) session.load(Continent.class, 3);
        List<Country> europeanCountries = session.createQuery(
                "from Country where continent = :cont")
                .setParameter("cont", europe)
                .list();
        showCountries(europeanCountries);

        // Null comparison
        // Attempt to get countries where the population updated on is missing (FAILS!)
        List<Country> missingUpdatedOn = session.createQuery(
                "from Country where populationUpdatedOn = :upd_on")
                .setParameter("upd_on", null)
                .list();
        showCountries(missingUpdatedOn);
        // Evaluates to POP_UPD_ON = null which returns nothing

        // Use IS NULL instead
        try {
            Date popUpdOn = new SimpleDateFormat("dd-MMM-yyyy").parse("4-sep-2009");
            popUpdOn = null;

            Query hqlQuery = session.createQuery("from Country where populationUpdatedOn " +
                    (popUpdOn == null ? "is null" : "= :upd_on"));
            if (popUpdOn != null) {
                hqlQuery.setParameter("upd_on", popUpdOn);
            }
            showCountries(hqlQuery.list());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // select clause (projection)
        // Get the European countries
        europeanCountries = session.createQuery(
                "select cont.countries " +
                        "from Continent cont " +
                        "where cont.name = 'Europe'")
                .list();
        showCountries(europeanCountries);

        // Get the continents for all the countries
        List<Continent> continents = session.createQuery(
                "select country.continent " +
                        "from Country country")
                .list();
        showContinents(continents);

        // Single scalar value
        // Get all the country names
        List<String> countryNames = session.createQuery(
                "select country.name from Country country")
                .list();
        showStrings(countryNames);

        // Multiple scalar values
        // Get the country name, continent name and currency for each country
        List<Object[]> countryProperties = session.createQuery(
                "select country.name, country.continent.name, country.currency " +
                        "from Country country")
                .list();
        showObjectArray(countryProperties);

        // Dynamic object instantiation
        // A count of the number of countries in each continent
        List<PopulationSummary> populationDetails = session.createQuery(
                "select new levelup.world.hql.PopulationSummary " +
                        "(country.continent.name, count(*) )" +
                        "from Country country " +
                        "group by country.continent.name")
                .list();
        showPopulationSummary(populationDetails);

        // Inner join without projection
        // African countries
        List<Object[]> continentsWithBigCountries = session.createQuery(
                "from Continent cont join cont.countries country " +
                        "where cont.name = 'Africa'")
                .list();
        showObjectArray(continentsWithBigCountries);

        // Inner join with projection
        // Names of continents with large countries
        List<String> continentNamesWithBigCountries = session.createQuery(
                "select distinct cont.name " +
                        "from Continent cont join cont.countries country " +
                        "where country.area > 100000")
                .list();
        showStrings(continentNamesWithBigCountries);

        // Left outer join
        // Continents with large countries or '[none]' if all countries are small
        List<Object[]> allContinentsAndCountries = session.createQuery(
                "select cont.name, nvl(country.name, '[none]') " +
                        "from Continent cont left join cont.countries country " +
                        "with country.area > 100000 " +
                        "order by cont.name")
                .list();
        showObjectArray(allContinentsAndCountries);

        // Theta-style joins
        // Continents with the same name as a country (of which there are none)
        List<Continent> duplicateNames = session.createQuery(
                "select cont from Continent cont, Country country where cont.name = country.name")
                .list();
        System.out.println("There area " + duplicateNames.size() +
                " continents with same name as a country");

        // Without fetch
        // Get Europe - proxy object is returned for countries
        europe = (Continent) session.createQuery(
                "select cont " +
                        "from Continent cont join cont.countries " +
                        "where cont.name = 'Europe'")
                .uniqueResult();
        System.out.println("Europe has " +
                europe.getCountries().size() +
                " countries");

        // With fetch
        // Use fetch to load the Country objects
        europe = (Continent) session.createQuery(
                "select cont " +
                        "from Continent cont join fetch cont.countries " +
                        "where cont.name = 'Europe'")
                .uniqueResult();
        System.out.println("Europe has " +
                europe.getCountries().size() +
                " countries");

        session.getTransaction().commit();

        HibernateUtil.shutdown();

    }




    private static void showContinents(List<Continent> continents) {
        for (Continent continent : continents) {
            System.out.println(continent.getName());
        }
    }

    private static void showCountries(List<Country> countries) {
        for (Country country : countries) {
            System.out.println(country.getName());
        }
    }

    private static void showPopulationSummary(List<PopulationSummary> popSumms) {
        for (PopulationSummary popSum : popSumms) {
            System.out.println("Continent " + popSum.getContinentName() +
                    " has " + popSum.getCountryCount() + " countries");

        }
    }

    private static void showStrings(List<String> strings) {
        for (String s : strings) {
            System.out.println(s);
        }
    }

    private static void showObjectArray(List<Object[]> objects) {
        for (Object[] o : objects) {
            StringBuffer row = new StringBuffer();
            for (int i = 0; i < o.length; i++) {
                row.append((o[i] == null ? "[null]" : o[i].toString()) + ", ");
            }
            System.out.println(row.deleteCharAt(row.length() - 2));
        }
    }
}
