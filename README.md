# Prototype til Abakus' programmeringskonkurranse

Enkel implementasjon av crawler, parser og overføring til indeks ifbm. Computas sin programmeringskonkurranse for Abakus.
Skrevet i Kotlin og benytter byggesystemet gradle.

## Sjekk ut koden

---
`$ git clone https://github.com/larsmsp/search-engine.git`
---

## Bygg

Kjør følgende i mappen koden ble sjekket ut til 

---
`$ gradle installDist`
---

Etter bygging vil det legge seg eksekverebare script i
mappen `build/install/bin.

## Kjøring

Scriptet har tre moduser med ulike argumenter.

### Crawling

---
$ build/install/bin/search-engine crawl <baseUrl> <outputDir>
---

For konkurransen til Abakus vil `<baseUrl>` typisk være http://www.computas.com. Crawlingen vil hente alle URL-er under dette
domenet og laste ned sidene som ren HTML til `<outputDir>`

### Parsing

---
$ build/install/bin/search-engine parse <inputDir> <outputDir>
---

Denne delen av scriptet vil lese alle HTML-filer i `<inputDir>` og parse dem til en JSON-struktur som API-et forstår, f.eks.

---
{
    "id": "http://www.computas.com",
    "title": "Computas",
    "contents": "...",
    "url": "http://wwww.computas.com"
}
---

Fordi URL-en er unik per side, så vil parsingen benytte dette som ID til indekseringen senere.
Det vil genereres èn JSON-fil per HTML-fil og disse vil havne i `<outputDir>`

## Indeksering

---
$ build/install/bin/search-engine index <inputDir> <adresse-til-api> <din-api-nøkkel>
---

Scriptet gjør ingen indeksering selv, men dette steget vil lese JSON-filer generert fra `parse`-steget og dytte det ut til Elasticsearch-indeksen.
Hvor filene ligger er angitt av `<inputDir>`. I tillegg trenger scriptet å vite adressen til API-et. I Abakus-konkurransen er denne https://abakus-api-dot-sinuous-tine-156112.appspot.com.
Til slutt vil scriptet ha API-nøkkelen for å vite hvilken indeks det skal legges i.