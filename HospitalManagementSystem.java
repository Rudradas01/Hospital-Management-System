import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {


    private static final String url = "jdbc:mysql://127.0.0.1:3306/Hospital";

    private static final String username = "root";

    private static final String password = "Rudradas@132";



    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url, username , password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1, Add patient");
                System.out.println("2, View Patients");
                System.out.println("3, View Doctors");
                System.out.println("4, Book Appointment");
                System.out.println("5, Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                    //Add patient
                    patient.addpatient();
                    System.out.println();
                    case 2:
                    //view patient
                    patient.viewpatient();
                    System.out.println();
                    case 3:
                    //view doctors
                    doctor.viewDoctors();
                    System.out.println();
                    case 4:
                    //book Appointment
                    bookAppointment(patient, doctor, connection, scanner);
                    System.out.println();
                    case 5:
                    return;
                
                    default:
                        System.out.println("Enter valid choice!!");
                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        
    }



public static void bookAppointment(Patient patient, Doctor doctor,Connection connection, Scanner scanner){
    System.out.println("Enter Patient Id: ");
    int patientId = scanner.nextInt();
    System.out.print("Enter Doctor Id: ");
    int doctorId = scanner.nextInt();
    System.out.println("Enter appoinntment date (YYYY_MM_DD): ");
    String appointmentDate = scanner.next();
    if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
        if(checkDoctorAvailability(doctorId, connection, appointmentDate)){
                    String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?) ";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                        preparedStatement.setInt(1, patientId);
                        preparedStatement.setInt(2, doctorId);
                        preparedStatement.setString(3, appointmentDate);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Appointment Booked");
                        }else{
                            System.out.println("Failed to Book Appointment");
        
                        }
        
                    } catch (SQLException e){
                        e.printStackTrace();
                    } 
                }else{
                    System.out.println("Doctor not available on this date!!");

                }
            }else{
                System.out.println("Either docotr or patient doesn't exist!!!");

            }
        
        }
        
        
        
        private static boolean checkDoctorAvailability(int doctorId,Connection connection,String appointmentDate) {

            String  Query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(Query);
                preparedStatement.setInt(1, doctorId);
                preparedStatement.setString(2, appointmentDate);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    int count = resultSet.getInt(1);
                    if(count==0){
                        return true;
                    }else{
                        return false;
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
                        return false;

          
        }
    
}
