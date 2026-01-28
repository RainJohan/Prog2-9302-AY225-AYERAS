package Java;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;

public class PrelimGradeCalculator extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    
    // Constants
    private static final int MAX_ATTENDANCE = 5;
    private static final int MAX_ABSENCES_ALLOWED = 3;
    
    // GUI Components
    private JTextField attendanceField, lab1Field, lab2Field, lab3Field;
    private JTextArea resultArea;
    private JButton calculateButton, clearButton;
    private JLabel titleLabel, subtitleLabel;
    
    // Blue color theme
    private final Color primaryColor = new Color(59, 130, 246);      // Blue-500
    private final Color lightBlue = new Color(191, 219, 254);         // Blue-200
    private final Color successColor = new Color(34, 197, 94);
    private final Color dangerColor = new Color(239, 68, 68);
    
    public PrelimGradeCalculator() {
        // Set up the frame
        setTitle("Prelim Grade Calculator");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with animated gradient background
        JPanel mainPanel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Create multi-stop gradient with blue theme
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(59, 130, 246),      // Blue-500
                    w, h, new Color(29, 78, 216)        // Blue-700
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                // Add decorative circles with enhanced transparency
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                g2d.setColor(lightBlue);
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(w - 300, h - 300, 400, 400);
                
                // Add middle circle for more depth
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                g2d.setColor(Color.WHITE);
                g2d.fillOval(w / 2 - 150, h / 2 - 150, 300, 300);
                
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // Add sparkle effects
                g2d.setColor(new Color(255, 255, 255, 150));
                for (int i = 0; i < 25; i++) {
                    int x = (int) (Math.random() * w);
                    int y = (int) (Math.random() * h);
                    int size = (int) (Math.random() * 3) + 1;
                    g2d.fillOval(x, y, size, size);
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Title Label with icon and shadow effect
        titleLabel = new JLabel("📊 PRELIM GRADE CALCULATOR", SwingConstants.CENTER) {
            @Serial
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x + 3, y + 3);
                
                // Draw main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 25, 600, 40);
        mainPanel.add(titleLabel);
        
        // Subtitle
        subtitleLabel = new JLabel("Calculate your required exam scores", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(224, 242, 254));  // Blue-50
        subtitleLabel.setBounds(50, 65, 600, 25);
        mainPanel.add(subtitleLabel);
        
        // Input Panel with shadow effect
        JPanel inputPanel = new RoundedPanel(20);
        inputPanel.setLayout(null);
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBounds(60, 110, 580, 320);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        // Input fields with modern styling
        int yPos = 20;
        int spacing = 70;
        
        // Attendance
        addInputField(inputPanel, "👥 Number of Attendances (Max: 5)", attendanceField = createStyledTextField(MAX_ATTENDANCE), yPos);
        yPos += spacing;
        
        // Lab Work 1
        addInputField(inputPanel, "📝 Lab Work 1 Grade", lab1Field = createStyledTextField(100), yPos);
        yPos += spacing;
        
        // Lab Work 2
        addInputField(inputPanel, "📝 Lab Work 2 Grade", lab2Field = createStyledTextField(100), yPos);
        yPos += spacing;
        
        // Lab Work 3
        addInputField(inputPanel, "📝 Lab Work 3 Grade", lab3Field = createStyledTextField(100), yPos);
        
        mainPanel.add(inputPanel);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(60, 450, 580, 60);
        
        // Calculate Button with enhanced design
        calculateButton = createStyledButton("✓ Calculate", successColor);
        calculateButton.setPreferredSize(new Dimension(200, 50));
        calculateButton.addActionListener(new CalculateButtonListener());
        
        // Clear Button
        clearButton = createStyledButton("↻ Clear", dangerColor);
        clearButton.setPreferredSize(new Dimension(150, 50));
        clearButton.addActionListener(new ClearButtonListener());
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        mainPanel.add(buttonPanel);
        
        // Results Panel with modern styling
        JPanel resultsPanel = new RoundedPanel(20);
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBackground(new Color(248, 250, 252));
        resultsPanel.setBounds(60, 530, 580, 220);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Results header
        JLabel resultsHeader = new JLabel("📋 Results", JLabel.LEFT);
        resultsHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsHeader.setForeground(new Color(51, 65, 85));
        resultsHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        resultsPanel.add(resultsHeader, BorderLayout.NORTH);
        
        // Results Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(248, 250, 252));
        resultArea.setForeground(new Color(51, 65, 85));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.setBackground(new Color(248, 250, 252));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(resultsPanel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Helper method to create styled text fields
    private JTextField createStyledTextField(int maxValue) {
        JTextField field = new JTextField() {
            @Serial
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        
        // Apply document filter to limit input
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumberLimitFilter(maxValue));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        field.setBackground(new Color(248, 250, 252));
        field.setForeground(new Color(30, 41, 59));
        field.setCaretColor(primaryColor);
        field.setOpaque(false);
        
        // Focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                field.setBackground(new Color(239, 246, 255));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                field.setBackground(new Color(248, 250, 252));
            }
        });
        
        return field;
    }
    
    // Helper method to add input fields with labels
    private void addInputField(JPanel panel, String labelText, JTextField field, int yPos) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(51, 65, 85));
        label.setBounds(20, yPos, 300, 25);
        panel.add(label);
        
        field.setBounds(280, yPos, 270, 45);
        panel.add(field);
    }
    
    // Helper method to create styled buttons with enhanced effects
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Serial
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = bgColor.darker();
                } else if (getModel().isRollover()) {
                    buttonColor = bgColor.brighter();
                } else {
                    buttonColor = bgColor;
                }
                
                g2.setColor(buttonColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Add shine effect at the top
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 12, 12);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // Draw text with shadow
                g2.setColor(new Color(0, 0, 0, 80));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX + 1, textY + 1);
                
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }
    
    // Calculate Button Listener
    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get input values
                double attendance = Double.parseDouble(attendanceField.getText().trim());
                double lab1 = Double.parseDouble(lab1Field.getText().trim());
                double lab2 = Double.parseDouble(lab2Field.getText().trim());
                double lab3 = Double.parseDouble(lab3Field.getText().trim());
                
                // Validate inputs
                if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                    showStyledMessage("Lab grades must be between 0 and 100!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (attendance < 0 || attendance > MAX_ATTENDANCE) {
                    showStyledMessage("Attendance must be between 0 and " + MAX_ATTENDANCE + "!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calculate absences
                int absences = MAX_ATTENDANCE - (int) attendance;
                int excusedAbsences = 0;
                
                // Check if attendance is low and ask about excused absences
                if (attendance < MAX_ATTENDANCE) {
                    int response = JOptionPane.showConfirmDialog(
                        PrelimGradeCalculator.this,
                        "You have " + absences + " absence(s).\nDo you have any excused absences?",
                        "Excused Absences",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (response == JOptionPane.YES_OPTION) {
                        // Loop until valid input is received
                        boolean validInput = false;
                        while (!validInput) {
                            String input = JOptionPane.showInputDialog(
                                PrelimGradeCalculator.this,
                                "How many excused absences do you have?\n(Maximum: " + absences + ")\n\nNote: Each excused absence will count as +1 attendance\nfor grade calculation.",
                                "Enter Excused Absences",
                                JOptionPane.QUESTION_MESSAGE
                            );
                            
                            // If user cancels, treat as 0 excused absences
                            if (input == null) {
                                excusedAbsences = 0;
                                validInput = true;
                            } else if (input.trim().isEmpty()) {
                                excusedAbsences = 0;
                                validInput = true;
                            } else {
                                try {
                                    int tempExcused = Integer.parseInt(input.trim());
                                    
                                    if (tempExcused < 0) {
                                        showStyledMessage(
                                            "Excused absences cannot be negative!\nPlease try again.",
                                            "Invalid Input",
                                            JOptionPane.ERROR_MESSAGE
                                        );
                                    } else if (tempExcused > absences) {
                                        showStyledMessage(
                                            "Excused absences (" + tempExcused + ") cannot exceed total absences (" + absences + ")!\nPlease try again.",
                                            "Invalid Input",
                                            JOptionPane.ERROR_MESSAGE
                                        );
                                    } else {
                                        excusedAbsences = tempExcused;
                                        validInput = true;
                                    }
                                } catch (NumberFormatException ex) {
                                    showStyledMessage(
                                        "Please enter a valid whole number!\nTry again.",
                                        "Invalid Input",
                                        JOptionPane.ERROR_MESSAGE
                                    );
                                }
                            }
                        }
                    }
                }
                
                // Calculate unexcused absences
                int unexcusedAbsences = absences - excusedAbsences;
                
                // Check if unexcused absences exceed the allowed limit - AUTOMATIC FAILURE
                if (unexcusedAbsences > MAX_ABSENCES_ALLOWED) {
                    showStyledMessage(
                        "⚠️ AUTOMATIC FAILURE\n\n" +
                        "You have " + unexcusedAbsences + " unexcused absence(s)\n\n" +
                        "You have FAILED due to excessive unexcused absences!\n" +
                        "Maximum allowed unexcused absences: " + MAX_ABSENCES_ALLOWED + "\n" +
                        "Your unexcused absences: " + unexcusedAbsences + "\n" +
                        "Your excused absences: " + excusedAbsences + "\n\n" +
                        "You cannot proceed with grade calculation.\n" +
                        "Please attend more classes to meet the requirement.",
                        "Too Many Absences - Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return; // Stop here - don't calculate or show results
                }
                
                // Add excused absences to attendance for calculation (each excused = +1 attendance)
                double effectiveAttendance = attendance + excusedAbsences;
                
                // Convert effective attendance to percentage (0-100 scale)
                double attendancePercentage = (effectiveAttendance / MAX_ATTENDANCE) * 100;
                
                // Calculate Lab Work Average
                double labWorkAverage = (lab1 + lab2 + lab3) / 3;
                
                // Calculate Class Standing
                double classStanding = (attendancePercentage * 0.40) + (labWorkAverage * 0.60);
                
                // Calculate required Prelim Exam scores
                double requiredPrelimForPassing = (75 - (classStanding * 0.70)) / 0.30;
                double requiredPrelimForExcellent = (100 - (classStanding * 0.70)) / 0.30;
                
                // Build result text with enhanced formatting
                StringBuilder result = new StringBuilder();
                
                result.append("╔════════════════════════════════════════╗\n");
                result.append("║         COMPUTATION RESULTS            ║\n");
                result.append("╚════════════════════════════════════════╝\n\n");
                
                result.append(String.format("📊 Actual Attendance:    %d/%d\n", (int)attendance, MAX_ATTENDANCE));
                result.append(String.format("📊 Total Absences:       %d\n", absences));
                result.append(String.format("📊 Excused Absences:     %d\n", excusedAbsences));
                result.append(String.format("📊 Unexcused Absences:   %d/%d allowed ✓\n", unexcusedAbsences, MAX_ABSENCES_ALLOWED));
                result.append(String.format("📊 Effective Attendance: %d/%d (%.2f%%) [includes excused]\n", (int)effectiveAttendance, MAX_ATTENDANCE, attendancePercentage));
                
                result.append(String.format("📝 Lab Work 1:           %.2f\n", lab1));
                result.append(String.format("📝 Lab Work 2:           %.2f\n", lab2));
                result.append(String.format("📝 Lab Work 3:           %.2f\n", lab3));
                result.append(String.format("📈 Lab Work Average:     %.2f\n", labWorkAverage));
                result.append(String.format("⭐ Class Standing:       %.2f\n\n", classStanding));
                
                result.append("╔════════════════════════════════════════╗\n");
                result.append("║     REQUIRED PRELIM EXAM SCORES        ║\n");
                result.append("╚════════════════════════════════════════╝\n\n");
                
                // Passing requirement
                result.append(String.format("✅ To PASS (75%%): %.2f\n", requiredPrelimForPassing));
                if (requiredPrelimForPassing > 100) {
                    result.append("❌ It is NOT possible to PASS. This exceeds the maximum possible score (100). You have failed even if you score 100 on the Prelim Exam.\n");
                } else if (requiredPrelimForPassing <= 0) {
                    result.append("🎉 Remark: You have already secured a passing grade!\n");
                    result.append("    You will pass even with a score of 0.\n");
                } else if (requiredPrelimForPassing <= 100) {
                    result.append("💪 Remark: You need to score at least this\n");
                    result.append("    on the Prelim Exam to pass.\n");
                }
                
                result.append("\n");
                
                // Excellent requirement
                result.append(String.format("🏆 To achieve EXCELLENT (100%%): %.2f\n", requiredPrelimForExcellent));
                if (requiredPrelimForExcellent > 100) {
                    result.append("❌ Remark: It is impossible to achieve\n");
                    result.append("    an excellent grade of 100.\n");
                    result.append("    The required score exceeds 100.\n");
                } else if (requiredPrelimForExcellent <= 0) {
                    result.append("🌟 Remark: You have already achieved excellent standing!\n");
                    result.append("    You will get 100 with any Prelim Exam score.\n");
                } else if (requiredPrelimForExcellent <= 100) {
                    if (requiredPrelimForExcellent <= 50) {
                        result.append("🎯 Remark: Great job! Your class standing is excellent.\n");
                        result.append("    You only need a moderate score on the\n");
                        result.append("    Prelim Exam to achieve a perfect grade.\n");
                    } else {
                        result.append("🎓 Remark: You need to score at least this\n");
                        result.append("    on the Prelim Exam to achieve\n");
                        result.append("    an excellent grade of 100.\n");
                    }
                }
                
                result.append("\n╚════════════════════════════════════════╝");
                
                resultArea.setText(result.toString());
                resultArea.setCaretPosition(0);
                
            } catch (NumberFormatException ex) {
                showStyledMessage("Please enter valid numbers in all fields!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Clear Button Listener
    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            attendanceField.setText("");
            lab1Field.setText("");
            lab2Field.setText("");
            lab3Field.setText("");
            resultArea.setText("");
            attendanceField.requestFocus();
        }
    }
    
    // Helper method for styled message dialogs
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", new Color(51, 65, 85));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    // Rounded Panel class
    class RoundedPanel extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;
        private final int cornerRadius;
        
        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g);
        }
    }
    
    // Shadow Border class for depth effect
    class ShadowBorder implements Border {
        @Serial
        private static final long serialVersionUID = 1L;
        private final int shadowSize = 8;
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            for (int i = 0; i < shadowSize; i++) {
                int alpha = (int) (50 * (1 - (i / (float) shadowSize)));
                g2.setColor(new Color(0, 0, 0, alpha));
                g2.drawRoundRect(x + i, y + i, width - (i * 2) - 1, height - (i * 2) - 1, 20, 20);
            }
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
    
    // Document Filter to limit input to numbers with max value
    class NumberLimitFilter extends DocumentFilter {
        private final int maxValue;
        
        public NumberLimitFilter(int maxValue) {
            this.maxValue = maxValue;
        }
        
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            
            if (isValid(fb, offset, string, 0)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            
            if (isValid(fb, offset, text, length)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
        
        private boolean isValid(FilterBypass fb, int offset, String text, int length) {
            try {
                Document doc = fb.getDocument();
                String currentText = doc.getText(0, doc.getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                
                // Allow empty string
                if (newText.isEmpty()) return true;
                
                // Only allow whole numbers (no decimals)
                if (!newText.matches("^\\d+$")) return false;
                
                // Prevent leading zeros (except "0")
                if (newText.length() > 1 && newText.startsWith("0")) {
                    return false;
                }
                
                // Check if the number is within range
                int value = Integer.parseInt(newText);
                return value >= 0 && value <= maxValue;
                
            } catch (NumberFormatException | BadLocationException e) {
                return false;
            }
        }
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new PrelimGradeCalculator());
    }
}