/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package semana2prueba3;

import java.util.Scanner;

/**
 *
 * @author alira
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GestorEmpleados gestor = new GestorEmpleados();
        Scanner sc = new Scanner(System.in);
        int opcion;
 
        do {
            System.out.println("\n========== SISTEMA DE NOMINA ==========");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Buscar empleado");
            System.out.println("3. Registrar venta de un empleado");
            System.out.println("4. Pagar empleado");
            System.out.println("5. Verificar si fue pagado este mes");
            System.out.println("6. Mostrar reporte completo de empleado");
            System.out.println("7. Listar empleados registrados");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opcion: ");
 
            opcion = leerEntero(sc);
 
            switch (opcion) {
 
                case 1:
                    gestor.registrarEmpleado(sc);
                    break;
 
                case 2:
                    System.out.print("Ingrese el codigo del empleado: ");
                    int codigoBuscar = leerEntero(sc);
                    gestor.buscarEmpleado(codigoBuscar);
                    break;
 
                case 3:
                    System.out.print("Ingrese el codigo del empleado: ");
                    int codigoVenta = leerEntero(sc);
                    System.out.print("Ingrese el monto de la venta: ");
                    double montoVenta = leerDouble(sc);
                    gestor.addSaleToEmployee(codigoVenta, montoVenta);
                    break;
 
                case 4:
                    System.out.print("Ingrese el codigo del empleado: ");
                    int codigoPago = leerEntero(sc);
                    gestor.payEmployee(codigoPago);
                    break;
 
                case 5:
                    System.out.print("Ingrese el codigo del empleado: ");
                    int codigoVerificar = leerEntero(sc);
 
                    if (!gestor.existeEmpleado(codigoVerificar)) {
                        System.out.println("Empleado no encontrado.");
                    } else if (gestor.isEmployeePayed(codigoVerificar)) {
                        System.out.println("El empleado YA fue pagado este mes.");
                    } else {
                        System.out.println("El empleado AUN NO ha sido pagado este mes.");
                    }
                    break;
 
                case 6:
                    System.out.print("Ingrese el codigo del empleado: ");
                    int codigoReporte = leerEntero(sc);
                    gestor.printEmployee(codigoReporte);
                    break;
 
                case 7:
                    gestor.listarEmpleados();
                    break;
 
                case 8:
                    System.out.println("Saliendo del sistema...");
                    break;
 
                default:
                    System.out.println("Opcion no valida, intente de nuevo.");
            }
 
        } while (opcion != 8);
 
        sc.close();
    }
 
    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Entrada invalida. Ingrese un numero entero: ");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }
 
    private static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Entrada invalida. Ingrese un numero: ");
            sc.next();
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }
    
}
