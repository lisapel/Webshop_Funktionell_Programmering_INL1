# Funktionell_Programmering_INL1

G:
Ett gränssnitt via kommandoraden kan se ut enligt följande: användaren blir promptad att
skriva sitt användarnamn och lösenord (så att vi vet vilken användare som beställer).

Prompta sedan användaren genom att skriva ut alla produkter som finns i lager, användaren
får välja en. Beroende på hur du har modellerat din databas kan användaren behöva
promptas flera gånger för att specifik produkt ska kunna pekas ut (kanske färg, storlek etc.
måste väljas separat?). Skriv den kod som behövs för att detta ska funka med just din
databas. 

När användaren har valt ut en unik produkt anropar du din stored procedure så att
produkten läggs in i aktuell beställningen (eller att en ny beställning, som innehåller vald
produkt skapas upp).

Se till att användaren får återkoppling om allt gick bra eller om fel uppstod när en produkt
lades till i beställningen.

Ett krav är att användaren aldrig ska behöva se databasens interna id:n. Antag att
användaren vill välja en svart sko. Svart har id 32 i databasen. När du listar färger som
användaren ska välja mellan ska ”svart” skrivas ut, inte ”32”.

Använd dig av lambdas på minst 5 ställen (helst mer) i din Java-kod.

VG:
För att få VG måste G-uppgiften vara klar och uppfylla alla krav ovan. Du ska dessutom utveckla en
enkel säljstöds-console-applikation (det är tillåtet att bygga grafiska gränssnitt om man vill, men det
är inget krav) som visar följande rapporter:

1. En rapport som listar alla kunder, med namn och adress, som har handlat varor i en viss
storlek, av en viss färg eller av ett visst märke.

2. En rapport som listar alla kunder och hur många ordrar varje kund har lagt. Skriv ut namn
och sammanlagda antalet ordrar för varje kund.

3. En rapport som listar alla kunder och hur mycket pengar varje kund, sammanlagt, har
beställt för. Skriv ut varje kunds namn och summa.

4. En rapport som listar beställningsvärde per ort. Skriv ut orternas namn och summa.

5. En topplista över de mest sålda produkterna som listar varje modell och hur många ex som
har sålts av den modellen. Skriv ut namn på modellen och hur många ex som sålts.
