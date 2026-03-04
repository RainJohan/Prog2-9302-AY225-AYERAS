import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        File file;

        while (true) {

            System.out.print("Enter dataset file path: ");
            String path = input.nextLine();

            file = new File(path);

            if (!file.exists() || !file.isFile() || !file.canRead()) {
                System.out.println("Invalid file path.");
                continue;
            }

            if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("File is not CSV.");
                continue;
            }

            break;
        }

        Map<String, Double> totalSales = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header

            String line;

            while ((line = br.readLine()) != null) {

                List<String> cols = parseCSV(line);

                if (cols.size() < 8) continue;

                String category = cols.get(3).trim(); // genre
                String salesText = cols.get(7).trim(); // total_sales

                if (category.isEmpty()) continue;

                double sales;

                try {
                    sales = Double.parseDouble(salesText);
                } catch (Exception e) {
                    continue;
                }

                totalSales.put(category,
                        totalSales.getOrDefault(category, 0.0) + sales);

                countMap.put(category,
                        countMap.getOrDefault(category, 0) + 1);
            }

            displayResults(totalSales, countMap);

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        input.close();
    }

    // ⭐ Robust CSV parser (handles quoted commas)
    private static List<String> parseCSV(String line) {

        List<String> result = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        boolean insideQuote = false;

        for (char c : line.toCharArray()) {

            if (c == '"') {
                insideQuote = !insideQuote;
            } else if (c == ',' && !insideQuote) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        result.add(sb.toString());

        return result;
    }

    private static void displayResults(Map<String, Double> totals,
                                       Map<String, Integer> counts) {

        String mostProfitable = "";
        String leastProfitable = "";

        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;

        System.out.println("\n=== CATEGORY PROFITABILITY REPORT ===");

        for (String category : totals.keySet()) {

            double total = totals.get(category);
            double avg = total / counts.get(category);

            System.out.printf("\nCategory: %s", category);
            System.out.printf("\nTotal Sales: %.2f", total);
            System.out.printf("\nAverage Sales: %.2f\n", avg);

            if (total > max) {
                max = total;
                mostProfitable = category;
            }

            if (total < min) {
                min = total;
                leastProfitable = category;
            }
        }

        System.out.println("\nMost Profitable Category: " + mostProfitable);
        System.out.println("Least Profitable Category: " + leastProfitable);
    }
}