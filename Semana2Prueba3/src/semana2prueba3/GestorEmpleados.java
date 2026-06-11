/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana2prueba3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author alira
 */
public class GestorEmpleados {
 
    private static final String CARPETA_EMPLEADOS = "empleados";
    private static final int LONGITUD_NOMBRE = 50;
    private static final int TAM_REGISTRO_VENTA = 9;
    private static final int TAM_REGISTRO_RECIBO = (4 * 5) + (8 * 2);
 
 
    private File getCarpetaEmpleado(int codigo) {
        return new File(CARPETA_EMPLEADOS, String.valueOf(codigo));
    }
 
    public boolean existeEmpleado(int codigo) {
        File carpeta = getCarpetaEmpleado(codigo);
        File archivo = new File(carpeta, "empleado.emp");
        return carpeta.exists() && archivo.exists();
    }
 
    private void escribirTextoFijo(RandomAccessFile raf, String texto, int longitud) throws IOException {
        if (texto.length() > longitud) {
            texto = texto.substring(0, longitud);
        } else {
            StringBuilder textoCompletado = new StringBuilder(texto);
            while (textoCompletado.length() < longitud) {
                textoCompletado.append(' ');
            }
            texto = textoCompletado.toString();
        }
        raf.writeChars(texto);
    }
 
