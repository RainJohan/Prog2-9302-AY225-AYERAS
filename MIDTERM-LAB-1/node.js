const fs = require('fs');
const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function askFilePath() {

    rl.question("Enter dataset file path: ", function(path) {

        if (!fs.existsSync(path)) {
            console.log("Invalid file path.");
            return askFilePath();
        }

        if (!path.toLowerCase().endsWith(".csv")) {
            console.log("File is not CSV.");
            return askFilePath();
        }

        try {
            const data = fs.readFileSync(path, 'utf8');
            processData(data);

        } catch (err) {
            console.log("File not readable.");
            askFilePath();
        }
    });
}

function processData(data) {

    const lines = data.split('\n');
    lines.shift();

    let totals = {};
    let counts = {};

    lines.forEach(line => {

        if (!line.trim()) return;

        const cols = parseCSV(line);

        if (cols.length < 8) return;

        const category = cols[3]?.trim();
        const sales = parseFloat(cols[7]);

        if (!category || isNaN(sales)) return;

        if (!totals[category]) {
            totals[category] = 0;
            counts[category] = 0;
        }

        totals[category] += sales;
        counts[category]++;
    });

    displayResults(totals, counts);
    rl.close();
}

function parseCSV(line) {

    let result = [];
    let current = "";
    let insideQuote = false;

    for (let char of line) {

        if (char === '"') {
            insideQuote = !insideQuote;
        }
        else if (char === ',' && !insideQuote) {
            result.push(current);
            current = "";
        }
        else {
            current += char;
        }
    }

    result.push(current);

    return result;
}

function displayResults(totals, counts) {

    let mostProfitable = "";
    let leastProfitable = "";

    let max = -Infinity;
    let min = Infinity;

    console.log("\n=== CATEGORY PROFITABILITY REPORT ===");

    for (let category in totals) {

        let total = totals[category];
        let avg = total / counts[category];

        console.log(`\nCategory: ${category}`);
        console.log(`Total Sales: ${total.toFixed(2)}`);
        console.log(`Average Sales: ${avg.toFixed(2)}`);

        if (total > max) {
            max = total;
            mostProfitable = category;
        }

        if (total < min) {
            min = total;
            leastProfitable = category;
        }
    }

    console.log("\nMost Profitable Category:", mostProfitable);
    console.log("Least Profitable Category:", leastProfitable);
}

askFilePath();