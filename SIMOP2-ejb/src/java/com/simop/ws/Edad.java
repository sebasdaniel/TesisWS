/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author 53B45
 */
public class Edad {
    
    public static Integer calcularEdad(String fecha) {
        
        Date fechaNac;
        
        try {
            /**
             * Se puede cambiar la mascara por el formato de la fecha que se
             * quiera recibir, por ejemplo año mes día "yyyy-MM-dd" en este caso
             * es día mes año
             */
            fechaNac = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            
        } catch (ParseException ex) {
            System.out.println("Error:" + ex);
            return -1;
        }
        
        Calendar fechaNacimiento = Calendar.getInstance();
        
        //Se crea un objeto con la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        
        //Se asigna la fecha recibida a la fecha de nacimiento.
        fechaNacimiento.setTime(fechaNac);
        
        //Se restan la fecha actual y la fecha de nacimiento
        int año = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
        
        //Se ajusta el año dependiendo el mes y el día
        if (mes < 0 || (mes == 0 && dia < 0)) {
            año--;
        }
        
        //Regresa la edad en base a la fecha de nacimiento
        return año;
    }
}
