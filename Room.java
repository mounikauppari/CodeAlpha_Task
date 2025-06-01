package codealpha;

import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category; // Standard, Deluxe, Suite
    boolean isBooked;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }

    public String toString() {
        return "Room " + roomNumber + " [" + category + "] - " + (isBooked ? "Booked" : "Available");
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    String date;

    public Booking(String customerName, int roomNumber, String category, String date) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.date = date;
    }

    public String toString() {
        return "Booking: " + customerName + " - Room " + roomNumber + " (" + category + ") on " + date;
    }
}

 class HotelReservationSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static final String ROOMS_FILE = "rooms.txt";
    static final String BOOKINGS_FILE = "bookings.txt";

    public static void main(String[] args) {
        loadRooms();
        loadBookings();

        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");
            System.out.print("Select option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: viewAvailableRooms(); break;
                case 2: bookRoom(); break;
                case 3: cancelBooking(); break;
                case 4: viewBookings(); break;
                case 5: saveData(); System.out.println("Thank you! Exiting."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (!room.isBooked) {
                System.out.println(room);
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter room category (Standard/Deluxe/Suite): ");
        String category = scanner.nextLine();

        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (!room.isBooked && room.category.equalsIgnoreCase(category)) {
                available.add(room);
            }
        }

        if (available.isEmpty()) {
            System.out.println("No rooms available in " + category + " category.");
            return;
        }

        Room room = available.get(0); // pick the first available
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        // Simulate payment
        System.out.println("Room price: $" + getPriceByCategory(room.category));
        System.out.print("Simulate payment? (yes/no): ");
        if (!scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("Booking cancelled.");
            return;
        }

        room.isBooked = true;
        Booking booking = new Booking(name, room.roomNumber, room.category, date);
        bookings.add(booking);
        System.out.println("Booking successful:\n" + booking);
    }

    static void cancelBooking() {
        System.out.print("Enter your name to cancel booking: ");
        String name = scanner.nextLine();

        Iterator<Booking> iterator = bookings.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.customerName.equalsIgnoreCase(name)) {
                for (Room room : rooms) {
                    if (room.roomNumber == b.roomNumber) {
                        room.isBooked = false;
                        break;
                    }
                }
                iterator.remove();
                found = true;
                System.out.println("Booking cancelled.");
            }
        }

        if (!found) {
            System.out.println("No booking found for " + name);
        }
    }

    static void viewBookings() {
        System.out.println("\n--- Current Bookings ---");
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    static void loadRooms() {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            // Create sample rooms
            for (int i = 1; i <= 5; i++) rooms.add(new Room(i, "Standard"));
            for (int i = 6; i <= 8; i++) rooms.add(new Room(i, "Deluxe"));
            for (int i = 9; i <= 10; i++) rooms.add(new Room(i, "Suite"));
        } else {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    Room room = new Room(Integer.parseInt(parts[0]), parts[1]);
                    room.isBooked = Boolean.parseBoolean(parts[2]);
                    rooms.add(room);
                }
            } catch (IOException e) {
                System.out.println("Error loading rooms: " + e.getMessage());
            }
        }
    }

    static void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    bookings.add(new Booking(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]));
                }
            } catch (IOException e) {
                System.out.println("Error loading bookings: " + e.getMessage());
            }
        }
    }

    static void saveData() {
        try (PrintWriter pw = new PrintWriter(ROOMS_FILE)) {
            for (Room r : rooms) {
                pw.println(r.roomNumber + "," + r.category + "," + r.isBooked);
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(BOOKINGS_FILE)) {
            for (Booking b : bookings) {
                pw.println(b.customerName + "," + b.roomNumber + "," + b.category + "," + b.date);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    static int getPriceByCategory(String category) {
        switch (category.toLowerCase()) {
            case "standard": return 100;
            case "deluxe": return 200;
            case "suite": return 300;
            default: return 150;
        }
    }
}