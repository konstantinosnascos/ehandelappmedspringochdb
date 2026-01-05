# E-Handelsapplikation (Java Spring Boot)

Detta är en konsolbaserad e-handelsplattform byggd med **Java**, **Spring Boot** och **PostgreSQL**. Applikationen hanterar hela flödet från produktkatalog och lagerhantering till kundvagn, orderläggning och rapportering.

Projektet är utvecklat som en inlämningsuppgift med fokus på arkitektur, databasdesign och hantering av stora datamängder.
---

Teknikstack
*   **Språk:** Java 17+
*   **Ramverk:** Spring Boot 3.x, Spring Data JPA
*   **Databas:** PostgreSQL
*   **Byggverktyg:** Maven
*   **Testning:** JUnit 5, Mockito

---

Installation & Körning

Förberedelser
1.  Se till att du har **Java 17** (eller senare) och **Maven** installerat.
2.  Se till att **PostgreSQL** körs lokalt.

Konfigurera Databas
Skapa en tom databas i PostgreSQL:
```sql
CREATE DATABASE ecommerce_db;

Kontrollera src/main/resources/application.properties och uppdatera vid behov:

spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=ditt_lösenord

##Starta App i webbläsare
Kör programmet och gå till valfri webbläsare och gå till http://localhost:8080/
Än så länge kan man se kunder, lägga till kunder ta bort kunder(som inte har ordrar)
Mer kommer.

## Starta Applikationen
Kör följande kommando i projektets rotmapp:

mvn spring-boot:run

Alternativt kör EcommerceApplication.java från din IDE

## Funktionalitet & Menyer
All interaktion sker via terminalen. Här är huvudflödena:

Produkter:
Lista aktiva produkter.
Sök produkter via SKU.
CRUD (Skapa, Uppdatera, Inaktivera/Aktivera).

Kunder:
Registrera nya kunder.
Uppdatera kunduppgifter.
Lista och sök kunder.

Kundvagn:
Välj aktiv kund (Logga in).
Lägg till/Ta bort varor (kontrollerar lagersaldo).
Töm varukorg.

Order & Checkout:
Skapa order från kundvagn.
Välj betalmetod (Kort/Faktura).
Simulerad betalning (Inventory uppdateras automatiskt).
Se orderhistorik och detaljer.

Rapporter:
Topplista: Bästsäljande produkter.
Lågt lager: Produkter som behöver beställas.
Omsättning: Total försäljning under vald period.

System & Import:
Importera bulk-data via CSV (Produkter, Kunder, Ordrar).

Designbeslut & Arkitektur
1. Separation of Concerns (MVC-ish)
Vi har separerat koden i tydliga lager för att göra den testbar och underhållbar:

Model: JPA Entities (Product, Order, Customer) som speglar databasen.
Repository: Interface som ärver JpaRepository för databasåtkomst.
Service: Innehåller all affärslogik (t.ex. createOrder, checkStock, importCSV). Transaktioner hanteras här med @Transactional.
Menu/UI: Hanterar endast inmatning och utskrift. Använder InputHelper för säker inmatning.
2. Dependency Injection
Vi använder Spring's @Component och @Service för att injicera beroenden. MenuHandler styr flödet utan att behöva skapa nya instanser manuellt.

3. CSV Import & Prestanda
För att hantera kravet på stora datamängder byggde vi en CSVImportService. Istället för att använda data.sql (som gör uppstart långsam), läser vi in data dynamiskt vid behov.

Databas
Systemet använder en relationell databasmodell.

Tabeller:

products: Lagrar produktinfo (sku, namn, pris).
inventory: 1-till-1 relation med Product. Håller lagersaldo.
customers: Kundinfo.
carts & cart_items: Temporär lagring innan köp.
orders & order_items: Historik över köp.
payments: Betalningsstatus kopplat till order.
(Se docs/er-diagram.png för visuell representation)

Scenarios & Testdata
Applikationen stödjer tre datascenarier enligt kravspecifikationen.

1. Enkelt Scenario (Default)
Vid uppstart laddas en mindre mängd data via data.sql (om aktiverat) eller manuell inmatning.

~10 produkter
~5 kunder
2. & 3. Medel & Stort Scenario (CSV Import)
För att testa prestanda med 1000+ produkter och 2000+ ordrar använder vi en Data Generator.

Gör så här:

Kör hjälpklassen com.example.ecommerce.utils.DataGenerator.main().
Detta skapar filerna large_customers.csv, large_products.csv, large_orders.csv i mappen incoming/.
Starta applikationen.
Gå till System & Import -> Importera CSV.
Importera filerna i ordningen: Kunder -> Produkter -> Ordrar.
Detta simulerar en fullskalig e-handel med historik för rapportering.

Testrapport
Enhetstester och integrationstester finns under src/test/java.

Kör tester med:

bash
mvn test
Sammanfattning av tester:

Repository-tester: Verifierar att JPA-queries fungerar mot H2/PostgreSQL.
Service-tester: Mockade tester för affärslogik (t.ex. att lagersaldo minskar vid köp, att undantag kastas vid tomt lager).
Import-tester: Verifierar parsning av CSV-filer.