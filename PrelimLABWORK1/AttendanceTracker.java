package PrelimLABWORK1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AttendanceTracker {
    // Main frame for the application
    private JFrame frame;
    
    // Text fields for user input and display
    private JTextField nameField;
    private JTextField courseYearField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    
    // Button to generate attendance record
    private JButton submitButton;
    private JButton clearButton;
    private JButton viewHistoryButton;
    
    // Table for attendance history
    private JTable historyTable;
    private DefaultTableModel tableModel;
    
    // History window
    private JFrame historyFrame;
    
    // Date formatter for consistent display
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor - Initializes the attendance tracker application
     */
    public AttendanceTracker() {
        initialize();
    }
    
    /**
     * Initialize the contents of the frame
     */
    private void initialize() {
        // Create the main frame
        frame = new JFrame("Attendance Tracker - UPHSD Molino");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title Label
        JLabel titleLabel = new JLabel("Student Attendance System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Student Name Label and Field
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(nameLabel, gbc);
        
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(nameField, gbc);
        
        // Course & Year Level Label and Field
        JLabel courseYearLabel = new JLabel("Course/Year:");
        courseYearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(courseYearLabel, gbc);
        
        courseYearField = new JTextField(20);
        courseYearField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(courseYearField, gbc);
        
        // Time In Label and Field
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(timeInLabel, gbc);
        
        timeInField = new JTextField(20);
        timeInField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeInField.setEditable(false);
        timeInField.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(timeInField, gbc);
        
        // E-Signature Label and Field
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(eSignatureLabel, gbc);
        
        eSignatureField = new JTextField(20);
        eSignatureField.setFont(new Font("Arial", Font.PLAIN, 12));
        eSignatureField.setEditable(false);
        eSignatureField.setBackground(Color.LIGHT_GRAY );
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(eSignatureField, gbc);
        
        // Submit Button
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        submitButton = new JButton("Log in");
        submitButton.setFont(new Font("Arial", Font.BOLD, 11));
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 11));
        clearButton.setBackground(new Color(220, 100, 100));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        
        viewHistoryButton = new JButton("View History");
        viewHistoryButton.setFont(new Font("Arial", Font.BOLD, 11));
        viewHistoryButton.setBackground(new Color(60, 179, 113));
        viewHistoryButton.setForeground(Color.BLACK);
        viewHistoryButton.setFocusPainted(false);
        
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(new JLabel("")); // Empty space for balance
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Add action listener to the submit button
        submitButton.addActionListener(e -> generateAttendanceRecord());
        
        // Add action listener to the clear button
        clearButton.addActionListener(e -> clearFields());
        
        // Add action listener to the view history button
        viewHistoryButton.addActionListener(e -> openHistoryWindow());
        
        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Initialize history table model
        initializeHistoryTable();
        
        // Center the frame on screen
        frame.setLocationRelativeTo(null);
        
        // Automatically generate time in and e-signature on startup (without validation)
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);
        timeInField.setText(formattedDateTime);
        
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
    }
    
    /**
     * Initialize the attendance history table model
     */
    private void initializeHistoryTable() {
        // Create table model with column names
        String[] columnNames = {"#", "Attendance Name", "Course/Year", "Time In", "E-Signature"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
    }
    
    /**
     * Open the attendance history window
     */
    private void openHistoryWindow() {
        // Create history window if it doesn't exist or was closed
        if (historyFrame == null || !historyFrame.isVisible()) {
            historyFrame = new JFrame("Attendance History - UPHSD Molino");
            historyFrame.setSize(900, 500);
            historyFrame.setLayout(new BorderLayout(10, 10));
            
            // Create panel for history
            JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
            historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            historyPanel.setBackground(new Color(240, 248, 255));
            
            // History title
            JLabel historyTitle = new JLabel("Attendance History (" + tableModel.getRowCount() + " records)");
            historyTitle.setFont(new Font("Arial", Font.BOLD, 16));
            historyTitle.setHorizontalAlignment(SwingConstants.CENTER);
            historyPanel.add(historyTitle, BorderLayout.NORTH);
            
            // Create table
            historyTable = new JTable(tableModel);
            historyTable.setFont(new Font("Arial", Font.PLAIN, 12));
            historyTable.setRowHeight(25);
            historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            historyTable.getTableHeader().setBackground(new Color(70, 130, 180));
            historyTable.getTableHeader().setForeground(Color.WHITE);
            
            // Set column widths
            historyTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // #
            historyTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
            historyTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Course
            historyTable.getColumnModel().getColumn(3).setPreferredWidth(130); // Time
            historyTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Signature
            
            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(historyTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
            historyPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Add close button
            JButton closeButton = new JButton("Close");
            closeButton.setFont(new Font("Arial", Font.BOLD, 14));
            closeButton.setBackground(new Color(70, 130, 180));
            closeButton.setForeground(Color.BLACK);
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> historyFrame.dispose());
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setBackground(new Color(240, 248, 255));
            bottomPanel.add(closeButton);
            historyPanel.add(bottomPanel, BorderLayout.SOUTH);
            
            // Add history panel to frame
            historyFrame.add(historyPanel);
            
            // Center the history window
            historyFrame.setLocationRelativeTo(frame);
            historyFrame.setVisible(true);
        } else {
            // If window already exists, just bring it to front and update title
            historyFrame.toFront();
            historyFrame.requestFocus();
        }
    }
    
    /**
     * Clear all input fields
     */
    private void clearFields() {
        nameField.setText("");
        courseYearField.setText("");
        
        // Regenerate time and signature
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);
        timeInField.setText(formattedDateTime);
        
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        nameField.requestFocus();
    }
    
    /**
     * Validate course and year level format for UPHSD Molino
     * Expected formats: "BSCS 3rd Year", "BSIT 2nd Year", "BSN 1st Year", etc.
     * @param courseYear The course and year level string to validate
     * @return true if valid, false otherwise
     */
    private boolean validateCourseYear(String courseYear) {
        if (courseYear == null || courseYear.trim().isEmpty()) {
            return false;
        }
        
        // Remove extra spaces and convert to uppercase for checking
        String normalized = courseYear.trim().toUpperCase();
        
        // Valid UPHSD Molino course codes
        String[] validCourses = {
            // Computer Studies
            "BSCS", "BS COMPUTER SCIENCE", "BSIT", "BS INFORMATION TECHNOLOGY", "ACT",
            // Engineering
            "BSCE", "BS CIVIL ENGINEERING", "BSEE", "BS ELECTRICAL ENGINEERING", 
            "BSECE", "BS ELECTRONICS ENGINEERING", "BSME", "BS MECHANICAL ENGINEERING",
            // Medical & Allied Health
            "BSN", "BS NURSING", "BSRT", "BS RADIOLOGIC TECHNOLOGY", "BSP", "BS PHARMACY",
            "BSMT", "BS MEDICAL TECHNOLOGY", "BSPT", "BS PHYSICAL THERAPY",
            // Business & Management
            "BSBA", "BS BUSINESS ADMINISTRATION", "BSHRM", "BS HOTEL & RESTAURANT MANAGEMENT",
            "BSA", "BS ACCOUNTANCY", "BSAIS", "BS ACCOUNTING INFORMATION SYSTEM",
            // Education
            "BEED", "BACHELOR OF ELEMENTARY EDUCATION", "BSED", "BACHELOR OF SECONDARY EDUCATION",
            // Arts & Sciences
            "AB", "BACHELOR OF ARTS", "BS PSYCHOLOGY", "BSPSYCH",
            // Criminology
            "BSCRIM", "BS CRIMINOLOGY"
        };
        
        // Check if it contains year level keywords
        boolean hasYearLevel = normalized.contains("1ST") || normalized.contains("FIRST") ||
                               normalized.contains("2ND") || normalized.contains("SECOND") ||
                               normalized.contains("3RD") || normalized.contains("THIRD") ||
                               normalized.contains("4TH") || normalized.contains("FOURTH") ||
                               normalized.contains("5TH") || normalized.contains("FIFTH") ||
                               normalized.contains("YEAR");
        
        // Check if it contains a valid course code
        boolean hasValidCourse = false;
        for (String course : validCourses) {
            if (normalized.contains(course)) {
                hasValidCourse = true;
                break;
            }
        }
        
        return hasYearLevel && hasValidCourse;
    }
    
    /**
     * Generate attendance record with current date/time and unique e-signature
     */
    private void generateAttendanceRecord() {
        // Validate student name
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please enter your Attendance Name.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        // Validate course and year level
        String courseYear = courseYearField.getText().trim();
        if (courseYear.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please enter your Course/Year.\n\n" +
                "Example formats:\n" +
                "• BSCS 3rd Year\n" +
                "• BSIT 2nd Year\n" +
                "• ACT 1st Year",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            courseYearField.requestFocus();
            return;
        }
        
        // Validate course and year level format
        if (!validateCourseYear(courseYear)) {
            JOptionPane.showMessageDialog(frame,
                "Invalid Course & Year Level format!\n\n" +
                "Please use a valid UPHSD Molino course code:\n\n" +
                "Computer Studies: BSCS, BSIT, ACT\n" +
                "Engineering: BSCE, BSEE, BSECE, BSME\n" +
                "Medical/Health: BSN, BSRT, BSP, BSMT, BSPT\n" +
                "Business: BSBA, BSHRM, BSA, BSAIS\n" +
                "Education: BEED, BSED\n" +
                "Others: BS Psychology, AB, BSCRIM\n\n" +
                "Format: [Course] [Year Level]\n" +
                "Examples: BSCS 3rd Year, BSN 2nd Year\n\n" +
                "Your input: \"" + courseYear + "\"\n\n" +
                "Please try again.",
                "Format Error",
                JOptionPane.ERROR_MESSAGE);
            courseYearField.requestFocus();
            courseYearField.selectAll();
            return;
        }
        
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Format the date and time for better readability
        String formattedDateTime = currentDateTime.format(formatter);
        
        // Set the formatted time in the Time In field
        timeInField.setText(formattedDateTime);
        
        // Generate a unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        
        // Set the e-signature in the E-Signature field
        eSignatureField.setText(eSignature);
        
        // Add to history table
        int rowNumber = tableModel.getRowCount() + 1;
        Object[] rowData = {
            rowNumber,
            nameField.getText().trim(),
            courseYearField.getText().trim(),
            formattedDateTime,
            eSignature
        };
        tableModel.addRow(rowData);
        
        // Show success confirmation message
        JOptionPane.showMessageDialog(frame, 
            "Attendance record generated successfully!\n" +
            "Attendance Name: " + nameField.getText() + "\n" +
            "Course & Year Level: " + courseYearField.getText() + "\n" +
            "Time In: " + formattedDateTime + "\n\n" +
            "Record added to history list.",
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear fields for next entry
        clearFields();
    }
    
    /**
     * Display the frame
     */
    public void show() {
        frame.setVisible(true);
    }
    
    /**
     * Main method - Entry point of the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create and display the attendance tracker
            AttendanceTracker tracker = new AttendanceTracker();
            tracker.show();
        });
    }
}
