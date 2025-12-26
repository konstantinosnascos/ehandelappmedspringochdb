# E-handelsapplikation – Java & PostgreSQL

Detta projekt är en konsolbaserad e-handelsapplikation byggd i Java med Spring Boot och PostgreSQL.  
Projektet är utvecklat som en del av kursen **Databaser** och uppfyller samtliga krav i gruppuppgiften.

All funktionalitet nås via ett menybaserat gränssnitt i terminalen.

---

## Teknik & verktyg

- Java 21+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- JUnit 5
- Mockito

---

## Funktionalitet

Applikationen innehåller följande funktioner:

- Produktkatalog
- Kategorisering av produkter
- Kundhantering
- Kundvagn per kund
- Checkout och orderläggning
- Lagerhantering
- Betalningsflöde (simulerat)
- Rapporter via SQL-queries
- Persistens via databas
- Import av data via CSV-filer
- Enhetstester för repository- och service-lager

---

## Domänmodell (Entities)

Systemet består av följande centrala entiteter:

- **Product**
- **Category**
- **Customer**
- **Inventory**
- **Cart**
- **CartItem**
- **Order**
- **OrderItem**
- **Payment**

ER-diagrammet speglar exakt databasschemat i PostgreSQL och inkluderar samtliga relationer:
- One-to-Many
- Many-to-Many
- Foreign Keys
- Constraints

---

## Databasdesign

Databasen är normaliserad enligt **tredje normalformen (3NF)**.

Designprinciper:
- Produkter och kategorier hanteras via kopplingstabell (`product_category`)
- Lager hanteras separat via `inventory`
- Kundvagn och order är separata koncept
- Order och OrderItem möjliggör historik
- Betalning är en egen entitet för statusflöde

Databasen är optimerad för att klara stora datamängder.

---

## Scenarion & datamängder

Applikationen stödjer flera scenarion enligt uppgiften:

### Litet scenario
- Ett fåtal kunder
- Ett fåtal produkter
- Manuella köp via meny

### Mellanstort scenario
- Import av kunder och produkter via CSV
- Flera ordrar per kund
- Lagerpåverkan vid köp

### Stort scenario
- 1000+ kunder
- 1000+ produkter
- 2000+ ordrar
- Rapporter och aggregeringar via SQL

---

## CSV-import

Applikationen kan importera data via CSV-filer:

- Kunder
- Kategorier
- Produkter

Importen sker via `CSVReaderService` och används för att simulera stora datamängder.

---

## Tester

Projektet innehåller både **repository-tester** och **service-tester**.

### Repository-tester
- CustomerRepositoryTests
- ProductRepositoryTests
- InventoryRepositoryTests

### Service-tester
- OrderServiceTests
- InventoryServiceTests

Tester täcker:
- Positiva scenarion
- Negativa scenarion (t.ex. slut i lager)
- Exception-hantering
- Affärslogik

---

## Kör projektet

1. Skapa en PostgreSQL-databas
2. Uppdatera `application.properties`
3. Kör applikationen via:

```bash
mvn spring-boot:run