    private String leerTextoFijo(RandomAccessFile raf, int longitud) throws IOException {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            char caracter = raf.readChar();
            texto.append(caracter);
        }
        return texto.toString().trim();
    }
 
 
    public void guardarEmpleado(Empleado emp) throws IOException {
        File carpeta = getCarpetaEmpleado(emp.getCodigo());
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
 
        File archivo = new File(carpeta, "empleado.emp");
        RandomAccessFile raf = new RandomAccessFile(archivo, "rw");
 
        raf.writeInt(emp.getCodigo());
        escribirTextoFijo(raf, emp.getNombre(), LONGITUD_NOMBRE);
        raf.writeDouble(emp.getSalarioBase());
        raf.writeInt(emp.getDiaContratacion());
        raf.writeInt(emp.getMesContratacion());
        raf.writeInt(emp.getAnioContratacion());
        raf.writeBoolean(emp.isActivo());
 
        raf.close();
    }
 
    public Empleado leerEmpleado(int codigo) {
        if (!existeEmpleado(codigo)) {
            return null;
        }
 
        Empleado emp = null;
 
        try {
            File archivo = new File(getCarpetaEmpleado(codigo), "empleado.emp");
            RandomAccessFile raf = new RandomAccessFile(archivo, "r");
 
            raf.seek(0);
            int codigoLeido = raf.readInt();
            String nombre = leerTextoFijo(raf, LONGITUD_NOMBRE);
            double salario = raf.readDouble();
            int dia = raf.readInt();
            int mes = raf.readInt();
            int anio = raf.readInt();
            boolean activo = raf.readBoolean();
 
            raf.close();
 
            emp = new Empleado(codigoLeido, nombre, salario, dia, mes, anio, activo);
 
        } catch (IOException e) {
            System.out.println("Error al leer el empleado: " + e.getMessage());
        }
 
        return emp;
    }
 
 
    private RandomAccessFile abrirArchivoVentas(int codigo, int anio) throws IOException {
        File carpeta = getCarpetaEmpleado(codigo);
        File archivoVentas = new File(carpeta, "ventas" + anio + ".emp");
 
        boolean esArchivoNuevo = !archivoVentas.exists();
 
        RandomAccessFile raf = new RandomAccessFile(archivoVentas, "rw");
 
        if (esArchivoNuevo) {
            for (int mes = 1; mes <= 12; mes++) {
                raf.writeDouble(0.0);
                raf.writeBoolean(false);
            }
        }
 
        return raf;
    }
 
    public void registrarVentaMes(int codigo, int anio, int mes, double monto) {
        try {
            RandomAccessFile raf = abrirArchivoVentas(codigo, anio);
            long posicion = (mes - 1) * TAM_REGISTRO_VENTA;
            raf.seek(posicion);
            raf.writeDouble(monto);
            raf.close();
        } catch (IOException e) {
            System.out.println("Error al registrar la venta: " + e.getMessage());
        }
    }
 
    public void addSaleToEmployee(int codigo, double monto) {
        if (!existeEmpleado(codigo)) {
            System.out.println("El empleado no existe.");
            return;
        }
 
        try {
            LocalDate hoy = LocalDate.now();
            int anioActual = hoy.getYear();
            int mesActual = hoy.getMonthValue();
 
            RandomAccessFile raf = abrirArchivoVentas(codigo, anioActual);
 
            long posicion = (mesActual - 1) * TAM_REGISTRO_VENTA;
 
            raf.seek(posicion);
            double ventaActual = raf.readDouble();
 
            raf.seek(posicion);
            raf.writeDouble(ventaActual + monto);
 
            raf.close();
 
            System.out.println("Venta registrada correctamente.");
 
        } catch (IOException e) {
            System.out.println("Error al registrar la venta: " + e.getMessage());
        }
    }
 
 
    public RandomAccessFile billsFileFor(int code) throws IOException {
        File carpetaEmpleado = getCarpetaEmpleado(code);
 
        if (!carpetaEmpleado.exists()) {
            return null;
        }
 
        File recibos = new File(carpetaEmpleado, "recibos.emp");
 
        if (!recibos.exists()) {
            recibos.createNewFile();
        }
 
        return new RandomAccessFile(recibos, "rw");
    }
 
 
    public boolean isEmployeePayed(int codigo) {
        boolean pagado = false;
 
        try {
            LocalDate hoy = LocalDate.now();
            int anioActual = hoy.getYear();
            int mesActual = hoy.getMonthValue();
 
            RandomAccessFile raf = abrirArchivoVentas(codigo, anioActual);
 
            long posicion = (mesActual - 1) * TAM_REGISTRO_VENTA + 8;
 
            raf.seek(posicion);
            pagado = raf.readBoolean();
 
            raf.close();
 
        } catch (IOException e) {
            System.out.println("Error al verificar el pago: " + e.getMessage());
        }
 
        return pagado;
    }
 
 
    public void payEmployee(int codigo) {
        try {
            if (!existeEmpleado(codigo)) {
                System.out.println("No se pudo pagar");
                return;
            }
 
            Empleado emp = leerEmpleado(codigo);
 
            if (emp == null || !emp.isActivo()) {
                System.out.println("No se pudo pagar");
                return;
            }
 
            if (isEmployeePayed(codigo)) {
                System.out.println("No se pudo pagar");
                return;
            }
 
            LocalDate hoy = LocalDate.now();
            int anioActual = hoy.getYear();
            int mesActual = hoy.getMonthValue();
 
            RandomAccessFile rafVentas = abrirArchivoVentas(codigo, anioActual);
            long posicionVenta = (mesActual - 1) * TAM_REGISTRO_VENTA;
 
            rafVentas.seek(posicionVenta);
            double ventasMes = rafVentas.readDouble();
 
            double sueldo = emp.getSalarioBase() + (ventasMes * 0.10);
            double deduccion = sueldo * 0.035;
            double totalPagar = sueldo - deduccion;
 
            rafVentas.seek(posicionVenta + 8);
            rafVentas.writeBoolean(true);
            rafVentas.close();
 
            RandomAccessFile rafRecibos = billsFileFor(codigo);
            rafRecibos.seek(rafRecibos.length());
 
            rafRecibos.writeInt(hoy.getDayOfMonth());
            rafRecibos.writeInt(hoy.getMonthValue());
            rafRecibos.writeInt(hoy.getYear());
            rafRecibos.writeDouble(sueldo);
            rafRecibos.writeDouble(deduccion);
            rafRecibos.writeInt(anioActual);
            rafRecibos.writeInt(mesActual);
 
            rafRecibos.close();
 
            System.out.printf("Empleado %s se le pago Lps. %.2f%n", emp.getNombre(), totalPagar);
 
        } catch (IOException e) {
            System.out.println("Error al pagar al empleado: " + e.getMessage());
        }
    }
 
 
    public void printEmployee(int codigo) {
        Empleado emp = leerEmpleado(codigo);
 
        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }
 
        System.out.println("\n--------- DATOS DEL EMPLEADO ---------");
        System.out.println("Codigo: " + emp.getCodigo());
        System.out.println("Nombre: " + emp.getNombre());
        System.out.println("Salario base: Lps. " + emp.getSalarioBase());
        System.out.println("Fecha de contratacion: " + emp.getDiaContratacion() + "/"
                + emp.getMesContratacion() + "/" + emp.getAnioContratacion());
        System.out.println("Estado: " + (emp.isActivo() ? "Activo" : "Inactivo"));
 
        try {
            int anioActual = LocalDate.now().getYear();
            RandomAccessFile rafVentas = abrirArchivoVentas(codigo, anioActual);
 
            System.out.println("\n--------- VENTAS DEL ANIO " + anioActual + " ---------");
            double totalVentas = 0.0;
 
            for (int mes = 1; mes <= 12; mes++) {
                rafVentas.seek((mes - 1) * TAM_REGISTRO_VENTA);
                double venta = rafVentas.readDouble();
                totalVentas += venta;
                System.out.printf("Mes %2d: Lps. %.2f%n", mes, venta);
            }
 
            rafVentas.close();
 
            System.out.printf("\nTotal de ventas del anio: Lps. %.2f%n", totalVentas);
 
            RandomAccessFile rafRecibos = billsFileFor(codigo);
            long cantidadRecibos = rafRecibos.length() / TAM_REGISTRO_RECIBO;
            rafRecibos.close();
 
            System.out.println("Total de pagos realizados: " + cantidadRecibos);
 
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
 
 
    public void buscarEmpleado(int codigo) {
        Empleado emp = leerEmpleado(codigo);
 
        if (emp == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }
 
        System.out.println("\n--------- EMPLEADO ENCONTRADO ---------");
        System.out.println(emp.toString());
    }
 
    public void listarEmpleados() {
        File carpetaEmpleados = new File(CARPETA_EMPLEADOS);
 
        if (!carpetaEmpleados.exists()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
 
        File[] carpetas = carpetaEmpleados.listFiles(File::isDirectory);
 
        if (carpetas == null || carpetas.length == 0) {
            System.out.println("No hay empleados registrados.");
            return;
        }
 
        System.out.println("\n--------- LISTA DE EMPLEADOS ---------");
 
        for (File carpeta : carpetas) {
            try {
                int codigo = Integer.parseInt(carpeta.getName());
                Empleado emp = leerEmpleado(codigo);
                if (emp != null) {
                    System.out.println(emp.toString());
                }
            } catch (NumberFormatException e) {
            }
        }
    }
 
 
    private int leerEnteroValido(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero entero.");
            }
        }
    }
 
    private int leerEnteroEnRango(Scanner sc, String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEnteroValido(sc, mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("El valor debe estar entre " + minimo + " y " + maximo + ".");
        }
    }
 
    private double leerDoubleValido(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero.");
            }
        }
    }
 
    private int[] leerFechaValida(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            String[] partes = entrada.split("/");
 
            if (partes.length != 3) {
                System.out.println("Formato invalido. Use el formato dd/mm/aaaa, ejemplo: 12/10/2020");
                continue;
            }
 
            try {
                int dia = Integer.parseInt(partes[0].trim());
                int mes = Integer.parseInt(partes[1].trim());
                int anio = Integer.parseInt(partes[2].trim());
 
                if (mes < 1 || mes > 12) {
                    System.out.println("El mes debe estar entre 1 y 12.");
                    continue;
                }
 
                if (dia < 1 || dia > 31) {
                    System.out.println("El dia debe estar entre 1 y 31.");
                    continue;
                }
 
                if (anio < 1900 || anio > 2100) {
                    System.out.println("El anio ingresado no es valido.");
                    continue;
                }
 
                return new int[] { dia, mes, anio };
 
            } catch (NumberFormatException e) {
                System.out.println("Formato invalido. Use solo numeros, ejemplo: 12/10/2020");
            }
        }
    }
 
    public void registrarEmpleado(Scanner sc) {
        try {
            int codigo = leerEnteroValido(sc, "Codigo del empleado: ");
 
            if (existeEmpleado(codigo)) {
                System.out.println("Ya existe un empleado con ese codigo.");
                return;
            }
 
            System.out.print("Nombre completo: ");
            String nombre = sc.nextLine().trim();
 
            double salario = leerDoubleValido(sc, "Salario base: ");
 
            int[] fecha = leerFechaValida(sc, "Fecha de contratacion (dd/mm/aaaa): ");
            int dia = fecha[0];
            int mes = fecha[1];
            int anio = fecha[2];
 
            Empleado emp = new Empleado(codigo, nombre, salario, dia, mes, anio, true);
 
            guardarEmpleado(emp);
 
            int anioActual = LocalDate.now().getYear();
            RandomAccessFile rafVentas = abrirArchivoVentas(codigo, anioActual);
            rafVentas.close();
 
            int cantidadMeses = leerEnteroValido(sc, "Cuantos meses tuvo ventas durante el anio actual? ");
 
            for (int i = 0; i < cantidadMeses; i++) {
                int numeroMes = leerEnteroEnRango(sc, "  Numero del mes (1-12): ", 1, 12);
                double monto = leerDoubleValido(sc, "  Monto vendido: ");
 
                registrarVentaMes(codigo, anioActual, numeroMes, monto);
            }
 
            RandomAccessFile rafRecibos = billsFileFor(codigo);
            rafRecibos.close();
 
            System.out.println("Empleado registrado correctamente.");
 
        } catch (IOException e) {
            System.out.println("Error al registrar el empleado: " + e.getMessage());
        }
    }
}