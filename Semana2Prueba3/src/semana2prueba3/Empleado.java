/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana2prueba3;

/**
 *
 * @author alira
 */
public class Empleado {
 
    private int codigo;
    private String nombre;
    private double salarioBase;
    private int diaContratacion;
    private int mesContratacion;
    private int anioContratacion;
    private boolean activo;
 
    public Empleado() {
    }
 
    public Empleado(int codigo, String nombre, double salarioBase,
                     int diaContratacion, int mesContratacion, int anioContratacion,
                     boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.salarioBase = salarioBase;
        this.diaContratacion = diaContratacion;
        this.mesContratacion = mesContratacion;
        this.anioContratacion = anioContratacion;
        this.activo = activo;
    }
 
    public int getCodigo() {
        return codigo;
    }
 
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public double getSalarioBase() {
        return salarioBase;
    }
 
    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }
 
    public int getDiaContratacion() {
        return diaContratacion;
    }
 
    public void setDiaContratacion(int diaContratacion) {
        this.diaContratacion = diaContratacion;
    }
 
    public int getMesContratacion() {
        return mesContratacion;
    }
 
    public void setMesContratacion(int mesContratacion) {
        this.mesContratacion = mesContratacion;
    }
 
    public int getAnioContratacion() {
        return anioContratacion;
    }
 
    public void setAnioContratacion(int anioContratacion) {
        this.anioContratacion = anioContratacion;
    }
 
    public boolean isActivo() {
        return activo;
    }
 
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
 
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Nombre: " + nombre
                + " | Salario base: Lps. " + salarioBase
                + " | Fecha de contratacion: " + diaContratacion + "/" + mesContratacion + "/" + anioContratacion
                + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}