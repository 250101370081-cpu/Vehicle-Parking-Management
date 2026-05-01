import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

class Vehicle {
    String vehicleNo;
    String ownerName;
    String type;
    LocalDateTime entryTime;

    Vehicle(String vehicleNo, String ownerName, String type) {
        this.vehicleNo = vehicleNo;
        this.ownerName = ownerName;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }
}

class VehicleParkingManagement extends JFrame {

    private JTextField vehicleNoField, ownerField;
    private JComboBox<String> typeBox;
    private JLabel totalVehicleLabel, totalFeeLabel, availableSlotLabel;
    private DefaultTableModel model;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    int totalSlots = 50;
    double totalCollection = 0;

    public VehicleParkingManagement() {

        setTitle("Vehicle Parking Management System");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(1,3,10,10));

        totalVehicleLabel = createCard("Parked: 0", new Color(52,152,219));
        totalFeeLabel = createCard("Total Fee: ₹0", new Color(46,204,113));
        availableSlotLabel = createCard("Slots: 50", new Color(231,76,60));

        topPanel.add(totalVehicleLabel);
        topPanel.add(totalFeeLabel);
        topPanel.add(availableSlotLabel);

        add(topPanel, BorderLayout.NORTH);

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridLayout(6,2,10,10));

        vehicleNoField = new JTextField();
        ownerField = new JTextField();

        typeBox = new JComboBox<>(new String[]{
                "Cycle","Bike","Car","Truck","Bus"
        });

        JButton addBtn = new JButton("Park");
        JButton exitBtn = new JButton("Exit");
        JButton clearBtn = new JButton("Clear");

        styleButton(addBtn, new Color(39,174,96));
        styleButton(exitBtn, new Color(192,57,43));
        styleButton(clearBtn, new Color(52,73,94));

        inputPanel.add(new JLabel("Vehicle No:"));
        inputPanel.add(vehicleNoField);
        inputPanel.add(new JLabel("Owner:"));
        inputPanel.add(ownerField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeBox);
        inputPanel.add(addBtn);
        inputPanel.add(exitBtn);
        inputPanel.add(clearBtn);

        add(inputPanel, BorderLayout.WEST);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Vehicle No","Owner","Type","Entry Time"},0);

        JTable table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== ACTIONS =====
        addBtn.addActionListener(e -> addVehicle());
        exitBtn.addActionListener(e -> exitVehicle());
        clearBtn.addActionListener(e -> clearFields());
    }

    // ===== UI METHODS =====
    private JLabel createCard(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(color);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return l;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    // ===== ADD VEHICLE =====
    private void addVehicle() {
        if (vehicles.size() >= totalSlots) {
            JOptionPane.showMessageDialog(this, "Parking Full!");
            return;
        }

        String vNo = vehicleNoField.getText();
        String owner = ownerField.getText();
        String type = typeBox.getSelectedItem().toString();

        Vehicle v = new Vehicle(vNo, owner, type);
        vehicles.add(v);

        model.addRow(new Object[]{
                vNo, owner, type, v.entryTime.toLocalTime().toString()
        });

        updateDashboard();
    }

    // ===== EXIT + BILL + RECEIPT =====
    private void exitVehicle() {

        String vNo = JOptionPane.showInputDialog(this, "Enter Vehicle No:");

        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle v = vehicles.get(i);

            if (v.vehicleNo.equals(vNo)) {

                LocalDateTime exitTime = LocalDateTime.now();
                long minutes = Duration.between(v.entryTime, exitTime).toMinutes();

                double rate = getFinalRate(v.type);
                double rawFee = (minutes / 60.0) * rate;

                // ROUND OFF
                double finalFee = Math.ceil(rawFee);

                totalCollection += finalFee;

                // ===== RECEIPT =====
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                String period = (rate > getBaseRate(v.type)) ? "Night" : "Day";

                String receipt =
                        "===== PARKING RECEIPT =====\n" +
                                "Vehicle No : " + v.vehicleNo + "\n" +
                                "Owner      : " + v.ownerName + "\n" +
                                "Type       : " + v.type + "\n" +
                                "Entry Time : " + dtf.format(v.entryTime) + "\n" +
                                "Exit Time  : " + dtf.format(exitTime) + "\n" +
                                "Duration   : " + minutes + " minutes\n" +
                                "Period     : " + period + "\n" +
                                "Total Fee  : ₹" + finalFee + "\n" +
                                "===========================\n" +
                                "Thank You! Visit Again";

                JTextArea textArea = new JTextArea(receipt);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
                textArea.setEditable(false);

                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Receipt", JOptionPane.INFORMATION_MESSAGE);

                vehicles.remove(i);
                model.removeRow(i);

                updateDashboard();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Vehicle Not Found!");
    }

    // ===== RATE METHODS =====
    private double getBaseRate(String type) {
        switch(type) {
            case "Cycle": return 10;
            case "Bike": return 20;
            case "Car": return 50;
            case "Truck": return 70;
            default: return 100;
        }
    }

    private double getFinalRate(String type) {
        double baseRate = getBaseRate(type);
        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 6 && hour < 18)
            return baseRate;      // Day
        else
            return baseRate * 1.5; // Night
    }

    // ===== DASHBOARD =====
    private void updateDashboard() {
        totalVehicleLabel.setText("Parked: " + vehicles.size());
        totalFeeLabel.setText("Total Fee: ₹" + totalCollection);
        availableSlotLabel.setText("Slots: " + (totalSlots - vehicles.size()));
    }

    // ===== CLEAR =====
    private void clearFields() {
        vehicleNoField.setText("");
        ownerField.setText("");
        typeBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {}

        SwingUtilities.invokeLater(() -> new VehicleParkingManagement().setVisible(true));
    }
}