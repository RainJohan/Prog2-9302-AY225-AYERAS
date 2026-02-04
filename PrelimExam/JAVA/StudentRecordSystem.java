// Programmer: [YOUR FULL NAME] - [YOUR STUDENT ID]
// Student Record System - Java Swing Version

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;

public class StudentRecordSystem extends JFrame {
    
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField idField, firstNameField, lastNameField;
    private JTextField lab1Field, lab2Field, lab3Field, prelimField, attendanceField;
    private JTextField searchField;
    private JButton addButton, deleteButton, clearButton, searchButton, showAllButton;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public StudentRecordSystem() {
        // Set window title with your identifier
        setTitle("Student Records - Rain Johan Ayeras 23-0819-409");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Initialize components
        initializeComponents();
        
        // Load data from CSV
        loadCSVData();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Create table with columns - removed Average column
        String[] columnNames = {"Student ID", "First Name", "Last Name", "Lab 1", "Lab 2", "Lab 3", "Prelim", "Attendance"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Student ID
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(120); // First Name
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Last Name
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Lab 1
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Lab 2
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Lab 3
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Prelim
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Attendance
        
        // Add table sorter for search functionality
        sorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Students"));
        
        searchField = new JTextField(25);
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All");
        
        searchButton.setBackground(new Color(33, 150, 243));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        
        showAllButton.setBackground(new Color(96, 125, 139));
        showAllButton.setForeground(Color.BLACK);
        showAllButton.setFocusPainted(false);
        
        searchPanel.add(new JLabel("Search by Name or ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        
        // Create input panel with GridBagLayout for better organization
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Student"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Initialize text fields
        idField = new JTextField(12);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        lab1Field = new JTextField(8);
        lab2Field = new JTextField(8);
        lab3Field = new JTextField(8);
        prelimField = new JTextField(8);
        attendanceField = new JTextField(8);
        
        // Row 1: Student ID, First Name, Last Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(firstNameField, gbc);
        
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(lastNameField, gbc);
        
        // Row 2: Lab Works and Prelim
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Lab Work 1:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(lab1Field, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Lab Work 2:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(lab2Field, gbc);
        
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Lab Work 3:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(lab3Field, gbc);
        
        // Row 3: Prelim and Attendance
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Prelim Exam:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(prelimField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Attendance:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(attendanceField, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        addButton = new JButton("Add Student");
        deleteButton = new JButton("Delete Selected");
        clearButton = new JButton("Clear All Fields");
        
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFocusPainted(false);
        
        clearButton.setBackground(new Color(255, 152, 0));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Combine panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add event listeners
        addButton.addActionListener(e -> addStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearAllFields());
        searchButton.addActionListener(e -> searchStudents());
        showAllButton.addActionListener(e -> showAllStudents());
        
        // Allow Enter key to search
        searchField.addActionListener(e -> searchStudents());
        
        // Allow Enter key to move between fields
        idField.addActionListener(e -> firstNameField.requestFocus());
        firstNameField.addActionListener(e -> lastNameField.requestFocus());
        lastNameField.addActionListener(e -> lab1Field.requestFocus());
        lab1Field.addActionListener(e -> lab2Field.requestFocus());
        lab2Field.addActionListener(e -> lab3Field.requestFocus());
        lab3Field.addActionListener(e -> prelimField.requestFocus());
        prelimField.addActionListener(e -> attendanceField.requestFocus());
        attendanceField.addActionListener(e -> addStudent());
    }
    
    private void searchStudents() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a name or ID to search!", 
                "Search Empty", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create a filter that searches in ID, First Name, and Last Name columns
        RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + searchText, 0, 1, 2);
        sorter.setRowFilter(rf);
        
        int visibleRows = studentTable.getRowCount();
        JOptionPane.showMessageDialog(this, 
            "Found " + visibleRows + " matching student(s)", 
            "Search Results", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAllStudents() {
        sorter.setRowFilter(null);
        searchField.setText("");
        JOptionPane.showMessageDialog(this, 
            "Showing all students", 
            "Search Cleared", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearAllFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        prelimField.setText("");
        attendanceField.setText("");
        
        // Focus back to ID field
        idField.requestFocus();
        
        JOptionPane.showMessageDialog(this, 
            "All input fields have been cleared!", 
            "Fields Cleared", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadCSVData() {
        BufferedReader reader = null;
        try {
            // Try multiple file path options
            File csvFile = null;
            String[] possiblePaths = {
                "MOCK_DATA.csv",
                "./MOCK_DATA.csv",
                "src/MOCK_DATA.csv",
                "../MOCK_DATA.csv",
                System.getProperty("user.dir") + "/MOCK_DATA.csv",
                System.getProperty("user.dir") + "/src/MOCK_DATA.csv"
            };
            
            for (String path : possiblePaths) {
                File testFile = new File(path);
                if (testFile.exists() && testFile.canRead()) {
                    csvFile = testFile;
                    System.out.println("Found CSV file at: " + testFile.getAbsolutePath());
                    break;
                }
            }
            
            if (csvFile == null) {
                throw new FileNotFoundException("MOCK_DATA.csv not found in any expected location");
            }
            
            reader = new BufferedReader(new FileReader(csvFile));
            String line;
            boolean isHeader = true;
            int rowCount = 0;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Skip header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                // Parse CSV line
                String[] data = line.split(",");
                
                // CSV structure: StudentID, first_name, last_name, LAB WORK 1, LAB WORK 2, LAB WORK 3, PRELIM EXAM, ATTENDANCE GRADE
                if (data.length >= 8) {
                    String studentId = data[0].trim();
                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String lab1 = data[3].trim();
                    String lab2 = data[4].trim();
                    String lab3 = data[5].trim();
                    String prelim = data[6].trim();
                    String attendance = data[7].trim();
                    
                    tableModel.addRow(new Object[]{studentId, firstName, lastName, lab1, lab2, lab3, prelim, attendance});
                    rowCount++;
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                rowCount + " student records loaded successfully!", 
                "Data Loaded", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "WARNING: MOCK_DATA.csv file not found!\n\n" +
                "The program will start with an empty table.\n" +
                "You can still add students manually.\n\n" +
                "Searched in: " + System.getProperty("user.dir") + "\n" +
                "Please ensure MOCK_DATA.csv is in the same directory as StudentRecordSystem.class", 
                "File Not Found", 
                JOptionPane.WARNING_MESSAGE);
            System.err.println("File not found: " + e.getMessage());
            System.err.println("Current directory: " + System.getProperty("user.dir"));
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "ERROR: Could not read MOCK_DATA.csv file!\n\n" +
                "Details: " + e.getMessage(), 
                "IO Error", 
                JOptionPane.ERROR_MESSAGE);
            System.err.println("IO Error: " + e.getMessage());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "ERROR: An unexpected error occurred!\n\n" +
                "Details: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }
    
    private void addStudent() {
        String id = idField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String lab1 = lab1Field.getText().trim();
        String lab2 = lab2Field.getText().trim();
        String lab3 = lab3Field.getText().trim();
        String prelim = prelimField.getText().trim();
        String attendance = attendanceField.getText().trim();
        
        // Validate inputs - all fields required
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            lab1.isEmpty() || lab2.isEmpty() || lab3.isEmpty() || 
            prelim.isEmpty() || attendance.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields are required!\n\nPlease fill in all information.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate ID format (should be numeric)
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Student ID must be a valid number!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            idField.requestFocus();
            return;
        }
        
        // Validate grades (should be numeric and between 0-100)
        String[] grades = {lab1, lab2, lab3, prelim};
        String[] gradeNames = {"Lab Work 1", "Lab Work 2", "Lab Work 3", "Prelim Exam"};
        
        for (int i = 0; i < grades.length; i++) {
            try {
                double grade = Double.parseDouble(grades[i]);
                if (grade < 0 || grade > 100) {
                    JOptionPane.showMessageDialog(this, 
                        gradeNames[i] + " must be between 0 and 100!", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    gradeNames[i] + " must be a valid number!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Validate attendance (should be numeric and non-negative)
        try {
            double attendanceValue = Double.parseDouble(attendance);
            if (attendanceValue < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Attendance must be a non-negative number!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Attendance must be a valid number!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check for duplicate ID
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(id)) {
                JOptionPane.showMessageDialog(this, 
                    "A student with ID " + id + " already exists!\n\n" +
                    "Existing student: " + tableModel.getValueAt(i, 1) + " " + tableModel.getValueAt(i, 2), 
                    "Duplicate ID", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Add new row (without average)
        tableModel.addRow(new Object[]{id, firstName, lastName, lab1, lab2, lab3, prelim, attendance});
        
        // Clear fields
        clearAllFields();
        
        // Show success message
        JOptionPane.showMessageDialog(this, 
            "Student added successfully!\n\n" +
            "ID: " + id + "\n" +
            "Name: " + firstName + " " + lastName + "\n" +
            "Lab 1: " + lab1 + "\n" +
            "Lab 2: " + lab2 + "\n" +
            "Lab 3: " + lab3 + "\n" +
            "Prelim: " + prelim + "\n" +
            "Attendance: " + attendance, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "No row selected!\n\nPlease select a student record to delete.", 
                "Selection Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row to model row (important when filter is active)
        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        
        // Get student details
        String id = tableModel.getValueAt(modelRow, 0).toString();
        String firstName = tableModel.getValueAt(modelRow, 1).toString();
        String lastName = tableModel.getValueAt(modelRow, 2).toString();
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?\n\n" +
            "ID: " + id + "\n" +
            "Name: " + firstName + " " + lastName, 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            
            JOptionPane.showMessageDialog(this, 
                "Student record deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch application
        SwingUtilities.invokeLater(() -> new StudentRecordSystem());
    }
}