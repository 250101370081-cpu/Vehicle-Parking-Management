# Vehicle Parking Management System

A desktop-based Graphical User Interface (GUI) application built with Java and Swing to manage vehicle parking operations seamlessly. It tracks incoming vehicles, calculates parking duration, dynamically adjusts fees based on the time of day, and generates a structured receipt upon exit.

## 🌟 Features

*   **Vehicle Entry**: Register incoming vehicles by inputting the Vehicle Number, Owner Name, and selecting the vehicle type (Cycle, Bike, Car, Truck, or Bus).
*   **Real-time Dashboard**: A color-coded top panel displays the number of currently parked vehicles, the total fees collected, and the number of available slots (out of a 50-slot capacity).
*   **Live Tracking Table**: View all currently parked vehicles along with their details and exact entry time in a scrollable table layout.
*   **Automated Checkout & Billing**: Process exiting vehicles using their Vehicle Number. The system automatically calculates the duration parked in minutes.
*   **Dynamic Pricing**: Applies base rates during the day (6:00 AM to 6:00 PM). It automatically applies a 1.5x multiplier for nighttime parking.
*   **Receipt Generation**: Generates a detailed, monospaced pop-up receipt showing entry/exit times, duration, day/night period, and the final rounded fee.

## 💰 Parking Rates

The application calculates fees per hour (prorated by the minute) using the following base rates:

*   **Cycle**: ₹10
*   **Bike**: ₹20
*   **Car**: ₹50
*   **Truck**: ₹70
*   **Bus/Other**: ₹100

*Note: Vehicles parked during nighttime hours (18:00 to 05:59) are charged at 1.5x the base rate.*

## 🛠️ Technology Stack

*   **Language**: Java (Standard Edition)
*   **GUI Framework**: Java Swing & AWT
*   **Date & Time API**: `java.time` (`LocalDateTime`, `Duration`, `DateTimeFormatter`) for precise duration tracking
*   **Look and Feel**: Nimbus LookAndFeel for a modernized, clean aesthetic

## 🚀 How to Run

### Prerequisites
*   Java Development Kit (JDK) 8 or higher installed on your system.

### Running the Application
1. Clone the repository to your local machine:
   ```bash
   git clone [https://github.com/your-username/your-repo-name.git](https://github.com/your-username/your-repo-name.git)
