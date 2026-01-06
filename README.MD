# E‑handelsapplikation – Java & Spring Boot
Detta projekt är en e‑handelsapplikation byggd i Java med Spring Boot och PostgreSQL.
Applikationen utvecklades ursprungligen som en konsolbaserad skoluppgift med fokus på arkitektur, 
databasdesign och affärslogik, och har därefter vidareutvecklats med ett webblager (Spring MVC + Thymeleaf) 
som del av portföljarbete inför LIA-sökning.

Projektet är medvetet byggt stegvis för att visa hur ett system kan växa från enkel CLI‑applikation till ett webbaserat system med rollstyrd åtkomst.

## Teknikstack
- Språk: Java 17+
- Ramverk: Spring Boot, Spring Data JPA, Spring MVC, Thymeleaf
- Databas: PostgreSQL
- Byggverktyg: Maven
- Testning: JUnit 5, Mockito
- Övrigt: CSV‑import, transaktioner, pagination, rapporter
- Körning & Installation
- Förutsättningar
- Java 17 eller senare
- Maven
- PostgreSQL lokalt installerad
- Databas
- Skapa en tom databas:

## sql
- CREATE DATABASE ecommerce_db;
- Justera vid behov src/main/resources/application.properties:

- properties
- spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
- spring.datasource.username=postgres
- spring.datasource.password=******
- Starta applikationen

```bash```
```mvn spring-boot:run```

Eller kör EcommerceApplication.java från IDE.

## Webblager
När applikationen körs:
http://localhost:8080/

## Funktionalitet – Översikt
- rodukter
- Lista aktiva/inaktiva produkter
- Skapa, aktivera och inaktivera produkter
- Kategorisering (many‑to‑many)
- Lagerstatus via separat Inventory‑entitet
- Kunder
- Skapa, uppdatera och lista kunder
- Välja aktiv kund (session‑baserat)
- Kund kan inte tas bort om den har ordrar
- Kundvagn & Checkout
- Kundvagn per kund
- Lägga till / ta bort produkter
- Kontroll av lagersaldo
- Skapa order från kundvagn
- Simulerad betalning (CARD / INVOICE)
- Ordrar
- Orderhistorik per kund
- Orderstatus (NEW / PAID / CANCELLED)
- Koppling till betalning och orderrader
- Rapporter (Admin)
- Topp 5 mest sålda produkter
- Lågt lager (under vald gräns)
- Omsättning per tidsperiod
- Webblager (Spring MVC)
- Webblagret är byggt med Thymeleaf och följer klassisk MVC‑struktur:

- Controllers hanterar HTTP‑flöden
- Service‑lagret återanvänds från konsolapplikationen
- Templates är uppdelade i återanvändbara fragment (t.ex. navbar)
- Navigation & UX
- Global navbar (Thymeleaf fragment)
- Mini‑cart i navbar (badge + dropdown)
- Pagination i listor
- Flash‑messages och felhantering via ControllerAdvice
- Authorization Design (Rollbaserad åtkomst)
- En central del i vidareutvecklingen är databaserad behörighetsstyrning.

## Roller i databasen
- Systemet använder en klassisk RBAC‑modell:

- roles
- customer_roles (many‑to‑many)
- Exempelroller:

- CUSTOMER
- ADMIN
- REPORT_VIEWER
- Roller kopplas till kunder och kan ändras utan kodändring.

## Åtkomst i två lager
1. UI‑styrning (Presentation)
   Navbar och admin‑länkar visas endast om kunden har rätt roll
   Implementerat med Thymeleaf och Customer.hasRole(...)
2. Backend‑skydd (Controllers)
- Alla /admin/**‑endpoints kontrollerar ADMIN‑roll
-   Direkt URL‑åtkomst blockeras även om UI kringgås
-   Vid otillåten åtkomst kastas AccessDeniedException
-   java
-   if (!customer.hasRole("ADMIN")) {
-   throw new AccessDeniedException("Du saknar behörighet");
-   }
-   Felhantering
-   Central GlobalExceptionHandler
-   Åtkomstfel presenteras tydligt för användaren
-   Detta tvålagerskydd (UI + backend) speglar hur auktorisation hanteras i verkliga system.

## Databasdesign
Systemet använder en normaliserad relationsmodell:

- products, categories, product_category
- inventory (1‑till‑1 med product)
- customers
- carts, cart_items
- orders, order_items
- payments
- roles, customer_roles

## Designen möjliggör:

- historikbevarande
- rapportering
- stora datamängder
- CSV‑import(endast konsol) & Prestanda
- För att simulera realistiska datamängder finns stöd för CSV‑import:

- Produkter
- Kunder
- Ordrar
- En hjälpklass (DataGenerator) kan skapa:

- 1000+ produkter
- 500+ kunder
- 2000+ ordrar
- Importen sker via service‑lagret och är transaktionssäker.

## Tester
- Tester finns under src/test/java.

- Repository‑tester (JPA)
- Service‑tester med Mockito
- Negativa testfall (t.ex. otillräckligt lager)
- Kör:

```bash```
```mvn test```
## Designfilosofi
-Projektet är byggt med fokus på:

- tydlig ansvarsfördelning
- testbar affärslogik
- datadriven design
- möjlighet att växa (t.ex. till Spring Security)
- Applikationen är inte tänkt som ett färdigt kommersiellt system, utan som ett tekniskt case som visar hur man resonerar kring arkitektur, data och säkerhet.

### DTO‑användning

För rapporter och sammanställningar används separata DTO‑klasser
(t.ex. `TopProductDTO`, `LowStockDTO`, `RevenueDTO`) istället för
att exponera JPA‑entiteter direkt.

Detta minskar koppling mellan databasmodell och presentation,
och gör koden lättare att testa och vidareutveckla.